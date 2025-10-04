package app.ninesevennine.twofactorauthenticator.features.crypto

import androidx.lifecycle.ViewModel
import app.ninesevennine.twofactorauthenticator.BuildConfig
import app.ninesevennine.twofactorauthenticator.utils.Logger
import javax.crypto.SecretKey

class SecureCryptoViewModel : ViewModel() {
    private val keyAlias = "${BuildConfig.APPLICATION_ID}.secure.crypto.key"
    private var key: SecretKey? = null

    private fun getSecretKey(): SecretKey? {
        return try {
            val keyStore = SecureCryptoModel.getKeyStore()
            keyStore.getKey(keyAlias, null) as? SecretKey
        } catch (e: Exception) {
            Logger.e("SecureCryptoViewModel", "getSecretKey failed: ${e.stackTraceToString()}")
            null
        }
    }

    fun init() {
        try {
            key = getSecretKey() ?: run {
                SecureCryptoModel.generateKey(keyAlias)
                getSecretKey()
            }
        } catch (e: Exception) {
            Logger.e("SecureCryptoViewModel", "init failed: ${e.stackTraceToString()}")
        }
    }

    fun encrypt(data: ByteArray): ByteArray? = key?.let { SecureCryptoModel.encrypt(it, data) }

    fun decrypt(data: ByteArray): ByteArray? = key?.let { SecureCryptoModel.decrypt(it, data) }
}