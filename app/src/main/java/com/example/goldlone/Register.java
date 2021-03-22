package com.example.goldlone;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.zxing.WriterException;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class Register extends AppCompatActivity {

    private EditText amount, R_interest, name, address,mobile_no,quantity;
    private Button buttonSave, buttonClear;
    private ImageView imageView, img_qrcode;
    private CardView cardView;

    private SimpleDateFormat dateFormatter;
    public TextView fromDateEtxt;
    private String date;
    String str_val = "4";
    AlertDialogManager alert = new AlertDialogManager();
    private String str_name, str_address, str_amount, str_interest, str_date,str_mobile_no,str_quantity;
    private RegisterSqliteConnection mDatabase;


    int year, month, day;

    private static final int STORAGE_PERMISSION_CODE = 4655;
    private int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private Uri filepath;
    private Bitmap image_bitmap, bitmap_qrcode;
    LinearLayout rel_home;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        rel_home=findViewById(R.id.lin_home);
        setFadeIn();
        cardView=findViewById(R.id.card);
        imageView = (ImageView) findViewById(R.id.imageView);
        img_qrcode = (ImageView) findViewById(R.id.imageView_qrcode);
        amount = findViewById(R.id.balance);
        R_interest = findViewById(R.id.intrest);
        name = findViewById(R.id.name);
        address = findViewById(R.id.address);
        mobile_no = findViewById(R.id.mobile_no);
        quantity = findViewById(R.id.quantity);
        fromDateEtxt = (TextView) findViewById(R.id.tv_date);

        buttonSave = findViewById(R.id.save);
        buttonClear = findViewById(R.id.clear);
        mDatabase = new RegisterSqliteConnection(this);

        requestStoragePermission();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // ShowFileChooser();
                selectImage();
            }
        });
        img_qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable drawable=img_qrcode.getDrawable();
                Bitmap bitmap=((BitmapDrawable)drawable).getBitmap();
                String path=MediaStore.Images.Media.insertImage(getContentResolver(),bitmap,"image description",null);
                Uri uri=Uri.parse(path);

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(intent, "Share Image"));
            }
        });
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_name = name.getText().toString().trim();
                str_address = address.getText().toString().trim();
                str_amount = amount.getText().toString().trim();
                str_interest = R_interest.getText().toString().trim();
                str_date = fromDateEtxt.getText().toString().trim();
                str_mobile_no = mobile_no.getText().toString().trim();
                str_quantity = quantity.getText().toString().trim();
                // String path = getPath(filepath);

                if (validate_data()) {
                    cardView.setVisibility(View.VISIBLE);
                    QrcodeGenerate();
                    Register_data register_data = new Register_data(str_name, str_address, str_amount, str_interest, str_date, str_val,str_mobile_no,str_quantity, image_bitmap, bitmap_qrcode);
                    mDatabase.addData(register_data);
                    amount.setText("");
                    R_interest.setText("");
                    name.setText("");
                    address.setText("");
                   /* imageView.setImageResource(R.drawable.add_image);
                    cardView.setVisibility(View.INVISIBLE);*/
                    mobile_no.setText("");
                    quantity.setText("");
                    Toast.makeText(Register.this, "Record Saved Successfully", Toast.LENGTH_SHORT).show();

                }
            }
        });

        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount.setText("");
                R_interest.setText("");
                name.setText("");
                address.setText("");
                imageView.setImageResource(R.drawable.add_image);
                cardView.setVisibility(View.INVISIBLE);
                mobile_no.setText("");
                quantity.setText("");
                //img_qrcode.setImageDrawable(null);
                //fromDateEtxt.setText("");
            }
        });

        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        fromDateEtxt = (TextView) findViewById(R.id.tv_date);
        fromDateEtxt.setInputType(InputType.TYPE_NULL);
        fromDateEtxt.requestFocus();
        date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        fromDateEtxt.setText(date);

        fromDateEtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar newCalendar = Calendar.getInstance();
                DatePickerDialog fromDatePickerDialog = new DatePickerDialog(Register.this, android.R.style.Theme_Holo_Light_Dialog, new DatePickerDialog.OnDateSetListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        //Todo your work here
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, month, day);
                        fromDateEtxt.setText(dateFormatter.format(newDate.getTime()));
                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                fromDatePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                fromDatePickerDialog.setTitle("Set Date");
                fromDatePickerDialog.show();
            }
        });
    }


    //for menu
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.register_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.Register_Data) {
            // Toast.makeText(this, "Clicked " , Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Register.this, Register_Details.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean validate_data() {
        str_name = name.getText().toString().trim();
        str_address = address.getText().toString().trim();
        str_amount = amount.getText().toString().trim();
        str_interest = R_interest.getText().toString().trim();
        str_date = fromDateEtxt.getText().toString().trim();
        str_mobile_no = mobile_no.getText().toString().trim();
        str_quantity = quantity.getText().toString().trim();

        if (str_name.isEmpty()) {
            alert.showAlertDialog(Register.this, "Register failed..", "Please Enter Name.", false);
            //name.setError("! Enter Name");
            name.requestFocus();
            return false;
        }
        else if (str_address.isEmpty()) {
            alert.showAlertDialog(Register.this, "Register failed..", "Please Enter Address.", false);
           // address.setError("! Enter Address");
            address.requestFocus();
            return false;
        }
        else if (str_amount.isEmpty()) {
            alert.showAlertDialog(Register.this, "Register failed..", "Please Enter Amount.", false);
           // amount.setError("! Enter Amount");
            amount.requestFocus();
            return false;
        }
        else if (str_interest.isEmpty()) {
            alert.showAlertDialog(Register.this, "Register failed..", "Please Enter Interest.", false);
            //R_interest.setError("! Enter Interest");
            R_interest.requestFocus();
            return false;
        }
        else if (str_mobile_no.isEmpty()) {
            alert.showAlertDialog(Register.this, "Register failed..", "Please Enter Mobile No.", false);
           // mobile_no.setError("! Enter Mobile No.");
            mobile_no.requestFocus();
            return false;
        }
        else if (str_quantity.isEmpty()) {
            alert.showAlertDialog(Register.this, "Register failed..", "Please Enter Quantity in gram.", false);
            //quantity.setError("! Enter Quantity in gram");
            quantity.requestFocus();
            return false;
        }
        else if (image_bitmap==null) {
            alert.showAlertDialog(Register.this, "Register failed..", "Please select image.", false);
           // Toast.makeText(this,"select image ",Toast.LENGTH_SHORT).show();
            imageView.requestFocus();
            return false;
        }
        return true;
    }

    private void requestStoragePermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void ShowFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null) {

            filepath = data.getData();
            try {

                image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                imageView.setImageBitmap(image_bitmap);
                String path = filepath.toString();
               // Toast.makeText(getApplicationContext(), path, Toast.LENGTH_LONG).show();
            } catch (Exception ex) {

            }
        }
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK)
        {
            image_bitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(image_bitmap);
        }
    }


    public void QrcodeGenerate() {

        String data = "Date :" + fromDateEtxt.getText().toString()+" : \n"+"Name : " + name.getText().toString().trim()
                + " : \n" + "Address :" + address.getText().toString().trim() + " : \n" + "Amount :" + amount.getText().toString().trim() +" : \n"+ "Rate of Interest :"  + R_interest.getText().toString().trim()+
        " : \n" + "Mobile No. :" + mobile_no.getText().toString().trim() +" : \n"+ "Quantity :" + quantity.getText().toString().trim();
        //String all=name.getText().toString().trim()+"\n"+address.getText().toString().trim()+"\n" +amount.getText().toString().trim()+"\n"+R_interest.getText().toString().trim()+"\n"+fromDateEtxt.getText().toString().trim()+"\n";

        /*if (inputValue.length() > 0) {*/
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 3 / 4;

        QRGEncoder qrgEncoder = new QRGEncoder(
                data, null,
                QRGContents.Type.TEXT,
                smallerDimension);
        try {
            Bitmap combined = qrgEncoder.encodeAsBitmap();
            mergeBitmaps(image_bitmap, combined);
           // img_qrcode.setImageBitmap(bitmap_qrcode);
        } catch (WriterException e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
     /*  } else {
           edtValue.setError("Required");
       }*/
    }

    public Bitmap mergeBitmaps(Bitmap logo, Bitmap qrcode) {

         bitmap_qrcode = Bitmap.createBitmap(qrcode.getWidth(), qrcode.getHeight(), qrcode.getConfig());
        Canvas canvas = new Canvas(bitmap_qrcode);
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();
        canvas.drawBitmap(qrcode, new Matrix(), null);

        Bitmap resizeLogo = Bitmap.createScaledBitmap(logo, canvasWidth / 5, canvasHeight / 5, true);
        int centreX = (canvasWidth - resizeLogo.getWidth()) /2;
        int centreY = (canvasHeight - resizeLogo.getHeight()) / 2;
        canvas.drawBitmap(resizeLogo, centreX, centreY, null);
        img_qrcode.setImageBitmap(bitmap_qrcode);
        return bitmap_qrcode;

    }

    private void selectImage() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                    {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                    }
                    else
                    {
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    }
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    ShowFileChooser();
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    private void setFadeIn() {
        AnimationSet set = new AnimationSet(true);
        Animation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setDuration(2000);
        fadeIn.setFillAfter(true);
        set.addAnimation(fadeIn);
        LayoutAnimationController controller = new LayoutAnimationController(set, 0.3f);
        rel_home.setLayoutAnimation(controller);

    }
}

