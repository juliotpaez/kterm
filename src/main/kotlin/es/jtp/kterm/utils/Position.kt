package es.jtp.kterm.utils

/**
 * Positions for the logger.
 */
data class Position(val index: Int, val row: Int, val column: Int) {
    companion object {
        /**
         * Return a new [Position] from an index in a text.
         */
        fun fromIndex(index: Int, text: String): Position {
            if (index > text.length) {
                throw KTermException("The index is not in the range [0, ${text.length})")
            }

            var row = 1
            var column = 1
            var lastWasCR = false

            for (i in 0 until index) {
                val ch = text[i]
                when (ch) {
                    '\n' -> {
                        if (lastWasCR) {
                            row -= 1
                        }

                        row += 1
                        column = 1
                        lastWasCR = false
                    }
                    '\r' -> {
                        row += 1
                        column = 1
                        lastWasCR = true
                    }
                    else -> {
                        column += 1
                        lastWasCR = false
                    }
                }
            }

            return Position(index, row, column)
        }
    }
}
