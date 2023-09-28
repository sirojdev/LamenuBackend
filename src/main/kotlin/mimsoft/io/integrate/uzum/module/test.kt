package mimsoft.io.integrate.uzum.module

import mimsoft.io.integrate.uzum.UzumService
import java.security.KeyPair

fun main() {
    // Generate a key pair
    val keyPair: KeyPair = UzumService.generateKeys()

    // Message to be signed and verified
    val message = "Hello, world!"

    // Generate the signature
    val signature =UzumService.generateSignature(message, keyPair)

    // Verify the signature
    val isSignatureValid = UzumService.validateSignature(message, keyPair, signature)

    // Print the result
    if (isSignatureValid) {
        println("Signature is VALID")
    } else {
        println("Signature is INVALID")
    }
}
