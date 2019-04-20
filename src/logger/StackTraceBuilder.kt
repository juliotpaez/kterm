package es.jtp.kterm.logger

import es.jtp.kterm.*
import es.jtp.kterm.utils.*

/**
 * A stack message for logs.
 */
internal data class StackTrace(
        val className: String?,
        val methodName: String?,
        val message: String?,
        val filePath: String?,
        val line: Int?,
        val column: Int?
) {
    /**
     * Gets the stack trace as a string formatted to be writen into an ANSI interpreter.
     */
    internal fun toUnixString(sb: StringBuilder, logger: Logger, position: Int, maxPositionDigits: Int, indent: Indent) {
        var innerIndent = indent.getLength()

        sb.append(indent.indent)
        sb.append(indent.timesIndent)

        sb.append(logger.level.color.boldAndColorText("|"))
        sb.append(' ')

        if (logger.withStackNumbers) {
            innerIndent += maxPositionDigits + 3
            sb.append(AnsiColor.boldText("["))
            sb.append(logger.level.color.boldAndColorText(position.toString().padStart(maxPositionDigits, ' ')))
            sb.append(AnsiColor.boldText("]"))
            sb.append(' ')
        }

        if (className != null) {
            innerIndent += className.length + 1
            sb.append(className)
            sb.append('.')
        }

        if (methodName != null) {
            innerIndent += methodName.length
            sb.append(logger.level.color.colorText(methodName))
        }

        if (filePath != null) {
            innerIndent += filePath.length + 2
            sb.append('(')
            sb.append(filePath)

            if (line != null) {
                var text = ":$line"
                innerIndent += text.length
                sb.append(text)

                if (column != null) {
                    text = ":$column"
                    innerIndent += text.length
                    sb.append(text)
                }
            }

            sb.append(')')
        }

        if (message != null) {
            sb.append(' ')
            sb.append(
                    AnsiColor.boldText(
                            indentText(
                                    message, innerIndent
                            )
                    )
            )
        }

        sb.append('\n')
    }
}

/**
 * A stack message builder for logs.
 */
class StackTraceBuilder {
    private var className: String? = null
    private var methodName: String? = null
    private var message: String? = null
    private var filePath: String? = null
    private var line: Int? = null
    private var column: Int? = null

    /**
     * Adds a file path to the log stack level.
     */
    fun location(filePath: String) {
        this.filePath = filePath
        line = null
        column = null
    }

    /**
     * Adds a file path and the line of the executed code to the log stack level.
     */
    fun location(filePath: String, line: Int) {
        if (line < 1) {
            throw KTermException("The 'line' parameter can't be lower than 1")
        }

        this.filePath = filePath
        this.line = line
        column = null
    }

    /**
     * Adds a file path and the line and column of the executed code to the log stack level.
     */
    fun location(filePath: String, line: Int, column: Int) {
        if (line < 1) {
            throw KTermException("The 'line' parameter can't be lower than 1")
        }
        if (column < 1) {
            throw KTermException("The 'column' parameter can't be lower than 1")
        }

        this.filePath = filePath
        this.line = line
        this.column = column
    }

    /**
     * Adds a message to the log stack level.
     */
    fun message(message: String) {
        this.message = message
    }

    /**
     * Adds the method name to the log stack level.
     */
    fun method(methodName: String) {
        this.className = null
        this.methodName = methodName
    }

    /**
     * Adds the method and its class name to the log stack level.
     */
    fun method(className: String, methodName: String) {
        this.className = className
        this.methodName = methodName
    }

    /**
     * Creates a new [StackTrace] from this builder.
     */
    internal fun toStackTrace() = StackTrace(
            className?.stringify(), methodName?.stringify(), message, filePath?.stringify(), line, column
    )
}