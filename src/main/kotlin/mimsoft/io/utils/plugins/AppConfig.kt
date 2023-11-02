package mimsoft.io.utils.plugins

import io.ktor.server.config.*

object AppConfig {

  var config: ApplicationConfig? = null
    set(value) {
      if (field == null) {
        field = value
      }
    }
}
