package mimsoft.io.onlinePbx

data class CallModel(
    val accountcode: String? = null,
    val caller_id_name: String? = null,
    val caller_id_number: String? = null,
    val destination_number: String? = null,
    val direction : String? = null,
    val end_stamp: Int? = null,
    val from_host: String? = null,
    val gateway: String? = null,
    val hangup_cause: String? = null,
    val quality_score: Int? = null,
    val start_stamp: Int? = null,
    val to_host: String? = null,
    val user_talk_time: Int? = null,
    val uuid: String? = null,
    var outgoingCall : Boolean? = null
)