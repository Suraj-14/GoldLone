package com.example.goldlone;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class RegisterSqliteConnection extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 6;
    private static final String DATABASE_NAME = "Register.db";
    private static final String TABLE_NAME = "Register";

    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_Amount = "amount";
    private static final String COLUMN_Interest = "interest";
    private static final String COLUMN_Name = "name";
    private static final String COLUMN_Address = "address";
    private static final String COLUMN_Date = "date";
    private static final String COLUMN_Value = "value";
    private static final String COLUMN_Mobile_no = "mobile_no";
    private static final String COLUMN_Quantity = "quantity";
    private static final String COLUMN_Image ="image";
    private static final String COLUMN_Qrcode ="qrcode";

    private ByteArrayOutputStream byteArrayOutputStream1,byteArrayOutputStream2;
    private  byte[] imageinbytes,qrcodeinbytes;

    public RegisterSqliteConnection(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE	TABLE " + TABLE_NAME + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_Name + " TEXT," + COLUMN_Address + " TEXT," + COLUMN_Amount + " TEXT," + COLUMN_Interest + " TEXT," + COLUMN_Date + " TEXT," + COLUMN_Value + " TEXT,"+ COLUMN_Mobile_no + " TEXT," + COLUMN_Quantity + " TEXT," + COLUMN_Image + " BLOB,"+ COLUMN_Qrcode + " BLOB"+")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public ArrayList<Register_data> listContacts() {
        String sql = "select * from " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Register_data> storedata = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
            if (cursor.moveToFirst()) {
                do {
                    int id = Integer.parseInt(cursor.getString(0));
                    String name = cursor.getString(1);
                    String address = cursor.getString(2);
                    String amount = cursor.getString(3);
                    String interest = cursor.getString(4);
                    String date = cursor.getString(5);
                    String val = cursor.getString(6);
                    String mobile_no = cursor.getString(7);
                    String quantity = cursor.getString(8);
                    byte[] image= cursor.getBlob(9);
                    byte[] qrcode= cursor.getBlob(10);

                    Bitmap bitmap_img= BitmapFactory.decodeByteArray(image,0,image.length);
                    Bitmap bitmap_qrcode= BitmapFactory.decodeByteArray(qrcode,0,qrcode.length);
                    storedata.add(new Register_data(id, name, address, amount, interest, date, val,mobile_no,quantity,bitmap_img,bitmap_qrcode));
                } while (cursor.moveToNext());
            }
            cursor.close();
            return storedata;

    }

    public void addData(Register_data data){
        Bitmap image_bitmap=data.getImage();
        byteArrayOutputStream1=new ByteArrayOutputStream();
        image_bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream1);
        imageinbytes=byteArrayOutputStream1.toByteArray();

        Bitmap qrcode_bitmap=data.getQrcode();
        byteArrayOutputStream2=new ByteArrayOutputStream();
        qrcode_bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream2);
        qrcodeinbytes=byteArrayOutputStream2.toByteArray();

        ContentValues values = new ContentValues();
        values.put(COLUMN_Name, data.getName());
        values.put(COLUMN_Address, data.getAddress());
        values.put(COLUMN_Amount, data.getAmount());
        values.put(COLUMN_Interest, data.getInterest());
        values.put(COLUMN_Date, data.getDate());
        values.put(COLUMN_Value, data.getValue());
        values.put(COLUMN_Mobile_no, data.getMobile_no());
        values.put(COLUMN_Quantity, data.getQuantity());
        values.put(COLUMN_Image, imageinbytes);
        values.put(COLUMN_Qrcode, qrcodeinbytes);
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_NAME, null, values);
    }

    public void updateContacts(Register_data data){
        Bitmap image_bitmap=data.getImage();
        byteArrayOutputStream1=new ByteArrayOutputStream();
        image_bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream1);
        imageinbytes=byteArrayOutputStream1.toByteArray();

        Bitmap qrcode_bitmap=data.getQrcode();
        byteArrayOutputStream2=new ByteArrayOutputStream();
        qrcode_bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream2);
        qrcodeinbytes=byteArrayOutputStream2.toByteArray();

        ContentValues values = new ContentValues();
        values.put(COLUMN_Name, data.getName());
        values.put(COLUMN_Address, data.getAddress());
        values.put(COLUMN_Amount, data.getAmount());
        values.put(COLUMN_Interest, data.getInterest());
        values.put(COLUMN_Date, data.getDate());
        values.put(COLUMN_Value, data.getValue());
        values.put(COLUMN_Mobile_no, data.getMobile_no());
        values.put(COLUMN_Quantity, data.getQuantity());
        values.put(COLUMN_Image, imageinbytes);
        values.put(COLUMN_Qrcode, qrcodeinbytes);
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_NAME, values, COLUMN_ID	+ "	= ?", new String[] { String.valueOf(data.getId())});
    }


    public ArrayList<Register_data> search(String keyword){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from " + TABLE_NAME + " where " + COLUMN_Name + " like ?", new String[] { "%" + keyword + "%" });
        //String sql = "select * from " + TABLE_NAME + " where " + COLUMN_Name + " like ?", new String[] { "%" + keyword + "%" };
        ArrayList<Register_data> storedata = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                int id = Integer.parseInt(cursor.getString(0));
                String name = cursor.getString(1);
                String address = cursor.getString(2);
                String amount = cursor.getString(3);
                String interest = cursor.getString(4);
                String date = cursor.getString(5);
                String val = cursor.getString(6);
                String mobile_no = cursor.getString(7);
                String quantity = cursor.getString(8);
                byte[] image= cursor.getBlob(9);
                byte[] qrcode= cursor.getBlob(10);
                Bitmap bitmap_img= BitmapFactory.decodeByteArray(image,0,image.length);
                Bitmap bitmap_qrcode= BitmapFactory.decodeByteArray(qrcode,0,qrcode.length);
                storedata.add(new Register_data(id,name , address,amount,interest,date,val,mobile_no,quantity,bitmap_img,bitmap_qrcode));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return storedata;
    }

    public ArrayList<Register_data> searchBarcode(String keyword,String keyword2){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from " + TABLE_NAME + " where " + COLUMN_Name + " like ?", new String[]{new String[]{"%" + keyword + "%"} + "AND" + COLUMN_Amount + "=" + keyword2});
        //String sql = "select * from " + TABLE_NAME + " where " + COLUMN_Name + " like ?", new String[] { "%" + keyword + "%" };
        ArrayList<Register_data> storedata = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                int id = Integer.parseInt(cursor.getString(0));
                String name = cursor.getString(1);
                String address = cursor.getString(2);
                String amount = cursor.getString(3);
                String interest = cursor.getString(4);
                String date = cursor.getString(5);
                String val = cursor.getString(6);
                String mobile_no = cursor.getString(7);
                String quantity = cursor.getString(8);
                byte[] image= cursor.getBlob(9);
                byte[] qrcode= cursor.getBlob(10);
                Bitmap bitmap_img= BitmapFactory.decodeByteArray(image,0,image.length);
                Bitmap bitmap_qrcode= BitmapFactory.decodeByteArray(qrcode,0,qrcode.length);
                storedata.add(new Register_data(id,name , address,amount,interest,date,val,mobile_no,quantity,bitmap_img,bitmap_qrcode));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return storedata;
    }

    public void deleteContact(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID	+ "	= ?", new String[] { String.valueOf(id)});
    }
    public void deleteAll()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME,null,null);
        db.close();
    }
}
