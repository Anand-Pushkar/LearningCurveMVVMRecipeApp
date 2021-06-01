package com.example.learningcurvemvvmrecipeapp.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.viewinterop.viewModel
import androidx.hilt.navigation.HiltViewModelFactory
import androidx.navigation.NavType
import androidx.navigation.compose.*
import com.example.learningcurvemvvmrecipeapp.presentation.navigation.Screen
import com.example.learningcurvemvvmrecipeapp.presentation.ui.recipe.RecipeDetailScreen
import com.example.learningcurvemvvmrecipeapp.presentation.ui.recipe.RecipeDetailViewModel
import com.example.learningcurvemvvmrecipeapp.presentation.ui.recipe_list.RecipeListScreen
import com.example.learningcurvemvvmrecipeapp.presentation.ui.recipe_list.RecipeListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = Screen.RecipeList.route
            ) {

                // Destination #1 RecipeListScreen
                composable(route = Screen.RecipeList.route) { navBackStackEntry ->

                    val factory = HiltViewModelFactory(
                        AmbientContext.current, navBackStackEntry
                    )
                    val viewModel: RecipeListViewModel = viewModel(
                        "RecipeListViewModel", factory
                    )

                    RecipeListScreen(
                        isDarkTheme = (application as BaseApplication).isDark.value,
                        onToggleTheme = { (application as BaseApplication)::toggleTheme },
                        onNavigateToRecipeDetailScreen = navController::navigate,
//                        {
//                            navController.navigate(it)
//                        },
                        viewModel = viewModel
                    )
                }

                // Destination #2 RecipeDetailScreen
                composable(
                    route = Screen.RecipeDetail.route + "/{recipeId}",
                    arguments = listOf(navArgument("recipeId") {
                        type = NavType.IntType
                    })
                ) { navBackStackEntry ->

                    val factory = HiltViewModelFactory(
                        AmbientContext.current, navBackStackEntry
                    )
                    val viewModel: RecipeDetailViewModel = viewModel(
                        "RecipeDetailViewModel", factory
                    )

                    RecipeDetailScreen(
                        isDarkTheme = (application as BaseApplication).isDark.value,
                        recipeId = navBackStackEntry.arguments?.getInt("recipeId"),
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}