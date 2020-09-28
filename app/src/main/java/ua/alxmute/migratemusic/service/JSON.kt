package ua.alxmute.migratemusic.service

import com.google.gson.Gson
import kotlin.reflect.KClass

object JSON {

    private val gson = Gson()

    fun <T : Any> fromJson(json: String?, clazz: KClass<T>): T = gson.fromJson(json, clazz.java)

}