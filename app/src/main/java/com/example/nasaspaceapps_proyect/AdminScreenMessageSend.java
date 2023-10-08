package com.example.nasaspaceapps_proyect;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class AdminScreenMessageSend extends AppCompatActivity {
    double latitudeAtt;
    double longitudAtt;
    Button alertButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_screen_message_send);

        EditText locationInput = findViewById(R.id.locationInput);

        double latitude = getIntent().getDoubleExtra("latitude", 0);
        double longitude = getIntent().getDoubleExtra("longitude", 0);

        longitudAtt = longitude;
        latitudeAtt = latitude;

        locationInput.setText(latitude + ", " + longitude);
        alertButton.setOnClickListener(view -> {
            // Aquí puedes poner el código que se ejecutará cuando se haga clic en el botón
        });

    }
}