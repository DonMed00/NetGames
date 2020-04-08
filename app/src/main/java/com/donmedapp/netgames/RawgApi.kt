package com.donmedapp.netgames

import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query


interface RawgApi {
    @Headers("User-Agent: com.theobviousexit.rawg")
    @GET("games?")
    suspend fun getGames(
        @Query("search") name: String,
        @Query("page") page: Int,
        @Query("page_size") page_size: Int
    ): RawgResponse

    @Headers("User-Agent: com.theobviousexit.rawg")
    @GET("games")
    suspend fun orderByGenres(@Query("genres") name: String): RawgResponse


    @Headers("User-Agent: com.theobviousexit.rawg")
    @GET("games/{gameId}")
    suspend fun getGame(@Path("gameId") gameId: Long): Result

    //https://api.rawg.io/api/games?last_30_days
    //https://api.rawg.io/api/games?this_week


}

//games search
@Parcelize
data class Result(
    @SerializedName("id") val id: Long = 0,
    @SerializedName("name") val name: String = "",
    @SerializedName("slug") val slug: String = "",
    @SerializedName("rating") val rating: String = "",
    @SerializedName("clip") val clip: Clip? = null,
    @SerializedName("background_image") val backgroundImage: String = "",
    @SerializedName("metacritic") val metacritic: String? = "",
    @SerializedName("short_screenshots") val shortScreenshots: List<ShortScreenshot> = emptyList(),
    @SerializedName("platforms") val platforms: List<PlatformObj>? = null

) : Parcelable {
    fun hasPlatform(platform: String) =
        platforms?.mapNotNull { t -> t.platform?.slug }?.find { t -> t.contains(platform) } != null

    fun hasVideoContent() = clip?.clip != null
    fun hasMetacriticRating() = metacritic?.isNotBlank() ?: false

    fun displayImageMode() {
        imageVisibility.value = true
        videoVisibility.value = false
        playVideoIconVisibility.value = true
    }

    fun displayVideoMode() {
        imageVisibility.value = false
        videoVisibility.value = true
        playVideoIconVisibility.value = false
    }

    @IgnoredOnParcel
    val imageVisibility = MutableLiveData<Boolean>(true)

    @IgnoredOnParcel
    val videoVisibility = MutableLiveData<Boolean>(false)

    @IgnoredOnParcel
    val playVideoIconVisibility: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(
            hasVideoContent()
        )
    }
}

@Parcelize
data class ShortScreenshot(
    @SerializedName("id") val id: Long = 0,
    @SerializedName("image") val image: String = ""
) : Parcelable

@Parcelize
data class Clip(
    @SerializedName("clip") val clip: String = "",
    @SerializedName("clips") val clips: Clips? = null,
    @SerializedName("preview") val preview: String = ""
) : Parcelable

@Parcelize
data class Clips(
    @SerializedName("320") val small: String = "",
    @SerializedName("640") val medium: String = "",
    @SerializedName("full") val full: String = ""
) : Parcelable

@Parcelize
data class PlatformObj(
    @SerializedName("platform") val platform: Platform? = null
) : Parcelable

@Parcelize
data class Platform(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("name") val name: String = "",
    @SerializedName("slug") val slug: String = ""
) : Parcelable

@Parcelize
data class RawgResponse(
    val next: String? = null,
    val previous: String? = null,
    val results: List<Result> = arrayListOf(),


    var loadStarted: Boolean = false
) : Parcelable

//game details
@Parcelize
data class GameDetailResult(
    @SerializedName("id") val id: Long = 0,
    @SerializedName("name") val name: String = "",
    @SerializedName("slug") val slug: String = "",
    @SerializedName("background_image") val backgroundImage: String = "",
    @SerializedName("background_image_additional") val backgroundImageAdditional: String = "",
    @SerializedName("description") val description: String = "",
    @SerializedName("metacritic") val metacritic: Long = 0,
    @SerializedName("saturated_color") val saturatedColor: String = "",
    @SerializedName("dominant_color") val dominantColor: String = ""
) : Parcelable