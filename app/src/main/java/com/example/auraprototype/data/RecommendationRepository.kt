package com.example.auraprototype.data

import com.example.auraprototype.data.remote.RecommendationRemoteDataSource
import com.example.auraprototype.model.Recommendation
import com.example.auraprototype.model.Resource
import kotlinx.coroutines.CoroutineScope
import retrofit2.Response
import javax.inject.Inject

class RecommendationRepository @Inject constructor(
    private val recommendationRemoteDataSource: RecommendationRemoteDataSource
    //private val externalScope: CoroutineScope
){
    suspend fun getRecommendations(faceShape:String) : Resource<Recommendation> {
        return try{
            val response = recommendationRemoteDataSource.getRecommendations(faceShape)
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

