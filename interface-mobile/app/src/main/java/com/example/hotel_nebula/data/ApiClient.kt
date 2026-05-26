package com.example.hotel_nebula.data

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    // Android emulators cannot use localhost to reach the computer.
    // 10.0.2.2 is the emulator alias for the host machine running Spring Boot.
    const val DEFAULT_BASE_URL = "http://10.0.2.2:8083/"

    fun create(baseUrl: String): HotelNebulaApi {
        // Retrofit requires base URLs to end with a slash.
        val normalizedUrl = baseUrl.trim().let { url ->
            if (url.endsWith("/")) url else "$url/"
        }

        // BASIC logs method, URL and response code, which is useful for beginners
        // without printing every request/response body in Logcat.
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl(normalizedUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HotelNebulaApi::class.java)
    }
}
