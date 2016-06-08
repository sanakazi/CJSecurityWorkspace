package com.callndata.chrisjones;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class SignUp extends Activity {

	EditText customerId, name, mail, phone, street, password, zipcode, city;
	EditText emergencyName, emergencyContact, emergencyRelationship,
			emergencyNameSecondary, emergencyContactSecondary,
			emergencyRelationshipSecondary,specialCondition;
	Button btnSignUp;
	TextView txtTerms;
	
	public static String parentId="0", status = "", cityId="", longt, lat, userType="Home", serviceType="Free";
	
	RadioButton rbFree;
    RadioButton rbPaid;
	
	ArrayList<String> cities;
	HashMap<String, String> cityWithId;
	ArrayAdapter<String> cityAdapter;

	private ArrayList<NameValuePair> namevalue = new ArrayList<NameValuePair>();
	private ArrayList<NameValuePair> snamevalue = new ArrayList<NameValuePair>();
	
	Double latitude=0.0, longitude=0.0;
	LocationManager lm;
	
	private ProgressDialog pDialog;
	boolean netCheck = false;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sign_up);
		
		if (Build.VERSION.SDK_INT >= 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		
		pDialog = new ProgressDialog(SignUp.this);
		
		rbFree = (RadioButton) findViewById(R.id.serviceFree);
		rbPaid = (RadioButton) findViewById(R.id.servicePaid);
		
		customerId = (EditText) findViewById(R.id.edRCustomerId);
		name = (EditText) findViewById(R.id.edRName);
		mail = (EditText) findViewById(R.id.edRMail);
		phone = (EditText) findViewById(R.id.edRPhone);
		street = (EditText) findViewById(R.id.edRStreet);
		password = (EditText) findViewById(R.id.edRPassword);
		zipcode = (EditText) findViewById(R.id.edRZipcode);
		city = (EditText) findViewById(R.id.edRCity);
		txtTerms = (TextView) findViewById(R.id.txtRTerms);
		emergencyName = (EditText) findViewById(R.id.sign_up_emergency_contact_one_name);
		emergencyContact = (EditText) findViewById(R.id.sign_up_emergency_contact_one_contact);
		emergencyRelationship = (EditText) findViewById(R.id.sign_up_emergency_contact_one_relation);
		emergencyNameSecondary = (EditText) findViewById(R.id.sign_up_emergency_contact_two_name);
		emergencyContactSecondary = (EditText) findViewById(R.id.sign_up_emergency_contact_two_contact);
		emergencyRelationshipSecondary = (EditText) findViewById(R.id.sign_up_emergency_contact_two_relation);
		specialCondition = (EditText) findViewById(R.id.sign_up_special_condition);
		
		btnSignUp = (Button) findViewById(R.id.btnSignup);
		
		password.setTypeface(Typeface.DEFAULT);
		
		try
		{
			GPSTracker gps = new GPSTracker(SignUp.this);
			if(gps.canGetLocation())
			{ 
				latitude = gps.getLatitude();
				longitude = gps.getLongitude();
			}
			else
			{
				gps.showSettingsAlert();
			}
		}
		catch(Exception e)
		{
			latitude = 0.0;
			longitude = 0.0;
		}
		
//		cities = new ArrayList<String>();
//		cityWithId = new HashMap<String, String>();
//		cityAdapter = new ArrayAdapter<String>(SignUp.this, android.R.layout.simple_spinner_item, cities);
//		city.setAdapter(cityAdapter);
		
		try{
			netCheck = UserFunctions.isConnectionAvailable(SignUp.this);
		
//		if (netCheck == true) {
//			getCityList();
//		} else {
//			UserFunctions.dialogboxshow("Message", "Internet Connection not available.", SignUp.this);
//		}

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		btnSignUp.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				
				
				if (name.getText().toString().equals("")) {
					UserFunctions.dialogboxshow("Message","Please Enter Name", SignUp.this);
					return;
				}
				else if (street.getText().toString().equals("")) {
					UserFunctions.dialogboxshow("Message","Please Enter Street", SignUp.this);
					return;
				}
				else if (city.getText().toString().equals("")) {
					UserFunctions.dialogboxshow("Message","Please Enter City", SignUp.this);
					return;
				}
				else if (zipcode.getText().toString().equals("")) {
					UserFunctions.dialogboxshow("Message","Please Enter ZipCode", SignUp.this);
					return;
				}
				else if (phone.getText().toString().equals("")) {
					UserFunctions.dialogboxshow("Message","Please Enter Phone Number", SignUp.this);
					return;
				}
				else if (mail.getText().toString().equals("")) {
					UserFunctions.dialogboxshow("Message","Please Enter E-Mail", SignUp.this);
					return;
				}
				if (!isValidEmail(mail.getText().toString())) {
					UserFunctions.dialogboxshow("Message","Please Enter Valid Email-Id.", SignUp.this);
					return;
				}
				else if (password.getText().toString().equals("")) {
					UserFunctions.dialogboxshow("Message","Please Enter Password", SignUp.this);
					return;
				}
				else
				{
					netCheck = UserFunctions.isConnectionAvailable(SignUp.this);
					if (netCheck == true) {
						//register();
						openTermsActivity();
					} else {
						UserFunctions.dialogboxshow("Message", "Internet Connection not available.", SignUp.this);
					}
				}
			}
		});
		
		
		txtTerms.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent i = new Intent(SignUp.this, Terms.class);
				startActivity(i);
			}
		});
		
//		city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//
//			@Override
//			public void onItemSelected(AdapterView<?> arg0, View arg1,
//					int arg2, long arg3) {
//				// TODO Auto-generated method stub
//
//				((TextView) arg0.getChildAt(0)).setTextColor(Color.BLACK);
//
//				String cityName = arg0.getItemAtPosition(arg2).toString();
//				if(cityWithId.containsValue(cityName))
//				{
//					for (Entry<String, String> entry : cityWithId.entrySet()) {
//			            if (entry.getValue().equalsIgnoreCase(cityName)) {
//			            	cityId = entry.getKey();
//			            }
//			        }
//				}
//				
//			}
//
//			@Override
//			public void onNothingSelected(AdapterView<?> arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//		});

		
		getServiceFee();
		
	}
	
	private boolean isValidEmail(String email) {
		String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}
	
	public void getCityList() {
		
		namevalue.clear();
		
		new AsyncTask<String, String, String>() {

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				pDialog.setMessage(Constant.PROGRESS_MESSAGE);
				pDialog.setCancelable(false);
				pDialog.show();
				
				cities.clear();
				cityWithId.clear();
				cityAdapter.clear();
			}

			@Override
			protected String doInBackground(String... params) {

//				namevalue.add(new BasicNameValuePair("RequestType","CityList"));

				String json = UserFunctions.loadJson(
								"http://cjssecurity.com/RESTWS/cityList.asp",
								namevalue);

				JSONArray arr = null;
				try {
					arr = new JSONArray(json);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				cities.add("Select City");
				for (int i = 0; i < arr.length(); i++) {
					try {
						cities.add(arr.getJSONObject(i).getString("cityname"));
						cityWithId.put(arr.getJSONObject(i).getString("countryID"), 
								arr.getJSONObject(i).getString("cityname"));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				return null;
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if (pDialog.isShowing())
					pDialog.dismiss();
			
				cityAdapter.notifyDataSetChanged();
			}

		}.execute();

	}
	
	public void openTermsActivity(){
		
		Constant.custId = customerId.getText().toString();
		Constant.uname = name.getText().toString();
		Constant.addr = street.getText().toString();
		Constant.areaName = "a";
		Constant.zipCode = zipcode.getText().toString();
		Constant.phoneNo = phone.getText().toString();
		Constant.mailId = mail.getText().toString();
		Constant.pwd = password.getText().toString();
		
		Constant.ECName1 = emergencyName.getText().toString();
		Constant.ECContactNo1 = emergencyContact.getText().toString();
		Constant.ECRelationship1 = emergencyRelationship.getText().toString();
		Constant.ECName2 = emergencyNameSecondary.getText().toString();
		Constant.ECContactNo2 = emergencyContactSecondary.getText().toString();
		Constant.ECRelationship2 = emergencyRelationshipSecondary.getText().toString();
		Constant.anySpecialConditions = specialCondition.getText().toString();
		
		/*Intent i = new Intent(SignUp.this, Terms.class);
		i.putExtra("SignUp", true);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
		finish();*/
		
		register();
		
	}


	public void register() {
		
		snamevalue.clear();
		
		new AsyncTask<String, String, String>() {
	
			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				pDialog.setMessage("Relax for a while...");
				pDialog.setCancelable(false);
				pDialog.show();
			}
	
			@Override
			protected String doInBackground(String... params) {
	
	//			snamevalue.add(new BasicNameValuePair("RequestType","CityList"));
				
 				String custId = customerId.getText().toString();
				String uname = name.getText().toString();
				String addr = street.getText().toString();
				String areaName = "a";
				String zipCode = zipcode.getText().toString();
				String phoneNo = phone.getText().toString();
				String mailId = mail.getText().toString();
				String pwd = password.getText().toString();
				
				if(custId.equals(""))
				{
					custId="1";
				}
				
				try
				{
					longt = longitude.toString();
					lat = latitude.toString();
				}
				catch(Exception e)
				{
					longt = "0";
					lat = "0";
				}
				
				
				
				if(longt==null || longt.equals(""))
				{
					longt = "0";
				}
				if(lat==null || lat.equals(""))
				{
					lat = "0";
				}
				
				cityId="1";
				if(parentId.equals(""))
				{
					parentId = "0";
				}

				userType = "Home";
				
				if(rbFree.isChecked())
				{
					serviceType = "Free";
				}
				else
				{
					serviceType = "Paid";
				}
				
				/*String registrationUrl = "http://cjssecurity.com/RESTWS/register.asp?" + 
										 "parentCompanyDetailsID=" + parentId + "&userType=" + userType +
										 "&fullName="+ uname + "&CustomerID=" + custId + "&address=" + addr + "&areaName=" + areaName +
										 "&cityID=" + cityId + "&zipCode=" + zipCode + "&longitude=" + longt + 
										 "&latitude=" + lat + "&phoneNumbers=" + phoneNo + "&emailID=" + mailId +
										 "&upassword=" + pwd + "&ServiceType=Paid";
				
				String finalUrl = registrationUrl.replace(" ", "%20");
				
				String json = UserFunctions.loadJson(
								finalUrl,
								snamevalue);
				
				JSONArray arr = null;
				try {
					arr = new JSONArray(json);
					status = arr.getJSONObject(0).getString("status");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	
				if(status.equals("1"))
				{
					try {
						Constant.num = arr.getJSONObject(1).getString("num");
						Constant.userType = arr.getJSONObject(1).getString("userType");
						Constant.ServiceType = arr.getJSONObject(1).getString("ServiceType");
						Constant.fullName = arr.getJSONObject(1).getString("fullName");
						Constant.CustomerID = arr.getJSONObject(1).getString("CustomerID");
						Constant.emailID = arr.getJSONObject(1).getString("emailID");
						Constant.servicesEndDate = arr.getJSONObject(1).getString("servicesEndDate");
						Constant.parentCompanyDetailsID = "0";
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
	*/
				return null;
			}
	
			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if (pDialog.isShowing())
					pDialog.dismiss();
				
				Log.d("SignUp", "Usernam n pwrd is - "+Constant.uname+"-"+Constant.pwd);
				
				Intent i = new Intent(SignUp.this, Terms.class);
				i.putExtra("SignUp", true);
				i.putExtra("Register","Valid_values");
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
				finish();
			
				if(status.equals("1"))
				{
					
					/*SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(SignUp.this);
					Editor edt = sp.edit();
					edt.putString("num", Constant.num);
					edt.putString("userType", Constant.fullName);
					edt.putString("ServiceType", Constant.ServiceType);
					edt.putString("fullName", Constant.fullName);
					edt.putString("CustomerID", Constant.CustomerID);
					edt.putString("emailID", Constant.emailID);
					edt.putString("servicesEndDate", Constant.servicesEndDate);
					edt.putString("parentCompanyDetailsID", "0");
					edt.commit();*/
					
//					Toast.makeText(SignUp.this, "Registration Successful.", Toast.LENGTH_LONG).show();
					/*Intent i = new Intent(SignUp.this, Terms.class);
					i.putExtra("SignUp", true);
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);
					finish();*/
					
				}
				else
				{
					//Toast.makeText(SignUp.this, "Problem while Registration.", Toast.LENGTH_LONG).show();
				}
			}
	
		}.execute();
	
	}
	
	//to get service fee
	public void getServiceFee() {
		 
		snamevalue.clear();
		
		new AsyncTask<String, String, String>() {
	
			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();/*
				pDialog.setMessage(Constant.PROGRESS_MESSAGE);
				pDialog.setCancelable(false);
				pDialog.show();*/
			}
	
			@Override
			protected String doInBackground(String... params) {
				String serviceFeeUrl = "http://webservices.cjssecurity.com/members.asmx/getServiceFee";
				
				String json = UserFunctions.loadJson(
						serviceFeeUrl,
								snamevalue);
				
				JSONObject jsonOnj = null;
				try {
					jsonOnj = new JSONObject(json);
					status = jsonOnj.getString("status");
					Constant.serviceFee = jsonOnj.getString("amount");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	
	
				return null;
			}
	
			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				/*if (pDialog.isShowing())
					pDialog.dismiss();*/
			}
	
		}.execute();
	
	}
}
