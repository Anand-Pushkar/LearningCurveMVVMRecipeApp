package com.example.learningcurvemvvmrecipeapp.presentation.ui.recipe

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learningcurvemvvmrecipeapp.domain.model.Recipe
import com.example.learningcurvemvvmrecipeapp.interactors.recipe_details.GetRecipe
import com.example.learningcurvemvvmrecipeapp.presentation.ui.recipe.RecipeEvent.GetRecipeEvent
import com.example.learningcurvemvvmrecipeapp.presentation.ui.util.DialogQueue
import com.example.learningcurvemvvmrecipeapp.presentation.util.MyConnectivityManager
import com.example.learningcurvemvvmrecipeapp.util.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

const val STATE_KEY_RECIPE = "state.key.recipeId"

@HiltViewModel
class RecipeDetailViewModel
@Inject
constructor(
    private val getRecipe: GetRecipe,
    private val myConnectivityManager: MyConnectivityManager,
    @Named("auth_token") private val token: String,
    private val state: SavedStateHandle,
): ViewModel() {

    val recipe: MutableState<Recipe?> = mutableStateOf(null)
    val loading = mutableStateOf(false)
    val onLoad: MutableState<Boolean> = mutableStateOf(false)
    val dialogQueue = DialogQueue()

    init {
        // restore if process dies
        state.get<Int>(STATE_KEY_RECIPE)?.let { recipeId ->
            onTriggerEvent(GetRecipeEvent(recipeId))
        }
    }

    fun onTriggerEvent(event: RecipeEvent){
        viewModelScope.launch {
            try {
                when(event){
                    is GetRecipeEvent -> {
                        if (recipe.value == null){
                            getRecipe(event.id)
                        }
                    }

                }

            }catch (e: Exception){
                Log.e(TAG, "onTriggerEvent: Exception: ${e}, ${e.cause}")
            }
        }
    }

    private fun getRecipe(id: Int){
        getRecipe.execute(
            recipeId = id,
            token = token,
            isNetworkAvailable = myConnectivityManager.isNetworkAvailable.value
        ).onEach { dataState ->
            loading.value = dataState.loading

            dataState.data?.let { data ->
                recipe.value = data
                state.set(STATE_KEY_RECIPE, data.id)
            }

            dataState.error?.let { error ->
                Log.e(TAG, "getRecipe: ${error}")
                dialogQueue.appendErrorMessage("Error", error)
            }
        }.launchIn(viewModelScope)
    }

}