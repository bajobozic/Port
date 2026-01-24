package com.bajobozic.network

import io.ktor.client.engine.HttpClientEngine

/**
 * Provides the platform-specific [HttpClientEngine] used to configure the Ktor [HttpClient].
 *
 * This is an expected declaration that must be implemented in each target platform
 * (e.g., Android, iOS) to provide the appropriate engine (like OkHttp, Darwin, etc.).
 *
 * @return The [HttpClientEngine] for the current platform.
 */
expect fun getClientEngine(): HttpClientEngine