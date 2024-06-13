package com.example;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalInput;
import com.pi4j.io.gpio.digital.DigitalInputConfig;
import com.pi4j.io.gpio.digital.DigitalOutput;
import com.pi4j.io.gpio.digital.DigitalOutputConfig;
import com.pi4j.io.gpio.digital.DigitalState;

public class Main {
    private static final int GPIO_PIN = 4;  // GPIO pin number where the data pin of DHT22 is connected

    public static void main(String[] args) {
        // Initialize Pi4J context
        Context pi4j = Pi4J.newAutoContext();

        // Configure digital output for triggering the sensor
        DigitalOutputConfig outputConfig = DigitalOutput.newConfigBuilder(pi4j)
                .id("dht22-output")
                .name("DHT22 Output")
                .address(GPIO_PIN)
                .shutdown(DigitalState.LOW)
                .initial(DigitalState.HIGH)
                .provider("pigpio-digital-output")
                .build();

        DigitalOutput output = pi4j.create(outputConfig);

        // Configure digital input for reading sensor data
        DigitalInputConfig inputConfig = DigitalInput.newConfigBuilder(pi4j)
                .id("dht22-input")
                .name("DHT22 Input")
                .address(GPIO_PIN)
                .provider("pigpio-digital-input")
                .build();

        DigitalInput input = pi4j.create(inputConfig);

        // Read data from the sensor
        while (true) {
            try {
                // Trigger the sensor
                output.low();
                Thread.sleep(18);
                output.high();

                // Wait for the sensor response
                while (input.state() == DigitalState.HIGH);
                while (input.state() == DigitalState.LOW);
                while (input.state() == DigitalState.HIGH);

                // Read 40 bits of data
                int[] data = new int[5];
                for (int i = 0; i < 40; i++) {
                    while (input.state() == DigitalState.LOW);
                    long start = System.nanoTime();
                    while (input.state() == DigitalState.HIGH);
                    long duration = System.nanoTime() - start;
                    int bitIndex = i / 8;
                    data[bitIndex] <<= 1;
                    if (duration > 50) {
                        data[bitIndex] |= 1;
                    }
                }

                // Verify checksum
                int checksum = (data[0] + data[1] + data[2] + data[3]) & 0xFF;
                if (checksum == data[4]) {
                    int humidity = (data[0] << 8) + data[1];
                    int temperature = (data[2] << 8) + data[3];
                    System.out.printf("Humidity: %.1f%%, Temperature: %.1f°C%n", humidity / 10.0, temperature / 10.0);
                } else {
                    System.out.println("Checksum failed");
                }

            } catch (InterruptedException e) {
                System.out.println("Interrupted exception: " + e.getMessage());
                // Optionally handle the exception
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
                // Optionally handle the exception
            } finally {
                // Ensure we wait before reading again, even if an exception occurs
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    System.out.println("Interrupted exception during sleep: " + e.getMessage());
                    // Optionally handle the exception
                }
            }
        }
    }
}
