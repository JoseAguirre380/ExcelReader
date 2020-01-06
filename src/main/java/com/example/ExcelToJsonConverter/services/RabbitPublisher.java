package com.example.ExcelToJsonConverter.services;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitPublisher {

    private final static String QUEUE_NAME = "queue.log.messages.anonymous.rds7E-3UTWiLDliOXXX11A";

    public static void rabbitPulblisher(String jsonString) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.basicPublish("", QUEUE_NAME, null, jsonString.getBytes("UTF-8"));
        }
    }
}


