package com.example.fnx_huerto_hogar.data.repository

import com.example.fnx_huerto_hogar.Remote.RetrofitWeatherInstance
import com.example.fnx_huerto_hogar.data.model.WeatherResponse

class WeatherRepository {

    suspend fun getWeather(
        lat: Double,
        lon: Double,
        apiKey: String
    ): WeatherResponse {
        return RetrofitWeatherInstance.api.getWeatherByCoordinates(
            lat = lat,
            lon = lon,
            apiKey = apiKey
        )
    }
}
