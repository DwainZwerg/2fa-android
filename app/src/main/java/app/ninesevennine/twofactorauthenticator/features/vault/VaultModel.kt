package app.ninesevennine.twofactorauthenticator.features.vault

import android.content.Context
import app.ninesevennine.twofactorauthenticator.features.crypto.SecureCrypto
import app.ninesevennine.twofactorauthenticator.features.otp.OtpHashFunctions
import app.ninesevennine.twofactorauthenticator.features.otp.OtpTypes
import app.ninesevennine.twofactorauthenticator.ui.elements.otpcard.OtpCardColors
import app.ninesevennine.twofactorauthenticator.utils.Argon2id
import app.ninesevennine.twofactorauthenticator.utils.ChaCha20Poly1305
import app.ninesevennine.twofactorauthenticator.utils.Logger
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import kotlin.io.encoding.Base64
import kotlin.random.Random
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

object VaultModel {
    private const val FILE_NAME = "vault.json"

    private fun vaultAsJson(vault: List<VaultItem>): String {
        val json = JSONArray().apply {
            vault.forEach { item ->
                put(JSONObject().apply {
                    put("lastUpdated", item.lastUpdated)
                    if (item.name.isNotEmpty()) put("name", item.name)
                    if (item.issuer.isNotEmpty()) put("issuer", item.issuer)
                    if (item.note.isNotEmpty()) put("note", item.note)
                    put("secret", Base64.encode(item.secret))
                    if (item.otpType != OtpTypes.HOTP) put("period", item.period)
                    put("digits", item.digits)
                    if (item.otpType != OtpTypes.TOTP) put("counter", item.counter)
                    put("otpType", item.otpType.value)
                    put("otpHashFunction", item.otpHashFunction.value)
                    put("otpCardColor", item.otpCardColor.value)
                })
            }
        }.toString()

        return json
    }

    private fun encryptVault(vault: String): ByteArray {
        SecureCrypto.getInstance().encrypt(vault.toByteArray(Charsets.UTF_8))?.let {
            return it
        }
        throw Exception("encrypt returned null")
    }

    fun saveVault(context: Context, vault: List<VaultItem>) {
        Logger.i("VaultModel", "saveVault")

        runCatching {
            JSONObject().apply {
                put("version", 1)
                put("data", Base64.encode(encryptVault(vaultAsJson(vault))))
            }.toString().let { jsonString ->
                File(context.noBackupFilesDir, FILE_NAME).writeText(jsonString, Charsets.UTF_8)
            }
        }.onFailure { e ->
            Logger.e("VaultModel", "Error saving vault: ${e.stackTraceToString()}")
        }
    }

    fun backupVault(vault: List<VaultItem>, password: String): String {
        Logger.i("VaultModel", "BackupVault")

        return runCatching {
            val (salt, key) = Argon2id.hash(
                password.toByteArray(Charsets.UTF_8),
                ChaCha20Poly1305.KEY_SIZE
            )
            val nonce = ChaCha20Poly1305.randomNonce()

            val vaultJsonAsByteArray = vaultAsJson(vault).toByteArray(Charsets.UTF_8)

            val encryptedData = ChaCha20Poly1305.encrypt(vaultJsonAsByteArray, key, nonce)

            JSONObject().apply {
                put("version", 1)
                put("data", Base64.encode(encryptedData))
                put("salt", Base64.encode(salt))
                put("nonce", Base64.encode(nonce))
            }.toString()
        }.onFailure { e ->
            Logger.e("VaultModel", "Error backing up vault: ${e.stackTraceToString()}")
        }.getOrElse { "" }
    }

    private fun decryptVault(dataBase64: String): List<VaultItem> {
        if (dataBase64.isBlank()) return emptyList()

        return runCatching {
            val bytes = try {
                SecureCrypto.getInstance().decrypt(Base64.decode(dataBase64))
                    ?: throw Exception("decrypt returned null")
            } catch (e: Exception) {
                Logger.e("VaultModel", "Failed to decode items base64: ${e.stackTraceToString()}")
                return@runCatching emptyList<VaultItem>()
            }

            val jsonArray = try {
                JSONArray(String(bytes, Charsets.UTF_8))
            } catch (e: Exception) {
                Logger.e(
                    "VaultModel",
                    "Failed to parse items JSON array: ${e.stackTraceToString()}"
                )
                return@runCatching emptyList<VaultItem>()
            }

            val list = mutableListOf<VaultItem>()

            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.optJSONObject(i) ?: continue

                val secret = obj.getString("secret").let { Base64.decode(it) }
                val digits = obj.getInt("digits")
                val otpType = obj.getString("otpType").let { OtpTypes.fromString(it) }
                val otpHashFunction =
                    obj.getString("otpHashFunction").let { OtpHashFunctions.fromString(it) }
                val otpCardColor =
                    obj.getString("otpCardColor").let { OtpCardColors.fromString(it) }

                @OptIn(ExperimentalTime::class)
                val item = VaultItem(
                    id = Random.nextLong(),
                    lastUpdated = obj.optLong(
                        "lastUpdated",
                        Clock.System.now().epochSeconds // Temporarily use current time as fallback
                    ),
                    name = obj.optString("name", ""),
                    issuer = obj.optString("issuer", ""),
                    note = obj.optString("note", ""),
                    secret = secret,
                    period = obj.optInt("period", 30),
                    digits = digits,
                    counter = obj.optLong("counter", 0),
                    otpType = otpType,
                    otpHashFunction = otpHashFunction,
                    otpCardColor = otpCardColor
                )

                list.add(item)
            }

            list
        }.getOrElse { e ->
            Logger.e("VaultModel", "Error decrypting vault: ${e.stackTraceToString()}")
            emptyList()
        }
    }

    internal fun parseVault(jsonString: String): Pair<Int, String> {
        val json = JSONObject(jsonString)
        val version = json.getInt("version")
        val data = json.getString("data")
        return version to data
    }

    fun readVault(context: Context): List<VaultItem> {
        Logger.i("VaultModel", "readVault")

        val file = File(context.noBackupFilesDir, FILE_NAME)
        if (!file.exists()) return emptyList()

        return runCatching {
            val content = file.readText(Charsets.UTF_8)
            if (content.isBlank()) return emptyList()
            parseVault(content).let { (_, data) ->
                decryptVault(data)
            }
        }.getOrElse { e ->
            Logger.e("VaultModel", "Error reading vault: ${e.stackTraceToString()}")
            emptyList()
        }
    }
}