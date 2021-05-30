package com.example.learningcurvemvvmrecipeapp.presentation.components

import android.os.Bundle
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
import com.example.learningcurvemvvmrecipeapp.presentation.ui.recipe_list.PAGE_SIZE
import com.example.learningcurvemvvmrecipeapp.presentation.ui.recipe_list.RecipeListEvent
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun RecipeList(
    loading: Boolean,
    recipes: List<Recipe>,
    onChangeRecipeScrollPosition: (Int) -> Unit,
    page: Int,
    onNextPage: (RecipeListEvent) -> Unit,
    scaffoldState: ScaffoldState,
    snackbarController: SnackbarController,
    navController: NavController,
){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.background)
    ) {
        if (loading && recipes.isEmpty()) {
            // we are not showing shimmer for new page only for brand new search
            LoadingRecipeListShimmer(imageHeight = 250.dp, repetition = 5)
        } else {
            LazyColumn {
                itemsIndexed(
                    items = recipes
                ) { index, recipe ->
                    onChangeRecipeScrollPosition(index) //tracking scroll position
                    if ((index + 1) >= (page * PAGE_SIZE) && !loading){
                        onNextPage(RecipeListEvent.NextPageEvent)
                    }
                    RecipeCard(
                        recipe = recipe,
                        onClick = {
                            /*
                             * recipe.id should not be null by this point.
                             * if the id of any recipe is null then there
                             * is some error somewhere and that particular
                             * recipe shouldn't have been shown in the list
                             * in the first place. null handling for that
                             * should have been done in the repository or
                             * domain in the first place.
                             */
                            if(recipe.id != null){
                                val bundle = Bundle()
                                bundle.putInt("recipeId", recipe.id)
                                navController.navigate(R.id.viewRecipe, bundle)
                            }
                            else{
                                snackbarController.getScope().launch {
                                    snackbarController.showSnackbar(
                                        scaffoldState = scaffoldState,
                                        message = "Recipe Error!",
                                        actionLabel = "Ok"
                                    )
                                }
                            }
                        }
                    )
                }
            }
        }

    }
}