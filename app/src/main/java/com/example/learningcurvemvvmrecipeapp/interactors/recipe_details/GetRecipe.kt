package com.example.learningcurvemvvmrecipeapp.interactors.recipe_details

import android.util.Log
import com.example.learningcurvemvvmrecipeapp.cache.RecipeDao
import com.example.learningcurvemvvmrecipeapp.cache.model.RecipeEntityMapper
import com.example.learningcurvemvvmrecipeapp.domain.data.DataState
import com.example.learningcurvemvvmrecipeapp.domain.model.Recipe
import com.example.learningcurvemvvmrecipeapp.network.RecipeService
import com.example.learningcurvemvvmrecipeapp.network.model.RecipeDtoMapper
import com.example.learningcurvemvvmrecipeapp.util.TAG
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetRecipe(
    private val recipeDao: RecipeDao,
    private val recipeService: RecipeService,
    private val entityMapper: RecipeEntityMapper,
    private val dtoMapper: RecipeDtoMapper
) {
    fun execute(
        recipeId: Int,
        token: String
    ): Flow<DataState<Recipe>> = flow {
        try {
            emit(DataState.loading<Recipe>())

            delay(1000)
            // getting recipe from the cache
            var recipe = getRecipeFromCache(recipeId = recipeId)

            // if cache holds the recipe
            if(recipe != null){
                emit(DataState.success(recipe))
            }
            /**
             * if recipe is null, we will get the recipe from the network,
             * put it into cache and then retrieve the recipe from cache
             * and then emit the recipe
             */
            else{

                // get recipe from te network
                val networkRecipe = getRecipeFromNetwork(token, recipeId)

                // insert into cache
                recipeDao.insertRecipe(
                    entityMapper.mapFromDomainModel(networkRecipe)
                )

                // get recipe from cache
                recipe = getRecipeFromCache(recipeId = recipeId)

                // emit recipe
                // this should not be null now but if it is we throw exception
                if(recipe != null){
                    emit(DataState.success(recipe))
                }
                else{
                    throw Exception("Unable to get the recipe from the cache.")
                }

            }

        } catch (e: Exception) {
            Log.e(TAG, "execute: ${e.message}")
            emit(DataState.error<Recipe>(e.message ?: "Unknown error"))
        }

    }

    private suspend fun getRecipeFromCache(recipeId: Int): Recipe?{
        return recipeDao.getRecipeById(recipeId)?.let { recipeEntity ->
            entityMapper.mapToDomainModel(recipeEntity)
        }
    }

    private suspend fun getRecipeFromNetwork(token: String, recipeId: Int): Recipe{
        return dtoMapper.mapToDomainModel(recipeService.get(token, recipeId))
    }
}