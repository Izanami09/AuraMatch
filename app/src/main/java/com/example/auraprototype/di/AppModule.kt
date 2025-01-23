package com.example.auraprototype.di

import android.content.Context
import com.example.auraprototype.data.FaceShapeRepository
import com.example.auraprototype.data.RecommendationRepository
import com.example.auraprototype.data.remote.RecommendationRemoteDataSource
import com.example.auraprototype.network.RecommendationAPIService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule{

    @Provides
    @Singleton
    fun provideFaceShapeRepository(@ApplicationContext context: Context) : FaceShapeRepository {
        return FaceShapeRepository(context)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideRecommendationAPI(okayHttpClient : OkHttpClient) : RecommendationAPIService {
        val baseURL = "https://auramatch.onrender.com/"
        val retrofit = Retrofit.Builder()
            .client(okayHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseURL)
            .build()

        val retrofitService by lazy { retrofit.create(RecommendationAPIService::class.java)}

        return retrofitService
    }

    @Provides
    @Singleton
    fun provideRecommendationRemoteDataSource(apiService: RecommendationAPIService) : RecommendationRemoteDataSource{
        return RecommendationRemoteDataSource(apiService)
    }

    @Provides
    @Singleton
    fun provideRecommendationRepository(remoteDataSource: RecommendationRemoteDataSource) : RecommendationRepository{
        return RecommendationRepository(remoteDataSource)
    }





}