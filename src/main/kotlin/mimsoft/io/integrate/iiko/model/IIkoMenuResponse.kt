package mimsoft.io.integrate.iiko.model

import com.google.gson.annotations.SerializedName

data class IIkoMenuResponse(
  @SerializedName("correlationId") var correlationId: String? = null,
  @SerializedName("groups") var groups: ArrayList<Groups>? = null,
  @SerializedName("productCategories") var productCategories: ArrayList<ProductCategories>? = null,
  @SerializedName("products") var products: ArrayList<Products>? = null,
  @SerializedName("sizes") var sizes: ArrayList<Sizes>? = null,
  @SerializedName("revision") var revision: Int? = null
)

data class IIkoProductsResponse(
  @SerializedName("correlationId") var correlationId: String? = null,
  @SerializedName("products") var products: ArrayList<Products>? = null,
  @SerializedName("revision") var revision: Long? = null
)

data class IIkoProductsModifiersResponse(
  @SerializedName("correlationId") var correlationId: String? = null,
  @SerializedName("products") var products: ArrayList<Products>? = null,
  @SerializedName("revision") var revision: Long? = null
)

data class IIkoCategoryResponse(
  @SerializedName("productCategories") var productCategories: ArrayList<ProductCategories>? = null,
  @SerializedName("revision") var revision: Long? = null
)

data class IIkoGroupResponse(
  @SerializedName("correlationId") var correlationId: String? = null,
  @SerializedName("groups") var groups: ArrayList<Groups>? = null,
  @SerializedName("revision") var revision: Long? = null
)

data class Groups(
  @SerializedName("productCategories") var productCategories: ArrayList<ProductCategories>? = null,
  @SerializedName("imageLinks") var imageLinks: ArrayList<String>? = null,
  @SerializedName("parentGroup") var parentGroup: String? = null,
  @SerializedName("order") var order: Int? = null,
  @SerializedName("isIncludedInMenu") var isIncludedInMenu: Boolean? = null,
  @SerializedName("isGroupModifier") var isGroupModifier: Boolean? = null,
  @SerializedName("id") var id: String? = null,
  @SerializedName("code") var code: String? = null,
  @SerializedName("name") var name: String? = null,
  @SerializedName("description") var description: String? = null,
  @SerializedName("additionalInfo") var additionalInfo: String? = null,
  @SerializedName("tags") var tags: ArrayList<String>? = null,
  @SerializedName("isDeleted") var isDeleted: Boolean? = null,
  @SerializedName("seoDescription") var seoDescription: String? = null,
  @SerializedName("seoText") var seoText: String? = null,
  @SerializedName("seoKeywords") var seoKeywords: String? = null,
  @SerializedName("seoTitle") var seoTitle: String? = null
)

data class ProductCategories(
  @SerializedName("id") var id: String? = null,
  @SerializedName("name") var name: String? = null,
  @SerializedName("isDeleted") var isDeleted: Boolean? = null
)

data class Price(
  @SerializedName("currentPrice") var currentPrice: Int? = null,
  @SerializedName("isIncludedInMenu") var isIncludedInMenu: Boolean? = null,
  @SerializedName("nextPrice") var nextPrice: Int? = null,
  @SerializedName("nextIncludedInMenu") var nextIncludedInMenu: Boolean? = null,
  @SerializedName("nextDatePrice") var nextDatePrice: String? = null
)

data class SizePrices(
  @SerializedName("sizeId") var sizeId: String? = null,
  @SerializedName("price") var price: Price? = Price()
)

data class Modifiers(
  val productId: String? = null,
  val amount: Double? = null,
  @SerializedName("id") var id: String? = null,
  @SerializedName("defaultAmount") var defaultAmount: Int? = null,
  @SerializedName("minAmount") var minAmount: Int? = null,
  @SerializedName("maxAmount") var maxAmount: Int? = null,
  @SerializedName("required") var required: Boolean? = null,
  @SerializedName("hideIfDefaultAmount") var hideIfDefaultAmount: Boolean? = null,
  @SerializedName("splittable") var splittable: Boolean? = null,
  @SerializedName("freeOfChargeAmount") var freeOfChargeAmount: Int? = null
)

data class ChildModifiers(
  @SerializedName("id") var id: String? = null,
  @SerializedName("defaultAmount") var defaultAmount: Int? = null,
  @SerializedName("minAmount") var minAmount: Int? = null,
  @SerializedName("maxAmount") var maxAmount: Int? = null,
  @SerializedName("required") var required: Boolean? = null,
  @SerializedName("hideIfDefaultAmount") var hideIfDefaultAmount: Boolean? = null,
  @SerializedName("splittable") var splittable: Boolean? = null,
  @SerializedName("freeOfChargeAmount") var freeOfChargeAmount: Int? = null
)

data class GroupModifiers(
  @SerializedName("id") var id: String? = null,
  @SerializedName("minAmount") var minAmount: Int? = null,
  @SerializedName("maxAmount") var maxAmount: Int? = null,
  @SerializedName("required") var required: Boolean? = null,
  @SerializedName("childModifiersHaveMinMaxRestrictions")
  var childModifiersHaveMinMaxRestrictions: Boolean? = null,
  @SerializedName("childModifiers") var childModifiers: ArrayList<ChildModifiers>? = null,
  @SerializedName("hideIfDefaultAmount") var hideIfDefaultAmount: Boolean? = null,
  @SerializedName("defaultAmount") var defaultAmount: Int? = null,
  @SerializedName("splittable") var splittable: Boolean? = null,
  @SerializedName("freeOfChargeAmount") var freeOfChargeAmount: Int? = null
)

data class Sizes(
  @SerializedName("id") var id: String? = null,
  @SerializedName("name") var name: String? = null,
  @SerializedName("priority") var priority: Int? = null,
  @SerializedName("isDefault") var isDefault: Boolean? = null
)

data class Products(
  @SerializedName("fatAmount") var fatAmount: Int? = null,
  @SerializedName("proteinsAmount") var proteinsAmount: Int? = null,
  @SerializedName("carbohydratesAmount") var carbohydratesAmount: Int? = null,
  @SerializedName("energyAmount") var energyAmount: Int? = null,
  @SerializedName("fatFullAmount") var fatFullAmount: Int? = null,
  @SerializedName("proteinsFullAmount") var proteinsFullAmount: Int? = null,
  @SerializedName("carbohydratesFullAmount") var carbohydratesFullAmount: Int? = null,
  @SerializedName("energyFullAmount") var energyFullAmount: Int? = null,
  @SerializedName("weight") var weight: Int? = null,
  @SerializedName("groupId") var groupId: String? = null,
  @SerializedName("productCategoryId") var productCategoryId: String? = null,
  @SerializedName("type") var type: String? = null,
  @SerializedName("orderItemType") var orderItemType: String? = null,
  @SerializedName("modifierSchemaId") var modifierSchemaId: String? = null,
  @SerializedName("modifierSchemaName") var modifierSchemaName: String? = null,
  @SerializedName("splittable") var splittable: Boolean? = null,
  @SerializedName("measureUnit") var measureUnit: String? = null,
  @SerializedName("sizePrices") var sizePrices: ArrayList<SizePrices>? = null,
  @SerializedName("modifiers") var modifiers: ArrayList<Modifiers>? = null,
  @SerializedName("groupModifiers") var groupModifiers: ArrayList<GroupModifiers>? = null,
  @SerializedName("imageLinks") var imageLinks: ArrayList<String>? = null,
  @SerializedName("doNotPrintInCheque") var doNotPrintInCheque: Boolean? = null,
  @SerializedName("parentGroup") var parentGroup: String? = null,
  @SerializedName("order") var order: Int? = null,
  @SerializedName("fullNameEnglish") var fullNameEnglish: String? = null,
  @SerializedName("useBalanceForSell") var useBalanceForSell: Boolean? = null,
  @SerializedName("canSetOpenPrice") var canSetOpenPrice: Boolean? = null,
  @SerializedName("paymentSubject") var paymentSubject: String? = null,
  @SerializedName("id") var id: String? = null,
  @SerializedName("code") var code: String? = null,
  @SerializedName("name") var name: String? = null,
  @SerializedName("description") var description: String? = null,
  @SerializedName("additionalInfo") var additionalInfo: String? = null,
  @SerializedName("tags") var tags: ArrayList<String>? = null,
  @SerializedName("isDeleted") var isDeleted: Boolean? = null,
  @SerializedName("seoDescription") var seoDescription: String? = null,
  @SerializedName("seoText") var seoText: String? = null,
  @SerializedName("seoKeywords") var seoKeywords: String? = null,
  @SerializedName("seoTitle") var seoTitle: String? = null
)
