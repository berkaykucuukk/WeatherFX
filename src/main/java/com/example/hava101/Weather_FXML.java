package com.example.hava101;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Weather_FXML {
    @FXML private TextField citySearchField;
    @FXML private Label pressureLabel;
    @FXML private Label sunriseClockLabel;
    @FXML private Label sunsetClockLabel;
    @FXML private Label windLabel;
    @FXML private Label feelsLikeLabel;
    @FXML private Label humidityLabel;
    @FXML private Label airQualityLabel;
    @FXML private Label currrentCityLabel;
    @FXML private Label userNameLabel;
    @FXML private Label currentDate;
    @FXML private Label currentTemparatureLabel;
    @FXML private Label currentWeatherCondition;
    @FXML private ImageView currentImage;
    @FXML private ImageView sunriseImage;
    @FXML private ImageView sunsetImage;
    
    // Forecast labels
    @FXML private Label forecastDay1, forecastDay2, forecastDay3, forecastDay4, forecastDay5, forecastDay6, forecastDay7;
    @FXML private Label forecastTemparature1, forecastTemparature2, forecastTemparature3, forecastTemparature4, 
                        forecastTemparature5, forecastTemparature6, forecastTemparature7;
    @FXML private ImageView forecastImage1, forecastImage2, forecastImage3, forecastImage4, 
                           forecastImage5, forecastImage6, forecastImage7;
    
    // Today's forecast
    @FXML private Label todaysClock1, todaysClock2, todaysClock3, todaysClock4;
    @FXML private Label todaysForecastTemparature1, todaysForecastTemparature2, todaysForecastTemparature3, todaysForecastTemparature4;
    @FXML private ImageView todaysForecastImage1, todaysForecastImage2, todaysForecastImage3, todaysForecastImage4;
    @FXML private Button logOutButton;







    private Weather_API weatherAPI;
    private String currentUser = null; // I'm not giving any name before starting because I will update it with file.

    public void setCurrentUser(String username) {  // In LoginController.java , I'm setting the current user.
        this.currentUser = username; // In here current user will be choosen from file.
        
        initialize(); // Then the login screen comes.
    }
    
    @FXML  // FXML annotation is used here. It runs automatically after FXML is loaded.
    public void initialize() {
        weatherAPI = new Weather_API();

        String city = "İstanbul"; // To fix unknown city problem.
        try {
            if (currentUser != null && Files.exists(Paths.get("users.txt"))) {
                List<String> lines = Files.readAllLines(Paths.get("users.txt"));  // In here I open the users.txt file and read all lines. Every line will be String then I put them into a list.
                for (String line : lines) {
                    String[] parts = line.split(":");  // In here I split the line with ":" and put them into a parts array.
                    if (parts.length == 3 && parts[0].equals(currentUser)) {
                        city = parts[2]; // It will give the city name from the file.
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        updateWeatherData(city);
        updateCurrentDate();
    }
    
    
    
    @FXML  // If I write any city name into the text field, the weather data will be updated with this function. In fxml file, textfield is connected to this method.
    private void searchCity() {
        String city = citySearchField.getText().trim();
        if (!city.isEmpty()) {

            updateWeatherData(city);
            citySearchField.clear();
        }
    }

    public void updateWeatherData(String city) {
        JSONObject weatherData = weatherAPI.getWeatherData(city); // Right hand side takes the API data and loads it into the weatherData variable.
        if (weatherData != null) { // If weatherData would be loaded do this below methods.           
            updateCurrentWeather(weatherData);
            updateForecast(weatherData);
            updateTodayForecast(weatherData);
        }
    }
    
    private void updateCurrentDate() {  // This method gives the current date.

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM");
        currentDate.setText(LocalDateTime.now().format(formatter));
    }

    @FXML
    private void logOutButton() throws IOException { // To go login page.
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
        Stage stage = (Stage) logOutButton.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("WeatherFX - Login");
    }
    

    
    private void updateCurrentWeather(JSONObject weatherData) {
        JSONObject main = weatherData.getJSONObject("main");  // Takes the main part from API which includes temperature, humidity, etc.
        JSONObject wind = weatherData.getJSONObject("wind");  // Takes the wind part from API which includes wind speed.
        JSONObject sys = weatherData.getJSONObject("sys");  // Takes the sys part from API which includes sunrise and sunset time.
        JSONObject weather = weatherData.getJSONArray("weather").getJSONObject(0);  // Takes the weather part from API which includes weather condition.
        
        // Taking the values from the API and putting them into the parts. 
        double temp = main.getDouble("temp");
        int humidity = main.getInt("humidity");
        humidityLabel.setText(humidity + " %");
        currentTemparatureLabel.setText(String.format("%.0f", temp));
        currentWeatherCondition.setText(weather.getString("description"));
        
        // Weather condition icons
        String iconCode = weather.getString("icon");  // Takes the icon code from API.
        updateWeatherIcon(currentImage, iconCode);  // API icons changes with local ones. This method created by AI.
        
        // Wind speed
        double windSpeed = wind.getDouble("speed");
        windLabel.setText(String.format("%.1f km/h", windSpeed * 3.6)); // API gives the wind speed in m/s so I changed it to km/h. 
        
        // Feels like temperature
        double feelsLike = main.getDouble("feels_like");
        feelsLikeLabel.setText(String.format("%.0f°", feelsLike));
        
        // For air quality part taking the coordinates of the city.
        double lat = weatherData.getJSONObject("coord").getDouble("lat");
        double lon = weatherData.getJSONObject("coord").getDouble("lon");
        
        // Air quality
        JSONObject airQualityData = weatherAPI.getAirQualityData(lat, lon);
        if (airQualityData != null) {
            int aqi = airQualityData.getJSONArray("list").getJSONObject(0)
                    .getJSONObject("main").getInt("aqi");
            airQualityLabel.setText(String.valueOf(aqi));
        }
        
        // Pressure
        int pressure = main.getInt("pressure");
        pressureLabel.setText(String.format("%d hPa", pressure));
        
        // Sunrise/sunset part  // Made by AI.
        long sunrise = sys.getLong("sunrise");
        long sunset = sys.getLong("sunset");
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");
        sunriseClockLabel.setText(LocalDateTime.ofEpochSecond(sunrise, 0, java.time.ZoneOffset.UTC)
                .format(formatter));
        sunsetClockLabel.setText(LocalDateTime.ofEpochSecond(sunset, 0, java.time.ZoneOffset.UTC)
                .format(formatter));
        
        // Set sunrise and sunset images
        try {
            Image sunriseImg = new Image(getClass().getResourceAsStream("/images/sunrise.png"));
            Image sunsetImg = new Image(getClass().getResourceAsStream("/images/sunset.png"));
            sunriseImage.setImage(sunriseImg);
            sunsetImage.setImage(sunsetImg);
        } catch (Exception e) {
            System.out.println("Sunrise/Sunset images could not be loaded");
            e.printStackTrace();
        }


        
        // City name
        currrentCityLabel.setText(weatherData.getString("name") + ", " + 
                                weatherData.getJSONObject("sys").getString("country"));
    }
    
    private void updateWeatherIcon(ImageView imageView, String iconCode) {  // Changing the icons with local ones. 
        String imagePath = "/images/";
        
        switch (iconCode) {
            case "01d": // sunny day
            case "01n": // sunny night
                imagePath += "sun.png";
                break;
            case "02d": // partly cloudy day
            case "02n": // partly cloudy night
            case "03d": // scattered clouds day
            case "03n": // scattered clouds night
            case "04d": // cloudy day
            case "04n": // cloudy night
                imagePath += "cloudy.png";
                break;
            case "09d": // light rain day
            case "09n": // light rain night
            case "10d": // rain day
            case "10n": // rain night
                imagePath += "raining.png";
                break;
            case "11d": // thunderstorm day
            case "11n": // thunderstorm night
                imagePath += "thunderstorm.png";
                break;
            case "13d": // snow day
            case "13n": // snow night
                imagePath += "snow.png";
                break;
            case "50d": // mist day
            case "50n": // mist night
                imagePath += "mist.png";
                break;
            default:
                imagePath += "sun.png"; // Default is sunny day.
                break;
        }
        
        try {
            Image image = new Image(getClass().getResourceAsStream(imagePath));
            imageView.setImage(image);
        } catch (Exception e) {
            System.out.println("Image could not be loaded: " + imagePath);
            e.printStackTrace();
        }
    }
    
    private void updateForecast(JSONObject weatherData) {
        JSONObject forecastData = weatherAPI.getForecastData(weatherData.getString("name")); // I get the next 5 days forecast data from API.
        // It gives every 3 hours data. Icons, temperatures, etc.
        if (forecastData == null) return;

        JSONArray list = forecastData.getJSONArray("list");  // I send the data to list object.
        LocalDateTime now = LocalDateTime.now();
        
        // Get tomorrow's date

        LocalDateTime tomorrow = now.plusDays(1);
        
        // Create arrays for the next 5 days

        String[] days = new String[5];
        String[] temps = new String[5];
        String[] iconCodes = new String[5];
        
        // Initialize with tomorrow's date
        for (int i = 0; i < 5; i++) {
            LocalDateTime forecastDate = tomorrow.plusDays(i);
            days[i] = forecastDate.format(DateTimeFormatter.ofPattern("EEE"));
            
            // Find the forecast data for this day (using noon data)
            double temp = 0;  // I initialize the temperature to 0.
            String iconCode = "01d";  // I initialize the icon code to sunny day.
            int count = 0;  // I initialize the count to 0. // We will use this count thing to find the average temperature.
            
            for (int j = 0; j < list.length(); j++) {     // This part made by AI.
                JSONObject item = list.getJSONObject(j);
                LocalDateTime itemDate = LocalDateTime.ofEpochSecond(
                    item.getLong("dt"), 0, java.time.ZoneOffset.UTC);
                
                if (itemDate.toLocalDate().equals(forecastDate.toLocalDate())) {
                    temp += item.getJSONObject("main").getDouble("temp");
                    iconCode = item.getJSONArray("weather").getJSONObject(0).getString("icon");
                    count++;
                }
            }
            
            if (count > 0) {   // 
                temp = temp / count; // Calculate average temperature
                temps[i] = String.format("%.0f°", temp);
                iconCodes[i] = iconCode;
            } else {
                temps[i] = "N/A";
                iconCodes[i] = "01d";
            }
        }
        
        Label[] dayLabels = {forecastDay1, forecastDay2, forecastDay3, forecastDay4, forecastDay5};
        Label[] tempLabels = {forecastTemparature1, forecastTemparature2, forecastTemparature3, 
                            forecastTemparature4, forecastTemparature5};
        ImageView[] imageViews = {forecastImage1, forecastImage2, forecastImage3, forecastImage4,
                                forecastImage5};
        
        // Update first 5 days
        for (int i = 0; i < 5; i++) {
            dayLabels[i].setText(days[i]);
            tempLabels[i].setText(temps[i]);
            updateWeatherIcon(imageViews[i], iconCodes[i]);
        }
        
        // Başlangıçta 7 günün verisini çekmek istemiştim fakat API 5 gün verdiği için burada son iki günü devre dışı bırakıyorum.
        forecastDay6.setVisible(false);
        forecastDay7.setVisible(false);
        forecastTemparature6.setVisible(false);
        forecastTemparature7.setVisible(false);
        forecastImage6.setVisible(false);
        forecastImage7.setVisible(false);
    }
    
    private void updateTodayForecast(JSONObject weatherData) {
        JSONObject forecastData = weatherAPI.getForecastData(weatherData.getString("name"));
        if (forecastData == null) return;

        JSONArray list = forecastData.getJSONArray("list");
        LocalDateTime now = LocalDateTime.now();
        
        // Get the next 3 hours of forecast
        String[] times = new String[4];
        String[] temps = new String[4];
        String[] iconCodes = new String[4];
        
        // First entry is current weather
        times[0] = "Now";
        temps[0] = String.format("%.0f°", weatherData.getJSONObject("main").getDouble("temp"));
        iconCodes[0] = weatherData.getJSONArray("weather").getJSONObject(0).getString("icon");
        
        // Get next 3 hours
        for (int i = 1; i < 4; i++) {
            LocalDateTime targetTime = now.plusHours(i);
            boolean foundData = false;
            
            // Find the closest forecast data point        // This part made by AI.
            for (int j = 0; j < list.length(); j++) {
                JSONObject item = list.getJSONObject(j);
                LocalDateTime itemDate = LocalDateTime.ofEpochSecond(
                    item.getLong("dt"), 0, java.time.ZoneOffset.UTC);
                
                if (itemDate.getHour() == targetTime.getHour() && 
                    itemDate.getDayOfMonth() == targetTime.getDayOfMonth()) {
                    times[i] = targetTime.format(DateTimeFormatter.ofPattern("HH:mm"));
                    temps[i] = String.format("%.0f°", item.getJSONObject("main").getDouble("temp"));
                    iconCodes[i] = item.getJSONArray("weather").getJSONObject(0).getString("icon");
                    foundData = true;
                    break;
                }
            }
            
            // If no data found for this hour, use the last available data
            if (!foundData && i > 0) {
                times[i] = targetTime.format(DateTimeFormatter.ofPattern("HH:mm"));
                temps[i] = temps[i-1]; // Use previous temperature
                iconCodes[i] = iconCodes[i-1]; // Use previous icon
            }
        }
        
        Label[] timeLabels = {todaysClock1, todaysClock2, todaysClock3, todaysClock4};
        Label[] tempLabels = {todaysForecastTemparature1, todaysForecastTemparature2, 
                            todaysForecastTemparature3, todaysForecastTemparature4};
        ImageView[] imageViews = {todaysForecastImage1, todaysForecastImage2, 
                                todaysForecastImage3, todaysForecastImage4};
        
        for (int i = 0; i < 4; i++) {
            if (times[i] != null && temps[i] != null && iconCodes[i] != null) {
                timeLabels[i].setText(times[i]);
                tempLabels[i].setText(temps[i]);
                updateWeatherIcon(imageViews[i], iconCodes[i]);
            }
        }
    }
} 