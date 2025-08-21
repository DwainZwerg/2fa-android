package app.ninesevennine.twofactorauthenticator.utils

import org.bouncycastle.crypto.generators.Argon2BytesGenerator
import org.bouncycastle.crypto.params.Argon2Parameters
import java.security.SecureRandom

object Argon2id {
    const val DEFAULT_M: Int = 96 * 1024 // 96 MiB
    const val DEFAULT_T: Int = 3 // time cost
    const val DEFAULT_P: Int = 2 // parallelism

    fun get(
        password: ByteArray,
        salt: ByteArray,
        outLength: Int,
        m: Int = DEFAULT_M,
        t: Int = DEFAULT_T,
        p: Int = DEFAULT_P
    ): ByteArray {
        val params = Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
            .withSalt(salt)
            .withMemoryAsKB(m)
            .withParallelism(t)
            .withIterations(p)
            .build()

        val generator = Argon2BytesGenerator()
        generator.init(params)

        val output = ByteArray(outLength)
        generator.generateBytes(password, output, 0, output.size)

        return output
    }

    fun generateSalt(sizeBytes: Int = 16): ByteArray =
        SecureRandom().generateSeed(sizeBytes)
}