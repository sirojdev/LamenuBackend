package mimsoft.io.entities.option.repository

import mimsoft.io.entities.option.OptionDto
import mimsoft.io.entities.option.OptionTable

interface OptionRepository {

    suspend fun getSubOptions(id: Long?): List<OptionDto?>

    suspend fun getAll(): List<OptionDto?>
    suspend fun get(id: Long?): OptionDto?
    suspend fun add(optionTable: OptionTable?): Long?
    suspend fun update(optionTable: OptionTable?): Boolean
    suspend fun delete(id: Long?): Boolean
}