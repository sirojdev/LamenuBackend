package mimsoft.io.integrate.iiko.model

import com.google.gson.annotations.SerializedName

data class Webhook(
  @SerializedName("eventType") var eventType: String? = null,
  @SerializedName("eventTime") var eventTime: String? = null,
  @SerializedName("organizationId") var organizationId: String? = null,
  @SerializedName("correlationId") var correlationId: String? = null,
  @SerializedName("eventInfo") var eventInfo: EventInfo? = null
)

data class Order(
  @SerializedName("parentDeliveryId") var parentDeliveryId: String? = null,
  @SerializedName("customer") var customer: Customer? = null,
  @SerializedName("phone") var phone: String? = null,
  @SerializedName("deliveryPoint") var deliveryPoint: DeliveryPoint? = null,
  @SerializedName("status") var status: String? = null,
  @SerializedName("cancelInfo") var cancelInfo: CancelInfo? = null,
  @SerializedName("courierInfo") var courierInfo: CourierInfo? = null,
  @SerializedName("completeBefore") var completeBefore: String? = null,
  @SerializedName("whenCreated") var whenCreated: String? = null,
  @SerializedName("whenConfirmed") var whenConfirmed: String? = null,
  @SerializedName("whenPrinted") var whenPrinted: String? = null,
  @SerializedName("whenCookingCompleted") var whenCookingCompleted: String? = null,
  @SerializedName("whenSended") var whenSended: String? = null,
  @SerializedName("whenDelivered") var whenDelivered: String? = null,
  @SerializedName("comment") var comment: String? = null,
  @SerializedName("problem") var problem: Problem? = null,
  @SerializedName("operator") var operator: Operator? = null,
  @SerializedName("marketingSource") var marketingSource: MarketingSource? = null,
  @SerializedName("deliveryDuration") var deliveryDuration: Int? = null,
  @SerializedName("indexInCourierRoute") var indexInCourierRoute: Int? = null,
  @SerializedName("cookingStartTime") var cookingStartTime: String? = null,
  @SerializedName("isDeleted") var isDeleted: Boolean? = null,
  @SerializedName("whenReceivedByApi") var whenReceivedByApi: String? = null,
  @SerializedName("whenReceivedFromFront") var whenReceivedFromFront: String? = null,
  @SerializedName("movedFromDeliveryId") var movedFromDeliveryId: String? = null,
  @SerializedName("movedFromTerminalGroupId") var movedFromTerminalGroupId: String? = null,
  @SerializedName("movedFromOrganizationId") var movedFromOrganizationId: String? = null,
  @SerializedName("externalCourierService")
  var externalCourierService: ExternalCourierService? = null,
  @SerializedName("movedToDeliveryId") var movedToDeliveryId: String? = null,
  @SerializedName("movedToTerminalGroupId") var movedToTerminalGroupId: String? = null,
  @SerializedName("movedToOrganizationId") var movedToOrganizationId: String? = null,
  @SerializedName("menuId") var menuId: String? = null,
  @SerializedName("sum") var sum: Int? = null,
  @SerializedName("number") var number: Int? = null,
  @SerializedName("sourceKey") var sourceKey: String? = null,
  @SerializedName("whenBillPrinted") var whenBillPrinted: String? = null,
  @SerializedName("whenClosed") var whenClosed: String? = null,
  @SerializedName("conception") var conception: Conception? = null,
  @SerializedName("guestsInfo") var guestsInfo: GuestsInfo? = null,
  @SerializedName("items") var items: ArrayList<Items>? = null,
  @SerializedName("combos") var combos: ArrayList<Combos>? = null,
  @SerializedName("payments") var payments: ArrayList<Payments>? = null,
  @SerializedName("tips") var tips: ArrayList<Tips>? = null,
  @SerializedName("discounts") var discounts: ArrayList<Discounts>? = null,
  @SerializedName("terminalGroupId") var terminalGroupId: String? = null,
  @SerializedName("processedPaymentsSum") var processedPaymentsSum: Int? = null,
  @SerializedName("loyaltyInfo") var loyaltyInfo: LoyaltyInfo? = null,
  @SerializedName("externalData") var externalData: ArrayList<ExternalData>? = null,
)

data class EventInfo(
  @SerializedName("id") var id: String? = null,
  @SerializedName("posId") var posId: String? = null,
  @SerializedName("externalNumber") var externalNumber: String? = null,
  @SerializedName("organizationId") var organizationId: String? = null,
  @SerializedName("timestamp") var timestamp: Long? = null,
  @SerializedName("creationStatus") var creationStatus: String? = null,
  @SerializedName("errorInfo") var errorInfo: ErrorInfo? = null,
  @SerializedName("order") var order: Order? = null
)

data class Cause(
  @SerializedName("id") var id: String? = null,
  @SerializedName("name") var name: String? = null
)

data class RemovalType(
  @SerializedName("id") var id: String? = null,
  @SerializedName("name") var name: String? = null
)

data class Size(
  @SerializedName("id") var id: String? = null,
  @SerializedName("name") var name: String? = null
)

data class Deleted(
  @SerializedName("deletionMethod") var deletionMethod: DeletionMethod? = null,
)

data class DeletionMethod(
  @SerializedName("id") var id: String? = null,
  @SerializedName("comment") var comment: String? = null,
  @SerializedName("removalType") var removalType: RemovalType? = null,
)

data class GuestsInfo(
  @SerializedName("count") var count: Int? = null,
  @SerializedName("splitBetweenPersons") var splitBetweenPersons: Boolean? = null
)

data class Conception(
  @SerializedName("id") var id: String? = null,
  @SerializedName("name") var name: String? = null,
  @SerializedName("code") var code: String? = null
)

data class ExternalCourierService(
  @SerializedName("id") var id: String? = null,
  @SerializedName("name") var name: String? = null
)

data class MarketingSource(
  @SerializedName("id") var id: String? = null,
  @SerializedName("name") var name: String? = null
)

data class Operator(
  @SerializedName("id") var id: String? = null,
  @SerializedName("name") var name: String? = null,
  @SerializedName("phone") var phone: String? = null
)

data class Problem(
  @SerializedName("hasProblem") var hasProblem: Boolean? = null,
  @SerializedName("description") var description: String? = null
)

data class CourierInfo(
  @SerializedName("courier") var courier: Courier? = null,
  @SerializedName("isCourierSelectedManually") var isCourierSelectedManually: Boolean? = null
)

data class Courier(
  @SerializedName("id") var id: String? = null,
  @SerializedName("name") var name: String? = null,
  @SerializedName("phone") var phone: String? = null
)

data class CancelInfo(
  @SerializedName("whenCancelled") var whenCancelled: String? = null,
  @SerializedName("cause") var cause: Cause? = null,
  @SerializedName("comment") var comment: String? = null
)

data class Region(
  @SerializedName("id") var id: String? = null,
  @SerializedName("name") var name: String? = null
)

data class City(
  @SerializedName("id") var id: String? = null,
  @SerializedName("name") var name: String? = null
)
