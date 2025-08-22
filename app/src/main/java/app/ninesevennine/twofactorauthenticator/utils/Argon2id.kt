package app.ninesevennine.twofactorauthenticator.utils

import org.bouncycastle.crypto.generators.Argon2BytesGenerator
import org.bouncycastle.crypto.params.Argon2Parameters
import java.security.SecureRandom

object Argon2id {
    const val DEFAULT_M: Int = 128 * 1024 // 128 MiB
    const val DEFAULT_T: Int = 4 // time cost
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
            .withIterations(t)
            .withParallelism(p)
            .build()

        val gen = Argon2BytesGenerator()
        gen.init(params)

        val out = ByteArray(outLength)
        gen.generateBytes(password, out)

        return out
    }

    fun generateSalt(sizeBytes: Int = 16): ByteArray =
        SecureRandom().generateSeed(sizeBytes)
}