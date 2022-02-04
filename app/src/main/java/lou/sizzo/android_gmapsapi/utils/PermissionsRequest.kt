package lou.sizzo.android_gmapsapi.utils

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import lou.sizzo.android_gmapsapi.dialogs.Dialogs
import androidx.core.content.*

open class PermissionsRequest {

    fun isStoragePermissionGranted(context: Context): Boolean {
        var condicion = true
        if (Build.VERSION.SDK_INT >= 23) {
            for (i in Dialogs.PERMISSIONS.indices) {
                if (ContextCompat.checkSelfPermission(context, Dialogs.PERMISSIONS[i]) == PackageManager.PERMISSION_GRANTED) {
                    condicion = true
                } else {
                    condicion = false
                    break
                }
            }
        } else {
            condicion = true
        }
        return condicion
    }
}