@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.vanniktech.mavenPublish)
}

group = "io.github.ravenzip.composia-core"

version = "0.0.1"

kotlin {
    jvm()

    wasmJs {
        outputModuleName.set("composia-core")
        browser { commonWebpackConfig { outputFileName = "composia-core.js" } }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlin.coroutines)
            //            implementation(libs.kotlin.reflect)
        }

        commonTest.dependencies { implementation(libs.kotlin.test) }
    }
}

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "io.github.ravenzip.composia-core"
            artifactId = "composia-core"
            version = "0.0.1"

            afterEvaluate { from(components["kotlin"]) }
        }
    }
}
