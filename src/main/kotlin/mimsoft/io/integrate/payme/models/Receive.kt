package mimsoft.io.integrate.payme.models

data class Receive(
  val id: Long,
  val method: String?,
  val params: HashMap<*, *>,
) {
  companion object {
    val CHECK_PERFORM_TRANSACTION = "CheckPerformTransaction"
    val CREATE_TRANSACTION = "CreateTransaction"
    val PERFORM_TRANSACTION = "PerformTransaction"
    val CANCEL_TRANSACTION = "CancelTransaction"
    val CHECK_TRANSACTION = "CheckTransaction"
    val GET_STATEMENT = "GetStatement"
  }
}
