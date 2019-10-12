package com.example.socialconnectapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;

public class One extends Fragment {
    static String x;
    String t;
    EditText editText, editText2;
    DatabaseReference firebase;
    View view;
    TextView textView, textView2, textView3;
    Button button;
    FloatingActionButton floatingActionButton;
    ProgressBar progressBar;
    private static final int img = 0;
    NfcAdapter nfcAdapter;
    // Flag to indicate that Android Beam is available
    boolean androidBeamAvailable  = false;
    String email;
    DatabaseReference dataReference;
    FirebaseUser auth;
    Button button2;
    

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = (View) inflater.inflate(R.layout.mainprofile, container, false);
        button = view.findViewById(R.id.button5);
        button2 = view.findViewById(R.id.button9);
        textView = view.findViewById(R.id.textView2);
        textView2 = view.findViewById(R.id.textView3);
        textView3 = view.findViewById(R.id.textView5);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), barcodereader.class));
            }
        });
        dataReference = FirebaseDatabase.getInstance().getReference().child("FIREBASE DATA");
        floatingActionButton = view.findViewById(R.id.floatingActionButton);
        auth = FirebaseAuth.getInstance().getCurrentUser();
        email = auth.getEmail().split("\\.")[0]+","+auth.getEmail().split("\\.")[1];
        String first = dataReference.child(email).child("firstName").toString();
        String last = dataReference.child(email).child("lastName").toString();
        System.out.println(first+last);
        String Age = dataReference.child(email).child("Age").toString();
        String PhoneNumber = dataReference.child(email).child("PhoneNumber").toString();
        if(Age.equals("0")){
            Age = "";
        }
        if(PhoneNumber.equals("0")){
            PhoneNumber = "";
        }
        FirebaseDatabase.getInstance().getReference().child("FIREBASE DATA").child(email)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        System.out.println("BEFORE");
                        System.out.println("DATABASE:"+dataSnapshot.toString());
                        t ="";
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            if(snapshot.getKey().equals("age")){
                                textView2.setText(snapshot.getValue().toString());
                            }
                            else if(snapshot.getKey().equals("phoneNumber")){
                                textView3.setText(snapshot.getValue().toString());
                            }
                            else if(snapshot.getKey().equals("firstName")||snapshot.getKey().equals("lastName")) {
                                t += " " + snapshot.getValue();
                            }
                            //System.out.println(snapshot.getKey());
                        }
                        System.out.println(t);
                        textView.setText(t);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), EditProfile.class));
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), socialmedias.class));
            }
        });
        return view;
    }
}
