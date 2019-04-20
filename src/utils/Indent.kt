package es.jtp.kterm.utils

/**
 * Indent container for logger.
 */
internal data class Indent(var indent: String, var times: Int, var timesIndent: String) {
    fun getLength() = indent.length + times * 2
}