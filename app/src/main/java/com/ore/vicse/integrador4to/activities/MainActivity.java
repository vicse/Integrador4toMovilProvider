package com.ore.vicse.integrador4to.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ore.vicse.integrador4to.R;

public class MainActivity extends AppCompatActivity {

    private TextView btnProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnProvider = (TextView) findViewById(R.id.txtRegistrarProvedor);


    }
    public void Login(View view){
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    public void showRegisterProvider(View view){
        Intent intent = new Intent(MainActivity.this, RegisterProviderActivity.class);
        startActivity(intent);
    }




}
