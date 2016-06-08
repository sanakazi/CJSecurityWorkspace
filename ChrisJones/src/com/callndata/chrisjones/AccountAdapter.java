package com.callndata.chrisjones;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AccountAdapter extends ArrayAdapter<AccountRow> {

	List<AccountRow> data;
	Context context;
	int layoutResID;
	
	static class Holder {
		
		TextView name;
		TextView id;
		TextView mail;
	}

	public AccountAdapter(Context context, int layoutResourceId,
			List<AccountRow> data) {
		super(context, layoutResourceId, data);

		this.data = data;
		this.context = context;
		this.layoutResID = layoutResourceId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Holder holder = null;
		View row = convertView;
		holder = null;
		
		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResID, parent, false);

			holder = new Holder();

			holder.name = (TextView) row.findViewById(R.id.custName);
			holder.id = (TextView) row.findViewById(R.id.custId);
			holder.mail = (TextView) row.findViewById(R.id.custMail);

			row.setTag(holder);
		} else {
			holder = (Holder) row.getTag();
		}

		final AccountRow itemdata = data.get(position);
		
		holder.name.setText(itemdata.getFullName());
		holder.id.setText("Customer Id : " + itemdata.getCustomerID() + " - Usertype : " + itemdata.getUserType());
		holder.mail.setText(itemdata.getEmailID());
		
		return row;

	}	
}
