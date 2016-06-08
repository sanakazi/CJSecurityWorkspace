package com.callndata.chrisjones;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.SyncStateContract.Constants;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Home extends Activity {

	TextView txtVersion, txtUser, txtHomeTerms;
	LinearLayout bulgary, fire, duress, medical;
	
	Button upgrade, addAccount;
	
	String num, reload, status = "";
	
	private ArrayList<NameValuePair> snamevalue = new ArrayList<NameValuePair>();
	private ProgressDialog pDialog;
	boolean netCheck = false;
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		//stopService(new Intent(this, PayPalService.class));
		super.onDestroy();
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
			    }
			  } else if (resultCode == Activity.RESULT_CANCELED) {
			    // Show the user that this got canceled
			  } else if (resultCode == PaymentActivity.RESULT_PAYMENT_INVALID) {
			    // Check the docs ;)
			  }
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		
		pDialog = new ProgressDialog(Home.this);

		Intent i = getIntent();
		
	/*	 Intent intent = new Intent(this, PayPalService.class);
		 intent.putExtra(PaymentActivity.EXTRA_PAYPAL_ENVIRONMENT, Constant.CONFIG_ENVIRONMENT);
		 intent.putExtra(PaymentActivity.EXTRA_CLIENT_ID, Constant.CONFIG_CLIENT_ID);
		 intent.putExtra(PaymentActivity.EXTRA_RECEIVER_EMAIL, Constant.CONFIG_RECEIVER_EMAIL);
		 startService(intent);*/
		
		try{
			
			reload = i.getStringExtra("reload");
			num = i.getStringExtra("num");
		}
		catch(Exception e)
		{
			reload = "";
		}
		
		txtUser = (TextView) findViewById(R.id.txtUserName);
		txtVersion = (TextView) findViewById(R.id.txtHmVersion);
		txtHomeTerms = (TextView) findViewById(R.id.txtHomeTerms);
		
		upgrade = (Button) findViewById(R.id.btnUpgrade);
		addAccount = (Button) findViewById(R.id.btnAddAccount);

		bulgary = (LinearLayout) findViewById(R.id.ll_bulgary);
		fire = (LinearLayout) findViewById(R.id.ll_fire);
		duress = (LinearLayout) findViewById(R.id.ll_duress);
		medical = (LinearLayout) findViewById(R.id.ll_medical);
		
		txtUser.setText(Constant.fullName);
		
		if(Constant.ServiceType.equalsIgnoreCase("Free"))
		{
			//upgrade button made invisible
			//upgrade.setVisibility(View.VISIBLE);
		}
		else
		{
			txtVersion.setText("Pro Version");
		}
		
		if(!Constant.parentCompanyDetailsID.equalsIgnoreCase("0"))
		{
			addAccount.setVisibility(View.GONE);
		}
		
		if(reload!=null)
		{
			if(reload.equalsIgnoreCase("true"))
			{
				netCheck = UserFunctions.isConnectionAvailable(Home.this);
				if (netCheck == true) {
					reloadInfo();
				} else {
//					UserFunctions.dialogboxshow("Message", "Internet Connection not available.", Home.this);
				}
			}
		}
		
		txtHomeTerms.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent i = new Intent(Home.this, Terms.class);
				startActivity(i);
			}
		});
		
		txtUser.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				/*Intent userDetails = new Intent(Home.this, SubAccounts.class);
				userDetails.putExtra("parentId", Constant.num);
				startActivity(userDetails);*/
				
			}
		});
		
		txtUser.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				
				final Dialog dialog = new Dialog(Home.this);
				dialog.setCancelable(true);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.getWindow().setBackgroundDrawable(
						new ColorDrawable(android.graphics.Color.TRANSPARENT));
				dialog.setContentView(R.layout.logout);

				Button yes = (Button) dialog.findViewById(R.id.logout_yes);
				Button no = (Button) dialog.findViewById(R.id.logout_no);

				yes.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
						SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		        		Editor edt = sp.edit();
		        		edt.clear();
		        		edt.commit();
		        		
		            	Intent i=new Intent(getBaseContext(),Login.class);
		                startActivity(i);
		                finish();
		                dialog.dismiss();
					}
				});
				
				no.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});

				try {
					dialog.show();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				return true;
			}
		});

		upgrade.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				netCheck = UserFunctions.isConnectionAvailable(Home.this);
				if (netCheck == true) {
					//upgrade();
					/*PayPalPayment thingToBuy = new PayPalPayment(new BigDecimal("1"), "USD", "CJ security");

					Intent intent = new Intent(Home.this, PaymentActivity.class);
					intent.putExtra(PaymentActivity.EXTRA_PAYPAL_ENVIRONMENT, Constant.CONFIG_ENVIRONMENT);
					intent.putExtra(PaymentActivity.EXTRA_CLIENT_ID, Constant.CONFIG_CLIENT_ID);
					intent.putExtra(PaymentActivity.EXTRA_RECEIVER_EMAIL, Constant.CONFIG_RECEIVER_EMAIL);
					intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);
					intent.putExtra(PaymentActivity.EXTRA_PAYER_ID, "CJPayer");

					startActivityForResult(intent, 0);*/
				} else {
					txtUser.setText(Constant.fullName);
					UserFunctions.dialogboxshow("Message", "Internet Connection not available.", Home.this);
				}
			}
		});
		
		addAccount.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Home.this, AddAccount.class);
				startActivity(i);
			}
		});
		
		txtVersion.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			}
		});

		bulgary.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Home.this, Confirm.class);
				i.putExtra("type", "General Emergency");
				i.putExtra("alert", "You will receive a call back to confirm the nature of your request and then provided with the appropriate service or advice." );
				startActivity(i);
			}
		});

		fire.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Home.this, Confirm.class);
				i.putExtra("type", "Fire");
				i.putExtra("alert", getString(R.string.fire_alert));
				startActivity(i);
			}
		});

		duress.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Home.this, Confirm.class);
				i.putExtra("type", "Duress");
				i.putExtra("alert", getString(R.string.duress_alert));
				startActivity(i);
			}
		});

		medical.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Home.this, Confirm.class);
				i.putExtra("type", "Medical");
				i.putExtra("alert", getString(R.string.fire_alert));
				startActivity(i);
			}
		});
	}
	
	
	public void reloadInfo() {
		
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
				
				String reloadUrl = "http://cjssecurity.com/RESTWS/getlatetInfo.asp?" +
										 "num=" + num;
				
				String json = UserFunctions.loadJson(
								reloadUrl,
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
						Constant.emailID = arr.getJSONObject(1).getString("CustomerID");
						Constant.servicesEndDate = arr.getJSONObject(1).getString("servicesEndDate");
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
					txtUser.setText(Constant.fullName);
				}
				else
				{
					Toast.makeText(Home.this, "Problem while loading data.", Toast.LENGTH_LONG).show();
				}
			}
	
		}.execute();
	
	}
	
	
	public void upgrade() {
		
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
	
				
				String userId = Constant.num;
				String customerId = Constant.CustomerID;
				
				String upgradeUrl = "http://cjssecurity.com/RESTWS/upgradeRequest.asp?" +
									"userid=" + userId +
									"&CustomerID=" + customerId;
				
				String json = UserFunctions.loadJson(
								upgradeUrl,
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
						Constant.CustomerID = arr.getJSONObject(1).getString("CustomerID");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						
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
					upgrade.setVisibility(View.GONE);
					Toast.makeText(Home.this, "Upgraded Successfully.", Toast.LENGTH_LONG).show();
				}
				else
				{
					Toast.makeText(Home.this, "Problem while Upgrading.", Toast.LENGTH_LONG).show();
				}
			}
	
		}.execute();
	
	}
}
