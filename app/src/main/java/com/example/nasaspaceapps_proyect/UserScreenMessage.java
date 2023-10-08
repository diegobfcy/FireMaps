package com.example.nasaspaceapps_proyect;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class UserScreenMessage extends AppCompatActivity {
    private Button btnSMS;
    private static final int SMS_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_screen_message);

        btnSMS = findViewById(R.id.btnSMS);

        // Solicitar permiso para enviar SMS
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_REQUEST_CODE);
        }


        btnSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Coordenadas de prueba (cambia según tus necesidades)

                double latitudVariable = 10.9718; // Cambia esto con las coordenadas variables
                double longitudVariable = 67.5948; // Cambia esto con las coordenadas variables

                // Calcula la distancia y la dirección
                String mensajeAlerta = "¡Alerta de fuego cerca!";

                // Muestra un mensaje de éxito o cualquier otra retroalimentación al usuario
                Toast.makeText(UserScreenMessage.this, "Alerta enviada a los pobladores", Toast.LENGTH_SHORT).show();

                // Envia SMS con el mensaje de alerta
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