package com.example.auraprototype.data.remote
import com.example.auraprototype.model.Recommendation
import com.example.auraprototype.model.Resource
import com.example.auraprototype.model.Resource.Success
import com.example.auraprototype.network.RecommendationAPIService
import okio.IOException
import retrofit2.HttpException
import retrofit2.Response

import javax.inject.Inject

class RecommendationRemoteDataSource @Inject constructor(
    private val recommendationAPIService : RecommendationAPIService
) {

    suspend fun getRecommendations(faceShape:String) : Response<Recommendation> {
        return recommendationAPIService.getUsersByFaceShapeAndGender(faceShape)
    }

}