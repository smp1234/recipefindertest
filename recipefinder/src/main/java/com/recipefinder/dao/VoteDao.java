package com.recipefinder.dao;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.recipefinder.model.UndetectedItem;
import com.recipefinder.model.User;
import com.recipefinder.model.Vote;

@Repository
public class VoteDao {
	
	@Autowired
	private VoteRepository voteRepository;
	
	public boolean addVote(User user, UndetectedItem item) {
		boolean status = false;
		try {						
			voteRepository.save(new Vote(user, item));
			status = true;
		} catch (Exception e) {
			e.printStackTrace();
			status = false;
		}
		return status;
	}
	
	public boolean updateVote(User user, UndetectedItem item) {
		boolean status = false;
		try {
			Vote vote = voteRepository.findByVoter(user);
			vote.getItems().add(item);
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
	
	public HashSet<UndetectedItem> getVotedItems(User user){
		Vote vote = voteRepository.findByVoter(user);
		if(vote == null)
			return null;
		return vote.getItems();
	}
}
