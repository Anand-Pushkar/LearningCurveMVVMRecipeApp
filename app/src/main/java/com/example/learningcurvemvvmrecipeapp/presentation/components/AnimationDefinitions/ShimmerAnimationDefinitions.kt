package com.example.learningcurvemvvmrecipeapp.presentation.components.AnimationDefinitions

import androidx.compose.animation.core.*
import com.example.learningcurvemvvmrecipeapp.presentation.components.AnimationDefinitions.ShimmerAnimationDefinitions.ShimmerAnimationState.END
import com.example.learningcurvemvvmrecipeapp.presentation.components.AnimationDefinitions.ShimmerAnimationDefinitions.ShimmerAnimationState.START

class ShimmerAnimationDefinitions(
    private val widthPx: Float,
    private val heightPx: Float,
){
    var gradientWidth: Float

    init {
        gradientWidth = 0.2f * heightPx
    }

    enum class ShimmerAnimationState{
        START, END
    }

    val xShimmerPropKey = FloatPropKey("xShimmer")
    val yShimmerPropKey = FloatPropKey("yShimmer")

    val shimmerTransitionDefinition = transitionDefinition<ShimmerAnimationState> {

        state(START){
            this[xShimmerPropKey] = 0f
            this[yShimmerPropKey] = 0f
        }
        state(END){
            this[xShimmerPropKey] = widthPx + gradientWidth
            this[yShimmerPropKey] = heightPx + gradientWidth
        }

        transition(START, END){
            xShimmerPropKey using infiniteRepeatable(
                animation = tween(
                    durationMillis = 1300,
                    delayMillis = 300,
                    easing = LinearEasing
                )
            )
            yShimmerPropKey using infiniteRepeatable(
                animation = tween(
                    durationMillis = 1300,
                    delayMillis = 300,
                    easing = LinearEasing
                )
            )
        }
    }

}