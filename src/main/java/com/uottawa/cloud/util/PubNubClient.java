package com.uottawa.cloud.util;

import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import com.uottawa.cloud.model.PatientInfo;

import java.util.Arrays;
import java.util.Properties;

public class PubNubClient {
    private static PubNub pubnub = null;
    private static Properties prop = null;

    public static void setProp(Properties _prop) {
        prop = _prop;
    }

    public static PubNub getPubNub() {
        if (pubnub == null) {
            PNConfiguration pnConfiguration = new PNConfiguration();
            pnConfiguration.setSubscribeKey(prop.getProperty("pubnubSubKey"));
            pnConfiguration.setPublishKey(prop.getProperty("pubnubPubKey"));
            pnConfiguration.setUuid(prop.getProperty("pubnubUUID"));
            pubnub = new PubNub(pnConfiguration);
        }

        return pubnub;
    }

    public static void registerListener() {
        System.out.println("Registering PubNub Listener ...");
        PubNubClient.getPubNub().addListener(new SubscribeCallback() {
            @Override
            public void status(PubNub pubnub, PNStatus status) {
                if (status.getCategory() == PNStatusCategory.PNConnectedCategory) {
                    System.out.println("Connected...");
                }
            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {
                // TODO Auto-generated method stub
            }

            @Override
            public void message(PubNub pubnub, PNMessageResult message) {
                System.out.println("Hear message:" + message.toString());
                try {
                    PatientInfo patientInfo = Helpers.parsePatientInfo(message);

                    IOServer.write(prop.getProperty("socketChannel"), patientInfo);
                    // If aws disabled, calling the following two functions does nothing.
                    ConnectToRedshiftCluster.insertTableInRedshift(patientInfo);
                    // DynamoClient.write(prop.getProperty("awsTable"), patientInfo);
                    if(patientInfo.getFallDetected() == true) {
                        SNSClient.write(prop.getProperty("pubnubTopic"), "\"My text published to SNS topic with SMS endpoint\"");
                    }
                } catch (Exception e) {
                    System.err.println("Unable to add item, error:" + e.getMessage());
                }
            }
        });

        PubNubClient.getPubNub().subscribe().channels(Arrays.asList(prop.getProperty("pubnubChannel"))).execute();
    }
}
