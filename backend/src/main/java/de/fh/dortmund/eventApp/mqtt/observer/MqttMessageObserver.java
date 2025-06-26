package de.fh.dortmund.eventApp.mqtt.observer;

public interface MqttMessageObserver {
    void onMessage(String topic, String payload);
}