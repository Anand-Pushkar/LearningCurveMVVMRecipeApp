package com.example.learningcurvemvvmrecipeapp.interactors.recipe_list

import android.util.Log
import com.example.learningcurvemvvmrecipeapp.cache.RecipeDao
import com.example.learningcurvemvvmrecipeapp.cache.model.RecipeEntityMapper
import com.example.learningcurvemvvmrecipeapp.domain.data.DataState
import com.example.learningcurvemvvmrecipeapp.domain.model.Recipe
import com.example.learningcurvemvvmrecipeapp.util.RECIPE_PAGINATION_PAGE_SIZE
import com.example.learningcurvemvvmrecipeapp.util.TAG
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RestoreRecipes(
    private val recipeDao: RecipeDao,
    private val entityMapper: RecipeEntityMapper,
){
    fun execute(
        page: Int,
        query: String
    ): Flow<DataState<List<Recipe>>> = flow {
        try {
            emit(DataState.loading())

            delay(1000)

            val cacheResult = if(query.isBlank()){
                recipeDao.restoreAllRecipes(
                    page = page
                )
            }else{
                recipeDao.restoreRecipes(
                    query = query,
                    page = page
                )
            }

            val list = entityMapper.toDomainList(cacheResult)

            emit(DataState.success(list))

        }catch (e: Exception){
            Log.e(TAG, "execute: ${e.message}")
            emit(DataState.error<List<Recipe>>(e.message?: "Unknown error"))
        }
    }
}