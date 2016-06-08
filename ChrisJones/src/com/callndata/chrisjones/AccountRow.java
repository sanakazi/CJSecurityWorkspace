package com.callndata.chrisjones;

public class AccountRow {

	String num;
	String userType;
	String ServiceType;
	String fullName;
	String CustomerID;
	String emailID;
	String servicesEndDate;

	public AccountRow(String num, String userType, String serviceType,
			String fullName, String customerID, String emailID,
			String servicesEndDate) {
		super();
		this.num = num;
		this.userType = userType;
		ServiceType = serviceType;
		this.fullName = fullName;
		CustomerID = customerID;
		this.emailID = emailID;
		this.servicesEndDate = servicesEndDate;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getServiceType() {
		return ServiceType;
	}

	public void setServiceType(String serviceType) {
		ServiceType = serviceType;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getCustomerID() {
		return CustomerID;
	}

	public void setCustomerID(String customerID) {
		CustomerID = customerID;
	}

	public String getEmailID() {
		return emailID;
	}

	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}

	public String getServicesEndDate() {
		return servicesEndDate;
	}

	public void setServicesEndDate(String servicesEndDate) {
		this.servicesEndDate = servicesEndDate;
	}

}
