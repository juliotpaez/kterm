package es.jtp.kterm

import es.jtp.kterm.logger.*
import es.jtp.kterm.utils.*
import java.time.*
import java.time.format.*

/**
 * A builder for [Logger].
 */
class LoggerBuilder internal constructor(internal val message: String, internal var level: LogLevel) {
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

        val fixedIndentText = " ".repeat(3)
        val mainIndent = Indent(fixedIndentText, 0)

        // Stack
        if (stack != null) {
            val initPosition = stack!!.countStackLength()
            stack!!.toUnixString(sb, this, initPosition, Math.ceil(Math.log10(initPosition + 0.0)).toInt(), mainIndent)

            if (sourceCodes.isNotEmpty()) {
                sb.append(fixedIndentText)
                sb.append(level.color.boldAndColorText("|"))
                sb.append('\n')
            }
        }

        // Source codes
        for (sourceCode in sourceCodes) {
            sourceCode.toUnixString(sb, this, mainIndent)
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

        sb.deleteCharAt(sb.length - 1)
        return sb.toString()
    }

    companion object {
        private val dateTimeFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss.SSS")
    }
}

/**
 * A log note.
 */
internal data class LogNote(val tag: String, val message: String?)

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

/**
 * Makes a text to be correctly indented with a prefix.
 */
internal fun indentText(text: String, prefix: String): String {
    if (prefix == "") {
        return text
    }

    return text.replace("\n", "\n$prefix")
}

/**
 * Makes a text to be correctly indented with a prefix and a build function.
 */
internal fun indentText(text: String, prefix: String, build: (String) -> String): String {
    if (prefix == "") {
        return build(text)
    }

    val result = text.split("\n").map(build)
    return result.joinToString("\n$prefix")
}
