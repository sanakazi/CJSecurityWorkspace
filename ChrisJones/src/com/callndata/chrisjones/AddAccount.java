package com.callndata.chrisjones;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddAccount extends Activity {

	EditText customerId, name, mail, phone, address, password, zipcode, city;
	Button btnAddAccount;
	TextView txtTerms, lblRegTitle;
	
	String parentId="0", status = "", cityId="", longt, lat, userType="Home", serviceType="Free";
	
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
		setContentView(R.layout.add_account);
		
		if (Build.VERSION.SDK_INT >= 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		
		pDialog = new ProgressDialog(AddAccount.this);
		
		customerId = (EditText) findViewById(R.id.edAACustomerId);
		name = (EditText) findViewById(R.id.edAAName);
		mail = (EditText) findViewById(R.id.edAAMail);
		phone = (EditText) findViewById(R.id.edAAPhone);
		address = (EditText) findViewById(R.id.edAAAddress);
		password = (EditText) findViewById(R.id.edAAPassword);
		zipcode = (EditText) findViewById(R.id.edAAZipcode);
		city = (EditText) findViewById(R.id.edAAArea);
		txtTerms = (TextView) findViewById(R.id.txtAATerms);
		lblRegTitle = (TextView) findViewById(R.id.lblAATitle);
		
		btnAddAccount = (Button) findViewById(R.id.btnSubmitAddAccount);
		
		password.setTypeface(Typeface.DEFAULT);
		
		parentId = Constant.num;
		
		try
		{
			GPSTracker gps = new GPSTracker(AddAccount.this);
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
//		cityAdapter = new ArrayAdapter<String>(AddAccount.this, android.R.layout.simple_spinner_item, cities);
//		city.setAdapter(cityAdapter);
		
		try{
			netCheck = UserFunctions.isConnectionAvailable(AddAccount.this);
		
//		if (netCheck == true) {
//			getCityList();
//		} else {
//			UserFunctions.dialogboxshow("Message", "Internet Connection not available.", AddAccount.this);
//		}

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		btnAddAccount.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				
				
				if (name.getText().toString().equals("")) {
					UserFunctions.dialogboxshow("Message","Please Enter Name", AddAccount.this);
					return;
				}
				else if (mail.getText().toString().equals("")) {
					UserFunctions.dialogboxshow("Message","Please Enter E-Mail", AddAccount.this);
					return;
				}
				else if (phone.getText().toString().equals("")) {
					UserFunctions.dialogboxshow("Message","Please Enter Phone Number", AddAccount.this);
					return;
				}
				else if (address.getText().toString().equals("")) {
					UserFunctions.dialogboxshow("Message","Please Enter Address", AddAccount.this);
					return;
				}
				else if (password.getText().toString().equals("")) {
					UserFunctions.dialogboxshow("Message","Please Enter Password", AddAccount.this);
					return;
				}
				else if (zipcode.getText().toString().equals("")) {
					UserFunctions.dialogboxshow("Message","Please Enter ZipCode", AddAccount.this);
					return;
				}
				else if (city.getText().toString().equals("")) {
					UserFunctions.dialogboxshow("Message","Please Enter City", AddAccount.this);
					return;
				}
				else {
					
					netCheck = UserFunctions.isConnectionAvailable(AddAccount.this);
					if (netCheck == true) {
						dothePayment();
						//addAccount();
					} else {
						UserFunctions.dialogboxshow("Message", "Internet Connection not available.", AddAccount.this);
					}
				}

			}
		});
		
		
		txtTerms.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent i = new Intent(AddAccount.this, Terms.class);
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
		
		Intent intent = new Intent(this, PayPalService.class);
		intent.putExtra(PaymentActivity.EXTRA_PAYPAL_ENVIRONMENT,
				Constant.CONFIG_ENVIRONMENT);
		intent.putExtra(PaymentActivity.EXTRA_CLIENT_ID,
				Constant.CONFIG_CLIENT_ID);
		intent.putExtra(PaymentActivity.EXTRA_RECEIVER_EMAIL,
				Constant.CONFIG_RECEIVER_EMAIL);
		startService(intent);
		
		
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		 if (resultCode == Activity.RESULT_OK) {
			    PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
			    if (confirm != null) {
			      //verifyPayment(confirm);
			    	Toast.makeText(getApplicationContext(), "payment successfull", Toast.LENGTH_SHORT).show();
			    	//upgrade();=
			    	addAccount();
			    }
			  } else if (resultCode == Activity.RESULT_CANCELED) {
			    // Show the user that this got canceled
				  Toast.makeText(AddAccount.this, "Payment unsuccessful", Toast.LENGTH_LONG).show();
			  } else if (resultCode == PaymentActivity.RESULT_PAYMENT_INVALID) {
			    // Check the docs ;)
				  Toast.makeText(AddAccount.this, "Payment unsuccessful", Toast.LENGTH_LONG).show();
			  }
	}
	
	public void dothePayment() {
		Log.w("AddAccount = " , "dothePayment");
		PayPalPayment thingToBuy = new PayPalPayment(new BigDecimal(Constant.serviceFee), "USD", "CJ security");

		Intent intent = new Intent(AddAccount.this, PaymentActivity.class);
		intent.putExtra(PaymentActivity.EXTRA_PAYPAL_ENVIRONMENT, Constant.CONFIG_ENVIRONMENT);
		intent.putExtra(PaymentActivity.EXTRA_CLIENT_ID, Constant.CONFIG_CLIENT_ID);
		intent.putExtra(PaymentActivity.EXTRA_RECEIVER_EMAIL, Constant.CONFIG_RECEIVER_EMAIL);
		intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);
		intent.putExtra(PaymentActivity.EXTRA_PAYER_ID, "CJPayer");

		startActivityForResult(intent, 0);
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


	public void addAccount() {
		
		snamevalue.clear();
		
		new AsyncTask<String, String, String>() {
	
			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				pDialog.setMessage(Constant.PROGRESS_MESSAGE);
				pDialog.setCancelable(false);
				pDialog.show();
			}
	
			@Override
			protected String doInBackground(String... params) {
	
	//			snamevalue.add(new BasicNameValuePair("RequestType","CityList"));
				
 				String custId = customerId.getText().toString();
				String uname = name.getText().toString();
				String addr = address.getText().toString();
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
				
				serviceType = "-";
				
				String registrationUrl = "http://cjssecurity.com/RESTWS/register.asp?" + 
										 "parentCompanyDetailsID=" + parentId + "&userType=" + userType +
										 "&fullName="+ uname + "&CustomerID=" + custId + "&address=" + addr + "&areaName=" + areaName +
										 "&cityID=" + cityId + "&zipCode=" + zipCode + "&longitude=" + longt + 
										 "&latitude=" + lat + "&phoneNumbers=" + phoneNo + "&emailID=" + mailId +
										 "&upassword=" + pwd + "&ServiceType=" + serviceType;
				
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
	
				return null;
			}
	
			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if (pDialog.isShowing())
					pDialog.dismiss();
			
				if(status.equals("1"))
				{
					
					Toast.makeText(AddAccount.this, "Account Added Successful.", Toast.LENGTH_LONG).show();
					finish();
					
				}
				else
				{
					Toast.makeText(AddAccount.this, "Problem while Adding Account.", Toast.LENGTH_LONG).show();
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
