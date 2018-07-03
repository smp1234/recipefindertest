package com.recipefinder.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.recipefinder.model.Recipe;

@Repository
public class RecipeDao {
	
	@Autowired
	private RecipeRepository recipeRepository;	
	
	public String getRecipeByName(String recipeName) throws JsonProcessingException {
		
		Recipe recipe=recipeRepository.findByName(recipeName);
		
		ObjectMapper mapper = new ObjectMapper();
		

		//Object to JSON in String
		String jsonInString = mapper.writeValueAsString(recipe);
		
		//convert to json and return
		return jsonInString;
	}
		
}
