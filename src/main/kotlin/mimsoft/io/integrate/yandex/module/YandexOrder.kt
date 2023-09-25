package mimsoft.io.integrate.yandex.module

import com.google.gson.annotations.SerializedName

data class YandexOrder(
    @SerializedName("auto_accept")
    val autoAccept: Boolean,
    @SerializedName("callback_properties")
    val callbackProperties: CallbackProperties,
    @SerializedName("client_requirements")
    val clientRequirements: Requirement,
    val comment: String,
    val due: String,
    @SerializedName("emergency_contact")
    val emergencyContact: EmergencyContact,
    val items: List<Item>,
    @SerializedName("offer_payload")
    val offerPayload: String,
    @SerializedName("optional_return")
    val optionalReturn: Boolean,
    @SerializedName("order_url")
    val referralSource: String,
    @SerializedName("route_points")
    val routePoints: List<RoutePoint>,
    @SerializedName("same_day_data")
    val sameDayData: SameDayData,
    @SerializedName("shipping_document")
    val shippingDocument: String,
    @SerializedName("skip_act")
    val skipAct: Boolean,
    @SerializedName("skip_client_notify")
    val skipClientNotify: Boolean,
    @SerializedName("skip_door_to_door")
    val skipDoorToDoor: Boolean,
    @SerializedName("skip_emergency_notify")
    val skipEmergencyNotify: Boolean,
    val requirement: Requirement

)

data class Requirement(
    @SerializedName("assign_robot")
    val assignRobot: Boolean,
    @SerializedName("cargo_loaders")
    val cargoLoaders: Int,
    @SerializedName("cargo_options")
    val cargoOptions: List<String>,
    @SerializedName("cargo_type")
    val cargoType: String,
    @SerializedName("pro_courier")
    val proCourier: Boolean,
    @SerializedName("taxi_class")
    val taxiClass: String,
    @SerializedName("same_day_data")
    val sameDayData:SameDayData
)

data class CallbackProperties(
    val callbackUrl: String
)

data class EmergencyContact(
    val name: String,
    val phone: String,
    @SerializedName("phone_additional_code")
    val phoneAdditionalCode: String
)

data class Item(
    @SerializedName("cost_currency")
    val costCurrency: String?=null,
    @SerializedName("cost_value")
    val costValue: String?=null,
    @SerializedName("droppof_point")
    val droppofPoint: Int?=null,
    @SerializedName("extra_id")
    val extraId: String?=null,
    val fiscalization: Fiscalization,
    @SerializedName("pickup_point")
    val pickupPoint: Int?=null,
    val quantity: Int?=null,
    val size: Size?=null,
    val title: String?=null,
    val weight: Double?=null
)

data class Fiscalization(
    val article: String?=null,
    val excise: String?=null,
    @SerializedName("item_type")
    val itemType: String?=null,
    val mark: Mark?=null,
    @SerializedName("supplier_inn")
    val supplierInn: String?=null,
    @SerializedName("vat_code_str")
    val vatCodeStr: String?=null
)

data class Mark(
    val code: String?=null,
    val kind: String?=null
)

data class Size(
    val height: Double?=null,
    val length: Double?=null,
    val width: Double?=null
)


data class RoutePoint(
    val address: Address?=null,
    val buyout: Buyout?=null,
    val contact: Contact?=null,
    @SerializedName("external_order_cost")
    val externalOrderCost: ExternalOrderCost?=null,
    @SerializedName("external_order_id")
    val externalOrderId: String?=null,
    @SerializedName("leave_under_door")
    val leaveUnderDoor: Boolean?=null,
    @SerializedName("meet_outside")
    val meetOutside: Boolean?=null,
    @SerializedName("no_door_call")
    val noDoorCall: Boolean?=null,
    @SerializedName("payment_on_delivery")
    val paymentOnDelivery: PaymentOnDelivery?=null,
    @SerializedName("pick_up_code")
    val pickupCode: String?=null,
    @SerializedName("point_id")
    val pointId: Int?=null,
    @SerializedName("skip_confirmation")
    val skipConfirmation: Boolean?=null,
    val type: String?=null,
    @SerializedName("visit_order")
    val visitOrder: Int?=null
)

data class Address(
    val building: String?=null,
    @SerializedName("building_name")
    val buildingName: String?=null,
    val city: String?=null,
    val comment: String?=null,
    val coordinates: List<Double>?=null,
    val country: String?=null,
    val description: String?=null,
    @SerializedName("door_code")
    val doorCode: String?=null,
    @SerializedName("door_code_extra")
    val doorCodeExtra: String?=null,
    @SerializedName("door_bell_name")
    val doorbellName: String?=null,
    val flat: Int?=null,
    val floor: Int?=null,
    val fullname: String?=null,
    val porch: String?=null,
    val sflat: String?=null,
    val sfloor: String?=null,
    val shortname: String?=null,
    val street: String?=null,
    val uri: String?=null
)

data class Buyout(
    @SerializedName("payment_method")
    val paymentMethod: String?=null
)

data class Contact(
    val email: String?=null,
    val name: String?=null,
    val phone: String?=null,
    @SerializedName("phone_additional_code")
    val phoneAdditionalCode: String?=null
)

data class ExternalOrderCost(
    val currency: String?=null,
    @SerializedName("currency_sign")
    val currencySign: String?=null,
    val value: String?=null
)

data class PaymentOnDelivery(
    val customer: Customer?=null,
    @SerializedName("payment_method")
    val paymentMethod: String?=null
)

data class Customer(
    val email: String?=null,
    val inn: String?=null,
    val phone: String?=null
)

data class SameDayData(
    @SerializedName("delivery_interval")
    val deliveryInterval: DeliveryInterval?=null
)

data class DeliveryInterval(
    val from: String?=null,
    val to: String?=null
)