package com.example.learningcurvemvvmrecipeapp.presentation.ui.recipe_list

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learningcurvemvvmrecipeapp.domain.model.Recipe
import com.example.learningcurvemvvmrecipeapp.repository.RecipeRepository
import com.example.learningcurvemvvmrecipeapp.util.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

const val PAGE_SIZE = 30

@HiltViewModel
class RecipeListViewModel
@Inject
constructor(
    private val repository: RecipeRepository,
    private @Named("auth_token") val token: String
) : ViewModel() {

    val recipes: MutableState<List<Recipe>> = mutableStateOf(listOf())
    val query = mutableStateOf("")
    val selectedCategory: MutableState<FoodCategory?> = mutableStateOf(null) // for chips
    var categoryScrollPosition: Float = 0f
    var loading = mutableStateOf(false)
    val page = mutableStateOf(1)
    private var recipeListScrollPosition = 0

    init {
        newSearch()
    }

    fun newSearch() {
        viewModelScope.launch {
            loading.value = true

            resetSearchState()
            delay(1000)

            val result = repository.search(
                token = token,
                page = 1,
                query = query.value
            )
            recipes.value = result
            loading.value = false
        }
    }

    fun nextPage(){
        viewModelScope.launch {
            /* prevent duplicate events due to recompose happening to quickly
             * we will fetch the new page when user scrolls to the bottom.
             * when the user is at the bottom it will keep fetching new page
             * as the function might be called multiple number of times
             * but we just need to fetch 1 page at a time.
             * So we will put a lock there saying fetch a new page
             * when user hits the bottom but stop fetching if a query is going on
             */

            if((recipeListScrollPosition + 1) >= (page.value * PAGE_SIZE)){
                loading.value = true
                incrementPage()
                Log.d(TAG, "nextPage: triggered: ${page.value}")

                // just to show pagination, api is fast
                delay(1000)

                // api request
                if (page.value > 1){
                    val result = repository.search(
                        token = token,
                        page = page.value,
                        query = query.value
                    )
                    Log.d(TAG, "nextPage: ${result}")
                    appendRecipes(result)
                }
                loading.value = false
            }
        }
    }

    /*
     * Append new recipes to the current list of recipes
     */
    private fun appendRecipes(recipes: List<Recipe>){
        val current = ArrayList(this.recipes.value)
        current.addAll(recipes)
        this.recipes.value = current
    }

    private fun incrementPage(){
        page.value = page.value + 1
    }

    fun onChangeRecipeScrollPosition(position: Int){
        recipeListScrollPosition = position
    }

    /*
     * Called when a new search is executed
     */
    private fun resetSearchState() {
        recipes.value = listOf()
        page.value = 1
        onChangeRecipeScrollPosition(0)
        if (selectedCategory.value?.value != query.value) {
            clearSelectedCategory()
        }
    }

    private fun clearSelectedCategory() {
        selectedCategory.value = null
    }

    fun onQueryChanged(query: String) {
        this.query.value = query
    }

    fun onSelectedCategoryChanged(category: String) {
        val newCategory = getFoodCategory(category)
        selectedCategory.value = newCategory
        onQueryChanged(category)
    }

    fun onChangeCategoryScrollPosition(position: Float) {
        categoryScrollPosition = position
    }
}