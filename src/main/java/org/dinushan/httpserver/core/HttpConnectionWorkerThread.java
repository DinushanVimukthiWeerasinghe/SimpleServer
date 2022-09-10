package org.dinushan.httpserver.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

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
        //outputStream is used to write the response to the socket
        OutputStream outputStream = null;
        try {
            //inputStream is created from the socket
            inputStream = socket.getInputStream();
            //outputStream is created from the socket
            outputStream = socket.getOutputStream();
            //readRequest method is used to read the request from the socket
            String request = readRequest(inputStream);
            //method is used to get the method from the request
            String method = request.split(" ")[0];
            //path is used to get the path from the request
            String path = request.split(" ")[1];
            //version is used to get the version from the request
            String version = request.split(" ")[2];
            //Declare file to be null
            File file=null;
            //If the path is /, then index.html is used as the path
            if (method.equals("GET")) {
                if (path.equals("/")) {
                    path = "index";
                } else {
                    //If the path is not /, then the path is used as it is.
                    if (path.contains(".html")) {
                        path = path.substring(0, path.indexOf(".html"));
                    }
                }
                //file is created from the path
                file = new File(webRoot + path + ".html");
                //If the file does not exist, then 404.html is used as the file
                if(!file.exists()){
                    file= new File(webRoot + "404.html");
                }
            }
            //If the method is not GET, then 405.html is used as the file
            else{
                file= new File(webRoot + "405.html");
            }
            //String builder is used to create the response
            StringBuilder HtmlPage=new StringBuilder();
            //If the file exists, then the response is created
            assert file != null;
            //Read the file and create the response
            FileReader fileReader = new FileReader(file);
            try {
                //Buffered reader is used to read the file
                BufferedReader in =new BufferedReader(fileReader);
                String str;
                //While loop is used to read the file and create the response
                while ((str = in.readLine()) != null) {
                    HtmlPage.append(str);
                }
                //Close the buffered reader
                in.close();

            }
            // If any error occurs, the error is logged
            catch (IOException e) {
                e.printStackTrace();
                LOGGER.error("Problem with reading the file", e);
            }
            //Create the response and convert it to String
            String html = HtmlPage.toString();
            //Declare the CR and LF as constants. CR is used to indicate the end of the line and LF is used to indicate the end of the header
            final String CRLF = "\n\r"; // 13,10
            //Create the response header. Below is the format of the response header
            String response = "HTTP/1.1 200 OK" + CRLF + // Status Line : HTTP Version, Status Code, Response Message
                    "Content-Length: " + html.getBytes().length + CRLF +
                    CRLF +
                    html +
                    CRLF + CRLF;
            //Write the response to the socket
            outputStream.write(response.getBytes());
            }
        //If any Exception occurs, then the error is logged
        catch (IOException e) {
            LOGGER.error("Error while Communication", e);
            e.printStackTrace();
            } finally {
            //Close the socket and the streams after the response is sent
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ignored) {}
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException ignored) {}
            }
            if (socket != null) {
                try {
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
