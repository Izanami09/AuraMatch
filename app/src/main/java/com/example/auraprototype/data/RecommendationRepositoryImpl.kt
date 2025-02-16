package com.example.auraprototype.data
import com.example.auraprototype.model.Recommendation
import com.example.auraprototype.model.Resource
import com.example.auraprototype.network.RecommendationAPIService

import javax.inject.Inject

class RecommendationRepositoryImpl @Inject constructor(
    private val recommendationAPIService : RecommendationAPIService,
    private val recommendationLocalDataSource : String
) : RecommendationRepository {

    override suspend fun getRecommendations(
        faceShape: String,
        gender: String
    ): Resource<Recommendation> {
        return try{
            val response = recommendationAPIService.getUsersByFaceShapeAndGender(faceShape, gender)
            if(response.isSuccessful){
                val recommendations = response.body() ?: Recommendation(
                    filteredBread = emptyList(),
                    filteredGlass = emptyList(),
                    filteredHair = emptyList()
                )
                Resource.Success(recommendations)
            } else {
                Resource.Error("Error: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception){
            Resource.Error("Network error: ${e.localizedMessage}", e)
        }
    }

}