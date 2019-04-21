package es.jtp.kterm.prompt

import es.jtp.kterm.*

/**
 * Prompt builder for confirmation messages.
 */
class ConfimationPromptBuilder internal constructor(private val message: String,
                                                    private val color: AnsiColor = AnsiColor.Blue) {
    private var isInline = false

    /**
     * Inlines the answer with the question.
     */
    fun inlineAnswer() {
        isInline = true
    }

    /**
     * Gets the log message as a string formatted to be writen into an ANSI interpreter.
     */
    fun toUnixString(): String {
        val sb = StringBuilder()

        if (isInline) {
            sb.append(color.boldAndColorText("-->"))
            sb.append(' ')
            sb.append(AnsiColor.boldText(message))
            sb.append(' ')
            sb.append(AnsiColor.boldText("[y/"))
            sb.append(color.boldAndColorText("N"))
            sb.append(AnsiColor.boldText("]:"))
            sb.append(' ')
        } else {
            sb.append(AnsiColor.boldText(message))
            sb.append('\n')
            sb.append(color.boldAndColorText("-->"))
            sb.append(' ')
            sb.append(AnsiColor.boldText("[y/"))
            sb.append(color.boldAndColorText("N"))
            sb.append(AnsiColor.boldText("]:"))
            sb.append(' ')
        }

        return sb.toString()
    }
}