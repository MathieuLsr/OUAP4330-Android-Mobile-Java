package com.example.projetp;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.sql.Date;

public class RdvDetails implements Parcelable {

    private long ID ;
    private String title ;
    private String date ;
    private String time ;
    private String contact ;
    private String address ;
    private String phoneNumber ;
    private boolean isDone ;


    public RdvDetails(long ID) {
        this.ID = ID;
        setTitle("title_"+ID);
        setDate("");
        setTime("");
        setContact("contact_"+ID);
        setAddress("address_"+ID);
        setPhoneNumber("phonenumber_"+ID);
    }

    public RdvDetails(long ID, String title, String date, String time, String contact, String address, String phoneNumber, boolean isDone){
        setID(ID);
        setTitle(title);
        setDate(date);
        setTime(time);
        setContact(contact);
        setAddress(address);
        setPhoneNumber(phoneNumber);
        setDone(isDone);
    }

    protected RdvDetails(Parcel in) {
        ID = in.readLong();
        title = in.readString();
        date = in.readString();
        time = in.readString();
        contact = in.readString();
        address = in.readString();
        phoneNumber = in.readString();
        isDone = in.readByte() != 0;
    }

    public static final Creator<RdvDetails> CREATOR = new Creator<RdvDetails>() {
        @Override
        public RdvDetails createFromParcel(Parcel in) {
            return new RdvDetails(in);
        }

        @Override
        public RdvDetails[] newArray(int size) {
            return new RdvDetails[size];
        }
    };

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public int describeContents() {
        return this.hashCode() ;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeLong(this.getID());
        parcel.writeString(this.getTitle());
        parcel.writeString(this.getDate());
        parcel.writeString(this.getTime());
        parcel.writeString(this.getContact());
        parcel.writeString(this.getAddress());
        parcel.writeString(this.getPhoneNumber());
        parcel.writeBoolean(this.isDone());
    }

}
