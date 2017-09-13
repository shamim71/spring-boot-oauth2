package com.myappteam.microservice.auth.utility;

import org.apache.commons.codec.binary.Base64;

import com.myappteam.microservice.auth.dao.UserInfo;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		System.out.println("-------------");
		String email = "admin@vcomlp.com";
		String pass  ="123";
		
		UserInfo userInfo = new UserInfo();
		/** 32 bin random number */
		byte[] sharedSecret = CryptographicUtility.generateSecureRandom();

		byte[] hashedSharedSecret = CryptographicUtility.encode(pass.toCharArray(), sharedSecret);


		String encodedSharedSecreet = Base64
				.encodeBase64String(hashedSharedSecret);
		String encodedSalt = Base64.encodeBase64String(sharedSecret);

		userInfo.setSalt(encodedSalt);
		userInfo.setHashedPassword(encodedSharedSecreet);
		System.out.println(userInfo.getSalt());
		System.out.println(userInfo.getHashedPassword());
		String stml = "UPDATE user SET hashed_password='"+ userInfo.getHashedPassword()+"', salt='"+userInfo.getSalt()+"' WHERE email ='"+ email +"' and id>=0;";
		System.out.println(stml);
		
	}

}
