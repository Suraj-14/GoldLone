package com.example.goldlone;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

@RequiresApi(api = Build.VERSION_CODES.N)
public class Register_Adapter extends RecyclerView.Adapter<RegisterViewHolder> //implements Filterable

{
    private Bitmap bitmap;
    private Context context;
    private ArrayList<Register_data> listdata;
    private ArrayList<Register_data> rArrayList;

    private RegisterSqliteConnection mDatabase;
    private static final int REQUEST_PHONE_CALL = 1;

    long days = 0;
    int year, months, day;
    String str_amount, str_tot_int, str_tot_amt, time;

    Calendar c = Calendar.getInstance();
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String today = df.format(c.getTime());
    SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
    String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

    public Register_Adapter(Context context, ArrayList<Register_data> listdata) {
        this.context = context;
        this.listdata = listdata;
        this.rArrayList = listdata;
        mDatabase = new RegisterSqliteConnection(context);
    }


    @Override
    public RegisterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.register_data_layout, parent, false);
        return new RegisterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RegisterViewHolder holder, int position) {

        final Register_data contacts = listdata.get(position);
       /* Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = df.format(c.getTime());*/
        holder.share.setVisibility(Integer.parseInt(contacts.getValue()));

        holder.tv_name.setText(contacts.getName());
        holder.tv_address.setText(contacts.getAddress());
        holder.tv_amount.setText(contacts.getAmount());
        holder.tv_interest.setText(contacts.getInterest());
        holder.tv1_date.setText(contacts.getDate());
        holder.tv1_mobile_no.setText(contacts.getMobile_no());
        holder.tv1_quantity.setText(contacts.getQuantity());
        // String path=String.valueOf(contacts.getPath());
        //Toast.makeText(context, ""+path, Toast.LENGTH_SHORT).show();
        holder.imageView.setImageBitmap(contacts.getImage());
        holder.img_qrcode.setImageBitmap(contacts.getQrcode());
        //Picasso.get().load(path).into(holder.imageView);

        holder.tv1_mobile_no.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {

                final Dialog nagDialog = new Dialog(context,android.R.style.ThemeOverlay_Material_Dialog_Alert);
                nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                nagDialog.setCancelable(false);
                nagDialog.setContentView(R.layout.dialog_button);
                CardView card=nagDialog.findViewById(R.id.card);
                card.setVisibility(View.INVISIBLE);
                //Button show = (Button)nagDialog.findViewById(R.id.show);
                Button share = (Button)nagDialog.findViewById(R.id.share);
                Button cancel = (Button)nagDialog.findViewById(R.id.cancel);
                //show.setVisibility(View.INVISIBLE);
                share.setText("Call");
                share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {

                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + contacts.getMobile_no().trim()));
                        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);
                            }
                            else
                            {
                                context.startActivity(intent);
                            }
                        }
                        else
                        {
                            context.startActivity(intent);
                        }


                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {

                        nagDialog.dismiss();
                    }
                });
                nagDialog.show();
                /*Toast.makeText(context, "mobile no click", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + contacts.getMobile_no().trim()));
                context.startActivity(intent);*/
            }
        });

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog nagDialog = new Dialog(context,android.R.style.Theme_Material_Light_DialogWhenLarge);
                nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                nagDialog.setCancelable(false);
                nagDialog.setContentView(R.layout.preview_image);
                Button btnClose = (Button)nagDialog.findViewById(R.id.btnIvClose);
                Button Close = (Button)nagDialog.findViewById(R.id.btnClose);
                ImageView ivPreview = (ImageView)nagDialog.findViewById(R.id.iv_preview_image);
                ivPreview.setImageBitmap(contacts.getImage());

                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {

                        nagDialog.dismiss();
                    }
                });
                Close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {

                        nagDialog.dismiss();
                    }
                });
                nagDialog.show();
            }
        });

        holder.img_qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog nagDialog = new Dialog(context,android.R.style.ThemeOverlay_Material_Dialog_Alert);
                nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                nagDialog.setCancelable(false);
                nagDialog.setContentView(R.layout.dialog_button);
                Button show = (Button)nagDialog.findViewById(R.id.show);
                Button share = (Button)nagDialog.findViewById(R.id.share);
                Button cancel = (Button)nagDialog.findViewById(R.id.cancel);
                /*ImageView ivPreview = (ImageView)nagDialog.findViewById(R.id.iv_preview_image);
                ivPreview.setImageBitmap(contacts.getQrcode());*/
                show.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        final Dialog nagDialog = new Dialog(context,android.R.style.ThemeOverlay_Material_Dialog_Alert);
                        nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        nagDialog.setCancelable(false);
                        nagDialog.setContentView(R.layout.preview_image);
                        Button btnClose = (Button)nagDialog.findViewById(R.id.btnIvClose);
                        Button Close = (Button)nagDialog.findViewById(R.id.btnClose);
                        ImageView ivPreview = (ImageView)nagDialog.findViewById(R.id.iv_preview_image);
                        ivPreview.setImageBitmap(contacts.getQrcode());
                        btnClose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View arg0) {
                                nagDialog.dismiss();
                            }
                        });
                        Close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View arg0) {
                                nagDialog.dismiss();
                            }
                        });
                        nagDialog.show();
                    }
                });
                share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        Drawable drawable=holder.img_qrcode.getDrawable();
                        Bitmap bitmap=((BitmapDrawable)drawable).getBitmap();
                        String path=MediaStore.Images.Media.insertImage(context.getContentResolver(),bitmap,"image description",null);
                        Uri uri=Uri.parse(path);

                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("image/jpeg");
                        intent.putExtra(Intent.EXTRA_STREAM, uri);
                        context.startActivity(Intent.createChooser(intent, "Share Image"));
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {

                        nagDialog.dismiss();
                    }
                });
                nagDialog.show();
            }
        });

        holder.deleteContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //delete row from database
                SweetAlertDialog progressDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
                progressDialog.setCancelable(false);
                progressDialog.setTitleText("Are you sure you want to Delete?");
                progressDialog.setCancelText("No");
                progressDialog.setConfirmText("Yes");
                progressDialog.setCanceledOnTouchOutside(true);
                progressDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                       mDatabase.deleteContact(contacts.getId());
                        ((Activity) context).finish();
                        context.startActivity(((Activity) context).getIntent());
                    }
                });progressDialog.show();

            }
        });
       /* holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String string = "\u20B9";
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.setType("text/plain");
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Given Date :"+contacts.getDate() +"\n"+"End date :"+date+"\n"+"Time :"+time+"\n"+"Amount :"+string+" "+contacts.getAmount()+"\n"+ "Rate of Interest :"+" "+contacts.getInterest()+"\n"+"Total Interest :"+string+" "+str_tot_int
                        +"\n"+ "Total Amount :"+string+" "+str_tot_amt );
                context.startActivity(sendIntent);
            }
        });*/

        holder.lin_register.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
               // Toast.makeText(view.getContext(), "click on item: " + contacts.getName(), Toast.LENGTH_LONG).show();
                LayoutInflater inflater = LayoutInflater.from(context);
                View subView = inflater.inflate(R.layout.register_int_calc_details, null);

                TextView tv_amount, tv_tot_int, tv_tot_amt, tv_givendate, tv_returndate, tv_duration, tv_c_date, tv_r_int;

                tv_amount = (TextView) subView.findViewById(R.id.amount);
                tv_tot_int = (TextView) subView.findViewById(R.id.tot_int);
                tv_tot_amt = (TextView) subView.findViewById(R.id.tot_amt);
                tv_givendate = (TextView) subView.findViewById(R.id.tv_g_date);
                tv_returndate = (TextView) subView.findViewById(R.id.tv_r_date);
                tv_duration = (TextView) subView.findViewById(R.id.d_time);
                tv_c_date = (TextView) subView.findViewById(R.id.c_date);
                tv_r_int = (TextView) subView.findViewById(R.id.r_interest);

                calculate(contacts.getDate(),date);
                interest_calc(contacts.getAmount(),contacts.getInterest());

                if (contacts != null) {
                    tv_amount.setText(contacts.getAmount());
                    tv_r_int.setText(contacts.getInterest());
                    tv_c_date.setText(today);
                    tv_givendate.setText(contacts.getDate());
                    tv_returndate.setText(date);
                    tv_tot_amt.setText(str_tot_amt);
                    tv_tot_int.setText(str_tot_int);
                    tv_duration.setText(time);

                }

                final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.DialogTheme);
                // builder.setTitle("Edit Record Details");
                builder.setView(subView);
                builder.create();

                builder.setPositiveButton("Share", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String string = "\u20B9";
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.setType("text/plain");
                        sendIntent.putExtra(Intent.EXTRA_TEXT, "Given Date :"+contacts.getDate() +"\n"+"End date :"+date+"\n"+"Time :"+time+"\n"+"Amount :"+string+" "+contacts.getAmount()+"\n"+ "Rate of Interest :"+" "+contacts.getInterest()+"\n"+"Total Interest :"+string+" "+str_tot_int
                                +"\n"+ "Total Amount :"+string+" "+str_tot_amt );
                        context.startActivity(sendIntent);
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Toast.makeText(context, "Task cancelled", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNeutralButton("" + "paid", new DialogInterface.OnClickListener() {
                    @SuppressLint("WrongConstant")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        holder.lin_register.setBackgroundColor(Color.BLUE);
                        String val="0";
                        mDatabase.updateContacts(new Register_data(contacts.getId(),contacts.getName() ,contacts.getAddress() , contacts.getAmount(),contacts.getInterest(),contacts.getDate(),val,contacts.getMobile_no(),contacts.getQuantity(),contacts.getImage(),contacts.getQrcode()));
                        //refresh the activity
                        ((Activity)context).finish();
                        context.startActivity(((Activity)context).getIntent());
                    }
                });

                builder.show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public void calculate(String g_date, String e_date) {
        DateTimeFormatter dateFormatter = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dateFormatter = DateTimeFormatter.ofPattern("d/M/u");
        }
        LocalDate startDateValue = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startDateValue = LocalDate.parse(g_date, dateFormatter);
        }
        LocalDate endDateValue = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            endDateValue = LocalDate.parse(e_date, dateFormatter);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            days = ChronoUnit.DAYS.between(startDateValue, endDateValue) + 1;

        }
        // Toast.makeText(context, "days  " + days, Toast.LENGTH_SHORT).show();
    }

    public void interest_calc(String bal, String r_int) {
        float intrest_day, intrest_month, intrest_year;
        float total_interest;
        float total;

        year = (int) (days / 365);
        months = ((int) (days % 365)) / 30;
        day = (int) ((days % 365) % 30);

        time = day + " Day || " + months + " Month || " + year + " Year";

        float balance = Float.parseFloat(bal);
        float interest = Float.parseFloat(r_int);

        intrest_month = (balance * interest) / 100;
        intrest_day = intrest_month / 30;
        intrest_year = 12 * intrest_month;
        // Toast.makeText(this,intrest_year+" Year  "+intrest_month+" month  " +intrest_day +" Days",Toast.LENGTH_SHORT).show();

        total_interest = (year * intrest_year) + (months * intrest_month) + (day * intrest_day);
        total = total_interest + balance;

       // str_amount = String.valueOf(balance);
        str_tot_int = String.valueOf(total_interest);
        str_tot_amt = String.valueOf(total);
    }
}



