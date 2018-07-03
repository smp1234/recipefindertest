package com.recipefinder.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.recipefinder.model.Recipe;


public interface RecipeRepository extends MongoRepository<Recipe, String> {
		
	public Recipe findByName(String name);

}

