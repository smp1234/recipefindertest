package com.recipefinder.model;

import java.util.HashMap;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Document(collection="Recipe")
public class Recipe {
	@Id
	private String Id;
	private String name;
	private HashMap<String, String> ingredients;
	private String steps;
	private String origin;
	
	public String getName() {
		return name;
	}
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public HashMap<String, String> getIngredients() {
		return ingredients;
	}
	public void setIngredients(HashMap<String, String> ingredients) {
		this.ingredients = ingredients;
	}
	public String getSteps() {
		return steps;
	}
	public void setSteps(String steps) {
		this.steps = steps;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public Recipe() {
		
	}
	public Recipe(String name, HashMap<String, String> ingredients, String steps, String origin) {
		
		this.name = name;
		this.ingredients = ingredients;
		this.steps = steps;
		this.origin = origin;
	}
	@Override
	public String toString() {
		return "in to string method";
	}
	

}
