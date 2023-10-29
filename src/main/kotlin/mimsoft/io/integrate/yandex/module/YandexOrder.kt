package mimsoft.io.integrate.yandex.module

import com.google.gson.annotations.SerializedName

data class YandexOrder(
  @SerializedName("auto_accept") var autoAccept: Boolean? = null,
  @SerializedName("callback_properties") var callbackProperties: CallbackProperties? = null,
  @SerializedName("client_requirements") var clientRequirements: Requirement? = null,
  var comment: String? = null,
  var due: String? = null,
  @SerializedName("emergency_contact") var emergencyContact: EmergencyContact? = null,
  var items: ArrayList<YandexOrderItem?>? = null,
  @SerializedName("offer_payload") var offerPayload: String? = null,
  @SerializedName("optional_return") var optionalReturn: Boolean? = null,
  @SerializedName("order_url") var referralSource: String? = null,
  @SerializedName("route_points") var routePoints: List<YandexOrderRoutePoint>? = null,
  @SerializedName("same_day_data") var sameDayData: SameDayData? = null,
  @SerializedName("shipping_document") var shippingDocument: String? = null,
  @SerializedName("skip_act") var skipAct: Boolean? = null,
  @SerializedName("skip_client_notify") var skipClientNotify: Boolean? = null,
  @SerializedName("skip_door_to_door") var skipDoorToDoor: Boolean? = null,
  @SerializedName("skip_emergency_notify") var skipEmergencyNotify: Boolean? = null,
  var requirement: Requirement? = null
)

data class Requirement(
  @SerializedName("assign_robot") var assignRobot: Boolean? = null,
  @SerializedName("cargo_loaders") var cargoLoaders: Int? = null,
  @SerializedName("cargo_options") var cargoOptions: List<String>? = null,
  @SerializedName("cargo_type") var cargoType: String? = null,
  @SerializedName("pro_courier") var proCourier: Boolean? = null,
  @SerializedName("taxi_class") var taxiClass: String? = null,
  @SerializedName("same_day_data") var sameDayData: SameDayData? = null
)

data class CallbackProperties(@SerializedName("callback_url") var callbackUrl: String? = null)

data class EmergencyContact(
  var name: String? = null,
  var phone: String? = null,
  @SerializedName("phone_additional_code") var phoneAdditionalCode: String? = null
)

data class YandexOrderItem(
  @SerializedName("cost_currency") var costCurrency: String? = null,
  @SerializedName("cost_value") var costValue: String? = null,
  @SerializedName("droppof_point") var droppofPoint: Int? = null,
  @SerializedName("extra_id") var extraId: String? = null,
  var fiscalization: Fiscalization? = null,
  @SerializedName("pickup_point") var pickupPoint: Int? = null,
  var quantity: Int? = null,
  var size: Size? = null,
  var title: String? = null,
  var weight: Double? = null
)

data class Fiscalization(
  var article: String? = null,
  var excise: String? = null,
  @SerializedName("item_type") var itemType: String? = null,
  var mark: Mark? = null,
  @SerializedName("supplier_inn") var supplierInn: String? = null,
  @SerializedName("vat_code_str") var vatCodeStr: String? = null
)

data class Mark(var code: String? = null, var kind: String? = null)

data class Size(var height: Double? = null, var length: Double? = null, var width: Double? = null)

data class YandexOrderRoutePoint(
  var address: Address? = null,
  var buyout: Buyout? = null,
  var contact: Contact? = null,
  @SerializedName("external_order_cost") var externalOrderCost: ExternalOrderCost? = null,
  @SerializedName("external_order_id") var externalOrderId: String? = null,
  @SerializedName("leave_under_door") var leaveUnderDoor: Boolean? = null,
  @SerializedName("meet_outside") var meetOutside: Boolean? = null,
  @SerializedName("no_door_call") var noDoorCall: Boolean? = null,
  @SerializedName("payment_on_delivery") var paymentOnDelivery: PaymentOnDelivery? = null,
  @SerializedName("pick_up_code") var pickupCode: String? = null,
  @SerializedName("point_id") var pointId: Int? = null,
  @SerializedName("skip_confirmation") var skipConfirmation: Boolean? = null,
  var type: String? = null,
  @SerializedName("visit_order") var visitOrder: Int? = null
)

data class Address(
  var building: String? = null,
  @SerializedName("building_name") var buildingName: String? = null,
  var city: String? = null,
  var comment: String? = null,
  var coordinates: List<Double>? = null,
  var country: String? = null,
  var description: String? = null,
  @SerializedName("door_code") var doorCode: String? = null,
  @SerializedName("door_code_extra") var doorCodeExtra: String? = null,
  @SerializedName("door_bell_name") var doorbellName: String? = null,
  var flat: Int? = null,
  var floor: Int? = null,
  var fullname: String? = null,
  var porch: String? = null,
  var sflat: String? = null,
  var sfloor: String? = null,
  var shortname: String? = null,
  var street: String? = null,
  var uri: String? = null
)

data class Buyout(@SerializedName("payment_method") var paymentMethod: String? = null)

data class Contact(
  var email: String? = null,
  var name: String? = null,
  var phone: String? = null,
  @SerializedName("phone_additional_code") var phoneAdditionalCode: String? = null
)

data class ExternalOrderCost(
  var currency: String? = null,
  @SerializedName("currency_sign") val currencySign: String? = null,
  var value: String? = null
)

data class PaymentOnDelivery(
  var customer: Customer? = null,
  @SerializedName("payment_method") var paymentMethod: String? = null
)

data class Customer(var email: String? = null, var inn: String? = null, var phone: String? = null)

data class SameDayData(
  @SerializedName("delivery_intervar ") var deliveryIntervar: DeliveryIntervar? = null
)

data class DeliveryIntervar(var from: String? = null, var to: String? = null)
