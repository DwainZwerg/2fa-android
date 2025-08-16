package app.ninesevennine.twofactorauthenticator.ui.elements.otpcard

import androidx.compose.ui.graphics.Color

data class OtpCardPalette(
    val firstColor: Color,
    val secondColor: Color,
    val thirdColor: Color
)

fun otpLightRedPalette() = OtpCardPalette(
    firstColor = Color(0xFFFFD6D5),
    secondColor = Color(0xFFD32F2F),
    thirdColor = Color(0xFF8A1E1E)
)

fun otpDarkRedPalette() = OtpCardPalette(
    firstColor  = Color(0xFF111012),
    secondColor = Color(0xFFB3261E),
    thirdColor  = Color(0xFFFFB4AB)
)

fun otpLightGreenPalette() = OtpCardPalette(
    firstColor = Color(0xFFD9EFE2),
    secondColor = Color(0xFF28A160),
    thirdColor = Color(0xFF054E36)
)

fun otpDarkGreenPalette() = OtpCardPalette(
    firstColor  = Color(0xFF071A15),
    secondColor = Color(0xFF2D7A4E),
    thirdColor  = Color(0xFFA8EBD0)
)

fun otpLightBluePalette() = OtpCardPalette(
    firstColor = Color(0xFFD9DEFF),
    secondColor = Color(0xFF383EA0),
    thirdColor = Color(0xFF122A7A)
)

fun otpDarkBluePalette() = OtpCardPalette(
    firstColor  = Color(0xFF0B1020),
    secondColor = Color(0xFF4451C9),
    thirdColor  = Color(0xFFDCE7FF)
)

fun otpLightPinkPalette() = OtpCardPalette(
    firstColor = Color(0xFFFFD6E0),
    secondColor = Color(0xFFCC356E),
    thirdColor = Color(0xFF6E1F4A)
)

fun otpDarkPinkPalette() = OtpCardPalette(
    firstColor  = Color(0xFF141014),
    secondColor = Color(0xFFC01363),
    thirdColor  = Color(0xFFFFC8DC)
)

fun otpLightOrangePalette() = OtpCardPalette(
    firstColor = Color(0xFFFFE4C8),
    secondColor = Color(0xFFFA9F20),
    thirdColor = Color(0xFF7A3F14)
)

fun otpDarkOrangePalette() = OtpCardPalette(
    firstColor  = Color(0xFF24120A),
    secondColor = Color(0xFFCF6A00),
    thirdColor  = Color(0xFFFFD7A8)
)

fun otpLightBrownPalette() = OtpCardPalette(
    firstColor = Color(0xFFEDE3DE),
    secondColor = Color(0xFF6D4C41),
    thirdColor = Color(0xFF432A24)
)

fun otpDarkBrownPalette() = OtpCardPalette(
    firstColor  = Color(0xFF120B08),
    secondColor = Color(0xFF7B4E3A),
    thirdColor  = Color(0xFFDCCFC7)
)