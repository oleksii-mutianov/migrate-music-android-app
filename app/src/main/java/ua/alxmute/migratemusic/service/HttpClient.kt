package ua.alxmute.migratemusic.service

import okhttp3.*

object HttpClient {

    private val httpClient = OkHttpClient()

    fun get(url: String, vararg headers: Pair<String, String>): Response {
        return doCall(url, headers) { get() }
    }

    fun post(url: String, vararg headers: Pair<String, String>): Response {
        return doCall(url, headers) { post() }
    }

    fun put(url: String, vararg headers: Pair<String, String>): Response {
        return doCall(url, headers) { put() }
    }

    fun Response.json() = body()?.string()

    private fun Request.Builder.post() = post(RequestBody.create(null, ""))
    private fun Request.Builder.put() = put(RequestBody.create(null, ""))

    private fun doCall(
        url: String,
        headers: Array<out Pair<String, String>>,
        method: Request.Builder.() -> Request.Builder
    ): Response {
        val request = Request.Builder()
            .url(url)
            .headers(Headers.of(headers.toMap()))
            .method()
            .build()
        return httpClient.newCall(request).execute()
    }


}

