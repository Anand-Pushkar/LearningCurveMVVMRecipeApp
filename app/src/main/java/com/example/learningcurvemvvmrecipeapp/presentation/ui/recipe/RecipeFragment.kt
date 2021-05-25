package com.example.learningcurvemvvmrecipeapp.presentation.ui.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.learningcurvemvvmrecipeapp.presentation.ui.recipe_list.RecipeListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipeFragment : Fragment() {

    private var recipeId = mutableStateOf<Int?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainScope().launch {
            delay(1000)
            arguments?.getInt("recipeId")?.let { rId ->
                recipeId.value = rId
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = recipeId?.let {
                            "Selected recipe id: ${recipeId.value}"
                        }?: "Loading...",
                        style = TextStyle(fontSize = TextUnit.Sp(21))
                    )
                }
            }
        }
    }
}