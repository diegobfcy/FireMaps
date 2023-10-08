package com.example.nasaspaceapps_proyect;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FirmsDataFetcher {
    private static final OkHttpClient client = new OkHttpClient();

    public String fetchData(String mapKey, String source, String areaCoordinates, String dayRange) {
        String url = "https://firms.modaps.eosdis.nasa.gov/api/area/csv/" + mapKey + "/" + source + "/" + areaCoordinates + "/" + dayRange;

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                return "Error: " + response.code();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
}
