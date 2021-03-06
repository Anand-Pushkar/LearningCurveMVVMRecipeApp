package com.example.learningcurvemvvmrecipeapp.interactors.recipe_list

import android.util.Log
import com.example.learningcurvemvvmrecipeapp.cache.RecipeDao
import com.example.learningcurvemvvmrecipeapp.cache.model.RecipeEntityMapper
import com.example.learningcurvemvvmrecipeapp.domain.data.DataState
import com.example.learningcurvemvvmrecipeapp.domain.model.Recipe
import com.example.learningcurvemvvmrecipeapp.network.RecipeService
import com.example.learningcurvemvvmrecipeapp.network.model.RecipeDtoMapper
import com.example.learningcurvemvvmrecipeapp.presentation.ui.recipe_list.PAGE_SIZE
import com.example.learningcurvemvvmrecipeapp.util.RECIPE_PAGINATION_PAGE_SIZE
import com.example.learningcurvemvvmrecipeapp.util.TAG
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchRecipes(
    private val recipeDao: RecipeDao,
    private val recipeService: RecipeService,
    private val entityMapper: RecipeEntityMapper,
    private val dtoMapper: RecipeDtoMapper
) {
    fun execute(
        token: String,
        page: Int,
        query: String,
        isNetworkAvailable: Boolean,
    ): Flow<DataState<List<Recipe>>> = flow {

        try {
            emit(DataState.loading()) // 1st emission at index [0]

            // just to show pagination and progress bar, the api is fast
            delay(1000)

            // if there is a network connection
            if (isNetworkAvailable) {
                // #1 get the recipes from network
                // Convert: NetworkRecipeEntity -> Recipe -> RecipeCacheEntity
                val recipes = getRecipesFromNetwork(
                    token = token,
                    page = page,
                    query = query
                )
                Log.d(TAG, "execute:  ${recipes.size}")

                // #2 insert recipes into cache
                recipeDao.insertRecipes(entityMapper.fromDomainList(recipes))
            }

            // #3 query the cache
            Log.d(TAG, "SearchRecipes execute: query =  $query")
            val cacheResult = if (query.isBlank()) {
                recipeDao.getAllRecipes(
                    pageSize = RECIPE_PAGINATION_PAGE_SIZE,
                    page = page
                )
            } else {
                recipeDao.searchRecipes(
                    query = query,
                    pageSize = RECIPE_PAGINATION_PAGE_SIZE,
                    page = page
                )
            }
            Log.d(TAG, "SearchRecipes execute: list size =  ${cacheResult.size}")

            // #4 emit List<Recipe> from the cache
            val list = entityMapper.toDomainList(cacheResult)

            emit(DataState.success(list)) // 2nd emission at index [1]


        } catch (e: Exception) {
            Log.e(TAG, "execute: ${e.message}")
            emit(DataState.error<List<Recipe>>(e.message ?: "Unknown error"))
        }
    }

    // This can throw an exception if there is no network
    private suspend fun getRecipesFromNetwork(
        token: String,
        page: Int,
        query: String
    ): List<Recipe> {
        return dtoMapper.toDomainList(
            recipeService.search(
                token = token,
                page = page,
                query = query
            ).recipes
        )
    }
}