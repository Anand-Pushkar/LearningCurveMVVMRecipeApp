package com.example.learningcurvemvvmrecipeapp.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.example.learningcurvemvvmrecipeapp.domain.model.Recipe
import com.example.learningcurvemvvmrecipeapp.util.DEFAULT_RECIPE_IMAGE
import com.example.learningcurvemvvmrecipeapp.util.loadPicture

const val IMAGE_HEIGHT = 260

@Composable
fun RecipeView(
    recipe: Recipe,
){
    ScrollableColumn(
        modifier = Modifier.fillMaxWidth()
    ) {

        /**
         * TOP RECIPE IMAGE
         */
        recipe.featuredImage?.let { url ->
            // load image from url into a variable
            val image = loadPicture(
                url = url,
                defaultImage = DEFAULT_RECIPE_IMAGE
            ).value
            // check if the immage loaded is null or not
            image?.let { img ->
                // put the image in Image composable
                Image(
                    contentDescription = null,
                    bitmap = img.asImageBitmap(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .preferredHeight(IMAGE_HEIGHT.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }

        /**
         * RECIPE DETAILS
         */
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            recipe.title?.let { title ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    Text(
                        text = title,
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .wrapContentWidth(Alignment.Start),
                        style = MaterialTheme.typography.h3
                    )
                    val rating = recipe.rating.toString()
                    Text(
                        text = rating,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.End)
                            .align(Alignment.CenterVertically),
                        style = MaterialTheme.typography.h5
                    )
                }
            }
            recipe.publisher?.let { publisher ->
                val updated = recipe.dateUpdated
                Text(
                    text = updated?.let { "Updated on $updated by $publisher" } ?: "By $publisher",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    style = MaterialTheme.typography.caption
                )
            }
            for(ingredient in recipe.ingredients){
                Text(
                    text = ingredient,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    style = MaterialTheme.typography.body1
                )
            }

        }


    }
}