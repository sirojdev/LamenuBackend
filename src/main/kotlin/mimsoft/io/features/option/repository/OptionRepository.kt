package mimsoft.io.features.option.repository

import mimsoft.io.features.option.OptionDto
import mimsoft.io.features.option.OptionTable

interface OptionRepository {

  suspend fun getSubOptions(id: Long?): List<OptionDto?>

  suspend fun getAll(merchantId: Long?, branchId: Long?): List<OptionTable?>

  suspend fun get(id: Long?, merchantId: Long? = null, branchId: Long? = null): OptionDto?

  suspend fun add(optionTable: OptionTable?): Long?

  suspend fun update(dto: OptionDto): Boolean

  suspend fun delete(id: Long?, merchantId: Long?, branchId: Long?): Boolean
}