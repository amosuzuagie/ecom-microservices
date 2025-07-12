package com.demo.producer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;
import java.util.function.Supplier;

@Configuration
public class KafkaProducerStream {

    @Bean
    public Supplier<RiderLocation> sendRiderLocation() {
        Random random = new Random();
        return () -> {
            String riderId = "rider" + random.nextInt(20);
            RiderLocation location = new RiderLocation(riderId, 73.4, 37.8);
            System.out.println("Sending: " + location.getRiderId());
            return location;
        };
    }

    @Bean
    public Supplier<String> sendRiderStatus() {
        Random random = new Random();
        return () -> {
            String riderId = "rider" + random.nextInt(20);
            RiderLocation location = new RiderLocation(riderId, 73.4, 37.8);
            System.out.println("Sending: " + location.getRiderId());
            return location.getRiderId() + " Completed";
        };
    }

}
