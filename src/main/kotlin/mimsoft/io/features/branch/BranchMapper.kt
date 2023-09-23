package mimsoft.io.features.branch

import mimsoft.io.utils.TextModel

object BranchMapper {
    fun toBranchTable(branchDto: BranchDto?): BranchTable? {
        return if (branchDto == null) null
        else {
            BranchTable(
                id = branchDto.id,
                nameUz = branchDto.name?.uz,
                nameRu = branchDto.name?.ru,
                nameEng = branchDto.name?.eng,
                open = branchDto.open,
                close = branchDto.close,
                longitude = branchDto.longitude,
                latitude = branchDto.latitude,
                address = branchDto.address,
                merchantId = branchDto.merchantId,
                joinPosterId = branchDto.joinPosterId,
                jowiId = branchDto.jowiId,
                iikoId = branchDto.iikoId,
            )
        }
    }

    fun toBranchDto(branchTable: BranchTable?): BranchDto? {
        return if (branchTable == null) null
        else BranchDto(
            id = branchTable.id,
            name = TextModel(
                uz = branchTable.nameUz,
                ru = branchTable.nameRu,
                eng = branchTable.nameEng,
            ),
            open = branchTable.open,
            close = branchTable.close,
            longitude = branchTable.longitude,
            latitude = branchTable.latitude,
            address = branchTable.address,
            merchantId = branchTable.merchantId,
            joinPosterId = branchTable.joinPosterId,
            jowiId = branchTable.jowiId,
            iikoId = branchTable.iikoId,
        )
    }
}