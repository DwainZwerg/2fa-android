package app.ninesevennine.twofactorauthenticator.features.otp
enum class OtpHashFunctions(val value: String) {
    SHA1("SHA-1"),
    SHA256("SHA-256"),
    SHA512("SHA-512");

    companion object {
        private val entriesMap = entries.associateBy { it.value }

        fun fromString(value: String) = entriesMap[value] ?: SHA1
    }
}