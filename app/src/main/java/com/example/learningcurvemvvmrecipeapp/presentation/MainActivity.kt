package com.example.learningcurvemvvmrecipeapp.presentation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.HiltViewModelFactory
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import com.example.learningcurvemvvmrecipeapp.datastore.SettingsDataStore
import com.example.learningcurvemvvmrecipeapp.presentation.navigation.Screen
import com.example.learningcurvemvvmrecipeapp.presentation.ui.recipe.RecipeDetailScreen
import com.example.learningcurvemvvmrecipeapp.presentation.ui.recipe.RecipeDetailViewModel
import com.example.learningcurvemvvmrecipeapp.presentation.ui.recipe_list.RecipeListScreen
import com.example.learningcurvemvvmrecipeapp.presentation.ui.recipe_list.RecipeListViewModel
import com.example.learningcurvemvvmrecipeapp.presentation.util.MyConnectivityManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var myConnectivityManager: MyConnectivityManager

    @Inject
    lateinit var settingsDataStore: SettingsDataStore

    override fun onStart() {
        super.onStart()
        myConnectivityManager.registerConnectionObserver(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        myConnectivityManager.unregisterConnectionObserver(this)
    }

    @ExperimentalComposeUiApi
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
                        LocalContext.current, navBackStackEntry
                    )
                    val viewModel: RecipeListViewModel = viewModel(
                        "RecipeListViewModel", factory
                    )

                    RecipeListScreen(
                        isDarkTheme = settingsDataStore.isDark.value,
                        isNetworkAvailable = myConnectivityManager.isNetworkAvailable.value,
                        onToggleTheme = settingsDataStore::toggleTheme,
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
                        LocalContext.current, navBackStackEntry
                    )
                    val viewModel: RecipeDetailViewModel = viewModel(
                        "RecipeDetailViewModel", factory
                    )

                    RecipeDetailScreen(
                        isDarkTheme = settingsDataStore.isDark.value,
                        isNetworkAvailable = myConnectivityManager.isNetworkAvailable.value,
                        recipeId = navBackStackEntry.arguments?.getInt("recipeId"),
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}