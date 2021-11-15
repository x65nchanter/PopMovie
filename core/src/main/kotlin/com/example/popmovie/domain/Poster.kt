package com.example.popmovie.domain

import java.net.URI
import java.net.URL

sealed class Poster {
    object Empty : Poster()
    data class ExternalSized(
        val host: URI, val file: URI, val width: Size = Size.ORIGINAL
    ) : Poster() {
        val url: URL = host
            .resolve(width.path)
            .resolve(file)
            .toURL()

        constructor(base: String, file: String): this(URI.create(base), URI.create(file))
    }

    enum class Size(val path: URI) {
        ORIGINAL(URI.create("./original/")),
    }

    companion object
}