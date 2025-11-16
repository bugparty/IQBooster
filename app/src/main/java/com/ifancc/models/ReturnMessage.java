package com.ifancc.models;


public class ReturnMessage {
	private Integer status_code = null;
	private String message = null;
	private Integer id = null;
	private String token = null;
	private Integer userId = null;
	
	public ReturnMessage(Integer  statusCode, String message, Integer id,
			String token, Integer userId) {
		super();
		this.status_code = statusCode;
		this.message = message;
		this.id = id;
		this.token = token;
		this.userId = userId;
	}
	
	
	public Integer getStatus_code() {
		return status_code;
	}


	public void setStatus_code(Integer status_code) {
		this.status_code = status_code;
	}


	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	@Override
	public String toString() {
		return "ReturnMessage [statusCode=" + status_code + ", message="
				+ message + ", id=" + id + ", token=" + token + ", userId="
				+ userId + "]";
	}

	
}
