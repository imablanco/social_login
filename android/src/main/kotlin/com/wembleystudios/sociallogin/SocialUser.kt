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
    val extraData: Map<String, String?>?
) {
    fun topMap(): Map<String, Any?> =
        mapOf(
            "id" to id,
            "email" to email,
            "name" to name,
            "picture_url" to pictureUrl,
            "extra_data" to extraData
        )
}