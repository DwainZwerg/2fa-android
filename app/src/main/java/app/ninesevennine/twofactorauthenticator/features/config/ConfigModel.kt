package app.ninesevennine.twofactorauthenticator.features.config

import android.content.Context
import android.util.Log
import app.ninesevennine.twofactorauthenticator.features.locale.LocaleOption
import app.ninesevennine.twofactorauthenticator.features.theme.ThemeOption
import org.json.JSONObject
import java.io.File

object ConfigModel {
    private const val FILE_NAME = "config.json"

    data class Config(
        var locale: LocaleOption = LocaleOption.SYSTEM_DEFAULT,
        var theme: ThemeOption = ThemeOption.SYSTEM_DEFAULT,
        var requirePINUnlock: Boolean = false,
        var requireTapToReveal: Boolean = false,
        var enableFocusSearch: Boolean = false
    ) {
        fun save(context: Context) {
            runCatching {
                JSONObject().apply {
                    put("version", 1)
                    put("locale", locale.value)
                    put("theme", theme.value)
                    put("requirePINUnlock", requirePINUnlock)
                    put("requireTapToReveal", requireTapToReveal)
                    put("enableFocusSearch", enableFocusSearch)
                }.toString().let { jsonString ->
                    File(context.noBackupFilesDir, FILE_NAME)
                        .writeText(jsonString, Charsets.UTF_8)
                }
            }.onFailure { e ->
                Log.e("ConfigModel", "Failed saving config", e)
            }
        }

        companion object {
            fun load(context: Context): Config =
                runCatching {
                    val file = File(context.noBackupFilesDir, FILE_NAME)

                    if (!file.exists() || file.length() == 0L) return Config()

                    val jsonString = file.readText(Charsets.UTF_8)
                    if (jsonString.isBlank()) return Config()

                    val json = JSONObject(jsonString)

                    Config(
                        locale = LocaleOption.fromString(json.getString("locale")),
                        theme = ThemeOption.fromInt(json.getInt("theme")),
                        requirePINUnlock = json.optBoolean("requirePINUnlock", false),
                        requireTapToReveal = json.getBoolean("requireTapToReveal"),
                        enableFocusSearch = json.getBoolean("enableFocusSearch")
                    )
                }.getOrElse { e ->
                    Log.e("ConfigModel", "Failed loading config", e)
                    Config()
                }
        }
    }
}