package com.wembleystudios.sociallogin

import android.app.Activity
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException


/**
 * Created by Álvaro Blanco Cabrero on 09/11/2018.
 * android.
 */
class GoogleHandler(private val socialConfigOwner: SocialConfigOwner) {

    private var onLoginResult: ((SocialUser?, Throwable?) -> Unit)? = null

    fun login(activity: Activity, callback: (SocialUser?, Throwable?) -> Unit) {

        GoogleSignIn.getLastSignedInAccount(activity)?.let {
            callback(it.toSocialUser(), null)
        } ?: run {
            onLoginResult = callback
            val gsoBuilder = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
            socialConfigOwner.socialConfig.googleWebClientId?.let(gsoBuilder::requestIdToken)
            activity.startActivityForResult(
                GoogleSignIn.getClient(activity, gsoBuilder.build()).signInIntent,
                RC_SIGN_IN
            )
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        if (requestCode != RC_SIGN_IN) {
            return false
        }
        try {
            val account =
                GoogleSignIn.getSignedInAccountFromIntent(data).getResult(ApiException::class.java)
            onLoginResult?.invoke(account?.toSocialUser(), null)
        } catch (e: ApiException) {
            onLoginResult?.invoke(null, e)
        }
        return true
    }

    private fun GoogleSignInAccount.toSocialUser(): SocialUser =
        SocialUser(
            id,
            email,
            displayName,
            photoUrl?.toString(),
            mapOf(Constants.GOOGLE_ID_TOKEN to idToken)
        )

    companion object {
        private const val RC_SIGN_IN = 424242
    }
}