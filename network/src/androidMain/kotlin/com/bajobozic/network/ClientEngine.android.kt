package com.bajobozic.network

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp

internal actual fun getClientEngine(): HttpClientEngine = OkHttp.create()