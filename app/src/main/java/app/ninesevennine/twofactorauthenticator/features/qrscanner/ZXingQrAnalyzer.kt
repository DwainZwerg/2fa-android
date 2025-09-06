package app.ninesevennine.twofactorauthenticator.features.qrscanner

import android.graphics.ImageFormat
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import app.ninesevennine.twofactorauthenticator.utils.Logger
import com.google.zxing.BarcodeFormat
import com.google.zxing.BinaryBitmap
import com.google.zxing.DecodeHintType
import com.google.zxing.MultiFormatReader
import com.google.zxing.PlanarYUVLuminanceSource
import com.google.zxing.ReaderException
import com.google.zxing.common.HybridBinarizer
import java.util.EnumMap
import kotlin.math.min

class ZXingQrAnalyzer(
    private val viewfinderPercent: Float,
    private val onQrCodeScanned: (String) -> Unit
) : ImageAnalysis.Analyzer {
    private val reader = MultiFormatReader().apply {
        val hints = EnumMap<DecodeHintType, Any>(DecodeHintType::class.java)
        hints[DecodeHintType.POSSIBLE_FORMATS] = listOf(BarcodeFormat.QR_CODE)
        setHints(hints)
    }

    private var imageBuffer = ByteArray(0)

    override fun analyze(imageProxy: ImageProxy) {
        if (imageProxy.format != ImageFormat.YUV_420_888) {
            imageProxy.close()
            return
        }

        try {
            val plane = imageProxy.planes[0]
            val buffer = plane.buffer
            val dataSize = buffer.remaining()

            // Reallocate buffer only when necessary
            if (imageBuffer.size < dataSize) {
                imageBuffer = ByteArray(dataSize)
            }

            buffer.get(imageBuffer, 0, dataSize)

            val minDim = min(imageProxy.width, imageProxy.height)
            val roiSize = (minDim * viewfinderPercent).toInt()
            val left = (imageProxy.width - roiSize) / 2
            val top = (imageProxy.height - roiSize) / 2

            val source = PlanarYUVLuminanceSource(
                imageBuffer,
                plane.rowStride,
                imageProxy.height,
                left.coerceAtLeast(0),
                top.coerceAtLeast(0),
                roiSize.coerceAtMost(imageProxy.width - left),
                roiSize.coerceAtMost(imageProxy.height - top),
                false
            )

            processImage(source)
        } catch (e: Exception) {
            Logger.e("ZxingQrAnalyzer", "Analyze error: ${e.stackTraceToString()}")
        } finally {
            imageProxy.close()
        }
    }

    private fun processImage(source: PlanarYUVLuminanceSource) {
        val bitmap = BinaryBitmap(HybridBinarizer(source))

        try {
            reader.decodeWithState(bitmap)?.text?.let(onQrCodeScanned)
        } catch (_: ReaderException) {
            try {
                val inverted = BinaryBitmap(HybridBinarizer(source.invert()))
                reader.decodeWithState(inverted)?.text?.let(onQrCodeScanned)
            } catch (_: ReaderException) {
                // Both attempts failed
            }
        } finally {
            reader.reset()
        }
    }
}