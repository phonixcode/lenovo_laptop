package com.alanson.eproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AnimationUtils;

public class StartActivity extends AppCompatActivity {
    //declaring variable
    private ConstraintLayout startScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        //init view
        startScreen = findViewById(R.id.start_layout);

        //opening new activity after 3seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //fade in animation
                startScreen.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in));
                //starting new activity
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        }, 3000);
    }
}