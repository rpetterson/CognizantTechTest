package com.qaselenium.framework.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.qaselenium.framework.utils.reporter.Reporter;
import com.qaselenium.framework.utils.testCase.AbstractTestCase;


public class IOUtility {

	protected Reporter REPORTER = AbstractTestCase.REPORTER;
	
	/**
     * Converts an InputStream to String 
     * 
     * @param inputStream
     * @return String
     **/
	public String convertInputStreamToString(InputStream inputStream)
    {
		
        StringBuilder sb = new StringBuilder();
        try{
	        try {
	            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
	            String line;
	            while ((line = reader.readLine()) != null) {
	                sb.append(line);
	            }
	        } finally {
	        	inputStream.close();
	        }
        }catch (IOException e){
        	REPORTER.Error("Error while converting InputStream to String");
        }
        
        return sb.toString();
    }	
}
