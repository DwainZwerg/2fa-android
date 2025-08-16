package app.ninesevennine.twofactorauthenticator.features.theme

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ThemeViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass == ThemeViewModel::class.java) {
            "Unknown ViewModel class: ${modelClass.simpleName}"
        }

        @Suppress("UNCHECKED_CAST")
        return ThemeViewModel(context.applicationContext) as T
    }
}