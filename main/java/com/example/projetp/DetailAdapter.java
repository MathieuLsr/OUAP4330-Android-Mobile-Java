package com.example.projetp;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Calendar;

public class DetailAdapter extends ArrayAdapter<RdvDetails> {

    private long id;
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TextView mDisplayTime;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;

    public static String NOTIFICATION_CHANNEL_ID = "1001";
    public static String default_notification_id = "default";

    public int numberID = 0 ;


    public DetailAdapter(Context context, int resource, long id) {
        super(context, resource);
        this.id = id;
    }

    @Nullable
    @Override
    public RdvDetails getItem(int id) {
        DatabaseHelper databaseHelper = MainActivity.INSTANCE.getDatabaseHelper();
        return databaseHelper.get(id);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = LayoutInflater.from(getContext()).inflate(R.layout.details_activity, null, false);
        TextView rdvTitre = (TextView) view.findViewById(R.id.editTextTitreModif);
        TextView rdvC = (TextView) view.findViewById(R.id.editTextContactModif);
        TextView rdvA = (TextView) view.findViewById(R.id.editTextAdresseModif);
        TextView rdvTel = (TextView) view.findViewById(R.id.editTextTelModif);
        Button valider = (Button) view.findViewById(R.id.buttonValiderModif);
        mDisplayDate = (TextView) view.findViewById(R.id.tvDateModif);
        mDisplayTime = (TextView) view.findViewById(R.id.tvTimeModif);

        Button buttonAppeler = view.findViewById(R.id.buttonAppel);


        RdvDetails rdv = getItem((int) this.id);

        if (rdv == null) return view;

        rdvTitre.setText(rdv.getTitle());
        rdvC.setText(rdv.getContact());
        mDisplayDate.setText(rdv.getDate());
        mDisplayTime.setText(rdv.getTime());
        rdvA.setText(rdv.getAddress());
        rdvTel.setText(rdv.getPhoneNumber());


        //..........MODIFICATION............

        Switch sw = view.findViewById(R.id.switch1);

        sw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (sw.isChecked()) {
                    rdvTitre.setEnabled(true);
                    rdvC.setEnabled(true);
                    mDisplayDate.setEnabled(true);
                    mDisplayTime.setEnabled(true);
                    rdvA.setEnabled(true);
                    rdvTel.setEnabled(true);
                }
                if (!sw.isChecked()) {
                    rdvTitre.setEnabled(false);
                    rdvC.setEnabled(false);
                    mDisplayDate.setEnabled(false);
                    mDisplayTime.setEnabled(false);
                    rdvA.setEnabled(false);
                    rdvTel.setEnabled(false);
                }
            }
        });


        //............VALIDER.............

        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (sw.isChecked()) {
                    String titreStr = rdvTitre.getText().toString();
                    String contactStr = rdvC.getText().toString();
                    String dateStr = mDisplayDate.getText().toString();
                    String heureStr = mDisplayTime.getText().toString();
                    String adresseStr = rdvA.getText().toString();
                    String telStr = rdvTel.getText().toString();

                    rdv.setTitle(titreStr);
                    rdv.setContact(contactStr);
                    rdv.setDate(dateStr);
                    rdv.setTime(heureStr);
                    rdv.setAddress(adresseStr);
                    rdv.setPhoneNumber(telStr);

                    MainActivity.INSTANCE.getDatabaseHelper().update(rdv);
                    Intent intent = new Intent(view.getContext(), MainActivity.class);
                    view.getContext().startActivity(intent);
                }

                Intent intent = new Intent(view.getContext(), MainActivity.class);
                view.getContext().startActivity(intent);


            }
        });


        //.................DATE..................

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
                        year, month, day);
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



        //.....................HEURE........................

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
            public void onTimeSet(TimePicker view, int hour, int minute) {
                String temps = hour + ":" + minute;
                mDisplayTime.setText(temps);
            }
        };



        //..................APPELER.....................

        buttonAppeler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = rdvTel.getText().toString();
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                view.getContext().startActivity(intent);
            }
        });



        //...................MAPS......................

        Button buttonMaps = (Button) view.findViewById(R.id.buttonMaps);

        buttonMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String adresse = rdvA.getText().toString();
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + adresse);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                view.getContext().startActivity(mapIntent);
            }
        });



        //...................SHARE......................

        Button buttonShare = view.findViewById(R.id.buttonShare);

        buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String titreStr = rdvTitre.getText().toString();
                String contactStr = rdvC.getText().toString();
                String dateStr = mDisplayDate.getText().toString();
                String heureStr = mDisplayTime.getText().toString();
                String adresseStr = rdvA.getText().toString();
                String telStr = rdvTel.getText().toString();

                String text = "RDV : " + titreStr + "\n" +
                        "Contact : " + contactStr + "\n" +
                        "Date : " + dateStr + "\n" +
                        "Heure : " + heureStr + "\n" +
                        "Adresse : " + adresseStr + "\n" +
                        "Téléphone : " + telStr;

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, text);
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                view.getContext().startActivity(shareIntent);
            }
        });



        //..............NOTIFICATION................

        createNotifChannel();

        Button buttonNotif = view.findViewById(R.id.buttonNotif);

        buttonNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Notification activée", Toast.LENGTH_SHORT).show();

                String dateStr = mDisplayDate.getText().toString();
                String heureStr = mDisplayTime.getText().toString();
                String titreStr = rdvTitre.getText().toString();

                String[] tab = dateStr.split("/") ;

                int jour = Integer.parseInt(tab[0]),
                        mois = Integer.parseInt(tab[1]),
                        annee = Integer.parseInt(tab[2]);

                String[] tabH = heureStr.split(":") ;

                int heure = Integer.parseInt(tabH[0]),
                        minute = Integer.parseInt(tabH[1]);



                Notification notification = getNotification(view, "RDV dans 24h : "+titreStr);

                Intent notificationIntent = new Intent(view.getContext(), ReminderBroadcast.class);
                notificationIntent.putExtra(ReminderBroadcast.NOTIFICATIONID, numberID);
                numberID++;
                notificationIntent.putExtra(ReminderBroadcast.NOTIFICATION,notification);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(view.getContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);


                AlarmManager alarmManager = (AlarmManager) view.getContext().getSystemService(Context.ALARM_SERVICE);
                assert alarmManager != null;

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, annee);
                calendar.set(Calendar.MONTH, mois-1);
                calendar.set(Calendar.DAY_OF_MONTH, jour);
                calendar.set(Calendar.HOUR_OF_DAY, heure);
                calendar.set(Calendar.MINUTE, minute);

                long timeInMillis = calendar.getTimeInMillis();
                timeInMillis -= 1000*60*60*24;

                alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);

            }
        });

        return view;
    }



    //..............FONCTION_NOTIFICATION..............

    private Notification getNotification(View view, String content){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(view.getContext(), default_notification_id);

        builder.setContentTitle("RDV");
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.ic_launcher_foreground);
        builder.setAutoCancel(true);
        builder.setChannelId(NOTIFICATION_CHANNEL_ID);

        return builder.build();
    }


    private void createNotifChannel(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.details_activity, null, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "Rappel de RDV";
            String description = "Vous avez un RDV";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("MyNotification", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = view.getContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }



}
