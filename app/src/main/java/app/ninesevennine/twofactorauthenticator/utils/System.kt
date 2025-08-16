package app.ninesevennine.twofactorauthenticator.utils

import android.annotation.SuppressLint

object System {
    @SuppressLint("PrivateApi")
    fun getSystemProperty(key: String): String {
        return try {
            val spClass = Class.forName("android.os.SystemProperties")
            val getMethod = spClass.getMethod("get", String::class.java)
            (getMethod.invoke(null, key) as? String).orEmpty()
        } catch (_: Throwable) {
            ""
        }
    }
}