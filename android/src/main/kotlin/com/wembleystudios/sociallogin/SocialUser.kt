package com.wembleystudios.sociallogin

/**
 * Created by Álvaro Blanco Cabrero on 07/11/2018.
 * android.
 */
data class SocialUser(
    val id: String?,
    val email: String?,
    val name: String?,
    val pictureUrl: String?,
    val token: String?,
    val idToken: String?
) {
    fun topMap(): Map<String, String?> =
        mapOf(
            "id" to id,
            "email" to email,
            "name" to name,
            "picture_url" to pictureUrl,
            "token" to token,
            "id_token" to idToken
        )
}