package app.ninesevennine.twofactorauthenticator.features.otp

import app.ninesevennine.twofactorauthenticator.features.vault.VaultItem
import app.ninesevennine.twofactorauthenticator.ui.elements.otpcard.OtpCardColors
import app.ninesevennine.twofactorauthenticator.utils.Base32
import java.net.URI
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

fun otpParser(url: String): VaultItem? {
    val uri = try {
        URI(url)
    } catch (_: Exception) {
        return null
    }
    if (uri.scheme != "otpauth") return null

    val type = uri.host?.lowercase() ?: return null
    val otpType = when (type) {
        "totp" -> OtpTypes.TOTP
        "hotp" -> OtpTypes.HOTP
        else -> return null
    }

    // Decode label ("/Issuer:Account" or just "/Issuer")
    val rawLabel = URLDecoder.decode(uri.path.removePrefix("/"), StandardCharsets.UTF_8)
    val (labelIssuer: String, accountName: String?) = when {
        rawLabel.isBlank() -> return null
        ":" in rawLabel -> {
            val parts = rawLabel.split(":", limit = 2)
            (parts[0]) to parts[1]
        }

        else -> rawLabel to null
    }

    // Parse query params
    val queryParams = uri.query
        ?.split("&")
        ?.mapNotNull {
            val parts = it.split("=", limit = 2)
            if (parts.size == 2)
                parts[0] to URLDecoder.decode(parts[1], StandardCharsets.UTF_8)
            else null
        }
        ?.toMap()
        ?: emptyMap()

    val secret = queryParams["secret"] ?: return null

    val issuer = (queryParams["issuer"] ?: labelIssuer)
        .takeIf { it.isNotBlank() }
        ?: ""

    val algorithm = queryParams["algorithm"]?.uppercase() ?: "SHA1"
    val digits = queryParams["digits"]?.toIntOrNull() ?: 6
    val period = queryParams["period"]?.toIntOrNull() ?: 30
    val counter = queryParams["counter"]?.toLongOrNull() ?: 0L

    val hashFunction = when (algorithm) {
        "SHA256" -> OtpHashFunctions.SHA256
        "SHA512" -> OtpHashFunctions.SHA512
        else -> OtpHashFunctions.SHA1
    }

    @OptIn(ExperimentalUuidApi::class)
    return VaultItem(
        uuid = Uuid.random(),
        name = accountName ?: "",
        issuer = issuer,
        note = "",
        secret = Base32.decode(secret) ?: return null,
        period = period.coerceAtLeast(10),
        digits = digits.coerceIn(4, 10),
        counter = counter.coerceAtLeast(0),
        otpType = otpType,
        otpHashFunction = hashFunction,
        otpCardColor = OtpIssuer.getColor(issuer) ?: OtpCardColors.random()
    )
}
