package mimsoft.io.integrate.yandex.module

import com.google.gson.annotations.SerializedName

data class YandexOrder(
    @SerializedName("auto_accept")
    val autoAccept: Boolean,
    @SerializedName("callback_properties")
    val callbackProperties: CallbackProperties,
    @SerializedName("client_requirements")
    val clientRequirements: ClientRequirements,
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
    val skipEmergencyNotify: Boolean

)

data class ClientRequirements(
    @SerializedName("assign_robot")
    val assignRobot: Boolean,
    @SerializedName("cargo_loaders")
    val cargoLoaders: Int,
    @SerializedName("cargo_options")
    val cargoOptions: List<String>,
    @SerializedName("cargo_types")
    val cargoType: String,
    @SerializedName("pro_courier")
    val proCourier: Boolean,
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
    val costCurrency: String,
    @SerializedName("cost_value")
    val costValue: String,
    @SerializedName("droppof_point")
    val droppofPoint: Int,
    @SerializedName("extra_id")
    val extraId: String,
    val fiscalization: Fiscalization,
    @SerializedName("pickup_point")
    val pickupPoint: Int,
    val quantity: Int,
    val size: Size,
    val title: String,
    val weight: Double
)

data class Fiscalization(
    val article: String,
    val excise: String,
    @SerializedName("item_type")
    val itemType: String
)

data class Mark(
    val code: String,
    val kind: String
)

data class Size(
    val height: Double,
    val length: Double,
    val width: Double
)


data class RoutePoint(
    val address: Address,
    val buyout: Buyout,
    val contact: Contact,
    @SerializedName("external_order_cost")
    val externalOrderCost: ExternalOrderCost,
    @SerializedName("external_order_id")
    val externalOrderId: String,
    @SerializedName("leave_under_door")
    val leaveUnderDoor: Boolean,
    @SerializedName("meet_outside")
    val meetOutside: Boolean,
    @SerializedName("no_door_call")
    val noDoorCall: Boolean,
    @SerializedName("payment_on_delivery")
    val paymentOnDelivery: PaymentOnDelivery,
    @SerializedName("pick_up_code")
    val pickupCode: String,
    @SerializedName("point_id")
    val pointId: Int,
    @SerializedName("skip_confirmation")
    val skipConfirmation: Boolean,
    val type: String,
    @SerializedName("visit_order")
    val visitOrder: Int
)

data class Address(
    val building: String,
    @SerializedName("building_name")
    val buildingName: String,
    val city: String,
    val comment: String,
    val coordinates: List<Double>,
    val country: String,
    val description: String,
    @SerializedName("door_code")
    val doorCode: String,
    @SerializedName("door_code_extra")
    val doorCodeExtra: String,
    @SerializedName("door_bell_name")
    val doorbellName: String,
    val flat: Int,
    val floor: Int,
    val fullname: String,
    val porch: String,
    val sflat: String,
    val sfloor: String,
    val shortname: String,
    val street: String,
    val uri: String
)

data class Buyout(
    @SerializedName("payment_method")
    val paymentMethod: String
)

data class Contact(
    val email: String,
    val name: String,
    val phone: String,
    @SerializedName("phone_additional_code")
    val phoneAdditionalCode: String
)

data class ExternalOrderCost(
    val currency: String,
    @SerializedName("currency_sign")
    val currencySign: String,
    val value: String
)

data class PaymentOnDelivery(
    val customer: Customer,
    @SerializedName("payment_method")
    val paymentMethod: String
)

data class Customer(
    val email: String,
    val inn: String,
    val phone: String
)

data class SameDayData(
    @SerializedName("delivery_interval")
    val deliveryInterval: DeliveryInterval
)

data class DeliveryInterval(
    val from: String,
    val to: String
)