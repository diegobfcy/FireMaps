package com.example.nasaspaceapps_proyect;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.text.DecimalFormat;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AdminScreenMessageSend extends AppCompatActivity {
    double latitudeAtt;
    double longitudAtt;

    double latitudeFija = -8.94431;
    double longitudFija = -75.15337;
    private Button alertButton;

    private static final int SMS_PERMISSION_REQUEST_CODE = 1;

    private double calcularDistancia(double latitudVariable, double longitudVariable) {
        // Crear objetos de ubicación para las coordenadas
        Location ubicacionFija = new Location("Fija");
        ubicacionFija.setLatitude(latitudeFija);
        ubicacionFija.setLongitude(longitudFija);

        Location ubicacionVariable = new Location("Variable");
        ubicacionVariable.setLatitude(latitudVariable);
        ubicacionVariable.setLongitude(longitudVariable);

        // Calcular la distancia entre las dos ubicaciones en metros
        float distanciaMetros = ubicacionFija.distanceTo(ubicacionVariable);

        // Calcular la dirección (norte, sur, este, oeste)

        String direccion = obtenerDireccion(latitudVariable, longitudVariable);


        // Convertir la distancia a kilómetros
        double distanciaKm = distanciaMetros / 1000.0;
        //Log.d("BUG",String.format("¡Alerta de incendio en tu área! Distancia aproximada: %s km.", distanciaKm));
        // Crear el mensaje de alerta con la distancia y la dirección

        //return String.format("¡Alerta de incendio en tu área! Distancia aproximada: %s km al", distanciaKm);
        return distanciaKm;
    }

    private String obtenerDireccion(double latitudVariable, double longitudVariable) {
        double deltaLatitud = latitudVariable - latitudeFija;
        double deltaLongitud = longitudVariable - longitudFija;  // Tenías un typo aquí (logitudFija)

        if (Math.abs(deltaLatitud) < 0.0001 && Math.abs(deltaLongitud) < 0.0001) {
            return "la misma ubicación";
        } else if (deltaLatitud > 0) {
            if (deltaLongitud > 0) {
                return "noreste";
            } else if (deltaLongitud < 0) {
                return "noroeste";
            } else {
                return "norte";
            }
        } else if (deltaLatitud < 0) {
            if (deltaLongitud > 0) {
                return "sureste";
            } else if (deltaLongitud < 0) {
                return "suroeste";
            } else {
                return "sur";
            }
        } else {
            if (deltaLongitud > 0) {
                return "este";
            } else {
                return "oeste";
            }
        }
    }

    private double calcularTiempoLlegadaFuego(double velocidadViento, double temperatura, double distanciaKm) {
        // Definir una velocidad de propagación base (puedes ajustar este valor)
        double velocidadPropagacionBase = 10.0; // Kilómetros por hora (ejemplo)

        // Calcular la velocidad de propagación del fuego en función de la velocidad del viento y la temperatura
        // Puedes ajustar esta fórmula según tus necesidades
        double velocidadPropagacion = velocidadPropagacionBase + (velocidadViento * 0.5) - (temperatura * 0.1);

        // Calcular el tiempo en horas
        double tiempoHoras = distanciaKm / velocidadPropagacion;

        // Limitar a un decimal
        tiempoHoras = Math.round(tiempoHoras * 10.0) / 10.0;

        return tiempoHoras;
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
        Log.d("Ubicacion",""+longitudAtt+latitudeAtt);
        Log.d("DIRECCION",obtenerDireccion(latitudeAtt,longitudAtt));

        locationInput.setText(latitude + ", " + longitude);

        alertButton = findViewById(R.id.alertButton);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_REQUEST_CODE);
        }
        alertButton.setOnClickListener(new View.OnClickListener() {
            // Aquí puedes poner el código que se ejecutará cuando se haga clic en el botón
            @Override
            public void onClick(View view){

                double distanciaKm = calcularDistancia(latitudeAtt, longitudAtt);
                String direccion = obtenerDireccion(latitudeAtt,longitudAtt);
                String mensajeAlerta = "¡Alerta de fuego!, Distancia: " + distanciaKm + " - Dirección: " + direccion + ".";
                List<String> numeros = leerNumerosDeArchivo("numeros.txt");

                enviarAlertaSMS(numeros, mensajeAlerta);
                Toast.makeText(AdminScreenMessageSend.this, "Alerta enviada a los pobladores", Toast.LENGTH_SHORT).show();



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
            Log.d("bug",numero);
            smsManager.sendTextMessage(numero, null, mensajeAlerta, null, null);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SMS_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido
            } else {
                // Permiso denegado, puedes manejarlo aquí
            }
        }
    }


    
}