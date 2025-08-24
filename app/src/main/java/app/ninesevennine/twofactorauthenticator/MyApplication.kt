package app.ninesevennine.twofactorauthenticator

import android.app.Application
import app.ninesevennine.twofactorauthenticator.features.config.ConfigViewModel

class MyApplication : Application() {
    val configViewModel = ConfigViewModel()


    override fun onCreate() {
        super.onCreate()

        configViewModel.load(this)
    }
}