package com.example.socialconnectapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ProgressBar progressBar;
    static FirebaseAuth Auth;
    EditText password, username;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = getApplicationContext().getSharedPreferences("com.example.login", MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Auth = FirebaseAuth.getInstance();
        // tof = true;
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar);
        findViewById(R.id.signup).setOnClickListener(this);
        findViewById(R.id.login).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(Auth.getCurrentUser()!=null){
           Intent intent = new Intent(getApplicationContext(), main.class);
           startActivity(intent);
        }
    }

    private void userLogin(){
        String user = username.getText().toString().trim();
        String pass = password.getText().toString().trim();
        System.out.println("A");
        if(user.isEmpty()){
            username.setError("Username Required");
            System.out.println("ERRORR");
            username.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(user).matches()){
            username.setError("Enter Valid Email");
            username.requestFocus();
            return;
        }
        if(pass.isEmpty()){
            password.setError("Password Required");
            password.requestFocus();
            return;
        }
        if(pass.length()<6){
            password.setError("Minimum Length: 6");
            password.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        Auth.signInWithEmailAndPassword(user, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                     finish();
                    SharedPreferences.Editor editor = sharedPreferences.edit().putBoolean("Login", true);
                    editor.apply();
                    Intent intent = new Intent(getApplicationContext(), main.class);
                    startActivity(intent);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                }
                else{
                    Toast.makeText(MainActivity.this, "Incorrect Email or Password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void onClick(View view){
        switch(view.getId()) {
            case R.id.signup:
                finish();
                startActivity(new Intent(this, com.example.socialconnectapp.SignUp.class));
                break;
            case R.id.login:
                userLogin();
                break;
        }

    }
}
