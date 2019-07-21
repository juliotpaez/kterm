package es.jtp.kterm.logger

import es.jtp.kterm.*
import es.jtp.kterm.utils.*
import kotlin.math.*

internal object SourceCodeErasingPrinter : SourceCodePrinter {
    /**
     * Prints cursor-like messages.
     */
    override fun logCursorLikeLine(source: SourceCode, sb: StringBuilder, logger: LoggerBuilder, indent: Indent) {
        val currentRow = source.fromIndex.row
        val line = source.contentLineSequence.elementAt(currentRow - 1)

        // Upper line.
        logUpperLine(source, sb, logger, indent)

        // Code line.
        sb.append(indent.textIndent)
        sb.append(logger.level.color.boldAndColorText("|"))
        sb.append(' ')

        // Print number
        val lineNumStr = currentRow.toString()
        sb.append(logger.level.color.boldAndColorText(lineNumStr))
        sb.append("  ")

        sb.append(line.substring(0, source.fromIndex.column - 1))
        sb.append(logger.level.color.boldAndColorText("·"))
        sb.append(line.substring(source.fromIndex.column - 1, line.length))

        sb.append('\n')

        // Message line.
        logMessageLine(source, sb, logger, indent, lineNumStr.length - 1)
    }

    /**
     * Prints one-line messages.
     */
    override fun logOneLine(source: SourceCode, sb: StringBuilder, logger: LoggerBuilder, indent: Indent) {
        val currentRow = source.fromIndex.row
        val line = source.contentLineSequence.elementAt(currentRow - 1)

        // Upper line.
        logUpperLine(source, sb, logger, indent)

        // Code line.
        sb.append(indent.textIndent)
        sb.append(logger.level.color.boldAndColorText("|"))
        sb.append(' ')

        // Print number
        var lineNumStr = currentRow.toString()
        sb.append(logger.level.color.boldAndColorText(lineNumStr))
        sb.append("  $line\n")

        // Message line.
        val length = source.toIndex.column - source.fromIndex.column + 1

        if (source.message.isNullOrBlank()) {
            sb.append(indent.textIndent)
            sb.append(logger.level.color.boldAndColorText("|"))
            sb.append(" ".repeat(source.fromIndex.column + 3))
            lineNumStr = "─".repeat(length)
            sb.append(logger.level.color.boldAndColorText(lineNumStr))
            sb.append('\n')

            sb.append(indent.textIndent)
            sb.append(logger.level.color.boldAndColorText("└─"))
            sb.append('\n')
        } else {
            if (source.inlineMessage) {
                sb.append(indent.textIndent)
                sb.append(logger.level.color.boldAndColorText("└─>"))
                sb.append(" ".repeat(source.fromIndex.column + 2))

                lineNumStr = "─".repeat(length)

                when {
                    source.message.isNullOrBlank() -> sb.append(logger.level.color.boldAndColorText(lineNumStr))
                    else -> {
                        sb.append(logger.level.color.boldAndColorText(lineNumStr))
                        sb.append(' ')
                        sb.append(AnsiColor.boldText(indentText(source.message,
                                indent.textIndent.length + source.fromIndex.column + 4 + length)))
                    }
                }

                sb.append('\n')
            } else {
                sb.append(indent.textIndent)
                sb.append(logger.level.color.boldAndColorText("|"))
                sb.append(" ".repeat(source.fromIndex.column + 3))
                lineNumStr = "─".repeat(length)
                sb.append(logger.level.color.boldAndColorText(lineNumStr))
                sb.append('\n')

                // Empty line
                sb.append(indent.textIndent)
                sb.append(logger.level.color.boldAndColorText("|"))
                sb.append('\n')

                sb.append(indent.textIndent)
                sb.append(logger.level.color.boldAndColorText("└─>"))
                sb.append("  ")
                sb.append(AnsiColor.boldText(indentText(source.message, indent.textIndent.length + 2)))
                sb.append('\n')
            }
        }
    }

    /**
     * Prints multiline messages with less than 10 lines.
     */
    override fun logLess10Multiline(source: SourceCode, sb: StringBuilder, logger: LoggerBuilder, indent: Indent) {
        val maxNumDigits = ceil(log10(source.toIndex.row + 0.0)).toInt()

        // Upper line.
        logUpperLine(source, sb, logger, indent)

        // Content lines.
        var currentRow = source.fromIndex.row
        val lineSequence = source.contentLineSequence.drop(currentRow - 1).take(source.toIndex.row - currentRow + 1)
        for (line in lineSequence) {
            // Code line.
            sb.append(indent.textIndent)
            sb.append(logger.level.color.boldAndColorText("|"))
            sb.append(' ')
            sb.append(logger.level.color.boldAndColorText(currentRow.toString().padStart(maxNumDigits, ' ')))
            sb.append("  ")

            when (currentRow) {
                source.fromIndex.row -> {
                    sb.append(logger.level.color.boldAndColorText("·".repeat(source.fromIndex.column - 1)))
                    sb.append("${line.substring(source.fromIndex.column - 1)}\n")
                }
                source.toIndex.row -> {
                    sb.append(line.substring(0, source.toIndex.column))
                    val index = line.length - source.toIndex.column
                    sb.append(logger.level.color.boldAndColorText("·".repeat(index)))
                    sb.append('\n')
                }
                else -> {
                    sb.append("$line\n")
                }
            }

            currentRow += 1
        }

        // Message line.
        logMessageLine(source, sb, logger, indent, maxNumDigits - 1)
    }

    /**
     * Prints multiline messages with more than 10 lines.
     */
    override fun logMore10Multiline(source: SourceCode, sb: StringBuilder, logger: LoggerBuilder, indent: Indent) {
        val maxNumDigits = ceil(log10(source.toIndex.row + 0.0)).toInt()

        // Upper line.
        logUpperLine(source, sb, logger, indent)

        // Content upper lines.
        var currentRow = source.fromIndex.row
        var lineSequence = source.contentLineSequence.drop(currentRow - 1).take(5)
        for (line in lineSequence) {
            // Code line.
            sb.append(indent.textIndent)
            sb.append(logger.level.color.boldAndColorText("|"))
            sb.append(' ')
            sb.append(logger.level.color.boldAndColorText(currentRow.toString().padStart(maxNumDigits, ' ')))
            sb.append("  ")

            if (currentRow == source.fromIndex.row) {
                sb.append(logger.level.color.boldAndColorText("·".repeat(source.fromIndex.column - 1)))
                sb.append("${line.substring(source.fromIndex.column - 1, line.length)}\n")
            } else {
                sb.append("$line\n")
            }

            currentRow += 1
        }

        // Dotted line.
        sb.append(indent.textIndent)
        sb.append(logger.level.color.boldAndColorText("·"))
        sb.append("   ")
        sb.append(logger.level.color.boldAndColorText(" ".padStart(maxNumDigits, ' ')))
        sb.append(logger.level.color.boldAndColorText("···"))
        sb.append('\n')

        // Content lower lines.
        currentRow = source.toIndex.row - 4
        lineSequence = source.contentLineSequence.drop(currentRow - 1).take(5)
        for (line in lineSequence) {
            // Code line.
            sb.append(indent.textIndent)
            sb.append(logger.level.color.boldAndColorText("|"))
            sb.append(' ')
            sb.append(logger.level.color.boldAndColorText(currentRow.toString().padStart(maxNumDigits, ' ')))
            sb.append("  ")

            if (currentRow == source.toIndex.row) {
                sb.append(line.substring(0, source.toIndex.column))

                val index = line.length - source.toIndex.column + 1
                sb.append(logger.level.color.boldAndColorText("·".repeat(index - 1)))
                sb.append('\n')
            } else {
                sb.append("${line.substring(0, line.length)}\n")
            }

            currentRow += 1
        }

        // Message line.
        logMessageLine(source, sb, logger, indent, maxNumDigits - 1)
    }

    /**
     * Prints the upper line.
     */
    private fun logUpperLine(source: SourceCode, sb: StringBuilder, logger: LoggerBuilder, indent: Indent) {
        sb.append(indent.textIndent)
        sb.append(logger.level.color.boldAndColorText("┌─"))
        if (source.title != null) {
            sb.append(' ')
            sb.append(logger.level.color.boldAndColorText(indentText(source.title, "${indent.textIndent}|  ")))
        }
        sb.append('\n')
    }

    /**
     * Prints the message line.
     */
    private fun logMessageLine(source: SourceCode, sb: StringBuilder, logger: LoggerBuilder, indent: Indent,
            maxNumDigits: Int) {
        if (source.message.isNullOrBlank()) {
            sb.append(indent.textIndent)
            sb.append(logger.level.color.boldAndColorText("└─"))
            sb.append('\n')
        } else {
            if (source.inlineMessage) {
                sb.append(indent.textIndent)
                sb.append(logger.level.color.boldAndColorText("└─>"))
                sb.append(" ".repeat(maxNumDigits + source.toIndex.column + 1))
                sb.append(logger.level.color.boldAndColorText("^"))
                sb.append(' ')
                sb.append(AnsiColor.boldText(indentText(source.message,
                        indent.textIndent.length + maxNumDigits + source.toIndex.column + 6)))
                sb.append('\n')
            } else {
                // Empty line
                sb.append(indent.textIndent)
                sb.append(logger.level.color.boldAndColorText("|"))
                sb.append('\n')

                sb.append(indent.textIndent)
                sb.append(logger.level.color.boldAndColorText("└─>"))
                sb.append("  ")
                sb.append(AnsiColor.boldText(indentText(source.message, indent.textIndent.length + 5)))
                sb.append('\n')
            }
        }
    }
}
