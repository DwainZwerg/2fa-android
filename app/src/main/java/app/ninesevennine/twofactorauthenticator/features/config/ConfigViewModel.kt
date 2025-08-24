package app.ninesevennine.twofactorauthenticator.features.config

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ConfigViewModel : ViewModel() {
    var global by mutableStateOf(ConfigModel.Config())
        private set

    fun save(context: Context) = global.save(context)
    fun load(context: Context) = ConfigModel.Config.load(context).also { global = it }

    fun updateTapToReveal(value: Boolean) {
        global = global.copy(requireTapToReveal = value)
    }
}