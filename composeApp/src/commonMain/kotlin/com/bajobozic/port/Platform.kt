package com.bajobozic.port

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform