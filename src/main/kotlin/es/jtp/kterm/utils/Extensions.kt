package es.jtp.kterm.utils

/**
 * Returns a string representation with some characters escaped to be represented as the content of a quoted string, i.e. "...".
 */
fun String.stringify() = this.replace("\\", "\\\\").replace("\t", "\\t").replace("\r", "\\r").replace("\n",
                "\\n").replace("\"", "\\\"")
