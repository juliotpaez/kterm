package es.jtp.kterm

import org.junit.jupiter.api.*

internal class LoggerTest {
    @Test
    @Disabled
    fun `test to see the logs`() {
        val text = List(20) { "this is a text" }.joinToString("\n")
        val source = "path/to/file.test"
        Logger.error("This is the main message") {
            addSourceCode(text, null) {
                highlightAt(5)
                message("This is a message")
                useErasingPrinter()
            }
            addSourceCode(text, source) {
                title("Title")
                highlightSection(2, 5)
                message("This is a message")
                useErasingPrinter()
            }
            addSourceCode(text, source) {
                title("Title")
                highlightSection(4, 60)
                message("This is a message")
                useErasingPrinter()
            }
            addSourceCode(text, source) {
                title("Title")
                highlightSection(4, 152)
                message("This is a message")
                useErasingPrinter()
            }
        }

        Logger.info("This is the main message") {
            addSourceCode(text, null) {
                highlightAt(5)
                message("This is a message")
                printMessageAtBottom()
                useErasingPrinter()
            }
            addSourceCode(text, source) {
                title("Title")
                highlightSection(2, 5)
                message("This is a message")
                printMessageAtBottom()
                useErasingPrinter()
            }
            addSourceCode(text, source) {
                title("Title")
                highlightSection(4, 60)
                message("This is a message")
                printMessageAtBottom()
                useErasingPrinter()
            }
            addSourceCode(text, source) {
                title("Title")
                highlightSection(4, 152)
                message("This is a message")
                printMessageAtBottom()
                useErasingPrinter()
            }
        }

        throw Exception()
    }
}