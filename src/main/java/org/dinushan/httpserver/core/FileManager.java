package org.dinushan.httpserver.core;

import java.io.File;

//FileManager is used to get the file from the webroot and return the file according to the requested path
public class FileManager {
    //fileManager is used to create a singleton instance of the FileManager

    private static final FileManager fileManager = new FileManager();
    FileManager() {
    }
    public static FileManager getInstance() {
        return fileManager;
    }
    //getWebRootFile is used to get the file from the webroot and Get the Content-Type of the file
    public String getContentType(String fileName) {
        if (fileName.endsWith(".html") || fileName.endsWith(".htm")) {
            return "text/html";
        } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return "image/jpeg";
        }else if (fileName.endsWith(".txt")) {
            return "text/plain";
        }else if (fileName.endsWith(".xml")) {
            return "text/xml";
        } else if (fileName.endsWith(".js")) {
            return "text/javascript";
        } else if (fileName.endsWith(".css")) {
            return "text/css";
        }else {
            return "text/plain";
        }
    }

    //Check whether the file exists in the webroot is an Image
    public boolean IsImage(String fileName) {
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".gif") || fileName.endsWith(".png") || fileName.endsWith(".ico") || fileName.endsWith(".svg")) {
            return true;
        } else {
            return false;
        }
    }
    //Check whether the file exists in the webroot is an CSS
    public boolean IsCSS(String fileName) {
        if (fileName.endsWith(".css")) {
            return true;
        } else {
            return false;
        }
    }

    //Check whether the file exists in the webroot is an HTML
    public boolean IsHTML(String path) {
        return path.endsWith(".html") || path.endsWith(".htm");
    }

    //Check whether the file exists in the webroot is an JavaScript
    public boolean IsJS(String path) {
        return path.endsWith(".js");
    }

    //Check whether the file exists in the webroot is an Text
    public boolean IsFileExist(String path,String filename) {
        File file = new File(path + filename);
        return file.exists();
    }
}
