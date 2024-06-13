package com.example;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalOutput;
import com.pi4j.io.gpio.digital.DigitalOutputConfig;
import com.pi4j.io.gpio.digital.DigitalState;

public class TestPiGpio {
    private static final int GPIO_PIN = 4;  // Example GPIO pin

    public static void main(String[] args) {
        Context pi4j = Pi4J.newAutoContext();

        DigitalOutputConfig outputConfig = DigitalOutput.newConfigBuilder(pi4j)
                .id("test-output")
                .name("Test Output")
                .address(GPIO_PIN)
                .shutdown(DigitalState.LOW)
                .initial(DigitalState.HIGH)
                .provider("pigpio-digital-output")
                .build();

        DigitalOutput output = pi4j.create(outputConfig);

        output.low();
        System.out.println("GPIO pin set to LOW");
    }
}
