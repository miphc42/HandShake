package com.example.socialconnectapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Two extends Fragment {
    View view;
    String email;
    Button button;
    FloatingActionButton floatingActionButton;
    DatabaseReference firebase;
    DatabaseReference firebase2, firebase3;
    ListView listView;
    ArrayList<String> list;
    FirebaseUser auth;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.contacts, container, false);
        listView = view.findViewById(R.id.listView);
        list = new ArrayList<>();
        auth = FirebaseAuth.getInstance().getCurrentUser();
        email = auth.getEmail().split("\\.")[0]+","+auth.getEmail().split("\\.")[1];
        FirebaseDatabase.getInstance().getReference().child("FIREBASE DATA").child(email).child("Contacts")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            System.out.println(snapshot.getKey());
                            //String fn = firebase.child(snapshot.getKey()).child("firstName").toString();
                           // String ln = firebase.child(snapshot.getKey()).child("lastName").toString();
                            list.add(snapshot.getKey());
                            System.out.println(list);
                            }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
       ArrayAdapter adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, list);
        try {
          listView.setAdapter(adapter);
        }catch (Exception e){
            e.printStackTrace();
        }
        return view;




    }
}
