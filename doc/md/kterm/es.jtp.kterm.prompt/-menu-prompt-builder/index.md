[kterm](../../index.md) / [es.jtp.kterm.prompt](../index.md) / [MenuPromptBuilder](./index.md)

# MenuPromptBuilder

`class MenuPromptBuilder`

Prompt builder for selections.

### Functions

| Name | Summary |
|---|---|
| [addDefaultOption](add-default-option.md) | `fun addDefaultOption(tag: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, message: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)` = "", resultAction: (() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`)? = null): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Adds a default option with a custom tag and message. |
| [addOption](add-option.md) | `fun addOption(tag: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, message: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)` = "", resultAction: (() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`)? = null): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Adds a new option with a custom tag and message. |
| [checkInput](check-input.md) | `fun checkInput(input: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`Pair`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, (() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`)?>?`<br>Checks if an input matches any of the tags. |
| [toUnixString](to-unix-string.md) | `fun toUnixString(): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Gets the log message as a string formatted to be writen into an ANSI interpreter. |
