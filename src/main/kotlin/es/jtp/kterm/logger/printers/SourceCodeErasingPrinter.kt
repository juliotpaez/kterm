package es.jtp.kterm.logger.printers

import es.jtp.kterm.*
import es.jtp.kterm.logger.*
import es.jtp.kterm.utils.Indent.indentText
import kotlin.math.*

internal object SourceCodeErasingPrinter : SourceCodePrinter {
    /**
     * Prints cursor-like messages.
     */
    override fun logCursorLikeLine(source: SourceCodeLogger, level: LogLevel, logger: Logger): String {
        val sb = StringBuilder()
        val fromIndex = source.fromRowColumn!!
        val currentRow = fromIndex.row
        val contentLineSequence = source.normalizedContent()
        val line = contentLineSequence.elementAt(currentRow - 1)

        // Upper line.
        var result = logUpperLine(source, logger)
        sb.append(result)

        // Code line.
        sb.append("| ")

        // Print number
        val lineNumStr = currentRow.toString()
        sb.append(lineNumStr)
        sb.append("  ")

        sb.append(line.substring(0, fromIndex.column - 1))
        sb.append("·")
        sb.append(line.substring(fromIndex.column - 1, line.length))

        sb.append('\n')

        // Message line.
        result = logMessageLine(source, logger, lineNumStr.length - 1)
        sb.append(result)

        return sb.toString()
    }

    /**
     * Prints one-line messages.
     */
    override fun logOneLine(source: SourceCodeLogger, level: LogLevel, logger: Logger): String {
        val sb = StringBuilder()
        val fromIndex = source.fromRowColumn!!
        val toIndex = source.toRowColumn!!
        val currentRow = fromIndex.row
        val contentLineSequence = source.normalizedContent()
        val line = contentLineSequence.elementAt(currentRow - 1)

        // Upper line.
        val result = logUpperLine(source, logger)
        sb.append(result)

        // Code line.
        sb.append("| ")

        // Print number
        var lineNumStr = currentRow.toString()
        sb.append(lineNumStr)
        sb.append("  $line\n")

        // Message line.
        val length = toIndex.column - fromIndex.column + 1

        if (source.message.isNullOrBlank()) {
            sb.append("|")
            sb.append(" ".repeat(fromIndex.column + 3))
            lineNumStr = "─".repeat(length)
            sb.append(lineNumStr)
            sb.append('\n')

            sb.append("└─")
            sb.append('\n')
        } else {
            if (!source.messageAtBottom) {
                sb.append("└─>")
                sb.append(" ".repeat(fromIndex.column + 2))

                lineNumStr = "─".repeat(length)

                when {
                    source.message.isNullOrBlank() -> sb.append(lineNumStr)
                    else -> {
                        sb.append(lineNumStr)
                        sb.append(' ')
                        sb.append(indentText(source.message!!, fromIndex.column + 4 + length))
                    }
                }

                sb.append('\n')
            } else {
                sb.append("|")
                sb.append(" ".repeat(fromIndex.column + 3))
                lineNumStr = "─".repeat(length)
                sb.append(lineNumStr)
                sb.append('\n')

                // Empty line
                sb.append("|")
                sb.append('\n')

                sb.append("└─>")
                sb.append("  ")
                sb.append(indentText(source.message!!, 2))
                sb.append('\n')
            }
        }

        return sb.toString()
    }

    /**
     * Prints multiline messages with less than 10 lines.
     */
    override fun logLess10Multiline(source: SourceCodeLogger, level: LogLevel, logger: Logger): String {
        val sb = StringBuilder()
        val fromIndex = source.fromRowColumn!!
        val toIndex = source.toRowColumn!!
        val maxNumDigits = ceil(log10(toIndex.row + 0.0)).toInt()
        val contentLineSequence = source.normalizedContent()

        // Upper line.
        var result = logUpperLine(source, logger)
        sb.append(result)

        // Content lines.
        var currentRow = fromIndex.row
        val lineSequence = contentLineSequence.drop(currentRow - 1).take(toIndex.row - currentRow + 1)
        for (line in lineSequence) {
            // Code line.
            sb.append("| ")
            sb.append(currentRow.toString().padStart(maxNumDigits, ' '))
            sb.append("  ")

            when (currentRow) {
                fromIndex.row -> {
                    sb.append("·".repeat(fromIndex.column - 1))
                    sb.append("${line.substring(fromIndex.column - 1)}\n")
                }
                toIndex.row -> {
                    sb.append(line.substring(0, toIndex.column))
                    val index = line.length - toIndex.column
                    sb.append("·".repeat(index))
                    sb.append('\n')
                }
                else -> {
                    sb.append("$line\n")
                }
            }

            currentRow += 1
        }

        // Message line.
        result = logMessageLine(source, logger, maxNumDigits - 1)
        sb.append(result)

        return sb.toString()
    }

    /**
     * Prints multiline messages with more than 10 lines.
     */
    override fun logMore10Multiline(source: SourceCodeLogger, level: LogLevel, logger: Logger): String {
        val sb = StringBuilder()
        val fromIndex = source.fromRowColumn!!
        val toIndex = source.toRowColumn!!
        val maxNumDigits = ceil(log10(toIndex.row + 0.0)).toInt()
        val contentLineSequence = source.normalizedContent()

        // Upper line.
        var result = logUpperLine(source, logger)
        sb.append(result)

        // Content upper lines.
        var currentRow = fromIndex.row
        var lineSequence = contentLineSequence.drop(currentRow - 1).take(5)
        for (line in lineSequence) {
            // Code line.
            sb.append("|")
            sb.append(' ')
            sb.append(currentRow.toString().padStart(maxNumDigits, ' '))
            sb.append("  ")

            if (currentRow == fromIndex.row) {
                sb.append("·".repeat(fromIndex.column - 1))
                sb.append("${line.substring(fromIndex.column - 1, line.length)}\n")
            } else {
                sb.append("$line\n")
            }

            currentRow += 1
        }

        // Dotted line.
        sb.append("·")
        sb.append("   ")
        sb.append(" ".padStart(maxNumDigits, ' '))
        sb.append("···")
        sb.append('\n')

        // Content lower lines.
        currentRow = toIndex.row - 4
        lineSequence = contentLineSequence.drop(currentRow - 1).take(5)
        for (line in lineSequence) {
            // Code line.
            sb.append("|")
            sb.append(' ')
            sb.append(currentRow.toString().padStart(maxNumDigits, ' '))
            sb.append("  ")

            if (currentRow == toIndex.row) {
                sb.append(line.substring(0, toIndex.column))

                val index = line.length - toIndex.column + 1
                sb.append("·".repeat(index - 1))
                sb.append('\n')
            } else {
                sb.append("${line.substring(0, line.length)}\n")
            }

            currentRow += 1
        }

        // Message line.
        result = logMessageLine(source, logger, maxNumDigits - 1)
        sb.append(result)

        return sb.toString()
    }

    /**
     * Prints the upper line.
     */
    private fun logUpperLine(source: SourceCodeLogger, logger: Logger): String {
        val sb = StringBuilder()

        sb.append("┌─")
        if (source.title != null) {
            sb.append(' ')
            sb.append(indentText(source.title!!, "|  "))
        }
        sb.append('\n')

        return sb.toString()
    }

    /**
     * Prints the message line.
     */
    private fun logMessageLine(source: SourceCodeLogger, logger: Logger, maxNumDigits: Int): String {
        val sb = StringBuilder()
        val toIndex = source.toRowColumn!!

        if (source.message.isNullOrBlank()) {
            sb.append("└─")
            sb.append('\n')
        } else {
            if (!source.messageAtBottom) {
                sb.append("└─>")
                sb.append(" ".repeat(maxNumDigits + toIndex.column + 1))
                sb.append("^ ")
                sb.append(indentText(source.message!!, maxNumDigits + toIndex.column + 6))
                sb.append('\n')
            } else {
                // Empty line
                sb.append("|\n")

                sb.append("└─>")
                sb.append("  ")
                sb.append(indentText(source.message!!, 5))
                sb.append('\n')
            }
        }

        return sb.toString()
    }
}
