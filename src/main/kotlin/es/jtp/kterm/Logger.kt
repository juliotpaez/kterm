package es.jtp.kterm

import es.jtp.kterm.logger.*

/**
 * Custom logs for the compiler.
 */
class Logger internal constructor(private val builder: LoggerBuilder,
        internal val buildFunction: (LoggerBuilder.() -> Unit)? = null) {
    private var wasInvoked = buildFunction == null

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
        if (!wasInvoked) {
            buildFunction!!.invoke(builder)
            wasInvoked = true
        }

        builder.level = level
        println(builder.toUnixString())
    }

    companion object {
        /**
         * Builds a new [Logger] logging a message.
         */
        fun build(message: String, buildFunction: (LoggerBuilder.() -> Unit)? = null) =
                Logger(LoggerBuilder(message, LogLevel.Debug), buildFunction) // This level will be overwritten lately.

        /**
         * Builds a new [Logger] logging an exception.
         */
        fun build(exception: Throwable, buildFunction: (LoggerBuilder.() -> Unit)? = null) =
                Logger(ExceptionTreeMapper.createLogger(
                        exception.message ?: exception.cause?.message ?: "Undefined error", exception), buildFunction)

        /**
         * Builds a new [Logger] logging an exception with a custom message.
         */
        fun build(message: String, exception: Throwable, buildFunction: (LoggerBuilder.() -> Unit)? = null) =
                Logger(ExceptionTreeMapper.createLogger(message, exception), buildFunction)

        /**
         * Logs a message at debug level.
         */
        fun debug(message: String, buildFunction: (LoggerBuilder.() -> Unit)? = null) =
                build(message, buildFunction).logAsDebug()

        /**
         * Logs an exception at debug level.
         */
        fun debug(exception: Throwable, buildFunction: (LoggerBuilder.() -> Unit)? = null) =
                build(exception, buildFunction).logAsDebug()

        /**
         * Logs an exception at debug level with a custom message.
         */
        fun debug(message: String, exception: Throwable, buildFunction: (LoggerBuilder.() -> Unit)? = null) =
                build(message, exception, buildFunction).logAsDebug()

        /**
         * Logs a message at info level.
         */
        fun info(message: String, buildFunction: (LoggerBuilder.() -> Unit)? = null) =
                build(message, buildFunction).logAsInfo()

        /**
         * Logs an exception at info level.
         */
        fun info(exception: Throwable, buildFunction: (LoggerBuilder.() -> Unit)? = null) =
                build(exception, buildFunction).logAsInfo()

        /**
         * Logs an exception at info level with a custom message.
         */
        fun info(message: String, exception: Throwable, buildFunction: (LoggerBuilder.() -> Unit)? = null) =
                build(message, exception, buildFunction).logAsInfo()

        /**
         * Logs a message at warn level.
         */
        fun warn(message: String, buildFunction: (LoggerBuilder.() -> Unit)? = null) =
                build(message, buildFunction).logAsWarn()

        /**
         * Logs an exception at warn level.
         */
        fun warn(exception: Throwable, buildFunction: (LoggerBuilder.() -> Unit)? = null) =
                build(exception, buildFunction).logAsWarn()

        /**
         * Logs an exception at warn level with a custom message.
         */
        fun warn(message: String, exception: Throwable, buildFunction: (LoggerBuilder.() -> Unit)? = null) =
                build(message, exception, buildFunction).logAsWarn()

        /**
         * Logs a message at error level.
         */
        fun error(message: String, buildFunction: (LoggerBuilder.() -> Unit)? = null) =
                build(message, buildFunction).logAsError()

        /**
         * Logs an exception at error level.
         */
        fun error(exception: Throwable, buildFunction: (LoggerBuilder.() -> Unit)? = null) =
                build(exception, buildFunction).logAsError()

        /**
         * Logs an exception at error level with a custom message.
         */
        fun error(message: String, exception: Throwable, buildFunction: (LoggerBuilder.() -> Unit)? = null) =
                build(message, exception, buildFunction).logAsError()
    }
}

/**
 * The different log levels for [Logger].
 *
 * @param tag The tag used to identify the level in the console.
 * @param color The ANSI color associated with the log level.
 */
enum class LogLevel(val tag: String, val color: AnsiColor) {
    /**
     * Debug level for logging.
     */
    Debug("debug", AnsiColor.Green),
    /**
     * Info level for logging.
     */
    Info("info", AnsiColor.Blue),
    /**
     * Warn level for logging.
     */
    Warn("warning", AnsiColor.Yellow),
    /**
     * Error level for logging.
     */
    Error("error", AnsiColor.Red);
}
