package com.example.life

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Intent
import android.widget.TextView
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.Volley
import com.android.volley.toolbox.JsonObjectRequest
import android.widget.EditText
import android.app.AlertDialog
import android.content.DialogInterface
import org.json.JSONObject
import java.nio.charset.Charset
import com.android.volley.toolbox.StringRequest
import org.json.JSONArray


class ExistenteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_existente)

        val btnGoToAcceder = findViewById<Button>(R.id.btn_go_to_acceder)
        btnGoToAcceder.setOnClickListener {
            gotoAcceder()
        }
        val tvgoinicio = findViewById<TextView>(R.id.tv_go_to_inicioE)
        tvgoinicio.setOnClickListener {
            goinicio()
        }
    }

    private fun gotoAcceder(){
        val editTextCodigo = findViewById<EditText>(R.id.txt_codigo)
        val codigo = editTextCodigo.text.toString()
        realizarSolicitud(codigo)
    }

    companion object {
        private const val TAG = "ExistenteActiviy"
    }


    private fun goinicio(){
        val i = Intent(this,MainActivity::class.java)
        startActivity(i)
    }


    private fun realizarSolicitud(codigo: String) {
        val colaDeSolicitudes = Volley.newRequestQueue(this)

        val url = "https://hip-koi-logically.ngrok-free.app/Usuario/Usuario"

        // Crear el objeto JSON que deseas enviar
        val jsonBody = JSONObject().apply {
            put("CUSTOM", "MOVIL")
            put("CONFIGURACION_QR", codigo)
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
                    val msg = jsonObject.getString("MSG")


                    when (msg) {
                        "OK" -> {
                            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                                if (!task.isSuccessful) {
                                    Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                                    return@addOnCompleteListener
                                }

                                // Get the token
                                val token = task.result

                                // Log the token
                                Log.d(TAG, "FCM Token: $token")
                                segundaSolicitud(token!!, codigo!!)

                            }
                            val nombre = jsonObject.getString("NOMBRE")
                            val i = Intent(this, AccederActivity::class.java)
                            startActivity(i)
                            i.putExtra("nombre", nombre)
                            startActivity(i)
                        }
                        "NOEXISTE" -> {
                            mostrarAlerta("El código ingresado no existe")
                        }
                        "ERROR" -> {
                            mostrarAlerta("Datos incorrectos")
                        }
                        else -> {
                            Log.d("Respuesta", "MSG no reconocido: $msg")
                        }
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                if (error.networkResponse != null) {
                    when (error.networkResponse.statusCode) {
                        401, 404, 400, 301 -> {
                            mostrarAlerta("Hubo un problema: ${error.networkResponse.statusCode}")
                        }
                        else -> {
                            mostrarAlerta("Hubo un problema con el servidor")
                        }
                    }
                }
                error.printStackTrace()
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

    private fun segundaSolicitud(token: String, codigo: String) {
        val urlSegunda = "https://hip-koi-logically.ngrok-free.app/Contacto/ConfigMovil"

        // Aquí creas y configuras la solicitud según necesites, similar a como hiciste con la primera solicitud.
        val colaDeSolicitudesSegunda = Volley.newRequestQueue(this)

        val jsonBodySegunda = JSONObject().apply {
            put("token", codigo)
            put("imei", "AndreshomoHernandez")
            put("token_firebase", token)
            // Puedes agregar otros campos si son necesarios.
        }

        val solicitudSegunda = object : StringRequest(
            Request.Method.POST, urlSegunda,
            Response.Listener<String> { respuestaSegunda ->
                // Maneja la respuesta de tu segunda solicitud aquí
            },
            Response.ErrorListener { error ->
                // Maneja los errores de tu segunda solicitud aquí
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                // Si necesitas más encabezados, agrégales aquí.
                return headers
            }

            override fun getBody(): ByteArray {
                return jsonBodySegunda.toString().toByteArray()
            }
        }

        colaDeSolicitudesSegunda.add(solicitudSegunda)
    }

    private fun mostrarAlerta(mensaje: String) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(mensaje)
            .setPositiveButton("Aceptar") { dialog: DialogInterface, id: Int ->
                // Cierra el diálogo al hacer clic en "Aceptar"
                dialog.dismiss()
            }
        val dialog = builder.create()
        dialog.show()
    }


}