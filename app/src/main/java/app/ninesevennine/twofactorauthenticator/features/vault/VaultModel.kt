package app.ninesevennine.twofactorauthenticator.features.vault

import android.content.Context
import app.ninesevennine.twofactorauthenticator.features.crypto.SecureCrypto
import app.ninesevennine.twofactorauthenticator.features.otp.OtpHashFunctions
import app.ninesevennine.twofactorauthenticator.features.otp.OtpTypes
import app.ninesevennine.twofactorauthenticator.ui.elements.otpcard.OtpCardColors
import app.ninesevennine.twofactorauthenticator.utils.Logger
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import kotlin.io.encoding.Base64
import kotlin.random.Random

object VaultModel {
    private const val FILE_NAME = "vault.json"

    private fun encryptVault(vault: List<VaultItem>): String {
        val byteArray = JSONArray().apply {
            vault.forEach { item ->
                put(JSONObject().apply {
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
        }.toString().toByteArray(Charsets.UTF_8)

        SecureCrypto.getInstance().encrypt(byteArray)?.let {
            return Base64.encode(it)
        }

        throw Exception("encrypt returned null")
    }

    fun saveVault(context: Context, vault: List<VaultItem>) {
        Logger.i("VaultModel", "Saving vault")

        runCatching {
            JSONObject().apply {
                put("version", 1)
                put("data", encryptVault(vault))
            }.toString().let { jsonString ->
                File(context.noBackupFilesDir, FILE_NAME).writeText(jsonString, Charsets.UTF_8)
            }
        }.onFailure { e ->
            Logger.e("VaultModel", "Error saving vault: ${e.stackTraceToString()}")
        }
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

                val item = VaultItem(
                    id = Random.nextInt(),
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
        Logger.i("VaultModel", "Reading vault")

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