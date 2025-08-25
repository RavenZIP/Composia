@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.vanniktech.mavenPublish)
}

group = "io.github.ravenzip.composia-ui"

version = "0.0.1"

kotlin {
    jvm()

    wasmJs {
        outputModuleName.set("composia-ui")
        browser { commonWebpackConfig { outputFileName = "composia-ui.js" } }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)

            implementation(project(":composia-core"))
        }

        commonTest.dependencies { implementation(libs.kotlin.test) }
    }
}

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "io.github.ravenzip.composia-ui"
            artifactId = "composia-ui"
            version = "0.0.1"

            afterEvaluate { from(components["kotlin"]) }
        }
    }
}
