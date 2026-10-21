package com.veggie.studio

import android.app.Activity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import java.util.Locale

class MainActivity : Activity(), TextToSpeech.OnInitListener {
    private lateinit var web: WebView
    private var tts: TextToSpeech? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tts = TextToSpeech(this, this)
        web = WebView(this)
        web.settings.javaScriptEnabled = true
        web.addJavascriptInterface(Bridge(), "Android")
        web.webViewClient = WebViewClient()
        setContentView(web)
        web.loadUrl("file:///android_asset/index.html")
    }
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) tts?.language = Locale.forLanguageTag("es-ES")
    }
    inner class Bridge {
        @JavascriptInterface fun speak(line: String) { runOnUiThread {
            tts?.speak(line, TextToSpeech.QUEUE_FLUSH, null, "veggie_line")
        } }
        @JavascriptInterface fun stop() { runOnUiThread { tts?.stop() } }
    }
    override fun onDestroy() { tts?.stop(); tts?.shutdown(); web.destroy(); super.onDestroy() }
}
