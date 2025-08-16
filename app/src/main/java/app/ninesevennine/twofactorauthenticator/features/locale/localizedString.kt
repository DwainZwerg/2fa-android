package app.ninesevennine.twofactorauthenticator.features.locale

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import app.ninesevennine.twofactorauthenticator.LocalLocaleViewModel

// This function exists to prevent localization from breaking when the theme changes.
// Using localeViewModel.getLocalizedString(resourceId) directly outside a @Composable
// does not trigger recomposition on theme or locale changes, causing stale strings.
// Marking this as @Composable and using remember(locale) ensures the localized string
// updates correctly whenever the locale (or theme) changes.
@Composable
fun localizedString(@StringRes resourceId: Int): String {
    val localeViewModel = LocalLocaleViewModel.current

    val locale = localeViewModel.effectiveLocale

    return remember(locale) {
        localeViewModel.getLocalizedString(resourceId)
    }
}