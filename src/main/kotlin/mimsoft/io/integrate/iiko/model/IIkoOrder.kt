package mimsoft.io.integrate.iiko.model

import com.google.gson.annotations.SerializedName

data class IIkoOrder(
    @SerializedName("organizationId") var organizationId: String? = null,
    @SerializedName("terminalGroupId") var terminalGroupId: String? = null,
    @SerializedName("createOrderSettings") var createOrderSettings: CreateOrderSettings? = null,
    @SerializedName("order") var order: IIkoOrderItem? = null
)

data class CreateOrderSettings(
    @SerializedName("transportToFrontTimeout") var transportToFrontTimeout: Int? = null,
    @SerializedName("checkStopList") var checkStopList: Boolean? = null
)

data class Coordinates(
    @SerializedName("latitude") var latitude: Double? = null,
    @SerializedName("longitude") var longitude: Double? = null
)

data class Street(
    @SerializedName("classifierId") var classifierId: String? = null,
    @SerializedName("id") var id: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("city") var city: City? = null
)

data class Address(
    @SerializedName("street") var street: Street? = null,
    @SerializedName("index") var index: String? = null,
    @SerializedName("house") var house: String? = null,
    @SerializedName("building") var building: String? = null,
    @SerializedName("flat") var flat: String? = null,
    @SerializedName("entrance") var entrance: String? = null,
    @SerializedName("floor") var floor: String? = null,
    @SerializedName("doorphone") var doorphone: String? = null,
    @SerializedName("regionId") var regionId: String? = null
)

data class DeliveryPoint(
    @SerializedName("coordinates") var coordinates: Coordinates? = null,
    @SerializedName("address") var address: Address? = null,
    @SerializedName("externalCartographyId") var externalCartographyId: String? = null,
    @SerializedName("comment") var comment: String? = null
)

data class Customer(
    @SerializedName("type") var type: String? = null,
    @SerializedName("name") var name: String? = null
)


data class Guests(
    @SerializedName("count") var count: Int? = null,
    @SerializedName("splitBetweenPersons") var splitBetweenPersons: Boolean? = null
)

data class ComboInformation(
    @SerializedName("comboId") var comboId: String? = null,
    @SerializedName("comboSourceId") var comboSourceId: String? = null,
    @SerializedName("comboGroupId") var comboGroupId: String? = null
)

data class Items(
    @SerializedName("productId") var productId: String? = null,
    @SerializedName("price") var price: Double? = null,
    @SerializedName("positionId") var positionId: String? = null,
    @SerializedName("modifiers") var modifiers: MutableList<Modifiers>? = null,
    @SerializedName("type") var type: String? = null,
    @SerializedName("productSizeId") var productSizeId: String? = null,
    @SerializedName("comboInformation") var comboInformation: ComboInformation? = null,
    @SerializedName("comment") var comment: String? = null,
    @SerializedName("amount") var amount: Double? = null
)

data class Combos(
    @SerializedName("id") var id: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("amount") var amount: Int? = null,
    @SerializedName("price") var price: Int? = null,
    @SerializedName("sourceId") var sourceId: String? = null,
    @SerializedName("programId") var programId: String? = null,
    @SerializedName("sizeId") var sizeId: String? = null
)

data class PaymentAdditionalData(
    @SerializedName("type") var type: String? = null
)

data class Payments(
    @SerializedName("paymentTypeKind") var paymentTypeKind: String? = null,
    @SerializedName("sum") var sum: Int? = null,
    @SerializedName("paymentTypeId") var paymentTypeId: String? = null,
    @SerializedName("isProcessedExternally") var isProcessedExternally: Boolean? = null,
    @SerializedName("paymentAdditionalData") var paymentAdditionalData: PaymentAdditionalData? = null,
    @SerializedName("isFiscalizedExternally") var isFiscalizedExternally: Boolean? = null,
    @SerializedName("isPrepay") var isPrepay: Boolean? = null
)


data class Tips(
    @SerializedName("paymentTypeKind") var paymentTypeKind: String? = null,
    @SerializedName("tipsTypeId") var tipsTypeId: String? = null,
    @SerializedName("sum") var sum: Int? = null,
    @SerializedName("paymentTypeId") var paymentTypeId: String? = null,
    @SerializedName("isProcessedExternally") var isProcessedExternally: Boolean? = null,
    @SerializedName("paymentAdditionalData") var paymentAdditionalData: PaymentAdditionalData? = null,
    @SerializedName("isFiscalizedExternally") var isFiscalizedExternally: Boolean? = null,
    @SerializedName("isPrepay") var isPrepay: Boolean? = null
)

data class Card(
    @SerializedName("track") var track: String? = null
)

data class Discounts(
    @SerializedName("type") var type: String? = null
)

data class DiscountsInfo(
    @SerializedName("card") var card: Card? = null,
    @SerializedName("discounts") var discounts: ArrayList<Discounts>? = null
)

data class LoyaltyInfo(
    @SerializedName("coupon") var coupon: String? = null,
    @SerializedName("applicableManualConditions") var applicableManualConditions: ArrayList<String>? = null
)

data class ChequeAdditionalInfo(
    @SerializedName("needReceipt") var needReceipt: Boolean? = null,
    @SerializedName("email") var email: String? = null,
    @SerializedName("settlementPlace") var settlementPlace: String? = null,
    @SerializedName("phone") var phone: String? = null
)

data class ExternalDataOrder(
    @SerializedName("key") var key: String? = null,
    @SerializedName("value") var value: String? = null,
    @SerializedName("isPublic") var isPublic: Boolean? = null
)

data class IIkoOrderItem(
    @SerializedName("id") var id: String? = null,
    @SerializedName("externalNumber") var externalNumber: String? = null,
    @SerializedName("tableIds") var tablesIds: String? = null,
    @SerializedName("menuId") var menuId: String? = null,
    @SerializedName("customer") var customer: Customer? = null,
    @SerializedName("phone") var phone: String? = null,
    @SerializedName("completeBefore") var completeBefore: String? = null,
    @SerializedName("orderTypeId") var orderTypeId: String? = null,
    @SerializedName("orderServiceType") var orderServiceType: String? = null,
    @SerializedName("deliveryPoint") var deliveryPoint: DeliveryPoint? = null,
    @SerializedName("comment") var comment: String? = null,
    @SerializedName("guests") var guests: Guests? = null,
    @SerializedName("marketingSourceId") var marketingSourceId: String? = null,
    @SerializedName("operatorId") var operatorId: String? = null,
    @SerializedName("items") var items: ArrayList<Items>? = null,
    @SerializedName("combos") var combos: ArrayList<Combos>? = null,
    @SerializedName("payments") var payments: ArrayList<Payments>? = null,
    @SerializedName("tips") var tips: ArrayList<Tips>? = null,
    @SerializedName("sourceKey") var sourceKey: String? = null,
    @SerializedName("discountsInfo") var discountsInfo: DiscountsInfo? = null,
    @SerializedName("loyaltyInfo") var loyaltyInfo: LoyaltyInfo? = null,
    @SerializedName("chequeAdditionalInfo") var chequeAdditionalInfo: ChequeAdditionalInfo? = null,
    @SerializedName("externalData") var externalData: ArrayList<ExternalDataOrder>? = null
)