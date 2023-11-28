plugins {
    kotlin("jvm") version "1.9.10"
    id("com.google.protobuf") version "0.9.4"
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    protobuf(files("./proto"))

    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")

    api("io.grpc:grpc-stub:1.59.0")
    api("io.grpc:grpc-protobuf:1.59.0")
    api("io.grpc:grpc-kotlin-stub:1.4.1")
    api("com.google.protobuf:protobuf-kotlin:3.25.1")
}

kotlin {
    jvmToolchain(8)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().all {
    kotlinOptions {
        freeCompilerArgs = listOf("-opt-in=kotlin.RequiresOptIn")
    }
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.25.1"
    }
    plugins {
        create("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.59.0"
        }
        create("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:1.4.1:jdk8@jar"
        }
    }
    generateProtoTasks {
        all().forEach {
            it.builtins {
                named("java")
                create("kotlin")
            }
            it.plugins {
                create("grpc")
                create("grpckt")
            }
            it.generateDescriptorSet = true
            it.descriptorSetOptions.path = "$projectDir/generated-sources/descriptors/client_protos.dsc"
            it.descriptorSetOptions.includeImports = true
            it.descriptorSetOptions.includeSourceInfo = true
        }
    }
}

