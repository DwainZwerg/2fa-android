package app.ninesevennine.twofactorauthenticator.utils

import org.bouncycastle.crypto.generators.Argon2BytesGenerator
import org.bouncycastle.crypto.params.Argon2Parameters
import java.security.SecureRandom
import kotlin.math.min

object Argon2id {
    private const val DEFAULT_MEMORY_KIB: Int = 64 * 1024 // 64 MiB
    private const val DEFAULT_ITERATIONS: Int = 3
    private val DEFAULT_PARALLELISM: Int = min(Runtime.getRuntime().availableProcessors(), 2)
    private const val DEFAULT_SALT_LEN: Int = 16

    private val secureRandom = SecureRandom()

    fun hash(password: ByteArray, hashLength: Int): Pair<ByteArray, ByteArray> {
        val salt = ByteArray(DEFAULT_SALT_LEN).also { secureRandom.nextBytes(it) }
        val hash = hashWithSalt(password, hashLength, salt)

        return salt to hash
    }

    fun hashWithSalt(password: ByteArray, hashLength: Int, salt: ByteArray): ByteArray {
        val params = Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
            .withSalt(salt)
            .withIterations(DEFAULT_ITERATIONS)
            .withMemoryAsKB(DEFAULT_MEMORY_KIB)
            .withParallelism(DEFAULT_PARALLELISM)
            .build()

        val gen = Argon2BytesGenerator()
        gen.init(params)

        val out = ByteArray(hashLength)
        gen.generateBytes(password, out)

        return out
    }
}