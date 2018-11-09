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
    PluginRegistry.ActivityResultListener {

    private val facebookHandler: FacebookHandler by lazy {
        FacebookHandler()
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
            Constants.METHOD_LOGIN_FACEBOOK -> {
                facebookHandler.logInWithReadPermissions(
                    activity,
                    call.arguments as List<String>
                ) { socialUser, throwable ->
                    when {
                        socialUser == null && throwable == null ->
                            result.error(
                                Constants.METHOD_CODE_ERROR,
                                Constants.FACEBOOK_ERROR_USER_CANCELED,
                                null
                            )
                        socialUser != null -> result.success(socialUser.topMap())
                        throwable != null -> result.error(
                            Constants.METHOD_CODE_ERROR,
                            throwable.message,
                            throwable
                        )
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        facebookHandler.onActivityResult(requestCode, resultCode, data)
        return false
    }

}
