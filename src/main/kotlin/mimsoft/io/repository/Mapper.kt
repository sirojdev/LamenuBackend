package mimsoft.io.repository

import mimsoft.io.utils.TextModel
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

object Mapper {

    inline fun <reified T : Any, reified R : Any> toTable(dto: T): R? {
        val dtoClass = T::class
        dtoClass.simpleName?.replace("Dto", "Table") ?: return null
        val tableClass = try {
            Class.forName(dtoClass.qualifiedName!!.replace("Dto", "Table")).kotlin
        } catch (e: ClassNotFoundException) {
            return null
        }

        if (tableClass != R::class) return null

        val tableConstructor = tableClass.primaryConstructor ?: return null
        val tableParameters = tableConstructor.parameters.associateBy { it.name }
        val tableValues = tableParameters.mapValues { (name, parameter) ->
            val propertyName = name?.replace("Uz", "")?.replace("Ru", "")?.replace("En", "")
            println("\nmapper propertyName-->$propertyName")
            dtoClass.memberProperties.find { it.name == propertyName }?.get(dto)?.let {
                when (it) {

                    is TextModel -> {
                        when (name) {
                            "${propertyName}Uz" -> it.uz
                            "${propertyName}Ru" -> it.ru
                            "${propertyName}En" -> it.eng
                            else -> null
                        }
                    }

                    else -> {
                        println("\nmapper it-->$it")
                        it
                    }
                }
            } ?: getDefaultParameterValue(parameter, tableClass)
        }
        return tableConstructor.callBy(tableValues.filterKeys { it != null }.mapKeys {
            tableConstructor.parameters.find { p -> p.name == it.key }!!
        }) as? R
    }
    inline fun <reified T : Any, reified R : Any> toDto(table: T?): R? {
        val tableClass = T::class
        val dtoClass = try {
            Class.forName(tableClass.qualifiedName!!.replace("Table", "Dto")).kotlin
        } catch (e: ClassNotFoundException) {
            return null
        }

        val dtoConstructor = dtoClass.primaryConstructor ?: return null
        val dtoParameters = dtoConstructor.parameters.associateBy { it.name }
        val dtoValues = dtoParameters.mapValues { (name, parameter) ->
            val propertyName = name?.replace("Uz", "")?.replace("Ru", "")?.replace("En", "")
            val property = tableClass.memberProperties.find { it.name == propertyName }

            if (parameter.type.classifier == TextModel::class) {
                val textModel = TextModel("", "", "")
                tableClass.memberProperties.filter { it.name.startsWith(propertyName ?: "") }.forEach { prop ->
                    val lang = prop.name.takeLast(2)
                    when (lang) {
                        "Uz" -> textModel.uz = table?.let { prop.get(it).toString() }
                        "Ru" -> textModel.ru = table?.let { prop.get(it).toString() }
                        "En" -> textModel.eng = table?.let { prop.get(it).toString() }
                    }
                }
                textModel
            } else {
                table?.let {
                    property?.get(it)?.let {
                        if (it is String) {
                            it.trim()
                        } else {
                            it
                        }
                    }
                } ?: getDefaultParameterValue(parameter, dtoClass)
            }
        }

        return dtoConstructor.callBy(dtoValues.filterKeys { it != null }.mapKeys {
            dtoConstructor.parameters.find { p -> p.name == it.key }!!
        }) as R
    }

    fun getDefaultParameterValue(parameter: KParameter, clazz: KClass<*>): Any? {
        val defaultValueConstructor = clazz.primaryConstructor?.parameters?.firstOrNull { it == parameter }
        return defaultValueConstructor?.type?.classifier?.let { classifier ->
            when (classifier) {
                Int::class -> null
                Long::class -> null
                Float::class -> null
                Double::class -> null
                String::class -> null
                else -> null
            }
        }
    }

}