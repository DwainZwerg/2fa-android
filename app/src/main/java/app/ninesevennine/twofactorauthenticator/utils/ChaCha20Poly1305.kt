package app.ninesevennine.twofactorauthenticator.utils

import org.bouncycastle.crypto.modes.ChaCha20Poly1305
import org.bouncycastle.crypto.params.AEADParameters
import org.bouncycastle.crypto.params.KeyParameter
import java.security.SecureRandom

object ChaCha20Poly1305 {
    const val KEY_SIZE: Int = 32
    const val NONCE_SIZE: Int = 12
    const val MAC_SIZE = 16

    private val secureRandom = SecureRandom()

    fun randomNonce(): ByteArray = ByteArray(NONCE_SIZE).also { secureRandom.nextBytes(it) }

    fun encrypt(input: ByteArray, key: ByteArray, nonce: ByteArray): ByteArray {
        val cipher = ChaCha20Poly1305()
        val params = AEADParameters(KeyParameter(key), MAC_SIZE * 8, nonce)

        cipher.init(true, params)

        val output = ByteArray(cipher.getOutputSize(input.size))
        val len = cipher.processBytes(input, 0, input.size, output, 0)
        cipher.doFinal(output, len)
        return output
    }

    fun decrypt(input: ByteArray, key: ByteArray, nonce: ByteArray): ByteArray? {
        val cipher = ChaCha20Poly1305()
        val params = AEADParameters(KeyParameter(key), MAC_SIZE * 8, nonce)

        return try {
            cipher.init(false, params)

            val output = ByteArray(cipher.getOutputSize(input.size))
            val len = cipher.processBytes(input, 0, input.size, output, 0)
            val finalLen = cipher.doFinal(output, len)

            output.copyOf(len + finalLen)
        } catch (_: Exception) {
            null
        }
    }
}