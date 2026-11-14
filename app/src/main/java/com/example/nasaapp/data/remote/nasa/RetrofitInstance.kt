
package com.example.nasaapp.data.remote.nasa

import com.example.nasaapp.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val BASE_URL = BuildConfig.API_BASE_URL

    val api: NasaApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NasaApiService::class.java)
    }
}
