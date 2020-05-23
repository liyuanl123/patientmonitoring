package com.uottawa.cloud.util;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import com.uottawa.cloud.model.PatientInfo;

import java.util.HashMap;
import java.util.Properties;

public class IOServer {
    // TODO thread safe?
    private static HashMap<String, SocketIOServer> servers = new HashMap();
    private static Properties prop = null;

    public static void setProp(Properties _prop) {
        prop = _prop;
    }

    public static SocketIOServer getSocketIOServer(String host) {
        if (!servers.containsKey(host)) {
            System.out.println("Setup socketIO server ...");
            Configuration config = new Configuration();
            config.setHostname(host);
            config.setPort(9094);
            servers.put(host, new SocketIOServer(config));
        }
        return servers.get(host);
    }

    public static void write(String channel, PatientInfo patientInfo) {
        getSocketIOServer(prop.getProperty("socketHost"))
                .getBroadcastOperations()  // TODO: broadcast?
                .sendEvent(channel, patientInfo);
    }
}
