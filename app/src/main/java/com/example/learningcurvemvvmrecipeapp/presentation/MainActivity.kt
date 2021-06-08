package com.example.learningcurvemvvmrecipeapp.presentation

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.net.NetworkRequest
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.viewinterop.viewModel
import androidx.hilt.navigation.HiltViewModelFactory
import androidx.navigation.NavType
import androidx.navigation.compose.*
import com.example.learningcurvemvvmrecipeapp.datastore.SettingsDataStore
import com.example.learningcurvemvvmrecipeapp.interactors.app.DoesNetworkHaveInternet
import com.example.learningcurvemvvmrecipeapp.presentation.navigation.Screen
import com.example.learningcurvemvvmrecipeapp.presentation.ui.recipe.RecipeDetailScreen
import com.example.learningcurvemvvmrecipeapp.presentation.ui.recipe.RecipeDetailViewModel
import com.example.learningcurvemvvmrecipeapp.presentation.ui.recipe_list.RecipeListScreen
import com.example.learningcurvemvvmrecipeapp.presentation.ui.recipe_list.RecipeListViewModel
import com.example.learningcurvemvvmrecipeapp.presentation.util.MyConnectivityManager
import com.example.learningcurvemvvmrecipeapp.util.TAG
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
                        AmbientContext.current, navBackStackEntry
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