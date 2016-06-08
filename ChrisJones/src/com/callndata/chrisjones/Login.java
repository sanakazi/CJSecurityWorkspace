package com.callndata.chrisjones;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends Activity{

	TextView tvSignUp, txtTerms;
	Button btnLogin;
	EditText etUsername, etPassword;
	
	private ArrayList<NameValuePair> snamevalue = new ArrayList<NameValuePair>();
	private ProgressDialog pDialog;
	boolean netCheck = false;
	
	String status = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sign_in);

		pDialog = new ProgressDialog(Login.this);
		
		etUsername = (EditText) findViewById(R.id.edUsername);
		etPassword = (EditText) findViewById(R.id.edPassword);
		btnLogin = (Button) findViewById(R.id.btnlogin);		
		tvSignUp = (TextView) findViewById(R.id.tvSignup);
		txtTerms = (TextView) findViewById(R.id.txtLTerms);
		
		etPassword.setTypeface(Typeface.DEFAULT);
		
		btnLogin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (etUsername.getText().toString().equals("")) {
					UserFunctions.dialogboxshow("Message","Please Enter Username.", Login.this);
					return;
				}
				if (!isValidEmail(etUsername.getText().toString())) {
					UserFunctions.dialogboxshow("Message","Please Enter Valid Email-Id.", Login.this);
					return;
				}
				else if (etPassword.getText().toString().equals("")) {
					UserFunctions.dialogboxshow("Message","Please Enter Password.", Login.this);
					return;
				}
				else {
					netCheck = UserFunctions.isConnectionAvailable(Login.this);
					if (netCheck == true) {
						login();
					} else {
						UserFunctions.dialogboxshow("Message", "Internet Connection not available.", Login.this);
					}
				}
			}
		});
		
		tvSignUp.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent i = new Intent(Login.this, SignUp.class);
				startActivity(i);
			}
		});
		
		txtTerms.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent i = new Intent(Login.this, Terms.class);
				startActivity(i);
			}
		});
	}
	
	private boolean isValidEmail(String email) {
		String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}
	
	public void login() {
		
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
				
				String uname = etUsername.getText().toString();
				String pwd = etPassword.getText().toString();
				
				
				/*http://cjssecurity.com/RESTWS/login.asp*/				
				/*String loginUrl = "http://webservices.cjssecurity.com/members.asmx/memberLogin?"+
										 "emailID="+ uname +
										 "&upassword=" + pwd;*/
				String loginUrl = "http://webservices.cjssecurity.com/members.asmx/memberLogin";
				
				snamevalue.add(new BasicNameValuePair("emailID", uname));
				snamevalue.add(new BasicNameValuePair("upassword", pwd));
				
				String json = UserFunctions.loadJson(
								loginUrl,
								snamevalue);
				
				Log.d("Login","Login JsonResponse is -"+json);
				
				JSONArray arr = null;
				JSONObject jsonOnj = null;
				try {
					jsonOnj = new JSONObject(json);
					arr = jsonOnj.getJSONArray("Message");
					status = jsonOnj.getString("status");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	
				if(status.equals("1"))
				{
					try {
						Constant.num = arr.getJSONObject(0).getString("num");
						Constant.userType = arr.getJSONObject(0).getString("userType");
						Constant.ServiceType = arr.getJSONObject(0).getString("ServiceType");
						Constant.fullName = arr.getJSONObject(0).getString("fullName");
						Constant.CustomerID = arr.getJSONObject(0).getString("CustomerID");
						Constant.emailID = arr.getJSONObject(0).getString("emailID");
						Constant.servicesEndDate = arr.getJSONObject(0).getString("servicesEndDate");
						Constant.parentCompanyDetailsID = arr.getJSONObject(0).getString("parentCompanyDetailsID");
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
			
				if(status.equals("1"))
				{
					SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Login.this);
					Editor edt = sp.edit();
					edt.putString("num", Constant.num);
					edt.putString("userType", Constant.fullName);
					edt.putString("ServiceType", Constant.ServiceType);
					edt.putString("fullName", Constant.fullName);
					edt.putString("CustomerID", Constant.CustomerID);
					edt.putString("emailID", Constant.emailID);
					edt.putString("servicesEndDate", Constant.servicesEndDate);
					edt.putString("parentCompanyDetailsID", Constant.parentCompanyDetailsID);
					edt.commit();
					
					Toast.makeText(Login.this, "Login Successful.", Toast.LENGTH_LONG).show();
					Intent i = new Intent(Login.this, Home.class);
					startActivity(i);
					finish();					
				}
				else if(status.equals("0"))
				{
					UserFunctions.dialogboxshow("Message", "Invalid Username or Password.", Login.this);
				}
				else
				{
					UserFunctions.dialogboxshow("Message", "Problem while Login.", Login.this);
				}
			}
	
		}.execute();
	
	}
}
