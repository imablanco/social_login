package com.wembleystudios.sociallogin

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import org.json.JSONObject

/**
 * Created by Álvaro Blanco Cabrero on 09/11/2018.
 * android.
 */
class FacebookHandler(context: Context, socialConfigOwner: SocialConfigOwner) {

    private val callbackManager: CallbackManager by lazy {
        CallbackManager.Factory.create()
    }

    private var onLoginResult: ((SocialUser?, Throwable?) -> Unit)? = null

    private val callback: FacebookCallback<LoginResult> by lazy {
        object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                fetchUserProfile(result.accessToken) { socialUser, throwable ->
                    onLoginResult?.invoke(socialUser, throwable)
                }
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
        FacebookSdk.setApplicationId(socialConfigOwner.socialConfig.facebookAppId)
        FacebookSdk.sdkInitialize(context.applicationContext)
        LoginManager.getInstance().registerCallback(callbackManager, callback)
    }

    fun getCurrentUserProfile(callback: (SocialUser?, Throwable?) -> Unit) {
        AccessToken.getCurrentAccessToken()?.let {
            fetchUserProfile(it, callback)
        } ?: run {
            callback(null, NO_USER_LOGGED_IN_ERROR)
        }
    }

    fun logInWithReadPermissions(
        activity: Activity,
        permissions: List<String>,
        callback: (SocialUser?, Throwable?) -> Unit
    ) {
        if (AccessToken.getCurrentAccessToken()?.permissions?.containsAll(permissions) == true) {
            fetchUserProfile(AccessToken.getCurrentAccessToken(), callback)
        } else {
            onLoginResult = callback
            LoginManager.getInstance().logInWithReadPermissions(activity, permissions)
        }
    }

    fun logOut() = LoginManager.getInstance().logOut()

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) =
        callbackManager.onActivityResult(requestCode, resultCode, data)

    private fun fetchUserProfile(
        accessToken: AccessToken,
        callback: (SocialUser?, Throwable?) -> Unit
    ) {
        val request =
            GraphRequest.newMeRequest(accessToken) { jsonObject, response ->
                response.error?.let {
                    callback(null, response.error.exception)
                } ?: run {
                    callback(
                        SocialUser(
                            jsonObject.getString(ME_REQUEST_RESPONSE_ID),
                            jsonObject.getString(ME_REQUEST_RESPONSE_EMAIL),
                            jsonObject.getString(ME_REQUEST_RESPONSE_NAME),
                            getProfilePicture(jsonObject),
                            mapOf(Constants.FACEBOOK_TOKEN to accessToken.token)
                        ),
                        null
                    )
                }
            }
        request.parameters = Bundle().apply {
            putString(ME_REQUEST_PARAM_FIELDS_KEY, ME_REQUEST_PARAM_FIELDS_VALUE)
        }
        request.executeAsync()
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

        private val NO_USER_LOGGED_IN_ERROR = FacebookException("No user logged in")
    }
}