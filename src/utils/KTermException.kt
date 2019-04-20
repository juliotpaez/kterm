package es.jtp.kterm.utils

import es.jtp.kterm.*

/**
 * Generic exception for the [es.jtp.kterm] project.
 */
open class KTermException : RuntimeException {
    constructor(message: String) : super(message)
    constructor(message: String, exception: Throwable) : super(message, exception)

    /**
     * Logs this exception to the terminal.
     */
    open fun logMessage() {
        Logger.error(this) {
            showDate()
            showThread()
            showStackExecutionOrder()
        }
    }
}