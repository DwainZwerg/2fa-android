package app.ninesevennine.twofactorauthenticator.features.locale

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class LocaleViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass == LocaleViewModel::class.java) {
            "Unknown ViewModel class: ${modelClass.simpleName}"
        }

        @Suppress("UNCHECKED_CAST")
        return LocaleViewModel(context.applicationContext) as T
    }
}