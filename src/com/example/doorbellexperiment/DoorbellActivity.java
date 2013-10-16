package com.example.doorbellexperiment;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Profile;
import android.provider.ContactsContract.Contacts;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class DoorbellActivity extends Activity {
	private static final int CONTACT_PICKER_RESULT = 1001;
	private static final String DEBUG_TAG = "DoorbellActivity";
	TextView contactName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.doorbell_activity);
		contactName = (TextView)findViewById(R.id.contact_name);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.doorbell, menu);
		return true;
	}
	
	public void onPickContact(View v) {
		Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Contacts.CONTENT_URI);
		startActivityForResult(pickContactIntent, CONTACT_PICKER_RESULT);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK) {
			switch(requestCode){
			case CONTACT_PICKER_RESULT:
				Uri result = data.getData(); 
				String id = result.getLastPathSegment();
				String[] selectionArgs = {id};
				
				Cursor cursor = getContentResolver().query(StructuredPostal.CONTENT_URI, null, StructuredPostal._ID + "=?", selectionArgs, null);
				Log.w(DEBUG_TAG, "Count = " + cursor.getCount());
				if(cursor.moveToFirst()) {
					if(!cursor.isAfterLast()) {
						
						int addressIndex = cursor.getColumnIndex(StructuredPostal.FORMATTED_ADDRESS);
						int nameIndex = cursor.getColumnIndex(StructuredPostal.DISPLAY_NAME);
						
						String address = cursor.getString(addressIndex);
						String name = cursor.getString(nameIndex);
						
						contactName.setText(name); 
						Log.w(DEBUG_TAG, "Name: " + name);
						Log.w(DEBUG_TAG, "Address: " + address);
					}
					else {
						Log.w(DEBUG_TAG, "No address in contacts");
					}
				}
				
				break;
			}
		}
		else {
			Log.w(DEBUG_TAG, "onPick result not ok");
		}
	}
}