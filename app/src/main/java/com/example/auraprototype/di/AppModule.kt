package com.example.auraprototype.di

import android.content.Context
import com.example.auraprototype.data.FaceShapeRepository
import com.example.auraprototype.data.RecommendationRepositoryImpl
import com.example.auraprototype.domain.sc.hasNetwork
import com.example.auraprototype.network.RecommendationAPIService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
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
    fun provideOkHttpClient(@ApplicationContext context: Context): OkHttpClient {
        val cacheSize = (10 * 1024 * 1024).toLong()

        //Initializing instance of Cache class
        val myCache = Cache(context.cacheDir, cacheSize)

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }


        return OkHttpClient.Builder()
            .cache(myCache)
            .addInterceptor{
                chain ->
                var request = chain.request()
                request = if(hasNetwork(context))
                    request
                        .newBuilder()
                        .cacheControl(
                            CacheControl.Builder()
                                .maxAge(30, TimeUnit.MINUTES)
                                .build()
                        )
                        .build()
                else
                    request
                        .newBuilder()
                        .cacheControl(
                            CacheControl.Builder()
                                .maxStale(1, TimeUnit.DAYS)
                                .build()
                        )
                        .build()
                chain.proceed(request)

            }
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
    fun provideRecommendationRepositoryImpl(recommendationAPIService: RecommendationAPIService) : RecommendationRepositoryImpl {
        return RecommendationRepositoryImpl(recommendationAPIService, "")
    }
}