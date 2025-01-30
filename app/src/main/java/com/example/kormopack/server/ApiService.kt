package com.example.kormopack.server

import com.example.kormopack.data.specsdatabase.SpecificationBrandEntity
import com.example.kormopack.data.specsdatabase.SpecificationFeedEntity
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @GET("/feedbrands")
    suspend fun getFeedBrands(): List<SpecificationBrandEntity>
    @GET("/feeds")
    suspend fun getFeeds(@Query("brand") brand: String): List<SpecificationFeedEntity>
    @GET("/feeds")
    suspend fun getFeeds(@Query("brand") brand: String, @Query("keyWord") keyWord: String): List<SpecificationFeedEntity>
    @POST("/addBrand")
    suspend fun addBrand(@Query("brandName") brandName: String): Response<Unit>
    @POST("/addFeed")
    suspend fun addFeed(@Query("spec") spec: String): Response<Unit>
}

object RetrofitClient {
    private const val BASE_URL = "http://localhost:8081"

    val interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}