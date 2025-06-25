package de.stadt.herne.eventApp.service;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Service;

@Service
public class MqttService {

    private final String brokerUrl = "tcp://mosquitto:1883";
    private final String clientId = "springboot-client";

    private MqttClient client;

    public MqttService() throws MqttException {
        client = new MqttClient(brokerUrl, clientId);
        client.connect();

        // S’abonner à un topic
        client.subscribe("test/topic", (topic, msg) -> {
            System.out.println("Message reçu sur " + topic + ": " + new String(msg.getPayload()));
        });
    }

    public void publish(String topic, String message) throws MqttException {
        MqttMessage mqttMessage = new MqttMessage(message.getBytes());
        mqttMessage.setQos(1);
        client.publish(topic, mqttMessage);
    }
}

