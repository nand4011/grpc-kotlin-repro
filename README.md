# gRPC Kotlin package bug repro

This is a repro of a bug in gRPC Kotlin. When a proto contains a message that includes a field with the same name as the package, the generated code attempts to refer to the package but actually references the field, causing a syntax error.

## Steps to reproduce
Run `./gradlew build`. The build will fail with a compilation error. The generated file _PutFooRequestKt.kt contains this line:
```kotlin
public inline fun foo.FooOuterClass._PutFooRequest.copy(block: `foo`._PutFooRequestKt.Dsl.() -> kotlin.Unit): foo.FooOuterClass._PutFooRequest =
  `foo`._PutFooRequestKt.Dsl._create(this.toBuilder()).apply { block() }._build()
```
If \`foo.\` is removed, the code compiles successfully.
