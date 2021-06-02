package com.example.learningcurvemvvmrecipeapp.cache.model

import com.example.learningcurvemvvmrecipeapp.domain.model.Recipe
import com.example.learningcurvemvvmrecipeapp.domain.util.DomainMapper
import com.example.learningcurvemvvmrecipeapp.util.DateUtils

class RecipeEntityMapper : DomainMapper<RecipeEntity, Recipe>{

    override fun mapToDomainModel(model: RecipeEntity): Recipe {
        return Recipe(
            id = model.id,
            title = model.title,
            publisher = model.publisher,
            featuredImage = model.featuredImage,
            rating = model.rating,
            sourceUrl = model.sourceUrl,
            ingredients = convertIngredientsToList(model.ingredients),
            dateAdded = DateUtils.longToDate(model.dateAdded),
            dateUpdated = DateUtils.longToDate(model.dateUpdated)
        )
    }

    override fun mapFromDomainModel(domainModel: Recipe): RecipeEntity {
        return RecipeEntity(
            id = domainModel.id,
            title = domainModel.title,
            publisher = domainModel.publisher,
            featuredImage = domainModel.featuredImage,
            rating = domainModel.rating,
            sourceUrl = domainModel.sourceUrl,
            ingredients = convertIngredientListToString(domainModel.ingredients),
            dateAdded = DateUtils.dateToLong(domainModel.dateAdded),
            dateUpdated = DateUtils.dateToLong(domainModel.dateUpdated),
            dateCached = DateUtils.dateToLong(DateUtils.createTimestamp())
        )
    }

    // ["Carrot", "Potato", "Chicken", ...] -> "Carrot, Potato, Chicken, ..."
    private fun convertIngredientListToString(ingredients: List<String>): String{
        val ingredientsString = StringBuilder()
        for (ingredient in ingredients){
            ingredientsString.append("$ingredient,")
        }
        return ingredientsString.toString()
    }

    // "Carrot, Potato, Chicken, ..." -> ["Carrot", "Potato", "Chicken", ...]
    private fun convertIngredientsToList(ingredientsString: String?): List<String>{
        val list: ArrayList<String> = ArrayList()
        ingredientsString?.let { ingredientsString ->
            for(ingredient in ingredientsString.split(",")){
                list.add(ingredient)
            }
        }
        return list
    }

    fun toDomainList(initial: List<RecipeEntity>): List<Recipe>{
        return initial.map { mapToDomainModel(it) }
    }

    fun fromDomainList(initial: List<Recipe>): List<RecipeEntity>{
        return initial.map { mapFromDomainModel(it) }
    }

}