[kterm](../../index.md) / [es.jtp.kterm](../index.md) / [Prompt](./index.md)

# Prompt

`object Prompt`

Terminal interactive methods.

### Functions

| Name | Summary |
|---|---|
| [confirm](confirm.md) | `fun confirm(message: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, color: `[`AnsiColor`](../-ansi-color/index.md)` = AnsiColor.Blue, buildFunction: (`[`ConfimationPromptBuilder`](../../es.jtp.kterm.prompt/-confimation-prompt-builder/index.md)`.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`)? = null): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Generates a confirmation prompt to ask for a boolean value. |
| [menu](menu.md) | `fun menu(message: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, color: `[`AnsiColor`](../-ansi-color/index.md)` = AnsiColor.Blue, buildFunction: (`[`MenuPromptBuilder`](../../es.jtp.kterm.prompt/-menu-prompt-builder/index.md)`.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`)? = null): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Generates a selection prompt to ask for a concrete option. |
