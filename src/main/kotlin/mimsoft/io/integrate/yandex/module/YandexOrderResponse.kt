package mimsoft.io.integrate.yandex.module

import com.google.gson.annotations.SerializedName

data class YandexOrderResponse(
    // to dto this json ()
    // {
    //  "available_cancel_state": string,
    //  "callback_properties": {
    //    "callback_url": string
    //  },
    //  "carrier_info": {
    //    "address": string,
    //    "inn": string,
    //    "name": string
    //  },
    //  "client_requirements": {
    //    "assign_robot": boolean,
    //    "cargo_loaders": integer,
    //    "cargo_options": [
    //      string
    //    ],
    //    "cargo_type": string,
    //    "pro_courier": boolean,
    //    "taxi_class": string
    //  },
    //  "comment": string,
    //  "corp_client_id": string,
    //  "created_ts": string,
    //  "current_point_id": integer,
    //  "due": string,
    //  "emergency_contact": {
    //    "name": string,
    //    "phone": string,
    //    "phone_additional_code": string
    //  },
    //  "error_messages": [
    //    {
    //      "code": string,
    //      "message": string
    //    }
    //  ],
    //  "eta": integer,
    //  "id": string,
    //  "items": [
    //    {
    //      "cost_currency": string,
    //      "cost_value": string,
    //      "droppof_point": integer,
    //      "extra_id": string,
    //      "fiscalization": {
    //        "article": string,
    //        "excise": string,
    //        "item_type": string,
    //        "mark": {
    //          "code": string,
    //          "kind": string
    //        },
    //        "supplier_inn": string,
    //        "vat_code_str": string
    //      },
    //      "pickup_point": integer,
    //      "quantity": integer,
    //      "size": {
    //        "height": number,
    //        "length": number,
    //        "width": number
    //      },
    //      "title": string,
    //      "weight": number
    //    }
    //  ],
    //  "matched_cars": [
    //    {
    //      "cargo_loaders": integer,
    //      "cargo_type": string,
    //      "cargo_type_int": integer,
    //      "client_taxi_class": string,
    //      "door_to_door": boolean,
    //      "pro_courier": boolean,
    //      "taxi_class": string
    //    }
    //  ],
    //  "optional_return": boolean,
    //  "performer_info": {
    //    "car_color": string,
    //    "car_color_hex": string,
    //    "car_model": string,
    //    "car_number": string,
    //    "courier_name": string,
    //    "legal_name": string,
    //    "transport_type": string
    //  },
    //  "pricing": {
    //    "currency": string,
    //    "currency_rules": {
    //      "code": string,
    //      "sign": string,
    //      "template": string,
    //      "text": string
    //    },
    //    "final_price": string,
    //    "final_pricing_calc_id": string,
    //    "offer": {
    //      "offer_id": string,
    //      "price": string,
    //      "price_raw": integer,
    //      "price_with_vat": string,
    //      "valid_until": string
    //    }
    //  },
    //  "revision": integer,
    //  "route_id": string,
    //  "route_points": [
    //    {
    //      "address": {
    //        "building": string,
    //        "building_name": string,
    //        "city": string,
    //        "comment": string,
    //        "coordinates": [
    //          number
    //        ],
    //        "country": string,
    //        "description": string,
    //        "door_code": string,
    //        "door_code_extra": string,
    //        "doorbell_name": string,
    //        "flat": integer,
    //        "floor": integer,
    //        "fullname": string,
    //        "porch": string,
    //        "sflat": string,
    //        "sfloor": string,
    //        "shortname": string,
    //        "street": string,
    //        "uri": string
    //      },
    //      "contact": {
    //        "email": string,
    //        "name": string,
    //        "phone": string,
    //        "phone_additional_code": string
    //      },
    //      "expected_visit_interval": {
    //        "from": string,
    //        "to": string
    //      },
    //      "external_order_cost": {
    //        "currency": string,
    //        "currency_sign": string,
    //        "value": string
    //      },
    //      "external_order_id": string,
    //      "id": integer,
    //      "leave_under_door": boolean,
    //      "meet_outside": boolean,
    //      "modifier_age_check": boolean,
    //      "no_door_call": boolean,
    //      "payment_on_delivery": {
    //        "client_order_id": string,
    //        "cost": string,
    //        "customer": {
    //          "email": string,
    //          "full_name": string,
    //          "inn": string,
    //          "phone": string
    //        },
    //        "is_paid": boolean,
    //        "payment_method": string,
    //        "payment_ref_id": string
    //      },
    //      "pickup_code": string,
    //      "return_comment": string,
    //      "return_reasons": [
    //        string
    //      ],
    //      "skip_confirmation": boolean,
    //      "type": string,
    //      "visit_order": integer,
    //      "visit_status": string,
    //      "visited_at": {
    //        "actual": string,
    //        "expected": string,
    //        "expected_waiting_time_sec": integer
    //      }
    //    }
    //  ],
    //  "same_day_data": {
    //    "delivery_interval": {
    //      "from": string,
    //      "to": string
    //    }
    //  },
    //  "shipping_document": string,
    //  "skip_act": boolean,
    //  "skip_client_notify": boolean,
    //  "skip_door_to_door": boolean,
    //  "skip_emergency_notify": boolean,
    //  "status": string,
    //  "taxi_offer": {
    //    "offer_id": string,
    //    "price": string,
    //    "price_raw": integer
    //  },
    //  "updated_ts": string,
    //  "user_request_revision": string,
    //  "version": integer,
    //  "warnings": [
    //    {
    //      "code": string,
    //      "message": string,
    //      "source": string
    //    }
    //  ]
    //}
    val availableCancelState: String,
    val callbackProperties: CallbackProperties,
    val carrierInfo: CarrierInfo,
    val clientRequirements: Requirement,
    val comment: String,
    val corpClientId: String,
    val createdTs: String,
    val currentPointId: Int,
    val due: String,
    val emergencyContact: EmergencyContact,
    val errorMessages: List<ErrorMessage>,
    val eta: Int,
    val id: String,
    val items: List<YandexOrderItem>,
    val matchedCars: List<MatchedCars>,
    val optionalReturn: Boolean,
    val performerInfo: PerformerInfo,
    val pricing: Pricing,
    val revision: Int,
    val routeId: String,
    val routePoints: List<YandexOrderRoutePoint>,
    val sameDayData: SameDayData,
    val shippingDocument: String,
    val skipAct: Boolean,
    val skipClientNotify: Boolean,
    val skipDoorToDoor: Boolean,
    val skipEmergencyNotify: Boolean,
    val status: String,
    val taxiOffer: TaxiOffer,
    val updatedTs: String,
    val userRequestRevision: String,
    val version: String,
    val warnings: List<Warning>
)

data class CarrierInfo(
    val address: String,
    val iin: String,
    val name: String
)

data class ErrorMessage(
    val code: String,
    val message: String,
)

data class MatchedCars(
    @SerializedName("cargo_loaders")
    val cargoLoaders: String,
    @SerializedName("cargo_type")
    val cargoType: String,
    @SerializedName("cargo_type_int")
    val cargoTypeInt: Int,
    @SerializedName("client_taxi_class")
    val clientTaxiClass: String,
    @SerializedName("door_to_door")
    val doorToDoor: Boolean,
    @SerializedName("pro_courier")
    val proCourier: Boolean,
    @SerializedName("taxi_class")
    val taxiClass: String
)

data class PerformerInfo(
    @SerializedName("car_color")
    val carColor: String,
    @SerializedName("car_color_hex")
    val carColorHex: String,
    @SerializedName("car_model")
    val carModel: String,
    @SerializedName("car_number")
    val carNumber: String,
    @SerializedName("courier_name")
    val courierName: String,
    @SerializedName("legal_name")
    val legalName: String,
    @SerializedName("transport_type")
    val transportType: String
)

data class Pricing(
    val currency: String,
    @SerializedName("currency_rules")
    val currencyRules: CurrencyRules,
    @SerializedName("final_price")
    val finalPrice: String,
    @SerializedName("final_pricing_calc_id")
    val finalPricingCalcId: String,
    val offer: Offer
)

data class CurrencyRules(
    val code: String,
    val sign: String,
    val template: String,
    val text: String
)

data class Offer(
    @SerializedName("offer_id")
    val offerId: String,
    @SerializedName("price")
    val price: String,
    @SerializedName("price_raw")
    val priceRaw: Int,
    @SerializedName("price_with_vat")
    val priceWithVat: String,
    @SerializedName("valid_until")
    val validUntil: String
)

data class TaxiOffer(
    @SerializedName("offer_id")
    val offerId: String,
    @SerializedName("price")
    val price: String,
    @SerializedName("price_raw")
    val priceRaw: Int
)

data class Warning(
    val code: String,
    val message: String,
    val source: String
)



