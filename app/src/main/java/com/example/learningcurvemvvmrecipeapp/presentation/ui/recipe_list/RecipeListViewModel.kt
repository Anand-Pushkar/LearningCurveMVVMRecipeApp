package com.example.learningcurvemvvmrecipeapp.presentation.ui.recipe_list

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learningcurvemvvmrecipeapp.domain.model.Recipe
import com.example.learningcurvemvvmrecipeapp.interactors.recipe_list.RestoreRecipes
import com.example.learningcurvemvvmrecipeapp.interactors.recipe_list.SearchRecipes
import com.example.learningcurvemvvmrecipeapp.presentation.ui.recipe_list.RecipeListEvent.*
import com.example.learningcurvemvvmrecipeapp.presentation.ui.util.DialogQueue
import com.example.learningcurvemvvmrecipeapp.presentation.util.MyConnectivityManager
import com.example.learningcurvemvvmrecipeapp.util.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

const val PAGE_SIZE = 30

const val STATE_KEY_PAGE = "recipe.state.page.key"
const val STATE_KEY_QUERY = "recipe.state.query.key"
const val STATE_KEY_LIST_POSITION = "recipe.state.query.list_position"
const val STATE_KEY_SELECTED_CATEGORY = "recipe.state.query.selected_category"


@HiltViewModel
class RecipeListViewModel
@Inject
constructor(
    private val searchRecipes: SearchRecipes, // interactors
    private val restoreRecipes: RestoreRecipes, // interactors
    private val myConnectivityManager: MyConnectivityManager,
    @Named("auth_token") private val token: String,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val recipes: MutableState<List<Recipe>> = mutableStateOf(listOf())
    val query = mutableStateOf("")
    val selectedCategory: MutableState<FoodCategory?> = mutableStateOf(null) // for chips
    var categoryScrollPosition: Float = 0f
    var loading = mutableStateOf(false)
    val page = mutableStateOf(1)
    private var recipeListScrollPosition = 0
    val dialogQueue = DialogQueue()

    init {
        Log.d(TAG, ":init: initializing ")

        /*
         * restoring state after process death
         */
        savedStateHandle.get<Int>(STATE_KEY_PAGE)?.let { page ->
            setPage(page)
        }
        savedStateHandle.get<Int>(STATE_KEY_LIST_POSITION)?.let { position ->
            setScrollPosition(position)
        }
        savedStateHandle.get<FoodCategory>(STATE_KEY_SELECTED_CATEGORY)?.let { category ->
            setSelectedCategory(category)
        }
        savedStateHandle.get<String>(STATE_KEY_QUERY)?.let { query ->
            setQuery(query)
        }

        if (recipeListScrollPosition != 0) {
            onTriggerEvent(RestoreStateEvent)
        } else {
            onTriggerEvent(NewSearchEvent)
        }

    }

    fun onTriggerEvent(event: RecipeListEvent) {
        viewModelScope.launch {
            try {
                when (event) {
                    is NewSearchEvent -> {
                        Log.d(TAG, "onTriggerEvent: triggering")
                        newSearch()
                    }
                    is NextPageEvent -> {
                        nextPage()
                    }
                    is RestoreStateEvent -> {
                        restoreState()
                    }
                }

            } catch (e: Exception) {
                Log.e(TAG, "onTriggerEvent: Exception: ${e}, ${e.cause}")
            }
        }
    }

    private fun restoreState() {
        restoreRecipes.execute(
            page = page.value,
            query = query.value
        ).onEach { dataState ->
            // loading
            loading.value = dataState.loading

            //data
            dataState.data?.let { list ->
                recipes.value = list
            }

            //error
            dataState.error?.let { error ->
                dialogQueue.appendErrorMessage("Error", error)
            }
        }.launchIn(viewModelScope)
    }

    // use case #1
    private fun newSearch() {
        Log.d(TAG, "newSearch: query: ${query.value}, page: ${page.value}")
        // New Search. Reset the state
        resetSearchState()

        searchRecipes.execute(
            token = token,
            page = page.value,
            query = query.value,
            isNetworkAvailable = myConnectivityManager.isNetworkAvailable.value
        ).onEach { dataState ->
            // loading
            loading.value = dataState.loading

            //data
            dataState.data?.let { list ->
                recipes.value = list
            }

            //error
            dataState.error?.let { error ->
                dialogQueue.appendErrorMessage("Error", error)
            }
        }.launchIn(viewModelScope)
    }

    // use case #2
    private fun nextPage() {
        /* prevent duplicate events due to recompose happening to quickly
             * we will fetch the new page when user scrolls to the bottom.
             * when the user is at the bottom it will keep fetching new page
             * as the function might be called multiple number of times
             * but we just need to fetch 1 page at a time.
             * So we will put a lock there saying fetch a new page
             * when user hits the bottom but stop fetching if a query is going on
             */
        if ((recipeListScrollPosition + 1) >= (page.value * PAGE_SIZE)) {
            incrementPage()
            Log.d(TAG, "nextPage: triggered: ${page.value}")

            if (page.value > 1) {
                searchRecipes.execute(
                    token = token,
                    page = page.value,
                    query = query.value,
                    isNetworkAvailable = myConnectivityManager.isNetworkAvailable.value
                ).onEach { dataState ->
                    // loading
                    loading.value = dataState.loading

                    //data
                    dataState.data?.let { list ->
                        appendRecipes(list)
                    }

                    //error
                    dataState.error?.let { error ->
                        Log.e(TAG, "nextPage: $error")
                        dialogQueue.appendErrorMessage("Error", error)
                    }
                }.launchIn(viewModelScope)
            }

        }
    }

    /*
     * Append new recipes to the current list of recipes
     */
    private fun appendRecipes(recipes: List<Recipe>) {
        val current = ArrayList(this.recipes.value)
        current.addAll(recipes)
        this.recipes.value = current
    }

    private fun incrementPage() {
        setPage(page.value + 1)
    }

    fun onChangeRecipeScrollPosition(position: Int) {
        setScrollPosition(position)
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
        setSelectedCategory(null)
    }

    fun onQueryChanged(query: String) {
        setQuery(query)
    }

    fun onSelectedCategoryChanged(category: String) {
        val newCategory = getFoodCategory(category)
        setSelectedCategory(newCategory)
        onQueryChanged(category)
    }

    fun onChangeCategoryScrollPosition(position: Float) {
        categoryScrollPosition = position
    }

    /**
     * saving state for restoration after process death
     * these functions act as setters for the variables and the constants for state
     */
    private fun setScrollPosition(position: Int) {
        recipeListScrollPosition = position
        savedStateHandle.set(STATE_KEY_LIST_POSITION, position)
    }

    private fun setPage(page: Int) {
        this.page.value = page
        savedStateHandle.set(STATE_KEY_PAGE, page)
    }

    private fun setSelectedCategory(category: FoodCategory?) {
        selectedCategory.value = category
        savedStateHandle.set(STATE_KEY_SELECTED_CATEGORY, category)
    }

    private fun setQuery(query: String) {
        this.query.value = query
        savedStateHandle.set(STATE_KEY_QUERY, query)
    }
}