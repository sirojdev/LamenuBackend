package mimsoft.io.features.address.repository

import mimsoft.io.features.address.ADDRESS_TABLE_NAME
import mimsoft.io.features.address.AddressDto
import mimsoft.io.features.address.AddressMapper
import mimsoft.io.features.address.AddressTable
import mimsoft.io.features.category.repository.CategoryRepositoryImpl
import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager

object AddressRepositoryImpl : AddressRepository {

    private val repository: BaseRepository = DBManager
    private val mapper = AddressMapper

    override suspend fun getAll(clientId: Long?): List<AddressDto?> {
        val data = repository.getPageData(
            dataClass = AddressTable::class,
            where = mapOf("client_id" to clientId as Any),
            tableName = ADDRESS_TABLE_NAME
        )?.data
        return data?.map { mapper.toAddressDto(it) } ?: emptyList()
    }

    override suspend fun get(id: Long?): AddressDto? {
        val data = repository.getData(dataClass = AddressTable::class, id = id, tableName = ADDRESS_TABLE_NAME)
        return mapper.toAddressDto(data.firstOrNull() as AddressTable?)
    }

    override suspend fun add(addressDto: AddressDto?): Long? =
        repository.postData(
            dataClass = AddressTable::class,
            dataObject = mapper.toAddressTable(addressDto),
            tableName = ADDRESS_TABLE_NAME
        )


    override suspend fun update(addressDto: AddressDto?): Boolean =
        repository.updateData(
            dataClass = AddressTable::class,
            dataObject = mapper.toAddressTable(addressDto),
            tableName = ADDRESS_TABLE_NAME
        )

    override suspend fun delete(id: Long?): Boolean =
        repository.deleteData(tableName = ADDRESS_TABLE_NAME, whereValue = id)
}