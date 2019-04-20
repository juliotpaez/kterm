package es.jtp.kterm

import es.jtp.kterm.logger.*
import es.jtp.kterm.logger.SourceCode
import es.jtp.kterm.utils.*
import java.time.*
import java.time.format.*

/**
 * Custom logs for the compiler.
 */
class Logger internal constructor(internal val message: String, internal val level: LogLevel) {
    // Generic data.
    private var withDate = false
    private var withThread = false
    internal var withStackNumbers = false

    private var stack: StackLevel? = null
    private var sourceCodes = mutableListOf<SourceCode>()
    private val notes = mutableListOf<LogNote>()

    /**
     * Shows the log date.
     */
    fun showDate() {
        withDate = true
    }

    /**
     * Shows the thread name.
     */
    fun showThread() {
        withThread = true
    }

    /**
     * Shows stack numbers.
     */
    fun showStackExecutionOrder() {
        withStackNumbers = true
    }

    /**
     * Adds a custom stack level to the log.
     */
    fun stack(message: String, builderFunction: StackLevelBuilder.() -> Unit) {
        val builder = StackLevelBuilder(0, message)
        builderFunction(builder)

        stack = builder.toStackLevel()
    }

    /**
     * Adds a custom source code builder to the log.
     */
    fun addSourceCode(content: String, builderFunction: SourceCodeBuilder.() -> Unit) {
        val builder = SourceCodeBuilder(content, null)
        builderFunction(builder)

        sourceCodes.add(builder.toSourceCode())
    }

    /**
     * Adds a custom source code builder to the log with the file path of the content.
     */
    fun addSourceCode(content: String, filename: String?, builderFunction: SourceCodeBuilder.() -> Unit) {
        val builder = SourceCodeBuilder(content, filename)
        builderFunction(builder)

        sourceCodes.add(builder.toSourceCode())
    }

    /**
     * Adds a custom tag to the log.
     */
    fun addTag(tag: String) {
        notes.add(LogNote(tag.stringify(), null))
    }

    /**
     * Adds a custom note with a tag to the log.
     */
    fun addNote(tag: String, message: String) {
        notes.add(LogNote(tag.stringify(), message))
    }

    /**
     * Gets the log message as a string formatted to be writen into an ANSI interpreter.
     */
    internal fun toUnixString(): String {
        val sb = StringBuilder()
        var anyHeader = false

        // The message type.
        sb.append(AnsiColor.boldText("["))
        sb.append(level.color.boldAndColorText(level.tag))
        sb.append(AnsiColor.boldText("]"))

        // The time.
        if (withDate) {
            sb.append(level.color.colorText(" at "))
            sb.append(AnsiColor.boldText(dateTimeFormatter.format(LocalDateTime.now())))
            anyHeader = true
        }

        // The thread.
        if (withThread) {
            sb.append(level.color.colorText(" in thread "))
            sb.append(AnsiColor.boldText("\"${Thread.currentThread().name}\""))
            anyHeader = true
        }

        // Main message.
        if (!message.isBlank()) {
            if (anyHeader) {
                sb.append(level.color.colorText(" - "))
            } else {
                sb.append(' ')
            }

            sb.append(AnsiColor.boldText(indentText(message, 5)))
        }

        sb.append('\n')

        val mainIndentText = " ".repeat(3)

        // Stack
        if (stack != null) {
            val initPosition = stack!!.countStackLength()
            stack!!.toUnixString(sb, this, initPosition, Math.ceil(Math.log10(initPosition + 0.0)).toInt(),
                    Indent(mainIndentText, 0, ""))

            if (sourceCodes.isNotEmpty()) {
                sb.append(mainIndentText)
                sb.append(level.color.boldAndColorText("|"))
                sb.append('\n')
            }
        }

        // Source codes
        for (sourceCode in sourceCodes) {
            sourceCode.toUnixString(sb, this, Indent(mainIndentText, 0, ""))
        }

        // Notes
        for (note in notes) {
            val innerIndent = mainIndentText.length + 4 + note.tag.length

            sb.append(mainIndentText)
            sb.append(level.color.boldAndColorText("="))
            sb.append(" ${note.tag}")

            if (note.message != null) {
                sb.append(level.color.boldAndColorText(":"))
                sb.append(' ')
                sb.append(AnsiColor.boldText(indentText(note.message, innerIndent)))
            }

            sb.append('\n')
        }

        sb.deleteCharAt(sb.length - 1)
        return sb.toString()
    }

    companion object {
        private val dateTimeFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss.SSS")

        /**
         * Logs a message at debug level.
         */
        fun debug(message: String, buildFunction: (Logger.() -> Unit)? = null) = log(Logger(message, LogLevel.Debug),
                buildFunction)

        /**
         * Logs an exception at debug level.
         */
        fun debug(exception: Throwable, buildFunction: (Logger.() -> Unit)? = null) = log(exception, LogLevel.Debug,
                buildFunction)

        /**
         * Logs an exception at debug level with a custom message.
         */
        fun debug(message: String, exception: Throwable, buildFunction: (Logger.() -> Unit)? = null) = log(message,
                exception, LogLevel.Debug, buildFunction)

        /**
         * Logs a message at info level.
         */
        fun info(message: String, buildFunction: (Logger.() -> Unit)? = null) = log(Logger(message, LogLevel.Info),
                buildFunction)

        /**
         * Logs an exception at info level.
         */
        fun info(exception: Throwable, buildFunction: (Logger.() -> Unit)? = null) = log(exception, LogLevel.Info,
                buildFunction)

        /**
         * Logs an exception at info level with a custom message.
         */
        fun info(message: String, exception: Throwable, buildFunction: (Logger.() -> Unit)? = null) = log(message,
                exception, LogLevel.Info, buildFunction)

        /**
         * Logs a message at warn level.
         */
        fun warn(message: String, buildFunction: (Logger.() -> Unit)? = null) = log(Logger(message, LogLevel.Warn),
                buildFunction)

        /**
         * Logs an exception at warn level.
         */
        fun warn(exception: Throwable, buildFunction: (Logger.() -> Unit)? = null) = log(exception, LogLevel.Warn,
                buildFunction)

        /**
         * Logs an exception at warn level with a custom message.
         */
        fun warn(message: String, exception: Throwable, buildFunction: (Logger.() -> Unit)? = null) = log(message,
                exception, LogLevel.Warn, buildFunction)

        /**
         * Logs a message at error level.
         */
        fun error(message: String, buildFunction: (Logger.() -> Unit)? = null) = log(Logger(message, LogLevel.Error),
                buildFunction)

        /**
         * Logs an exception at error level.
         */
        fun error(exception: Throwable, buildFunction: (Logger.() -> Unit)? = null) = log(exception, LogLevel.Error,
                buildFunction)

        /**
         * Logs an exception at error level with a custom message.
         */
        fun error(message: String, exception: Throwable, buildFunction: (Logger.() -> Unit)? = null) = log(message,
                exception, LogLevel.Error, buildFunction)

        private fun log(builder: Logger, buildFunction: (Logger.() -> Unit)? = null) {
            if (buildFunction != null) {
                buildFunction(builder)
            }
            println(builder.toUnixString())
        }

        private fun log(exception: Throwable, logLevel: LogLevel, buildFunction: (Logger.() -> Unit)? = null) = log(
                exception.message ?: exception.cause?.message ?: "Undefined error", exception, logLevel, buildFunction)


        private fun log(message: String, exception: Throwable, logLevel: LogLevel,
                        buildFunction: (Logger.() -> Unit)? = null) {
            val builder = ExceptionTreeMapper.createLogger(message, exception, logLevel)

            if (buildFunction != null) {
                buildFunction(builder)
            }
            println(builder.toUnixString())
        }
    }
}

/**
 * A log note.
 */
internal data class LogNote(val tag: String, val message: String?)

/**
 * The different log levels for [Logger].
 */
enum class LogLevel(val tag: String, val color: AnsiColor) {
    Debug("debug", AnsiColor.Green), Info("info", AnsiColor.Blue), Warn("warning", AnsiColor.Yellow), Error("error",
            AnsiColor.Red);
}

/**
 * Makes a text to be correctly indented.
 */
internal fun indentText(text: String, indent: Int): String {
    if (indent == 0) {
        return text
    }

    val indentText = "\n${" ".repeat(indent)}"
    return text.replace("\n", indentText)
}