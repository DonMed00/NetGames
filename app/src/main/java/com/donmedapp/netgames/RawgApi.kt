package com.donmedapp.netgames

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query


interface RawgApi {
    @Headers("User-Agent: com.donmedapp.netgames")
    @GET("games?")
    suspend fun getGames(
        @Query("search") name: String,
        @Query("page") page: Int,
        @Query("page_size") page_size: Int,
        @Query("key") key: String

    ): RawgResponse

    @Headers("User-Agent: com.donmedapp.netgames")
    @GET("games")
    suspend fun orderByGenres(
        @Query("genres") name: String,
        @Query("key") key: String
    ): RawgResponse


    @Headers("User-Agent: com.donmedapp.netgames")
    @GET("games/{gameId}")
    suspend fun getGame(@Path("gameId") gameId: Long, @Query("key") key: String): Result


    @GET("games/{gameId}/screenshots")
    suspend fun getScreenshotsOfGame(
        @Path("gameId") gameId: Long, @Query("key") key: String
    ): Screenshot2

}


@Parcelize
data class Result(
    @SerializedName("id") val id: Long = 0,
    @SerializedName("name") val name: String = "",
    @SerializedName("slug") val slug: String = "",
    @SerializedName("rating") val rating: String = "",
    @SerializedName("clip") val clip: Clip? = null,
    @SerializedName("description") val description: String = "",
    @SerializedName("released") val released: String = "",
    @SerializedName("screenshots_count") val screenshotsCount: Int,
    @SerializedName("background_image") val backgroundImage : String? = "",
    @SerializedName("metacritic") val metacritic: String? = "",
    @SerializedName("short_screenshots") val shortScreenshots: List<ShortScreenshot> = emptyList(),
    @SerializedName("platforms") val platforms: List<PlatformObj>? = null,
    @SerializedName("stores") val stores: List<StoreObj>? = null


) : Parcelable {
    fun hasPlatform(platform: String) =
        platforms?.mapNotNull { t -> t.platform?.slug }?.find { t -> t.contains(platform) } != null

    fun hasVideoContent() = clip?.clip != null
    fun hasMetacriticRating() = metacritic?.isNotBlank() ?: false

    fun toStringPlatforms(): String {
        var platformsFormatted = ""
        if(hasPlatform("pc") ||hasPlatform("linux")||hasPlatform("mac")){
            platformsFormatted = "PC "
        }
        if(hasPlatform("playstation")||hasPlatform("xbox")||hasPlatform("nintendo")||hasPlatform("wii")){
            platformsFormatted= platformsFormatted.plus("Console ")
        }
        if(hasPlatform("ios")||hasPlatform("android")){
            platformsFormatted= platformsFormatted.plus("Mobile ")
        }
        if(platformsFormatted.isNotEmpty()){
            platformsFormatted = platformsFormatted.substring(0,platformsFormatted.lastIndex)
        }else{
            return "No platforms"
        }

        return platformsFormatted.replace(" "," - ")

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

@Parcelize

data class Result2(
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
data class Screenshot2(
    @SerializedName("count")
    @Expose
    var count: Int? = null,

    @SerializedName("next")
    @Expose
    var next: Int,

    @SerializedName("previous")
    @Expose
    var previous: Int,

    @SerializedName("results")
    @Expose
    var results: List<Result2>? = null
) : Parcelable