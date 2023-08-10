package com.example.testtask

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.webkit.CookieManager
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import com.example.testtask.databinding.ActivityWebViewBinding

class WebViewActivity : AppCompatActivity() {

    private var binding:ActivityWebViewBinding? = null
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        sharedPreferences = getSharedPreferences(PREFERENCE_NAME,Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }

    override fun onStart() {
        super.onStart()

        binding?.webView?.webChromeClient = WebChromeClient()
        val webSettings: WebSettings = binding?.webView?.settings!!
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
        webSettings.databaseEnabled = true
        webSettings.cacheMode = WebSettings.LOAD_DEFAULT
        val cookieManager: CookieManager = CookieManager.getInstance()
        cookieManager.setAcceptCookie(true)

        binding?.webView?.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_DOWN) {
                if (binding?.webView?.canGoBack()!!) {
                    binding?.webView?.goBack()
                    return@OnKeyListener true
                }
                return@OnKeyListener true // Return true to indicate the event was handled
            }
            false
        })

        binding?.webView?.webViewClient = object:WebViewClient(){
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding?.progressBar?.visibility = ProgressBar.GONE
            }

            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val newUrl = request?.url.toString()
                if (newUrl.contains("id=") && newUrl.contains("uuid=")) {
                    val id = extractParamValue(newUrl, "id")
                    val uuid = extractParamValue(newUrl, "uuid")
                    binding?.textView?.text = "id: $id \nuuid: $uuid"
                    saveValues(id, uuid)
                }
                return super.shouldOverrideUrlLoading(view, request)
            }

        }
        binding?.webView?.loadUrl(BASE_URL)
    }

    private fun extractParamValue(url: String, paramName: String): String {
        val regex = "$paramName=([^&]+)".toRegex()
        val matchResult = regex.find(url)
        return matchResult?.groupValues?.getOrNull(1) ?: ""
    }

    private fun saveValues(id: String, uuid: String) {
        val editor = sharedPreferences.edit()
        editor.putString(ID_TEXT, id)
        editor.putString(UUID_TEXT, uuid)
        editor.apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}