package com.uottawa.cloud.util;

import com.google.gson.JsonObject;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.uottawa.cloud.model.PatientInfo;


public class Helpers {
    public static PatientInfo parsePatientInfo(PNMessageResult message) {
        System.out.println("Parsing PNMessageResult");
        JsonObject asJsonObject = message.getMessage().getAsJsonObject();
        String deviceID = asJsonObject.get("deviceID").getAsString();
        long epoch = asJsonObject.get("epoch").getAsLong();
        long interval = asJsonObject.get("Time").getAsLong();
        int detected = asJsonObject.get("Detected").getAsInt();
        double HeartBeat = asJsonObject.get("Range").getAsDouble();
        double breathRate = asJsonObject.get("BreathRate").getAsDouble();
        boolean fallDetected = asJsonObject.get("falldetected").getAsBoolean();
        System.out.println("deviceID:" + deviceID + ", epoch:" + epoch + ", interval:" + interval + ", activity:" + detected +
                ", HeartBeat:" + HeartBeat + ", breathrate:" + breathRate);

        PatientInfo patientInfo = new PatientInfo(deviceID, epoch, interval, "", HeartBeat, breathRate, fallDetected, detected);
        return patientInfo;
    }

    public enum QueryType {
        CREATE,
        INSERT,
        SELECT,
    }
}
