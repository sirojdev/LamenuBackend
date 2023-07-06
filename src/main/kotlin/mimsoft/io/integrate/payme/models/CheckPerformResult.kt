package mimsoft.io.integrate.payme.models

data class CheckPerformResult(
    val result: Result? = null,
)

data class Result(
    val allow: Boolean = false,
    val additional: HashMap<String, Any>? = null
)