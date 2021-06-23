package com.example.learningcurvemvvmrecipeapp.cache

import com.example.learningcurvemvvmrecipeapp.cache.model.RecipeEntity

/**
 * We are writing UNIT TESTS to test our USE CASES,
 * the LOGIC inside our USE CASES ONLY.
 */

class RecipeDaoFake(
    private val appDatabaseFake: AppDatabaseFake
): RecipeDao {
    override suspend fun insertRecipe(recipe: RecipeEntity): Long {

        /**
         * This is how to test the errors return from the cache in the use cases
         */
//        if(recipe.id == 1){
//            throw Exception("Something went wrong")
//        }

        appDatabaseFake.recipes.add(recipe)
        return 1 // return success
    }

    override suspend fun insertRecipes(recipes: List<RecipeEntity>): LongArray {
        appDatabaseFake.recipes.addAll(recipes)
        return longArrayOf(1) // return success
    }

    override suspend fun getRecipeById(id: Int): RecipeEntity? {
        return appDatabaseFake.recipes.find { recipe ->
            recipe.id == id
        }
    }

    override suspend fun deleteRecipes(ids: List<Int>): Int {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllRecipes() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteRecipe(primaryKey: Int): Int {
        TODO("Not yet implemented")
    }

    override suspend fun searchRecipes(
        query: String,
        page: Int,
        pageSize: Int
    ): List<RecipeEntity> {
        return appDatabaseFake.recipes
    }

    override suspend fun getAllRecipes(page: Int, pageSize: Int): List<RecipeEntity> {
        return appDatabaseFake.recipes
    }

    override suspend fun restoreRecipes(
        query: String,
        page: Int,
        pageSize: Int
    ): List<RecipeEntity> {
        return appDatabaseFake.recipes
    }

    override suspend fun restoreAllRecipes(page: Int, pageSize: Int): List<RecipeEntity> {
        return appDatabaseFake.recipes
    }

}