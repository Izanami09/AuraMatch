package com.example.auraprototype.di

import android.app.Application
import android.content.Context
import com.example.auraprototype.data.FaceShapeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule{

    @Provides
    @Singleton
    fun provideFaceShapeRepository(@ApplicationContext context: Context) : FaceShapeRepository {
        return FaceShapeRepository(context)
    }


}