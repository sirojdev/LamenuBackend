package mimsoft.io.features.address.repository

import mimsoft.io.features.address.AddressDto

interface AddressRepository {
    suspend fun getAll(clientId: Long?): List<AddressDto?>
    suspend fun get(id: Long?): AddressDto?
    suspend fun add(addressDto: AddressDto?): Long?
    suspend fun update(addressDto: AddressDto?): Boolean
    suspend fun delete(clientId: Long?, merchantId: Long?, id: Long?): Boolean
}