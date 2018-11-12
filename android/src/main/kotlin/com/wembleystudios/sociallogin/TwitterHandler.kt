package com.wembleystudios.sociallogin

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.twitter.sdk.android.core.*
import com.twitter.sdk.android.core.identity.TwitterAuthClient
import com.twitter.sdk.android.core.models.User

/**
 * Created by Álvaro Blanco Cabrero on 11/11/2018.
 * android.
 */
class TwitterHandler(context: Context, socialConfigOwner: SocialConfigOwner) {

    private val loginCallback: Callback<TwitterSession> = object : Callback<TwitterSession>() {
        override fun success(result: Result<TwitterSession>) {
            fetchCurrentUser(result.data.authToken) { user, error ->
                onLoginResult?.invoke(user, error)
            }
        }

        override fun failure(exception: TwitterException) {
            onLoginResult?.invoke(null, exception)
        }
    }

    private var onLoginResult: ((SocialUser?, Throwable?) -> Unit)? = null

    private val twitterAuthClient: TwitterAuthClient by lazy {
        TwitterAuthClient()
    }

    init {
        Twitter.initialize(
            TwitterConfig.Builder(context.applicationContext).twitterAuthConfig
                (
                TwitterAuthConfig(
                    socialConfigOwner.socialConfig.twitterConsumer,
                    socialConfigOwner.socialConfig.twitterSecret
                )
            ).build()
        )
    }

    fun login(activity: Activity, callback: (SocialUser?, Throwable?) -> Unit) {
        TwitterCore.getInstance().sessionManager.activeSession?.let {
            fetchCurrentUser(it.authToken, callback)
        } ?: run {
            onLoginResult = callback
            twitterAuthClient.cancelAuthorize()
            twitterAuthClient.authorize(activity, loginCallback)
        }
    }

    fun getCurrentUser(callback: (SocialUser?, Throwable?) -> Unit) {
        TwitterCore.getInstance().sessionManager.activeSession?.let {
            fetchCurrentUser(it.authToken, callback)
        } ?: run {
            callback(null, Constants.NO_USER_LOGGED_EXCEPTION)
        }
    }

    fun logOut() = TwitterCore.getInstance().sessionManager.clearActiveSession()


    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        if (requestCode == twitterAuthClient.requestCode) {
            twitterAuthClient.onActivityResult(requestCode, resultCode, data)
            return true
        }
        return false
    }

    private fun fetchCurrentUser(
        authToken: TwitterAuthToken,
        callback: (SocialUser?, Throwable?) -> Unit
    ) {
        TwitterCore.getInstance().apiClient.accountService.verifyCredentials(false, true, true)
            .enqueue(object : Callback<User>() {
                override fun success(result: Result<User>) {
                    callback(
                        SocialUser(
                            result.data.idStr,
                            result.data.email,
                            result.data.name,
                            result.data.profileImageUrl,
                            mapOf(
                                Constants.TWITTER_TOKEN to authToken.token,
                                Constants.TWITTER_SECRET to authToken.secret
                            )
                        ),
                        null
                    )
                }

                override fun failure(exception: TwitterException) {
                    callback(null, exception)
                }
            })
    }
}