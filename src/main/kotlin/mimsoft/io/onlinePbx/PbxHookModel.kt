package mimsoft.io.onlinePbx

data class PbxHookModel(
    val event: String? = null,
    val direction: String? = null,
    val caller: String? = null,
    val callee: String? = null
)
