package com.recipefinder.service;

import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.recipefinder.dao.NotificationDao;
import com.recipefinder.dao.RecipeDao;
import com.recipefinder.dao.UndetectedItemDao;
import com.recipefinder.dao.UserDao;
import com.recipefinder.dao.VoteDao;
import com.recipefinder.model.Notification;
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
	@Autowired
	private VoteDao voteDao;
	@Autowired
	private NotificationDao notificationDao;
	@Autowired
	private Notification notification;
	
	
	private static int userId = 0;
	private static int globalNotificationCount = 0;
	
	public int getUserId(String emailId) {
		User user = userDao.getUserByEmailId(emailId);
		if(user == null) {
			userId += 1;			
			userDao.addUser(emailId, userId);			
			notificationDao.createEntry(userDao.getUserByUserId(userId), globalNotificationCount);
			return userId;
		}
		return user.getUserId();	
	}
	//public String getRecipe(File multipartImage, int userId) {
	public String getRecipe(MultipartFile multipartImage, int userId) {
		// Receive image
		 File image = new File("./images/"+multipartImage.getOriginalFilename());

//		File image = null;
		boolean op = false;
		try {
			image.createNewFile();
			FileOutputStream outputStream = new FileOutputStream(image);
			outputStream.write(multipartImage.getBytes());
			
			outputStream.close();
//			Files.copy(Paths.get(multipartImage.getPath()), Paths.get("./images/"+multipartImage.getName()), StandardCopyOption.REPLACE_EXISTING);
//			image = new File("./images/" + multipartImage.getName());
			op = true;

		} catch (Exception e) {			
			e.printStackTrace();
		}
		if (op == false)
			return "Error in creating file...";
		else {
			String recipeName = predictRecipe(image.getPath());
//			String recipeName = "Unable to detect item. Please try again...";
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
			try {
				Files.delete(Paths.get(image.getPath()));
			} catch (IOException e) {				
				e.printStackTrace();
			}
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
			e.printStackTrace();
		}
		return data;
	}		
	
	public boolean handleUndetectedImage(File image, int userId) {
		boolean status = false;		
		String newPath = "./undetected/" + userId +"_" + image.getName();
		try {
			Files.move(Paths.get(image.getPath()), Paths.get(newPath),StandardCopyOption.REPLACE_EXISTING);
			status = undetectedItemDao.addItem(image.getName(), userDao.getUserByUserId(userId));
			globalNotificationCount += 1;
			sendNotification(userId);
		} catch (Exception e) {
			status = false;
			e.printStackTrace();
		}
		return status;
	}
	
	public boolean handleVote(String fileName, String vote, int userId) {
		boolean status = false;
		try {
			item = undetectedItemDao.getItemByName(fileName);
			Iterator<UnknownEntry> iterator = item.getGuessedItems().iterator();
			while (iterator.hasNext()) {
				UnknownEntry temp = iterator.next();
				if(temp.getName().equals(vote)) {
					temp.setVotes(temp.getVotes() + 1);
					if(temp.getVotes() >= 3) {
						handleDetection(item.getCreator().getUserId() + "_" + fileName, vote, fileName);
					}
					else {
						undetectedItemDao.updateItem(item);
					}
					if(voteDao.checkUserExistance(userId) == false) {
						status = voteDao.addVote(userDao.getUserByUserId(userId), undetectedItemDao.getItemByName(fileName));
					}
					else {
						status = voteDao.updateVote(userDao.getUserByUserId(userId), undetectedItemDao.getItemByName(fileName));
					}
					return true;
				}
			}
			item.getGuessedItems().offer(new UnknownEntry(vote, 1));
			undetectedItemDao.updateItem(item);
			if(voteDao.checkUserExistance(userId) == false) {
				status = voteDao.addVote(userDao.getUserByUserId(userId), undetectedItemDao.getItemByName(fileName));
			}
			else {
				status = voteDao.updateVote(userDao.getUserByUserId(userId), undetectedItemDao.getItemByName(fileName));
			}			
		} catch (Exception e) {
			e.printStackTrace();
			status = false;
		}
		return status;
	}
	
	public boolean handleDetection(String filePath, String result, String fileName) {
		
		boolean status = false;
		try {
			// Create a directory with name of result
			File dir = new File("./dataset/" + result);
			dir.mkdir();
			// Move file to new directory (If there exists a file with same name it will be replaced.)
			Files.move(Paths.get("./undetected/" + filePath), Paths.get("./dataset/" + result +"/" + filePath), StandardCopyOption.REPLACE_EXISTING);
			// Delete entry from database
			status = undetectedItemDao.deleteItem(fileName);
			
		} catch (Exception e) {
			e.printStackTrace();
			status = false;
		}
		return status;
	}
	
	public boolean checkForExpiry(String fileName) {
		boolean status = false;
		try {
			
			item = undetectedItemDao.getItemByName(fileName);
			long diff = Math.abs(new Date().getTime() - item.getEntryDate().getTime());
			long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
			if(days >= 3)
				status = undetectedItemDao.deleteItem(fileName);
			else
				status = true;
			
		} catch (Exception e) {
			e.printStackTrace();
			status = false;
		}			
		return status;
	}
	
	public HashMap<String, String> getAllUndetectedItems(){
		HashMap<String,String> result = new HashMap<>();
		HashMap<String, Integer> list = undetectedItemDao.getAllUndetectedItems();
		for(Entry<String, Integer> entry: list.entrySet()) {
			String path = "./undetected/" + entry.getValue() +"_" + entry.getKey();
			result.put(entry.getKey(),path);
		}
		return result;
	}
	
	public HashMap<String, String> getUndetectedItemsForUser(int uid){		
		HashMap<String, String> result = getAllUndetectedItems();
		HashSet<UndetectedItem> votedItems = voteDao.getVotedItems(userDao.getUserByUserId(uid));
		if(votedItems != null) {
			for(UndetectedItem undetectedItem: votedItems)
				result.remove(undetectedItem.getFileName());
		}
		List<UndetectedItem> itemsCreatedByUser = undetectedItemDao.getItemsByCreator(userDao.getUserByUserId(uid));
		if(itemsCreatedByUser != null) {
			for(UndetectedItem undetectedItem: itemsCreatedByUser)
				result.remove(undetectedItem.getFileName());
		}
		return result;
	}
	
	public String sendNotification(int userId) {
		notification = notificationDao.getNotificationEntry(userDao.getUserByUserId(userId));
		long result = (globalNotificationCount - notification.getNoOfNotifications());
		if(result > 0) {
			notification.setNoOfNotifications(globalNotificationCount);
			notificationDao.updateNotificationCount(notification);
		}
		return Long.toString(result);
	}
	
	public String Test() {
//		int userId = getUserId("spatel");
//		File file = new File("./test/fal.jpg");
//		getRecipe(file, userId);
//		getAllUndetectedItems();
//		handleVote(file.getName(), "falooda", 1);
		return "HI";
	}
		
}
