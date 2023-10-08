package com.example.nasaspaceapps_proyect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MaterialButton adminButton = findViewById(R.id.button1);
        MaterialButton userButton = findViewById(R.id.button2);

        // Establecer el OnClickListener
        adminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar la actividad AdminScreenMap
                Intent intent = new Intent(MainActivity.this, AdminScreenMap.class);
                startActivity(intent);
            }
        });
        userButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, UserScreenMessage.class);
                startActivity(intent);
            }
        });
    }
}