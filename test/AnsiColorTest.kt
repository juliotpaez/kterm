package es.jtp.kterm.test

import es.jtp.kterm.*
import org.junit.jupiter.api.*

internal class AnsiColorTest {

    @Test
    fun colorText() {
        // TEST 1
        run {
            val text = "TEST"
            val expected = "\u001b[33m$text\u001b[0m"
            val actual = AnsiColor.Yellow.colorText(text)

            Assertions.assertEquals(expected, actual)
        }

        // TEST 2
        run {
            val text = "TEST"
            val expected = "\u001b[32m$text\u001b[0m"
            val actual = AnsiColor.Green.colorText(text)

            Assertions.assertEquals(expected, actual)
        }

        // TEST 3
        run {
            val expected = ""
            val actual = AnsiColor.Purple.colorText("")

            Assertions.assertEquals(expected, actual)
        }
    }

    @Test
    fun boldAndColorText() {
        // TEST 1
        run {
            val text = "TEST"
            val expected = "\u001b[1;34m$text\u001b[0m"
            val actual = AnsiColor.Blue.boldAndColorText(text)

            Assertions.assertEquals(expected, actual)
        }

        // TEST 2
        run {
            val text = "TEST"
            val expected = "\u001b[1;37m$text\u001b[0m"
            val actual = AnsiColor.White.boldAndColorText(text)

            Assertions.assertEquals(expected, actual)
        }

        // TEST 3
        run {
            val expected = ""
            val actual = AnsiColor.Red.boldAndColorText("")

            Assertions.assertEquals(expected, actual)
        }
    }

    @Test
    fun boldText() {
        // TEST 1
        run {
            val text = "TEST"
            val expected = "\u001b[1m$text\u001b[0m"
            val actual = AnsiColor.boldText(text)

            Assertions.assertEquals(expected, actual)
        }

        // TEST 2
        run {
            val expected = ""
            val actual = AnsiColor.boldText("")

            Assertions.assertEquals(expected, actual)
        }
    }
}