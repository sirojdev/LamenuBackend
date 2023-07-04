package mimsoft.io.features.pantry

import mimsoft.io.features.branch.BranchDto
import mimsoft.io.features.product.ProductDto

object PantryMapper {
    fun toTable(pantryDto: PantryDto): PantryTable?{
        return PantryTable(
            id = pantryDto.id,
            merchantId = pantryDto.merchantId,
            branchId = pantryDto.branch?.id,
            productId = pantryDto.product?.id,
            count = pantryDto.count
        )
    }

    fun toDto(pantryTable: PantryTable?): PantryDto{
        return PantryDto(
            id = pantryTable?.id,
            merchantId = pantryTable?.merchantId,
            branch = BranchDto(
                id = pantryTable?.branchId,
            ),
            product = ProductDto(
                id = pantryTable?.productId
            ),
            count = pantryTable?.count
        )
    }
}