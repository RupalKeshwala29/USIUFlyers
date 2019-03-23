package com.example.notificationapp;

import android.app.ProgressDialog;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ListView extends AppCompatActivity {

    public static final String DATA_URL = "https://usiuflyers.000webhostapp.com/select.php?email=";
    public static final String KEY_GROUPVALUE = "usergroup";

    public static final String JSON_ARRAY = "result";
    //public static  String usergroup;

    String urladdress="https://usiuflyers.000webhostapp.com/displayevents.php?usergroup=";
    String[] name;
    String[] venue;
    String[] date;
    String[] time;
    String[] imagepath;
    android.widget.ListView listView;
    BufferedInputStream is;
    String line=null;
    String result=null;
    EditText ETgroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        listView=(android.widget.ListView)findViewById(R.id.lview);
        ETgroup=(EditText)findViewById(R.id.ETgroup);

        //String email="user2@usiu.ac.ke";
        String email=getIntent().getStringExtra("email");

        Toast.makeText(this,"Welcome "+email,Toast.LENGTH_LONG).show();

        getData();
        //SharedPreferences prefs = getSharedPreferences("groupinfo", Context.MODE_PRIVATE);
        //String usergroup=prefs.getString("usergroup","");
        //ETgroup.setText(usergroup);

        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));
        collectData();
        CustomListView customListView=new CustomListView(this,name,venue,date,time,imagepath);
        listView.setAdapter(customListView);
    }

    public void collectData()
    {
//Connection
        try{


            URL url=new URL(urladdress+usergroup);
            HttpURLConnection con=(HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            is=new BufferedInputStream(con.getInputStream());

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        //content
        try{
            BufferedReader br=new BufferedReader(new InputStreamReader(is));
            StringBuilder sb=new StringBuilder();
            while ((line=br.readLine())!=null){
                sb.append(line+"\n");
            }
            is.close();
            result=sb.toString();

        }
        catch (Exception ex)
        {
            ex.printStackTrace();

        }

//JSON
        try{
            JSONArray ja=new JSONArray(result);
            JSONObject jo=null;
            name=new String[ja.length()];
            venue=new String[ja.length()];
            date=new String[ja.length()];
            time=new String[ja.length()];
            imagepath=new String[ja.length()];

            for(int i=0;i<=ja.length();i++){
                jo=ja.getJSONObject(i);
                name[i]=jo.getString("name");
                venue[i]=jo.getString("venue");
                date[i]=jo.getString("date");
                time[i]=jo.getString("time");
                imagepath[i]=jo.getString("photo");
            }
        }
        catch (Exception ex)
        {

            ex.printStackTrace();
        }


    }

    private ProgressDialog loading;

    private void getData() {
        String email=getIntent().getStringExtra("email").trim();
        //String email="user2@usiu.ac.ke".toString().trim();


        if (email.equals("")) {
            Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
            return;
        }
        loading = ProgressDialog.show(this,"Please wait...","Fetching...",false,false);

        String url = DATA_URL+email.trim();

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                showJSON(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ListView.this,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public static String usergroup;
    public static void showJSON(String response){



        try {

            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(JSON_ARRAY);
            JSONObject collegeData = result.getJSONObject(0);
            usergroup = collegeData.getString(KEY_GROUPVALUE);




            //SharedPreferences prefs = getSharedPreferences("groupinfo", Context.MODE_PRIVATE);
            //SharedPreferences.Editor editor=prefs.edit();
            //editor.putString("usergroup",usergroup);
            //editor.apply();




        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Toast.makeText(this,usergroup,Toast.LENGTH_LONG).show();
        //return usergroup;

    }
//public String xyz=usergroup;

}
