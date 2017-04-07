package org.loklak.data;

import org.loklak.objects.MessageEntry;

import java.util.ArrayList;


public class TwitterMQTTPublisher extends AbstractMQTTPublisher {

    TwitterMQTTPublisher(String host, String port) throws InterruptedException {
        super(host, port);
    }

    @Override
    public ArrayList<String> getChannels(MessageEntry m) {
        return null;
    }
}
