package mimsoft.io.entities.address

import mimsoft.io.table.TableTable

interface AddressRepository {
    suspend fun getAll(): List<AddressTable?>
    suspend fun get(id: Long?): AddressTable?
    suspend fun add(addressTable: AddressTable?): Long?
    suspend fun update(addressTable: AddressTable?): Boolean
    suspend fun delete(id: Long?): Boolean
}