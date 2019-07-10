package com.joker.core.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.webkit.*
import com.joker.core.R
import com.joker.core.ext.click
import com.joker.core.ext.common
import com.joker.core.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_web.*

/**
 * WebActivity
 *
 * @author joker
 * @date 2019/1/18.
 */
class WebActivity : BaseActivity<Nothing>() {

    override val layout: Any
        get() = R.layout.activity_web

    private var isError: Boolean = false

    override fun onBindView(savedInstanceState: Bundle?) {
        titleBar.common(getWebTitle())
        initWebView()
        start2Load()
    }

    protected open fun start2Load() {
        val url = getUrl()
        val content = getContent()
        if (!TextUtils.isEmpty(url)) {
            webView.loadUrl(url)
        } else if (!TextUtils.isEmpty(content)) {
            webView.loadData(content, "text/html; charset=UTF-8", null)
        }
    }

    protected open fun getWebTitle(): String? {
        return if (intent.hasExtra(KEY_TITLE)) intent.getStringExtra(KEY_TITLE) else null
    }

    protected open fun getUrl(): String {
        return if (intent.hasExtra(KEY_URL)) intent.getStringExtra(KEY_URL) else ""
    }

    protected open fun getContent(): String {
        return if (intent.hasExtra(KEY_CONTENT)) intent.getStringExtra(KEY_CONTENT) else ""
    }

    protected open fun getBottomView(): View? {
        return null
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        val settings = webView.settings
        settings.defaultTextEncodingName = "UTF-8"
        settings.javaScriptEnabled = true
        settings.cacheMode = WebSettings.LOAD_NO_CACHE
        val databasePath = getDir("database", Context.MODE_PRIVATE).path
        settings.databaseEnabled = true
        settings.databasePath = databasePath
        settings.domStorageEnabled = true
        settings.setGeolocationEnabled(true)
        settings.setAppCachePath(databasePath)
        settings.setAppCacheEnabled(true)
        settings.allowFileAccess = true
        settings.useWideViewPort = true
        settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        settings.loadWithOverviewMode = true
        settings.setSupportZoom(false)
        settings.builtInZoomControls = false
        settings.displayZoomControls = false
        settings.loadsImagesAutomatically = true
        settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        webView.webChromeClient = object : WebChromeClient() {

            override fun onReceivedTitle(view: WebView?, title: String?) {
                super.onReceivedTitle(view, title)
                titleBar.centerTextView.text = title
            }

            override fun onProgressChanged(view: WebView, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                runOnUiThread {
                    pb_loading.progress = newProgress
                    pb_loading.visibility = if (newProgress in 1..99) View.VISIBLE else View.GONE
                }
            }

        }
        webView.webViewClient = object : WebViewClient() {

            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                isError = false
            }

            override fun onPageFinished(view: WebView, url: String) {
                if (!webView.settings.loadsImagesAutomatically) {
                    webView.settings.loadsImagesAutomatically = true
                }
            }

            override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
                super.onReceivedError(view, request, error)
                isError = true
            }

            override fun onReceivedError(view: WebView, errorCode: Int, description: String, failingUrl: String) {
                super.onReceivedError(view, errorCode, description, failingUrl)
                isError = true
            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String): Boolean {
                if (url.startsWith("http:") || url.startsWith("https:")) {
                    view?.loadUrl(url)
                    return false
                }
                try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                /*
                if (url.startsWith("tel:")) {
                    val phoneNumber = url.substring(4, url.length)
                    val intent = Intent(Intent.ACTION_DIAL)
                    val data = Uri.parse("tel:$phoneNumber")
                    intent.data = data
                    this@WebActivity.startActivity(intent)
                    return true
                }
                */
                return super.shouldOverrideUrlLoading(view, url)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        webView.destroy()
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    companion object {

        const val KEY_TITLE = "key_title"
        const val KEY_URL = "key_url"
        const val KEY_CONTENT = "key_content"

        fun startWithContent(context: Context, content: String, title: String? = null) {
            val intent = Intent(context, WebActivity::class.java)
            if (title != null) intent.putExtra(KEY_TITLE, title)
            intent.putExtra(KEY_CONTENT, content)
            context.startActivity(intent)
        }

        fun startWithUrl(context: Context, url: String, title: String? = null) {
            val intent = Intent(context, WebActivity::class.java)
            if (title != null) intent.putExtra(KEY_TITLE, title)
            intent.putExtra(KEY_URL, url)
            context.startActivity(intent)
        }
    }

}
