package es.jtp.kterm.logger

import es.jtp.kterm.*
import es.jtp.kterm.logger.printers.*

/**
 * A different code for logs.
 */
internal interface SourceCodePrinter {
    /**
     * Prints cursor-like messages.
     */
    fun logCursorLikeLine(source: SourceCodeLogger, level: LogLevel, logger: Logger): String

    /**
     * Prints one-line messages.
     */
    fun logOneLine(source: SourceCodeLogger, level: LogLevel, logger: Logger): String

    /**
     * Prints multiline messages with less than 10 lines.
     */
    fun logLess10Multiline(source: SourceCodeLogger, level: LogLevel, logger: Logger): String

    /**
     * Prints multiline messages with more than 10 lines.
     */
    fun logMore10Multiline(source: SourceCodeLogger, level: LogLevel, logger: Logger): String
}

/**
 * The types of source code printers.
 */
internal enum class SourceCodePrinters(val unixPrinter: SourceCodePrinter, val normalPrinter: SourceCodePrinter) {
    Coloring(SourceCodeColoringUnixPrinter, SourceCodeColoringPrinter),
    Erasing(SourceCodeErasingUnixPrinter, SourceCodeErasingPrinter) // TODO
}
