package com.example.notificationapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{


    private static final String LOGIN_URL = "https://usiuflyers.000webhostapp.com/login.php";
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLogin,buttonRegister;
    SharedPreferences logindb;
    SharedPreferences.Editor logineditor;
    private String TAG = LoginActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();

        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);
        buttonLogin = (Button) findViewById(R.id.buttonUserLogin);

        logindb=getSharedPreferences("db1",MODE_PRIVATE);
        logineditor=logindb.edit();


        buttonLogin.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        editTextEmail.setText(logindb.getString("text", ""));
    }

    @Override
    protected void onResume() {
        super.onResume();
        editTextEmail.setText(logindb.getString("text",""));
    }

    private void login(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        userLogin(email,password);
        logineditor.putString("text", String.valueOf(email));
        logineditor.commit();
    }

    private void userLogin(final String email, final String password){
        class UserLoginClass extends AsyncTask<String,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(LoginActivity.this,"Please Wait",null,true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Log.d(TAG,"@onPostExecute s "+s);
                if(s.contains("success")){
                    Intent intent = new Intent(LoginActivity.this,ListView.class);
                    intent.putExtra("email",email);
                    startActivity(intent);
                    Toast.makeText(LoginActivity.this,s, Toast.LENGTH_LONG).show();
                    editTextEmail.setText("");
                    editTextPassword.setText("");
                    if(s.contains("admin")){
                        Intent intentadmin = new Intent(LoginActivity.this,AdminLandingPage.class);
                        intentadmin.putExtra("email",email);
                        startActivity(intentadmin);
                        Toast.makeText(LoginActivity.this,s, Toast.LENGTH_LONG).show();
                        editTextEmail.setText("");
                        editTextPassword.setText("");
                    }
                }if(s.contains("Invalid Email or Password")){
                    Toast.makeText(LoginActivity.this,s, Toast.LENGTH_LONG).show();
                }
                if(s.equals("")){
                    Toast.makeText(LoginActivity.this,"Check your Network Connection!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String,String> data = new HashMap<>();
                data.put("email",params[0]);
                data.put("password",params[1]);

                RegisterUserClass ruc = new RegisterUserClass();

                String result = ruc.sendPostRequest(LOGIN_URL,data);

                return result;
            }
        }
        UserLoginClass ulc = new UserLoginClass();
        ulc.execute(email,password);
    }

    @Override
    public void onClick(View v) {
        if(v == buttonLogin){
            login();
        }
    }

    private Boolean exit = false;
    @Override
    public void onBackPressed() {
        if (exit) {
            finish();

        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }

    }
}
