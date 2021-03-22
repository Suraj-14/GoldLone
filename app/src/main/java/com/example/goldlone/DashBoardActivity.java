package com.example.goldlone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;
import android.widget.Toast;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DashBoardActivity extends AppCompatActivity {
    LinearLayout linearLayout_home,lin_scanner,lin_register,lin_register_details;
    Animation slidup;
    private long doubleBackToExitPressedOnce ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout2);
        slidup = AnimationUtils.loadAnimation(this, R.anim.move );
        linearLayout_home=findViewById(R.id.home);
        lin_scanner=findViewById(R.id.lin_scanner);
        lin_register=findViewById(R.id.lin_register);
        lin_register_details=findViewById(R.id.lin_register_details);

        lin_scanner.startAnimation(slidup);
        lin_register.startAnimation(slidup);
        lin_register_details.startAnimation(slidup);
        setFadeIn();

        lin_scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashBoardActivity.this, ScannedBarcodeActivity.class);
                startActivity(intent);
            }
        });

        lin_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashBoardActivity.this, Register.class);
                startActivity(intent);
            }
        });

        lin_register_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashBoardActivity.this, Register_Details.class);
                startActivity(intent);
            }
        });
    }
    private void setFadeIn() {
        AnimationSet set = new AnimationSet(true);
        Animation fadeIn = new AlphaAnimation(0.0f, 1.0f);
        fadeIn.setDuration(2000);
        fadeIn.setFillAfter(true);
        set.addAnimation(fadeIn);
        LayoutAnimationController controller = new LayoutAnimationController(set, 0.3f);
        linearLayout_home.setLayoutAnimation(controller);
        lin_scanner.setLayoutAnimation(controller);
        lin_register.setLayoutAnimation(controller);
        lin_register_details.setLayoutAnimation(controller);

    }
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce + 1000 > System.currentTimeMillis()) {
            SweetAlertDialog progressDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
            progressDialog.setCancelable(false);
            progressDialog.setTitleText("Are you sure you want to exit?");
            progressDialog.setCancelText("No");
            progressDialog.setConfirmText("Yes");
            progressDialog.setCanceledOnTouchOutside(true);
            progressDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismiss();
                    DashBoardActivity.super.onBackPressed();
                }
            });
            progressDialog.show();
            return;
        } else {
            Toast.makeText(this, "Double Back press to exit", Toast.LENGTH_SHORT).show();
        }
        doubleBackToExitPressedOnce = System.currentTimeMillis();
    }
}
