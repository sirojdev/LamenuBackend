package mimsoft.io.integrate.iiko.model

data class IIkoProductResponse(
  val correlationId: String? = null,
  val groups: List<IIkoGroup>? = null,
  val productCategories: List<IIkoProductCategory>? = null,
  val products: List<IIkoProduct>? = null,
  val sizes: List<IIkoSize>? = null,
  val revision: Long? = null
)

data class IIkoGroup(
  val imageLinks: List<String>? = null,
  val parentGroup: String? = null,
  val order: Int? = null,
  val isIncludedInMenu: Boolean? = null,
  val isGroupModifier: Boolean? = null,
  val id: String? = null,
  val code: String? = null,
  val name: String? = null,
  val description: String? = null,
  val additionalInfo: String? = null,
  val tags: List<String>? = null,
  val isDeleted: Boolean? = null,
  val seoDescription: String? = null,
  val seoText: String? = null,
  val seoKeywords: String? = null,
  val seoTitle: String? = null,
)

data class IIkoProductCategory(
  val id: String? = null,
  val name: String? = null,
  val isDeleted: Boolean?
)

data class IIkoProduct(
  val fatAmount: Double? = null,
  val proteinsAmount: Double? = null,
  val carbohydratesAmount: Double? = null,
  val energyAmount: Double? = null,
  val fatFullAmount: Double? = null,
  val proteinsFullAmount: Double? = null,
  val carbohydratesFullAmount: Double? = null,
  val energyFullAmount: Double? = null,
  val weight: Double? = null,
  val groupId: String? = null,
  val productCategoryId: String? = null,
  val type: String? = null,
  val orderItemType: String? = null,
  val modifierSchemaId: String? = null,
  val modifierSchemaName: String? = null,
  val splittable: Boolean? = null,
  val measureUnit: String? = null,
)

data class IIkoSize(val id: String? = null, val name: String? = null)
