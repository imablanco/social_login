package com.wembleystudios.sociallogin

/**
 * Created by Álvaro Blanco Cabrero on 09/11/2018.
 * android.
 */
data class SocialConfig(
    val facebookAppId: String? = null, val googleWebClientId: String? = null,
    val twitterConsumer: String? = null, val twitterSecret: String? = null
) {

    companion object {

        private const val KEY_GOOGLE_WEB_CLIENT_ID = "google_web_client_id"
        private const val KEY_FACEBOOK_APP_ID = "facebook_app_id"
        private const val KEY_TWITTER_CONSUMER = "twitter_consumer"
        private const val KEY_TWITTER_SECRET = "twitter_secret"

        fun fromMap(map: Map<*, *>): SocialConfig =
            SocialConfig(
                facebookAppId = map[KEY_FACEBOOK_APP_ID] as? String,
                googleWebClientId = map[KEY_GOOGLE_WEB_CLIENT_ID] as? String,
                twitterConsumer = map[KEY_TWITTER_CONSUMER] as? String,
                twitterSecret = map[KEY_TWITTER_SECRET] as? String
            )

        val EMPTY = SocialConfig()
    }
}