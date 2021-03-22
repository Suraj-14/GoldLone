package com.example.goldlone;

import android.graphics.Bitmap;

public class Register_data {
    private	int	id;
    private	String name,address,amount,interest,date,value,mobile_no,quantity;
    Bitmap image,qrcode;

    public Register_data(int id, String name, String address, String amount, String interest, String date, String value, String mobile_no, String quantity,Bitmap image,Bitmap qrcode) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.amount = amount;
        this.interest = interest;
        this.date = date;
        this.value=value;
        this.mobile_no = mobile_no;
        this.quantity=quantity;
        this.image=image;
        this.qrcode=qrcode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Register_data( String name, String address, String amount, String interest, String date, String value, String mobile_no, String quantity, Bitmap image,Bitmap qrcode) {
        this.name = name;
        this.address = address;
        this.amount = amount;
        this.interest = interest;
        this.date = date;
        this.value=value;
        this.mobile_no = mobile_no;
        this.quantity=quantity;
        this.image=image;
        this.qrcode=qrcode;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Bitmap getImage() {
        return image;
    }

    public Bitmap getQrcode() {
        return qrcode;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public String getQuantity() {
        return quantity;
    }
}
