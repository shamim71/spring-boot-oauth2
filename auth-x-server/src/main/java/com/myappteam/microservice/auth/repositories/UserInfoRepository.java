package com.myappteam.microservice.auth.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myappteam.microservice.auth.dao.UserInfo;



public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

	Optional<UserInfo> findByEmail(String email);
	
	
}
