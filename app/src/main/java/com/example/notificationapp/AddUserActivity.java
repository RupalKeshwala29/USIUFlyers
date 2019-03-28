package com.example.notificationapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.HashMap;

public class AddUserActivity extends AppCompatActivity {
    private static final String USER_URL = "https://usiuflyers.000webhostapp.com/users.php";
    EditText editTextName, editTextEmail, editTextPassword, editTextUsergroup;
    //Spinner spinnerPickGroup;
    Button buttonRegister, buttonLogin;
    String emailPattern = "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$";
    //String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+\\.+[a-z]";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        ActionBar actionBar=getSupportActionBar();
        actionBar.hide();
        editTextName=(EditText)findViewById(R.id.editTextName);
        editTextEmail=(EditText)findViewById(R.id.editTextEmail);
        editTextPassword=(EditText)findViewById(R.id.editTextPassword);
        editTextUsergroup=(EditText)findViewById(R.id.editTextUsergroup);
        //spinnerPickGroup=(Spinner)findViewById(R.id.spinnerPickGroup);
        buttonRegister=(Button)findViewById(R.id.buttonRegister);
        buttonLogin=(Button)findViewById(R.id.buttonLogin);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                if (email.matches(emailPattern))
                {
                addUser();
                }else {

                    Toast.makeText(getApplicationContext(),"Invalid email address", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddUserActivity.this,LoginActivity.class);

                startActivity(intent);

            }
        });


    }
    private void addUser() {
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String usergroup =editTextUsergroup.getText().toString().trim();
                //spinnerPickGroup.getSelectedItem().toString().trim();
        User(name,email,usergroup,password);
    }

    private void User(String name,String email,String usergroup,String password) {
        class UserClass extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            RegisterUserClass ruc = new RegisterUserClass();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(AddUserActivity.this, "Adding new user", null, true, true);

            }

            @Override
            protected void onPostExecute(String response) {
                super.onPostExecute(response);
                loading.dismiss();

                if (response.equals("")){
                    Toast.makeText(AddUserActivity.this,"Check your Network connection", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(AddUserActivity.this,response, Toast.LENGTH_LONG).show();
                }

            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String, String> data = new HashMap<>();
                data.put("name", params[0]);
                data.put("email", params[1]);
                data.put("usergroup",params[2]);
                data.put("password",params[3]);

                String result = ruc.sendPostRequest(USER_URL, data);

                return result;
            }
        }
        UserClass uc = new UserClass();
        uc.execute(name,email,usergroup,password);
    }


}
