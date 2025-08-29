package app.ninesevennine.twofactorauthenticator

import android.content.Context
import app.ninesevennine.twofactorauthenticator.features.config.ConfigViewModel
import app.ninesevennine.twofactorauthenticator.features.locale.LocaleViewModel
import app.ninesevennine.twofactorauthenticator.features.theme.ThemeViewModel

val Context.configViewModel: ConfigViewModel
    get() = (applicationContext as MyApplication).configViewModel

val Context.localeViewModel: LocaleViewModel
    get() = (applicationContext as MyApplication).localeViewModel

val Context.themeViewModel: ThemeViewModel
    get() = (applicationContext as MyApplication).themeViewModel