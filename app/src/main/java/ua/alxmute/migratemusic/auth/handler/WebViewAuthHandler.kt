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
package ua.alxmute.migratemusic.auth.handler

import android.app.Activity
import android.util.Log
import ua.alxmute.migratemusic.auth.LoginDialog
import ua.alxmute.migratemusic.auth.data.AuthRequest

class WebViewAuthHandler : AuthorizationHandler {
    companion object {
        private val TAG = WebViewAuthHandler::class.java.simpleName
    }

    private var mLoginDialog: LoginDialog? = null
    private var mListener: AuthorizationHandler.OnCompleteListener? = null

    override fun start(contextActivity: Activity, request: AuthRequest): Boolean {
        Log.d(TAG, "start")

        mLoginDialog = LoginDialog(contextActivity, request).apply {
            onCompleteListener = mListener
            show()
        }

        return true
    }

    override fun stop() {
        Log.d(TAG, "stop")
        mLoginDialog?.dismiss()
        mLoginDialog = null
    }

    override fun setOnCompleteListener(listener: AuthorizationHandler.OnCompleteListener?) {
        mListener = listener
        mLoginDialog?.onCompleteListener = listener
    }
}