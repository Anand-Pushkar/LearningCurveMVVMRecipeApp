package com.example.learningcurvemvvmrecipeapp.di

import com.example.learningcurvemvvmrecipeapp.cache.RecipeDao
import com.example.learningcurvemvvmrecipeapp.cache.model.RecipeEntityMapper
import com.example.learningcurvemvvmrecipeapp.interactors.recipe_details.GetRecipe
import com.example.learningcurvemvvmrecipeapp.interactors.recipe_list.RestoreRecipes
import com.example.learningcurvemvvmrecipeapp.interactors.recipe_list.SearchRecipes
import com.example.learningcurvemvvmrecipeapp.network.RecipeService
import com.example.learningcurvemvvmrecipeapp.network.model.RecipeDtoMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object InteractorsModule {

    @ViewModelScoped
    @Provides
    fun provideSearchRecipes(
        recipeDao: RecipeDao,
        recipeService: RecipeService,
        recipeEntityMapper: RecipeEntityMapper,
        recipeDtoMapper: RecipeDtoMapper
    ): SearchRecipes{
        return SearchRecipes(
            recipeDao = recipeDao,
            recipeService = recipeService,
            entityMapper = recipeEntityMapper,
            dtoMapper = recipeDtoMapper
        )
    }

    @ViewModelScoped
    @Provides
    fun provideRestoreRecipes(
        recipeDao: RecipeDao,
        recipeEntityMapper: RecipeEntityMapper,
    ): RestoreRecipes{
        return RestoreRecipes(
            recipeDao = recipeDao,
            entityMapper = recipeEntityMapper
        )
    }

    @ViewModelScoped
    @Provides
    fun provideGetRecipe(
        recipeDao: RecipeDao,
        recipeService: RecipeService,
        recipeEntityMapper: RecipeEntityMapper,
        recipeDtoMapper: RecipeDtoMapper
    ): GetRecipe{
        return GetRecipe(
            recipeDao = recipeDao,
            recipeService = recipeService,
            entityMapper = recipeEntityMapper,
            dtoMapper = recipeDtoMapper
        )
    }
}