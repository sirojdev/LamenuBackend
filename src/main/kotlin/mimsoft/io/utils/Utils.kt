package mimsoft.io.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import mimsoft.io.utils.principal.Role
import kotlin.reflect.KClass

fun <T> gsonToList(json: String?, clazz: Class<T>): List<T> {
    val gson = Gson()
    val type = TypeToken.getParameterized(List::class.java, clazz).type
        return gson.fromJson(json, type) ?: emptyList()
}

fun Any?.toJson(): String? = Gson().toJson(this)
