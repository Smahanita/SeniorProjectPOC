package com.seniorproject.sqlitedatabase;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//SQLiteOpenHelper helps you open or create a database

public class DatabaseHelper  extends SQLiteOpenHelper {

	// Context : provides access to application-specific resources and classes
	
	public DatabaseHelper(Context applicationcontext) {
		
		// Call use the database or to create it. Name of the database: contactbook.db
		
     super(applicationcontext, "contactbook.db", null, 1);
     
 }
	
	// onCreate is called the first time the database is created
	
	public void onCreate(SQLiteDatabase database) {
		
		// How to create a table in SQLite
		// Make sure you don't put a ; at the end of the query
		
		String contactsTable = "CREATE TABLE contacts ( contactId INTEGER PRIMARY KEY, name TEXT, " +
				"phoneNumber TEXT)";
		
		// Executes the query provided as long as the query isn't a select
		// or if the query doesn't return any data
		
     database.execSQL(contactsTable);

	}

	// onUpgrade is used to drop tables, add tables, or do anything 
	// else it needs to upgrade
	// This is droping the table to delete the data and then calling
	// onCreate to make an empty table
	
	public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
		String query = "DROP TABLE IF EXISTS contacts";
		
		// Executes the query provided as long as the query isn't a select
		// or if the query doesn't return any data
		
		database.execSQL(query);
     onCreate(database);
	}
	
	public void insertContact(HashMap<String, String> queryValues) {
		
		// Open a database for reading and writing
		
		SQLiteDatabase database = this.getWritableDatabase();
		
		// Stores key value pairs being the column name and the data
		// ContentValues data type is needed because the database
		// requires its data type to be passed
		
		ContentValues values = new ContentValues();
		
		values.put("name", queryValues.get("firstName"));
		values.put("phoneNumber", queryValues.get("phoneNumber"));
		
		// Inserts the data in the form of ContentValues into the
		// table name provided: contacts
		
		database.insert("contacts", null, values);
		
		// Release the reference to the SQLiteDatabase object
		
		database.close();
	}
	
	public int updateContact(HashMap<String, String> queryValues) {
		
		// Open a database for reading and writing
		
		SQLiteDatabase database = this.getWritableDatabase();	
		
		// Stores key value pairs being the column name and the data
		
	    ContentValues values = new ContentValues();
	    
	    values.put("name", queryValues.get("firstName"));
		values.put("phoneNumber", queryValues.get("phoneNumber"));
		
		// update(TableName, ContentValueForTable, WhereClause, ArgumentForWhereClause)
		
	    return database.update("contacts", values, "contactId" + " = ?", new String[] { queryValues.get("contactId") });
	}
	
	// Used to delete a contact with the matching contactId
	
	public void deleteContact(String id) {

		// Open a database for reading and writing
		
		SQLiteDatabase database = this.getWritableDatabase();
		
		String deleteQuery = "DELETE FROM  contacts where contactId='"+ id +"'";
		
		// Executes the query provided as long as the query isn't a select
		// or if the query doesn't return any data
		
		database.execSQL(deleteQuery);
	}
	
	public ArrayList<HashMap<String, String>> getAllContacts() {
		
		// ArrayList that contains every row in the database
		// and each row key / value stored in a HashMap
		
		ArrayList<HashMap<String, String>> contactArrayList;
		
		contactArrayList = new ArrayList<HashMap<String, String>>();
		
		String selectQuery = "SELECT  * FROM contacts ORDER BY name";
		
		// Open a database for reading and writing
		
	    SQLiteDatabase database = this.getWritableDatabase();
	    
	    // Cursor provides read and write access for the 
	    // data returned from a database query
	    
	    // rawQuery executes the query and returns the result as a Cursor
	    
	    Cursor cursor = database.rawQuery(selectQuery, null);	
	    
	    // Move to the first row
	    
	    if (cursor.moveToFirst()) {
	        do {
	        	HashMap<String, String> contactMap = new HashMap<String, String>();
	        	
	        	// Store the key / value pairs in a HashMap
	        	// Access the Cursor data by index that is in the same order
	        	// as used when creating the table
	        	
	        	contactMap.put("contactId", cursor.getString(0));
	        	contactMap.put("name", cursor.getString(1));
	           	contactMap.put("phoneNumber", cursor.getString(2));
	     	        	
	        	contactArrayList.add(contactMap);
	        } while (cursor.moveToNext()); // Move Cursor to the next row
	    }
	 
	    // return contact list
	    return contactArrayList;
	}
	
	public HashMap<String, String> getContactInfo(String id) {
		HashMap<String, String> contactMap = new HashMap<String, String>();
		
		// Open a database for reading only
		
		SQLiteDatabase database = this.getReadableDatabase();
		
		String selectQuery = "SELECT * FROM contacts where contactId='"+id+"'";
		
		// rawQuery executes the query and returns the result as a Cursor
		
		Cursor cursor = database.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
	        do {
					
	        	contactMap.put("contactId", cursor.getString(0));
	        	contactMap.put("name", cursor.getString(1));
	           	contactMap.put("phoneNumber", cursor.getString(2));
				   
	        } while (cursor.moveToNext());
	    }				    
	return contactMap;
	}	
}