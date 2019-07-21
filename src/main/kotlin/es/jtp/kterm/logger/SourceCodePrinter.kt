package es.jtp.kterm.logger

import es.jtp.kterm.*
import es.jtp.kterm.utils.*

/**
 * A different code for logs.
 */
internal interface SourceCodePrinter {
    /**
     * Prints cursor-like messages.
     */
    fun logCursorLikeLine(source: SourceCode, sb: StringBuilder, logger: LoggerBuilder, indent: Indent)

    /**
     * Prints one-line messages.
     */
    fun logOneLine(source: SourceCode, sb: StringBuilder, logger: LoggerBuilder, indent: Indent)

    /**
     * Prints multiline messages with less than 10 lines.
     */
    fun logLess10Multiline(source: SourceCode, sb: StringBuilder, logger: LoggerBuilder, indent: Indent)

    /**
     * Prints multiline messages with more than 10 lines.
     */
    fun logMore10Multiline(source: SourceCode, sb: StringBuilder, logger: LoggerBuilder, indent: Indent)
}

/**
 * The types of source code printers.
 */
internal enum class SourceCodePrinters(val printer: SourceCodePrinter) {
    Coloring(SourceCodeColoringPrinter),
    Erasing(SourceCodeErasingPrinter)
}
