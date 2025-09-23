pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Composia"

include("composia-ui")

include("composia-core")

if (System.getenv("JITPACK") == null) {
    include("composia-demo")
}
