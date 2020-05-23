package com.uottawa.cloud.util;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.uottawa.cloud.model.PatientInfo;

import java.util.HashMap;
import java.util.Properties;

public class DynamoClient {
    private static DynamoDB dynamoDB = null;
    private static Properties prop = null;

    // TODO: thread-safe?
    private static HashMap<String, Table> tables = new HashMap();

    public static void setProp(Properties _prop) {
        prop = _prop;
    }

    public static DynamoDB getDynamoDB() {
        if (Boolean.parseBoolean(prop.getProperty("disableAws"))) return null;

        if (dynamoDB == null) {
            AmazonDynamoDB client = AmazonDynamoDBClientBuilder
                    .standard()
                    .withCredentials(
                            new AWSStaticCredentialsProvider(new BasicAWSCredentials(prop.getProperty("awsAccessKey"), prop.getProperty("awsSecretKey")))
                    )
                    .withEndpointConfiguration(
                            new AwsClientBuilder.EndpointConfiguration(prop.getProperty("awsEndpoint"), prop.getProperty("awsRegion"))
                    )
                    .build();

            dynamoDB = new DynamoDB(client);
        }

        return dynamoDB;
    }

    public static void write(String tableName, PatientInfo patientInfo) {
        System.out.println("table isEmpty:" + tables.isEmpty());
        if (Boolean.parseBoolean(prop.getProperty("disableAws"))) {
            System.out.println("AWS disabled, log for debug: " + patientInfo.toString());
            return;
        }

        if (!tables.containsKey(tableName)) {
            tables.put(tableName, getDynamoDB().getTable(tableName));
        }
        System.out.println("table:" + tables.get(tableName).toString());
        System.out.println("table isEmpty:" + tables.isEmpty());
        PutItemOutcome outcome = tables.get(tableName)
                .putItem(new Item()
                        .withPrimaryKey(
                                "deviceID", patientInfo.getDeviceID(),
                                "time", patientInfo.getEpoch())
                        .withDouble("range", patientInfo.getHeartRate()) // heart rate
                        .withDouble("breathRate", patientInfo.getBreathRate())
                        .withBoolean("fallDetected", patientInfo.getFallDetected())
                        .withInt("detected", patientInfo.getDetected())
                );
        System.out.println("PutItem succeeded:\n" + outcome.getPutItemResult());
    }
}
