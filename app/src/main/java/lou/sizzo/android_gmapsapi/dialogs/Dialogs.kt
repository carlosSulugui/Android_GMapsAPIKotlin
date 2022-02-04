package lou.sizzo.android_gmapsapi.dialogs

import android.Manifest
import android.content.Context
import android.view.LayoutInflater
import com.google.android.material.bottomsheet.BottomSheetDialog
import android.app.Activity
import androidx.core.app.ActivityCompat
import lou.sizzo.android_gmapsapi.databinding.BottomSheetPermissionsBinding

class Dialogs {

    companion object{
        var PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        var PERMISSION_ALL = 1
    }

    fun bottomDialogCerrarSesion(context: Context, activity: Activity) {

        var bindingLogout: BottomSheetPermissionsBinding =
            BottomSheetPermissionsBinding.inflate(LayoutInflater.from(context))
        val dialogBSDPermissions = BottomSheetDialog(context)
        dialogBSDPermissions.setCancelable(false)

        bindingLogout.btnCancelar.setOnClickListener {
            (context as Activity).finish()
            dialogBSDPermissions.dismiss()
        }
        bindingLogout.btnAceptarPermisos.setOnClickListener {
            ActivityCompat.requestPermissions(activity, PERMISSIONS, PERMISSION_ALL)
            dialogBSDPermissions.dismiss()
        }
        dialogBSDPermissions.setContentView(bindingLogout.root)
        dialogBSDPermissions.show()
    }
}