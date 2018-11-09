package com.wembleystudios.sociallogin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import org.json.JSONObject

/**
 * Created by Álvaro Blanco Cabrero on 09/11/2018.
 * android.
 */
class FacebookHandler {

    private val callbackManager: CallbackManager by lazy {
        CallbackManager.Factory.create()
    }

    private var onLoginResult: ((SocialUser?, Throwable?) -> Unit)? = null

    private val callback: FacebookCallback<LoginResult> by lazy {
        object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                val request =
                    GraphRequest.newMeRequest(result.accessToken) { jsonObject, response ->
                        onLoginResult?.invoke(
                            jsonObject?.let {
                                SocialUser(
                                    it.getString(ME_REQUEST_RESPONSE_ID),
                                    it.getString(ME_REQUEST_RESPONSE_EMAIL),
                                    it.getString(ME_REQUEST_RESPONSE_NAME),
                                    getProfilePicture(it),
                                    mapOf(Constants.FACEBOOK_TOKEN to result.accessToken?.token)
                                )
                            }, null
                        )
                    }
                request.parameters = Bundle().apply {
                    putString(ME_REQUEST_PARAM_FIELDS_KEY, ME_REQUEST_PARAM_FIELDS_VALUE)
                }
                request.executeAsync()
            }

            override fun onCancel() {
                onLoginResult?.invoke(null, null)
            }

            override fun onError(error: FacebookException) {
                onLoginResult?.invoke(null, error)
            }
        }
    }

    init {
        LoginManager.getInstance().registerCallback(callbackManager, callback)
    }

    fun logInWithReadPermissions(
        activity: Activity,
        permissions: List<String>,
        callback: (SocialUser?, Throwable?) -> Unit
    ) {
        onLoginResult = callback
        LoginManager.getInstance().logInWithReadPermissions(activity, permissions)
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private fun getProfilePicture(jsonObject: JSONObject): String? {
        val pictureJson = jsonObject.getJSONObject(ME_REQUEST_RESPONSE_PICTURE)
        val data = pictureJson?.getJSONObject(ME_REQUEST_RESPONSE_DATA)
        return data?.getString(ME_REQUEST_RESPONSE_URL)
    }

    companion object {
        private const val ME_REQUEST_PARAM_FIELDS_KEY = "fields"
        private const val ME_REQUEST_PARAM_FIELDS_VALUE = "email, name, picture"

        private const val ME_REQUEST_RESPONSE_ID = "id"
        private const val ME_REQUEST_RESPONSE_EMAIL = "email"
        private const val ME_REQUEST_RESPONSE_NAME = "name"
        private const val ME_REQUEST_RESPONSE_PICTURE = "picture"
        private const val ME_REQUEST_RESPONSE_DATA = "data"
        private const val ME_REQUEST_RESPONSE_URL = "url"
    }
}