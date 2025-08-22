package app.ninesevennine.twofactorauthenticator.utils

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import androidx.core.graphics.createBitmap
import com.google.zxing.WriterException
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel

object QRCode {
    fun generateVersion15(content: String): Bitmap? {
        val size = 77 + 4 // + padding

        val hints = mapOf<EncodeHintType, Any>(
            EncodeHintType.MARGIN to 0,
            EncodeHintType.QR_VERSION to 15,
            EncodeHintType.ERROR_CORRECTION to ErrorCorrectionLevel.L
        )

        val bitMatrix: BitMatrix = try {
            MultiFormatWriter().encode(
                content,
                BarcodeFormat.QR_CODE,
                size,
                size,
                hints
            )
        }
        catch (_: WriterException) {
            // Content cannot fit in Version 15
            return null
        }
        catch (e: Exception) {
            Logger.e("QRCode", "Error generating QR code: ${e.stackTraceToString()}")
            return null
        }

        val width = bitMatrix.width
        val height = bitMatrix.height
        val pixels = IntArray(width * height)

        for (y in 0 until height) {
            for (x in 0 until width) {
                pixels[y * width + x] = if (bitMatrix[x, y]) Color.BLACK else Color.WHITE
            }
        }

        return createBitmap(width, height).apply {
            setPixels(pixels, 0, width, 0, 0, width, height)
        }
    }
}