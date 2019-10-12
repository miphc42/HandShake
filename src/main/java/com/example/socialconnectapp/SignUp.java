package com.example.socialconnectapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity implements View.OnClickListener {
    EditText password, username;
    private FirebaseAuth mAuth;
    ProgressBar progressBar;
    EditText editText, editText2, editText3, editText4;
    DatabaseReference firebase;
    DatabaseReference firebase2;
    FirebaseUser auth;
    SubClass subClass = new SubClass();
    TextView textView;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        editText = findViewById(R.id.fN);
        editText2 = findViewById(R.id.lN);
        editText3 = findViewById(R.id.lN);
        editText4 = findViewById(R.id.lN);
        textView = findViewById(R.id.textView);
        auth = FirebaseAuth.getInstance().getCurrentUser();
        firebase = FirebaseDatabase.getInstance().getReference().child("FIREBASE DATA");
        firebase2 = FirebaseDatabase.getInstance().getReference().child("TEMP DATA");
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar);
        findViewById(R.id.signup).setOnClickListener(this);
        findViewById(R.id.login).setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();

    }
    private void registerUser(){
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
        mAuth.createUserWithEmailAndPassword(user, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            String email = username.getText().toString().split("\\.")[0]+","+username.getText().toString().split("\\.")[1];
                            String n = editText.getText().toString();
                            String x = editText2.getText().toString();
                            subClass.setFirstName(n);
                            subClass.setLastName(x);
                            firebase.child(email.toLowerCase()).setValue(subClass);
                            firebase2.child(email.toLowerCase()).setValue("");
                            finish();
                            startActivity(new Intent(SignUp.this, main.class));
                            Toast.makeText(SignUp.this, "User Registered", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(getApplicationContext(), main.class));
                            //    updateUI(user);
                        } else {
                            if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                Toast.makeText(SignUp.this, "Email Already Exists", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.signup:
                registerUser();
              //  Toast.makeText(getApplicationContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                break;
            case R.id.login:
                finish();
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }
}
