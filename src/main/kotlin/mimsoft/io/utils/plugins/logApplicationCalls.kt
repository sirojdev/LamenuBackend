package mimsoft.io.utils.plugins

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.util.*
import mimsoft.io.repository.DBManager
import org.slf4j.LoggerFactory

fun Route.logApplicationCalls() {
    intercept(ApplicationCallPipeline.Monitoring) {
        val startTime = System.currentTimeMillis()

        // Формирование информации запроса
        val requestMethod = context.request.httpMethod.toString()
        val requestUri = context.request.uri
        val requestHeaders = context.request.headers.toMap().toString()
        val requestParameters = context.parameters.entries().toString()

        var responseStatus: Int?
        var responseHeaders: String?
        var responseTime: Long?


        // следите за ответом следующим образом
        try {
            proceed() // обязательно идите дальше

            responseStatus = context.response.status()?.value
            responseHeaders = context.response.headers.allValues().toString()
            responseTime = System.currentTimeMillis() - startTime
        }catch (e: Exception){
            responseStatus = 500
            responseHeaders = e.message
            responseTime = System.currentTimeMillis() - startTime
        }

        DBManager.insert(
            "INSERT INTO log_request (request_method, request_url, request_headers, request_params, response_status, response_headers, response_time) VALUES (?, ?, ?, ?, ?, ?, ?)",
            mapOf(1 to requestMethod, 2 to requestUri, 3 to requestHeaders, 4 to requestParameters, 5 to responseStatus, 6 to responseHeaders, 7 to responseTime))

        // логирование тела ответа может быть невозможно без поддержки двойного чтения тела или без нарушения чтения тела
        // со стороны реального клиента
    }
}