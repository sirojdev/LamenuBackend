package mimsoft.io.features.address

interface AddressService {
    suspend fun getAll(): List<mimsoft.io.features.address.AddressDto?>
    suspend fun get(id: Long?): mimsoft.io.features.address.AddressDto?
    suspend fun add(addressDto: mimsoft.io.features.address.AddressDto?): Long?
    suspend fun update(addressDto: mimsoft.io.features.address.AddressDto?): Boolean
    suspend fun delete(id: Long?): Boolean
}