package com.example.learningcurvemvvmrecipeapp.domain.model

data class Recipe(
    val id: Int, // should not be null
    val title: String,
    val publisher: String,
    val featuredImage: String,
    val rating: Int,
    val sourceUrl: String,
    val ingredients: List<String> = listOf(),
    val dateAdded: String,
    val dateUpdated: String,
)
