package app.ninesevennine.twofactorauthenticator.features.locale

import android.content.Context
import app.ninesevennine.twofactorauthenticator.utils.Logger
import org.json.JSONObject
import java.io.File

object LocaleModel {
    private const val FILE_NAME = "locale.json"

    fun saveLocale(context: Context, localeOption: LocaleOption) {
        runCatching {
            JSONObject().apply {
                put("version", 1)
                put("locale", localeOption.value)
            }.toString().let { jsonString ->
                File(context.noBackupFilesDir, FILE_NAME).writeText(jsonString, Charsets.UTF_8)
            }
        }.onFailure { e ->
            Logger.e("LocaleModel", "Error saving locale: ${e.stackTraceToString()}")
        }
    }

    internal fun parseLocale(jsonString: String): Pair<Int, LocaleOption> {
        val json = JSONObject(jsonString)
        val version = json.getInt("version")
        val localeValue = json.getString("locale")
        return version to LocaleOption.fromString(localeValue)
    }

    fun readLocale(context: Context): LocaleOption {
        val file = File(context.noBackupFilesDir, FILE_NAME)
        if (!file.exists()) return LocaleOption.SYSTEM_DEFAULT

        return runCatching {
            val content = file.readText(Charsets.UTF_8)
            if (content.isBlank()) return LocaleOption.SYSTEM_DEFAULT
            parseLocale(content).let { (_, locale) ->
                // Future version handling logic here
                locale
            }
        }.getOrElse {
            Logger.e("LocaleModel", "Error reading locale: ${it.stackTraceToString()}")
            LocaleOption.SYSTEM_DEFAULT
        }
    }
}