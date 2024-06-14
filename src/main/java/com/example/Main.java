package com.example;

import com.pi4j.util.Console;

public class Main {

    public static void main(String[] args) {
        Console console = new Console();
        
        // Create a DHT22 sensor instance
        DHT22SensorReader sensor = new DHT22SensorReader(4);
        
        // Read sensor data
        Pair<Double, Double> data = sensor.readData().orElseThrow();
        
        // Print temperature and humidity
        console.println("Temperature: " + data.getValue() + " °C");
        console.println("Humidity: " + data.getKey() + " %");
    }
}
