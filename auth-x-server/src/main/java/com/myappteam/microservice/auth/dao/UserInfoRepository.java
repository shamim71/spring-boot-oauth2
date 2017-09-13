package com.myappteam.microservice.auth.dao;

import org.springframework.data.jpa.repository.JpaRepository;



public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

	UserInfo findByEmail(String email);
}
