package mimsoft.io.rsa

import kotlin.random.Random

object Generator {
    fun generate(static: Boolean = true): GeneratorModel {

        val now = System.currentTimeMillis() / 1000
        val code = if (!static)
            Random.nextLong(10000, 100000)
        else 12345
        val hash = 1000L/*encode(now * 100000 + code)*/

        return GeneratorModel(
            code = code,
            hash = hash
        )
    }

    fun validate(gen: GeneratorModel?): Status {
        return Status.ACCEPTED
    }
}