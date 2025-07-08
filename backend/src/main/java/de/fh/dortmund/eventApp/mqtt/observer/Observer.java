package de.fh.dortmund.eventApp.mqtt.observer;

public interface Observer {
    void onMessage(String topic, String payload);
}