[kterm](../../index.md) / [es.jtp.kterm](../index.md) / [Logger](index.md) / [build](./build.md)

# build

`fun build(message: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, buildFunction: (`[`LoggerBuilder`](../-logger-builder/index.md)`.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`)? = null): `[`Logger`](index.md)

Builds a new [Logger](index.md) logging a message.

`fun build(exception: `[`Throwable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)`, buildFunction: (`[`LoggerBuilder`](../-logger-builder/index.md)`.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`)? = null): `[`Logger`](index.md)

Builds a new [Logger](index.md) logging an exception.

`fun build(message: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, exception: `[`Throwable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)`, buildFunction: (`[`LoggerBuilder`](../-logger-builder/index.md)`.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`)? = null): `[`Logger`](index.md)

Builds a new [Logger](index.md) logging an exception with a custom message.

