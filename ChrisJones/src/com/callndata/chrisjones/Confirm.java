package com.callndata.chrisjones;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Confirm extends Activity {

	TextView txtTerms, txtConfirmType;
	Button btnConfirm, btnCancel;
	String type, cellNo, msg, dateTime;
	
	Double latitude, longitude;
	LocationManager lm;
	
	String status = "";
	String SMSMessage = "";
	
	private ArrayList<NameValuePair> snamevalue = new ArrayList<NameValuePair>();
	private ProgressDialog pDialog;
	boolean netCheck = false;
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.confirm);
		
		pDialog = new ProgressDialog(Confirm.this);
		
		try
		{
			GPSTracker gps = new GPSTracker(Confirm.this);
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
		
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		Date now = new Date();
		dateTime = fmt.format(now);

		Intent i = getIntent();

		type = i.getStringExtra("type");		
		String message = "";
		if(i.hasExtra("alert")){
			 message = i.getStringExtra("alert");
		}
//		cellNo = "+61488171414";
		cellNo = "0411654771";
		//cellNo = "09987577454";
		//cellNo = "9969929915";
		
		/*msg = "Emergency Requested" + "\nUsername : "+ Constant.fullName + "\n"+type +"\n"+message;
		SMSMessage = "Emergency Requested.. : " + type + "\nUsername : "+ Constant.fullName;*/
		msg = "Emergency Requested.. : " + type + "\nUsername : " + Constant.fullName+"\n"+message;
		SMSMessage = "This is Emergency.. : " + type + "\nUsername : "+ Constant.fullName;

		txtTerms = (TextView) findViewById(R.id.txtCTerms);
		txtConfirmType = (TextView) findViewById(R.id.txtConfirmType);
		btnConfirm = (Button) findViewById(R.id.btnConfirm);
		btnCancel = (Button) findViewById(R.id.btnCancel);

		txtConfirmType.setText(msg);
		
		btnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Confirm.this, Home.class);
				startActivity(i);
				finish();
				
			}
		});
		
		btnConfirm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				
				netCheck = UserFunctions.isConnectionAvailable(Confirm.this);
				if (netCheck == true) {
					supportReq();
					//sendSMS();
				} else {
					sendSMS();
				}
				
//				int networkStatus = NetworkUtil.isNetworkAvail(Confirm.this);
//				if(networkStatus==1)
//				{
//					netCheck = UserFunctions.isConnectionAvailable(Confirm.this);
//					if (netCheck == true) {
//						supportReq();
//					} else {
//						sendSMS();
//					}
//				}
//				else
//				{
//					msg = msg + "\n\n Latitude : " + latitude + "\n Longitude : "+ longitude;
//
//					SharedPreferences spMsg = getApplicationContext().getSharedPreferences("SAVE_MSG", 0);
//					SharedPreferences.Editor editor = spMsg.edit();
//					editor.putString("cellno", cellNo);
//					editor.putString("msg", msg);
//					editor.commit();
//				}
			}
		});

		txtTerms.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent i = new Intent(Confirm.this, Terms.class);
				startActivity(i);
			}
		});
	}

	public void sendSMS() {

		String smsSent = "SMS_SENT";
		String smsDelivered = "SMS_DELIVERED";

		msg = msg + "\n\n Latitude : " + latitude + "\n Longitude : "+ longitude;
		SMSMessage = SMSMessage  + "\n\n Latitude : " + latitude + "\n Longitude : "+ longitude;

		PendingIntent sendPi = PendingIntent.getActivity(getApplicationContext(), 0, new Intent(smsSent), 0);
		PendingIntent deliveredPI = PendingIntent.getBroadcast(getApplicationContext(), 0,new Intent(smsDelivered), 0);
/*
		// Receiver for Sent SMS.
		registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				unregisterReceiver(this);
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					Toast.makeText(arg0, "SMS sent",
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					Toast.makeText(arg0, "Generic failure",
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_NO_SERVICE:
					Toast.makeText(arg0, "No service",
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_NULL_PDU:
					Toast.makeText(arg0, "Null PDU",
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_RADIO_OFF:
					Toast.makeText(arg0, "Radio off",
							Toast.LENGTH_SHORT).show();
					break;
				}
			}
		}, new IntentFilter(smsSent));

		// Receiver for Delivered SMS.
		registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				unregisterReceiver(this);
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					Toast.makeText(arg0, "SMS delivered",
							Toast.LENGTH_SHORT).show();
					break;
				case Activity.RESULT_CANCELED:
					Toast.makeText(arg0, "SMS not delivered",
							Toast.LENGTH_SHORT).show();
					break;
				}
			}
		}, new IntentFilter(smsDelivered));*/

		SmsManager smsManager = SmsManager.getDefault();
		smsManager.sendTextMessage(cellNo, null, SMSMessage, sendPi, deliveredPI);
		
		finish();
	}
	
	
	public void supportReq() {
		
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
				
				String URL = "http://webservices.cjssecurity.com/support.asmx/AddSupport";
				
				String supportUrl = "http://cjssecurity.com/RESTWS/supportRequest.asp?" +
									"userid=" + Constant.num +
									"&CustomerID=" + Constant.CustomerID +
									"&supportType=" + type +
									"&longitude=" + longitude +
									"&latitude=" + latitude +
									"&localDateTime=" + dateTime +
									"&fullName=" + Constant.fullName;
				
				supportUrl = supportUrl.replaceAll(" ", "%20");
				
				snamevalue.add(new BasicNameValuePair("userid", Constant.num));
				snamevalue.add(new BasicNameValuePair("CustomerID", Constant.CustomerID));
				snamevalue.add(new BasicNameValuePair("supportType", type));
				snamevalue.add(new BasicNameValuePair("longitude", ""+longitude));
				snamevalue.add(new BasicNameValuePair("latitude", ""+latitude));
				snamevalue.add(new BasicNameValuePair("localDateTime", dateTime));
				snamevalue.add(new BasicNameValuePair("fullName",  Constant.fullName));
				
				Log.d("Confirm","Url is"+supportUrl);
				
				String json = UserFunctions.loadJson(
								URL,
								snamevalue);
				
				Log.d("Confirm","Json is"+json);
				
				JSONArray arr = null;
				JSONObject JSONObj = null;
				try {
					//5/5/16 response has changed
					//arr = new JSONArray(json);
					//status = arr.getJSONObject(0).getString("status");
					
					JSONObj = new JSONObject(json);
					status = JSONObj.getString("status");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	
				if(status.equals("1"))
				{
					//5/5/16 response has changed  
					/*try {
						Constant.num = arr.getJSONObject(1).getString("num");
						//Constant.userType = arr.getJSONObject(1).getString("userType");
						//Constant.ServiceType = arr.getJSONObject(1).getString("ServiceType");
						//Constant.fullName = arr.getJSONObject(1).getString("fullName");
						Constant.CustomerID = arr.getJSONObject(1).getString("CustomerID");
						Constant.emailID = arr.getJSONObject(1).getString("CustomerID");
						//Constant.servicesEndDate = arr.getJSONObject(1).getString("servicesEndDate");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}*/
				}
	
				return null;
			}
	
			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if (pDialog.isShowing())
					pDialog.dismiss();
				
				sendSMS();
			}
	
		}.execute();
	
	}
}
