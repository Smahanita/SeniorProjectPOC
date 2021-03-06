package com.seniorproject.myassistant;

import java.sql.Date;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;

import android.app.Activity;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class CalendarEventActivity extends Activity implements
		android.view.View.OnClickListener {

	// getting Columns from the underlying calendar database
	private Cursor mCursor = null;
	private static final String[] COLS = new String[] {
			CalendarContract.Events.TITLE, CalendarContract.Events.DTSTART };

	public void onClick(DialogInterface dialog, int which) {
		
	}

	// Populate the database cursor
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar_event);

		mCursor = getContentResolver().query(CalendarContract.Events.CONTENT_URI, COLS, null, null, null);
		mCursor.moveToFirst();
		int numOfElementofCursor = mCursor.getCount();
		String calendars = CalendarContract.Events.ACCOUNT_NAME;
		String calendar6 = CalendarContract.Events.CALENDAR_DISPLAY_NAME;
		String calendar1 = CalendarContract.Events.ACCOUNT_TYPE.toString();
		String calendar2 = CalendarContract.Events._COUNT.toString();
		String calendar3 = CalendarContract.Events.CALENDAR_COLOR.toString();

		
				
				
		Button b = (Button) findViewById(R.id.next);

		b.setOnClickListener((android.view.View.OnClickListener) this);
		b = (Button) findViewById(R.id.previous);

		b.setOnClickListener((android.view.View.OnClickListener) this);
		onClick(findViewById(R.id.previous));

		// display all calendar event in a list

		// output the events

		ArrayList<String> CalendarEvents = new ArrayList<String>();

		while (mCursor.moveToNext()) {

			boolean islast = mCursor.isLast();
			
			if (mCursor == null || islast == true) {
				break;
			}
			Format df = DateFormat.getDateFormat(this);
			Format tf = DateFormat.getTimeFormat(this);

			int nameIndex = mCursor.getInt(0);
			String name = " ";
			long dateTime = 0;
			
			if (mCursor.isNull(nameIndex)) {
				break;
			}
			name = mCursor.getString(nameIndex);
		    dateTime = mCursor.getLong(1);
			// String dateTimeToStrig = df.format(dateTime);

			String contentToDispay = name + " on " + df.format(dateTime)
					+ " at " + tf.format(dateTime);

			CalendarEvents.add(contentToDispay);
			Collections.sort(CalendarEvents, String.CASE_INSENSITIVE_ORDER);
			
		}

		ListView dropdown = (ListView) findViewById(R.id.listCalendarEvents);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1,
				CalendarEvents);
		dropdown.setAdapter(adapter);
			
					
	}


	public void onClick(View v) {
		TextView tv = (TextView) findViewById(R.id.data);

		String title = "N/A";

		Long start = 0L;

		switch (v.getId()) {
		case R.id.next:
			if (!mCursor.isLast())
				mCursor.moveToNext();
			break;
		case R.id.previous:
			if (!mCursor.isFirst())
				mCursor.moveToPrevious();
			break;
		}

		Format df = DateFormat.getDateFormat(this);
		Format tf = DateFormat.getTimeFormat(this);
		try {
			title = mCursor.getString(0);

			start = mCursor.getLong(1);

		} catch (Exception e) {
			// ignore

		}

		tv.setText(title + " on " + df.format(start) + " at "
				+ tf.format(start));

	}

	private long getTodaysDate() throws ParseException {
		Calendar currentDate = Calendar.getInstance(); // Get the current date
		Format df = DateFormat.getDateFormat(this);
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy"); //("yyyy/MMM/dd HH:mm:ss"); // format it as per your requirement
		String dateNow = formatter.format(currentDate);
		
		Date d = (Date)formatter.parse(dateNow);
		long dateTime = d.getTime();
		
		System.out.println("Now the date is :=>  " + dateNow);
		
		return dateTime;
		
		
	}
}
