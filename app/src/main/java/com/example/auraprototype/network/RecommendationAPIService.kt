package com.example.auraprototype.network

import com.example.auraprototype.model.Recommendation
import com.example.auraprototype.model.Resource
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RecommendationAPIService {

    @GET("api/users")
    suspend fun getUsersByFaceShapeAndGender(
        @Query("faceshape") faceShape: String,
        @Query("gender") gender: String = "male"
    ): Response<Recommendation>


}
