package mimsoft.io.integrate.join_poster.model

import com.google.gson.annotations.SerializedName

data class PosterFoodModel (
    @SerializedName("product_id")
    val id  : Long? = null,
    val count : Long? = null
)