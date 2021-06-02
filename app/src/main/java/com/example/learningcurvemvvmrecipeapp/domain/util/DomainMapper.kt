package com.example.learningcurvemvvmrecipeapp.domain.util

/**
 * This interface holds the functions to map
 * the domain model Recipe class to and from
 * our RecipeEntity (Cache model), and RecipeDto (Network model).
 *
 * We will implement this interface in our DtoMapper and EntityMapper.
 */

interface DomainMapper <T, DomainModel>{

    fun mapToDomainModel(model: T): DomainModel

    fun mapFromDomainModel(domainModel: DomainModel) : T
}