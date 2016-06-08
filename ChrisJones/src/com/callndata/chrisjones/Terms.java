package com.callndata.chrisjones;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class Terms extends Activity{

	CheckBox chkTerms;
	TextView txtAgree;
	
	boolean isSignUp=false;
	
	String validUser = "";
	
	private ProgressDialog pDialog;
	
	ArrayList<NameValuePair> snamevalue = new ArrayList<NameValuePair>();
	public static final String TAG= Terms.class.getSimpleName();
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		stopService(new Intent(this, PayPalService.class));
		super.onDestroy();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.terms);
		
		Intent intent = new Intent(this, PayPalService.class);
		intent.putExtra(PaymentActivity.EXTRA_PAYPAL_ENVIRONMENT,
				Constant.CONFIG_ENVIRONMENT);
		intent.putExtra(PaymentActivity.EXTRA_CLIENT_ID,
				Constant.CONFIG_CLIENT_ID);
		intent.putExtra(PaymentActivity.EXTRA_RECEIVER_EMAIL,
				Constant.CONFIG_RECEIVER_EMAIL);
		startService(intent);
		
		Intent i = getIntent();
		isSignUp = i.getBooleanExtra("SignUp", false);
		if(i.hasExtra("Register")){
			validUser = i.getExtras().getString("Register");
		}
		
		pDialog = new ProgressDialog(Terms.this);
		
		chkTerms = (CheckBox) findViewById(R.id.chkTerms);
		txtAgree = (TextView) findViewById(R.id.txtTAgree);
		
		if(isSignUp==true)
		{
			chkTerms.setVisibility(View.VISIBLE);
			txtAgree.setVisibility(View.VISIBLE);
		}
		
		txtAgree.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(isSignUp=true)
				{
					if(chkTerms.isChecked()==true)
					{
						/*Intent i = new Intent(Terms.this, Home.class);
						startActivity(i);
						finish();*/
						if( validUser.equals("Valid_values") ){
							dothePayment();
						}else{
							finish();
						}
					}
					else
					{
						Toast.makeText(Terms.this, "You must be agree to terms to sign up.", Toast.LENGTH_LONG).show();
					}
				}
				else
				{
					Intent i = new Intent(Terms.this, Login.class);
					startActivity(i);
					finish();
				}
			}
		});
	}
	
	public void dothePayment() {
		
		Log.w(TAG, "dothePayment");
		PayPalPayment thingToBuy = new PayPalPayment(new BigDecimal(Constant.serviceFee), "AUD", "CJ security");

		Intent intent = new Intent(Terms.this, PaymentActivity.class);
		intent.putExtra(PaymentActivity.EXTRA_PAYPAL_ENVIRONMENT, Constant.CONFIG_ENVIRONMENT);
		intent.putExtra(PaymentActivity.EXTRA_CLIENT_ID, Constant.CONFIG_CLIENT_ID);
		intent.putExtra(PaymentActivity.EXTRA_RECEIVER_EMAIL, Constant.CONFIG_RECEIVER_EMAIL);
		intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);
		intent.putExtra(PaymentActivity.EXTRA_PAYER_ID, "CJPayer");

		startActivityForResult(intent, 0);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		 if (resultCode == Activity.RESULT_OK) {
			    PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
			    if (confirm != null) {
			      //verifyPayment(confirm);
			    	Toast.makeText(getApplicationContext(), "payment successfull", Toast.LENGTH_SHORT).show();
			    	//upgrade();
			    	 register();
			    }
			  } else if (resultCode == Activity.RESULT_CANCELED) {
			    // Show the user that this got canceled
				  Toast.makeText(Terms.this, "Payment unsuccessful", Toast.LENGTH_LONG).show();
			  } else if (resultCode == PaymentActivity.RESULT_PAYMENT_INVALID) {
			    // Check the docs ;)
				  Toast.makeText(Terms.this, "Payment unsuccessful", Toast.LENGTH_LONG).show();
			  }
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
				
				String BASE_URL = "http://webservices.cjssecurity.com/members.asmx/MemberRegistration?";
				//http://cjssecurity.com/RESTWS/register.asp?*****OLD URL
				String registrationUrl = BASE_URL + 
										 "parentCompanyDetailsID=" + SignUp.parentId+
										 "&userType=" + SignUp.userType +
										 "&fullName="+ Constant.uname + "&CustomerID=" + Constant.custId + "&address=" + Constant.addr + "&areaName=" + Constant.areaName +
										 "&cityID=" + SignUp.cityId + "&zipCode=" + Constant.zipCode + "&longitude=" + SignUp.longt + 
										 "&latitude=" + SignUp.lat + "&phoneNumbers=" + Constant.phoneNo + "&emailID=" + Constant.mailId +
										 "&upassword=" + Constant.pwd + "&ServiceType=Paid" 
										 +"&ECName1="+Constant.ECName1
										 +"&ECContactNo1="+Constant.ECContactNo1
										 +"&ECRelationship1="+Constant.ECRelationship1
										 +"&ECName2="+Constant.ECName2
										 +"&ECContactNo2="+Constant.ECContactNo2
										 +"&ECRelationship2="+Constant.ECRelationship2
										 +"&anySpecialConditions="+Constant.anySpecialConditions
										 +"&iPhoneRegID="
										 +"&androidRegID="
										 +"&registrationSource=Android" ;
				
			/*	snamevalue.add(new BasicNameValuePair("parentCompanyDetailsID", SignUp.parentId));
				snamevalue.add(new BasicNameValuePair("userType", SignUp.userType));
				snamevalue.add(new BasicNameValuePair("fullName", Constant.uname));
				snamevalue.add(new BasicNameValuePair("CustomerID", Constant.custId));
				snamevalue.add(new BasicNameValuePair("address", Constant.addr));
				snamevalue.add(new BasicNameValuePair("areaName", Constant.areaName));
				snamevalue.add(new BasicNameValuePair("cityID", SignUp.cityId));
				snamevalue.add(new BasicNameValuePair("zipCode", Constant.zipCode));
				snamevalue.add(new BasicNameValuePair("longitude", SignUp.longt));
				snamevalue.add(new BasicNameValuePair("latitude", SignUp.lat));
				snamevalue.add(new BasicNameValuePair("phoneNumbers", Constant.phoneNo));
				snamevalue.add(new BasicNameValuePair("emailID", Constant.mailId));
				snamevalue.add(new BasicNameValuePair("upassword", Constant.pwd));
				snamevalue.add(new BasicNameValuePair("registrationSource", "Android"));
				snamevalue.add(new BasicNameValuePair("ServiceType", "Paid"));
				snamevalue.add(new BasicNameValuePair("androidRegID", ""));
				snamevalue.add(new BasicNameValuePair("iPhoneRegID", ""));
				snamevalue.add(new BasicNameValuePair("ECName1", Constant.ECName1));
				snamevalue.add(new BasicNameValuePair("ECContactNo1", Constant.ECContactNo1));
				snamevalue.add(new BasicNameValuePair("ECRelationship1", Constant.ECRelationship1));
				snamevalue.add(new BasicNameValuePair("ECName2", Constant.ECName2));
				snamevalue.add(new BasicNameValuePair("ECContactNo2", Constant.ECContactNo2));
				snamevalue.add(new BasicNameValuePair("ECRelationship2", Constant.ECRelationship2));
				snamevalue.add(new BasicNameValuePair("anySpecialConditions", Constant.anySpecialConditions));*/
				
				Log.d("Terms","registration url is"+registrationUrl);
				
				//String finalUrl = registrationUrl.replace(" ", "%20");

				String finalUrl = registrationUrl.replaceAll(" ", "%20");
				InputStream mInputStream = null;
				
				/*String json = UserFunctions.loadJson(
								finalUrl,
								snamevalue);*/
				try {
					HttpClient mHttpClient = new DefaultHttpClient();
					HttpGet httpget = new HttpGet(finalUrl);
					HttpResponse mHttpResponse = mHttpClient.execute(httpget);
					HttpEntity mHttpEntity = mHttpResponse.getEntity();
					mInputStream = mHttpEntity.getContent();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					// Log.e(strTAG,"Error in HttpClient,HttpPost,HttpResponse,HttpEntity");
					e.printStackTrace();
				}

				String strLine = null;
				String strResult = null;
				// convert response in to the string.
				try {
					BufferedReader mBufferedReader = new BufferedReader(
							new InputStreamReader(mInputStream, "iso-8859-1"),
							8);
					StringBuilder mStringBuilder = new StringBuilder();
					while ((strLine = mBufferedReader.readLine()) != null) {
						mStringBuilder.append(strLine + "\n");
					}
					mInputStream.close();
					strResult = mStringBuilder.toString();

				} catch (Exception e) {
					// Log.e(strTAG, "Error in BufferedReadering");
					e.printStackTrace();
				}
				
				Log.d("Terms","registration json response is"+strResult);
				
				JSONArray arr = null;
				try {
					JSONObject jsonObj = new JSONObject(strResult);
					Log.w("Terms = ", "dothe" + strResult);
					arr = jsonObj.getJSONArray("Message");
					SignUp.status = jsonObj.getString("status");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	
				if(SignUp.status.equals("1"))
				{
					try {
						Constant.num = arr.getJSONObject(0).getString("num");
						Constant.userType = arr.getJSONObject(0).getString("userType");
						Constant.ServiceType = arr.getJSONObject(0).getString("ServiceType");
						Constant.fullName = arr.getJSONObject(0).getString("fullName");
						Constant.CustomerID = arr.getJSONObject(0).getString("CustomerID");
						Constant.emailID = arr.getJSONObject(0).getString("emailID");
						Constant.servicesEndDate = arr.getJSONObject(0).getString("servicesEndDate");
						Constant.parentCompanyDetailsID = "0";
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
			
				if(SignUp.status.equals("1"))
				{
					
					SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Terms.this);
					Editor edt = sp.edit();
					edt.putString("num", Constant.num);
					edt.putString("userType", Constant.fullName);
					edt.putString("ServiceType", Constant.ServiceType);
					edt.putString("fullName", Constant.fullName);
					edt.putString("CustomerID", Constant.CustomerID);
					edt.putString("emailID", Constant.emailID);
					edt.putString("servicesEndDate", Constant.servicesEndDate);
					edt.putString("parentCompanyDetailsID", "0");
					edt.commit();
					
					Toast.makeText(Terms.this, "Registration Successful.", Toast.LENGTH_LONG).show();
//					Toast.makeText(SignUp.this, "Registration Successful.", Toast.LENGTH_LONG).show();
					Intent i = new Intent(Terms.this, Home.class);
					i.putExtra("SignUp", true);
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);
					finish();
					
				}
				else
				{
					Toast.makeText(Terms.this, "Problem while Registration.", Toast.LENGTH_LONG).show();
				}
			}
	
		}.execute();
	
	}
}
