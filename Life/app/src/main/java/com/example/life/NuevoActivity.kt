package com.example.life

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.content.Intent
import android.widget.Button


class NuevoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nuevo)


        val tvgoinicio = findViewById<TextView>(R.id.tv_go_to_inicio)
        tvgoinicio.setOnClickListener {
            goinicio()
        }
        val btnGoToAccederN = findViewById<Button>(R.id.btn_go_to_accederN)
        btnGoToAccederN.setOnClickListener {
            gotoAccederN()
        }
    }
    private fun goinicio(){
        val i = Intent(this,MainActivity::class.java)
        startActivity(i)
    }
    private fun gotoAccederN(){
        val i = Intent(this, AccederActivity::class.java)
        startActivity(i)
    }

}