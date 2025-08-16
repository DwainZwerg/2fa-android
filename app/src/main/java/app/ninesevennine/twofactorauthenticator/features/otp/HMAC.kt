package app.ninesevennine.twofactorauthenticator.features.otp

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object HMAC {
    fun calculate(
        key: ByteArray,
        data: ByteArray,
        hashFunction: OtpHashFunctions
    ): ByteArray {
        val secretKeySpec = when (hashFunction) {
            OtpHashFunctions.SHA1 -> SecretKeySpec(key, "HmacSHA1")
            OtpHashFunctions.SHA256 -> SecretKeySpec(key, "HmacSHA256")
            OtpHashFunctions.SHA512 -> SecretKeySpec(key, "HmacSHA512")
        }

        val mac = Mac.getInstance(secretKeySpec.algorithm)
        mac.init(secretKeySpec)
        val result = mac.doFinal(data)

        return result
    }
}