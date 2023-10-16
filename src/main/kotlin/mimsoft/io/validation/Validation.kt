package mimsoft.io.validation

import io.ktor.server.application.*
import io.ktor.server.request.*
import jakarta.validation.Validation
import jakarta.validation.Validator
import mimsoft.io.utils.plugins.BadRequestException
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator

object ValidatorSingleton {
    val validator: Validator = Validation.byDefaultProvider()
        .configure()
        .messageInterpolator(ParameterMessageInterpolator())
        .buildValidatorFactory().validator
}
suspend inline fun <reified T : Any> ApplicationCall.bindJson(): T {
    val dto = this.receive<T>()
    val validator: Validator = ValidatorSingleton.validator
    val violations = validator.validate(dto);
    if (violations.size > 0) {
        // Throw error messages when found violdations
        val details = violations.map {
            val propertyName = it.propertyPath.toString()
            val errorMessage = it.message
            "${propertyName}: $errorMessage"
        }
        // Your custom Exception in Status Page feature of Ktor application
        throw BadRequestException(details)
    } else {
        return dto
    }
}