[kterm](../../index.md) / [es.jtp.kterm](../index.md) / [LoggerBuilder](./index.md)

# LoggerBuilder

`class LoggerBuilder`

A builder for [Logger](../-logger/index.md).

### Functions

| Name | Summary |
|---|---|
| [addNote](add-note.md) | `fun addNote(tag: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, message: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Adds a custom note with a tag to the log. |
| [addSourceCode](add-source-code.md) | `fun addSourceCode(content: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, builderFunction: `[`SourceCodeBuilder`](../../es.jtp.kterm.logger/-source-code-builder/index.md)`.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Adds a custom source code builder to the log.`fun addSourceCode(content: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, filename: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?, builderFunction: `[`SourceCodeBuilder`](../../es.jtp.kterm.logger/-source-code-builder/index.md)`.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Adds a custom source code builder to the log with the file path of the content. |
| [addTag](add-tag.md) | `fun addTag(tag: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Adds a custom tag to the log. |
| [showDate](show-date.md) | `fun showDate(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Shows the log date. |
| [showStackExecutionOrder](show-stack-execution-order.md) | `fun showStackExecutionOrder(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Shows stack numbers. |
| [showThread](show-thread.md) | `fun showThread(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Shows the thread name. |
| [stack](stack.md) | `fun stack(message: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, builderFunction: `[`StackLevelBuilder`](../../es.jtp.kterm.logger/-stack-level-builder/index.md)`.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Adds a custom stack level to the log. |
