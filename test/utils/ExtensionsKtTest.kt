package es.jtp.kterm.test.utils

import es.jtp.kterm.utils.*
import org.junit.jupiter.api.*

internal class ExtensionsKtTest {

    @Test
    fun stringify() {
        // TEST 1
        run {
            val text = "TEST(bar)\\TEST(t)\tTEST(n)\nTEST(r)\rTEST(quotes)\""
            val expected = "TEST(bar)\\\\TEST(t)\\tTEST(n)\\nTEST(r)\\rTEST(quotes)\\\""
            val actual = text.stringify()

            Assertions.assertEquals(expected, actual)
        }
    }
}