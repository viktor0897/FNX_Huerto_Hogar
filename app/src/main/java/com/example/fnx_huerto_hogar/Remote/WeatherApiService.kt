package com.example.fnx_huerto_hogar.Remote

import com.example.fnx_huerto_hogar.data.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("weather")
    suspend fun getWeatherByCoordinates(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric", // Temperatura en °C
        @Query("lang") lang: String = "es"
    ): WeatherResponse
}