package es.jtp.kterm

/**
 * Ansi color codes.
 *
 * @param textCode The code to change the text color.
 * @param backgroundCode The code to change the background color.
 * @param highIntensityTextCode The code to change the text color. High intensity variation.
 * @param highIntensityBackgroundCode The code to change the background color. High intensity variation.
 */
enum class AnsiColor(val textCode: String, val backgroundCode: String, val highIntensityTextCode: String,
        val highIntensityBackgroundCode: String) {
    /**
     * Black ANSI color codes.
     */
    Black("30", "40", "90", "100"),
    /**
     * Red ANSI color codes.
     */
    Red("31", "41", "91", "101"),
    /**
     * Green ANSI color codes.
     */
    Green("32", "42", "92", "102"),
    /**
     * Yellow ANSI color codes.
     */
    Yellow("33", "43", "93", "103"),
    /**
     * Blue ANSI color codes.
     */
    Blue("34", "44", "94", "104"),
    /**
     * Purple ANSI color codes.
     */
    Purple("35", "45", "95", "105"),
    /**
     * Cyan ANSI color codes.
     */
    Cyan("36", "46", "96", "106"),
    /**
     * White ANSI color codes.
     */
    White("37", "47", "97", "107");

    /**
     * Bounds a text into an ANSI block to highlight it with a custom color.
     */
    fun colorText(message: String) = if (message.isEmpty()) {
        ""
    } else {
        "\u001b[${textCode}m$message\u001b[0m"
    }

    /**
     * Bounds a text into an ANSI block to highlight it with a bolder font and a custom color.
     */
    fun boldAndColorText(message: String) = if (message.isEmpty()) {
        ""
    } else {
        "\u001b[1;${textCode}m$message\u001b[0m"
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