package mimsoft.io.entities.branch

import mimsoft.io.utils.TextModel

object BranchMapper {
    fun toBranchTable(branchDto: BranchDto?): BranchTable? {
        return if (branchDto==null) null
        else {
            BranchTable(
                id = branchDto.id,
                nameUz = branchDto.name?.uz,
                nameRu = branchDto.name?.ru,
                nameEn = branchDto.name?.eng,
                longitude = branchDto.longitude,
                latitude = branchDto.latitude,
                address = branchDto.address
            )
        }
    }

    fun toBranchDto(branchTable: BranchTable?): BranchDto? {
        return if (branchTable==null) null
        else BranchDto(
            id = branchTable.id,
            name = TextModel(
                uz = branchTable.nameUz,
                ru = branchTable.nameRu,
                eng = branchTable.nameEn,
            ),
            longitude = branchTable.longitude,
            latitude = branchTable.latitude,
            address = branchTable.address
        )
    }
}