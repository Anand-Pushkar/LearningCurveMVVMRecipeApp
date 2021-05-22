package com.example.learningcurvemvvmrecipeapp.presentation.components

import androidx.compose.animation.transition
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.WithConstraints
import androidx.compose.ui.platform.AmbientDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.learningcurvemvvmrecipeapp.presentation.components.AnimationDefinitions.ShimmerAnimationDefinitions
import com.example.learningcurvemvvmrecipeapp.presentation.components.AnimationDefinitions.ShimmerAnimationDefinitions.ShimmerAnimationState.END
import com.example.learningcurvemvvmrecipeapp.presentation.components.AnimationDefinitions.ShimmerAnimationDefinitions.ShimmerAnimationState.START

@Composable
fun LoadingRecipeListShimmer(
    imageHeight: Dp,
    padding: Dp = 16.dp
){
    WithConstraints() {

        val cardWidthPx = with(AmbientDensity.current){
            ((maxWidth - (padding*2)).toPx())
        }
        val cardHeightPx = with(AmbientDensity.current){
            ((imageHeight - padding).toPx())
        }

        val cardAnimationDefinitions = remember {
            ShimmerAnimationDefinitions(
                widthPx = cardWidthPx,
                heightPx = cardHeightPx,
            )
        }

        val cardShimmerTranslateAnim = transition(
            definition = cardAnimationDefinitions.shimmerTransitionDefinition,
            initState = START,
            toState = END
        )

        val colors = listOf(
            Color.LightGray.copy(alpha = 0.9f),
            Color.LightGray.copy(alpha = 0.2f),
            Color.LightGray.copy(alpha = 0.9f)
        )

        val xCardShimmer =
            cardShimmerTranslateAnim[cardAnimationDefinitions.xShimmerPropKey]
        val yCardShimmer =
            cardShimmerTranslateAnim[cardAnimationDefinitions.yShimmerPropKey]

        ScrollableColumn {
            repeat(5){
                ShimmerRecipeCardItem(
                    colors = colors,
                    cardHeight = imageHeight,
                    xShimmer = xCardShimmer,
                    yShimmer = yCardShimmer,
                    padding = padding,
                    gradientWidth = cardAnimationDefinitions.gradientWidth
                )
            }
        }
    }
}