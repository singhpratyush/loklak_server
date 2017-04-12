package org.loklak.data;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.CountDownLatch;
import net.sf.xenqtt.client.AsyncClientListener;
import net.sf.xenqtt.client.AsyncMqttClient;
import net.sf.xenqtt.client.MqttClient;
import net.sf.xenqtt.client.PublishMessage;
import net.sf.xenqtt.client.Subscription;
import net.sf.xenqtt.message.ConnectReturnCode;
import net.sf.xenqtt.message.QoS;
import org.eclipse.jetty.util.log.Log;
import org.loklak.objects.MessageEntry;


public abstract class AbstractMQTTPublisher {

    private AsyncMqttClient mqttClient;
    private boolean status = false;

    AbstractMQTTPublisher(String host, String port) throws InterruptedException {

        String url = host + ":" + port;

        AsyncClientListener listener = new AsyncClientListener() {

            @Override
            public void publishReceived(MqttClient client, PublishMessage message) {
                Log.getLog().warn("Received a message when no subscriptions were active. Check your broker ;)");
            }

            @Override
            public void disconnected(MqttClient client, Throwable cause, boolean reconnecting) {
                if (cause != null) {
                    Log.getLog().warn("Disconnected from the broker due to an exception.", cause);
                } else {
                    Log.getLog().info("Disconnected from the broker.");
                }
                if (reconnecting) {
                    Log.getLog().info("Attempting to reconnect to the broker.");
                }
            }

            @Override
            public void connected(MqttClient client, ConnectReturnCode returnCode) {
                Log.getLog().info("Connected to client " + client + " with return code " + returnCode);
            }

            @Override
            public void subscribed(MqttClient client, Subscription[] requestedSubscriptions, Subscription[] grantedSubscriptions, boolean requestsGranted) {
            }

            @Override
            public void unsubscribed(MqttClient client, String[] topics) {
            }

            @Override
            public void published(MqttClient client, PublishMessage message) {
            }

        };

        this.mqttClient = new AsyncMqttClient(url, listener, 5);
        this.mqttClient.connect("loklak_server", false);
        this.status = true;
    }

    AbstractMQTTPublisher () throws InterruptedException {
        this("tcp://127.0.0.1", "1883");
    }

    public boolean isReady() {
        return this.status;
    }

    public AsyncMqttClient getClient() {
        return this.mqttClient;
    }

    public void publish(MessageEntry message) {
        List<String> channels = this.getChannels(message);
        for(String channel: channels) {
            this.publish_real(channel, message.toJSON().toString());
        }
        Log.getLog().info("Published message " + message);
    }

    public void publish_real(String channel, String message) {
        this.mqttClient.publish(new PublishMessage(channel, QoS.AT_MOST_ONCE, message));
    }

    public abstract List<String> getChannels(MessageEntry m);
}
