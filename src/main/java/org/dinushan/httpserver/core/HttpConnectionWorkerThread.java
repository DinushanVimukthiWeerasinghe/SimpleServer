package org.dinushan.httpserver.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;

public class HttpConnectionWorkerThread extends Thread{
    //socket is used to create a socket for each request and pass it to the HttpConnectionWorkerThread to process the request and send the response back
    private Socket socket;
    //webRoot is used to get the webroot from the configuration file
    private String webRoot;
    //Logger is used to log the information to the console
    private final static Logger LOGGER = (Logger) LoggerFactory.getLogger(HttpConnectionWorkerThread.class);
    //Constructor is used to initialize the socket and webroot
    public HttpConnectionWorkerThread(Socket socket,String webRoot) {
        this.socket = socket;
        this.webRoot = webRoot;
    }

    //run method is used to process the request and send the response back
    @Override
    public void run() {
        //inputStream is used to read the request from the socket
        InputStream inputStream = null;
        boolean IsNotFound = false;
        //outputStream is used to write the response to the socket
        OutputStream outputStream = null;
        try {
            LOGGER.info("Request received from the client");
            //inputStream is created from the socket
            inputStream = socket.getInputStream();
            //outputStream is created from the socket
            outputStream = socket.getOutputStream();
            //readRequest method is used to read the request from the socket
            String request = readRequest(inputStream);
            //method is used to get the method from the request
            String method = request.split(" ")[0];
            //path is used to get the path from the request
            String path = null;
            boolean IsImage=false;
            //Check whether the request contains a path
            if (request.contains(" ")) {
                path = request.split(" ")[1];
            } else {
                path = request;
            }
            //version is used to get the version from the request
            //Declare file to be null
            File file = null;
            String ContentType = null;
            //Check whether the path is a directory or a file
            FileManager fileManager = new FileManager();
            //If the path is /, then index.html is used as the path
            //Read the file and create the response
            final String CRLF = "\n\r"; // 13,10
            //String builder is used to create the response
            StringBuilder HtmlPage = new StringBuilder();
            LOGGER.info(path);
            if (method.equals("GET")) {
                //If the file exists, then the response is created
                assert false;
                if (fileManager.IsFileExist(webRoot, path)) {
                    //If the path is /, then index.html is used as the path
                    if (path.equals("/")) {
                        file = new File(webRoot + "/index.html");
                        LOGGER.info("File found");
                        ContentType = "text/html";
                    }
                    //If the path is /favicon.ico, then favicon.ico is used as the path
                    else if (path.equals("/favicon.ico")) {
                        IsImage=true;
                        file = new File(webRoot + "/favicon.ico");
                        ContentType = "image/x-icon";
                    }
                    //If the file is an CSS
                    else if (fileManager.IsCSS(path)) {
                        file = new File(webRoot + path);
                        ContentType = "text/css";
                    }
                    else if (fileManager.IsJS(path)) {
                        file = new File(webRoot + path);
                        ContentType = "text/javascript";
                    }
                    //If Requested File is HTML the ContentType is set to 'text/html'
                    else if (fileManager.IsHTML(path)) {
                        file = new File(webRoot + path);
                        ContentType = "text/html";
                    }
                    //If Requested File is HTML the ContentType is set to 'text/plain'
                    else {
                        file = new File(webRoot + path);
                        ContentType = "text/plain";
                    }
                } else {
                    file = new File(webRoot + 404 + ".html");
                    ContentType = "text/html";
                    IsNotFound = true;
                }
                try {
                    if(!IsImage)
                    {
                        //Read the file and create the response using the string builder
                        FileReader fileReader = new FileReader(file);
                        //BufferedReader is used to read the file
                        BufferedReader in = new BufferedReader(fileReader);
                        //Read the file line by line and append it to the string builder
                        String str;
                        while ((str = in.readLine()) != null) {
                            HtmlPage.append(str);
                        }
                        String html = HtmlPage.toString();
                        html += " ";
                        //Declare the CR and LF as constants. CR is used to indicate the end of the line and LF is used to indicate the end of the header

                        //Create the response header. Below is the format of the response header
                        String response = "HTTP/1.1 "+ (IsNotFound?"404":"200")+" OK" + CRLF + // Status Line : HTTP Version, Status Code, Response Message
                            "Content-Length: " + html.getBytes().length + CRLF + // Response Header
                            "Content-Type: " + ContentType + CRLF + // Response Header
                            CRLF +
                            html +
                            CRLF;
                    //Write the response to the socket
                        outputStream.write(response.getBytes());
                        }
                    }
                    // If any error occurs, the error is logged
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //If any Exception occurs, then the error is logged
        catch (IOException e) {
            LOGGER.error("Error while Communication", e);
            e.printStackTrace();
        }
        finally {
            //Close the socket and the streams after the response is sent
            if (inputStream != null) {
                try {
                    //Close the input stream
                    inputStream.close();
                } catch (IOException ignored) {}
            }
            if (outputStream != null) {
                try {
                    //Close the outputStream
                    outputStream.close();
                } catch (IOException ignored) {}
            }
            if (socket != null) {
                try {
                    //Close the socket
                    socket.close();
                } catch (IOException ignored) {}
            }
        }


    }

    //readRequest method is used to read the request from the socket
    private String readRequest(InputStream inputStream) {
        //Buffered reader is used to read the request from the socket
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        //String builder is used to create the request
        StringBuilder request = new StringBuilder();
        String line;
        try {
            //While loop is used to read the request from the socket and create the request
            while ((line = bufferedReader.readLine()) != null) {
                //If the line is empty, then the request is completed
                request.append(line);

                if (line.isEmpty()) {
                    break;
                }
            }
        } catch (IOException e) {
            LOGGER.error("Error while reading request", e);
        }
        //Return the request
        return request.toString();
    }

}
