package com.wembleystudios.sociallogin

/**
 * Created by Álvaro Blanco Cabrero on 09/11/2018.
 * android.
 */
data class SocialConfig(val googleWebClientId: String?) {

    companion object {

        private const val KEY_GOOGLE_WEB_CLIENT_ID = "google_web_client_id"

        fun fromMap(map: Map<*, *>): SocialConfig =
            SocialConfig(map[KEY_GOOGLE_WEB_CLIENT_ID] as? String)

        val EMPTY = SocialConfig(null)
    }
}