package com.example.learningcurvemvvmrecipeapp.domain.model

import java.util.*

// core business model, every field is very important
data class Recipe(
    val id: Int, // should not be null
    val title: String,
    val publisher: String,
    val featuredImage: String,
    val rating: Int,
    val sourceUrl: String,
    val ingredients: List<String> = listOf(),
    val dateAdded: Date,
    val dateUpdated: Date,
)
