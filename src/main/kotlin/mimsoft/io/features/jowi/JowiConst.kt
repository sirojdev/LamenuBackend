package mimsoft.io.features.jowi

import java.security.MessageDigest

object JowiConst {

    val API_KEY = "JNRijPDgK3KU0tCybkAJbSi46lQt-IpVe-vOL6Yy"
    val API_SECRET = "EQ-ZzKG2VwtefVcNxKpfrfT9qJ66ImmNWLv16rMMCKaH7tDFxfSCnw"
    val sig = "ec7642ecf6930b9876d07796d7cc7381cb82ffedb1b8f7d93ee97a7ce988fcc0"
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
    return hexString.toString()
}
