package lou.sizzo.android_gmapsapi

import android.annotation.SuppressLint
import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import lou.sizzo.android_gmapsapi.databinding.ActivityMainBinding
import lou.sizzo.android_gmapsapi.dialogs.Dialogs
import lou.sizzo.android_gmapsapi.utils.*
import java.util.*

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    lateinit var binding: ActivityMainBinding
    lateinit var mapa: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        if (!PermissionsRequest().isStoragePermissionGranted(this)) {
            Dialogs().bottomDialogCerrarSesion(this, this)
        }


        val mapFragment = fragmentManager.findFragmentById(R.id.map_find) as MapFragment
        mapFragment.getMapAsync(this)
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(map: GoogleMap) {
        try {
            mapa = map
            mapa.isMyLocationEnabled = true
            mapa.uiSettings.isZoomControlsEnabled = false
            mapa.uiSettings.isMyLocationButtonEnabled = false
            mapa.uiSettings.isCompassEnabled = false
            mapa.uiSettings.isRotateGesturesEnabled = false
            mapa.uiSettings.isZoomGesturesEnabled = true

            mapa.clickMap{ loc ->

                mapa.clear()

                val geocoder =  Geocoder(this, Locale.getDefault());
                var addresses: List<Address> = geocoder.getFromLocation(loc.latitude, loc.longitude, 1)
                binding.tvStreet.text = "Calle: ${addresses[0].thoroughfare} ${addresses[0].subThoroughfare}"
                binding.tvCity.text = "Ciudad: ${addresses[0].locality}"
                binding.tvPostalCode.text = "C.P.: ${addresses[0].postalCode}"
                binding.tvState.text = "Estado: ${addresses[0].adminArea}"
                binding.tvCountry.text = "País: ${addresses[0].countryName}"

                val miMarker: View = (getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
                    R.layout.marker_location,
                    null)

                mapa.addMarker(
                    MarkerOptions()
                        .position(loc)
                        .icon(BitmapDescriptorFactory.fromBitmap(Utilidades().createDrawableFromView(this, miMarker)!!))
                        .title("Estoy aqui"))

                binding.lyAddress.fadeVisibility(View.VISIBLE)
            }

            binding.fabLocation.click {
                changeCamera(LatLng(20.66682, -103.39182))
            }
        }catch (e: Exception){
            toast(e.toString())
        }
    }


    private val CAMERA_ZOOM: Float = 16.0F;
    private val CAMERA_TILT: Float = 0.0F;
    private var CAMERA_BEARING: Float = 90.0F;
    fun changeCamera(location: LatLng){
        try {
            mapa.clear()

            val geocoder =  Geocoder(this, Locale.getDefault());
            var addresses: List<Address> = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            binding.tvStreet.text = "Calle: ${addresses[0].thoroughfare} ${addresses[0].subThoroughfare}"
            binding.tvCity.text = "Ciudad: ${addresses[0].locality}"
            binding.tvPostalCode.text = "C.P.: ${addresses[0].postalCode}"
            binding.tvState.text = "Estado: ${addresses[0].adminArea}"
            binding.tvCountry.text = "País: ${addresses[0].countryName}"

            val miMarker: View = (getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
                R.layout.marker_location,
                null)

            mapa.addMarker(
                MarkerOptions()
                    .position(location)
                    .icon(BitmapDescriptorFactory.fromBitmap(Utilidades().createDrawableFromView(this, miMarker)!!))
                    .title("Estoy aqui"))

            binding.lyAddress.fadeVisibility(View.VISIBLE)


            val cameraPosition = CameraPosition.Builder()
                .target(location) // Sets the center of the map to Mountain View
                .zoom(CAMERA_ZOOM)
                .bearing(CAMERA_BEARING)         // Sets the orientation of the camera to east
                .tilt(CAMERA_TILT)            // Sets the tilt of the camera to 30 degrees
                .build()
            mapa.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

        }catch (e: Exception){
            //Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
        }
    }
}