package com.qaselenium.framework.utils.testCase;

import java.util.Collection;

import org.junit.Before;

import com.qaselenium.framework.utils.DataLoad.CSVData;
import com.qaselenium.framework.utils.DataLoad.TestCaseData;

/**
 * Abstract class which loads different sites before each test.
 * It will be extended by TestScripts which run a testCase with 
 * several list of parameters read from csv file.
 * 
 * testCaseData --> List of pairs <parameter, value> needed to run a test case.
 * testCaseName --> name of test case represented by a row in .csv file. It is the name of each iteration.
 */
public abstract class AbstractTestCaseDataDriven extends AbstractTestCase {

	protected TestCaseData testCaseData; 
	
	protected String testCaseName;

	/**
	 * Constructor
	 *
	 * @testCaseData     Parameters and values for running a testCase.  
     * @see     TestCaseData
	 */
	public AbstractTestCaseDataDriven (TestCaseData testCaseData) {
		this.testCaseData=testCaseData;
	} 
	

	
	/**
	 * getTestCaseDataFromCSV 
	 * 
	 * Static method which will be called by all Data Driven Test Scripts.
	 * It creates a CSVData object which will load all data from .csv file.
	 * Depending on number of arguments, a set of test cases is read from .csv file.
	 * 1 String argument --> to load all test cases from .csv
	 *            --> arg[0]: Name of .csv file
	 * 2 String arguments --> to load only a test case from .csv
	 *             --> arg[0]: Name of .csv file
	 *             --> arg[1]: Id of test case required
	 * 3 String arguments --> to load a range of test cases from .csv
	 *             --> arg[0]: Name of .csv file
	 *             --> arg[1]: Id of first test case of range required
	 *             --> arg[2]: Id of first test case of range required
	 *             
	 * @return    Collection<TestCaseData[]> --> collection of list of parameters to be run by @test.
	 * @throws Exception 
	 * @see  Data Driven Test Scripts   
	 */
	public static Collection<TestCaseData[]> getTestCaseDataFromCSV(String... args) throws Exception 
	{
	   try
	   {
		   LOGGER.debug("getTestCaseDataFromCSV  - " + args);
		   
		   CSVData csvData;
		   
		   
	       /*
	        * Depending on number of arguments, a different constructor of CSVData is called
	        */
		   if (args.length==1)
		   {
      		   /*Reading all test cases from CSV file*/
			   csvData = new CSVData(args[0]);
		   }
		   else if (args.length==2)
		   {
			   /*Reading an specific test case from a CSV file*/
			   csvData = new CSVData(args[0], Integer.parseInt(args[1]));
		   }
		   else 
		   {
			   /*Reading a range of test cases from a CSV file*/
			   csvData = new CSVData(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]));
		   }
			
		   
		   /* Getting structure read from CSV*/	 
		   Collection<TestCaseData[]> collectionTest = csvData.getTestScriptData();
		   	   
		   return collectionTest;
		 
	   }
	   catch (Exception e)
       {
           String errorMessage = "Collection of TestCaseData could not be got - " + e.getMessage();
           LOGGER.fatal(errorMessage);
           throw new Exception(errorMessage);
       }

	}
	
	
 
	/**
	 * before    
	 *
	 * It overrides AbstractTestCase.before()
	 * It writes some logs related to Data Driven test cases and it set site read from .csv file
	 *    
	 * @throws Exception 
	 * @see   AbstractTestCase.before()  
	 */	
    @Before
    public void before() throws Exception
    { 	
    	testCaseName=null;
    	
    	/* Getting name of Test Case (1st row) */
    	testCaseName = testCaseData.getParameters().get("Test Name");
    
        /* Adding new iteration in reporter with test name and individual test case. */
    	REPORTER.addNewIteration(testName + " - " + testCaseName);
            	
    	if (testCaseName == null)
    	{       	
    		/* Invalid test case*/
    		testCaseName = "Invalid";
            REPORTER.Error(testName + " - " + " Skipping!! Test Case: " + testCaseName);
    	}
    	else
    	{
    		
    	    /* Getting Site where run test case */
            String domainValue = null;
            domainValue = testCaseData.getParameters().get("Domain");
    	
            /* If test case doesn't require a site, we use default site  */
            if (domainValue == null)
            {
            	domainValue = defaultSite;
            }
            
            /* Setting site   	 */
            setTestProperties(testCaseData.getParameters().get("baseURL"));
            
    		/* Update any setting from the csv sheet*/
    		updateDataDrivenTestProperties();
    		
            // once test settings have been set or updated run client.
            startBrowser();
            
    	}

    }   
  
   /**
    * This method is used to update the test properties from the CSV file before starting the webDriver.
    * 
    * 
    */
    private void updateDataDrivenTestProperties(){       
        
	    /* Getting browser where run test case */
        String browser = null;
		browser = testCaseData.getParameters().get("browser");
		
        /* If test case requires a new browser, we update it  */
        if (browser != null && !browser.equals(webDriverBrowser))
        {
  		    setBrowser(browser);
		    
		    /* Report on updating browser */
		    REPORTER.Info("Updated Test Properties from CSV: Browser [" + browser + "]");
        }
	
	    /* Getting baseURL where run test case */
        String baseURL = null;		
		baseURL = testCaseData.getParameters().get("baseURL");
	
        /* If test case requires a new baseURL, we update it  */
        if (baseURL != null)
        {
		    setBaseURL(baseURL);
		    siteUrl = "http://" + baseURL;
        }
        
	    /* Getting speed where run test case */
        String speed = null;		
		speed = testCaseData.getParameters().get("speed");
	
        /* If test case requires a new speed, we update it  */
        if (speed != null)
        {
        	setSpeed(speed);
        }      
    	
        /* Getting browser where run test case */
        String proxy = null;
        proxy = testCaseData.getParameters().get("proxy");
        
        /* as this is a static object we have to reset the proxy every time. */
        setProxyUrl(null);
        
        /* If test case requires a new browser, we update it  */
        if (proxy != null && !proxy.equalsIgnoreCase(""))
        	
        {
                setProxyUrl(proxy);
                                    
	            /* Report on updating browser */
	            REPORTER.Info("Updated Test Properties from CSV: Proxy [" + proxy + "]");
        }        
        
    }


}
