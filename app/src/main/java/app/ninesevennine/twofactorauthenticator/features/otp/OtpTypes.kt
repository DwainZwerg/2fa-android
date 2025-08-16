package app.ninesevennine.twofactorauthenticator.features.otp

enum class OtpTypes(val value: String) {
    TOTP("TOTP"),
    HOTP("HOTP");

    companion object {
        private val entriesMap = entries.associateBy { it.value }

        fun fromString(value: String) = entriesMap[value] ?: TOTP
    }
}