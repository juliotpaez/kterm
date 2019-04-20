package es.jtp.kterm

import es.jtp.kterm.prompt.*

/**
 * Terminal interactive methods.
 */
object Prompt {
    /**
     * Generates a confirmation prompt to ask for a boolean value.
     *
     * @return A result of the confimation.
     */
    fun confirm(message: String, color: AnsiColor = AnsiColor.Blue, buildFunction: (ConfimationPromptBuilder.() -> Unit)? = null): Boolean {
        val prompt = ConfimationPromptBuilder(message, color)
        if (buildFunction != null) {
            buildFunction(prompt)
        }

        var result = false
        loop@ while (true) {
            print(prompt.toUnixString())

            val answer = readLine() ?: ""
            when (answer.trim().toLowerCase()) {
                "", "n", "no" -> break@loop
                "y", "yes" -> {
                    result = true
                    break@loop
                }
                else -> {
                    Logger.error("The answer '$answer' is not correct. Use [n, no, y, yes] or leave it blank.")
                }
            }
        }

        return result
    }

    /**
     * Generates a selection prompt to ask for a concrete option.
     *
     * @return The selected option.
     */
    fun menu(message: String, color: AnsiColor = AnsiColor.Blue, buildFunction: (MenuPromptBuilder.() -> Unit)? = null): String {
        val prompt = MenuPromptBuilder(message, color)
        if (buildFunction != null) {
            buildFunction(prompt)
        }

        loop@ while (true) {
            print(prompt.toUnixString())

            val answer = readLine() ?: ""
            val checkedAnswer = prompt.checkInput(answer)
            if (checkedAnswer != null) {
                if (checkedAnswer.second != null) {
                    checkedAnswer.second!!()
                }

                return checkedAnswer.first
            }

            Logger.error("The answer '$answer' is not correct. Use one of the specified above.")
        }
    }
}