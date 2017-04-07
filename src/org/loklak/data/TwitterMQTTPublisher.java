package org.loklak.data;

import org.loklak.objects.MessageEntry;

import java.util.ArrayList;


public class TwitterMQTTPublisher extends AbstractMQTTPublisher {

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
}
