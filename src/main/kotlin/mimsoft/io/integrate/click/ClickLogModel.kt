package mimsoft.io.integrate.click

data class ClickLogModel(
    val id: Long? = null,
    val method: String? = null,
    val parameters: String? = null,
    val createdAt: String? = null
){
    companion object {
        const val PREPARE_IN = "prepare_in"
        const val PREPARE_OUT = "prepare_out"
        const val COMPLETE_IN = "complete_in"
        const val COMPLETE_OUT = "complete_out"
    }
}
