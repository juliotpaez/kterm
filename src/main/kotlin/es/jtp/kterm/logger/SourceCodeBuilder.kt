package es.jtp.kterm.logger

import es.jtp.kterm.*
import es.jtp.kterm.utils.*
import kotlin.math.*

/**
 * A source code for logs.
 */
internal data class SourceCode(val content: String, val filename: String?, val title: String?, val fromIndex: Position,
        val toIndex: Position, val message: String?, val printer: SourceCodePrinters, val inlineMessage: Boolean,
        val isCursorLikeMessage: Boolean, val showNewlineChars: Boolean) {

    val contentLineSequence: Sequence<String>

    init {
        val lineSequence = content.lineSequence()
        val count = lineSequence.count()

        contentLineSequence = lineSequence.mapIndexed { index, s ->
            if (index == count - 1) {
                s
            } else {
                if (showNewlineChars) {
                    "$s↩"
                } else {
                    "$s "
                }
            }
        }
    }

    /**
     * Gets the source code as a string formatted to be written into an ANSI interpreter.
     */
    internal fun toUnixString(sb: StringBuilder, logger: LoggerBuilder, indent: Indent) {
        // File position
        if (filename != null) {
            sb.append(indent.textIndent)
            sb.deleteCharAt(sb.lastIndex)
            sb.append(logger.level.color.boldAndColorText("-->"))
            sb.append(' ')

            if (isOneChar()) {
                sb.append("$filename:${fromIndex.row}:${fromIndex.column}\n")
            } else {
                sb.append("${AnsiColor.boldText("from")}: $filename:${fromIndex.row}:${fromIndex.column}\n")

                sb.append(indent.textIndent)
                sb.deleteCharAt(sb.lastIndex)
                sb.append(logger.level.color.boldAndColorText("-->"))
                sb.append("   ${AnsiColor.boldText("to")}: $filename:${toIndex.row}:${toIndex.column}\n")
            }
        }

        // Get the printer.
        val printer = printer.printer

        // Filter by line count.
        if (isCursorLikeMessage) {
            printer.logCursorLikeLine(this, sb, logger, indent)
        } else {
            val lines = countLines()
            val newIndent = Indent(indent.textIndent, ceil(log10(lines + fromIndex.row + 0.0)).toInt())
            when {

                lines == 1 -> printer.logOneLine(this, sb, logger, newIndent)
                lines <= 10 -> printer.logLess10Multiline(this, sb, logger, newIndent)
                else -> printer.logMore10Multiline(this, sb, logger, newIndent)
            }
        }
    }

    private fun isOneChar() = fromIndex.row == toIndex.row && fromIndex.column == toIndex.column + 1

    private fun countLines() = toIndex.row - fromIndex.row + 1
}

/**
 * A source code builder for logs.
 */
class SourceCodeBuilder(private val content: String, private val filename: String?) {
    private var title: String? = null
    private var fromRowColumn: Position? = null
    private var toRowColumn: Position? = null
    private var message: String? = null
    private var printer = SourceCodePrinters.Coloring
    private var inlineMessage = true
    private var isCursorLikeMessage = false
    private var showNewlineChars = false

    /**
     * Sets a title for the source code block.
     */
    fun title(title: String) {
        this.title = title
    }

    /**
     * Uses the erasing printer instead of the coloring one.
     */
    fun useErasingPrinter() {
        printer = SourceCodePrinters.Erasing
    }

    /**
     * Prints the message separated at the bottom instead of inline with the code.
     */
    fun printMessageAtBottom() {
        inlineMessage = false
    }

    /**
     * Prints the newline characters in the code.
     */
    fun showNewlineChars() {
        showNewlineChars = true
    }

    /**
     * Sets a cursor position at the specified position of the content.
     */
    fun highlightCursorAt(position: Int) {
        if (position < 0) {
            throw KTermException("The 'position' parameter can't be lower than 0.")
        }
        if (position > content.length) {
            throw KTermException("The 'position' parameter can't be greater than the contentLineSequence length.")
        }

        fromRowColumn = indexToRowColumn(position)
        toRowColumn = fromRowColumn
        isCursorLikeMessage = true
    }

    /**
     * Sets the character of the content to highlight.
     */
    fun highlightSection(position: Int) = highlightSection(position, position)

    /**
     * Sets the section of the content to highlight.
     */
    fun highlightSection(from: Int, to: Int) {
        if (to >= content.length) {
            throw KTermException("The 'to' parameter can't be greater or equal than the contentLineSequence length.")
        }
        if (from < 0) {
            throw KTermException("The 'fromRowColumn' parameter can't be lower than 0.")
        }
        if (to < from) {
            throw KTermException("The 'to' parameter must be greater than the 'fromRowColumn' parameter.")
        }

        fromRowColumn = indexToRowColumn(from)
        toRowColumn = indexToRowColumn(to)
        isCursorLikeMessage = false
    }

    /**
     * Adds a custom message to the source code message.
     */
    fun message(message: String) {
        this.message = message
    }

    /**
     * Creates a new [SourceCode] from this builder.
     */
    internal fun toSourceCode(): SourceCode {
        if (fromRowColumn == null) {
            throw KTermException("The source code requires at least a position to highlight")
        }

        return SourceCode(content, filename, title, fromRowColumn!!, toRowColumn!!, message, printer, inlineMessage,
                isCursorLikeMessage, showNewlineChars)
    }

    private fun checkRowColumn(row: Int, column: Int) {
        if (row < 1) {
            throw KTermException("The 'row' parameter can't be lower than 1")
        }
        if (column < 1) {
            throw KTermException("The 'column' parameter can't be lower than 1")
        }

        var currentRow = 1
        var currentColumn = 1

        for (i in 0 until content.length) {
            if (currentRow == row && currentColumn == column) {
                return
            }

            if (currentRow > row) {
                break
            }

            val ch = content[i]
            when (ch) {
                '\n' -> {
                    currentRow += 1
                    currentColumn = 1
                }
                '\r' -> {
                    if (i + 1 <= content.lastIndex && content[i + 1] == '\n') {
                        currentColumn += 1
                    } else {
                        currentRow += 1
                        currentColumn = 1
                    }
                }
                else -> {
                    currentColumn += 1
                }
            }
        }

        throw KTermException("The row-column pair ($row, $column) is not correct for the specified content: $content")
    }

    private fun indexToRowColumn(index: Int): Position {
        if (index > content.length) {
            throw KTermException("The index is not in the range [0, ${content.length})")
        }

        var row = 1
        var column = 1
        var lastWasCR = false

        for (i in 0 until index) {
            val ch = content[i]
            when (ch) {
                '\n' -> {
                    if (lastWasCR) {
                        row -= 1
                    }

                    row += 1
                    column = 1
                    lastWasCR = false
                }
                '\r' -> {
                    row += 1
                    column = 1
                    lastWasCR = true
                }
                else -> {
                    column += 1
                    lastWasCR = false
                }
            }
        }

        return Position(index, row, column)
    }
}
