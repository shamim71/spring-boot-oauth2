package com.myappteam.microservice.auth.service;

import com.myappteam.microservice.auth.dto.UserInfoDto;

public interface UserInfoService {

	UserInfoDto loadUserByEmail(String email);
	
}
