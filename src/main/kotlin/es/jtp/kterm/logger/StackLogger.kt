package es.jtp.kterm.logger

import es.jtp.kterm.*
import es.jtp.kterm.utils.*

/**
 * A stack for logs.
 */
class StackLogger(causeMessage: String, val causedAtStackTraceIndex: Int) {
    val causeMessage = causeMessage.stringify()
    private val stackTraces = mutableListOf<StackTraceLogger>()
    private val causes = mutableListOf<StackLogger>()

    // METHODS ----------------------------------------------------------------

    /**
     * Gets the stack trace list.
     */
    fun getStackTraces() = stackTraces.toList()

    /**
     * Gets the cause list.
     */
    fun getCauses() = causes.toList()

    /**
     * Adds a stack trace to the common section or the private one depending on 'cause' is already set or not.
     */
    fun addStackTrace(stackTrace: StackTraceLogger, configFunction: (StackTraceLogger.() -> Unit)? = null) {
        configFunction?.invoke(stackTrace)
        stackTraces.add(stackTrace)
    }

    /**
     * Adds a stack trace to the common section or the private one depending on 'cause' is already set or not.
     */
    fun addStackTrace(filePath: String, configFunction: (StackTraceLogger.() -> Unit)? = null) {
        val logger = StackTraceLogger(filePath)
        configFunction?.invoke(logger)
        stackTraces.add(logger)
    }

    /**
     * Adds a stack trace to the common section or the private one depending on 'cause' is already set or not.
     */
    fun addStackTrace(filePath: String, line: Int, configFunction: (StackTraceLogger.() -> Unit)? = null) {
        val logger = StackTraceLogger(filePath, line)
        configFunction?.invoke(logger)
        stackTraces.add(logger)
    }


    /**
     * Adds a stack trace to the common section or the private one depending on 'cause' is already set or not.
     */
    fun addStackTrace(filePath: String, line: Int, column: Int, configFunction: (StackTraceLogger.() -> Unit)? = null) {
        val logger = StackTraceLogger(filePath, line, column)
        configFunction?.invoke(logger)
        stackTraces.add(logger)
    }

    /**
     * Adds a cause to the stack.
     */
    fun addCause(cause: StackLogger, configFunction: (StackLogger.() -> Unit)? = null) {
        configFunction?.invoke(cause)
        causes.add(cause)
    }

    /**
     * Adds a cause to the stack.
     */
    fun addCause(message: String, configFunction: (StackLogger.() -> Unit)? = null) {
        if (stackTraces.lastIndex < 0) {
            throw KTermException("Cannot set a cause at the beginning of the stack trace.")
        }

        val logger = StackLogger(message, stackTraces.lastIndex)
        configFunction?.invoke(logger)
        causes.add(logger)
    }

    /**
     * Removes the stack trace at the specified index.
     */
    fun removeStackTrace(index: Int) = stackTraces.removeAt(index)

    /**
     * Removes the cause at the specified index.
     */
    fun removeCause(index: Int) = causes.removeAt(index)

    /**
     * Count the whole stack length.
     */
    internal fun countStackLength(): Int = stackTraces.size + causes.asSequence().sumBy {
        it.countStackLength()
    }

    /**
     * Gets the number of stack traces that are contained recursively in this stack.
     */
    internal fun countStackTraceIndexes(initPosition: Int): Int {
        var causeIndex = causes.lastIndex
        var currentPosition = initPosition
        for (i in stackTraces.lastIndex downTo 0) {
            while (causeIndex >= 0 && causes[causeIndex].causedAtStackTraceIndex == i) {
                currentPosition = causes[causeIndex].countStackTraceIndexes(currentPosition)
                causeIndex -= 1
            }

            currentPosition -= 1
        }

        return currentPosition
    }

    /**
     * Gets the stack as a string formatted to be written into an ANSI interpreter.
     *
     * @param initPosition The relative position of the stack.
     * @param maxPositionDigits The number of digits of the index of the last stack trace.
     *
     * @return The number on entries inside it.
     */
    fun toUnixString(logger: Logger, level: LogLevel, initPosition: Int, maxPositionDigits: Int): String {
        val sb = StringBuilder()

        var causeIndex = causes.lastIndex
        var currentPosition = initPosition
        for (i in stackTraces.lastIndex downTo 0) {
            while (causeIndex >= 0 && causes[causeIndex].causedAtStackTraceIndex == i) {

                sb.append(level.color.boldAndColorText("|  "))
                sb.append('\n')
                sb.append(level.color.boldAndColorText("|  * "))
                sb.append(AnsiColor.boldText(causes[causeIndex].causeMessage))
                sb.append('\n')

                val cause = causes[causeIndex].toUnixString(logger, level, currentPosition, maxPositionDigits)

                if (cause.isNotBlank()) {
                    sb.append(level.color.boldAndColorText("|  "))
                    sb.append(Indent.indentText(cause.dropLast(1), level.color.boldAndColorText("|  ")))
                    currentPosition = causes[causeIndex].countStackTraceIndexes(currentPosition)
                    sb.append('\n')
                }

                // Empty last line.
                sb.append(level.color.boldAndColorText("|---"))
                sb.append('\n')

                causeIndex -= 1
            }

            val trace = stackTraces[i].toUnixString(level, logger.showStackNumbers, currentPosition, maxPositionDigits)
            sb.append(trace)
            currentPosition -= 1
        }

        return sb.toString()
    }

    /**
     * Gets the stack as a string.
     *
     * @param initPosition The relative position of the stack.
     * @param maxPositionDigits The number of digits of the index of the last stack trace.
     *
     * @return The number on entries inside it.
     */
    fun toString(logger: Logger, initPosition: Int, maxPositionDigits: Int): String {
        val sb = StringBuilder()

        var causeIndex = causes.lastIndex
        var currentPosition = initPosition
        for (i in stackTraces.lastIndex downTo 0) {
            while (causeIndex >= 0 && causes[causeIndex].causedAtStackTraceIndex == i) {
                sb.append("|  \n")
                sb.append("|  * ${causes[causeIndex].causeMessage}\n")

                val cause = causes[causeIndex].toString(logger, currentPosition, maxPositionDigits)

                if (cause.isNotBlank()) {
                    sb.append("|  ")
                    sb.append(Indent.indentText(cause.dropLast(1), "|  "))
                    currentPosition = causes[causeIndex].countStackTraceIndexes(currentPosition)
                    sb.append('\n')
                }

                // Empty last line.
                sb.append("|---\n")

                causeIndex -= 1
            }

            val trace = stackTraces[i].toString(logger.showStackNumbers, currentPosition, maxPositionDigits)
            sb.append(trace)
            currentPosition -= 1
        }

        return sb.toString()
    }

    override fun toString(): String {
        return "StackLogger(causeMessage='$causeMessage', stackTraces=$stackTraces, causes=$causes)"
    }
}
