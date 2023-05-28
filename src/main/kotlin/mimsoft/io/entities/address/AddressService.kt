package mimsoft.io.entities.address

interface AddressService {
    suspend fun getAll(): List<AddressDto?>
    suspend fun get(id: Long?): AddressDto?
    suspend fun add(addressDto: AddressDto?): Long?
    suspend fun update(addressDto: AddressDto?): Boolean
    suspend fun delete(id: Long?): Boolean
}