package mimsoft.io.integrate.payme.models

data class CheckPerformResult(
  val result: ChResult? = null,
)

data class ChResult(val allow: Boolean = false, val additional: HashMap<String, Any>? = null)
