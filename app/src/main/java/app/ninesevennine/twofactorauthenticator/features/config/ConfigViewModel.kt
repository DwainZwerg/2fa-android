package app.ninesevennine.twofactorauthenticator.features.config

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import app.ninesevennine.twofactorauthenticator.features.locale.LocaleOption
import app.ninesevennine.twofactorauthenticator.features.theme.ThemeOption

class ConfigViewModel : ViewModel() {
    var global by mutableStateOf(ConfigModel.Config())
        private set

    fun save(context: Context) = global.save(context)
    fun load(context: Context) = ConfigModel.Config.load(context).also { global = it }

    fun updateLocale(value: LocaleOption) {
        global = global.copy(locale = value)
    }

    fun updateTheme(value: ThemeOption) {
        global = global.copy(theme = value)
    }

    fun updateTapToReveal(value: Boolean) {
        global = global.copy(requireTapToReveal = value)
    }

    fun updateFocusSearch(value: Boolean) {
        global = global.copy(enableFocusSearch = value)
    }
}