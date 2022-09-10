package org.dinushan.httpserver.core;

import org.dinushan.httpserver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerListenerThread extends Thread{
    //port is used to listen to the port
    private int port;
    //webRoot is used to get the webroot from the configuration file
    private String webRoot;
    //serverSocket is used to create a server socket
    private ServerSocket serverSocket;
    //Logger is used to log the information to the console
    private final static Logger LOGGER = (Logger) LoggerFactory.getLogger(HttpServer.class);

    //Constructor is used to initialize the port and webroot
    public ServerListenerThread(int port, String webRoot) throws IOException {
        this.port = port;
        this.webRoot = webRoot;
        this.serverSocket = new ServerSocket(this.port);
    }
    //run method is used to listen to the port and create a HttpConnectionWorkerThread for each request
    @Override
    public void run() {
        try {
            //while loop is used to listen to the port and create a HttpConnectionWorkerThread for each request until the server is stopped
            while (serverSocket.isBound() && !serverSocket.isClosed()) {
                //socket is used to create a socket for each request and pass it to the HttpConnectionWorkerThread to process the request and send the response back
                Socket socket = serverSocket.accept();
                //HttpConnectionWorkerThread is used to process the request and send the response back
                HttpConnectionWorkerThread httpConnectionWorkerThread = new HttpConnectionWorkerThread(socket,webRoot);
                //HttpConnectionWorkerThread is started
                httpConnectionWorkerThread.start();
            }
        //If there is an error while listening to the port, the error is logged and the application is terminated
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("Problem with setting Socket", e);
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException ignored) {}
            }
        }
    }
}
