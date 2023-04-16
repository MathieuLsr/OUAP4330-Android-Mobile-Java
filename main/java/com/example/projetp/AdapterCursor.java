package com.example.projetp;

import android.database.Cursor;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class AdapterCursor {

    private Cursor cursor ;

    public AdapterCursor(Cursor cursor){
        this.cursor = cursor ;
    }

    public List<String> getListNames(){
        List<String> list = new ArrayList<>() ;
        for(RdvDetails rdv : getList())
            list.add(rdv.getContact()+" - "+rdv.getDate()) ;
        return list ;
    }

    public List<RdvDetails> getList(){

        List<RdvDetails> list = new ArrayList<>() ;

        if(cursor.isAfterLast()) return list ;

        while(!cursor.isLast()){
            cursor.moveToNext();

            int numID = cursor.getColumnIndex(DatabaseHelper._ID) ;
            int numTitle = cursor.getColumnIndex(DatabaseHelper.TITLE) ;
            int numDate = cursor.getColumnIndex(DatabaseHelper.DATE) ;
            int numTime = cursor.getColumnIndex(DatabaseHelper.TIME) ;
            int numContact = cursor.getColumnIndex(DatabaseHelper.CONTACT) ;
            int numAddress = cursor.getColumnIndex(DatabaseHelper.ADDRESS) ;
            int numPhone = cursor.getColumnIndex(DatabaseHelper.PHONENUMBER) ;


            long ID = cursor.getLong(numID) ;
            String title = cursor.getString(numTitle) ;
            String date = cursor.getString(numDate) ;
            String time = cursor.getString(numTime) ;
            String contact = cursor.getString(numContact) ;
            String address = cursor.getString(numAddress) ;
            String phone = cursor.getString(numPhone) ;

            RdvDetails rdv = new RdvDetails(ID, title, date, time, contact, address, phone, true);

            list.add(rdv) ;
        }

        return list ;

    }

}
