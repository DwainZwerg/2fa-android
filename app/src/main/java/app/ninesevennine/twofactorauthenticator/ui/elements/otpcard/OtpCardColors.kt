package app.ninesevennine.twofactorauthenticator.ui.elements.otpcard

import kotlin.random.Random

enum class OtpCardColors(val value: String) {
    RED("RED"),
    GREEN("GREEN"),
    BLUE("BLUE"),
    PINK("PINK"),
    ORANGE("ORANGE"),
    BROWN("BROWN");

    companion object {
        private val entriesMap = entries.associateBy { it.value }

        fun fromString(value: String) = entriesMap[value] ?: random()
        fun random(): OtpCardColors {
            val values = entries
            return values[Random.nextInt(values.size)]
        }
    }
}