package mimsoft.io.utils

import kotlinx.serialization.Serializable

@Serializable
data class TextModel(
    var uz: String? = null,
    var ru: String? = null,
    var en: String? = null
)
