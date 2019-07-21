[es.jtp.kterm.logger](../index.md) / [SourceCodeBuilder](./index.md)

# SourceCodeBuilder

`class SourceCodeBuilder`

A source code builder for logs.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `SourceCodeBuilder(content: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, filename: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?)`<br>A source code builder for logs. |

### Functions

| Name | Summary |
|---|---|
| [highlightCursorAt](highlight-cursor-at.md) | `fun highlightCursorAt(position: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Sets a cursor position at the specified position of the content. |
| [highlightSection](highlight-section.md) | `fun highlightSection(position: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Sets the character of the content to highlight.`fun highlightSection(from: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, to: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Sets the section of the content to highlight. |
| [message](message.md) | `fun message(message: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Adds a custom message to the source code message. |
| [printMessageAtBottom](print-message-at-bottom.md) | `fun printMessageAtBottom(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Prints the message separated at the bottom instead of inline with the code. |
| [showNewlineChars](show-newline-chars.md) | `fun showNewlineChars(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Prints the newline characters in the code. |
| [title](title.md) | `fun title(title: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Sets a title for the source code block. |
| [useErasingPrinter](use-erasing-printer.md) | `fun useErasingPrinter(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Uses the erasing printer instead of the coloring one. |
