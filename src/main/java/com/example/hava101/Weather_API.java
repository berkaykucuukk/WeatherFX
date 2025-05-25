package com.example.hava101;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;
import org.json.JSONArray;

public class Weather_API {
    private static final String API_KEY = "4b1c82d32a5438c47dfa532b572588fe"; // OpenWeatherMap API anahtarınızı buraya ekleyin
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";
    private static final String FORECAST_URL = "https://api.openweathermap.org/data/2.5/forecast";
    private static final String AIR_QUALITY_URL = "https://api.openweathermap.org/data/2.5/air_pollution";



    public JSONObject getWeatherData(String city) {
        try {
            String urlString = String.format("%s?q=%s&appid=%s&units=metric&lang=tr", BASE_URL, city, API_KEY);
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JSONObject weatherData = new JSONObject(response.toString());
            System.out.println("Weather Data: " + weatherData.toString(2)); // Debug için JSON verisini yazdır
            return weatherData;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public JSONObject getForecastData(String city) {
        try {
            String urlString = String.format("%s?q=%s&appid=%s&units=metric&lang=tr", FORECAST_URL, city, API_KEY);
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JSONObject forecastData = new JSONObject(response.toString());
            System.out.println("Forecast Data: " + forecastData.toString(2));
            return forecastData;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public JSONObject getAirQualityData(double lat, double lon) {
        try {
            String urlString = String.format("%s?lat=%f&lon=%f&appid=%s", AIR_QUALITY_URL, lat, lon, API_KEY);
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JSONObject airQualityData = new JSONObject(response.toString());
            System.out.println("Air Quality Data: " + airQualityData.toString(2));
            return airQualityData;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



} 