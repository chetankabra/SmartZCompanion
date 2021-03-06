package com.example.nispand.smartzcompanion;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class CalEvent extends ActionBarActivity {
    CalendarView calendar;
    FrameLayout frame;
    CalendarContract Calendars;
    TextView tx;
    LinearLayout ll;
    Button Add, Search ,Delete;
    long cday;
    long cmonth;
    long cyear;
    long xyz;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cal_event);
        Add = (Button) findViewById(R.id.Add);
        Search = (Button) findViewById(R.id.Search);
        Delete = (Button)findViewById(R.id.Delete);
        calendar = (CalendarView) findViewById(R.id.calendarView2);
        //frame = (FrameLayout) (findViewById(R.id.frame1));
        ll = (LinearLayout) findViewById(R.id.mylinear);
        ll.setVisibility(View.INVISIBLE);

        //  tx = (TextView) findViewById(R.id.textView4);
        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.setVisibility(View.VISIBLE);
                initializeCalendar();
                add_event();
            }
        });
        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.setVisibility(View.VISIBLE);
                initializeCalendar();
                search_event();
            }
        });
        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.setVisibility(View.VISIBLE);
                initializeCalendar();
                delete();
            }
        });
    }
    public void initializeCalendar() {

        // sets whether to show the week number.
        calendar.setShowWeekNumber(false);

        // sets the first day of week according to Calendar.
        // here we set Monday as the first day of the Calendar
        calendar.setFirstDayOfWeek(2);

        //The background color for the selected week.
        calendar.setSelectedWeekBackgroundColor(getResources().getColor(R.color.green));

        //sets the color for the dates of an unfocused month.
        calendar.setUnfocusedMonthDateColor(getResources().getColor(R.color.transparent));

        //sets the color for the separator line between weeks.
        calendar.setWeekSeparatorLineColor(getResources().getColor(R.color.transparent));

        //sets the color for the vertical bar shown at the beginning and at the end of the selected date.
        calendar.setSelectedDateVerticalBar(R.color.darkgreen);


    }

    public void on_create() {
        Uri calUri = CalendarContract.Calendars.CONTENT_URI;
        ContentValues cv = new ContentValues();
        cv.put(CalendarContract.Calendars.ACCOUNT_NAME, "Chetan Kabra");
        cv.put(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL);
        cv.put(CalendarContract.Calendars.NAME, "CK");
        cv.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, "CK Calendar");
        cv.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER);
        cv.put(CalendarContract.Calendars.OWNER_ACCOUNT, true);
        cv.put(CalendarContract.Calendars.VISIBLE, 1);
        cv.put(CalendarContract.Calendars.SYNC_EVENTS, 1);

        calUri = calUri.buildUpon()
                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, "Chetan Kabra")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL)
                .build();
        Uri result = this.getContentResolver().insert(calUri, cv);
    }
    public void add_event()
    {
        //sets the listener to be notified upon selected date change
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            //show the selected date as a toast
            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
                Toast.makeText(getApplicationContext(), day + "/" + month + "/" + year, Toast.LENGTH_LONG).show();
                Calendar cal = Calendar.getInstance();
                Intent intent = new Intent(Intent.ACTION_INSERT);
                intent.setType("vnd.android.cursor.item/event");
                intent.putExtra("BeginTime", cal.getTimeInMillis());
                intent.putExtra("allDay", true);
                intent.putExtra("Rule", "FREQ=YEARLY");
                intent.putExtra("endTime", cal.getTimeInMillis() + 60 * 60 * 1000);
                intent.putExtra("Title", "Put Title ");
                GregorianCalendar calDate = new GregorianCalendar(year, month, day);
                intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);
                intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, calDate.getTimeInMillis());
                startActivity(intent);
            }
        });
    }
    public void search_event(){

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            //show the selected date as a toast
            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {


                Toast.makeText(getApplicationContext(), day + "/" + month + "/" + year, Toast.LENGTH_LONG).show();
                calendar.setVisibility(View.INVISIBLE);
                ll.setVisibility(View.VISIBLE);
                long date1 = calendar.getDate();
                Date d2 = new Date(date1);
                Intent calsearch = new Intent("android.CalSearch");
                Bundle data = new Bundle();
                System.out.print("First date" + d2);
                String cMonth = (String) android.text.format.DateFormat.format("MM", d2);
                String cyear = (String) android.text.format.DateFormat.format("yyyy", d2);
                String cday = (String) android.text.format.DateFormat.format("dd", d2);

                String[] projection = {CalendarContract.Reminders._ID,
                        CalendarContract.Reminders.TITLE,
                        CalendarContract.Reminders.DTSTART,
                        CalendarContract.Events.EXDATE,
                };
                // Get a Cursor over the Events Provider.
                Cursor cursor = getContentResolver().query(
                        CalendarContract.Events.CONTENT_URI, projection, null, null,
                        null);
                // Get the index of the columns.
                int nameIdx = cursor.getColumnIndexOrThrow(CalendarContract.Reminders.TITLE);
                int idIdx = cursor.getColumnIndexOrThrow(CalendarContract.Reminders._ID);
                int D1 = cursor.getColumnIndexOrThrow(CalendarContract.Reminders.DTSTART);
                int i = 0;

                // Initialize the result set.
                String[] result = new String[cursor.getCount()];
                String[] eventname = new String[cursor.getCount()];
                String[] eventID = new String[cursor.getCount()];
                String id1;
                String nameid;
                // Iterate over the result Cursor.
                while (cursor.moveToNext()) {
                    Date d1 = new Date(cursor.getLong(D1));
                    System.out.print("First date" + d2);
                    String ccMonth = (String) android.text.format.DateFormat.format("MM", d1);
                    String ccyear = (String) android.text.format.DateFormat.format("yyyy", d1);
                    String ccday = (String) android.text.format.DateFormat.format("dd", d1);
                    // Extract the name.
                    String name = cursor.getString(nameIdx);
                    // Extract the unique ID.
                    String id = cursor.getString(idIdx);
                    result[cursor.getPosition()] = name + "(" + id + ")";
                    if (ccMonth.compareTo(cMonth) == 0 & ccday.compareTo(cday) == 0) {
                        LinearLayout lm = new LinearLayout(this);
                        lm.setOrientation(LinearLayout.HORIZONTAL);

                        // Create TextView
                        TextView product = new TextView(this);
                        product.setText(name + "    " + id);
                        lm.addView(product);
                        ll.addView(lm);
                        //tx.setText(name + "(" + id + ")");
                    }
                }
                // Intent calsearch = new Intent("android.CalSearch");
                //  data.putStringArray("eventId",eventID);
                //  data.putStringArray("eventName",eventname);
                //  calsearch.putExtras(data);
                //   startActivity(calsearch);

            }
        });
    }
    public void delete() {

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            //show the selected date as a toast
            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
                Toast.makeText(getApplicationContext(), day + "/" + month + "/" + year, Toast.LENGTH_LONG).show();
                long date1 = calendar.getDate();
                Date d2 = new Date(date1);
                String[] selArgs = new String[]{String.valueOf(d2)};
                // Get a Cursor over the Events Provider.
                ContentResolver cursor = getContentResolver();
                cursor.delete(CalendarContract.CONTENT_URI, CalendarContract.Reminders.DTSTART + "=?", selArgs);
            }
        });
    }
        public boolean onCreateOptionsMenu(Menu menu) {
                // Inflate the menu; this adds items to the action bar if it is present.
                getMenuInflater().inflate(R.menu.menu_cal_event, menu);
                return true;
            }

            @Override
            public boolean onOptionsItemSelected(MenuItem item) {
                // Handle action bar item clicks here. The action bar will
                // automatically handle clicks on the Home/Up button, so long
                // as you specify a parent activity in AndroidManifest.xml.
                int id = item.getItemId();

                //noinspection SimplifiableIfStatement
                if (id == R.id.action_settings) {
                    return true;
                }

                return super.onOptionsItemSelected(item);
            }
        }