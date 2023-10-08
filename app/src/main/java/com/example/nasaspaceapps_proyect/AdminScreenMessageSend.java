package com.example.nasaspaceapps_proyect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class AdminScreenMessageSend extends AppCompatActivity {
    double latitudeAtt;
    double longitudAtt;

    double latitudeFija = -8.94431;
    double logitudFija = -75.15337;
    Button alertButton;

    private static final int SMS_PERMISSION_REQUEST_CODE = 1;

    private String calcularDistanciaYDireccion(double latitudVariable, double longitudVariable) {
        // Crear objetos de ubicación para las coordenadas
        Location ubicacionFija = new Location("Fija");
        ubicacionFija.setLatitude(latitudeFija);
        ubicacionFija.setLongitude(logitudFija);

        Location ubicacionVariable = new Location("Variable");
        ubicacionVariable.setLatitude(latitudVariable);
        ubicacionVariable.setLongitude(longitudVariable);

        // Calcular la distancia entre las dos ubicaciones en metros
        float distanciaMetros = ubicacionFija.distanceTo(ubicacionVariable);

        // Calcular la dirección (norte, sur, este, oeste)
        String direccion = obtenerDireccion(latitudVariable, longitudVariable);

        // Convertir la distancia a kilómetros
        double distanciaKm = distanciaMetros / 1000.0;

        // Crear el mensaje de alerta con la distancia y la dirección
        return String.format("¡Alerta de incendio en tu área! Distancia aproximada.");
    }

    private String obtenerDireccion(double latitudVariable, double longitudVariable) {
        double deltaLatitud = latitudVariable - latitudeFija;
        double deltaLongitud = longitudVariable - logitudFija;

        if (Math.abs(deltaLatitud) < 0.0001 && Math.abs(deltaLongitud) < 0.0001) {
            return "la misma ubicación";
        } else if (deltaLatitud > 0) {
            if (deltaLongitud > 0) {
                return "noreste";
            } else {
                return "norte";
            }
        } else {
            if (deltaLongitud > 0) {
                return "este";
            } else {
                return "oeste";
            }
        }
    }

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

        alertButton = findViewById(R.id.alertButton);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_REQUEST_CODE);
        }
        alertButton.setOnClickListener(new View.OnClickListener() {
            // Aquí puedes poner el código que se ejecutará cuando se haga clic en el botón
            @Override
            public void onClick(View view){

                String mensajeAlerta = calcularDistanciaYDireccion(longitudAtt, latitudeAtt);
                Toast.makeText(AdminScreenMessageSend.this, "Alerta enviada a los pobladores", Toast.LENGTH_SHORT).show();

                List<String> numeros = leerNumerosDeArchivo("numeros.txt");
                enviarAlertaSMS(numeros, mensajeAlerta);

            }
        });

    }

    private List<String> leerNumerosDeArchivo(String archivo) {
        List<String> numeros = new ArrayList<>();
        try {
            InputStream is = getAssets().open(archivo);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String linea;
            while ((linea = br.readLine()) != null) {
                numeros.add(linea);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return numeros;
    }
    private void enviarAlertaSMS(List<String> numeros, String mensajeAlerta) {
        SmsManager smsManager = SmsManager.getDefault();
        for (String numero : numeros) {
            smsManager.sendTextMessage(numero, null, mensajeAlerta, null, null);
        }
    }
}