package org.loklak.data;

import java.util.ArrayList;
import org.loklak.objects.MessageEntry;

public class TwitterMQTTPublisher extends AbstractMQTTPublisher {

    public static TwitterMQTTPublisher publisher;

    TwitterMQTTPublisher(String host, String port) throws InterruptedException {
        super(host, port);
    }

    TwitterMQTTPublisher() throws InterruptedException {
        this("tcp://127.0.0.1", "1883");
    }

    @Override
    public ArrayList<String> getChannels(MessageEntry m) {
        ArrayList<String> channels = new ArrayList<>();
        channels.add("twitter");
        return channels;
    }

    public static void init(String host, String port) throws InterruptedException {
        publisher = new TwitterMQTTPublisher(host, port);
    }

    public static void init() throws InterruptedException {
        publisher = new TwitterMQTTPublisher();
    }
}
