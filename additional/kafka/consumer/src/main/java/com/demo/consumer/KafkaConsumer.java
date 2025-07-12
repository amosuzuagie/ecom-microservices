//package com.demo.consumer;
//
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Component;
//
//@Component
//public class KafkaConsumer {
//
//    @KafkaListener(topics = "my-topic", groupId = "new-group")
//    public void listen1(String message) {
//        System.out.println("Received Message 1: " + message);
//    }
//
//    @KafkaListener(topics = "my-topic", groupId = "new-group")
//    public void listen2(String message) {
//        System.out.println("Received Message 2: " + message);
//    }
//
//    @KafkaListener(topics = "my-new-topic-1", groupId = "riders-group")
//    public void listenToRidersLocation(RiderLocation location) {
//        System.out.println("Received Location: " + location.getRiderId() + " : " + location.getLatitude() + " : " + location.getLongitude());
//    }
//}
