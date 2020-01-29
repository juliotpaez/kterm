package es.jtp.kterm

import es.jtp.kterm.logger.*
import es.jtp.kterm.utils.*
import es.jtp.kterm.utils.Indent.indentText
import java.time.*
import java.time.format.*
import kotlin.math.*

/**
 * Custom logs for the compiler.
 */
class Logger constructor(val message: String, exception: Throwable? = null, val level: LogLevel = LogLevel.Debug,
        configFunction: (Logger.() -> Unit)? = null) {

    // PROPERTIES -------------------------------------------------------------

    var showDate = false
    var showThread = false
    var showStackNumbers = false

    var stack: StackLogger? = null
    var cause: Logger? = null
    private var sourceCodes = mutableListOf<SourceCodeLogger>()
    private val notes = mutableListOf<NoteLogger>()

    init {
        if (exception != null) {
            ExceptionTreeMapper.mapException(this, exception)
        }
        configFunction?.invoke(this)
    }

    // CONSTRUCTORS -----------------------------------------------------------

    /**
     * Creates a new [Logger] from a [Throwable].
     */
    constructor(exception: Throwable, level: LogLevel = LogLevel.Debug,
            buildFunction: (Logger.() -> Unit)? = null) : this(
            exception.message ?: exception.cause?.message ?: "Undefined error", exception, level, buildFunction)

    /**
     * Creates a new [Logger] with a message.
     */
    constructor(message: String, level: LogLevel = LogLevel.Debug, buildFunction: (Logger.() -> Unit)? = null) : this(
            message, null, level, buildFunction)

    // METHODS ----------------------------------------------------------------

    /**
     * Gets the source code list.
     */
    fun getSourceCodes() = sourceCodes.toList()

    /**
     * Gets the list of notes and tags.
     */
    fun getNotesAndTags() = notes.toList()

    /**
     * Sets a custom stack to the log.
     */
    fun setStack(configFunction: StackLogger.() -> Unit) {
        stack = StackLogger("", 0)
        configFunction.invoke(stack!!)
    }

    /**
     * Sets another [Logger] as a cause of the current one.
     */
    fun setCause(message: String, configFunction: (Logger.() -> Unit)? = null) {
        cause = Logger(message, level, configFunction)
    }

    /**
     * Adds a custom source code to the log with the file path of the content.
     */
    fun addSourceCode(sourceCodeLogger: SourceCodeLogger, configFunction: SourceCodeLogger.() -> Unit) {
        configFunction(sourceCodeLogger)

        sourceCodeLogger.checkIsOk()
        sourceCodes.add(sourceCodeLogger)
    }

    /**
     * Adds a custom source code to the log with the file path of the content.
     */
    fun addSourceCode(content: String, filename: String? = null, configFunction: SourceCodeLogger.() -> Unit) {
        val config = SourceCodeLogger(content, filename)
        configFunction(config)

        config.checkIsOk()
        sourceCodes.add(config)
    }

    /**
     * Adds a custom note with only a name to the log.
     */
    fun addTag(tag: String) {
        notes.add(NoteLogger(tag.stringify(), null))
    }

    /**
     * Adds a custom note with a tag to the log.
     */
    fun addNote(tag: String, message: String) {
        notes.add(NoteLogger(tag.stringify(), message))
    }

    /**
     * Gets the log message as a string formatted to be writen into an ANSI interpreter.
     */
    fun toUnixString(level: LogLevel): String {
        val sb = StringBuilder()
        var anyHeader = false

        // The message type.
        sb.append(AnsiColor.boldText("["))
        sb.append(level.color.boldAndColorText(level.tag))
        sb.append(AnsiColor.boldText("]"))

        // The time.
        if (showDate) {
            sb.append(level.color.colorText(" at "))
            sb.append(AnsiColor.boldText(dateTimeFormatter.format(LocalDateTime.now())))
            anyHeader = true
        }

        // The thread.
        if (showThread) {
            sb.append(level.color.colorText(" in thread "))
            sb.append(AnsiColor.boldText("\"${Thread.currentThread().name}\""))
            anyHeader = true
        }

        // Main message.
        if (!message.isBlank()) {
            if (anyHeader) {
                sb.append(level.color.colorText(" - "))
                sb.append(AnsiColor.boldText(indentText(message, 5)))
            } else {
                sb.append(' ')
                sb.append(AnsiColor.boldText(indentText(message, level.tag.length + 3)))
            }
        }

        sb.append('\n')

        val fixedIndentText = " ".repeat(3)

        // Stack
        if (stack != null) {
            val initPosition = stack!!.countStackLength()
            val result = stack!!.toUnixString(this, level, initPosition, ceil(log10(initPosition + 0.0)).toInt())
            sb.append(fixedIndentText)
            sb.append(indentText(result.dropLast(1), fixedIndentText))
            sb.append('\n')

            if (sourceCodes.isNotEmpty()) {
                sb.append(fixedIndentText)
                sb.append(level.color.boldAndColorText("|"))
                sb.append('\n')
            }
        }

        // Source codes
        for (sourceCode in sourceCodes) {
            val result = sourceCode.toUnixString(this, level)
            sb.append(fixedIndentText)
            sb.append(indentText(result.dropLast(1), fixedIndentText))
            sb.append('\n')
        }

        // Notes
        for (note in notes) {
            val innerIndent = fixedIndentText.length + 4 + note.tag.length

            sb.append(fixedIndentText)
            sb.append(level.color.boldAndColorText("="))
            sb.append(" ${note.tag}")

            if (note.message != null) {
                sb.append(level.color.boldAndColorText(":"))
                sb.append(' ')
                sb.append(AnsiColor.boldText(indentText(note.message, innerIndent)))
            }

            sb.append('\n')
        }

        // Cause
        if (cause != null) {
            sb.append(level.color.boldAndColorText("^^^^^^^^^^^^^^ CAUSED BY ^^^^^^^^^^^^^^"))
            sb.append('\n')
            sb.append(cause!!.toUnixString(level))
        } else {
            sb.deleteCharAt(sb.length - 1)
        }

        return sb.toString()
    }

    /**
     * Gets the log message as a string.
     */
    fun toString(level: LogLevel): String {
        val sb = StringBuilder()
        var anyHeader = false

        // The message type.
        sb.append("[${level.tag}]")

        // The time.
        if (showDate) {
            sb.append(" at ")
            sb.append(dateTimeFormatter.format(LocalDateTime.now()))
            anyHeader = true
        }

        // The thread.
        if (showThread) {
            sb.append(" in thread \"${Thread.currentThread().name}\"")
            anyHeader = true
        }

        // Main message.
        if (!message.isBlank()) {
            if (anyHeader) {
                sb.append(" - ")
                sb.append(indentText(message, 5))
            } else {
                sb.append(' ')
                sb.append(indentText(message, level.tag.length + 3))
            }
        }

        sb.append('\n')

        val fixedIndentText = " ".repeat(3)

        // Stack
        if (stack != null) {
            val initPosition = stack!!.countStackLength()
            val result = stack!!.toString(this, initPosition, ceil(log10(initPosition + 0.0)).toInt())
            sb.append(fixedIndentText)
            sb.append(indentText(result.dropLast(1), fixedIndentText))
            sb.append('\n')

            if (sourceCodes.isNotEmpty()) {
                sb.append(fixedIndentText)
                sb.append("|\n")
            }
        }

        // Source codes
        for (sourceCode in sourceCodes) {
            val result = sourceCode.toString(this)
            sb.append(fixedIndentText)
            sb.append(indentText(result.dropLast(1), fixedIndentText))
            sb.append('\n')
        }

        // Notes
        for (note in notes) {
            val innerIndent = fixedIndentText.length + 4 + note.tag.length

            sb.append(fixedIndentText)
            sb.append("= ${note.tag}")

            if (note.message != null) {
                sb.append(": ")
                sb.append(indentText(note.message, innerIndent))
            }

            sb.append('\n')
        }

        // Cause
        if (cause != null) {
            sb.append("^^^^^^^^^^^^^^ CAUSED BY ^^^^^^^^^^^^^^\n")
            sb.append(cause!!.toString(level))
        } else {
            sb.deleteCharAt(sb.length - 1)
        }

        return sb.toString()
    }

    /**
     * Logs at level specified at the creation time.
     */
    fun log() = logAs(level)

    /**
     * Logs at debug level.
     */
    fun logAsDebug() = logAs(LogLevel.Debug)

    /**
     * Logs at info level.
     */
    fun logAsInfo() = logAs(LogLevel.Info)

    /**
     * Logs at warn level.
     */
    fun logAsWarn() = logAs(LogLevel.Warn)

    /**
     * Logs at error level.
     */
    fun logAsError() = logAs(LogLevel.Error)

    /**
     * Logs at error level.
     */
    private fun logAs(level: LogLevel) {
        println(toUnixString(level))
    }

    override fun toString(): String {
        return "Logger(message='$message', showDate=$showDate, showThread=$showThread, showStackNumbers=$showStackNumbers, stack=$stack, sourceCodes=$sourceCodes, notes=$notes)"
    }

    // STATIC -----------------------------------------------------------------

    companion object {
        private val dateTimeFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss.SSS")

        // METHODS ------------------------------------------------------------

        /**
         * Logs a message at debug level.
         */
        fun debug(message: String, buildFunction: (Logger.() -> Unit)? = null) =
                Logger(message, LogLevel.Debug, buildFunction).logAsDebug()

        /**
         * Logs an exception at debug level.
         */
        fun debug(exception: Throwable, buildFunction: (Logger.() -> Unit)? = null) =
                Logger(exception, LogLevel.Debug, buildFunction).logAsDebug()

        /**
         * Logs an exception at debug level with a custom message.
         */
        fun debug(message: String, exception: Throwable, buildFunction: (Logger.() -> Unit)? = null) =
                Logger(message, exception, LogLevel.Debug, buildFunction).logAsDebug()

        /**
         * Logs a message at info level.
         */
        fun info(message: String, buildFunction: (Logger.() -> Unit)? = null) =
                Logger(message, LogLevel.Info, buildFunction).logAsInfo()

        /**
         * Logs an exception at info level.
         */
        fun info(exception: Throwable, buildFunction: (Logger.() -> Unit)? = null) =
                Logger(exception, LogLevel.Info, buildFunction).logAsInfo()

        /**
         * Logs an exception at info level with a custom message.
         */
        fun info(message: String, exception: Throwable, buildFunction: (Logger.() -> Unit)? = null) =
                Logger(message, exception, LogLevel.Info, buildFunction).logAsInfo()

        /**
         * Logs a message at warn level.
         */
        fun warn(message: String, buildFunction: (Logger.() -> Unit)? = null) =
                Logger(message, LogLevel.Warn, buildFunction).logAsWarn()

        /**
         * Logs an exception at warn level.
         */
        fun warn(exception: Throwable, buildFunction: (Logger.() -> Unit)? = null) =
                Logger(exception, LogLevel.Warn, buildFunction).logAsWarn()

        /**
         * Logs an exception at warn level with a custom message.
         */
        fun warn(message: String, exception: Throwable, buildFunction: (Logger.() -> Unit)? = null) =
                Logger(message, exception, LogLevel.Warn, buildFunction).logAsWarn()

        /**
         * Logs a message at error level.
         */
        fun error(message: String, buildFunction: (Logger.() -> Unit)? = null) =
                Logger(message, LogLevel.Error, buildFunction).logAsError()

        /**
         * Logs an exception at error level.
         */
        fun error(exception: Throwable, buildFunction: (Logger.() -> Unit)? = null) =
                Logger(exception, LogLevel.Error, buildFunction).logAsError()

        /**
         * Logs an exception at error level with a custom message.
         */
        fun error(message: String, exception: Throwable, buildFunction: (Logger.() -> Unit)? = null) =
                Logger(message, exception, LogLevel.Error, buildFunction).logAsError()
    }
}
