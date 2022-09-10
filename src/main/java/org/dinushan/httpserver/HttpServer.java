package org.dinushan.httpserver;

import org.dinushan.httpserver.core.ServerListenerThread;
import org.dinushan.httpserver.configuration.Configuration;
import org.dinushan.httpserver.configuration.ConfigurationManager;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;


import java.io.IOException;

public class HttpServer {
    //Logger is used to log the information to the console
    private final static Logger LOGGER = LoggerFactory.getLogger(HttpServer.class);
    //Main method is the entry point of the application
    public static void main(String[] args) {
        LOGGER.info("Server starting ....");
        //ConfigurationManager is used to read the configuration file and create a Configuration object from the file in resources folder named http.json
        ConfigurationManager.getInstance().loadConfiguration("src/main/resources/http.json");
        //Configuration object is used to get the port and webroot from the configuration file
        Configuration configuration = ConfigurationManager.getInstance().getConfiguration();

        LOGGER.info("Server started on port " + configuration.getPort());
        LOGGER.info("Server started on WebRoot " + configuration.getWebRoot());
        //ServerListenerThread is used to listen to the port and create a HttpConnectionWorkerThread for each request
        ServerListenerThread serverListenerThread = null;
        try {
            //ServerListenerThread is created with the port and webroot
            serverListenerThread = new ServerListenerThread(configuration.getPort(), configuration.getWebRoot());
        } catch (IOException e) {
            //If there is an error while creating the ServerListenerThread, the error is logged and the application is terminated
            throw new RuntimeException(e);
        }
        //ServerListenerThread is started
        serverListenerThread.start();




    }
}