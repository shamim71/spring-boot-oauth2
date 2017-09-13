package com.myappteam.microservice.auth.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.myappteam.microservice.auth.dao.UserInfo;
import com.myappteam.microservice.auth.dao.UserInfoRepository;
import com.myappteam.microservice.auth.dto.UserInfoDto;
import com.myappteam.microservice.auth.service.UserInfoService;


@Service
@Transactional
public class UserInfoServiceImpl implements UserInfoService {

	private static final Logger logger = LoggerFactory.getLogger(UserInfoServiceImpl.class);
	 
	@Autowired
	UserInfoRepository userRepository;
	

	@Override
	public UserInfoDto loadUserByEmail(String email) {
		UserInfo userInfo = userRepository.findByEmail(email);
		
		if(null == userInfo){
			logger.error("No User found with email address: "+ email);
		}
		return null;
	}

}
