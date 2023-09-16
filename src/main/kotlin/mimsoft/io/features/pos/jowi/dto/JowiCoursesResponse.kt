package mimsoft.io.features.pos.jowi.dto

import com.google.gson.annotations.SerializedName
import mimsoft.io.features.pos.jowi.Course

const val JOWI_PRODUCTS = "jowi_products"

data class JowiCoursesResponse(
    val status: Int? = null,
    val courses: List<Courses>? = null
)

data class Courses(
    var id: String? = null,
    @SerializedName("course_category_id")
    var courseCategoryId: String? = null,
    var title: String? = null,
    var price: Double? = null,
    @SerializedName("price_for_online_order")
    var priceForOnlineOrder: Double? = null,
    @SerializedName("is_visible")
    val isVisible: Boolean? = null,
    @SerializedName("prepare_time")
    var prepareTime: String? = null,
    @SerializedName("online_order")
    var onlineOrder: Boolean? = null,
    var description: String? = null,
    @SerializedName("image_url")
    var imageUrl: String? = null,
)