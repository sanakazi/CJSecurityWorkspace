package com.callndata.chrisjones;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.ListView;
import android.widget.TextView;

public class SubAccounts extends Activity {

	Context context;

	TextView txtExbName;

	ListView listSubAcc;
	AccountAdapter adapterAccount;
	List<AccountRow> dataAccount;

	private ArrayList<NameValuePair> namevalue = new ArrayList<NameValuePair>();
	private ProgressDialog pDialog;
	boolean netCheck = false;

	String parentId, status = "";

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sub_acc_list);

		if (Build.VERSION.SDK_INT >= 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		context = SubAccounts.this;

		pDialog = new ProgressDialog(context);

		Intent i = getIntent();
		parentId = i.getStringExtra("parentId");
		
		listSubAcc = (ListView) findViewById(R.id.listSubAccounts);
		dataAccount = new ArrayList<AccountRow>();
		adapterAccount = new AccountAdapter(context, R.layout.row_accounts, dataAccount);
		listSubAcc.setAdapter(adapterAccount);

		netCheck = UserFunctions.isConnectionAvailable(context);
		if (netCheck == true) {
			getSubAccountsList();
		} else {
			UserFunctions.dialogboxshow("Message", "Internet Connection not available.", context);
		}
	}

	public void getSubAccountsList() {

		// Fetch Data
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

				// **Code**

				namevalue.clear();
				
				String subListUrl = "http://cjssecurity.com/RESTWS/subAccountList.asp?" +
									"parentCompanyDetailsID=" + parentId;
				
				String json = UserFunctions.loadJson(
								subListUrl,
								namevalue);
				
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
					for (int i = 1; i < arr.length(); i++) {
						try {

							dataAccount.add(new AccountRow(
									arr.getJSONObject(i).getString("num"), 
									arr.getJSONObject(i).getString("userType"),
									arr.getJSONObject(i).getString("ServiceType"),
									arr.getJSONObject(i).getString("fullName"),
									arr.getJSONObject(i).getString("CustomerID"),
									arr.getJSONObject(i).getString("emailID"),
									arr.getJSONObject(i).getString("servicesEndDate")
									));

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
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

				adapterAccount.notifyDataSetChanged();

			}

		}.execute();

	}
}
