package mimsoft.io.features.product.repository

import mimsoft.io.features.product.ProductDto
import mimsoft.io.features.product.ProductInfoDto
import mimsoft.io.features.product.ProductTable

interface ProductRepository {
    suspend fun getAllProductInfo(merchantId: Long?, branchId: Long?): List<ProductInfoDto?>
    suspend fun getProductInfo(merchantId: Long?, id: Long?, branchId: Long?): ProductInfoDto?
    suspend fun getAll(merchantId: Long?, search: String? = null, branchId: Long?): List<ProductDto?>
    suspend fun get(id: Long?, merchantId: Long? = null): ProductTable?
    suspend fun add(productTable: ProductTable?): Long?
    suspend fun update(dto: ProductDto?): Boolean
    suspend fun delete(id: Long?, merchantId: Long?, branchId: Long?): Boolean
//    suspend fun getProductInfo(id: Long, merchantId: Long?): ProductInfoDto?
}