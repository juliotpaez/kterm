package es.jtp.kterm.logger

import es.jtp.kterm.*
import es.jtp.kterm.utils.*

/**
 * A stack level for logs.
 */
internal data class StackLevel(val causedAt: Int, val causeMessage: String, val stackTrace: List<StackTrace>,
                               val causes: List<StackLevel>) {
    /**
     * Gets the stack level as a string formatted to be writen into an ANSI interpreter.
     *
     * @return The number on entries inside it.
     */
    internal fun toUnixString(sb: StringBuilder, logger: Logger, initPosition: Int, maxPositionDigits: Int,
                              indent: Indent): Int {
        // Ignore the first cause if it is the same as the message.
        if (indent.times > 0 || logger.message != causeMessage) {
            // Empty first line only if there is no the first one.
            if (indent.times > 0) {
                sb.append(indent.indent)
                sb.append(indent.timesIndent)
                sb.append('\n')
            }

            val text = ". cause:"
            sb.append(indent.indent)
            sb.append(indent.timesIndent)
            sb.append(logger.level.color.boldAndColorText(text))
            sb.append(' ')
            sb.append(AnsiColor.boldText(indentText(causeMessage, indent.getLength() + text.length)))
            sb.append('\n')
        }

        var causeIndex = causes.lastIndex
        var currentPosition = initPosition
        for (i in stackTrace.lastIndex downTo 0) {
            while (causeIndex >= 0 && causes[causeIndex].causedAt == i) {
                currentPosition = causes[causeIndex].toUnixString(sb, logger, currentPosition, maxPositionDigits,
                        Indent(indent.indent, indent.times + 1,
                                indent.timesIndent + "${logger.level.color.boldAndColorText("|")} "))

                // Empty last line.
                sb.append(indent.indent)
                sb.append(indent.timesIndent)
                sb.append(logger.level.color.boldAndColorText("|--"))
                sb.append('\n')

                causeIndex -= 1
            }

            val trace = stackTrace[i]
            trace.toUnixString(sb, logger, currentPosition, maxPositionDigits, indent)
            currentPosition -= 1
        }

        return currentPosition
    }

    /**
     * Count the whole stack length.
     */
    fun countStackLength(): Int = stackTrace.size + causes.asSequence().sumBy {
        it.countStackLength()
    }
}

/**
 * A stack message builder for logs.
 */
class StackLevelBuilder(private val causedAt: Int, private val message: String) {
    private val stackTrace = mutableListOf<StackTrace>()
    private val causes = mutableListOf<StackLevel>()

    /**
     * Adds a stack trace to the common section or the private one depending on 'cause' is already set or not.
     */
    fun addStackTrace(builderFunction: StackTraceBuilder.() -> Unit) {
        val builder = StackTraceBuilder()
        builderFunction(builder)
        stackTrace.add(builder.toStackTrace())
    }

    /**
     * Adds a cause to the stack level.
     */
    fun addCause(message: String, builderFunction: (StackLevelBuilder.() -> Unit)? = null) {
        if (stackTrace.lastIndex < 0) {
            throw KTermException("Cannot set a cause at the beginning of the stack trace.")
        }

        val builder = StackLevelBuilder(stackTrace.lastIndex, message)
        if (builderFunction != null) {
            builderFunction(builder)
        }
        causes.add(builder.toStackLevel())
    }

    /**
     * Creates a new [StackLevel] from this builder.
     */
    internal fun toStackLevel(): StackLevel {
        val last = causes.lastOrNull()
        if (last != null && last.causedAt == stackTrace.lastIndex) {
            throw KTermException("Cannot set a cause at the end of the stack trace.")
        }

        return StackLevel(causedAt, message, stackTrace, causes)
    }
}