package com.example;

public class Main {

    public static void main(String[] args) {
        // Create a DHT22 sensor instance
        DHT22SensorReader sensor = new DHT22SensorReader(4);
        
        // Read sensor data
        Pair<Double, Double> data = sensor.readData().orElseThrow();
        
        // Print temperature and humidity
        System.out.println("Temperature: " + data.getValue() + " °C");
        System.out.println("Humidity: " + data.getKey() + " %");
    }
}
