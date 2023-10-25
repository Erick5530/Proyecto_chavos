package com.example.life

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import android.app.AlertDialog
import android.content.DialogInterface

class AccederActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_acceder)

        val saludoTextView: TextView = findViewById(R.id.saludoTextView)
        val estadoTextView: TextView = findViewById(R.id.estadoTextView)

        val calendar = Calendar.getInstance()
        val hora = calendar.get(Calendar.HOUR_OF_DAY)

        val nombre = intent.getStringExtra("nombre")
        val estado = "Todo Bien"

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
}
