package com.myappteam.microservice.auth.repositories;

import java.util.Optional;

import org.springframework.data.repository.Repository;

import com.myappteam.microservice.auth.dao.CustomerInfo;

public interface CustomerInfoRepo extends Repository<CustomerInfo, Long> {
	
	Optional<CustomerInfo> findByDomain(String domain);
	
	Optional<CustomerInfo> findByCode(String code);
	
	void save(CustomerInfo customer);
}