package com.example.projetp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {


    private SQLiteDatabase database;
    // Table Name
    public static final String TABLE_NAME = "RDVManager";
    // Table columns
    public static final String _ID = "_id";
    public static final String TITLE = "title";
    public static final String DATE = "date";
    public static final String TIME = "time";
    public static final String CONTACT = "contact";
    public static final String ADDRESS = "address";
    public static final String PHONENUMBER = "phonenumber";

    // Database Information
    public static final String DB_NAME = "RDVManager.DB";
    // database version
    public static final int DB_VERSION = 1;

    // Creating table query
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            TITLE + " TEXT NOT NULL, "+
            DATE + " TEXT NOT NULL, " +
            TIME + " TEXT NOT NULL, "+
            CONTACT + " TEXT NOT NULL, "+
            ADDRESS + " TEXT NOT NULL, "+
            PHONENUMBER + " CHAR(10)"+
            ");";


    public DatabaseHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION) ;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void open() throws SQLException {
        database = this.getWritableDatabase();
    }
    public void close() {
        database.close();
    }

    public void add(RdvDetails rdv){
        ContentValues values= new ContentValues();

        values.put(TITLE, rdv.getTitle());
        values.put(DATE, rdv.getDate());
        values.put(TIME, rdv.getTime());
        values.put(CONTACT, rdv.getContact());
        values.put(ADDRESS, rdv.getAddress());
        values.put(PHONENUMBER, rdv.getPhoneNumber());

        database.insert(TABLE_NAME, null, values) ;
    }
    public int update(RdvDetails rdv) {
        Long _id= rdv.getID();
        ContentValues values = new ContentValues();
        values.put(TITLE, rdv.getTitle());
        values.put(DATE, rdv.getDate());
        values.put(TIME, rdv.getTime());
        values.put(CONTACT, rdv.getContact());
        values.put(ADDRESS, rdv.getAddress());
        values.put(PHONENUMBER, rdv.getPhoneNumber());

        int count = database.update(TABLE_NAME, values, this._ID + " = " + _id, null);
        return count;
    }
    public Cursor getAllRDV(){
        String[] projection = {_ID,TITLE, DATE, TIME, CONTACT, ADDRESS, PHONENUMBER};
        Cursor cursor = database.query(TABLE_NAME,projection,null,null,null,null,null,null);
        return cursor;
    }
    public void delete(long ID){
        database.delete(TABLE_NAME, _ID + "=" + ID, null);
    }

    public int size(){
        Cursor cursor = getAllRDV() ;
        cursor.moveToLast() ;
        return cursor.getPosition() ;
    }


    public RdvDetails get(int id){

        Cursor cursor = getAllRDV() ;
        AdapterCursor adapterCursor = new AdapterCursor(cursor) ;
        for(RdvDetails rdv : adapterCursor.getList()) {
            System.out.println(rdv.getID());
            if (rdv.getID() == id) return rdv;

        }
        return null ;
    }



}
