package com.example.pramendra.spotnavigation;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class Themed extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_themed);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }
}
