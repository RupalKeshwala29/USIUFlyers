package com.example.notificationapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.HashMap;

public class ModifyUserActivity extends AppCompatActivity{

   private static final String URL_DELETE = "https://usiuflyers.000webhostapp.com/deleteuser.php?email=";
    private static final String UPDATE_USER_URL = "https://usiuflyers.000webhostapp.com/updateuser.php";
    private EditText editTextEmail, editTextUsergroup, editTextName, editTextPassword;
    private Button buttonRemoveUser, buttonModifyUser;
    private Spinner PickGroup;
    String emailPattern = "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_user);
        buttonRemoveUser=(Button) findViewById(R.id.buttonRemoveUser);
        buttonModifyUser=(Button) findViewById(R.id.buttonModifyUser);
        editTextEmail=(EditText)findViewById(R.id.editTextEmail);
        editTextUsergroup=(EditText)findViewById(R.id.editTextUsergroup);
        editTextName=(EditText)findViewById(R.id.editTextName);
        editTextPassword=(EditText)findViewById(R.id.editTextPassword);
        buttonRemoveUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = editTextEmail.getText().toString().trim();
                if (email.matches(emailPattern))
                {
                    confirmDeleteEntry();
                }else {

                    Toast.makeText(getApplicationContext(),"Invalid email address", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonModifyUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString().trim();
                if (email.matches(emailPattern))
                {
                    confirmUpdateEntry();
                }else {

                    Toast.makeText(getApplicationContext(),"Invalid email address", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }



    private void deleteEntry(){
        final String email=editTextEmail.getText().toString().trim().toLowerCase();

        class DeleteEntry extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ModifyUserActivity.this, "Deleting...", "Wait...", true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if (s.equals("")){
                    Toast.makeText(ModifyUserActivity.this,"Check your Network connection", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(ModifyUserActivity.this,s, Toast.LENGTH_LONG).show();
                }

            }

            @Override
            protected String doInBackground(Void... params) {
                RegisterUserClass ruc= new RegisterUserClass();
                String s = ruc.sendGetRequestParam(URL_DELETE,email);
                return s;
            }
        }

        DeleteEntry de = new DeleteEntry();
        de.execute();
        editTextEmail.setText("");
    }

    private void confirmDeleteEntry(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to remove this user?");

        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        deleteEntry();

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


    private void updateuserentries() {
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String usergroup =editTextUsergroup.getText().toString().trim();
        //spinnerPickGroup.getSelectedItem().toString().trim();
        UpdateUser(name,email,usergroup,password);

    }

    private void UpdateUser(String name,String email, String usergroup, String password) {
        class UpdateUserClass extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            RegisterUserClass ruc = new RegisterUserClass();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ModifyUserActivity.this, "Updating entry", null, true, true);

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if (s.equals("")){
                    Toast.makeText(ModifyUserActivity.this,"Check your Network connection", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(ModifyUserActivity.this,s, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String, String> data = new HashMap<>();
                data.put("name", params[0]);
                data.put("email", params[1]);
                data.put("usergroup",params[2]);
                data.put("password",params[3]);

                String result = ruc.sendPostRequest(UPDATE_USER_URL, data);

                return result;
            }
        }
        UpdateUserClass dc = new UpdateUserClass();
        dc.execute(name,email,usergroup,password);
    }

    private void confirmUpdateEntry(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to update this entry?");

        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        updateuserentries();

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

        Intent intent=new Intent(ModifyUserActivity.this,AdminLandingPage.class);
        startActivity(intent);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
    }
}
