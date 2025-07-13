package de.fh.dortmund.eventApp.mqtt.subject;

import de.fh.dortmund.eventApp.mqtt.observer.Observer;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
@Component
public class MqttSubject implements Subject {
    private MqttClient client;

    private MqttObserverDispatcher dispatcher = new MqttObserverDispatcher();
    private final String broker = "tcp://mosquitto:1883";
    private final String topic = "infos/servertest";
    private final String clientID = "subscriber";
    private final List<Observer> observers = new ArrayList<>();

    public MqttSubject() {
        configClient();
    }

    private class MqttObserverDispatcher implements MqttCallback {

        public MqttObserverDispatcher() {

        }

        @Override
        public void connectionLost(Throwable cause) {

            System.out.println("Connection Lost");
        }


        @Override
        public void messageArrived(String topic, MqttMessage msg) throws Exception {
            notifyObservers(msg);
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
        }
    }


    private void configClient() {
        try {
            client = new MqttClient(broker, clientID, new MemoryPersistence());
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

    public void connect() {
        client.setCallback(dispatcher);
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(false);
        try {
            client.connect(options);
            client.subscribe(topic);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void notifyObservers(MqttMessage message) {
        for (Observer obs : observers) {
            obs.onMessage(topic, new String(message.getPayload()));
        }
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

}
