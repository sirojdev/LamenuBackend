package mimsoft.io.features.order

import mimsoft.io.features.order.OrderUtils.generateQuery

fun main() {
    val tableNames = listOf(
        mapOf(
            "order" to listOf("name_uz"),
            "branch" to listOf("name_uz", "name_eng", "name_ru"),
            "payment" to listOf("icon", "name")
        )
    )
    val query = generateQuery(null, tableNames)
}