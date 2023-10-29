package mimsoft.io.files

import io.ktor.http.content.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object FilesService {

  private const val PATH = "/var/www/html/files"
  private const val template = "yyyy-MM-dd-HH-mm-ss-SSSSSSSSS"

  const val IMAGE = "images"
  const val VIDEO = "videos"

  suspend fun uploadFile(multipart: MultiPartData, type: String = IMAGE): String {
    var name = ""
    multipart.forEachPart { part ->
      if (part is PartData.FileItem) {
        if (part.originalFileName != null) {
          val ext = part.originalFileName?.let { File(it).extension }
          val parent = File("$PATH/$type/")
          if (!parent.exists()) {
            parent.mkdirs()
          }
          name = "$type/${SimpleDateFormat(template).format(Date())}.$ext"
          val file = File("$PATH/$name")
          part.streamProvider().use { its -> file.outputStream().buffered().use { its.copyTo(it) } }
        }
      }
      part.dispose()
    }
    return name
  }

  suspend fun uploadImage(multipart: MultiPartData) = uploadFile(multipart, type = IMAGE)

  fun deleteFile(url: String): Boolean {
    val fd = File("$PATH/$url")
    if (fd.exists()) {
      fd.delete()
    }
    return true
  }
}
