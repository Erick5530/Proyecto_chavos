package com.example.life

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.life.databinding.ActivityAlertaBinding
import com.example.life.viewmodel.ViewModelAlertaAcceder
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class AlertaActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityAlertaBinding
    private lateinit var viewModel: ViewModelAlertaAcceder

    private var mapaG: SupportMapFragment? = null
    private var googleMap: GoogleMap? = null

    private var extraCoordinates: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        recoverDataIntent()
        initObservers()
        initConfigForMap()
    }

    private fun initBinding() {
        viewModel = ViewModelProvider(this)[ViewModelAlertaAcceder::class.java]
        binding = ActivityAlertaBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun recoverDataIntent() {
        extraCoordinates = intent.getStringExtra("ubi")
    }

    private fun initObservers() {
        viewModel.lastUbication.observe(this) { coordinates ->
            setCoordinatesInMap(coordinates)
        }
    }

    private fun setCoordinatesInMap(coordinates: String) {
        this.googleMap?.addMarker(
            MarkerOptions()
                .position(
                    LatLng(
                        coordinates.split(",")[0].trim().toDouble(),
                        coordinates.split(",")[1].trim().toDouble()
                    )
                )
                .title("Lo que quieren que se vea en la ventana al presionar el marker despues del servicio")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        )

        val location = CameraUpdateFactory.newLatLngZoom(
            LatLng(
                coordinates.split(",")[0].trim().toDouble(),
                coordinates.split(",")[1].trim().toDouble()
            ), 15f
        )
        this.googleMap?.animateCamera(location)

    }

    private fun requestLastUbication(token: String) {
        viewModel.getLastUbicationService(
            context = this,
            token = token,
            callback = { isOk, message ->
                runOnUiThread {
                    Toast.makeText(
                        this, if (isOk) "Consulta correcta"
                        else "Error en consulta: $message", Toast.LENGTH_SHORT
                    ).show()
                }

            }
        )
    }

    private fun initConfigForMap() {

        mapaG = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapaG?.getMapAsync(this)

        val options = GoogleMapOptions()
        options.useViewLifecycleInFragment(true)
        mapaG = SupportMapFragment.newInstance(options)
    }


    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setTitle("Salir")
            .setMessage("¿Estás seguro de que deseas salir de la aplicación?")
            .setPositiveButton("OK") { _, _ ->
                finishAffinity()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun mostrarAlerta(mensaje: String) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(mensaje)
            .setPositiveButton("Aceptar") { dialog: DialogInterface, id: Int ->
                dialog.dismiss()
            }
        val dialog = builder.create()
        dialog.show()
    }


    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        println("Se cargo el mapa correctamente en alerta")

        if (extraCoordinates == null) {
            requestLastUbication("57DB0A")
        } else {
            viewModel.setNewCoordinates(extraCoordinates ?: "0.0,0.0")
        }


    }
}
