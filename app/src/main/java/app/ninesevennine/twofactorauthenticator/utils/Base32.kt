package app.ninesevennine.twofactorauthenticator.utils

object Base32 {
    // Standard Base32 alphabet (RFC 4648)
    private const val ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567"
    private const val PADDING = '='

    // Lookup table for decoding - maps ASCII values to Base32 values
    private val DECODE_TABLE = IntArray(128) { -1 }.apply {
        ALPHABET.forEachIndexed { index, char ->
            this[char.code] = index
            // Support lowercase as well
            this[char.lowercaseChar().code] = index
        }
    }

    /**
     * Encodes a byte array to Base32 string
     */
    fun encode(data: ByteArray): String {
        if (data.isEmpty()) return ""

        val output = StringBuilder((data.size * 8 + 4) / 5) // Pre-calculate size
        var buffer = 0L
        var bitsInBuffer = 0

        for (byte in data) {
            buffer = (buffer shl 8) or (byte.toInt() and 0xFF).toLong()
            bitsInBuffer += 8

            // Extract 5-bit chunks
            while (bitsInBuffer >= 5) {
                val index = ((buffer shr (bitsInBuffer - 5)) and 0x1F).toInt()
                output.append(ALPHABET[index])
                bitsInBuffer -= 5
            }
        }

        // Handle remaining bits
        if (bitsInBuffer > 0) {
            val index = ((buffer shl (5 - bitsInBuffer)) and 0x1F).toInt()
            output.append(ALPHABET[index])
        }

        // Add padding to make length multiple of 8
        while (output.length % 8 != 0) {
            output.append(PADDING)
        }

        return output.toString()
    }

    /**
     * Decodes a Base32 string to byte array
     * Returns null if input is invalid
     */
    fun decode(input: String): ByteArray? {
        if (input.isEmpty()) return null

        // Remove padding and validate length
        val cleanInput = input.trimEnd(PADDING)
        if (cleanInput.isEmpty()) return null

        // Validate characters
        for (char in cleanInput) {
            if (char.code >= DECODE_TABLE.size || DECODE_TABLE[char.code] == -1) {
                return null // Invalid character
            }
        }

        val output = mutableListOf<Byte>()
        var buffer = 0L
        var bitsInBuffer = 0

        for (char in cleanInput) {
            val value = DECODE_TABLE[char.code]
            buffer = (buffer shl 5) or value.toLong()
            bitsInBuffer += 5

            // Extract complete bytes
            if (bitsInBuffer >= 8) {
                val byte = ((buffer shr (bitsInBuffer - 8)) and 0xFF).toByte()
                output.add(byte)
                bitsInBuffer -= 8
            }
        }

        // Validate that remaining bits are zeros (proper padding)
        if (bitsInBuffer > 0) {
            val remainingBits = buffer and ((1L shl bitsInBuffer) - 1)
            if (remainingBits != 0L) {
                return null // Invalid padding bits
            }
        }

        return output.toByteArray()
    }
}