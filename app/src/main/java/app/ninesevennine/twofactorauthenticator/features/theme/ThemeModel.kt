package app.ninesevennine.twofactorauthenticator.features.theme

import android.content.Context
import app.ninesevennine.twofactorauthenticator.utils.Logger
import org.json.JSONObject
import java.io.File

object ThemeModel {
    private const val FILE_NAME = "theme.json"

    fun saveTheme(context: Context, themeOption: ThemeOption) {
        runCatching {
            JSONObject().apply {
                put("version", 1)
                put("theme", themeOption.value)
            }.toString().let { jsonString ->
                File(context.noBackupFilesDir, FILE_NAME).writeText(jsonString, Charsets.UTF_8)
            }
        }.onFailure { e ->
            Logger.e("ThemeModel", "Error saving theme: ${e.stackTraceToString()}")
        }
    }

    internal fun parseTheme(jsonString: String): Pair<Int, ThemeOption> {
        val json = JSONObject(jsonString)
        val version = json.getInt("version")
        val themeValue = json.getInt("theme")
        return version to ThemeOption.fromInt(themeValue)
    }

    fun readTheme(context: Context): ThemeOption {
        val file = File(context.noBackupFilesDir, FILE_NAME)
        if (!file.exists()) return ThemeOption.SYSTEM_DEFAULT

        return runCatching {
            val content = file.readText(Charsets.UTF_8)
            if (content.isBlank()) return ThemeOption.SYSTEM_DEFAULT
            parseTheme(content).let { (_, theme) ->
                // Future version handling logic here
                theme
            }
        }.getOrElse {
            Logger.e("ThemeModel", "Error reading theme: ${it.stackTraceToString()}")
            ThemeOption.SYSTEM_DEFAULT
        }
    }
}