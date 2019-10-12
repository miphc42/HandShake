package com.example.socialconnectapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditProfile extends AppCompatActivity {
    EditText editText, editText2, editText3, editText4;
    static String x;
    static String t;
    DatabaseReference firebase;
    FirebaseUser auth;
    SubClass subClass = new SubClass();
    TextView textView;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        editText = findViewById(R.id.editText);
        editText2 = findViewById(R.id.editText2);
        editText3 = findViewById(R.id.editText4);
        editText4 = findViewById(R.id.editText5);
        Button button  = findViewById(R.id.button4);
        auth = FirebaseAuth.getInstance().getCurrentUser();
        firebase = FirebaseDatabase.getInstance().getReference().child("FIREBASE DATA");
        email = auth.getEmail().split("\\.")[0]+","+auth.getEmail().split("\\.")[1];
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = auth.getEmail().split("\\.")[0]+","+auth.getEmail().split("\\.")[1];
                String n = editText.getText().toString();
                String x = editText2.getText().toString();
                String l = editText3.getText().toString();
                String r = editText4.getText().toString();
                //  System.out.println(n);
                subClass.setFirstName(x);
                subClass.setLastName(n);
                subClass.setAge(Integer.parseInt(l));
                subClass.setPhoneNumber(Integer.parseInt(r));
                firebase.child(email).setValue(subClass);
                Toast.makeText(getApplicationContext(), "Uploaded", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
