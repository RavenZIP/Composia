package io.github.ravenzip.composia

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
