[es.jtp.kterm.prompt](../index.md) / [MenuPromptBuilder](./index.md)

# MenuPromptBuilder

`class MenuPromptBuilder`

Prompt builder for selections.

### Functions

| Name | Summary |
|---|---|
| [addDefaultOption](add-default-option.md) | `fun addDefaultOption(tag: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, message: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)` = "", resultAction: (() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`)? = null): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Adds a default option with a custom tag and message. |
| [addOption](add-option.md) | `fun addOption(tag: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, message: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)` = "", resultAction: (() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`)? = null): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Adds a new option with a custom tag and message. |
| [promptColor](prompt-color.md) | `fun promptColor(color: `[`AnsiColor`](../../es.jtp.kterm/-ansi-color/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Sets the color of the prompt. |
