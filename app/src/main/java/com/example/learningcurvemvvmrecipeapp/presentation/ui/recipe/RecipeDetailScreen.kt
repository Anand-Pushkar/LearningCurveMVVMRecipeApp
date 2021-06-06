package com.example.learningcurvemvvmrecipeapp.presentation.ui.recipe

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.learningcurvemvvmrecipeapp.presentation.components.LoadingRecipeListShimmer
import com.example.learningcurvemvvmrecipeapp.presentation.components.RecipeView
import com.example.learningcurvemvvmrecipeapp.presentation.theme.AppTheme

@ExperimentalMaterialApi
@Composable
fun RecipeDetailScreen(
    isDarkTheme: Boolean,
    recipeId: Int?,
    viewModel: RecipeDetailViewModel
) {

    if (recipeId == null) {
        // show invalid recipe
    } else {
        // fire a one-off event to get the recipe from api
        val onLoad = viewModel.onLoad.value
        if (!onLoad) {
            viewModel.onLoad.value = true
            viewModel.onTriggerEvent(RecipeEvent.GetRecipeEvent(recipeId))
        }

        val loading = viewModel.loading.value
        val recipe = viewModel.recipe.value
        val scaffoldState = rememberScaffoldState()
        val dialogQueue = viewModel.dialogQueue

        AppTheme(
            darkTheme = isDarkTheme,
            displayProgressBar = loading,
            scaffoldState = scaffoldState,
            dialogQueue = dialogQueue.queue.value
        ) {
            Scaffold(
                scaffoldState = scaffoldState,
                snackbarHost = { scaffoldState.snackbarHostState }
            ) {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (loading && recipe == null) {
                        LoadingRecipeListShimmer(imageHeight = 260.dp, lines = 3)
                    } else if (!loading && recipe == null && onLoad) {
                        //("Show Invalid Recipe")
                    } else recipe?.let {
                        RecipeView(
                            recipe = it,
                        )
                    }
                }
            }
        }
    }
}