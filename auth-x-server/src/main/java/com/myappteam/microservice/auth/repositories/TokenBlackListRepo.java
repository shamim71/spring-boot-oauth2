package com.myappteam.microservice.auth.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.Repository;

import com.myappteam.microservice.auth.dao.TokenBlackList;

public interface TokenBlackListRepo extends Repository<TokenBlackList, Long> {
	
	Optional<TokenBlackList> findByJti(String jti);

	List<TokenBlackList> queryAllByUserIdAndIsBlackListedTrue(Long userId);

	void save(TokenBlackList tokenBlackList);

	List<TokenBlackList> deleteAllByUserIdAndExpiresBefore(Long userId, Long date);
}