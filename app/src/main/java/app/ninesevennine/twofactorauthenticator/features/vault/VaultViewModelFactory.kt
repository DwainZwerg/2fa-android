package app.ninesevennine.twofactorauthenticator.features.vault

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class VaultViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass == VaultViewModel::class.java) {
            "Unknown ViewModel class: ${modelClass.simpleName}"
        }

        @Suppress("UNCHECKED_CAST")
        return VaultViewModel(context.applicationContext) as T
    }
}