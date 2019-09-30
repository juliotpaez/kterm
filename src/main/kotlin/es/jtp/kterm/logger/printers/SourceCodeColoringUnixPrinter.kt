package es.jtp.kterm.logger.printers

import es.jtp.kterm.*
import es.jtp.kterm.logger.*
import es.jtp.kterm.utils.Indent.indentText
import kotlin.math.*

internal object SourceCodeColoringUnixPrinter : SourceCodePrinter {
    /**
     * Prints cursor-like messages.
     */
    override fun logCursorLikeLine(source: SourceCodeLogger, level: LogLevel, logger: Logger): String {
        val sb = StringBuilder()
        val fromIndex = source.fromRowColumn!!
        val contentLineSequence = source.normalizedContent()

        val currentRow = fromIndex.row
        val line = contentLineSequence.elementAt(currentRow - 1)

        // Upper line.
        var result = logUpperLine(source, level, logger)
        sb.append(result)

        // Code line.
        sb.append(level.color.boldAndColorText("|"))
        sb.append(' ')

        // Print number
        val lineNumStr = currentRow.toString()
        sb.append(level.color.boldAndColorText(lineNumStr))
        sb.append("  ")

        sb.append(line.substring(0, fromIndex.column - 1))
        sb.append(level.color.boldAndColorText("·"))
        sb.append(line.substring(fromIndex.column - 1, line.length))

        sb.append('\n')

        // Message line.
        result = logMessageLine(source, level, logger, lineNumStr.length - 1)
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
        val contentLineSequence = source.normalizedContent()

        val currentRow = fromIndex.row
        val line = contentLineSequence.elementAt(currentRow - 1)

        // Upper line.
        var result = logUpperLine(source, level, logger)
        sb.append(result)

        // Code line.
        sb.append(level.color.boldAndColorText("|"))
        sb.append(' ')

        // Print number
        val lineNumStr = currentRow.toString()
        sb.append(level.color.boldAndColorText(lineNumStr))
        sb.append("  ")
        sb.append(line.substring(0, fromIndex.column - 1))
        sb.append(level.color.boldAndColorText(line.substring(fromIndex.column - 1, toIndex.column)))
        sb.append(line.substring(toIndex.column, line.length))
        sb.append('\n')

        // Message line.
        result = logMessageLine(source, level, logger, lineNumStr.length - 1)
        sb.append(result)

        return sb.toString()
    }

    /**
     * Prints multiline messages with less than 10 lines.
     */
    override fun logLess10Multiline(source: SourceCodeLogger, level: LogLevel, logger: Logger): String {
        val sb = StringBuilder()
        val fromIndex = source.fromRowColumn!!
        val toIndex = source.toRowColumn!!
        val contentLineSequence = source.normalizedContent()

        val maxNumDigits = ceil(log10(toIndex.row + 0.0)).toInt()

        // Upper line.
        var result = logUpperLine(source, level, logger)
        sb.append(result)

        // Content lines.
        var currentRow = fromIndex.row
        val lineSequence = contentLineSequence.drop(currentRow - 1).take(toIndex.row - currentRow + 1)
        for (line in lineSequence) {
            // Code line.
            sb.append(level.color.boldAndColorText("|"))
            sb.append(' ')
            sb.append(level.color.boldAndColorText(currentRow.toString().padStart(maxNumDigits, ' ')))
            sb.append("  ")

            when (currentRow) {
                fromIndex.row -> {
                    sb.append(line.substring(0, fromIndex.column - 1))
                    sb.append(level.color.boldAndColorText(line.substring(fromIndex.column - 1, line.length)))
                    sb.append('\n')
                }
                toIndex.row -> {
                    val text = line.substring(0, toIndex.column)
                    sb.append(level.color.boldAndColorText(text))
                    sb.append(line.substring(toIndex.column, line.length))
                    sb.append('\n')
                }
                else -> {
                    sb.append("${level.color.boldAndColorText(line)}\n")
                }
            }

            currentRow += 1
        }

        // Message line.
        result = logMessageLine(source, level, logger, maxNumDigits - 1)
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
        val contentLineSequence = source.normalizedContent()

        val maxNumDigits = ceil(log10(toIndex.row + 0.0)).toInt()

        // Upper line.
        var result = logUpperLine(source, level, logger)
        sb.append(result)

        // Content upper lines.
        var currentRow = fromIndex.row
        var lineSequence = contentLineSequence.drop(currentRow - 1).take(5)
        for (line in lineSequence) {
            // Code line.
            sb.append(level.color.boldAndColorText("|"))
            sb.append(' ')
            sb.append(level.color.boldAndColorText(currentRow.toString().padStart(maxNumDigits, ' ')))
            sb.append("  ")

            if (currentRow == fromIndex.row) {
                sb.append(line.substring(0, fromIndex.column - 1))
                sb.append(level.color.boldAndColorText(line.substring(fromIndex.column - 1, line.length)))
                sb.append('\n')
            } else {
                sb.append(level.color.boldAndColorText(line))
                sb.append('\n')
            }

            currentRow += 1
        }

        // Dotted line.
        sb.append(level.color.boldAndColorText("·"))
        sb.append("   ")
        sb.append(level.color.boldAndColorText(" ".padStart(maxNumDigits, ' ')))
        sb.append(level.color.boldAndColorText("···"))
        sb.append('\n')

        // Content lower lines.
        currentRow = toIndex.row - 4
        lineSequence = contentLineSequence.drop(currentRow - 1).take(5)
        for (line in lineSequence) {
            // Code line.
            sb.append(level.color.boldAndColorText("|"))
            sb.append(' ')
            sb.append(level.color.boldAndColorText(currentRow.toString().padStart(maxNumDigits, ' ')))
            sb.append("  ")

            if (currentRow == toIndex.row) {
                val text = line.substring(0, toIndex.column)
                sb.append(level.color.boldAndColorText(text))
                sb.append(line.substring(toIndex.column, line.length))
                sb.append('\n')
            } else {
                val text = line.substring(0, line.length)
                sb.append(level.color.boldAndColorText(text))
                sb.append('\n')
            }

            currentRow += 1
        }

        // Message line.
        result = logMessageLine(source, level, logger, maxNumDigits - 1)
        sb.append(result)

        return sb.toString()
    }

    /**
     * Prints the upper line.
     */
    private fun logUpperLine(source: SourceCodeLogger, level: LogLevel, logger: Logger): String {
        val sb = StringBuilder()

        sb.append(level.color.boldAndColorText("┌─"))
        if (source.title != null) {
            sb.append(' ')
            sb.append(level.color.boldAndColorText(indentText(source.title!!, "|  ")))
        }
        sb.append('\n')

        return sb.toString()
    }

    /**
     * Prints the message line.
     */
    private fun logMessageLine(source: SourceCodeLogger, level: LogLevel, logger: Logger, maxNumDigits: Int): String {
        val sb = StringBuilder()
        val toIndex = source.toRowColumn!!

        if (source.message.isNullOrBlank()) {
            sb.append(level.color.boldAndColorText("└─"))
            sb.append('\n')
        } else {
            if (!source.messageAtBottom) {
                sb.append(level.color.boldAndColorText("└─>"))
                sb.append(" ".repeat(maxNumDigits + toIndex.column + 1))
                sb.append(level.color.boldAndColorText("^"))
                sb.append(' ')
                sb.append(AnsiColor.boldText(indentText(source.message!!, maxNumDigits + toIndex.column + 6)))
                sb.append('\n')
            } else {
                // Empty line
                sb.append(level.color.boldAndColorText("|"))
                sb.append('\n')

                sb.append(level.color.boldAndColorText("└─>"))
                sb.append("  ")
                sb.append(AnsiColor.boldText(indentText(source.message!!, 5)))
                sb.append('\n')
            }
        }

        return sb.toString()
    }
}
