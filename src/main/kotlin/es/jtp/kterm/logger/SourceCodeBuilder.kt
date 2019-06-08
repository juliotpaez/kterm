package es.jtp.kterm.logger

import es.jtp.kterm.*
import es.jtp.kterm.utils.*

/**
 * A source code for logs.
 */
internal data class SourceCode(val content: String, val filename: String?, val title: String?,
        val fromIndex: Pair<Int, Int>, val toIndex: Pair<Int, Int>, val message: String?) {
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

        // Filter by line count.
        val lines = countLines()
        indent.times = Math.ceil(Math.log10(lines + fromIndex.first + 0.0)).toInt()
        when {
            lines == 1 -> logOneLine(sb, logger, indent)
            lines <= 10 -> logLess10Multiline(sb, logger, indent)
            else -> logMore10Multiline(sb, logger, indent)
        }
    }

    /**
     * Prints one-line messages.
     */
    private fun logOneLine(sb: StringBuilder, logger: LoggerBuilder, indent: Indent) {
        val currentRow = fromIndex.first
        val line: String = content.lineSequence().elementAt(currentRow - 1)
        var innerIndent = 0

        // Upper line.
        sb.append(indent.indent)
        sb.append(logger.level.color.boldAndColorText("┌─"))
        if (title != null) {
            sb.append(' ')
            sb.append(logger.level.color.boldAndColorText(indentText(title, "${indent.indent}|  ")))
        }
        sb.append('\n')

        // Code line.
        sb.append(indent.indent)
        sb.append(logger.level.color.boldAndColorText("|"))
        sb.append(' ')
        var text = currentRow.toString()
        innerIndent += text.length
        sb.append(logger.level.color.boldAndColorText(text))
        sb.append(' ')
        sb.append("  $line\n")

        // Symbol line.
        sb.append(indent.indent)
        sb.append(logger.level.color.boldAndColorText("└─>"))
        sb.append(" ".repeat(fromIndex.second + 2))

        val length = toIndex.second - fromIndex.second + 1
        text = when (length) {
            1 -> "^"
            else -> "─".repeat(length)
        }

        when {
            message.isNullOrBlank() -> sb.append(logger.level.color.boldAndColorText(text))
            else -> {
                sb.append(logger.level.color.boldAndColorText(text))
                sb.append(' ')
                sb.append(AnsiColor.boldText(indentText(message, indent.getLength() + fromIndex.second + 4 + length)))
            }
        }

        sb.append('\n')
    }

    /**
     * Prints multiline messages with less than 10 lines.
     */
    private fun logLess10Multiline(sb: StringBuilder, logger: LoggerBuilder, indent: Indent) {
        val maxNumDigits = Math.ceil(Math.log10(toIndex.first + 0.0)).toInt()

        // Upper line.
        sb.append(indent.indent)
        sb.append(logger.level.color.boldAndColorText("┌─"))
        if (title != null) {
            sb.append(' ')
            sb.append(logger.level.color.boldAndColorText(indentText(title, "${indent.indent}|  ")))
        }
        sb.append('\n')

        // Content lines.
        var currentRow = fromIndex.first
        val lineSequence = content.lineSequence().drop(currentRow - 1).take(toIndex.first - currentRow + 1)
        for (line in lineSequence) {
            // Code line.
            sb.append(indent.indent)
            sb.append(logger.level.color.boldAndColorText("|"))
            sb.append(' ')
            sb.append(logger.level.color.boldAndColorText(currentRow.toString().padStart(maxNumDigits, ' ')))
            sb.append("  ")

            when (currentRow) {
                fromIndex.first -> {
                    if (fromIndex.second != 0) {
                        sb.append(logger.level.color.boldAndColorText("·".repeat(fromIndex.second - 1)))
                    }
                    sb.append("${line.substring(fromIndex.second - 1)}\n")
                }
                toIndex.first -> {
                    sb.append(getString(0, toIndex.second, line))

                    val index = line.length - toIndex.second + 1
                    if (index > 0) {
                        sb.append(logger.level.color.boldAndColorText("·".repeat(index - 1)))
                    }
                    sb.append('\n')
                }
                else -> {
                    sb.append("$line\n")
                }
            }

            currentRow += 1
        }

        // Symbol line.
        if (message.isNullOrBlank()) {
            sb.append(indent.indent)
            sb.append(logger.level.color.boldAndColorText("└─"))
            sb.append('\n')
        } else {
            // Empty line
            sb.append(indent.indent)
            sb.append(logger.level.color.boldAndColorText("|"))
            sb.append('\n')

            sb.append(indent.indent)
            sb.append(logger.level.color.boldAndColorText("└─>"))
            sb.append(' ')
            sb.append(AnsiColor.boldText(indentText(message, indent.getLength() + 2)))
            sb.append('\n')
        }
    }

    /**
     * Prints multiline messages with more than 10 lines.
     */
    private fun logMore10Multiline(sb: StringBuilder, logger: LoggerBuilder, indent: Indent) {
        val maxNumDigits = Math.ceil(Math.log10(toIndex.first + 0.0)).toInt()

        // Upper line.
        sb.append(indent.indent)
        sb.append(logger.level.color.boldAndColorText("┌─"))
        if (title != null) {
            sb.append(' ')
            sb.append(logger.level.color.boldAndColorText(indentText(title, "${indent.indent}|  ")))
        }
        sb.append('\n')

        // Content upper lines.
        var currentRow = fromIndex.first
        var lineSequence = content.lineSequence().drop(currentRow).take(5)
        for (line in lineSequence) {
            // Code line.
            sb.append(indent.indent)
            sb.append(logger.level.color.boldAndColorText("|"))
            sb.append(' ')
            sb.append(logger.level.color.boldAndColorText(currentRow.toString().padStart(maxNumDigits, ' ')))
            sb.append("  ")

            if (currentRow == fromIndex.first) {
                if (fromIndex.second != 0) {
                    sb.append(logger.level.color.boldAndColorText("·".repeat(fromIndex.second - 1)))
                }
                sb.append("${getString(fromIndex.second - 1, line.length, line)}\n")
            } else {
                sb.append("$line\n")
            }

            currentRow += 1
        }

        // Dotted line.
        sb.append(indent.indent)
        sb.append(logger.level.color.boldAndColorText("·"))
        sb.append("   ")
        sb.append(logger.level.color.boldAndColorText(" ".padStart(maxNumDigits, ' ')))
        sb.append(logger.level.color.boldAndColorText("···"))
        sb.append('\n')

        // Content lower lines.
        currentRow = toIndex.first - 4
        lineSequence = content.lineSequence().drop(currentRow).take(5)
        for (line in lineSequence) {
            // Code line.
            sb.append(indent.indent)
            sb.append(logger.level.color.boldAndColorText("|"))
            sb.append(' ')
            sb.append(logger.level.color.boldAndColorText(currentRow.toString().padStart(maxNumDigits, ' ')))
            sb.append("  ")

            if (currentRow == toIndex.first) {
                sb.append(getString(0, toIndex.second, line))

                val index = line.length - toIndex.second + 1
                if (index > 0) {
                    sb.append(logger.level.color.boldAndColorText("·".repeat(index - 1)))
                }
                sb.append('\n')
            } else {
                sb.append("${getString(0, line.length, line)}\n")
            }

            currentRow += 1
        }

        // Symbol line.
        if (message.isNullOrBlank()) {
            sb.append(indent.indent)
            sb.append(logger.level.color.boldAndColorText("└─"))
            sb.append('\n')
        } else {
            // Empty line
            sb.append(indent.indent)
            sb.append(logger.level.color.boldAndColorText("|"))
            sb.append('\n')

            sb.append(indent.indent)
            sb.append(logger.level.color.boldAndColorText("└─>"))
            sb.append(' ')
            sb.append(AnsiColor.boldText(indentText(message, indent.getLength() + 2)))
            sb.append('\n')
        }
    }

    private fun isOneChar() = fromIndex.first == toIndex.first && fromIndex.second == toIndex.second + 1

    private fun countLines() = toIndex.first - fromIndex.first + 1

    private fun getString(from: Int, to: Int, text: String) = when {
        from > text.length -> ""
        to > text.length -> text.substring(from)
        else -> text.substring(from, to)
    }
}

/**
 * A source code builder for logs.
 */
class SourceCodeBuilder(private val content: String, private val filename: String?) {
    private var title: String? = null
    private var fromRowColumn: Pair<Int, Int>? = null
    private var toRowColumn: Pair<Int, Int>? = null
    private var message: String? = null

    /**
     * Sets a title for the source code block.
     */
    fun title(title: String) {
        this.title = title
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

        return SourceCode(content, filename, title, fromRowColumn!!, toRowColumn!!, message)
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