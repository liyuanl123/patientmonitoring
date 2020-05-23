package com.uottawa.cloud;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.uottawa.cloud.util.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

@SpringBootApplication
public class App {
    public static void main(String[] args) throws Exception {
        System.out.println(args.length);
        Properties prop = new Properties();
        if (args.length == 0) {
            prop.load(new InputStreamReader(App.class.getResourceAsStream("/config.properties")));
        } else if (args.length == 1) {
            File configFile = new File(args[0]);
            InputStream stream = new FileInputStream(configFile);
            prop.load(stream);
        } else {
            throw new Exception("At most one argument should be provided.");
        }
        System.out.println(prop.getProperty("pubnubTopic"));
        System.out.println(Boolean.parseBoolean(prop.getProperty("disableAws")));
        // Set properties
        PubNubClient.setProp(prop);
        DynamoClient.setProp(prop);
        SNSClient.setProp(prop);
        IOServer.setProp(prop);
        // Start socket IO
        IOServer.getSocketIOServer(prop.getProperty("socketHost")).start();
        PubNubClient.registerListener();
        ConnectToRedshiftCluster.setProp(prop);
        ConnectToRedshiftCluster.createTableInRedshift();
        SpringApplication.run(App.class, args);
    }
}
