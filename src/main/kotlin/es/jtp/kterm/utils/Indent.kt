package es.jtp.kterm.utils

/**
 * Indent container for logger.
 */
object Indent {
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
}
