package com.example.projetp;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.security.Permission;
import java.util.Calendar;
import android.Manifest;
import java.util.TimeZone;
import java.util.Timer;

public class AddManager extends AppCompatActivity {

    private DatabaseHelper databaseHelper ;
    private Button addButton ;
    private EditText titre;
    private EditText contact;
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private EditText heure;
    private EditText adresse;
    private EditText tel;

    private TextView mDisplayTime;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;


    public AddManager(){
        this.databaseHelper = MainActivity.INSTANCE.getDatabaseHelper() ;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_activity);

        addButton = (Button) super.findViewById(R.id.buttonValiderAdd);
        titre = (EditText) findViewById(R.id.editTextTitreAdd);
        contact = (EditText) findViewById(R.id.editTextContactAdd);
        adresse = (EditText) findViewById(R.id.editTextAdresseAdd);
        tel = (EditText) findViewById(R.id.editTextTelAdd);

        mDisplayDate = (TextView) findViewById(R.id.tvDateAdd);
        mDisplayTime = (TextView) findViewById(R.id.tvTimeAdd);



        //...................AJOUTER......................

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String titreStr = titre.getText().toString() ;
                String contactStr = contact.getText().toString();
                String adresseStr = adresse.getText().toString();
                String telStr = tel.getText().toString();

                String dateStr = mDisplayDate.getText().toString();
                String heureStr = mDisplayTime.getText().toString();

                if(titreStr.equals("") || contactStr.equals("") || dateStr.equals("") || heureStr.equals("") ||
                        adresseStr.equals("") || telStr.equals("")){

                    Toast.makeText(view.getContext(), "Champ(s) vide(s)", Toast.LENGTH_LONG).show();
                    return ;
                }

                else{
                    RdvDetails rdv = new RdvDetails(databaseHelper.size()+1, titreStr, dateStr, heureStr, contactStr,
                            adresseStr, telStr, true);
                    databaseHelper.add(rdv);
                }

                Intent intent = new Intent(getApplicationContext(), MainActivity.class) ;
                startActivity(intent);
            }
        });


        //...................ANNULER......................

        Button buttonAnnuler = (Button) findViewById(R.id.buttonAnnuler);

        buttonAnnuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class) ;
                startActivity(intent);

            }
        });


        //...................DATE......................

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        view.getContext(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d("", "onDateSet: jj/mm/aaa: " + day + "/" + month + "/" + year);

                String date = day + "/" + month + "/" + year;
                mDisplayDate.setText(date);
            }
        };


        //...................HEURE......................

        mDisplayTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                TimePickerDialog dialogTime = new TimePickerDialog(
                        view.getContext(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mTimeSetListener,
                        hour, minute, false);
                dialogTime.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogTime.show();
            }
        });

        mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hour,int minute) {
                String temps = hour + ":" + minute;
                mDisplayTime.setText(temps);
            }
        };



        //...............CONTACT................

        Button findContact = findViewById(R.id.buttonContacts) ;

        findContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(MainActivity.INSTANCE, Manifest.permission.READ_CONTACTS)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(MainActivity.INSTANCE,
                            new String[]{Manifest.permission.READ_CONTACTS},
                            CONTACT_PERMISSION_CODE);
                }

                pickContactIntent() ;

            }
        });


    }


    private static final int CONTACT_PERMISSION_CODE = 1;
    private static final int CONTACT_PICK_CODE = 2;

    private void pickContactIntent(){
        //intent to pick contact
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, CONTACT_PICK_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //handle permission request result
        if (requestCode == CONTACT_PERMISSION_CODE){
            if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //permission granted, can pick contact now
                pickContactIntent();
            }
            else {
                //permission denied
                Toast.makeText(this, "Permission denied...", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //handle intent results
        if (resultCode == RESULT_OK){
            //calls when user click a contact from list

            if (requestCode == CONTACT_PICK_CODE){


                Cursor cursor1, cursor2;

                //get data from intent
                Uri uri = data.getData();

                cursor1 = getContentResolver().query(uri, null, null, null, null);

                if (cursor1.moveToFirst()){
                    //get contact details
                    @SuppressLint("Range")
                    String contactId = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts._ID));
                    @SuppressLint("Range")
                    String contactName = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    @SuppressLint("Range")
                    String contactThumnail = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI));
                    @SuppressLint("Range")
                    String idResults = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                    int idResultHold = Integer.parseInt(idResults);

                    //System.out.println(contactId);
                    //System.out.println(contactName);
                    this.contact.setText(contactName);

                    //System.out.println(getPhoneNumberFromContactId(MainActivity.INSTANCE, contactId));

                    if (idResultHold == 1){
                        cursor2 = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+contactId,
                                null,
                                null
                        );
                        //a contact may have multiple phone numbers
                        while (cursor2.moveToNext()){
                            //get phone number
                            @SuppressLint("Range") String contactNumber = cursor2.getString(cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            //set details

                            //System.out.println("Phone: "+contactNumber);

                            this.tel.setText(contactNumber);
                        }
                        cursor2.close();
                    }
                    cursor1.close();
                }
            }
        }
        else {
            //calls when user click back button | don't pick contact
        }
    }

    @SuppressLint("Range")
    public String getPhoneNumberFromContactId(Context context, String contactId) {

        // Projection pour récupérer le numéro de téléphone
        String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};

        // Clause WHERE pour récupérer le numéro de téléphone du contact
        String selection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?";

        // Argument pour la clause WHERE
        String[] selectionArgs = new String[]{contactId};

        // Exécution de la requête
        Cursor cursor = context.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null
        );

        // Récupération du numéro de téléphone
        String phoneNumber = null;
        if (cursor != null && cursor.moveToFirst()) {
            phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            cursor.close();
        }

        return phoneNumber;
    }


}
