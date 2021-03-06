package com.example.learningcurvemvvmrecipeapp.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {

        item {

            /**
             * TOP RECIPE IMAGE
             */
            // load image from url into a variable
            val image = loadPicture(
                url = recipe.featuredImage,
                defaultImage = DEFAULT_RECIPE_IMAGE
            ).value
            // check if the image loaded is null or not
            image?.let { img ->
                // put the image in Image composable
                Image(
                    contentDescription = null,
                    bitmap = img.asImageBitmap(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IMAGE_HEIGHT.dp),
                    contentScale = ContentScale.Crop
                )
            }

            /**
             * RECIPE DETAILS
             */
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    Text(
                        text = recipe.title,
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
                val updated = recipe.dateUpdated
                Text(
                    text = "Updated on $updated by ${recipe.publisher}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    style = MaterialTheme.typography.caption
                )
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
}