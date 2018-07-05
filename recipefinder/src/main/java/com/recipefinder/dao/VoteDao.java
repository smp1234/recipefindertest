package com.recipefinder.dao;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.recipefinder.model.UndetectedItem;
import com.recipefinder.model.Vote;

@Repository
public class VoteDao {
	
	@Autowired
	private VoteRepository voteRepository;
	
	public boolean addVote(int uid, String fileName) {
		boolean status = false;
		try {			
			UndetectedItem item = new UndetectedItemDao().getItemByName(fileName);
			voteRepository.save(new Vote(new UserDao().getUserByUserId(uid), item));
			status = true;
		} catch (Exception e) {
			e.printStackTrace();
			status = false;
		}
		return status;
	}
	
	public boolean updateVote(int uid, String fileName) {
		boolean status = false;
		try {
			Vote vote = voteRepository.findByVoter(new UserDao().getUserByUserId(uid));
			vote.getItems().add(new UndetectedItemDao().getItemByName(fileName));
			voteRepository.save(vote);
			status = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return status;
	}
	
	public boolean checkUserExistance(int uid) {
		boolean exist = false;
		try {
			List<Vote> list = voteRepository.findAll();
			Iterator<Vote> iterator = list.iterator();
			while(iterator.hasNext()) {
				if(iterator.next().getVoter().getUserId() == uid)
					exist = true;
			}
		}catch (Exception e) {
			e.printStackTrace();
			exist = false;
		}
		
		return exist;
	}
}
