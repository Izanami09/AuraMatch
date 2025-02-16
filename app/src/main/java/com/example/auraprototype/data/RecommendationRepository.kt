package com.example.auraprototype.data

import com.example.auraprototype.model.Recommendation
import com.example.auraprototype.model.Resource

interface RecommendationRepository{
    suspend fun getRecommendations(faceShape:String, gender:String) : Resource<Recommendation>
}

