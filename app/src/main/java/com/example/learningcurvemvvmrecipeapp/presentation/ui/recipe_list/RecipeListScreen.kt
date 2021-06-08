package com.example.learningcurvemvvmrecipeapp.presentation.ui.recipe_list

import android.util.Log
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.navigation.fragment.findNavController
import com.example.learningcurvemvvmrecipeapp.presentation.components.RecipeList
import com.example.learningcurvemvvmrecipeapp.presentation.components.SearchAppBar
import com.example.learningcurvemvvmrecipeapp.presentation.theme.AppTheme
import com.example.learningcurvemvvmrecipeapp.util.TAG
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun RecipeListScreen(
    isDarkTheme: Boolean,
    isNetworkAvailable: Boolean,
    onToggleTheme: () -> Unit,
    onNavigateToRecipeDetailScreen: (String) -> Unit,
    viewModel: RecipeListViewModel

){
    Log.d(TAG, "RecipeListScreen: $viewModel")
    val recipes = viewModel.recipes.value
    val query = viewModel.query.value
    val selectedCategory = viewModel.selectedCategory.value
    val loading = viewModel.loading.value
    val scaffoldState = rememberScaffoldState()
    val page = viewModel.page.value
    val dialogQueue = viewModel.dialogQueue

    AppTheme(
        darkTheme = isDarkTheme,
        isNetworkAvailable = isNetworkAvailable,
        displayProgressBar = loading,
        scaffoldState = scaffoldState,
        dialogQueue = dialogQueue.queue.value
    ) {

        Scaffold(
            topBar = {
                SearchAppBar(
                    query = query,
                    onQueryChanged = viewModel::onQueryChanged,
                    onExecuteSearch = {
                        viewModel.onTriggerEvent(RecipeListEvent.NewSearchEvent)
                    },
                    categories = getAllFoodCategories(),
                    selectedCategory = selectedCategory,
                    onSelectedCategoryChanged = viewModel::onSelectedCategoryChanged,
                    onToggleTheme = {
                        onToggleTheme()
                    }
                )
            },
            scaffoldState = scaffoldState,
            snackbarHost = {
                scaffoldState.snackbarHostState
            }
        ) {
            Log.d(TAG, "RecipeListScreen#: loading = $loading")
            RecipeList(
                loading = loading,
                recipes = recipes,
                onChangeRecipeScrollPosition = viewModel::onChangeRecipeScrollPosition,
                page = page,
                onTriggerNextPage = {
                    viewModel.onTriggerEvent(RecipeListEvent.NextPageEvent)
                },
                onNavigateToRecipeDetailScreen = onNavigateToRecipeDetailScreen
            )
        }
    }
}