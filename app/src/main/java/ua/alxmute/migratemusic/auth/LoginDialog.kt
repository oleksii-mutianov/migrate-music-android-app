package ua.alxmute.migratemusic.auth

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import android.widget.LinearLayout
import ua.alxmute.migratemusic.R
import ua.alxmute.migratemusic.auth.deezer.AuthClient
import ua.alxmute.migratemusic.service.DeezerLoginListener

class LoginDialog(
    activity: Activity,
    private val url: String,
    private val authClient: AuthClient,
    private val loginListener: DeezerLoginListener
) : Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar) {

    companion object {
        private val TAG = LoginDialog::class.java.name
        private const val MAX_WIDTH_DP = 400
        private const val MAX_HEIGHT_DP = 640
    }

    private var uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        uri = Uri.parse(url)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        window!!.setBackgroundDrawableResource(android.R.drawable.screen_background_dark_transparent)

        setContentView(R.layout.login_dialog)

        setLayoutSize()

        createWebView()
    }

    private fun createWebView() {
        if (!internetPermissionGranted()) {
            Log.e(TAG, "Missing INTERNET permission")
        }

        val webView = findViewById<WebView>(R.id.login_webview)
        val mWebViewContainer =
            findViewById<LinearLayout>(R.id.login_webview_container)

        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                webView.visibility = View.VISIBLE
                mWebViewContainer.visibility = View.VISIBLE
                super.onPageFinished(view, url)
            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                if (url.contains("code")) {
                    authClient.onComplete(url.substringAfterLast("code="), loginListener)
                    dismiss()
                }

                view.loadUrl(url)
                return true
            }
        }
        webView.loadUrl(uri.toString())
    }

    private fun internetPermissionGranted(): Boolean {
        val pm = context.packageManager
        val packageName = context.packageName
        return pm.checkPermission(
            Manifest.permission.INTERNET,
            packageName
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun setLayoutSize() {
        val wm =
            context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val metrics = DisplayMetrics()
        display.getMetrics(metrics)
        var dialogWidth = ViewGroup.LayoutParams.MATCH_PARENT
        var dialogHeight = ViewGroup.LayoutParams.MATCH_PARENT

        // If width or height measured in dp exceeds accepted range,
        // use max values and convert them back to pixels before setting the size.
        if (metrics.widthPixels / metrics.density > MAX_WIDTH_DP) {
            dialogWidth = (MAX_WIDTH_DP * metrics.density).toInt()
        }
        if (metrics.heightPixels / metrics.density > MAX_HEIGHT_DP) {
            dialogHeight = (MAX_HEIGHT_DP * metrics.density).toInt()
        }
        val layout =
            findViewById<View>(R.id.login_webview_container) as LinearLayout
        layout.layoutParams = FrameLayout.LayoutParams(dialogWidth, dialogHeight, Gravity.CENTER)
    }
}