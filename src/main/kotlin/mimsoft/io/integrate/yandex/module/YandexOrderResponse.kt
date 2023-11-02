package mimsoft.io.integrate.yandex.module

import com.google.gson.annotations.SerializedName

data class YandexOrderResponse(
  @SerializedName("available_cancel_state") var availableCancelState: String? = null,
  @SerializedName("callback_properties") var callbackProperties: CallbackProperties? = null,
  @SerializedName("carrier_info") var carrierInfo: CarrierInfo? = null,
  @SerializedName("client_requirements") var clientRequirements: Requirement? = null,
  @SerializedName("comment") var comment: String? = null,
  @SerializedName("corp_client_id") var corpClientId: String? = null,
  @SerializedName("created_ts") var createdTs: String? = null,
  @SerializedName("current_point_id") var currentPointId: Long? = null,
  @SerializedName("due") var due: String? = null,
  @SerializedName("emergency_contact") var emergencyContact: EmergencyContact? = null,
  @SerializedName("error_messages") var errorMessages: ArrayList<ErrorMessage>? = null,
  @SerializedName("eta") var eta: Int? = null,
  @SerializedName("id") var id: String? = null,
  @SerializedName("items") var items: ArrayList<Items>? = null,
  @SerializedName("matched_cars") var matchedCars: ArrayList<MatchedCars>? = null,
  @SerializedName("optional_return") var optionalReturn: Boolean? = null,
  @SerializedName("performer_info") var performerInfo: PerformerInfo? = null,
  @SerializedName("pricing") var pricing: Pricing? = null,
  @SerializedName("revision") var revision: Int? = null,
  @SerializedName("route_points") var routePoints: ArrayList<RoutePoints>? = null,
  @SerializedName("same_day_data") var sameDayData: SameDayData? = null,
  @SerializedName("skip_act") var skipAct: Boolean? = null,
  @SerializedName("skip_client_notify") var skipClientNotify: Boolean? = null,
  @SerializedName("skip_door_to_door") var skipDoorToDoor: Boolean? = null,
  @SerializedName("skip_emergency_notify") var skipEmergencyNotify: Boolean? = null,
  @SerializedName("status") var status: String? = null,
  @SerializedName("taxi_offer") var taxiOffer: TaxiOffer? = null,
  @SerializedName("updated_ts") var updatedTs: String? = null,
  var version: Int? = null,
  @SerializedName("warnings") var warnings: ArrayList<Warning>? = null
)

data class CarrierInfo(val address: String, val iin: String, val name: String)

data class ErrorMessage(
  val code: String,
  val message: String,
)

data class MatchedCars(
  @SerializedName("cargo_loaders") val cargoLoaders: String,
  @SerializedName("cargo_type") val cargoType: String,
  @SerializedName("cargo_type_int") val cargoTypeInt: Int,
  @SerializedName("client_taxi_class") val clientTaxiClass: String,
  @SerializedName("door_to_door") val doorToDoor: Boolean,
  @SerializedName("pro_courier") val proCourier: Boolean,
  @SerializedName("taxi_class") val taxiClass: String
)

data class PerformerInfo(
  @SerializedName("car_color") val carColor: String,
  @SerializedName("car_color_hex") val carColorHex: String,
  @SerializedName("car_model") val carModel: String,
  @SerializedName("car_number") val carNumber: String,
  @SerializedName("courier_name") val courierName: String,
  @SerializedName("legal_name") val legalName: String,
  @SerializedName("transport_type") val transportType: String
)

data class Pricing(
  val currency: String,
  @SerializedName("currency_rules") val currencyRules: CurrencyRules,
  @SerializedName("final_price") val finalPrice: String,
  @SerializedName("final_pricing_calc_id") val finalPricingCalcId: String,
  val offer: Offer
)

data class CurrencyRules(
  val code: String,
  val sign: String,
  val template: String,
  val text: String
)

data class Offer(
  @SerializedName("offer_id") val offerId: String,
  @SerializedName("price") val price: String,
  @SerializedName("price_raw") val priceRaw: Int,
  @SerializedName("price_with_vat") val priceWithVat: String,
  @SerializedName("valid_until") val validUntil: String
)

data class TaxiOffer(
  @SerializedName("offer_id") val offerId: String,
  @SerializedName("price") val price: String,
  @SerializedName("price_raw") val priceRaw: Int
)

data class Warning(val code: String, val message: String, val source: String)
