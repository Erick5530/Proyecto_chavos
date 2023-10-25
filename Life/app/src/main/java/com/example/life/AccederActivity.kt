package com.example.life

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.life.databinding.ActivityAccederBinding

class AccederActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAccederBinding
    private var nombre: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        recoverDataIntent()
        setInitValues()
    }

    private fun initBinding() {
        binding = ActivityAccederBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setInitValues(){
        binding.saludoTextView.text = "Todo está bien \n $nombre \n la aplicación funciona correctamente."
    }

    private fun recoverDataIntent() {
        nombre = intent.getStringExtra("nombre") ?: "Desconocido"
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
