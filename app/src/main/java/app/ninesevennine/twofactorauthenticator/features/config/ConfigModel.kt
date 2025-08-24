package app.ninesevennine.twofactorauthenticator.features.config

import android.content.Context
import android.util.Log
import org.json.JSONObject
import java.io.File

object ConfigModel {
    private const val FILE_NAME = "config.json"

    data class Config(
        var requireTapToReveal: Boolean = false
    ) {
        fun save(context: Context) {
            runCatching {
                JSONObject().apply {
                    put("version", 1)
                    put("requireTapToReveal", requireTapToReveal)
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
                        requireTapToReveal = json.getBoolean("requireTapToReveal")
                    )
                }.getOrElse { e ->
                    Log.e("ConfigModel", "Failed loading config", e)
                    Config()
                }
        }
    }
}