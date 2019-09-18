package com.qaselenium.framework.utils;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class LoadEnvironmentProperties {

	
	public static Properties getProperty(String PROPERTY_FILE) 
  	{
        final Properties properties = new Properties();
        final URL url = ClassLoader.getSystemResource(PROPERTY_FILE);
        
        try {
        properties.load(url.openStream());
        
        }catch(Exception e) {}
        
        return properties;
    }
}
