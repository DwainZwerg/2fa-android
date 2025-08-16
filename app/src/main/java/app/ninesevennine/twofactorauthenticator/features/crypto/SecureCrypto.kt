package app.ninesevennine.twofactorauthenticator.features.crypto

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import app.ninesevennine.twofactorauthenticator.BuildConfig
import app.ninesevennine.twofactorauthenticator.utils.Logger
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

class SecureCrypto private constructor(@Suppress("UNUSED_PARAMETER") context: Context) {
    companion object {
        @Volatile
        private var INSTANCE: SecureCrypto? = null

        fun initialize(context: Context) {
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: SecureCrypto(context.applicationContext).also { INSTANCE = it }
            }
        }

        fun getInstance(): SecureCrypto {
            return INSTANCE ?: throw IllegalStateException(
                "SecureCrypto not initialized. Call initialize() first."
            )
        }
    }

    private val keyStore: KeyStore = KeyStore.getInstance("AndroidKeyStore").apply {
        load(null)
    }

    private val keyAlias = "${BuildConfig.APPLICATION_ID}.secure.crypto.key"
    private val ivLength = 12
    private val gcmTagLength = 128
    private val encryptionAlgorithm = "AES/GCM/NoPadding"

    init {
        initializeKey()
    }

    private fun initializeKey() {
        if (!keyStore.containsAlias(keyAlias)) {
            generateStrongBoxKey()
        }
    }

    private fun generateStrongBoxKey() {
        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES,
            "AndroidKeyStore"
        )

        val builder = KeyGenParameterSpec.Builder(
            keyAlias,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        ).apply {
            setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            setKeySize(256)
            setRandomizedEncryptionRequired(true)
            setUserAuthenticationRequired(false) // Disabled for now
            setUnlockedDeviceRequired(true)
            setIsStrongBoxBacked(true)
        }

        try {
            keyGenerator.init(builder.build())
            keyGenerator.generateKey()
        } catch (_: Exception) {
            Logger.w("SecureCrypto", "Falling back to Trusted Execution Environment")

            builder.setIsStrongBoxBacked(false)
            keyGenerator.init(builder.build())
            keyGenerator.generateKey()
        }
    }

    private fun getSecretKey(): SecretKey {
        val entry = keyStore.getEntry(keyAlias, null) as KeyStore.SecretKeyEntry
        return entry.secretKey
    }

    fun encrypt(data: ByteArray): ByteArray? {
        return try {
            val cipher = Cipher.getInstance(encryptionAlgorithm)
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey())
            val iv = cipher.iv
            val ciphertext = cipher.doFinal(data)
            iv + ciphertext
        } catch (e: Exception) {
            Logger.e("SecureCrypto", "Error encrypting data: ${e.stackTraceToString()}")
            null
        }
    }

    fun decrypt(encryptedData: ByteArray): ByteArray? {
        if (encryptedData.size < ivLength) return null

        return try {
            val iv = encryptedData.copyOfRange(0, ivLength)
            val ciphertext = encryptedData.copyOfRange(ivLength, encryptedData.size)

            val cipher = Cipher.getInstance(encryptionAlgorithm)
            val spec = GCMParameterSpec(gcmTagLength, iv)
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), spec)

            cipher.doFinal(ciphertext)
        } catch (e: Exception) {
            Logger.e("SecureCrypto", "Error decrypting data: ${e.stackTraceToString()}")
            null
        }
    }
}