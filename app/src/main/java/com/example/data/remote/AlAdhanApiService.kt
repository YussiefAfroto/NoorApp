package com.example.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

@JsonClass(generateAdapter = true)
data class AlAdhanResponse(
    val code: Int = 0,
    val status: String = "",
    val data: AlAdhanData? = null
)

@JsonClass(generateAdapter = true)
data class AlAdhanData(
    val timings: AlAdhanTimings? = null,
    val date: AlAdhanDateInfo? = null,
    val meta: AlAdhanMeta? = null
)

@JsonClass(generateAdapter = true)
data class AlAdhanTimings(
    @Json(name = "Fajr") val fajr: String = "",
    @Json(name = "Sunrise") val sunrise: String = "",
    @Json(name = "Dhuhr") val dhuhr: String = "",
    @Json(name = "Asr") val asr: String = "",
    @Json(name = "Sunset") val sunset: String = "",
    @Json(name = "Maghrib") val maghrib: String = "",
    @Json(name = "Isha") val isha: String = "",
    @Json(name = "Imsak") val imsak: String = "",
    @Json(name = "Midnight") val midnight: String = "",
    @Json(name = "Firstthird") val firstThird: String = "",
    @Json(name = "Lastthird") val lastThird: String = ""
)

@JsonClass(generateAdapter = true)
data class AlAdhanDateInfo(
    val readable: String = "",
    val timestamp: String = "",
    val hijri: AlAdhanHijriDate? = null,
    val gregorian: AlAdhanGregorianDate? = null
)

@JsonClass(generateAdapter = true)
data class AlAdhanHijriDate(
    val date: String = "",
    val day: String = "",
    val weekday: AlAdhanNameField? = null,
    val month: AlAdhanMonthField? = null,
    val year: String = ""
)

@JsonClass(generateAdapter = true)
data class AlAdhanGregorianDate(
    val date: String = "",
    val day: String = "",
    val weekday: AlAdhanWeekdayField? = null,
    val month: AlAdhanMonthField? = null,
    val year: String = ""
)

@JsonClass(generateAdapter = true)
data class AlAdhanNameField(
    val en: String = "",
    val ar: String = ""
)

@JsonClass(generateAdapter = true)
data class AlAdhanWeekdayField(
    val en: String = ""
)

@JsonClass(generateAdapter = true)
data class AlAdhanMonthField(
    val number: Int = 0,
    val en: String = "",
    val ar: String = ""
)

@JsonClass(generateAdapter = true)
data class AlAdhanMeta(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val timezone: String = "",
    val method: AlAdhanMethod? = null
)

@JsonClass(generateAdapter = true)
data class AlAdhanMethod(
    val id: Int = 0,
    val name: String = ""
)

interface AlAdhanApiService {
    @GET("v1/timings/{date}")
    suspend fun getTimings(
        @Path("date") date: String, // format: "DD-MM-YYYY" e.g. "22-09-2026"
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("method") method: Int
    ): AlAdhanResponse
}

object AlAdhanNetworkClient {
    private const val BASE_URL = "https://api.aladhan.com/"

    private val moshi: Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    val apiService: AlAdhanApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(AlAdhanApiService::class.java)
    }
}
