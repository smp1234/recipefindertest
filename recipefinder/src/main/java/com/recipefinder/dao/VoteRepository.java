package com.recipefinder.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.recipefinder.model.User;
import com.recipefinder.model.Vote;

public interface VoteRepository extends MongoRepository<Vote, String> {
	public Vote findByVoter(User voter);
}
