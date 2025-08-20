package app.ninesevennine.twofactorauthenticator.features.locale

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import androidx.annotation.StringRes
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import app.ninesevennine.twofactorauthenticator.utils.Logger
import java.util.Locale

class LocaleViewModel(
    @Suppress("StaticFieldLeak")
    private val context: Context
) : ViewModel() {
    var localeOption by mutableStateOf(LocaleModel.readLocale(context))
        private set

    private var _effectiveLocale by mutableStateOf(computeEffectiveLocale(localeOption))
    val effectiveLocale: String get() = _effectiveLocale

    private val stringCache = mutableMapOf<Int, String>()

    init {
        _effectiveLocale = computeEffectiveLocale(localeOption)
    }

    fun getLocalizedString(@StringRes resourceId: Int): String {
        return stringCache.getOrPut(resourceId) {
            Configuration(context.resources.configuration).run {
                setLocale(Locale.forLanguageTag(effectiveLocale))
                context.createConfigurationContext(this).getString(resourceId)
            }
        }
    }

    fun updateLocale(newOption: LocaleOption) {
        Logger.i("LocaleViewModel", "updateLocale $newOption")
        LocaleModel.saveLocale(context, newOption)

        if (localeOption == newOption) return

        localeOption = newOption

        val newEffectiveLocale = computeEffectiveLocale(newOption)
        if (_effectiveLocale != newEffectiveLocale) {
            _effectiveLocale = newEffectiveLocale
            stringCache.clear()
        }
    }

    private fun computeEffectiveLocale(option: LocaleOption): String = when (option) {
        LocaleOption.SYSTEM_DEFAULT -> {
            LocaleOption.fromLanguageOrDefault(
                Resources.getSystem().configuration.locales[0]?.language ?: "en"
            ).value
        }
        else -> option.value
    }
}