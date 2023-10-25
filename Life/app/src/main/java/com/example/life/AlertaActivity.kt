package com.example.life

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar
import android.app.AlertDialog
import android.content.DialogInterface
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject
import java.nio.charset.Charset


class AlertaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_acceder)

        // Inicializamos 'nombre' con un valor por defecto
        var nombre = "Desconocido"

        // Realizamos la solicitud y actualizamos 'nombre' cuando recibamos la ubicación
        realizarSolicitud("57DB0A") { ubicacion ->
            nombre = ubicacion
            actualizarUI(nombre)
        }
    }

    // Actualiza la UI con el nuevo valor de 'nombre'
    private fun actualizarUI(nombre: String) {
        val saludoTextView: TextView = findViewById(R.id.saludoTextView)
        val estadoTextView: TextView = findViewById(R.id.estadoTextView)

        val calendar = Calendar.getInstance()
        val hora = calendar.get(Calendar.HOUR_OF_DAY)
        val estado = "Todo MAL"

        val saludo: String = when (hora) {
            in 6..11 -> "Buenos Días, $nombre"
            in 12..17 -> "Buenas Tardes, $nombre"
            else -> "Buenas Noches, $nombre"
        }

        saludoTextView.text = saludo
        estadoTextView.text = "El estado de $nombre es $estado"
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setTitle("Salir")
            .setMessage("¿Estás seguro de que deseas salir de la aplicación?")
            .setPositiveButton("OK", DialogInterface.OnClickListener { _, _ ->
                finishAffinity()
            })
            .setNegativeButton("No", null)
            .show()
    }

    private fun realizarSolicitud(codigo: String, callback: (String) -> Unit) {
        val colaDeSolicitudes = Volley.newRequestQueue(this)
        val url = "https://hip-koi-logically.ngrok-free.app/Contacto/VerUltimaNotificacion"

        val jsonBody = JSONObject().apply {
            put("token", codigo)
        }

        val solicitud = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener<String> { respuesta ->
                if (respuesta.isEmpty()) {
                    mostrarAlerta("Respuesta vacía del servidor")
                    return@Listener
                }
                try {
                    val jsonArray = JSONArray(respuesta)
                    val jsonObject = jsonArray.getJSONObject(0)
                    val UBICACION = jsonObject.getString("UBICACION")
                    callback(UBICACION)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                // Manejo de errores
            }
        ) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return jsonBody.toString().toByteArray(Charset.forName("utf-8"))
            }
        }

        colaDeSolicitudes.add(solicitud)
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
}
