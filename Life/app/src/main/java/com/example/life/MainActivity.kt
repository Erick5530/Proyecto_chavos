package com.example.life


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Intent
import android.webkit.WebView
import android.webkit.WebViewClient
import android.net.Uri
import android.view.View
import android.widget.TextView
import com.example.life.ui.theme.SomosActivity
import com.example.life.util.PreferenceHelper
import com.example.life.util.PreferenceHelper.get
import com.example.life.util.PreferenceHelper.set

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val preferences = PreferenceHelper.defaultPrefs(this)
        if(preferences["session", false])
            gotoAcceder()

        val btnGoToNuevo = findViewById<Button>(R.id.btn_go_to_nuevo)
        btnGoToNuevo.setOnClickListener {
            gotoNuevo()
        }

        val btn_go_to_existente = findViewById<Button>(R.id.btn_go_to_existente)
        btn_go_to_existente.setOnClickListener {
            gotoExistente()
        }

        val btn_go_to_Webpage = findViewById<TextView>(R.id.btn_go_to_webpage)
        btn_go_to_Webpage.setOnClickListener {
            gotoWebpage()
        }



    }
    private fun gotoNuevo(){
        val i = Intent(this,NuevoActivity::class.java)
        startActivity(i)
    }
    private fun gotoExistente(){
        val i = Intent(this,ExistenteActivity::class.java)
        startActivity(i)
    }
    private fun gotoWebpage() {
            val i = Intent(this,SomosActivity::class.java)
            startActivity(i)
    }
    private fun gotoAcceder(){
        val i = Intent(this,AccederActivity::class.java)
        createSessionPreference()
        startActivity(i)
        finish()
    }
    private fun createSessionPreference(){
        val preference = PreferenceHelper.defaultPrefs(this)
        preference["session"] = true
    }
}