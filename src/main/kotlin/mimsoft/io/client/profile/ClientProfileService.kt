package mimsoft.io.client.profile

import io.ktor.http.*
import java.util.*
import mimsoft.io.client.user.UserDto
import mimsoft.io.client.user.repository.UserRepositoryImpl
import mimsoft.io.files.FilesService
import mimsoft.io.utils.ResponseModel
import mimsoft.io.utils.principal.BasePrincipal

object ClientProfileService {
  suspend fun updateImage(base64: String, principal: BasePrincipal?): ResponseModel {
    val client = UserRepositoryImpl.get(principal?.userId, principal?.merchantId)
    return try {
      val decodedImage = Base64.getDecoder().decode(base64)
      val imageName = FilesService.uploadFile(decodedImage, "images", "png")
      if (imageName == null) ResponseModel(httpStatus = HttpStatusCode.UnsupportedMediaType)
      else {
        if (client?.image != null) {
          FilesService.deleteFile(client.image)
        }
        UserRepositoryImpl.updateImage(imageName, principal?.userId)
        ResponseModel(body = UserDto(image = imageName))
      }
    } catch (e: Exception) {
      ResponseModel(HttpStatusCode.UnsupportedMediaType)
    }
  }

  /*suspend fun deleteImage(userId: Long?, merchantId: Long?) {
    TODO("Not yet implemented")
  }*/
}
