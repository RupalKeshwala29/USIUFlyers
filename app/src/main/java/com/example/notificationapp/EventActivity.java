package com.example.notificationapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.HashMap;

public class EventActivity extends AppCompatActivity {

    private static final String EVENT_URL = "https://usiuflyers.000webhostapp.com/addevent.php";
    private static final String UPDATE_EVENT_URL = "https://usiuflyers.000webhostapp.com/eventupdate.php";
    EditText editTextEventName, editTextVenue,editTextUserGroup, editTextDate, editTextTime;

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

        buttonCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addEvent();
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

    @Override
    public void onBackPressed() {

        Intent intent=new Intent(EventActivity.this,AdminLandingPage.class);
        startActivity(intent);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
    }
}
