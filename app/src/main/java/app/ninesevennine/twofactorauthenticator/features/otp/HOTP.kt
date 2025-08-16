package app.ninesevennine.twofactorauthenticator.features.otp

import java.nio.ByteBuffer
import kotlin.math.pow

object HOTP {
    fun generate(
        otpHashFunction: OtpHashFunctions,
        secret: ByteArray,
        digits: Int,
        counter: Long
    ): String {
        val counterBytes = ByteBuffer.allocate(8).putLong(counter).array()

        val hmac = HMAC.calculate(secret, counterBytes, otpHashFunction)

        val offset = (hmac[hmac.size - 1].toInt() and 0x0F)

        val binaryCode = ((hmac[offset].toInt() and 0x7F) shl 24) or
                ((hmac[offset + 1].toInt() and 0xFF) shl 16) or
                ((hmac[offset + 2].toInt() and 0xFF) shl 8) or
                (hmac[offset + 3].toInt() and 0xFF)

        val otp = binaryCode % 10.0.pow(digits).toInt()

        return otp.toString().padStart(digits, '0')
    }
}