package com.seniorproject.myassistant;

import java.sql.Date;
import java.text.Format;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

import android.R.bool;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class CalendarSyncAdapterActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar_syncadapter);
		Button qry = (Button) findViewById(R.id.querybut);
	//	ListView dropdown = (ListView) findViewById(R.id.listCalendarEvents);
		query();

		qry.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// query();
			}
		});
	}

	public void query() {
		String[] projection = new String[] {
				CalendarContract.Events.CALENDAR_ID,
				CalendarContract.Events.TITLE,
				CalendarContract.Events.DESCRIPTION,
				CalendarContract.Events.DTSTART, CalendarContract.Events.DTEND,
				CalendarContract.Events.ALL_DAY,
				CalendarContract.Events.EVENT_LOCATION };

		// 0 = January, 1 = February, ...

		Calendar startTime = Calendar.getInstance();
		startTime.set(2014, 00, 01, 00, 00);

		Calendar endTime = Calendar.getInstance();
		endTime.set(2015, 00, 01, 00, 00);

		// the range is all data from 2014

		String selection = "(( " + CalendarContract.Events.DTSTART + " >= "
				+ startTime.getTimeInMillis() + " ) AND ( "
				+ CalendarContract.Events.DTSTART + " <= "
				+ endTime.getTimeInMillis() + " ))";

		Cursor cursor = this
				.getBaseContext()
				.getContentResolver()
				.query(CalendarContract.Events.CONTENT_URI, projection,
						selection, null, null);

		// output the events

		ArrayList<String> CalendarEvents = new ArrayList<String>();

		while (cursor.moveToNext()) {

			boolean islast = cursor.isLast();
			if (cursor == null || islast == true) {
				break;
			}
			Format df = DateFormat.getDateFormat(this);
			Format tf = DateFormat.getTimeFormat(this);

			int nameIndex = cursor.getInt(0);
			String name = " ";
			long dateTime = 0;
			
			if (cursor.isNull(nameIndex)) {
				break;
			}
			name = cursor.getString(nameIndex);
		    dateTime = cursor.getLong(1);
			// String dateTimeToStrig = df.format(dateTime);

			String contentToDispay = name + " on " + df.format(dateTime)
					+ " at " + tf.format(dateTime);

			CalendarEvents.add(contentToDispay);
		}

		ListView dropdown = (ListView) findViewById(R.id.listCalendarEvents);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1,
				CalendarEvents);
		dropdown.setAdapter(adapter);

		if (cursor.moveToFirst()) {
			do {
				Toast.makeText(
						this.getApplicationContext(),
						"Title: " + cursor.getString(1) + " Start-Time: "
								+ (new Date(cursor.getLong(3))).toString(),
						Toast.LENGTH_LONG).show();
			} while (cursor.moveToNext());
		}

		// Close the Cursor.
		cursor.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}