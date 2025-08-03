package io.ravenzip.composia

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
