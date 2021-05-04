package com.example.learningcurvemvvmrecipeapp.repository

import com.example.learningcurvemvvmrecipeapp.domain.model.Recipe

interface RecipeRepository {

    suspend fun search(token: String, page: Int, query: String): List<Recipe>

    suspend fun get(token: String, id: Int): Recipe
}