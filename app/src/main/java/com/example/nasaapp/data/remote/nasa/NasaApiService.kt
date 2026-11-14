
package com.example.nasaapp.data.remote.nasa

import com.example.nasaapp.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Query

interface NasaApiService {

    @GET("https://science.nasa.gov/wp-json/wp/v2/apod-basic")
    suspend fun getApod(
        @Query("date_from") startDate: String,
        @Query("date_to") endDate: String
    ): List<ApodDto>

    @GET("neo/rest/v1/feed")
    suspend fun getAsteroids(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY
    ): NeoFeedDto
}
