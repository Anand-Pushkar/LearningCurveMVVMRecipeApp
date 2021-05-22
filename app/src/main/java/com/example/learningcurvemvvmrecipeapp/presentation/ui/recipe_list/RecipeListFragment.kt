package com.example.learningcurvemvvmrecipeapp.presentation.ui.recipe_list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.ScrollableRow
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.savedinstancestate.savedInstanceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush.Companion.linearGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import com.example.learningcurvemvvmrecipeapp.presentation.BaseApplication
import com.example.learningcurvemvvmrecipeapp.presentation.components.*
import com.example.learningcurvemvvmrecipeapp.presentation.components.HeartAnimationDefinition.HeartButtonState.ACTIVE
import com.example.learningcurvemvvmrecipeapp.presentation.components.HeartAnimationDefinition.HeartButtonState.IDLE
import com.example.learningcurvemvvmrecipeapp.presentation.theme.AppTheme
import com.example.learningcurvemvvmrecipeapp.util.TAG
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RecipeListFragment : Fragment() {

    @Inject
    lateinit var application: BaseApplication

    val viewModel: RecipeListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                AppTheme(
                    darkTheme = application.isDark.value
                ) {
                    val recipes = viewModel.recipes.value
                    val query = viewModel.query.value
                    val selectedCategory = viewModel.selectedCategory.value
                    val loading = viewModel.loading.value

                    Scaffold(
                        topBar = {
                            SearchAppBar(
                                query = query,
                                onQueryChanged = viewModel::onQueryChanged,
                                onExecuteSearch = viewModel::newSearch,
                                scrollPosition = viewModel.categoryScrollPosition,
                                selectedCategory = selectedCategory,
                                onSelectedCategoryChanged = viewModel::onSelectedCategoryChanged,
                                onChangeCategoryScrollPosition = viewModel::onChangeCategoryScrollPosition,
                                onToggleTheme = {
                                    application.toggleTheme()
                                }
                            )
                        },
                        bottomBar = {
                                    MyBottomBar()
                        },
                        drawerContent = {
                                        MyDrawer()
                        },

                        ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color = MaterialTheme.colors.background)
                        ) {
                            if (loading) {
                                LoadingRecipeListShimmer(imageHeight = 250.dp)
                            } else {
                                LazyColumn {
                                    itemsIndexed(
                                        items = recipes
                                    ) { index, recipe ->
                                        RecipeCard(recipe = recipe, onClick = {})
                                    }
                                }
                            }
                            CircularIndeterminateProgressBar(
                                isDisplayed = loading,
                                verticalBias = 0.3f
                            )
                        }

                    }
                }
            }
        }
    }
}

@Composable
fun MyBottomBar(
    //navigationController: NavController
){
    BottomNavigation(
        elevation = 12.dp
    ) {
        BottomNavigationItem(
            icon = { Icon(Icons.Default.BrokenImage) },
            selected = false,
            onClick = {
                //navigationController.navigate()
            }
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Default.Search) },
            selected = true,
            onClick = {}
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Default.AccountBalanceWallet) },
            selected = false,
            onClick = {}
        )
    }
}

@Composable
fun MyDrawer(){
    Column() {
        Text("Item1")
        Text("Item2")
        Text("Item3")
        Text("Item4")
        Text("Item5")
    }
}
