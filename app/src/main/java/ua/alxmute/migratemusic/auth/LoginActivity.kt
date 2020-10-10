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
package ua.alxmute.migratemusic.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import ua.alxmute.migratemusic.R
import ua.alxmute.migratemusic.auth.data.AuthRequest
import ua.alxmute.migratemusic.auth.data.AuthResponse
import java.io.Serializable

/**
 * The activity that manages the login flow.
 * It should not be started directly. Instead use
 * [AuthClient.openLoginActivity]
 */
class LoginActivity : Activity(), AuthClient.AuthorizationClientListener {

    companion object {
        private val TAG = LoginActivity::class.java.name

        private const val NO_REQUEST_ERROR = "No authorization request"

        private const val NO_CALLER_ERROR = "Can't use LoginActivity with a null caller. " +
                "Possible reasons: calling activity has a singleInstance mode " +
                "or LoginActivity is in a singleInstance/singleTask mode"

        const val REQUEST_KEY = "request"
        const val RESPONSE_KEY = "response"
        const val EXTRA_AUTH_REQUEST = "EXTRA_AUTH_REQUEST"
        const val EXTRA_AUTH_RESPONSE = "EXTRA_AUTH_RESPONSE"
    }

    private val mAuthorizationClient = AuthClient(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.com_spotify_sdk_login_activity)
        mAuthorizationClient.onCompleteListener = this

        val request = intent.getBundleExtra(EXTRA_AUTH_REQUEST)?.getSerializable(REQUEST_KEY) as AuthRequest?

        when {
            callingActivity == null -> {
                Log.e(TAG, NO_CALLER_ERROR)
                finish()
            }
            request == null -> {
                Log.e(TAG, NO_REQUEST_ERROR)
                setResult(RESULT_CANCELED)
                finish()
            }
            savedInstanceState == null -> {
                Log.d(TAG, String.format("Auth starting with the request [%s]", request.toUri().toString()))
                mAuthorizationClient.authorize(request)
            }
        }
    }

    override fun onDestroy() {
        mAuthorizationClient.cancel()
        mAuthorizationClient.onCompleteListener = null
        super.onDestroy()
    }

    override fun onClientComplete(response: AuthResponse) {
        // Put response into a bundle to work around classloader problems on Samsung devices
        // https://stackoverflow.com/questions/28589509/android-e-parcel-class-not-found-when-unmarshalling-only-on-samsung-tab3
        Log.i(TAG, String.format("Auth completing. The response is in EXTRA with key '%s'", RESPONSE_KEY))

        val bundle = Bundle().apply { putSerializable(RESPONSE_KEY, response) }
        val resultIntent = Intent().apply { putExtra(EXTRA_AUTH_RESPONSE, bundle) }

        setResult(RESULT_OK, resultIntent)
        finish()
    }

    override fun onClientCancelled() {
        // Called only when LoginActivity is destroyed and no other result is set.
        Log.w(TAG, "Auth cancelled due to LoginActivity being finished")
        setResult(RESULT_CANCELED)
    }

}

fun LoginActivity.Companion.getAuthIntent(contextActivity: Activity, request: Serializable): Intent {
    // Put request into a bundle to work around classloader problems on Samsung devices
    // https://stackoverflow.com/questions/28589509/android-e-parcel-class-not-found-when-unmarshalling-only-on-samsung-tab3
    val bundle = Bundle().apply { putSerializable(REQUEST_KEY, request) }

    return Intent(contextActivity, LoginActivity::class.java).apply { putExtra(EXTRA_AUTH_REQUEST, bundle) }
}

fun LoginActivity.Companion.getResponseFromIntent(intent: Intent?): AuthResponse? {
    return intent?.getBundleExtra(EXTRA_AUTH_RESPONSE)?.getSerializable(RESPONSE_KEY) as AuthResponse?
}

