package com.recipefinder.dao;

import java.util.List;
import java.util.HashMap;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.recipefinder.model.UndetectedItem;
import com.recipefinder.model.User;
@Repository
public class UndetectedItemDao {
	
	@Autowired
	private UndetectedItemRepository undetectedItemRepository;
	
	public boolean addItem(String fileName, User creator) {
		boolean status = false;
		try {
			undetectedItemRepository.save(new UndetectedItem(fileName, creator));
			status = true;
		} catch (Exception e) {
			status = false;
		}
		return status;	
	}
	
	public UndetectedItem getItemByName(String fileName) {
		UndetectedItem item = null;
		try {
			item = undetectedItemRepository.findUndetectedItemByFileName(fileName);			
		}catch (Exception e) {
			e.printStackTrace();			
		}
		return item;
	}
	
	public boolean updateItem(UndetectedItem item) {
		boolean status = false;
		try {			
			undetectedItemRepository.save(item);
			status = true;
		} catch (Exception e) {
			e.printStackTrace();
			status = false;
		}
		return status;
	}
	
	public boolean deleteItem(String fileName) {
		boolean status = false;
		try {
			undetectedItemRepository.deleteByFileName(fileName);
			status = true;
		} catch (Exception e) {
			e.printStackTrace();
			status = false;
		}
		return status;
	}
	
	public HashMap<String, Integer> getAllUndetectedItems(){
		HashMap<String, Integer> list = new HashMap<>();
		Iterator<UndetectedItem> iterator = undetectedItemRepository.findAll().iterator();
		while(iterator.hasNext()) {
			UndetectedItem temp = iterator.next();
			list.put(temp.getFileName(), temp.getCreator().getUserId());
		}
		return list;
	}
	
	public List<UndetectedItem> getItemsByCreator(User creator){
		List<UndetectedItem> list = undetectedItemRepository.findAllByCreator(creator);
		return list;
	}
}
