package com.callndata.chrisjones;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class SplashScreen extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		/****** Thread that will sleep for 2 seconds *************/
		Thread background = new Thread() {
			public void run() {

				try {
					// Thread will sleep for 3 seconds
					sleep(2000);

					SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
					String num = sp.getString("num", "");
					String userType = sp.getString("userType", "");
					String ServiceType = sp.getString("ServiceType", "");
					String fullName = sp.getString("fullName", "");
					String CustomerID = sp.getString("CustomerID", "");
					String emailID = sp.getString("emailID", "");
					String servicesEndDate = sp.getString("servicesEndDate", "");
					String parentCompanyDetailsID = sp.getString("parentCompanyDetailsID", "");
					

					if(num.equalsIgnoreCase("")) {

						Intent i = new Intent(SplashScreen.this, Login.class);
						startActivity(i);
						finish();

					} else {

						Intent i = new Intent(SplashScreen.this, Home.class);
						i.putExtra("reload", "true");
						i.putExtra("num", num);
						
						Constant.num = num;
						Constant.userType = userType;
						Constant.ServiceType = ServiceType;
						Constant.fullName = fullName;
						Constant.CustomerID = CustomerID;
						Constant.emailID = emailID;
						Constant.servicesEndDate = servicesEndDate;
						Constant.parentCompanyDetailsID = parentCompanyDetailsID;
						
						startActivity(i);
						finish();
					}

				} catch (Exception e) {

				}
			}
		};

		// Start Thread
		background.start();

	}

}
