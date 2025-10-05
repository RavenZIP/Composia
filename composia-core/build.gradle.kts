@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.vanniktech.mavenPublish)
}

group = "io.github.ravenzip.composia-core"

version = "0.1.0"

kotlin {
    jvm()

    wasmJs {
        outputModuleName.set("composia-core")
        browser { commonWebpackConfig { outputFileName = "composia-core.js" } }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(libs.kotlin.coroutines)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.turbine)
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "io.github.ravenzip.composia-core"
            artifactId = "composia-core"
            version = "0.1.0"

            afterEvaluate { from(components["kotlin"]) }
        }
    }
}
