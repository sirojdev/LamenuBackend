package mimsoft.io.ssl

import java.io.FileInputStream
import java.security.KeyStore
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager
import org.apache.http.conn.ssl.TrustSelfSignedStrategy
import org.apache.http.ssl.SSLContextBuilder

object SslSettings {
  fun getKeyStore(): KeyStore {
    val keyStoreFile = FileInputStream("/root/pay/uzum.jks")
    //    val keyStoreFile = FileInputStream("uzum.jks")
    val keyStorePassword = "m1msoftUzum".toCharArray()
    val keyStore: KeyStore = KeyStore.getInstance(KeyStore.getDefaultType())
    keyStore.load(keyStoreFile, keyStorePassword)
    return keyStore
  }

  fun getTrustManagerFactory(): TrustManagerFactory? {
    val trustManagerFactory =
      TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
    trustManagerFactory.init(getKeyStore())
    return trustManagerFactory
  }

  fun getSslContext(): SSLContext? {
    val sslContext: SSLContext =
      SSLContextBuilder.create()
        .loadKeyMaterial(getKeyStore(), "m1msoftUzum".toCharArray())
        .loadTrustMaterial(null, TrustSelfSignedStrategy())
        .build()
    //        val sslContext = SSLContext.getInstance("TLSv1.2")
    //        sslContext.init(null, getTrustManagerFactory()?.trustManagers, null)
    return sslContext
  }

  fun getTrustManager(): X509TrustManager {
    return getTrustManagerFactory()?.trustManagers?.first { it is X509TrustManager }
      as X509TrustManager
  }
}