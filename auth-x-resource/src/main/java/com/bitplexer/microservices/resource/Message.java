package com.bitplexer.microservices.resource;

public class Message {
	private String id;
	private String resource;
	private String user;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getResource() {
		return resource;
	}
	public void setResource(String resource) {
		this.resource = resource;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}

}
