package es.jtp.kterm.logger

import es.jtp.kterm.*
import es.jtp.kterm.utils.*

/**
 * A stack message for logs.
 */
class StackTraceLogger {
    /**
     * The class name where the stack trace is produced.
     */
    var className: String? = null
        set(value) {
            field = value?.stringify()
        }

    /**
     * The method name where the stack trace is produced.
     */
    var methodName: String? = null
        set(value) {
            field = value?.stringify()
        }

    /**
     * The message to show.
     */
    var message: String? = null
        set(value) {
            field = value?.stringify()
        }

    /**
     * The file name where the stack trace is produced.
     */
    val locationFilePath: String

    /**
     * The line of the file where the stack trace is produced.
     */
    val locationLine: Int?

    /**
     * The column of the file where the stack trace is produced.
     */
    val locationColumn: Int?

    // CONSTRUCTORS -----------------------------------------------------------

    /**
     * Creates a new [StackTraceLogger] with a file path to the log stack.
     */
    constructor(filePath: String) {
        this.locationFilePath = filePath.stringify()
        locationLine = null
        locationColumn = null
    }

    /**
     * Creates a new [StackTraceLogger] with a file path and the line of the executed code to the log stack.
     */
    constructor(filePath: String, line: Int) {
        if (line < 1) {
            throw KTermException("The 'line' parameter can't be lower than 1")
        }

        this.locationFilePath = filePath.stringify()
        this.locationLine = line
        locationColumn = null
    }

    /**
     * Creates a new [StackTraceLogger] with a file path and the line and column of the executed code to the log stack.
     */
    constructor(filePath: String, line: Int, column: Int) {
        if (line < 1) {
            throw KTermException("The 'line' parameter can't be lower than 1")
        }
        if (column < 1) {
            throw KTermException("The 'column' parameter can't be lower than 1")
        }

        this.locationFilePath = filePath.stringify()
        this.locationLine = line
        this.locationColumn = column
    }

    // METHODS ----------------------------------------------------------------

    /**
     * Gets the stack trace as a string formatted to be written into an ANSI interpreter.
     *
     * @param position The relative position of the stack trace.
     * @param maxPositionDigits The number of digits of the index of the last stack trace.
     */
    fun toUnixString(level: LogLevel, showStackNumbers: Boolean, position: Int, maxPositionDigits: Int): String {
        val sb = StringBuilder()
        var innerIndent = 0

        sb.append(level.color.boldAndColorText("|"))
        sb.append(' ')

        if (showStackNumbers) {
            innerIndent += maxPositionDigits + 3
            sb.append(AnsiColor.boldText("["))
            sb.append(level.color.boldAndColorText(position.toString().padStart(maxPositionDigits, ' ')))
            sb.append(AnsiColor.boldText("]"))
            sb.append(' ')
        }

        if (className != null) {
            innerIndent += className!!.length + 1
            sb.append(className)
            sb.append('.')
        }

        if (methodName != null) {
            innerIndent += methodName!!.length
            sb.append(level.color.colorText(methodName!!))
        }

        innerIndent += locationFilePath.length + 2
        sb.append('(')
        sb.append(locationFilePath)

        if (locationLine != null) {
            var text = ":$locationLine"
            innerIndent += text.length
            sb.append(text)

            if (locationColumn != null) {
                text = ":$locationColumn"
                innerIndent += text.length
                sb.append(text)
            }
        }

        sb.append(')')

        if (message != null) {
            sb.append(' ')
            sb.append(Indent.indentText(message!!, "${level.color.boldAndColorText("|")}${" ".repeat(innerIndent - 1)}",
                    AnsiColor.Companion::boldText))
        }

        sb.append('\n')

        return sb.toString()
    }

    /**
     * Gets the stack trace as a string.
     *
     * @param position The relative position of the stack trace.
     * @param maxPositionDigits The number of digits of the index of the last stack trace.
     */
    fun toString(showStackNumbers: Boolean, position: Int, maxPositionDigits: Int): String {
        val sb = StringBuilder()
        var innerIndent = 0

        sb.append("| ")

        if (showStackNumbers) {
            innerIndent += maxPositionDigits + 3
            sb.append('[')
            sb.append(position.toString().padStart(maxPositionDigits, ' '))
            sb.append("] ")
        }

        if (className != null) {
            innerIndent += className!!.length + 1
            sb.append(className)
            sb.append('.')
        }

        if (methodName != null) {
            innerIndent += methodName!!.length
            sb.append(methodName!!)
        }

        innerIndent += locationFilePath.length + 2
        sb.append('(')
        sb.append(locationFilePath)

        if (locationLine != null) {
            var text = ":$locationLine"
            innerIndent += text.length
            sb.append(text)

            if (locationColumn != null) {
                text = ":$locationColumn"
                innerIndent += text.length
                sb.append(text)
            }
        }

        sb.append(')')

        if (message != null) {
            sb.append(' ')
            sb.append(Indent.indentText(message!!, "|${" ".repeat(innerIndent - 1)}"))
        }

        sb.append('\n')
        return sb.toString()
    }

    override fun toString(): String {
        return "StackTraceLogger(className=$className, methodName=$methodName, message=$message, locationFilePath=$locationFilePath, locationLine=$locationLine, locationColumn=$locationColumn)"
    }
}
