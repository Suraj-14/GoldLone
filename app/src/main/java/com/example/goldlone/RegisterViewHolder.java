package com.example.goldlone;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class RegisterViewHolder extends RecyclerView.ViewHolder{

    public  TextView tv_name,tv_address,tv_amount,tv_interest,tv1_date,tv1_mobile_no,tv1_quantity;
    public  LinearLayout lin_register;
    public ImageView imageView,img_qrcode;
    public ImageView deleteContact;
    public ImageView editContact;
    public ImageView share;
    public RegisterViewHolder(View itemView) {
        super(itemView);
        tv_name = (TextView) itemView.findViewById(R.id.rname);
        tv_address= (TextView) itemView.findViewById(R.id.raddress);
        tv_amount = (TextView) itemView.findViewById(R.id.ramount);
       tv_interest= (TextView) itemView.findViewById(R.id.rinterest);
        tv1_date= (TextView) itemView.findViewById(R.id.rdate);

        tv1_mobile_no= (TextView) itemView.findViewById(R.id.rmobile_no);
        tv1_quantity= (TextView) itemView.findViewById(R.id.rquantity);
        imageView = (ImageView)itemView.findViewById(R.id.imageView);
        img_qrcode = (ImageView)itemView.findViewById(R.id.imageView_qrcode);

        lin_register=(LinearLayout)itemView.findViewById(R.id.lin_register) ;

        deleteContact = (ImageView) itemView.findViewById(R.id.delete_contact);
        //editContact = (ImageView) itemView.findViewById(R.id.edit_contact);
        share = (ImageView) itemView.findViewById(R.id.paid);
    }
}
