package com.example.learningcurvemvvmrecipeapp.presentation.components

import androidx.compose.foundation.layout.ConstraintLayout
import androidx.compose.foundation.layout.ConstraintSet
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.WithConstraints
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

@Composable
fun CircularIndeterminateProgressBar(
    isDisplayed: Boolean
) {
    if (isDisplayed) {

        WithConstraints(
            modifier = Modifier.fillMaxSize()
        ) {
            val constraints = if(minWidth < 600.dp){ // porrait
                myDecoupledConstraints(0.3f)
            }
            else{ // landscape
                myDecoupledConstraints(0.7f)
            }

            ConstraintLayout(
                modifier = Modifier.fillMaxSize(),
                constraintSet = constraints
            ) {

                CircularProgressIndicator(
                    modifier = Modifier.layoutId("progressBar"),
                    color = MaterialTheme.colors.primary
                )

                val loading = "Loading..."
                Text(
                    text = loading,
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = TextUnit.Companion.Sp(15)
                    ),
                    modifier = Modifier.layoutId("text")
                )
            }
        }
    }
}

private fun myDecoupledConstraints(verticalBias: Float): ConstraintSet {
    return ConstraintSet {
        val guideline = createGuidelineFromTop(verticalBias)
        val progressBar = createRefFor("progressBar")
        val text = createRefFor("text")

        constrain(progressBar){
            top.linkTo(guideline)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }

        constrain(text){
            top.linkTo(progressBar.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }
    }
}