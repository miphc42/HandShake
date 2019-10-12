package com.example.socialconnectapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class socialmedias extends AppCompatActivity {
    Button button;
   static String currentMedia;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socialmedias);
        button = findViewById(R.id.button8);
        currentMedia = "";
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentMedia = "Other";
                finish();

            }
    });
    }
    public void clicked(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(socialmedias.this);
        builder.setTitle("Title");

        EditText input = new EditText(getApplicationContext());
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.width = 150;
        lp.height = 500;
        lp.x=-170;
        lp.y=100;
        input.setLayoutParams(lp);
        input.setWidth(10);
        builder.setView(input);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }
}
