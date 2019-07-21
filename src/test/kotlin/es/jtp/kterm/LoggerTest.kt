package es.jtp.kterm

import org.junit.jupiter.api.*

internal class LoggerTest {
    @Test
    fun `test to see the logs`() {
        val text = List(16) { "${(it + 1).toString().padStart(2, '-')}2345678" }.joinToString("\n") + "9"
        val source = "path/to/file.test"

        Logger.error("This is the main message") {
            addSourceCode(text, null) {
                highlightCursorAt(6)
                message("This is a message\nmultiline")
                useErasingPrinter()
            }
            addSourceCode(text, null) {
                highlightSection(4)
                message("This is a message\nmultiline")
                useErasingPrinter()
            }
            addSourceCode(text, source) {
                title("Title")
                highlightSection(152, 155)
                message("This is a message\nmultiline")
                useErasingPrinter()
            }
            addSourceCode(text, source) {
                title("Title")
                highlightSection(135, 152)
                message("This is a message\nmultiline")
                useErasingPrinter()
            }
            addSourceCode(text, source) {
                title("Title")
                highlightSection(4, 152)
                message("This is a message\nmultiline")
                useErasingPrinter()
            }
        }

        Logger.warn("This is the main message") {
            addSourceCode(text, null) {
                highlightCursorAt(5)
                useErasingPrinter()
            }
            addSourceCode(text, null) {
                highlightSection(5)
                useErasingPrinter()
            }
            addSourceCode(text, source) {
                title("Title")
                highlightSection(2, 5)
                useErasingPrinter()
            }
            addSourceCode(text, source) {
                title("Title")
                highlightSection(4, 60)
                useErasingPrinter()
            }
            addSourceCode(text, source) {
                title("Title")
                highlightSection(4, 152)
                useErasingPrinter()
            }
        }

        Logger.info("This is the main message") {
            addSourceCode(text, null) {
                highlightCursorAt(5)
                message("This is a message\nmultiline")
                printMessageAtBottom()
                useErasingPrinter()
            }
            addSourceCode(text, null) {
                highlightSection(5)
                message("This is a message\nmultiline")
                printMessageAtBottom()
                useErasingPrinter()
            }
            addSourceCode(text, source) {
                title("Title")
                highlightSection(2, 5)
                message("This is a message\nmultiline")
                printMessageAtBottom()
                useErasingPrinter()
            }
            addSourceCode(text, source) {
                title("Title")
                highlightSection(4, 60)
                message("This is a message\nmultiline")
                printMessageAtBottom()
                useErasingPrinter()
            }
            addSourceCode(text, source) {
                title("Title")
                highlightSection(4, 152)
                message("This is a message\nmultiline")
                printMessageAtBottom()
                useErasingPrinter()
            }
        }
    }
}
