package de.fh.dortmund.eventApp.mqtt.subject;

import de.fh.dortmund.eventApp.mqtt.observer.Observer;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public interface Subject {
    void notifyObservers(MqttMessage message);

    void addObserver(Observer observer);

    void removeObserver(Observer observer);

}
