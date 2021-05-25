package com.example.learningcurvemvvmrecipeapp.presentation.ui.recipe_list

sealed class RecipeListEvent {

    // use class or data class identifier instead of object
    // if there are some arguments

    object NewSearchEvent: RecipeListEvent()
    object NextPageEvent: RecipeListEvent()

    // restore after process death
    object RestoreStateEvent: RecipeListEvent()
}