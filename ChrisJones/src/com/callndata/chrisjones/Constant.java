package com.callndata.chrisjones;

import com.paypal.android.sdk.payments.PaymentActivity;

public class Constant {

	public static String num = "";
	public static String userType = "";
	public static String ServiceType = "";
	public static String fullName = "";
	public static String CustomerID = "";
	public static String emailID = "";
	public static String servicesEndDate = "";
	public static String parentCompanyDetailsID = "";
	
	public static String uname = "";
	public static String addr = "";
	public static String areaName = "";
	public static String custId = "";
	public static String zipCode = "";
	public static String phoneNo = "";
	public static String mailId = "";
	public static String pwd = "";
	public static String ECName1 = "";
	public static String ECContactNo1 = "";
	public static String ECRelationship1 = "";
	public static String ECName2 = "";
	public static String ECContactNo2 = "";
	public static String ECRelationship2 = "";
	public static String anySpecialConditions = "";
	public static String serviceFee = "";
	
	// Can be NO_NETWORK for OFFLINE, SANDBOX for TESTING and LIVE for PRODUCTION
	//public static final String CONFIG_ENVIRONMENT = PaymentActivity.ENVIRONMENT_NO_NETWORK;
	public static final String CONFIG_ENVIRONMENT = PaymentActivity.ENVIRONMENT_LIVE;
	// note that these credentials will differ between live & sandbox environments.
//	public static final String CONFIG_CLIENT_ID = "AS7Hqv-Szk2rsrT9ZNDzDXaRxoUSOmoP361SQOqdzjVGaahtL0bcfxiGvurqdaXZ4eY8sWCQqn603rMt"; // testing
	
	public static final String CONFIG_CLIENT_ID = "AZQrXlRseJGDRwWpUXUpn4hZCP0l8erufNreMO3KJ7vzAQvNvlD88wGaHuVLyhNtikjMPV3gbNr_diKh";// original
	
	// when testing in sandbox, this is likely the -facilitator email address.
//	public static final String CONFIG_RECEIVER_EMAIL = "chrisjones@chrisjonessecurity.com.au"; //original
	public static final String CONFIG_RECEIVER_EMAIL = "payments@cjsecurityapp.com.au"; //shital
	public static final String PROGRESS_MESSAGE = "Loading CJ Security...";

}
