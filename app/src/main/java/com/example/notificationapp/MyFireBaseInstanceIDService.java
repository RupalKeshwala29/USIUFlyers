package com.example.notificationapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.iid.InstanceIdResult;

/*
public class MyFireBaseInstanceIDService extends FirebaseInstanceIdService {


@Override
    public void onTokenRefresh(){
    String refreshedToken=FirebaseInstanceId.getInstance().getToken();
    sendRegistrationToServer(refreshedToken);

}

    private void sendRegistrationToServer(String token) {
    }

}
*/
public class MyFireBaseInstanceIDService extends AppCompatActivity {
private static final String TAG = "MyFireBaseInstanceIDService";

@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseInstanceId.getInstance().getInstanceId()
        .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
@SuppressLint("LongLogTag")
@Override
public void onComplete(@NonNull Task<InstanceIdResult> task) {
        if (!task.isSuccessful()) {
//To do//
        return;
        }

// Get the Instance ID token//
        String token = task.getResult().getToken();
        String msg = getString(R.string.fcm_token, token);
        Log.d(TAG, msg);


        }
        });

        }
        }