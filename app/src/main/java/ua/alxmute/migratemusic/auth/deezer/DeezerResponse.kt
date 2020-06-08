package ua.alxmute.migratemusic.auth.deezer

import com.google.gson.annotations.SerializedName

class DeezerResponse(
    @SerializedName("access_token") val accessToken: String
)