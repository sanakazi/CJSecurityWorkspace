package com.callndata.chrisjones;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class UserFunctions {

	public static void dialogboxshow(String title, String msg, Context context) {

		final Dialog dialog = new Dialog(context);
		dialog.setCancelable(true);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setContentView(R.layout.dialogbox);

		TextView messagetitleis = (TextView) dialog
				.findViewById(R.id.message_title_is);
		TextView messageis = (TextView) dialog.findViewById(R.id.message_is);
		Button okbutton = (Button) dialog.findViewById(R.id.okbutton);

		messagetitleis.setText(title);
		messageis.setText(msg);

		okbutton.setOnClickListener(new OnClickListener() {

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

	}

	public static String loadJson(String strUrl,
			ArrayList<NameValuePair> alstNameValuePair) {
		InputStream mInputStream = null;
		try {

			// This is the default apacheconnection.
			HttpClient mHttpClient = new DefaultHttpClient();

			// Pathe of serverside
			HttpPost mHttpPost = new HttpPost(strUrl);

			if (alstNameValuePair != null) {
				// post the valur you want to pass.
				mHttpPost
						.setEntity(new UrlEncodedFormEntity(alstNameValuePair));
			}

			// get the valu from the saerverside as response.
			HttpResponse mHttpResponse = mHttpClient.execute(mHttpPost);
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
					new InputStreamReader(mInputStream, "iso-8859-1"), 8);
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
		return strResult;
	}

	public static boolean isConnectionAvailable(Context context) {

		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityManager != null) {
			NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
			if (netInfo != null && netInfo.isConnected()
					&& netInfo.isConnectedOrConnecting()
					&& netInfo.isAvailable()) {
				return true;
			}
		}
		return false;
	}

}
