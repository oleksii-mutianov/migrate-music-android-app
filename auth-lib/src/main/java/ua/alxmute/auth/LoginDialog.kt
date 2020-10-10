/*
 * Copyright (c) 2015-2016 Spotify AB
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package ua.alxmute.auth

import android.Manifest
import android.annotation.SuppressLint
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
import ua.alxmute.auth.constants.REDIRECT_URI
import ua.alxmute.auth.data.AuthRequest
import ua.alxmute.auth.data.AuthResponse
import ua.alxmute.auth.handler.AuthorizationHandler
import java.util.*

class LoginDialog(contextActivity: Activity, request: AuthRequest) : Dialog(contextActivity, DEFAULT_THEME) {

    companion object {
        private val TAG = LoginDialog::class.java.name
        private const val DEFAULT_THEME = android.R.style.Theme_Translucent_NoTitleBar

        /**
         * The maximum width and height of the login dialog in density independent pixels.
         * This value is expressed in pixels because the maximum size
         * should look approximately the same independent from device's screen size and density.
         */
        private const val MAX_WIDTH_DP = 400
        private const val MAX_HEIGHT_DP = 640
    }

    private val mUri = request.toUri()

    var onCompleteListener: AuthorizationHandler.OnCompleteListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        window?.setBackgroundDrawableResource(android.R.drawable.screen_background_dark_transparent)

        setContentView(R.layout.com_spotify_sdk_login_dialog)

        setLayoutSize()

        createWebView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun createWebView() {
        if (!internetPermissionGranted()) {
            Log.e(TAG, "Missing INTERNET permission")
        }

        val redirectUri = mUri.getQueryParameter(REDIRECT_URI)!!.toLowerCase(Locale.ENGLISH)
        val mWebViewContainer = findViewById<LinearLayout>(R.id.com_spotify_sdk_login_webview_container)
        val webView = findViewById<WebView>(R.id.com_spotify_sdk_login_webview).apply {
            settings.apply {
                javaScriptEnabled = true
                userAgentString = userAgentString.replace("; wv", "")
            }
        }

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                webView.visibility = View.VISIBLE
                mWebViewContainer.visibility = View.VISIBLE
                super.onPageFinished(view, url)
            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                val caseSafeResponseRedirectUri = url.toLowerCase(Locale.ENGLISH)
                val responseUri = Uri.parse(url)

                if (caseSafeResponseRedirectUri.contains(redirectUri)) {
                    sendComplete(responseUri)
                } else {
                    view.loadUrl(url)
                }
                return true
            }
        }
        webView.loadUrl(mUri.toString())
    }

    private fun sendComplete(responseUri: Uri) {
        onCompleteListener?.onComplete(AuthResponse.fromUri(responseUri))
        dismiss()
    }

    private fun internetPermissionGranted() =
        context.packageManager.checkPermission(
            Manifest.permission.INTERNET,
            context.packageName
        ) == PackageManager.PERMISSION_GRANTED

    private fun setLayoutSize() {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val metrics = DisplayMetrics().apply { wm.defaultDisplay.getMetrics(this) }

        // If width or height measured in dp exceeds accepted range,
        // use max values and convert them back to pixels before setting the size.
        val dialogWidth = if (metrics.widthPixels / metrics.density > MAX_WIDTH_DP) {
            (MAX_WIDTH_DP * metrics.density).toInt()
        } else ViewGroup.LayoutParams.MATCH_PARENT

        val dialogHeight = if (metrics.heightPixels / metrics.density > MAX_HEIGHT_DP) {
            (MAX_HEIGHT_DP * metrics.density).toInt()
        } else ViewGroup.LayoutParams.MATCH_PARENT

        findViewById<View>(R.id.com_spotify_sdk_login_webview_container).apply {
            layoutParams = FrameLayout.LayoutParams(dialogWidth, dialogHeight, Gravity.CENTER)
        }
    }

}