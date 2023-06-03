package mimsoft.io.features.address

import com.google.gson.Gson

object AddressMapper {
    fun toAddressTable(addressDto: mimsoft.io.features.address.AddressDto?): mimsoft.io.features.address.AddressTable? {
        return if (addressDto == null) null
        else mimsoft.io.features.address.AddressTable(
            id = addressDto.id,
            type = addressDto.type?.name,
            name = addressDto.name,
            details = addressDto.details.toString(),
            description = addressDto.description,
            latitude = addressDto.latitude,
            longitude = addressDto.longitude
        )
    }

    fun toAddressDto(addressTable: mimsoft.io.features.address.AddressTable?): mimsoft.io.features.address.AddressDto? {
        return if (addressTable == null) null
        else {
            val jsonString = addressTable.details

            val tableDetails = Gson().fromJson(jsonString, mimsoft.io.features.address.Details::class.java)
            mimsoft.io.features.address.AddressDto(
                id = addressTable.id,
                type = addressTable.type?.let { mimsoft.io.features.address.AddressType.valueOf(it) },
                name = addressTable.name,
                details = tableDetails,
                description = addressTable.description,
                latitude = addressTable.latitude,
                longitude = addressTable.longitude
            )
        }
    }


}