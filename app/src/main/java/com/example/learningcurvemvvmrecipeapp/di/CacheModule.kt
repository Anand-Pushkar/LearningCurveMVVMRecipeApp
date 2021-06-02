package com.example.learningcurvemvvmrecipeapp.di

import androidx.room.Room
import com.example.learningcurvemvvmrecipeapp.cache.RecipeDao
import com.example.learningcurvemvvmrecipeapp.cache.database.AppDatabase
import com.example.learningcurvemvvmrecipeapp.cache.model.RecipeEntityMapper
import com.example.learningcurvemvvmrecipeapp.presentation.BaseApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CacheModule {

    @Singleton
    @Provides
    fun provideDb(app: BaseApplication): AppDatabase{
        return Room
            .databaseBuilder(app, AppDatabase::class.java, AppDatabase.DATABASE_NAME)
            //.addMigrations() use this in production (maybe, check on it more)
            .fallbackToDestructiveMigration()// don't use this in production ever
            .build()
    }

    @Singleton
    @Provides
    fun provideRecipeDao(app: AppDatabase): RecipeDao{
        return app.recipeDao()
    }

    @Singleton
    @Provides fun provideCacheRecipeMapper(): RecipeEntityMapper{
        return RecipeEntityMapper()
    }

}