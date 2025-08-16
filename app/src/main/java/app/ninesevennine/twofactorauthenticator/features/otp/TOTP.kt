package app.ninesevennine.twofactorauthenticator.features.otp

object TOTP {
    fun generate(
        otpHashFunction: OtpHashFunctions,
        secret: ByteArray,
        digits: Int,
        period: Int,
        currentTimeSeconds: Long
    ): String {
        return HOTP.generate(
            otpHashFunction = otpHashFunction,
            secret = secret,
            digits = digits,
            counter = currentTimeSeconds / period
        )
    }
}