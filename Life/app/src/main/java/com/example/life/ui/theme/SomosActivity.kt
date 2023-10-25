package com.example.life.ui.theme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.life.R
import android.webkit.WebView

class SomosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_somos)

        val myWebView: WebView = findViewById(R.id.webview)
        myWebView.loadUrl("https://arihua.com/info.php")
    }
}