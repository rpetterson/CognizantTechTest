package com.qaselenium.framework.utils.DataLoad;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * It stores Test Case name and all its parameters.
 * parameters --> list of pairs (name of parameter, value of parameter) 
 * 
 */
public class TestCaseData implements Serializable
{

    /* If the object is serializable, we need a version to manage updates */
	private static final long serialVersionUID = 1L;

    /* list of pairs (name of parameter, value of parameter)  */
    private Map<String, String> parameters = new HashMap<String, String>();



    /**
     * getParameters()
     * method to get the list of parameters
     * 
     * @return Map<String, String>
     */
    public Map<String, String> getParameters()
    {
        return parameters;
    }

    
    /**
     * setParameters()
     * method to set the attribute parameters with a list of parameters
     * 
     */
    public void setParameters(Map<String, String> parameters)
    {
        this.parameters = parameters;
    }



}
