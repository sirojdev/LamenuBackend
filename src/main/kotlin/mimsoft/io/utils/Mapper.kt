package mimsoft.io.utils

import mimsoft.io.entities.product.ProductDto
import mimsoft.io.entities.product.ProductTable
import mimsoft.io.plugins.GSON
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

object Mapper {

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
            tableClass.memberProperties.find { it.name == propertyName }?.get(table)?.let {
                if (it is String) {
                    it.trim()
                } else if (it is TextModel) {
                    val textModel = TextModel("", "", "")
                    tableClass.memberProperties.filter { it.name.startsWith(propertyName ?: "") }.forEach { prop ->
                        val lang = prop.name.takeLast(2)
                        when (lang) {
                            "Uz" -> textModel.uz = prop.get(table).toString()
                            "Ru" -> textModel.ru = prop.get(table).toString()
                            "Eng" -> textModel.eng = prop.get(table).toString()
                        }
                    }
                    textModel
                } else {
                    it
                }
            } ?: getDefaultParameterValue(parameter, dtoClass)
        }
        return dtoConstructor.callBy(dtoValues.filterKeys { it != null }.mapKeys {
            dtoConstructor.parameters.find { p -> p.name == it.key }!!
        }) as R
    }


    fun getDefaultParameterValue(parameter: KParameter, clazz: KClass<*>): Any? {
        val defaultValueConstructor = clazz.primaryConstructor?.parameters?.firstOrNull { it == parameter }
        return defaultValueConstructor?.type?.classifier?.let { classifier ->
            when (classifier) {
                Int::class -> 0
                Long::class -> 0L
                Float::class -> 0f
                Double::class -> 0.0
                String::class -> ""
                else -> null
            }
        }
    }

}

fun main() {
    val productTable = ProductTable(
        id = 1,
        menuId = 4,
        nameUz = "6kuhkhu",
        nameRu = "hbkljbkj",
        nameEng = "jhbkjbhk",
        descriptionUz = "6kuhkhu",
        descriptionRu = "hbkljbkj",
        descriptionEng = "jhbkjbhk",
        image = "jhvkhv",
        costPrice = 458.54
    )
    val productDto = Mapper.toDto<ProductTable, ProductDto>(productTable)

    println(GSON.toJson(productDto))
}