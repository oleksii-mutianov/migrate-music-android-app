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

import android.app.Activity
import android.content.Intent
import android.util.Log
import ua.alxmute.auth.data.AuthRequest
import ua.alxmute.auth.data.AuthResponse
import ua.alxmute.auth.handler.AuthorizationHandler
import ua.alxmute.auth.handler.WebViewAuthHandler

class AuthClient(
    /**
     * The activity that receives and processes the result of authorization flow
     * and returns it to the context activity that invoked the flow.
     * An instance of [LoginActivity]
     */
    private val mLoginActivity: Activity
) {

    companion object {
        private const val TAG = "Auth Client"
    }

    private var mAuthorizationPending = false

    /**
     * A handler that performs authorization.
     * It is created with `mLoginActivity` as a context.
     * This activity will receive the result through the
     * [AuthorizationClientListener]
     */
    private var mCurrentHandler: AuthorizationHandler? = null

    private val mAuthorizationHandler: AuthorizationHandler = WebViewAuthHandler()

    /**
     * This listener will be used when authorization flow will return a result.
     *
     * @param listener The listener to be notified when authorization flow completes.
     */
    var onCompleteListener: AuthorizationClientListener? = null

    /**
     * Performs authorization.
     * First it will try to bind spotify auth service, if this is not possible
     * it will fallback to showing accounts page in the webview.
     *
     * @param request Authorization request
     */
    fun authorize(request: AuthRequest) {
        if (mAuthorizationPending) return

        mAuthorizationPending = true

        if (tryAuthorizationHandler(mAuthorizationHandler, request)) {
            mCurrentHandler = mAuthorizationHandler
        }
    }

    /**
     * Authorization process was interrupted.
     * This can happen when auth flow is not completed
     * but was cancelled e.g. when underlying LoginActivity
     * was paused or stopped.
     */
    fun cancel() {
        if (!mAuthorizationPending) return

        mAuthorizationPending = false
        closeAuthorizationHandler(mCurrentHandler)

        onCompleteListener?.onClientCancelled()
        onCompleteListener = null
    }

    /**
     * Authorization returned a result.
     * The result doesn't have to contain a response uri
     * e.g when back button was pressed.
     *
     * @param response The uri returned by auth flow.
     */
    fun complete(response: AuthResponse) {
        sendComplete(mCurrentHandler, response)
    }

    private fun sendComplete(authHandler: AuthorizationHandler?, response: AuthResponse) {
        mAuthorizationPending = false
        closeAuthorizationHandler(authHandler)

        onCompleteListener?.onClientComplete(response)
            ?: Log.w(TAG, "Can't deliver the Spotify Auth response. The listener is null")
        onCompleteListener = null
    }

    private fun tryAuthorizationHandler(authHandler: AuthorizationHandler, request: AuthRequest): Boolean {
        authHandler.setOnCompleteListener(object : AuthorizationHandler.OnCompleteListener {
            // TODO: pass Uri instead of AuthorizationResponse
            override fun onComplete(response: AuthResponse) {
                // TODO: now we have code, need to get token somewhere
                Log.i(TAG, String.format("Spotify auth response:%s", response.type.name))
                sendComplete(authHandler, response)
            }

            override fun onCancel() {
                Log.i(TAG, "Auth response: User cancelled")
                val response = AuthResponse(AuthResponse.Type.EMPTY)
                sendComplete(authHandler, response)
            }

            override fun onError(error: Throwable) {
                Log.e(TAG, "Auth Error", error)
                val response = AuthResponse(
                    AuthResponse.Type.ERROR,
                    error.message
                )
                sendComplete(authHandler, response)
            }
        })

        if (!authHandler.start(mLoginActivity, request)) {
            closeAuthorizationHandler(authHandler)
            return false
        }
        return true
    }

    private fun closeAuthorizationHandler(authHandler: AuthorizationHandler?) {
        authHandler?.setOnCompleteListener(null)
        authHandler?.stop()
    }

    interface AuthorizationClientListener {
        /**
         * Auth flow was completed.
         * The response can be successful and contain access token or authorization code.
         * The response can be an error response and contain error message.
         * It can also be an empty response which indicated that the
         * user cancelled authorization flow.
         *
         * @param response Response containing a result of authorization flow.
         */
        fun onClientComplete(response: AuthResponse)

        /**
         * Auth flow was cancelled before it could be completed.
         * This callbacks indicates that the auth flow was interrupted
         * for example because of underlying LoginActivity was paused or stopped.
         * This is different from the situation when user completes the flow
         * by closing LoginActivity (e.g. by pressing the back button).
         */
        fun onClientCancelled()
    }

}

/**
 * Opens LoginActivity which performs authorization.
 * The result of the authorization flow will be received by the
 * `contextActivity` in the `onActivityResult` callback.
 * The successful result of the authorization flow will contain an access token that can be used
 * to make calls to the Web API and/or to play music with Spotify.
 *
 * @param contextActivity A context activity for the LoginActivity.
 * @param requestCode     Request code for LoginActivity.
 * @param request         Authorization request
 * @throws IllegalArgumentException if any of the arguments is null
 */
fun AuthClient.Companion.openLoginActivity(contextActivity: Activity, requestCode: Int, request: AuthRequest) {
    val intent = LoginActivity.getAuthIntent(contextActivity, request).apply { addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) }
    contextActivity.startActivityForResult(intent, requestCode)
}

/**
 * Extracts [AuthResponse]
 * from the LoginActivity result.
 *
 * @param resultCode Result code returned with the activity result.
 * @param intent     Intent received with activity result. Should contain a Uri with result data.
 * @return response object.
 */
fun AuthClient.Companion.getResponse(resultCode: Int, intent: Intent?): AuthResponse {
    val responseFromIntent = LoginActivity.getResponseFromIntent(intent)
    return if (resultCode == Activity.RESULT_OK && responseFromIntent != null) {
        responseFromIntent
    } else {
        AuthResponse(AuthResponse.Type.EMPTY)
    }
}