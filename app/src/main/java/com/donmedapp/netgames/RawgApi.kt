package com.donmedapp.netgames

import android.os.Parcelable
import androidx.lifecycle.MutableLiveData
import com.google.gson.annotations.Expose
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


    @GET("games/{gameId}/screenshots")
    suspend fun getScreenshotsOfGame(@Path("gameId") gameId: Long): Screenshot2

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
    @SerializedName("description") val description: String = "",
    @SerializedName("screenshots_count") val screenshotsCount : Int,
    @SerializedName("background_image") val backgroundImage: String = "",
    @SerializedName("metacritic") val metacritic: String? = "",
    @SerializedName("short_screenshots") val shortScreenshots: List<ShortScreenshot> = emptyList(),
    @SerializedName("platforms") val platforms: List<PlatformObj>? = null,
    @SerializedName("stores") val stores: List<StoreObj>? = null


) : Parcelable {
    fun hasPlatform(platform: String) =
        platforms?.mapNotNull { t -> t.platform?.slug }?.find { t -> t.contains(platform) } != null

    fun hasStore(store: String) =
        stores?.mapNotNull { t -> t.store?.slug }?.find { t -> t.contains(store) } != null

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
data class Screenshot(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("image") var image: String? = null,
    @SerializedName("width") var width: Int? = null,
    @SerializedName("height") var height: Int? = null,
    @SerializedName("is_deleted") var isDeleted: Boolean? = null
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
) : Parcelable {
    override fun toString(): String {
        return String.format("")
    }
}

@Parcelize
data class StoreObj(
    @SerializedName("url") var url: String? = null,
    @SerializedName("store") val store: Store? = null
) : Parcelable

@Parcelize
data class Store(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("domain") var domain: String? = null,
    @SerializedName("slug") var slug: String? = null,
    @SerializedName("games_count") var gamesCount: Int? = null,
    @SerializedName("image_background") var imageBackground: String? = null,
    @SerializedName("description") var description: String? = null
) : Parcelable {
    override fun toString(): String {
        return String.format("")
    }
}

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
    @SerializedName("clip") val clip: Clip? = null,
    @SerializedName("background_image_additional") val backgroundImageAdditional: String = "",
    @SerializedName("description") val description: String = "",
    @SerializedName("metacritic") val metacritic: Long = 0,
    @SerializedName("saturated_color") val saturatedColor: String = "",
    @SerializedName("dominant_color") val dominantColor: String = ""
) : Parcelable

@Parcelize

data class Result2 (
    @SerializedName("id")
    @Expose
    var id: Int? = null,

    @SerializedName("image")
    @Expose
    var image: String? = null,

    @SerializedName("width")
    @Expose
    var width: Int? = null,

    @SerializedName("height")
    @Expose
    var height: Int? = null,

    @SerializedName("is_deleted")
    @Expose
    var isDeleted: Boolean? = null
    ) : Parcelable


@Parcelize
data class Screenshot2 (
    @SerializedName("count")
    @Expose
    var count: Int? = null,

    @SerializedName("next")
    @Expose
    var next: Int ,

    @SerializedName("previous")
    @Expose
    var previous: Int,

    @SerializedName("results")
    @Expose
    var results: List<Result2>? = null
): Parcelable