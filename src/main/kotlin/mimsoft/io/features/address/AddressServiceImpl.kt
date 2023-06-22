package mimsoft.io.features.address

import mimsoft.io.repository.BaseRepository
import mimsoft.io.repository.DBManager
object AddressServiceImpl : AddressService {

    private val repository: BaseRepository = DBManager
    private val mapper = AddressMapper

    override suspend fun getAll(): List<AddressDto?> =
        repository.getData(dataClass = AddressTable::class, tableName = "address")
            .filterIsInstance<AddressTable?>().map { mapper.toAddressDto(it) }

    override suspend fun get(id: Long?): AddressDto? {
        val data = repository.getData(dataClass = AddressTable::class, id = id, tableName = "address")

        println("\nget: $data")
        return mapper.toAddressDto(data.firstOrNull() as AddressTable?)
    }
    override suspend fun add(addressDto: AddressDto?): Long? =
        repository.postData(
            dataClass = AddressTable::class,
            dataObject = mapper.toAddressTable(addressDto),
            tableName = "address")


    override suspend fun update(addressDto: AddressDto?): Boolean =
        repository.updateData(
            dataClass = AddressTable::class,
            dataObject = mapper.toAddressTable(addressDto),
            tableName = "address")

    override suspend fun delete(id: Long?) : Boolean =
        repository.deleteData(tableName = "address", whereValue = id)
}