package mimsoft.io.rsa

object Generator {
  private const val AC_TIME = 3 * 60000
  private const val GONE_TIME = 600000

  fun checkValidate(generator: GeneratorModel?): Status {
    return if (generator != null) {
      val time = getTime(generator)
      val code = getCodeFromHash(generator)
      val now = System.currentTimeMillis()
      println("$time, $now")

      return if (time < now && time + AC_TIME >= now && code == generator.code) Status.ACCEPTED
      else if (time < now && time + AC_TIME >= now) Status.INVALID_CODE
      else if (time < now && time + GONE_TIME >= now) Status.GONE_CODE else Status.INVALID_CODE
    } else Status.INVALID_CODE
  }

  fun generate(fixed: Boolean = false): GeneratorModel {
    val code = if (!fixed) (10000..99999).random().toLong() else 13579
    val now = System.currentTimeMillis()
    return GeneratorModel(code = code, hash = now * 100000 + code)
  }

  private fun getTime(generator: GeneratorModel?): Long {
    return (generator?.hash ?: 0) / 100000
  }

  private fun getCodeFromHash(generator: GeneratorModel?): Long {
    return (generator?.hash ?: 0) % 100000
  }
}
