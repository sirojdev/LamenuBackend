package mimsoft.io.features.client.repository

data class UserPost(
    val id: Long? = null,
    val status: PostStatus? = null
)
enum class PostStatus{
    PHONE_CONFLICT,
    FIRSTNAME_NULL,
    OK
}
