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
        val isLetter = ((25 - value) ushr 31) xor 1
        val letterMask = -isLetter
        val digitMask = letterMask.inv()

        val baseChar = (letterMask and 65) or (digitMask and 24)
        return (baseChar + value).toChar()
    }

    fun decode(input: String): ByteArray? {
        if (input.isEmpty()) return null

        val cleanInput = input.trimEnd(PADDING)
        if (cleanInput.isEmpty()) return null

        val expectedPadding = when (cleanInput.length % 8) {
            0 -> 0
            2 -> 6
            4 -> 4
            5 -> 3
            7 -> 1
            else -> -1
        }

        if (expectedPadding < 0) return null

        val actualPadding = input.length - cleanInput.length
        if (actualPadding != expectedPadding) return null

        var hasInvalid = 0
        for (char in cleanInput) {
            val decoded = decodeChar(char.code)
            hasInvalid = hasInvalid or ((decoded ushr 8) and 1)
        }

        if (hasInvalid != 0) return null

        val output = ByteArray((cleanInput.length * 5) / 8)
        var outputIndex = 0
        var buffer = 0L
        var bitsInBuffer = 0

        for (char in cleanInput) {
            val value = decodeChar(char.code) and 0xFF
            buffer = (buffer shl 5) or value.toLong()
            bitsInBuffer += 5

            if (bitsInBuffer >= 8) {
                val byte = ((buffer ushr (bitsInBuffer - 8)) and 0xFF).toByte()
                output[outputIndex++] = byte
                bitsInBuffer -= 8
            }
        }

        if (bitsInBuffer > 0) {
            val remainingBits = buffer and ((1L shl bitsInBuffer) - 1)
            if (remainingBits != 0L) {
                output.fill(0)
                return null
            }
        }

        return output
    }

    private fun decodeChar(charCode: Int): Int {
        val isUpperLetter = inRange(charCode, 65, 90)
        val isLowerLetter = inRange(charCode, 97, 122)
        val isDigit = inRange(charCode, 50, 55)

        val isValid = isUpperLetter or isLowerLetter or isDigit
        val errorBit = (1 - isValid) shl 8

        val upperMask = -isUpperLetter
        val lowerMask = -isLowerLetter
        val digitMask = -isDigit

        val upperValue = (charCode - 65) and upperMask
        val lowerValue = (charCode - 97) and lowerMask

        val digitValue = (charCode - 24) and digitMask

        val value = upperValue or lowerValue or digitValue

        return value or errorBit
    }

    private fun inRange(value: Int, min: Int, max: Int): Int {
        val aboveMin = ((value - min) ushr 31) xor 1
        val belowMax = ((max - value) ushr 31) xor 1
        return aboveMin and belowMax
    }
}