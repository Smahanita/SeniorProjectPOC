package com.seniorproject.myassistant;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.seniorproject.sqlitedatabase.DatabaseHelper;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.PhoneLookup;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ContactListActivity extends Activity {

	public String contactName;
	public String phoneNumber;
	DatabaseHelper dbTools = new DatabaseHelper(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_list);

		// Get list of contact on the phone
		// Cursor cursor =
		// getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
		// null, null, null, null);
		Cursor cursor = getContentResolver().query(Phone.CONTENT_URI, null,
				null, null, null);

		ArrayList<String> contactList = new ArrayList<String>();

		while (cursor.moveToNext()) {
			int nameIndex = cursor.getColumnIndex(Phone.DISPLAY_NAME);
			contactName = cursor.getString(nameIndex);
			int phoneIdx = cursor.getColumnIndex(Phone.NUMBER);
			phoneNumber = cursor.getString(phoneIdx);

			String contentToDispay = contactName + "  " + phoneNumber + " ";
			contactList.add(contentToDispay);
			Collections.sort(contactList, String.CASE_INSENSITIVE_ORDER);

		}
		ListView dropdown = (ListView) findViewById(R.id.listContactList);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1,
				contactList);
		dropdown.setAdapter(adapter);

		// get Json folder path

		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			String path = Environment.getExternalStorageDirectory().getPath();
		}

		File jsonFolder = new File(Environment.getExternalStorageDirectory(),
				"jason_folder");

		// File jsonFolder = new File(getApplicationContext().getFilesDir(),
		// "jasonFolder");

		// Create JSON folder if it doesn't exist
		if (!jsonFolder.exists()) {
			jsonFolder.mkdirs();
		}

		JSONObject jsonObject = new JSONObject();
		JSONArray jsonArray = new JSONArray();

		// add each contact name to the JSON object
		try {
			for (String string : contactList) {
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("ContactName", contactName);
				jsonObj.put("PhoneNumber", phoneNumber);
				jsonArray.put(jsonObj);
			}

			jsonObject.put("ContactList", jsonArray);

			// Save JSON string to a file
			FileOutputStream outputStream = new FileOutputStream(new File(
					jsonFolder, "jsonFile.txt"));
			outputStream.write(jsonObject.toString().getBytes());
			outputStream.close();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void addContact(View view) {

		// Will hold the HashMap of values

		HashMap<String, String> queryValuesMap = new HashMap<String, String>();

		// Get the values from the EditText boxes

		queryValuesMap.put("name", contactName.toString());
		queryValuesMap.put("phoneNumber", phoneNumber.toString());

		// Call for the HashMap to be added to the database

		dbTools.insertContact(queryValuesMap);

		// Call for MainActivity to execute

		this.callActivity(view);
	}

	public void callActivity(View view) {
		Intent theIntent = new Intent(getApplication(),
				DisplayDatabaseContentActivity.class);
		startActivity(theIntent);
	}
}