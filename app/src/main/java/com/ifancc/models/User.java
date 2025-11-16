package com.ifancc.models;

public class User {
	private String userName = "";
	private String password = "";
	private String mailbox = "";
	private String sex = "";
	
	
	public User(String userName,String password,String mailbox,String sex) {
		this.mailbox = mailbox;
		this.userName = userName;
		this.password = password;
		this.sex = sex;
	}
	
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMailbox() {
		return mailbox;
	}

	public void setMailbox(String mailbox) {
		this.mailbox = mailbox;
	}

	@Override
	public String toString() {
		return "User [userName=" + userName + ", password=" + password
				+ ", mailbox=" + mailbox + "]";
	}
}
