package mimsoft.io.ssl

import io.ktor.network.tls.certificates.*
import java.io.File

fun sslConfiguration() {
    val keyStoreFile = File("uzum.jks")
    val keyStore = buildKeyStore {
        certificate("myAlias") {
            password = "m1msoftUzum"
            domains = listOf("https://ofd.ipt-merch.com/fiscal_receipt_generation")
        }
    }
    keyStore.saveToFile(keyStoreFile, "m1msoftUzum")
}