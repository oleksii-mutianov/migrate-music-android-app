package ua.alxmute.migratemusic.service

import com.google.gson.Gson

object JSON {

    val gson = Gson()

    inline fun <reified T> fromJson(json: String?): T = gson.fromJson(json, T::class.java)

}