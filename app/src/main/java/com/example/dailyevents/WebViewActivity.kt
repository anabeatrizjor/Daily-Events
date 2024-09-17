package com.example.dailyevents

import android.content.Intent
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity

class WebViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        val backButton = findViewById<ImageButton>(R.id.backButton)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val webView = findViewById<WebView>(R.id.webView)

        // Configure WebView settings
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true

        // Set WebViewClient to handle page load events
        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
                super.onPageStarted(view, url, favicon)
                progressBar.visibility = android.view.View.VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                progressBar.visibility = android.view.View.GONE
            }

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: android.webkit.WebResourceError?) {
                super.onReceivedError(view, request, error)
                progressBar.visibility = android.view.View.GONE
            }
        }

        // Load the web page
        webView.loadUrl("https://beecorp.com.br/mudanca-de-habito/")

        // Back button listener
        backButton.setOnClickListener {
            finish() // Finish the activity to return to the previous one
        }

        window.statusBarColor = getColor(R.color.subColor)
    }
}
