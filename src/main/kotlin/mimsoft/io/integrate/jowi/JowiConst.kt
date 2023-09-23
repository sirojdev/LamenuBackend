package mimsoft.io.integrate.jowi

import mimsoft.io.integrate.jowi.JowiConst.API_KEY
import mimsoft.io.integrate.jowi.JowiConst.API_SECRET
import java.security.MessageDigest

object JowiConst {

    val API_KEY = "RRuGCQ64X-8urEOR6Feg0hK2xLRoOdLuCWxPzLrj"
    val API_SECRET = "EQ-ZzKG2VwtefVcNxKpfrfT9qJ66ImmNWLv16rMMCKaH7tDFxfSCnw"
    val sig = "831390fe33a7f02"
}
fun sha256(apiKey: String,apiSecret:String): String {
    val messageDigest = MessageDigest.getInstance("SHA-256")
    val bytes = messageDigest.digest(StringBuilder(API_KEY+ API_SECRET).toString().toByteArray())
    val hexString = StringBuilder(2 * bytes.size)
    for (byte in bytes) {
        val hex = Integer.toHexString(0xFF and byte.toInt())
        if (hex.length == 1) {
            hexString.append('0')
        }
        hexString.append(hex)
    }
    return hexString.toString().substring(0,10)+hexString.toString().substring(hexString.length-5)
}
