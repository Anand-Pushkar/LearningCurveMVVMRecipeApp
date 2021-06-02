package com.example.learningcurvemvvmrecipeapp.cache.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.learningcurvemvvmrecipeapp.cache.RecipeDao
import com.example.learningcurvemvvmrecipeapp.cache.model.RecipeEntity

@Database(entities = [RecipeEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase(){

    abstract fun recipeDao(): RecipeDao

    companion object{
        val DATABASE_NAME = "recipe_db"
    }
}