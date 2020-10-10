package ua.alxmute.migratemusic.auth

import com.google.gson.annotations.SerializedName

class DeezerAuthTokenResponse(
    @SerializedName("access_token") val accessToken: String
)