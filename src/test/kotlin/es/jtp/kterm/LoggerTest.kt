package es.jtp.kterm

import org.junit.jupiter.api.*

internal class LoggerTest {
    @Test
    fun `test to see the stack`() {
        val logger = Logger("This is the main message\nline2\nline3") {
            showDate = true
            showThread = true
            showStackNumbers = true


            setStack {
                addStackTrace("file\nmultiline") {
                    className = "class\nmultiline"
                    methodName = "method\nmultiline"
                }
                addStackTrace("1")
                addStackTrace("2")
                addCause("Cause 1\nline2\nline3") {
                    addStackTrace("5")
                }
                addStackTrace("3")
                addCause("Cause 2\nline2\nline3")
                addStackTrace("4")
            }

            addSourceCode("This is the main message\nline2\nline3") {
                useErasingPrinter()
                highlightSection(1, 2)
                message = "Test\nis\nok"
                showNewlineChars = true
            }

            addSourceCode(
                    "line1\nline2\nline3\nline3\nline4\nline5\nline6\nline7\nline8\nline9\nline10\nline11\nline12\nline13\nline14\nline15\nline16\nline17\nline18") {
                useErasingPrinter()
                highlightSection(14, 16)
                message = "Test\nis\nok"
                title = "xxx"
                showNewlineChars = true
            }

            setCause("What's up")
        }

        logger.logAsDebug()
        logger.logAsInfo()
        logger.logAsWarn()
        logger.logAsError()

        println(logger.toString(LogLevel.Debug))
        println(logger.toString(LogLevel.Info))
        println(logger.toString(LogLevel.Warn))
        println(logger.toString(LogLevel.Error))
    }

    @Test
    fun `test to see the readme`() {
        Logger.warn("A warning message.\nIn two lines.\nOr more.") {
            showDate = true
            showThread = true
            showStackNumbers = true

            // Custom stack.
            setStack {
                addStackTrace("path/to/file.txt")
                addStackTrace("path/to/file2.txt", line = 25) {
                    message = "A custom message for this stack trace.\nIn two lines.\nOr more."
                    methodName = "methodName"
                }
                addStackTrace("path/to/file3.txt", line = 25, column = 3) {
                    message = "A custom message for this stack trace."
                }

                addCause("This is a sub-cause.") {
                    addStackTrace("path/to/file.txt")
                    addStackTrace("path/to/file2.txt")
                }

                addStackTrace("path/to/file4.txt")
            }

            // Add custom source code hints.
            addSourceCode("source\ncontent\nof more than\none\nline") {
                title = "This is the title.\nIn two lines."
                highlightCursorAt(4)
                message = "This is the message to explain\nthe highlighted section"
            }

            addSourceCode("source\ncontent\nof more than\none\nline", "path/to/file4.txt") {
                title = "This is the title."
                highlightSection(3, 29)
                message = "This is the message to explain the highlighted section\none\nline"
            }

            addTag("Tag 1")
            addTag("Tag 2")
            addNote("Note 1", "the two above lines are tags, like notes without message.")
            addNote("Note 2", "this is a note message.\nIn two lines.")
        }
    }
}
