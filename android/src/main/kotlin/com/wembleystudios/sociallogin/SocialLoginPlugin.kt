package com.wembleystudios.sociallogin

import android.app.Activity
import android.content.Intent
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry
import io.flutter.plugin.common.PluginRegistry.Registrar

@Suppress("UNCHECKED_CAST")
class SocialLoginPlugin(private val activity: Activity) : MethodCallHandler,
    PluginRegistry.ActivityResultListener, SocialConfigOwner {

    override val socialConfig: SocialConfig
        get() = backingSocialConfig

    private var backingSocialConfig: SocialConfig = SocialConfig.EMPTY

    private val facebookHandler: FacebookHandler by lazy {
        FacebookHandler()
    }

    private val googleHandler: GoogleHandler by lazy {
        GoogleHandler(this)
    }

    companion object {
        @JvmStatic
        fun registerWith(registrar: Registrar) {
            val channel = MethodChannel(registrar.messenger(), Constants.METHOD_CHANNEL_NAME)
            val plugin = SocialLoginPlugin(registrar.activity())
            channel.setMethodCallHandler(plugin)
            registrar.addActivityResultListener(plugin)
        }
    }

    override fun onMethodCall(call: MethodCall, result: Result) {
        when (call.method) {
            Constants.METHOD_SET_CONFIG -> {
                backingSocialConfig =
                        (call.arguments as? Map<*, *>)?.let { SocialConfig.fromMap(it) } ?:
                        SocialConfig.EMPTY
            }
            Constants.METHOD_LOGIN_FACEBOOK -> {
                facebookHandler.logInWithReadPermissions(
                    activity,
                    call.arguments as List<String>
                ) { socialUser, throwable ->
                    handleSocialLoginResponse(result, socialUser, throwable)
                }
            }
            Constants.METHOD_GET_CURRENT_USER_FACEBOOK -> {
                facebookHandler.getCurrentUserProfile { socialUser, throwable ->
                    handleSocialLoginResponse(result, socialUser, throwable)
                }
            }
            Constants.METHOD_LOGOUT_FACEBOOK -> {
                facebookHandler.logOut()
                result.success(null)
            }
            Constants.METHOD_LOGIN_GOOGLE -> {
                googleHandler.login(activity) { socialUser, throwable ->
                    handleSocialLoginResponse(result, socialUser, throwable)
                }
            }
            Constants.METHOD_GET_CURRENT_USER_GOOGLE -> {
                googleHandler.getCurrentUser(activity)?.let {
                    result.success(it.topMap())
                } ?: result.error(
                    Constants.METHOD_CODE_ERROR,
                    Constants.METHOD_ERROR_GOOGLE_NOT_LOGGED_USER,
                    null
                )
            }
            Constants.METHOD_LOGOUT_GOOGLE -> {
                googleHandler.logOut(activity) { result.success(null) }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        return facebookHandler.onActivityResult(requestCode, resultCode, data)
                || googleHandler.onActivityResult(requestCode, resultCode, data)

    }

    private fun handleSocialLoginResponse(
        result: Result,
        socialUser: SocialUser?,
        throwable: Throwable?
    ) {
        when {
            socialUser == null && throwable == null ->
                result.error(
                    Constants.METHOD_CODE_ERROR,
                    Constants.METHOD_ERROR_USER_CANCELED,
                    null
                )
            socialUser != null -> result.success(socialUser.topMap())
            throwable != null -> result.error(
                Constants.METHOD_CODE_ERROR,
                throwable.message,
                null
            )
        }
    }

}
