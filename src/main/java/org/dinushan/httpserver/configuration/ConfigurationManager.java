package org.dinushan.httpserver.configuration;

import org.dinushan.httpserver.util.Json;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ConfigurationManager {
    private static ConfigurationManager configurationManager  = null;
    private static Configuration configuration;

    private ConfigurationManager() {
        configuration = new Configuration();
    }
    public static ConfigurationManager getInstance() {
        if (configurationManager == null) {
            configurationManager = new ConfigurationManager();
        }
        return configurationManager;
    }

    public static ConfigurationManager getConfigurationManager() {
        if (configurationManager == null) {
            configurationManager = new ConfigurationManager();
        }
        return configurationManager;
    }

    public Configuration getConfiguration() {
        if (configuration == null) {
            throw new HttpConfigurationException("No Current Configuration");
        }
        return configuration;
    }

//    Use To LoadConfiguration From File
    public void loadConfiguration(String filePath) {
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(filePath);
            StringBuffer stringBuffer = new StringBuffer();
            int i;
            while (true) {
                try {
                    if ((i = fileReader.read()) == -1) break;
                } catch (IOException e) {
                    throw new HttpConfigurationException("Error Parsing Configuration Class",e);
                }
                stringBuffer.append((char) i);
            }
            JsonNode conf= Json.parse(stringBuffer.toString()) ;
            System.out.println(conf);
            try {
                configuration = Json.fromJSON(conf,Configuration.class);
            } catch (Exception e) {
                throw new HttpConfigurationException("Error Parsing Configuration File Internal",e);
            }
        } catch (FileNotFoundException e) {
            if(e.getMessage().contains("http.json")){
                JsonNode conf= Json.parse("{\"port\":2728,\"webRoot\":\"htdocs/\"}");
                try {
                    configuration = Json.fromJSON(conf,Configuration.class);
                } catch (Exception e1) {
                    throw new HttpConfigurationException("Error Parsing Configuration File Internal",e1);
                }
            }
        }


    }
}
