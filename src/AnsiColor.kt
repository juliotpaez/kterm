package es.jtp.kterm

/**
 * Ansi color codes.
 */
enum class AnsiColor(
        val colorCode: String,
        val backgroundCode: String,
        val highIntensityColorCode: String,
        val highIntensityBackgroundCode: String
) {
    Black("30", "40", "90", "100"),
    Red("31", "41", "91", "101"),
    Green("32", "42", "92", "102"),
    Yellow("33", "43", "93", "103"),
    Blue("34", "44", "94", "104"),
    Purple("35", "45", "95", "105"),
    Cyan("36", "46", "96", "106"),
    White("37", "47", "97", "107");

    /**
     * Bounds a text into an ANSI block to highlight it with a custom color.
     */
    fun colorText(message: String) = if (message.isEmpty()) {
        ""
    } else {
        "\u001b[${colorCode}m$message\u001b[0m"
    }

    /**
     * Bounds a text into an ANSI block to highlight it with a bolder font and a custom color.
     */
    fun boldAndColorText(message: String) = if (message.isEmpty()) {
        ""
    } else {
        "\u001b[1;${colorCode}m$message\u001b[0m"
    }

    companion object {
        /**
         * Bounds a text into an ANSI block to highlight it with a bolder font.
         */
        fun boldText(message: String) = if (message.isEmpty()) {
            ""
        } else {
            "\u001b[1m$message\u001b[0m"
        }
    }
}