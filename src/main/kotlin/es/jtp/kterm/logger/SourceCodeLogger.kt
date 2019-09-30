package es.jtp.kterm.logger

import es.jtp.kterm.*
import es.jtp.kterm.utils.*

/**
 * A source code for logs.
 */
data class SourceCodeLogger(private val content: String, private val filename: String?) {
    internal var fromRowColumn: Position? = null
    internal var toRowColumn: Position? = null
    private var printer = SourceCodePrinters.Coloring
    private var isCursorLikeMessage = false

    /**
     * The message to show.
     */
    var message: String? = null
        set(value) {
            field = value?.stringify()
        }

    /**
     * The title of the source code.
     */
    var title: String? = null
        set(value) {
            field = value?.stringify()
        }

    /**
     * Whether to print the message at the bottom instead of inline with the code.
     */
    var messageAtBottom = false

    /**
     * Whether to show the new line characters (\n, \r or \r\n) as ↩.
     */
    var showNewlineChars = false


    private val isOneChar: Boolean
        get() {
            if (fromRowColumn == null) {
                throw KTermException("The source code requires at least a position to highlight")
            }

            val fromIndex = fromRowColumn!!
            val toIndex = toRowColumn!!

            return fromIndex.row == toIndex.row && fromIndex.column == toIndex.column + 1
        }

    private val countLines: Int
        get() {
            if (fromRowColumn == null) {
                throw KTermException("The source code requires at least a position to highlight")
            }

            val fromIndex = fromRowColumn!!
            val toIndex = toRowColumn!!

            return toIndex.row - fromIndex.row + 1
        }

    // METHODS ----------------------------------------------------------------

    /**
     * Uses the erasing printer.
     */
    fun useErasingPrinter() {
        printer = SourceCodePrinters.Erasing
    }

    /**
     * Uses the coloring printer.
     */
    fun useColoringPrinter() {
        printer = SourceCodePrinters.Coloring
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

        fromRowColumn = Position.fromIndex(position, content)
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

        fromRowColumn = Position.fromIndex(from, content)
        toRowColumn = Position.fromIndex(to, content)
        isCursorLikeMessage = false
    }

    /**
     * Generates a normalized text.
     */
    internal fun normalizedContent(): Sequence<String> {
        var contentLineSequence = content.lineSequence()

        // Normalize content.
        let {
            val count = contentLineSequence.count()

            contentLineSequence = contentLineSequence.mapIndexed { index, s ->
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

        return contentLineSequence
    }

    /**
     * Checks whether the source code is ok or not.
     */
    internal fun checkIsOk() {
        if (fromRowColumn == null) {
            throw KTermException("SourceCodeLogger require something to highlight")
        }
    }

    /**
     * Gets the source code as a string formatted to be written into an ANSI interpreter.
     */
    fun toUnixString(logger: Logger, level: LogLevel): String {
        val sb = StringBuilder()

        val fromIndex = fromRowColumn!!
        val toIndex = toRowColumn!!

        // File position
        if (filename != null) {
            sb.append(level.color.boldAndColorText("-->"))
            sb.append(' ')

            if (isOneChar) {
                sb.append("$filename:${fromIndex.row}:${fromIndex.column}\n")
            } else {
                sb.append("${AnsiColor.boldText("from")}: $filename:${fromIndex.row}:${fromIndex.column}\n")

                sb.append(level.color.boldAndColorText("-->"))
                sb.append("   ${AnsiColor.boldText("to")}: $filename:${toIndex.row}:${toIndex.column}\n")
            }
        }

        // Get the printer.
        val printer = printer.unixPrinter

        // Filter by line count.
        if (isCursorLikeMessage) {
            val result = printer.logCursorLikeLine(this, level, logger)
            sb.append(result)
        } else {
            val lines = countLines
            val result = when {
                lines == 1 -> printer.logOneLine(this, level, logger)
                lines <= 10 -> printer.logLess10Multiline(this, level, logger)
                else -> printer.logMore10Multiline(this, level, logger)
            }
            sb.append(result)
        }

        return sb.toString()
    }

    /**
     * Gets the source code as a string.
     */
    fun toString(logger: Logger): String {
        val sb = StringBuilder()

        val fromIndex = fromRowColumn!!
        val toIndex = toRowColumn!!

        // File position
        if (filename != null) {
            sb.append("--> ")

            if (isOneChar) {
                sb.append("$filename:${fromIndex.row}:${fromIndex.column}\n")
            } else {
                sb.append("from: $filename:${fromIndex.row}:${fromIndex.column}\n")

                sb.append("-->")
                sb.append("   to: $filename:${toIndex.row}:${toIndex.column}\n")
            }
        }

        // Get the printer.
        val printer = printer.normalPrinter

        // Filter by line count.
        if (isCursorLikeMessage) {
            val result = printer.logCursorLikeLine(this, LogLevel.Error, logger)
            sb.append(result)
        } else {
            val lines = countLines
            val result = when {
                lines == 1 -> printer.logOneLine(this, LogLevel.Error, logger)
                lines <= 10 -> printer.logLess10Multiline(this, LogLevel.Error, logger)
                else -> printer.logMore10Multiline(this, LogLevel.Error, logger)
            }
            sb.append(result)
        }

        return sb.toString()
    }

    override fun toString(): String {
        return "SourceCodeLogger(content='$content', filename=$filename, fromRowColumn=$fromRowColumn, toRowColumn=$toRowColumn, printer=$printer, isCursorLikeMessage=$isCursorLikeMessage, message=$message, title=$title, messageAtBottom=$messageAtBottom, showNewlineChars=$showNewlineChars)"
    }
}
