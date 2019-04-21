[kterm](../../index.md) / [es.jtp.kterm](../index.md) / [AnsiColor](./index.md)

# AnsiColor

`enum class AnsiColor`

Ansi color codes.

### Parameters

`textCode` - The code to change the text color.

`backgroundCode` - The code to change the background color.

`highIntensityTextCode` - The code to change the text color. High intensity variation.

`highIntensityBackgroundCode` - The code to change the background color. High intensity variation.

### Enum Values

| Name | Summary |
|---|---|
| [Black](-black.md) | Black ANSI color codes. |
| [Red](-red.md) | Red ANSI color codes. |
| [Green](-green.md) | Green ANSI color codes. |
| [Yellow](-yellow.md) | Yellow ANSI color codes. |
| [Blue](-blue.md) | Blue ANSI color codes. |
| [Purple](-purple.md) | Purple ANSI color codes. |
| [Cyan](-cyan.md) | Cyan ANSI color codes. |
| [White](-white.md) | White ANSI color codes. |

### Properties

| Name | Summary |
|---|---|
| [backgroundCode](background-code.md) | `val backgroundCode: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The code to change the background color. |
| [highIntensityBackgroundCode](high-intensity-background-code.md) | `val highIntensityBackgroundCode: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The code to change the background color. High intensity variation. |
| [highIntensityTextCode](high-intensity-text-code.md) | `val highIntensityTextCode: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The code to change the text color. High intensity variation. |
| [textCode](text-code.md) | `val textCode: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The code to change the text color. |

### Functions

| Name | Summary |
|---|---|
| [boldAndColorText](bold-and-color-text.md) | `fun boldAndColorText(message: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Bounds a text into an ANSI block to highlight it with a bolder font and a custom color. |
| [colorText](color-text.md) | `fun colorText(message: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Bounds a text into an ANSI block to highlight it with a custom color. |

### Companion Object Functions

| Name | Summary |
|---|---|
| [boldText](bold-text.md) | `fun boldText(message: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Bounds a text into an ANSI block to highlight it with a bolder font. |
