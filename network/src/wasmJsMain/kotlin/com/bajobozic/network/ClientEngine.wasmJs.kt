package com.bajobozic.network

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.js.Js

internal actual fun getClientEngine(): HttpClientEngine = Js.create()
