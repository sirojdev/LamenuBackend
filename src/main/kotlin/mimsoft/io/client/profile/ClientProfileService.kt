package mimsoft.io.client.profile

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import java.util.*
import mimsoft.io.client.user.UserDto
import mimsoft.io.client.user.repository.UserRepositoryImpl
import mimsoft.io.files.FilesService
import mimsoft.io.utils.ResponseModel
import mimsoft.io.utils.principal.BasePrincipal

object ClientProfileService {
  suspend fun updateImage(base64: String, principal: BasePrincipal?): ResponseModel {
    val client = UserRepositoryImpl.get(principal?.userId, principal?.merchantId)
    var extension = ""
    val startImage: String =
      when (true) {
        base64.startsWith("data:image/png;base64,") -> {
          extension = "png"
          "data:image/png;base64,"
        }
        base64.startsWith("data:image/jpeg;base64,") -> {
          extension = "jpeg"
          "data:image/jpeg;base64,"
        }
        base64.startsWith("data:image/jpg;base64,") -> {
          extension = "jpg"
          "data:image/jpg;base64,"
        }
        else -> {
          return ResponseModel(httpStatus = HttpStatusCode.BadRequest)
        }
      }
    val base64EncodedImage = base64.removePrefix(startImage)
    val decodedImage = Base64.getDecoder().decode(base64EncodedImage)
    return try {
      val imageName = FilesService.uploadFile(decodedImage, "images", extension)
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
}
