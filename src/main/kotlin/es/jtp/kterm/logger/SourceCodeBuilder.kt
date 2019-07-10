package es.jtp.kterm.logger

import es.jtp.kterm.*
import es.jtp.kterm.utils.*

/**
 * A source code for logs.
 */
internal data class SourceCode(internal val content: String, internal val filename: String?,
        internal val title: String?, internal val fromIndex: Pair<Int, Int>, internal val toIndex: Pair<Int, Int>,
        internal val message: String?, val printer: SourceCodePrinters) {
    /**
     * Gets the source code as a string formatted to be written into an ANSI interpreter.
     */
    internal fun toUnixString(sb: StringBuilder, logger: LoggerBuilder, indent: Indent) {
        // File position
        if (filename != null) {
            sb.append(indent.indent)
            sb.deleteCharAt(sb.lastIndex)
            sb.append(logger.level.color.boldAndColorText("-->"))
            sb.append(' ')

            if (isOneChar()) {
                sb.append("$filename:${fromIndex.first}:${fromIndex.second}\n")
            } else {
                sb.append("${AnsiColor.boldText("from")}: $filename:${fromIndex.first}:${fromIndex.second}\n")

                sb.append(indent.indent)
                sb.deleteCharAt(sb.lastIndex)
                sb.append(logger.level.color.boldAndColorText("-->"))
                sb.append("   ${AnsiColor.boldText("to")}: $filename:${toIndex.first}:${toIndex.second}\n")
            }
        }

        // Get the printer.
        val printer = printer.printer

        // Filter by line count.
        val lines = countLines()
        indent.times = Math.ceil(Math.log10(lines + fromIndex.first + 0.0)).toInt()
        when {
            lines == 1 -> printer.logOneLine(this, sb, logger, indent)
            lines <= 10 -> printer.logLess10Multiline(this, sb, logger, indent)
            else -> printer.logMore10Multiline(this, sb, logger, indent)
        }
    }

    private fun isOneChar() = fromIndex.first == toIndex.first && fromIndex.second == toIndex.second + 1

    private fun countLines() = toIndex.first - fromIndex.first + 1
}

/**
 * A source code builder for logs.
 */
class SourceCodeBuilder(private val content: String, private val filename: String?) {
    private var title: String? = null
    private var fromRowColumn: Pair<Int, Int>? = null
    private var toRowColumn: Pair<Int, Int>? = null
    private var message: String? = null
    private var printer = SourceCodePrinters.Coloring

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
     * Sets a position of the content to highlight.
     */
    fun highlightAt(position: Int) {
        if (position < 0) {
            throw KTermException("The 'position' parameter can't be lower than 0.")
        }
        if (position > content.length) {
            throw KTermException("The 'position' parameter can't be greater than the content length.")
        }

        fromRowColumn = indexToRowColumn(position)
        toRowColumn = fromRowColumn
    }

    /**
     * Sets a position of the content to highlight.
     * The position is represented as a row-column pair.
     */
    fun highlightAt(row: Int, column: Int) {
        checkRowColumn(row, column)
        fromRowColumn = Pair(row, column)
        toRowColumn = Pair(row, column)
    }

    /**
     * Sets the section of the content to highlight.
     */
    fun highlightSection(from: Int, to: Int) {
        if (to > content.length) {
            throw KTermException("The 'to' parameter can't be greater than the content length.")
        }
        if (from < 0) {
            throw KTermException("The 'fromRowColumn' parameter can't be lower than 0.")
        }
        if (to < from) {
            throw KTermException("The 'to' parameter must be greater than the 'fromRowColumn' parameter.")
        }

        fromRowColumn = indexToRowColumn(from)
        toRowColumn = indexToRowColumn(to)
    }


    /**
     * Sets the section of the content to highlight.
     * Positions are represented as row-column pairs.
     */
    fun highlightSection(rowFrom: Int, columnFrom: Int, rowTo: Int, columnTo: Int) {
        if (rowTo < rowFrom) {
            throw KTermException("The 'rowTo' parameter must be greater or equal than the 'rowFrom' parameter.")
        }
        if (rowFrom == rowTo && columnTo < columnFrom) {
            throw KTermException(
                    "The 'columnTo' parameter must be greater or equal than the 'columnFrom' parameter if both rows are the same.")
        }

        checkRowColumn(rowFrom, columnFrom)
        checkRowColumn(rowTo, columnTo)
        fromRowColumn = Pair(rowFrom, columnFrom)
        toRowColumn = Pair(rowTo, columnTo)
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

        return SourceCode(content, filename, title, fromRowColumn!!, toRowColumn!!, message, printer)
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

    private fun indexToRowColumn(index: Int): Pair<Int, Int> {
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

        return Pair(row, column)
    }
}