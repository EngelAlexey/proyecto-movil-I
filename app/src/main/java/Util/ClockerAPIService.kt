package Util

import Interface.IClockerAPIService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ClockerAPIService {
    private const val BASE_URL = "https://clocker-api.onrender.com"

    val api: IClockerAPIService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(IClockerAPIService::class.java)
    }
}