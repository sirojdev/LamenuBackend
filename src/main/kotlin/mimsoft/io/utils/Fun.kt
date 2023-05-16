package mimsoft.io.utils

import mimsoft.io.utils.Mapper.getDefaultParameterValue
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

inline fun <reified T : Any, reified R : Any> toDto(table: T): R? {
    val tableClass = T::class
    val dtoClass = try {
        Class.forName(tableClass.qualifiedName!!.replace("Table", "Dto")).kotlin
    } catch (e: ClassNotFoundException) {
        return null
    }


    val dtoConstructor = dtoClass.primaryConstructor ?: return null
    val dtoParameters = dtoConstructor.parameters.associateBy { it.name }
    val dtoValues = dtoParameters.mapValues { (name, parameter) ->
        val propertyName = name?.replace("Uz", "")?.replace("Ru", "")?.replace("Eng", "")
        val property = tableClass.memberProperties.find { it.name == propertyName }

        if (property != null && property.returnType.classifier == TextModel::class) {
            toTextModel(tableClass, table, propertyName!!)
        } else {
            property?.get(table)?.let {
                if (it is String) {
                    it.trim()
                } else {
                    it
                }
            } ?: Mapper.getDefaultParameterValue(parameter, dtoClass)
        }
    }

    return dtoConstructor.callBy(dtoValues.filterKeys { it != null }.mapKeys {
        dtoConstructor.parameters.find { p -> p.name == it.key }!!
    }) as R
}

fun toTextModel(tableClass: KClass<*>, table: Any, propertyName: String): TextModel {
    val textModel = TextModel("", "", "")
    tableClass.memberProperties.filter { it.name.startsWith(propertyName) }.forEach { prop ->
        val lang = prop.name.takeLast(2)
        when (lang) {
            "Uz" -> textModel.uz = prop.get(table as Nothing)?.toString() ?: ""
            "Ru" -> textModel.ru = prop.get(table as Nothing)?.toString() ?: ""
            "Eng" -> textModel.eng = prop.get(table as Nothing)?.toString() ?: ""
        }
    }
    return textModel
}

inline fun <reified T : Any, reified R : Any> tooDto(table: T): R? {
    val tableClass = T::class
    val dtoClass = try {
        Class.forName(tableClass.qualifiedName!!.replace("Table", "Dto")).kotlin
    } catch (e: ClassNotFoundException) {
        return null
    }

    val dtoConstructor = dtoClass.primaryConstructor ?: return null
    val dtoParameters = dtoConstructor.parameters.associateBy { it.name }
    val dtoValues = dtoParameters.mapValues { (name, parameter) ->
        val propertyName = name?.replace("Uz", "")?.replace("Ru", "")?.replace("Eng", "")
        val property = tableClass.memberProperties.find { it.name == propertyName }

        if (property != null && property.returnType.classifier == TextModel::class) {
            val textModel = TextModel("", "", "")
            tableClass.memberProperties.filter { it.name.startsWith(propertyName ?: "") }.forEach { prop ->
                val lang = prop.name.takeLast(2)
                when (lang) {
                    "z" -> textModel.uz = prop.get(table).toString()
                    "Ru" -> textModel.ru = prop.get(table).toString()
                    "Eng" -> textModel.eng = prop.get(table).toString()
                }
            }
            textModel
        } else {
            property?.get(table)?.let {
                if (it is String) {
                    it.trim()
                } else {
                    it
                }
            } ?: getDefaultParameterValue(parameter, dtoClass)
        }
    }
    return dtoConstructor.callBy(dtoValues.filterKeys { it != null }.mapKeys {
        dtoConstructor.parameters.find { p -> p.name == it.key }!!
    }) as R
}

inline fun <reified T : Any> toTable(dto: T): Any? {
    val dtoClass = T::class
    val tableName = dtoClass.simpleName?.replace("Dto", "Table") ?: return null
    val tableClass = try {
        Class.forName(dtoClass.qualifiedName!!.replace("Dto", "Table")).kotlin
    } catch (e: ClassNotFoundException) {
        return null
    }

    val tableConstructor = tableClass.primaryConstructor ?: return null
    val tableParameters = tableConstructor.parameters.associateBy { it.name }
    val tableValues = tableParameters.mapValues { (name, parameter) ->
        val propertyName = name?.replace("Uz", "")?.replace("Ru", "")?.replace("Eng", "")
        dtoClass.memberProperties.find { it.name == propertyName }?.get(dto)?.let {
            if (it is String) {
                it.trim()
            } else if (it is TextModel) {
                when (name) {
                    "${propertyName}Uz" -> it.uz
                    "${propertyName}Ru" -> it.ru
                    "${propertyName}Eng" -> it.eng
                    else -> null
                }
            } else {
                it
            }
        } ?: getDefaultParameterValue(parameter, tableClass)
    }
    return tableConstructor.callBy(tableValues.filterKeys { it != null }.mapKeys {
        tableConstructor.parameters.find { p -> p.name == it.key }!! })
}


fun main() {
    val a: Map<Int, String> = mapOf(1 to "a", 2 to "b", 3 to "c")
    println(a)
}