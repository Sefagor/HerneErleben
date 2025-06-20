package de.fh.dortmund.eventApp.mqtt.subject;

import de.fh.dortmund.eventApp.mqtt.observer.MqttMessageObserver;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.List;

public interface Subject {
    void notifyObservers(MqttMessage message, List<MqttMessageObserver> observers);

    void addObserver(MqttMessageObserver observer);

    void removeObserver(MqttMessageObserver observer);

}
