package es.jtp.kterm

/**
 * The different log levels for [Logger].
 *
 * @param tag The tag used to identify the level in the console.
 * @param color The ANSI color associated with the log level.
 */
enum class LogLevel(internal val tag: String, val color: AnsiColor) {
    /**
     * Debug level for logging.
     */
    Debug("debug", AnsiColor.Green),
    /**
     * Info level for logging.
     */
    Info("info ", AnsiColor.Blue),
    /**
     * Warn level for logging.
     */
    Warn("warn ", AnsiColor.Yellow),
    /**
     * Error level for logging.
     */
    Error("error", AnsiColor.Red);
}
