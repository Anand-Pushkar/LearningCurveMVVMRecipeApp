package com.example.learningcurvemvvmrecipeapp.presentation.navigation

sealed class Screen(
    val route: String,
) {
    object RecipeList: Screen("recipeList")
    object RecipeDetail: Screen("recipeDetail")
}