[kterm](../../index.md) / [es.jtp.kterm.utils](../index.md) / [KTermException](./index.md)

# KTermException

`open class KTermException : `[`RuntimeException`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-runtime-exception/index.html)

Generic exception for the [es.jtp.kterm](../../es.jtp.kterm/index.md) project.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `KTermException(message: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`)`<br>Creates a new [KTermException](./index.md) with a message.`KTermException(message: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, exception: `[`Throwable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)`)`<br>Creates a new [KTermException](./index.md) with a message and a cause. |

### Functions

| Name | Summary |
|---|---|
| [logMessage](log-message.md) | `open fun logMessage(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Logs this exception to the terminal. |
