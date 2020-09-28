package ua.alxmute.migratemusic.service

import okhttp3.*

object HttpClient {

    private val httpClient = OkHttpClient()

    fun get(url: String, vararg headers: Pair<String, String>): Response {
        val request = Request.Builder()
            .headers(Headers.of(headers.toMap()))
            .url(url)
            .get()
            .build()
        return httpClient.newCall(request).execute()
    }

    fun post(url: String): Response {
        val request = Request.Builder()
            .url(url)
            .post(RequestBody.create(null, ""))
            .build()
        return httpClient.newCall(request).execute()
    }

    fun put(url: String, vararg headers: Pair<String, String>): Response {
        val request = Request.Builder()
            .headers(Headers.of(headers.toMap()))
            .url(url)
            .put(RequestBody.create(null, ""))
            .build()
        return httpClient.newCall(request).execute()
    }

    fun Response.json() = body()?.string()

}

