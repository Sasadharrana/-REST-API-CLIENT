package com.example;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class WeatherApp {
    private static final String API_KEY = "API_KEY"; // Replace with your OpenWeatherMap API
                                                                              // key
    private static final String BASE_URL = "BASE_URL";

    public static void main(String[] args) {
        String city = "London"; // You can change this to any city
        fetchWeatherData(city);
    }

    private static void fetchWeatherData(String city) {
        String url = String.format("%s?q=%s&appid=%s&units=metric", BASE_URL, city, API_KEY);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            HttpResponse response = httpClient.execute(request);

            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuilder jsonResponse = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonResponse.append(line);
            }

            parseAndDisplayWeatherData(jsonResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void parseAndDisplayWeatherData(String jsonResponse) {
        JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();

        String cityName = jsonObject.get("name").getAsString();
        String country = jsonObject.get("sys").getAsJsonObject().get("country").getAsString();
        double temperature = jsonObject.get("main").getAsJsonObject().get("temp").getAsDouble();
        String weatherDescription = jsonObject.get("weather").getAsJsonArray().get(0).getAsJsonObject()
                .get("description").getAsString();

        System.out.println("Weather in " + cityName + ", " + country + ":");
        System.out.println("Temperature: " + temperature + "°C");
        System.out.println("Description: " + weatherDescription);
    }
}
