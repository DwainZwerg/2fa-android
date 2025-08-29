package app.ninesevennine.twofactorauthenticator

import android.app.Application
import app.ninesevennine.twofactorauthenticator.features.config.ConfigViewModel
import app.ninesevennine.twofactorauthenticator.features.locale.LocaleViewModel
import app.ninesevennine.twofactorauthenticator.features.theme.ThemeViewModel

class MyApplication : Application() {
    val configViewModel = ConfigViewModel()
    val localeViewModel = LocaleViewModel()
    val themeViewModel = ThemeViewModel()


    override fun onCreate() {
        super.onCreate()

        configViewModel.load(this)
        localeViewModel.updateLocale(this, configViewModel.values.locale)
        themeViewModel.updateTheme(this, configViewModel.values.theme)
    }
}