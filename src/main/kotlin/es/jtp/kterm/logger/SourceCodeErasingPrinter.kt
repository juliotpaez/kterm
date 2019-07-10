package es.jtp.kterm.logger

import es.jtp.kterm.*
import es.jtp.kterm.utils.*

internal object SourceCodeErasingPrinter : SourceCodePrinter {
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
        var text = currentRow.toString()
        innerIndent += text.length
        sb.append(logger.level.color.boldAndColorText(text))
        sb.append(' ')
        sb.append("  $line\n")

        // Symbol line.
        sb.append(indent.indent)
        sb.append(logger.level.color.boldAndColorText("└─>"))
        sb.append(" ".repeat(source.fromIndex.second + 2))

        val length = source.toIndex.second - source.fromIndex.second + 1
        text = when (length) {
            1 -> "^"
            else -> "─".repeat(length)
        }

        when {
            source.message.isNullOrBlank() -> sb.append(logger.level.color.boldAndColorText(text))
            else -> {
                sb.append(logger.level.color.boldAndColorText(text))
                sb.append(' ')
                sb.append(AnsiColor.boldText(
                        indentText(source.message, indent.getLength() + source.fromIndex.second + 4 + length)))
            }
        }

        sb.append('\n')
    }

    /**
     * Prints multiline messages with less than 10 lines.
     */
    override fun logLess10Multiline(source: SourceCode, sb: StringBuilder, logger: LoggerBuilder, indent: Indent) {
        val maxNumDigits = Math.ceil(Math.log10(source.toIndex.first + 0.0)).toInt()

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
                        sb.append(logger.level.color.boldAndColorText("·".repeat(source.fromIndex.second - 1)))
                    }
                    sb.append("${line.substring(source.fromIndex.second - 1)}\n")
                }
                source.toIndex.first -> {
                    sb.append(SourceCodePrinter.getString(0, source.toIndex.second, line))

                    val index = line.length - source.toIndex.second + 1
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
        if (source.message.isNullOrBlank()) {
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
            sb.append(AnsiColor.boldText(indentText(source.message, indent.getLength() + 2)))
            sb.append('\n')
        }
    }

    /**
     * Prints multiline messages with more than 10 lines.
     */
    override fun logMore10Multiline(source: SourceCode, sb: StringBuilder, logger: LoggerBuilder, indent: Indent) {
        val maxNumDigits = Math.ceil(Math.log10(source.toIndex.first + 0.0)).toInt()

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
                    sb.append(logger.level.color.boldAndColorText("·".repeat(source.fromIndex.second - 1)))
                }
                sb.append("${SourceCodePrinter.getString(source.fromIndex.second - 1, line.length, line)}\n")
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
                sb.append(SourceCodePrinter.getString(0, source.toIndex.second, line))

                val index = line.length - source.toIndex.second + 1
                if (index > 0) {
                    sb.append(logger.level.color.boldAndColorText("·".repeat(index - 1)))
                }
                sb.append('\n')
            } else {
                sb.append("${SourceCodePrinter.getString(0, line.length, line)}\n")
            }

            currentRow += 1
        }

        // Symbol line.
        if (source.message.isNullOrBlank()) {
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
            sb.append(AnsiColor.boldText(indentText(source.message, indent.getLength() + 2)))
            sb.append('\n')
        }
    }
}
