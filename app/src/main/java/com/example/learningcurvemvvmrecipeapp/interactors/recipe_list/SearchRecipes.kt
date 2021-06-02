package com.example.learningcurvemvvmrecipeapp.interactors.recipe_list

import com.example.learningcurvemvvmrecipeapp.cache.RecipeDao
import com.example.learningcurvemvvmrecipeapp.cache.model.RecipeEntityMapper
import com.example.learningcurvemvvmrecipeapp.domain.data.DataState
import com.example.learningcurvemvvmrecipeapp.domain.model.Recipe
import com.example.learningcurvemvvmrecipeapp.network.RecipeService
import com.example.learningcurvemvvmrecipeapp.network.model.RecipeDtoMapper
import com.example.learningcurvemvvmrecipeapp.presentation.ui.recipe_list.PAGE_SIZE
import com.example.learningcurvemvvmrecipeapp.util.RECIPE_PAGINATION_PAGE_SIZE
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchRecipes(
    private val recipeDao: RecipeDao,
    private val recipeService: RecipeService,
    private val entityMapper: RecipeEntityMapper,
    private val dtoMapper: RecipeDtoMapper
){
    fun execute(
        token: String,
        page: Int,
        query: String
    ): Flow<DataState<List<Recipe>>> = flow{

        try {
            emit(DataState.loading())
            // just to show pagination and progress bar, the api is fast
            delay(1000)

            // TODO("Check if there is internet connection")
            // #1 get the recipes from network
            val recipes = getRecipesFromNetwork(
                token = token,
                page = page,
                query = query
            )

            // #2 insert recipes into cache
            recipeDao.insertRecipes(entityMapper.fromDomainList(recipes))

            // #3 query the cache
            val cacheResult = if(query.isBlank()){
                recipeDao.getAllRecipes(
                    pageSize = RECIPE_PAGINATION_PAGE_SIZE,
                    page = page
                )
            }else{
                recipeDao.searchRecipes(
                    query = query,
                    pageSize = RECIPE_PAGINATION_PAGE_SIZE,
                    page = page
                )
            }

            // #4 emit List<Recipe> from the cache
            val list = entityMapper.toDomainList(cacheResult)
            DataState.success(list)


        }catch (e: Exception){
            emit(DataState.error<List<Recipe>>(e.message?: "Unknown error"))
        }
    }

    // This can throw an exception if there is no network
    private  suspend fun getRecipesFromNetwork(
        token: String,
        page: Int,
        query: String
    ): List<Recipe>{
        return dtoMapper.toDomainList(
            recipeService.search(
                token = token,
                page = page,
                query = query
            ).recipes
        )
    }
}