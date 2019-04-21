package es.jtp.kterm.prompt

import es.jtp.kterm.*
import es.jtp.kterm.utils.*
import kotlin.math.*

/**
 * Prompt builder for selections.
 */
class MenuPromptBuilder internal constructor(private val message: String) {
    private var color: AnsiColor = AnsiColor.Blue
    private var maxTagSize = 0
    private val optionsMap = mutableMapOf<String, Pair<String, (() -> Unit)?>>()
    private val optionsKeys = mutableListOf<String>()
    private var default: Pair<String, Pair<String, (() -> Unit)?>>? = null

    /**
     * Sets the color of the prompt.
     */
    fun promptColor(color: AnsiColor) {
        this.color = color
    }

    /**
     * Adds a new option with a custom tag and message.
     */
    fun addOption(tag: String, message: String = "", resultAction: (() -> Unit)? = null) {
        val tag = tag.stringify()
        if (!optionsMap.containsKey(tag)) {
            maxTagSize = max(maxTagSize, tag.length)
            optionsKeys.add(tag)
        }

        optionsMap[tag] = Pair(message, resultAction)
    }

    /**
     * Adds a default option with a custom tag and message.
     */
    fun addDefaultOption(tag: String, message: String = "", resultAction: (() -> Unit)? = null) {
        default = Pair(tag.stringify(), Pair(message, resultAction))
        maxTagSize = max(maxTagSize, "default".length)
    }

    /**
     * Gets the log message as a string formatted to be writen into an ANSI interpreter.
     */
    internal fun toUnixString(): String {
        if (default == null && optionsKeys.isEmpty()) {
            throw KTermException("The menu prompt require at least one option")
        }

        val sb = StringBuilder()

        sb.append(AnsiColor.boldText(message))
        sb.append('\n')

        for (key in optionsKeys) {
            sb.append("  ${color.boldAndColorText("|")} ")
            sb.append(AnsiColor.boldText(key))
            sb.append(" ".repeat(maxTagSize - key.length))
            sb.append("  ")
            sb.append(indentText(optionsMap[key]!!.first, maxTagSize + 6))
            sb.append('\n')
        }

        if (default != null) {
            sb.append("  ${color.boldAndColorText("|")} ")
            sb.append(color.boldAndColorText("default"))
            sb.append(" ".repeat(maxTagSize - "default".length))
            sb.append("  ")
            sb.append(indentText(default!!.second.first, maxTagSize + 6))
            sb.append('\n')
        }

        sb.append(" ${color.boldAndColorText("-->")}")
        sb.append(' ')

        return sb.toString()
    }

    /**
     * Checks if an input matches any of the tags.
     */
    internal fun checkInput(input: String) = when {
        optionsMap.containsKey(input) -> Pair(input, optionsMap[input]!!.second)
        default != null -> Pair(default!!.first, default!!.second.second)
        else -> null
    }
}