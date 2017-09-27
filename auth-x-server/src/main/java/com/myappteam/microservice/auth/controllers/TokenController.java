package com.myappteam.microservice.auth.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.myappteam.microservice.auth.services.TokenBlackListService;
import com.myappteam.microservice.auth.services.TokenBlackListService.TokenNotFoundException;

@Controller
public class TokenController {


	    @Autowired
	    TokenBlackListService blackListService;


	    @RequestMapping(method = RequestMethod.POST, value = "/tokens/revokeRefreshToken/{tokenId:.*}")
	    @ResponseBody
	    public String revokeRefreshToken(@PathVariable String tokenId) {
	        
	    	try {
				blackListService.addToBlackList(tokenId);
			} catch (TokenNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	        return tokenId;
	    }
}
