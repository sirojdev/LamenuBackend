package mimsoft.io.entities.option.repository

import mimsoft.io.entities.option.OptionTable

interface OptionRepository {

    suspend fun getSubOptions(id: Long?): List<OptionTable?>

    suspend fun getAll(): List<OptionTable?>
    suspend fun get(id: Long?): OptionTable?
    suspend fun add(optionTable: OptionTable?): Long?
    suspend fun update(optionTable: OptionTable?): Boolean
    suspend fun delete(id: Long?): Boolean
}