package com.example.socialconnectapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Locale;

public class main extends AppCompatActivity {
    public BottomNavigationView bottomNavigationView;
    public FrameLayout frameLayout;
    Fragment fragment;
    ArrayList<String> arrayList;
    String identity;
    NfcAdapter nfcAdapter;
    boolean androidBeamAvailable  = false;
    ToggleButton toggleButton;
    EditText textView;
    TextView textView2;
    String[] array;
    SubClass subClass = new SubClass();
    EditText editText, editText2;
    DatabaseReference firebase;
    DatabaseReference firebase2, firebase3;
    FirebaseUser auth;
    String email;
    View view;
    String NOW;
    String NOW2;
    DatabaseReference mDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        bottomNavigationView = findViewById(R.id.main_bar);
        array = new String[2];
        frameLayout = findViewById(R.id.framerLayout);
        //toggleButton = findViewById(R.id.toggleButton);
        textView = findViewById(R.id.editText3);
        textView2 = findViewById(R.id.textView4);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        mDb = mDatabase.getReference();
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_NFC)) {
            Toast.makeText(getApplicationContext(), "Not Available", Toast.LENGTH_SHORT).show();
            /*
             * Disable NFC features here.
             * For example, disable menu items or buttons that activate
             * NFC-related features
             */
            // Android Beam file transfer isn't supported
        } else {
            androidBeamAvailable = true;
            nfcAdapter = NfcAdapter.getDefaultAdapter(getApplicationContext());
            Toast.makeText(getApplicationContext(), "Available", Toast.LENGTH_SHORT).show();
        }
        auth = FirebaseAuth.getInstance().getCurrentUser();
        firebase = FirebaseDatabase.getInstance().getReference().child("FIREBASE DATA");
        firebase2 = FirebaseDatabase.getInstance().getReference().child("TEMP DATA");
        firebase3 = FirebaseDatabase.getInstance().getReference().child("TRANSFER DATA");
        email = auth.getEmail().split("\\.")[0]+","+auth.getEmail().split("\\.")[1];
        //FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        mDb = mDatabase.getReference();
        getSupportFragmentManager().beginTransaction().replace(R.id.framerLayout,
                new One()).commit();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.one:
                        Drawable drawable = menuItem.getIcon();
                        drawable.mutate();
                        drawable.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_ATOP);
                       // menuItem.getIcon().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
                        //  bottomNavigationView.setItemBackground(R.color.colorAccent);
                        getSupportFragmentManager().beginTransaction().replace(R.id.framerLayout,
                                new One()).commit();
                        return true;
                    case R.id.two:
                        getSupportFragmentManager().beginTransaction().replace(R.id.framerLayout,
                                new Two()).commit();
                        return true;
                    case R.id.three:
                        getSupportFragmentManager().beginTransaction().replace(R.id.framerLayout,
                                new Three()).commit();
                        return  true;
                    default:
                        return false;
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        enableForegroundDispatchSystem();

    }
    @Override
    protected void onPause() {
        super.onPause();
        disableForegroundDispatchSystem();
    }
    public void disableForegroundDispatchSystem(){
        nfcAdapter.disableForegroundDispatch(this);
    }
    public void enableForegroundDispatchSystem(){
        Intent intent = new Intent(this, main.class);
        intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        IntentFilter[] intentFilters = new IntentFilter[]{};
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, null);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        FirebaseDatabase.getInstance().getReference("TEMP DATA").child(email).setValue("1");
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = mFirebaseDatabase.getReference("TEMP DATA");
        FirebaseDatabase.getInstance().getReference().child("TEMP DATA")
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("BEFORE");
                System.out.println("DATABASE:"+dataSnapshot.toString());
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if (snapshot.getValue().equals("1")) {
                       identity = snapshot.getKey();
                        System.out.println("VALUE:" + snapshot.getValue());
                    }
                    //System.out.println(snapshot.getKey());
                }
             //   System.out.println("KEYY:"+identity);
               // firebase.child(identity.toLowerCase()).child("Contacts").child(identity).setValue("");
                firebase3.child(identity).setValue("");


                    /*
                   // System.out.println(dataSnapshot.getValue(getKey().));
                    if (!dataSnapshot.getChildren().equals("")) {
                        if (!dataSnapshot.getChildren().equals(email)) {
                            System.out.println(dataSnapshot.getChildren());
                        }
                    }
                }
                */
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        FirebaseDatabase.getInstance().getReference().child("TEMP DATA")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            firebase2.child(snapshot.getKey()).setValue("");
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        arrayList = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("TRANSFER DATA")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int i = 0;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            System.out.println("KEY:"+snapshot.getKey());
                            System.out.println("VALUE:"+snapshot.getValue());
                            arrayList.add(snapshot.getKey());
                            System.out.println(arrayList);
                            System.out.println(i);
                            i++;

                        }
                      //  System.out.println("ARRAY:"+Arrays.toString(array));
                        if(i==2) {
                           firebase.child(arrayList.get(0)).child("Contacts").child(arrayList.get(1)).setValue("");
                           firebase.child(arrayList.get(1)).child("Contacts").child(arrayList.get(0)).setValue("");
                           firebase3.setValue("");

                            }
                        arrayList.clear();
                        }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });




       // final String userName = unameEditText.getText().toString();
        /*
        mDb.child("TEMP DATA").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot userChild : dataSnapshot.getChildren()){
                            DataSnapshot email = userChild.child("email");
                            String emailString = email.getValue(String.class);
                        }
                        System.out.println(dataSnapshot.toString());
                        if(dataSnapshot.hasChild("1@gmail,com")) {
                            System.out.println("WORKS");
                            String email2 = "2@gmail,com";
                            firebase.child(email2.toLowerCase()).setValue(One.t);
                        }else{
                            String email = "1@gmail,com";
                            System.out.println("ELSE");
                            firebase.child(email.toLowerCase()).setValue(One.t);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });*/
        /*
            if (toggleButton.isChecked()) {
                System.out.println("Read");
                Toast.makeText(this, "NFC Received", Toast.LENGTH_SHORT).show();
                Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                System.out.println("TAG: "+tag);
                NdefMessage ndefMessage = createNdefMessage(textView.getText().toString()+"");
                System.out.println("MESSSAGE "+ ndefMessage);
                String email = "2@gmail,com";
                firebase.child(email.toLowerCase()).setValue(textView.getText().toString());
                Toast.makeText(getApplicationContext(), "UPLOADED TO NFC", Toast.LENGTH_SHORT).show();
                mDb.child("TEMP DATA").child("2@gmail,com").addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot!=null) {
                                    System.out.println("WRITE");
                                    String x = dataSnapshot.getValue().toString();
                                    NOW = x;
                                    System.out.println("NOW:"+NOW);
                                    textView2.setText(NOW+"\t");
                                }gg
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                String n = "A";
                String x = "B";
                subClass.setFirstName(n);
                subClass.setLastName(x);

                if (parcelables != null && parcelables.length > 0) {
                    readTextFromTag((NdefMessage) parcelables[0]);
                } else {
                    Toast.makeText(this, "NO NFC", Toast.LENGTH_SHORT).show();
                }
            }else{
                textView2.setText("WORKS");
                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                System.out.println("TAG: "+tag);
                NdefMessage ndefMessage = createNdefMessage(textView.getText().toString()+"");
                System.out.println("MESSSAGE "+ ndefMessage);
                String email = "1@gmail,com";
                Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
                /*
                String n = "A";
                String x = "B";
                subClass.setFirstName(n);
                subClass.setLastName(x);*/
        /*
                System.out.println("WRITE");
                firebase.child(email.toLowerCase()).setValue(textView.getText().toString());
                Toast.makeText(this, "UPLOADED TO NFC", Toast.LENGTH_SHORT).show();
                readTextFromTag2((NdefMessage) parcelables[0]);
                writeNdefMessage(tag, ndefMessage);
            }
            */

    }
    public void clickNFC(View view){
                String n = "A";
                String x = "B";
                subClass.setFirstName(n);
                subClass.setLastName(x);
            System.out.println("WRITE");
            firebase.child(email.toLowerCase()).setValue(textView.getText().toString());
            Toast.makeText(this, "NFC Updated", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Not Set on Write Mode", Toast.LENGTH_SHORT).show();
    }
    private void readTextFromTag(NdefMessage ndefMessage) {
        NdefRecord[] ndefRecords = ndefMessage.getRecords();
        if(ndefRecords!=null&&ndefRecords.length>0){
            NdefRecord ndefRecord = ndefRecords[0];
            String tagContent = getTextNdef(ndefRecord);
            System.out.println(tagContent);
            System.out.println(NOW);
            mDb.child("TEMP DATA").child("temporaryaccount@gmail,com").addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot!=null) {
                                String x = dataSnapshot.getValue().toString();
                                NOW = x;
                                System.out.println("NOW:"+NOW);
                                textView2.setText(NOW+"\t");
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
        }else{
            Toast.makeText(this, "No Message Found", Toast.LENGTH_SHORT).show();
        }
    }
    private void readTextFromTag2(NdefMessage ndefMessage) {
        NdefRecord[] ndefRecords = ndefMessage.getRecords();
        if(ndefRecords!=null&&ndefRecords.length>0){
            NdefRecord ndefRecord = ndefRecords[0];
            String tagContent = getTextNdef(ndefRecord);
            System.out.println(tagContent);
            System.out.println(NOW);
            mDb.child("TEMP DATA").child("temporaryaccount2@gmail,com").addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot!=null) {
                                System.out.println("UPDATING NFC");
                                String x = dataSnapshot.getValue().toString();
                                NOW = x;
                                System.out.println("NOW:"+NOW);
                                textView2.setText(NOW+"\t");
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
        }else{
            Toast.makeText(this, "No Message Found", Toast.LENGTH_SHORT).show();
        }
    }


    public void formatTag(Tag tag, NdefMessage ndefMessage){
        try{
            NdefFormatable ndefFormatable = NdefFormatable.get(tag);
            if(ndefFormatable==null){
                Toast.makeText(this, "No Tag", Toast.LENGTH_SHORT).show();
                return;
            }
            ndefFormatable.connect();
            ndefFormatable.format(ndefMessage);
            ndefFormatable.close();
            Toast.makeText(this, "TAG WRITTEN", Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Error FORMAT");
        }
    }
    public void writeNdefMessage(Tag tag, NdefMessage ndefMessage){
        try{
            if(tag==null){
                Toast.makeText(this, "No Tag", Toast.LENGTH_SHORT).show();
                return;
            }
                Ndef ndef = Ndef.get(tag);
                if (ndef == null) {
                    //formats tag as Ndef
                    formatTag(tag, ndefMessage);
                } else {
                    ndef.connect();
                    if (!ndef.isWritable()) {
                        Toast.makeText(this, "not writtable", Toast.LENGTH_SHORT).show();
                        ndef.close();
                        return;
                    }
                    System.out.println("TAG IS WRITTEN");
                    ndef.writeNdefMessage(ndefMessage);
                    ndef.close();
                    Toast.makeText(this, "Tag is Written", Toast.LENGTH_SHORT).show();
                }
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Error WRITE NDEF");
        }

    }
    public NdefRecord createTextRecord(String content){
        try{
            byte[] language;
            language = Locale.getDefault().getLanguage().getBytes(StandardCharsets.UTF_8);
            final byte[] text = content.getBytes(StandardCharsets.UTF_8);
            final int languageSize = language.length;
            final int textLength = text.length;
            final ByteArrayOutputStream payload = new ByteArrayOutputStream(1+languageSize+textLength);
            payload.write((byte)(languageSize & 0x1F));
            payload.write(language, 0, languageSize);
            payload.write(text, 0, textLength);

            return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], payload.toByteArray());
        }
        catch(Exception e){
            System.out.println("Error CREATE TEXTRECORD");
        }
        return null;
    }
    public NdefMessage createNdefMessage(String content){
        NdefRecord ndefRecord = createTextRecord(content);
        NdefMessage ndefMessage = new NdefMessage(new NdefRecord[]{ndefRecord});
        return ndefMessage;
    }
    public void toggleOnClick(View view){
        textView2.setText("");
    }
    public String getTextNdef(NdefRecord ndefRecord){
        String tagContent = null;
        try{
            byte[] payload = ndefRecord.getPayload();
            String textEncoding = "UTF-8";
            int languageSize = payload[0]& 0063;
            tagContent = new String(payload, languageSize+1,
                    payload.length-languageSize-1, textEncoding);
        }catch(Exception e){
            System.out.println("Not encoded");
        }
        return tagContent;
    }

    public void onC(View view){
        finish();
    }
}
