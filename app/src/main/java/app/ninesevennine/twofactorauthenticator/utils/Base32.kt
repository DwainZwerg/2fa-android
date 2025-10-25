package app.ninesevennine.twofactorauthenticator.utils

object Base32 {
    private const val PADDING = '='

    fun encode(data: ByteArray): String {
        if (data.isEmpty()) return ""

        val output = StringBuilder((data.size * 8 + 4) / 5)
        var buffer = 0L
        var bitsInBuffer = 0

        for (byte in data) {
            buffer = (buffer shl 8) or (byte.toInt() and 0xFF).toLong()
            bitsInBuffer += 8

            while (bitsInBuffer >= 5) {
                val index = ((buffer shr (bitsInBuffer - 5)) and 0x1F).toInt()
                output.append(encodeChar(index))
                bitsInBuffer -= 5
            }
        }

        if (bitsInBuffer > 0) {
            val index = ((buffer shl (5 - bitsInBuffer)) and 0x1F).toInt()
            output.append(encodeChar(index))
        }

        while (output.length % 8 != 0) {
            output.append(PADDING)
        }

        return output.toString()
    }

    private fun encodeChar(value: Int): Char {
        val isLetter = ((25 - value) shr 31).inv() and 1
        val baseChar = (isLetter * 65) or ((1 - isLetter) * (50 - 26))

        return (baseChar + value).toChar()
    }

    fun decode(input: String): ByteArray? {
        if (input.isEmpty()) return null

        val cleanInput = input.trimEnd(PADDING)
        if (cleanInput.isEmpty()) return null

        var hasInvalid = 0
        for (char in cleanInput) {
            val decoded = decodeChar(char.code)
            hasInvalid = hasInvalid or ((decoded shr 8) and 1)
        }

        if (hasInvalid != 0) return null

        val output = mutableListOf<Byte>()
        var buffer = 0L
        var bitsInBuffer = 0

        for (char in cleanInput) {
            val value = decodeChar(char.code) and 0xFF
            buffer = (buffer shl 5) or value.toLong()
            bitsInBuffer += 5

            if (bitsInBuffer >= 8) {
                val byte = ((buffer shr (bitsInBuffer - 8)) and 0xFF).toByte()
                output.add(byte)
                bitsInBuffer -= 8
            }
        }

        if (bitsInBuffer > 0) {
            val remainingBits = buffer and ((1L shl bitsInBuffer) - 1)
            if (remainingBits != 0L) {
                return null
            }
        }

        return output.toByteArray()
    }

    private fun decodeChar(charCode: Int): Int {
        val isUpperLetter = inRange(charCode, 65, 90)
        val isLowerLetter = inRange(charCode, 97, 122)
        val isDigit = inRange(charCode, 50, 55)

        val isValid = isUpperLetter or isLowerLetter or isDigit
        val errorBit = (1 - isValid) shl 8

        val upperValue = (charCode - 65) and (-isUpperLetter)
        val lowerValue = (charCode - 97) and (-isLowerLetter)
        val digitValue = (charCode - 50 + 26) and (-isDigit)

        val value = upperValue or lowerValue or digitValue

        return value or errorBit
    }

    private fun inRange(value: Int, min: Int, max: Int): Int {
        val aboveMin = ((value - min) shr 31).inv() and 1
        val belowMax = ((max - value) shr 31).inv() and 1
        return aboveMin and belowMax
    }
}