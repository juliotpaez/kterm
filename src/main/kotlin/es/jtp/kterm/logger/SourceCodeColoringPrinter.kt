package es.jtp.kterm.logger

import es.jtp.kterm.*
import es.jtp.kterm.utils.*
import kotlin.math.*

internal object SourceCodeColoringPrinter : SourceCodePrinter {
    /**
     * Prints one-line messages.
     */
    override fun logOneLine(source: SourceCode, sb: StringBuilder, logger: LoggerBuilder, indent: Indent) {
        val currentRow = source.fromIndex.first
        val line: String = source.content.lineSequence().elementAt(currentRow - 1)
        var innerIndent = 0

        // Upper line.
        sb.append(indent.indent)
        sb.append(logger.level.color.boldAndColorText("┌─"))
        if (source.title != null) {
            sb.append(' ')
            sb.append(logger.level.color.boldAndColorText(indentText(source.title, "${indent.indent}|  ")))
        }
        sb.append('\n')

        // Code line.
        sb.append(indent.indent)
        sb.append(logger.level.color.boldAndColorText("|"))
        sb.append(' ')
        val text = currentRow.toString()
        innerIndent += text.length
        sb.append(logger.level.color.boldAndColorText(text))
        sb.append("  ")
        sb.append(SourceCodePrinter.getString(0, source.fromIndex.second - 1, line))
        if (source.fromIndex.second - 1 < line.length) {
            sb.append(logger.level.color.boldAndColorText(
                    SourceCodePrinter.getString(source.fromIndex.second - 1, source.toIndex.second, line)))
        }
        if (source.toIndex.second < line.length) {
            sb.append(SourceCodePrinter.getString(source.toIndex.second, line.length, line))
        }
        sb.append('\n')

        // Message line.
        val maxNumDigits = ceil(log10(source.toIndex.first + 0.0)).toInt()
        logMessageLine(source, sb, logger, indent, maxNumDigits)
    }

    /**
     * Prints multiline messages with less than 10 lines.
     */
    override fun logLess10Multiline(source: SourceCode, sb: StringBuilder, logger: LoggerBuilder, indent: Indent) {
        val maxNumDigits = ceil(log10(source.toIndex.first + 0.0)).toInt()

        // Upper line.
        sb.append(indent.indent)
        sb.append(logger.level.color.boldAndColorText("┌─"))
        if (source.title != null) {
            sb.append(' ')
            sb.append(logger.level.color.boldAndColorText(indentText(source.title, "${indent.indent}|  ")))
        }
        sb.append('\n')

        // Content lines.
        var currentRow = source.fromIndex.first
        val lineSequence =
                source.content.lineSequence().drop(currentRow - 1).take(source.toIndex.first - currentRow + 1)
        for (line in lineSequence) {
            // Code line.
            sb.append(indent.indent)
            sb.append(logger.level.color.boldAndColorText("|"))
            sb.append(' ')
            sb.append(logger.level.color.boldAndColorText(currentRow.toString().padStart(maxNumDigits, ' ')))
            sb.append("  ")

            when (currentRow) {
                source.fromIndex.first -> {
                    if (source.fromIndex.second != 0) {
                        sb.append(SourceCodePrinter.getString(0, source.fromIndex.second - 1, line))
                    }
                    sb.append(logger.level.color.boldAndColorText(
                            SourceCodePrinter.getString(source.fromIndex.second - 1, line.length, line)))
                    sb.append('\n')
                }
                source.toIndex.first -> {
                    val text = SourceCodePrinter.getString(0, source.toIndex.second, line)
                    sb.append(logger.level.color.boldAndColorText(text))

                    val index = line.length - source.toIndex.second + 1
                    if (index > 0) {
                        sb.append(SourceCodePrinter.getString(source.toIndex.second, line.length, line))
                    }
                    sb.append('\n')
                }
                else -> {
                    sb.append("${logger.level.color.boldAndColorText(line)}\n")
                }
            }

            currentRow += 1
        }

        // Message line.
        logMessageLine(source, sb, logger, indent, maxNumDigits)
    }

    /**
     * Prints multiline messages with more than 10 lines.
     */
    override fun logMore10Multiline(source: SourceCode, sb: StringBuilder, logger: LoggerBuilder, indent: Indent) {
        val maxNumDigits = ceil(log10(source.toIndex.first + 0.0)).toInt()

        // Upper line.
        sb.append(indent.indent)
        sb.append(logger.level.color.boldAndColorText("┌─"))
        if (source.title != null) {
            sb.append(' ')
            sb.append(logger.level.color.boldAndColorText(indentText(source.title, "${indent.indent}|  ")))
        }
        sb.append('\n')

        // Content upper lines.
        var currentRow = source.fromIndex.first
        var lineSequence = source.content.lineSequence().drop(currentRow).take(5)
        for (line in lineSequence) {
            // Code line.
            sb.append(indent.indent)
            sb.append(logger.level.color.boldAndColorText("|"))
            sb.append(' ')
            sb.append(logger.level.color.boldAndColorText(currentRow.toString().padStart(maxNumDigits, ' ')))
            sb.append("  ")

            if (currentRow == source.fromIndex.first) {
                if (source.fromIndex.second != 0) {
                    sb.append(SourceCodePrinter.getString(0, source.fromIndex.second - 1, line))
                }
                sb.append(logger.level.color.boldAndColorText(
                        SourceCodePrinter.getString(source.fromIndex.second - 1, line.length, line)))
                sb.append('\n')
            } else {
                sb.append("${logger.level.color.boldAndColorText(line)}\n")
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
        currentRow = source.toIndex.first - 4
        lineSequence = source.content.lineSequence().drop(currentRow).take(5)
        for (line in lineSequence) {
            // Code line.
            sb.append(indent.indent)
            sb.append(logger.level.color.boldAndColorText("|"))
            sb.append(' ')
            sb.append(logger.level.color.boldAndColorText(currentRow.toString().padStart(maxNumDigits, ' ')))
            sb.append("  ")

            if (currentRow == source.toIndex.first) {
                val text = SourceCodePrinter.getString(0, source.toIndex.second, line)
                sb.append(logger.level.color.boldAndColorText(text))

                val index = line.length - source.toIndex.second + 1
                if (index > 0) {
                    sb.append(SourceCodePrinter.getString(source.toIndex.second, line.length, line))
                }
                sb.append('\n')
            } else {
                val text = SourceCodePrinter.getString(0, line.length, line)
                sb.append("${logger.level.color.boldAndColorText(text)}\n")
            }

            currentRow += 1
        }

        // Message line.
        logMessageLine(source, sb, logger, indent, maxNumDigits)
    }

    /**
     * Prints the message line.
     */
    private fun logMessageLine(source: SourceCode, sb: StringBuilder, logger: LoggerBuilder, indent: Indent, maxNumDigits:Int) {
        if (source.message.isNullOrBlank()) {
            sb.append(indent.indent)
            sb.append(logger.level.color.boldAndColorText("└─"))
            sb.append('\n')
        } else {
            if(source.inlineMessage) {
                sb.append(indent.indent)
                sb.append(logger.level.color.boldAndColorText("└─>"))
                sb.append(" ".repeat(maxNumDigits + source.toIndex.second))
                sb.append(logger.level.color.boldAndColorText("^"))
                sb.append(' ')
                sb.append(AnsiColor.boldText(indentText(source.message, indent.getLength() + 2)))
                sb.append('\n')
            }
            else {
                // Empty line
                sb.append(indent.indent)
                sb.append(logger.level.color.boldAndColorText("|"))
                sb.append('\n')

                sb.append(indent.indent)
                sb.append(logger.level.color.boldAndColorText("└─>"))
                sb.append(' ')
                sb.append(AnsiColor.boldText(indentText(source.message, indent.getLength() + 2)))
                sb.append('\n')
            }
        }
    }
}
