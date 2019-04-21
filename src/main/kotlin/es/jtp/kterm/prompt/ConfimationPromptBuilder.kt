package es.jtp.kterm.prompt

import es.jtp.kterm.*

/**
 * Prompt builder for confirmation messages.
 */
class ConfimationPromptBuilder internal constructor(private val message: String) {
    private var color: AnsiColor = AnsiColor.Blue
    private var isInline = false
    internal var defaultValue = false

    /**
     * Sets the color of the prompt.
     */
    fun promptColor(color: AnsiColor) {
        this.color = color
    }

    /**
     * Sets the default value in case of blank input.
     */
    fun defaultValue(value: Boolean) {
        defaultValue = value
    }

    /**
     * Inlines the answer with the question.
     */
    fun inlineAnswer() {
        isInline = true
    }

    /**
     * Gets the log message as a string formatted to be writen into an ANSI interpreter.
     */
    internal fun toUnixString(): String {
        val sb = StringBuilder()

        if (isInline) {
            sb.append(color.boldAndColorText("-->"))
            sb.append(' ')
            sb.append(AnsiColor.boldText(message))
        } else {
            sb.append(AnsiColor.boldText(message))
            sb.append('\n')
            sb.append(color.boldAndColorText("-->"))
        }

        sb.append(' ')
        if (defaultValue) {
            sb.append(AnsiColor.boldText("["))
            sb.append(color.boldAndColorText("Y"))
            sb.append(AnsiColor.boldText("/n"))
        } else {
            sb.append(AnsiColor.boldText("[y/"))
            sb.append(color.boldAndColorText("N"))
        }
        sb.append(AnsiColor.boldText("]:"))
        sb.append(' ')

        return sb.toString()
    }
}