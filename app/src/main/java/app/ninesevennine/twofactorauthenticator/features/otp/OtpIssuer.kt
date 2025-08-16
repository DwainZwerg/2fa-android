package app.ninesevennine.twofactorauthenticator.features.otp

import app.ninesevennine.twofactorauthenticator.R
import app.ninesevennine.twofactorauthenticator.ui.elements.otpcard.OtpCardColors

object OtpIssuer {

    fun getIcon(issuer: String): Int? {
        return ISSUER_ICON_MAP[issuer.lowercase()]
    }

    fun getColor(issuer: String): OtpCardColors? {
        return ISSUER_COLOR_MAP[issuer.lowercase()]
    }

    private val ISSUER_ICON_MAP = mapOf(
        "amazon" to R.drawable.issuer_amazon,
        "cloudflare" to R.drawable.issuer_cloudflare,
        "discord" to R.drawable.issuer_discord,
        "google" to R.drawable.issuer_google,
        "posteo.de" to R.drawable.issuer_posteo,
        "wise" to R.drawable.issuer_wise
    )

    private val ISSUER_COLOR_MAP = mapOf(
        "amazon" to OtpCardColors.BROWN,
        "cloudflare" to OtpCardColors.ORANGE,
        "discord" to OtpCardColors.BLUE,
        "google" to OtpCardColors.BLUE,
        "posteo.de" to OtpCardColors.GREEN,
        "wise" to OtpCardColors.GREEN
    )
}