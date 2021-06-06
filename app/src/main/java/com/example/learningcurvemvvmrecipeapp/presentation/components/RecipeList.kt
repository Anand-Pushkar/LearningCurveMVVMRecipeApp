package com.example.learningcurvemvvmrecipeapp.presentation.components

import android.os.Bundle
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.learningcurvemvvmrecipeapp.R
import com.example.learningcurvemvvmrecipeapp.domain.model.Recipe
import com.example.learningcurvemvvmrecipeapp.presentation.components.util.SnackbarController
import com.example.learningcurvemvvmrecipeapp.presentation.navigation.Screen
import com.example.learningcurvemvvmrecipeapp.presentation.ui.recipe_list.PAGE_SIZE
import com.example.learningcurvemvvmrecipeapp.presentation.ui.recipe_list.RecipeListEvent
import com.example.learningcurvemvvmrecipeapp.util.TAG
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun RecipeList(
    loading: Boolean,
    recipes: List<Recipe>,
    onChangeRecipeScrollPosition: (Int) -> Unit,
    page: Int,
    onTriggerNextPage: () -> Unit,
    onNavigateToRecipeDetailScreen: (String) -> Unit,

){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.background)
    ) {
        Log.d(TAG, "RecipeList: loading = $loading")
        if (loading && recipes.isEmpty()) {
            // we are not showing shimmer for new page only for brand new search
            LoadingRecipeListShimmer(imageHeight = 250.dp, repetition = 5)
        }
        else if(recipes.isEmpty()){
            //NothingHere()
        }
        else {
            LazyColumn {
                itemsIndexed(
                    items = recipes
                ) { index, recipe ->
                    onChangeRecipeScrollPosition(index) //tracking scroll position
                    if ((index + 1) >= (page * PAGE_SIZE) && !loading){
                        onTriggerNextPage()
                    }
                    RecipeCard(
                        recipe = recipe,
                        onClick = {
                            val route = Screen.RecipeDetail.route + "/${recipe.id}"
                            onNavigateToRecipeDetailScreen(route)
                        }
                    )
                }
            }
        }

    }
}