package com.recipefinder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.recipefinder.dao.RecipeRepository;
import com.recipefinder.model.Recipe;

@SpringBootApplication
public class RecipefinderApplication implements CommandLineRunner{
	@Autowired
	private RecipeRepository recipeRepository;
	public static void main(String[] args) {
		SpringApplication.run(RecipefinderApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		for(Recipe r:recipeRepository.findAll())
			System.out.print(r.toString());
		System.out.println(recipeRepository.getClass());
		System.out.println(recipeRepository.count());
	}
	
	
	
}
