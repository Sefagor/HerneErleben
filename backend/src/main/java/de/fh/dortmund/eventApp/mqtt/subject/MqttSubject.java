package de.fh.dortmund.eventApp.mqtt.subject;

import de.fh.dortmund.eventApp.mqtt.observer.MqttMessageObserver;
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

    MqttObserverDispatcher dispatcher = new MqttObserverDispatcher();
    private final String broker = "tcp://mosquitto:1883";
    private final String topic = "infos/servertest";
    private final String clientID = "subscriber";

    public MqttSubject() {
        configClient();
    }

    private class MqttObserverDispatcher implements MqttCallback {


        private final List<MqttMessageObserver> observers = new ArrayList<>();

        public MqttObserverDispatcher() {

        }

        public void addObserver(MqttMessageObserver observer) {
            observers.add(observer);
        }

        public void removeObserver(MqttMessageObserver observer) {
            observers.remove(observer);
        }

        @Override
        public void connectionLost(Throwable cause) {
            System.out.println("Dispatcher test");
        }


        @Override
        public void messageArrived(String topic, MqttMessage msg) throws Exception {
            notifyObservers(msg, observers);


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
    public void notifyObservers(MqttMessage message, List<MqttMessageObserver> observers) {
        for (MqttMessageObserver obs : observers) {
            obs.onMessage(topic, new String(message.getPayload()));
        }
    }

    @Override
    public void addObserver(MqttMessageObserver observer) {
        dispatcher.addObserver(observer);
    }

    @Override
    public void removeObserver(MqttMessageObserver observer) {
        dispatcher.removeObserver(observer);
    }

}
