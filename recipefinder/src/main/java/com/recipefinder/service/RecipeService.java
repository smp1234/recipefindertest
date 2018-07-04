package com.recipefinder.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.nio.file.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.recipefinder.dao.RecipeDao;
import com.recipefinder.dao.UndetectedItemDao;
import com.recipefinder.dao.UserDao;
import com.recipefinder.model.UndetectedItem;
import com.recipefinder.model.UnknownEntry;
import com.recipefinder.model.User;
@Service
public class RecipeService {
	@Autowired
	private RecipeDao recipeDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private UndetectedItemDao undetectedItemDao;
	@Autowired
	private UndetectedItem item;
	
	private static int userId = 0;
	
	public int getUserId(String emailId) {
		User user = userDao.getUserByEmailId(emailId);
		if(user == null) {
			userId += 1;			
			userDao.addUser(emailId, userId);
			return userId;
		}
		return user.getUserId();	
	}
	
	public String getRecipe(MultipartFile multipartImage, int userId) {
		// Receive image
		File image = new File("./images/"+multipartImage.getOriginalFilename());

		boolean op = false;
		try {
			image.createNewFile();
			FileOutputStream outputStream = new FileOutputStream(image);
			outputStream.write(multipartImage.getBytes());
			outputStream.close();
			op = true;

		} catch (Exception e) {			
			e.printStackTrace();
		}
		if (op == false)
			return "Error in creating file...";
		else {
			String recipeName = predictRecipe(image.getPath());
			String response;
			if(recipeName == null || recipeName.isEmpty() || recipeName.equals("Unable to detect item. Please try again...")) {
				response = "Unable to detect item. Please try again!";
				if(recipeName.equals("Unable to detect item. Please try again...")) {
					boolean status = handleUndetectedImage(image,userId);
					if(status == true)
						return "Unable to detect item. We are asking other user to comment over it.";
					else
						return "Unable to detect item. Error while asking the other user.";
				}
								
				return response;
			}
			response = getDBRecipe(recipeName);
			
			return response;
		}		
	}

	public String predictRecipe(String fileName) {

		// call prediction model to get recipe name
		String result = "";		
		try {
			String image = fileName;
			String pythonPath = "ML/ml.py";			
			
			String[] command = new String[] {"C:\\Users\\spatel\\AppData\\Local\\Programs\\Python\\Python36\\python.exe", pythonPath, image};
			try {
				Process process = Runtime.getRuntime().exec(command);
				BufferedReader stream = new BufferedReader(new InputStreamReader(process.getInputStream()));
				result = stream.readLine();
				System.out.println(result);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (Exception e) {
			System.out.println(e);
		} 
		return result;
	}

	public String getDBRecipe(String recipeName) {

		// calls DAO layer using recipeName
		String data = null;
		try {
			data = recipeDao.getRecipeByName(recipeName);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}		
	
	public boolean handleUndetectedImage(File image, int userId) {
		boolean status = false;		
		String newPath = "./undetected/" + userId +"_" + image.getName();
		try {
			Path path = Files.move(Paths.get(image.getPath()), Paths.get(newPath));
			undetectedItemDao.addItem(path.getFileName().toString(), userDao.getUserByUserId(userId));
		} catch (Exception e) {
			status = false;
			e.printStackTrace();
		}
		return status;
	}
	
	public boolean handleVote(String fileName, String vote) {
		boolean status = false;
		try {
			item = undetectedItemDao.getItemByName(fileName);
			Iterator<UnknownEntry> iterator = item.getGuessedItems().iterator();
			while (iterator.hasNext()) {
				UnknownEntry temp = iterator.next();
				if(temp.getName().equals(vote)) {
					temp.setVotes(temp.getVotes() + 1);
					if(temp.getVotes() >= 3) {
						handleDetection();
					}	
					return true;
				}
			}
			item.getGuessedItems().offer(new UnknownEntry(vote, 1));
			undetectedItemDao.updateItem(item);
			status = true;
		} catch (Exception e) {
			e.printStackTrace();
			status = false;
		}
		return status;
	}
	
	public boolean handleDetection() {
		// TODO: implement logic for reaching vote threshold for an undetected item
		return false;
	}
	
	public boolean checkForExpiry() {
		// TODO: implement logic to remove expired entries from DB and folders 
		return false;
	}
}