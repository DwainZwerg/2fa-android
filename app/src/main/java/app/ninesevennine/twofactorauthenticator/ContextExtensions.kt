package app.ninesevennine.twofactorauthenticator

import android.content.Context
import app.ninesevennine.twofactorauthenticator.features.config.ConfigViewModel

val Context.config: ConfigViewModel
    get() = (applicationContext as MyApplication).configViewModel