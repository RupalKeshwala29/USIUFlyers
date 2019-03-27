package com.example.notificationapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Attendance extends AppCompatActivity {
    TextView eventNameTV,emailTV;
    ToggleButton toggle;
    TextView statusTV;

    private static final String UPDATE_DB_URL = "https://usiuflyers.000webhostapp.com/update.php";
    public static final String DATA_URL = "https://usiuflyers.000webhostapp.com/search.php?email=";
    public static final String JSON_ARRAY = "result";
    public static final String KEY_STATUS = "status";
    public String TAG = ListView.class.getSimpleName();
    String status="";
    String statusresult="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        eventNameTV=(TextView) findViewById(R.id.eventNameTV);
  //      emailTV=(TextView) findViewById(R.id.emailTV);
        toggle = (ToggleButton) findViewById(R.id.toggleButton);
        statusTV=(TextView) findViewById(R.id.statusTV);

       final String eventName=getIntent().getStringExtra("eventName");
       final String eventVenue=getIntent().getStringExtra("eventVenue");
       final String email=getIntent().getStringExtra("email");
        eventNameTV.setText(eventName);
   //     emailTV.setText(email);

        getData();


        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    statusTV.setText("You are attending the event. Tap to Leave event");
                    status="Attend";
                    UpdateStatus(email,status,eventName,eventVenue);
                } else {
                    // The toggle is disabled

                    status="Not Attend";
                    statusTV.setText("You are not in the list. Tap if attend event");
                    UpdateStatus(email,status,eventName,eventVenue);
                }
            }
        });
    }

    private void UpdateStatus(String email,String status,String eventName, String eventVenue) {
        class UpdateStatusClass extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            RegisterUserClass ruc = new RegisterUserClass();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Attendance.this, "Updating entry", null, true, true);

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if (s.equals("")){
                    Toast.makeText(Attendance.this,"Check your Network connection", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(Attendance.this,s, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String, String> data = new HashMap<>();
                data.put("email", params[0]);
                data.put("status", params[1]);
                data.put("eventName",params[2]);
                data.put("eventVenue",params[3]);


                String result = ruc.sendPostRequest(UPDATE_DB_URL, data);

                return result;
            }
        }
        UpdateStatusClass dc = new UpdateStatusClass();
        dc.execute(email,status,eventName,eventVenue);
    }

    private ProgressDialog loading;
    private void getData() {

        final String eventName=getIntent().getStringExtra("eventName");
        final String eventVenue=getIntent().getStringExtra("eventVenue");
        final String email=getIntent().getStringExtra("email");

        loading = ProgressDialog.show(this,"Please wait...","Fetching...",false,false);
        String url1 = DATA_URL+email+"&eventName="+eventName;



        StringRequest stringRequest = new StringRequest(url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                showJSON(response.toString());

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Attendance.this,error.getMessage(),Toast.LENGTH_LONG).show();
                        loading.dismiss();
                    }
                });


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void showJSON(String response){


        try {

            JSONObject jsonObject = new JSONObject(response.toString());
            JSONArray result = jsonObject.getJSONArray(JSON_ARRAY);
            JSONObject collegeData = result.getJSONObject(0);
            statusresult = collegeData.getString(KEY_STATUS);
            Log.d(TAG, "@status " + statusresult);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (statusresult.equals("Attend")) {
            toggle.setChecked(true);
            statusTV.setText("You are attending the event. Tap to Leave event");
        } else{
            toggle.setChecked(false);
            statusTV.setText("You are not in the list. Tap if attend event");
        }


    }


    @Override
    public void onBackPressed() {
        String email=getIntent().getStringExtra("email");
        Intent intent=new Intent(Attendance.this,ListView.class);
        intent.putExtra("email", email );
        startActivity(intent);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
    }
}


