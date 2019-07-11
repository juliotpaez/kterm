[es.jtp.kterm.logger](../index.md) / [StackLevelBuilder](./index.md)

# StackLevelBuilder

`class StackLevelBuilder`

A stack message builder for logs.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `StackLevelBuilder(causedAt: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, message: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`)`<br>A stack message builder for logs. |

### Functions

| Name | Summary |
|---|---|
| [addCause](add-cause.md) | `fun addCause(message: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, builderFunction: (`[`StackLevelBuilder`](./index.md)`.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`)? = null): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Adds a cause to the stack level. |
| [addStackTrace](add-stack-trace.md) | `fun addStackTrace(builderFunction: `[`StackTraceBuilder`](../-stack-trace-builder/index.md)`.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Adds a stack trace to the common section or the private one depending on 'cause' is already set or not. |
