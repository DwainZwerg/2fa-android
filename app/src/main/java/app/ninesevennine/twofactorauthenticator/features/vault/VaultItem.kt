package app.ninesevennine.twofactorauthenticator.features.vault

import app.ninesevennine.twofactorauthenticator.features.otp.OtpHashFunctions
import app.ninesevennine.twofactorauthenticator.features.otp.OtpTypes
import app.ninesevennine.twofactorauthenticator.ui.elements.otpcard.OtpCardColors
import app.ninesevennine.twofactorauthenticator.utils.Constants
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class VaultItem(
    var uuid: Uuid = Constants.NILUUID,
    var lastUpdated: Long = 0,
    var name: String = "",
    var issuer: String = "",
    var note: String = "",
    var secret: ByteArray = ByteArray(0),
    var period: Int = 30,
    var digits: Int = 6,
    var counter: Long = 0,
    var otpType: OtpTypes = OtpTypes.TOTP,
    var otpHashFunction: OtpHashFunctions = OtpHashFunctions.SHA1,
    val otpCardColor: OtpCardColors = OtpCardColors.random()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as VaultItem

        if (uuid != other.uuid) return false
        if (period != other.period) return false
        if (digits != other.digits) return false
        if (counter != other.counter) return false
        if (name != other.name) return false
        if (issuer != other.issuer) return false
        if (note != other.note) return false
        if (!secret.contentEquals(other.secret)) return false
        if (otpType != other.otpType) return false
        if (otpHashFunction != other.otpHashFunction) return false
        if (otpCardColor != other.otpCardColor) return false

        return true
    }

    override fun hashCode(): Int {
        var result = uuid.hashCode()
        result = 31 * result + period
        result = 31 * result + digits
        result = 31 * result + counter.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + issuer.hashCode()
        result = 31 * result + note.hashCode()
        result = 31 * result + secret.contentHashCode()
        result = 31 * result + otpType.hashCode()
        result = 31 * result + otpHashFunction.hashCode()
        result = 31 * result + otpCardColor.hashCode()
        return result
    }
}