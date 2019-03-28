package com.example.notificationapp;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EventActivity extends AppCompatActivity {

    private static final String EVENT_URL = "https://usiuflyers.000webhostapp.com/addevent.php";
    private static final String UPDATE_EVENT_URL = "https://usiuflyers.000webhostapp.com/eventupdate.php";
    EditText editTextEventName, editTextVenue,editTextUserGroup, editTextDate, editTextTime;
    private Pattern pattern;
    private Matcher matcher;
    private Calendar myCalendar = Calendar.getInstance();

    TimePickerDialog timePickerDialog;
    Calendar calendar;
    int currentHour;
    int currentMinute;
    String amPm;

    private static final String DATE_PATTERN =
            "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[13-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$";

    Button buttonCreateEvent, buttonModifyEvent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();
        editTextVenue=(EditText)findViewById(R.id.editTextVenue);
        editTextEventName=(EditText)findViewById(R.id.editTextEventName);
        editTextDate=(EditText)findViewById(R.id.editTextDate);
        editTextTime=(EditText)findViewById(R.id.editTextTime);
        editTextUserGroup=(EditText)findViewById(R.id.editTextUserGroup);
        buttonCreateEvent=(Button)findViewById(R.id.buttonCreateEvent);
        buttonModifyEvent=(Button)findViewById(R.id.buttonModifyEvent);



        final DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "dd/MM/yy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                editTextDate.setText(sdf.format(myCalendar.getTime()));
            }

        };
        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new DatePickerDialog(EventActivity.this, datePickerListener, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });





            editTextTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    calendar = Calendar.getInstance();
                    currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                    currentMinute = calendar.get(Calendar.MINUTE);

                    timePickerDialog = new TimePickerDialog(EventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                            if (hourOfDay >= 12) {
                                amPm = "PM";
                            } else {
                                amPm = "AM";
                            }
                            editTextTime.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);
                        }
                    }, currentHour, currentMinute, false);

                    timePickerDialog.show();
                }
            });



        buttonCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matcher = Pattern.compile(DATE_PATTERN).matcher(editTextDate.getText());
                if (matcher.matches()) {
                    addEvent();
                }
                else if (!matcher.matches()) {
                    Toast.makeText(getApplicationContext(), "Invalid Date! please enter using dd.mm.yyyy format", Toast.LENGTH_LONG).show();
                }
            }
        });

        buttonModifyEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmUpdateEntry();

            }
        });


    }
    private void addEvent() {
        String name = editTextEventName.getText().toString().trim();
        String usergroup = editTextUserGroup.getText().toString().trim();
        String venue= editTextVenue.getText().toString().trim();
        String date= editTextDate.getText().toString().trim();
        String time= editTextTime.getText().toString().trim();


        Event(name,venue,usergroup,date,time);
    }

    private void Event(String name,String venue,String usergroup,String date, String time) {
        class EventClass extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            RegisterUserClass ruc = new RegisterUserClass();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(EventActivity.this, "Adding new Event", null, true, true);

            }

            @Override
            protected void onPostExecute(String response) {
                super.onPostExecute(response);
                loading.dismiss();

                if (response.equals("")){
                    Toast.makeText(EventActivity.this,"Check your Network connection", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(EventActivity.this,response, Toast.LENGTH_LONG).show();
                }

            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String, String> data = new HashMap<>();
                data.put("name", params[0]);
                data.put("venue", params[1]);
                data.put("usergroup",params[2]);
                data.put("date",params[3]);
                data.put("time",params[4]);

                String result = ruc.sendPostRequest(EVENT_URL, data);

                return result;
            }
        }
        EventClass ec = new EventClass();
        ec.execute(name,venue,usergroup,date,time);
    }

    private void updateevententries() {
        String name = editTextEventName.getText().toString().trim();
        String venue = editTextVenue.getText().toString().trim();
        String usergroup = editTextUserGroup.getText().toString().trim();
        String date =editTextDate.getText().toString().trim();
        String time =editTextTime.getText().toString().trim();
        //spinnerPickGroup.getSelectedItem().toString().trim();
        UpdateEvent(name,venue,usergroup,date,time);

    }

    private void UpdateEvent(String name,String venue, String usergroup, String date, String time) {
        class UpdateEventClass extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            RegisterUserClass ruc = new RegisterUserClass();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(EventActivity.this, "Updating entry", null, true, true);

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if (s.equals("")){
                    Toast.makeText(EventActivity.this,"Check your Network connection", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(EventActivity.this,s, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String, String> data = new HashMap<>();
                data.put("name", params[0]);
                data.put("venue", params[1]);
                data.put("usergroup",params[2]);
                data.put("date",params[3]);
                data.put("time",params[4]);

                String result = ruc.sendPostRequest(UPDATE_EVENT_URL, data);

                return result;
            }
        }
        UpdateEventClass dc = new UpdateEventClass();
        dc.execute(name,venue,usergroup,date,time);
    }

    private void confirmUpdateEntry(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to update this entry?");

        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        updateevententries();

                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public boolean validate(final String date){

        matcher = pattern.matcher(date);

        if(matcher.matches()){
            matcher.reset();

            if(matcher.find()){
                String day = matcher.group(1);
                String month = matcher.group(2);
                int year = Integer.parseInt(matcher.group(3));

                if (day.equals("31") &&
                        (month.equals("4") || month .equals("6") || month.equals("9") ||
                                month.equals("11") || month.equals("04") || month .equals("06") ||
                                month.equals("09"))) {
                    return false; // only 1,3,5,7,8,10,12 has 31 days
                }

                else if (month.equals("2") || month.equals("02")) {
                    //leap year
                    if(year % 4==0){
                        if(day.equals("30") || day.equals("31")){
                            return false;
                        }
                        else{
                            return true;
                        }
                    }
                    else{
                        if(day.equals("29")||day.equals("30")||day.equals("31")){
                            return false;
                        }
                        else{
                            return true;
                        }
                    }
                }

                else{
                    return true;
                }
            }

            else{
                return false;
            }
        }
        else{
            return false;
        }
    }

    @Override
    public void onBackPressed() {

        Intent intent=new Intent(EventActivity.this,AdminLandingPage.class);
        startActivity(intent);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
    }
}
