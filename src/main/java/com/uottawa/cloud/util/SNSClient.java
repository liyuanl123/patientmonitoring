package com.uottawa.cloud.util;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;

import java.util.Properties;

public class SNSClient {
    private static AmazonSNS sns = null;
    private static Properties prop = null;

    public static void setProp(Properties _prop) {
        prop = _prop;
    }

    public static AmazonSNS getSns() {
        if (Boolean.parseBoolean(prop.getProperty("disableAws"))) return null;
        if (sns == null) {
            BasicAWSCredentials basicAwsCredentials = new BasicAWSCredentials(prop.getProperty("awsAccessKey"),prop.getProperty("awsSecretKey"));
            sns = AmazonSNSClient
                    .builder()
                    .withRegion(prop.getProperty("awsRegion"))
                    .withCredentials(new AWSStaticCredentialsProvider(basicAwsCredentials))
                    .build();
        }
        return sns;
    }

    public static void write(String SNS_TOPIC_ARN, String message) {
        if (Boolean.parseBoolean(prop.getProperty("disableAws"))) {
            System.out.println("AWS disabled, log for debug: " + SNS_TOPIC_ARN + ", " + message);
            return;
        }
        PublishRequest publishRequest = new PublishRequest(SNS_TOPIC_ARN, message);
        PublishResult publishResult = getSns().publish(publishRequest);
        System.out.println("MessageId: " + publishResult.getMessageId());
    }
}
