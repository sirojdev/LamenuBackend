package mimsoft.io.entities.address

import com.google.gson.Gson

object AddressMapper {
    fun toAddressTable(addressDto: AddressDto?): AddressTable? {
        return if (addressDto == null) null
        else AddressTable(
            id = addressDto.id,
            type = addressDto.type?.name,
            name = addressDto.name,
            details = addressDto.details.toString(),
            description = addressDto.description,
            latitude = addressDto.latitude,
            longitude = addressDto.longitude
        )
    }

    fun toAddressDto(addressTable: AddressTable?): AddressDto? {
        return if (addressTable == null) null
        else {
            val jsonString = addressTable.details

            val tableDetails = Gson().fromJson(jsonString, Details::class.java)
            AddressDto(
                id = addressTable.id,
                type = addressTable.type?.let { AddressType.valueOf(it) },
                name = addressTable.name,
                details = tableDetails,
                description = addressTable.description,
                latitude = addressTable.latitude,
                longitude = addressTable.longitude
            )
        }
    }


}