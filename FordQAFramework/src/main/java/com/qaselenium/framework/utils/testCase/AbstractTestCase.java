package com.qaselenium.framework.utils.testCase;

import com.qaselenium.framework.testingobjectmodel.AbstractPage;
import com.qaselenium.framework.utils.LoadEnvironmentProperties;
import com.qaselenium.framework.utils.reporter.Reporter;
import org.apache.log4j.Logger;
import org.junit.*;
import org.junit.rules.MethodRule;
import org.junit.rules.TestWatchman;
import org.junit.runners.model.FrameworkMethod;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.DriverCommand;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * The main scope of this class is to define basic functionalities and share them across all descendant test cases. By default
 * domain site is couk, it can be changed by call setSite method and passing desired domain, like: fr, de,
 * 
 * 
 */
public class AbstractTestCase extends Reporter {
	
	public static Reporter REPORTER = new Reporter();
	
    protected static final Logger LOGGER = Logger.getLogger("TEST");

    /* place holder for remote webDriver client */
    public static RemoteWebDriver webDriver;


    static Properties globalproperty = LoadEnvironmentProperties.getProperty("settings.properties");

    protected static final String defaultSite = globalproperty.getProperty("client.web.uri");

    /* store the running test name. It is used also by AbstractTestCaseDataDriven */
    protected String testName;

    protected static String siteDomain;

    /* place holder for soft assertions errors */
    protected StringBuffer verificationErrors = new StringBuffer();

    /* place holder for properties files so we only load it once */
    protected static Properties globalproperties = null;

    protected static Properties testProperties = null;

    /* global properties */
    protected static String webDriverServerHostname;
    protected static String webDriverServerPort;
    public static boolean reportDebug;
    
    /* test properties */
    protected static String webDriverBrowserTimeOut;
    protected static String webDriverBrowser;
    protected static String siteUrl;
    private static String   testPhase;
    private static String frameworkEnv;
    private static Proxy webDriverProxy=null;
    
    /**
     * This has been added to overwrite the screenshot method for the webDriver as 
     * it was not working.
     */
    public class ScreenShotRemoteWebDriver extends RemoteWebDriver implements TakesScreenshot {
    	/* this class extends the remoteWebDriver and overrides the methods getScreenShotAs */
    	public ScreenShotRemoteWebDriver(URL url, DesiredCapabilities dc) {
			super(url, dc); 
    	}

    	public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
    	if ((Boolean)
	    	getCapabilities().getCapability(CapabilityType.TAKES_SCREENSHOT)) {
	    	    return	target.convertFromBase64Png(execute(DriverCommand.SCREENSHOT).getValue().toString());
	    	}
    	return null;
    	} 
    }
    
    
    /**
     * TestWatchman is a base class for Rules that take note of the testing action, without modifying it <br/>
     * From here we log: <br/>
     * Test starting <br/>
     * Test failed <br/>
     * Test pass
     */
    @Rule
    public MethodRule watchman = new TestWatchman()
    {

        /**
         * Add and entry in the TEST log when test starts
         */
        public void starting(FrameworkMethod method)
        {
            testName = method.getName();
        }

        /**
         * Add and entry in the TEST log when a test fails and capture a screenshot
         */
        public void failed(Throwable e, FrameworkMethod method)
        {
                     
            try
            {
                stopBrowser();
            } 
            catch (Exception e2)
            {
            	/* Report any errors whilst stopping the browser */
            	REPORTER.Error(e2.toString());
            }
            
            /* Only reported to Console to close it off, REPORTER is automatically closed off. */
        	REPORTER.Info("Check for errors[" + testName + "] FAILED!!! " + e.getMessage());
        }

        /**
         * Add and entry in the TEST log when test pass
         */
        public void succeeded(FrameworkMethod method)
        {

            try
            {
                stopBrowser();                
            } 
            catch (Exception e)
            {
            	REPORTER.Error("Error during stopping browser " + e.toString());
            }
            
            /* Only reported to Console to close it off, REPORTER is automatically closed off. */
            REPORTER.Info("*** No errors reported for test [" + testName + "] test PASS *** \n");
        }

    };

    /**
     * Starts local webDriver server if required.
     */
    @BeforeClass
    public static void beforeClass() throws Exception
    {
        setGlobalProperties();
    }

    /**
     * Stops local selenium server in case is running.
     */
    @AfterClass
    public static void afterClass() throws Exception
    {
        /* stop the reporter and update metrics */
        REPORTER.stopReporter();
    }

    /**
     * This method is executed before every junit test case.By default eu site is used, call setSite() to change domain
     */
    @Before
    public void before() throws Exception
    {
        /* Called from here so that we can call in it DD as well with full script and test name */
    	REPORTER.addNewIteration(testName);
        
    	/* Set test properties using defaultSite value */
        setTestProperties(defaultSite);

        // once test settings have been set or updated run webDrive client.
        startBrowser();
    }

    /**
     * This method is executed after every junit test case.
     */
    @After
    public void after() throws Exception
    {
    	/* Check if any error occured and report them */
    	REPORTER.checkForVerificationErrors();

        //close the test iteration after test and update the metrics
        REPORTER.closeIteration();
    }

    /**
     * CurrentClassGetter static class which implements how to get Class Name of an static class
     * 
     */
    public static class CurrentClassGetter extends SecurityManager
    {
        public String getClassName()
        {
            return getClassContext()[1].getName();
        }
    }

    /**
     * Create webDriver object and open the browser. This method will also delete all cookies and set webDriver execution speed.
     * 
     * @return void
     * @throws Exception
     * 
     */
    protected void startBrowser() throws Exception
    {
    	
        /* if webDriver client was not closed off properly before kill it */
        if (webDriver != null)
        {
            stopBrowser();
        }

        /* start browser */
        try
        {
        	
        	boolean browserCreated = false;
        	
            URL remoteServerUrl = new URL("http://" + webDriverServerHostname + ":" + webDriverServerPort + "/wd/hub");  
            
            if(webDriverBrowser.contains("iexplore"))
            {
                DesiredCapabilities dc = new DesiredCapabilities();
                dc = DesiredCapabilities.internetExplorer();
                dc.setCapability(CapabilityType.TAKES_SCREENSHOT, true);  
            	if(webDriverProxy !=null){
            	String username = System.getProperty("user.name");
            	File file = new File("C:/Users/"+username+"/IEDriverServer.exe");            	
            	System.setProperty("webdriver.ie.driver", file.getAbsolutePath());
            	dc.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);           	
            	dc.setBrowserName("internet explorer");
            	
            	}
				webDriver = new ScreenShotRemoteWebDriver(remoteServerUrl, dc);
				browserCreated = true;
            }
                
			else if (webDriverBrowser.contains("chrome")) {
				DesiredCapabilities dc = new DesiredCapabilities();
				dc = DesiredCapabilities.chrome();
				dc.setCapability(CapabilityType.TAKES_SCREENSHOT, true);
				//dc.setCapability("chrome.switches", Arrays.asList("--incognito"));
					ChromeOptions options = new ChromeOptions();
					options.addArguments("--test-type");
					dc.setCapability(ChromeOptions.CAPABILITY, options);
				if (webDriverProxy != null) {
					String username = System.getProperty("user.name");
					File file = new File("/Users/" + username+ "/chromedriver.exe");
					System.setProperty("webdriver.chrome.driver",file.getAbsolutePath());
					dc.setBrowserName("chrome");
				}
			
				webDriver = new ScreenShotRemoteWebDriver(remoteServerUrl, dc);
				browserCreated = true;
			}
            
			else if (webDriverBrowser.contains("firefox")) {
				DesiredCapabilities dc = new DesiredCapabilities();
				dc.setCapability(CapabilityType.TAKES_SCREENSHOT, true);

				if (webDriverProxy != null) {
					FirefoxProfile fp = new FirefoxProfile();
					// fp.setProxyPreferences(webDriverProxy);
					dc.setCapability(FirefoxDriver.PROFILE, fp);
				}

				/* Instantiate firefox webDriver object */
				dc.setBrowserName("firefox");
				webDriver = new ScreenShotRemoteWebDriver(remoteServerUrl, dc);
				browserCreated = true;

			}

            else if(webDriverBrowser.contains("ios"))
            {

                /* Add proxy if required */
                if(webDriverProxy !=null){
                    webDriver = new RemoteWebDriver(new URL("http://localhost:3001/wd/hub"), DesiredCapabilities.ipad());
                }
                browserCreated  = true;
                webDriver.manage().timeouts().implicitlyWait(Long.valueOf(webDriverBrowserTimeOut), TimeUnit.SECONDS);

            }
            
            else if(webDriverBrowser.contains("android"))
            {

                /* Add proxy if required */
                if(webDriverProxy !=null){
                    webDriver = new RemoteWebDriver(new URL("http://localhost:8080/wd/hub"), DesiredCapabilities.android());
                }
                browserCreated  = true;
                webDriver.manage().timeouts().implicitlyWait(Long.valueOf(webDriverBrowserTimeOut), TimeUnit.SECONDS);


            }
            else if (webDriverBrowser.contains("htmlunit")) {

                String PROXY = "proxyvipecc.nb.ford.com:83";
                DesiredCapabilities capabilities = new DesiredCapabilities();
                capabilities = DesiredCapabilities.firefox();
               
                Proxy pr = new Proxy();
                pr.setHttpProxy(PROXY);
                // pr.setSslProxy(PROXY);
                if (webDriverProxy != null) {
                       capabilities.setCapability(CapabilityType.PROXY, pr);
                       capabilities.setJavascriptEnabled(true);
                       String username = System.getProperty("user.name");
                       File file = new File("/Users/" + username
                                     + "/selenium-htmlunit-driver.exe");
                       System.setProperty("webdriver.htmlunit.driver",
                                     file.getAbsolutePath());
                       capabilities.setBrowserName("htmlunit");
                }

                // WebDriver htmlunit = new HtmlUnitDriver(capabilities);

                webDriver = new ScreenShotRemoteWebDriver(remoteServerUrl,
                              capabilities);
                browserCreated = true;

          }

            /* This has been applied for test cases that do not require a browser*/
            else if(webDriverBrowser.equalsIgnoreCase("none")){
            	browserCreated = false;            	
            }
            else
            {
            	REPORTER.Error("The browser parameter [" + webDriverBrowser + "] could not not be recognised. Therefore the test has stopped.");
            	browserCreated = false;
            }
            
            /* The following steps can only be done if a browser was initialised.*/
            if (browserCreated && !webDriverBrowser.contains("android") && !webDriverBrowser.contains("ios")){
	            /* Report on instantiation of webDriver including details */
	        	REPORTER.Info("webDriver instantiated using: Browser [" + webDriverBrowser + "], Hostname [" + webDriverServerHostname + "], Port [" + webDriverServerPort + "], BaseURL [" + siteUrl + "]");
	        	            
	            /* clear cookies, maximize browser and set speed */
	            webDriver.manage().deleteAllCookies();
	            REPORTER.Debug("Cleared cookies.");
	            
	            try
	            {
	                webDriver.manage().timeouts().implicitlyWait(Long.valueOf(webDriverBrowserTimeOut), TimeUnit.SECONDS);
	                REPORTER.Debug("BrowserTimeOut set to [" + webDriverBrowserTimeOut + "].");
	            }
	            catch (Exception e)
	            {
	            	/* reporter an error as we could set webDriver time and exit test */
	                REPORTER.Error("Could not set BrowserTimeOut [" + webDriverBrowserTimeOut + "], Error: [" + e.toString() + "].");
	            }
	            
	            String WINDOW_MAXIMIZE = "if (window.screen) { window.moveTo(0, 0); window.resizeTo(window.screen.availWidth, window.screen.availHeight);};";
	            
	            try
	            {
	                ((JavascriptExecutor)webDriver).executeScript(WINDOW_MAXIMIZE);
	                REPORTER.Debug("Browser Window maximized.");
	            }
	            catch (Exception e)
	            {
	            	/* reporter an error as we could not maximize screen and exit test */
	            	REPORTER.Error("Error during executing javascipt to Maximize browser [" + e.toString() + "]");
	            }
            }
            
        } catch (Exception e)
        {
            /* unable to start client properly */
            String errorMessage = "webDriver browser not started - " + e.getMessage();
            REPORTER.Error(errorMessage);
            throw new Exception(errorMessage);
        }

    }

    /**
     * 
     * Close browser and set webDriver to null
     * 
     * @throws Exception
     * 
     */
    protected static void stopBrowser() throws Exception
    {
    	
        /* if the client is already null then no need to close it */
        if (webDriver != null)
        {
            /* close the client */
            try
            {
           	
                webDriver.quit();
                webDriver = null;
                
            	/* Report stopping browser */
            	REPORTER.Debug("Set webDriver to null.");
            } 
            catch (Exception e)
            {
                String errorMessage = "stopping browser failed -" + e.getMessage();
                REPORTER.Error(errorMessage);
                throw new Exception(errorMessage);
            }

        }
    }

   

    /** Sets the global properties */
    protected static void setGlobalProperties()
    {
        /* load framework settings from file */
        globalproperties = LoadEnvironmentProperties.getProperty("framework.properties");

        /* set variables using global properties file */
        webDriverServerHostname = globalproperties.getProperty("webdriver.hostname");
        webDriverServerPort = globalproperties.getProperty("webdriver.port");
        webDriverBrowserTimeOut = globalproperties.getProperty("webdriver.timeout");
        testPhase = globalproperties.getProperty("test.phase");
      	reportDebug = globalproperties.getProperty("reporter.debug").equalsIgnoreCase("true");
      	
      	/* Output settings to console, as REPORTER object has no iteration yet. */
      	LOGGER.info("Global Properties Set: BrowserTimeOut [" + webDriverBrowserTimeOut + "], testPhase [" + testPhase + "], reportDebug [" + reportDebug + "]\n");
    }

    /**
     * Sets the initial test properties
     * 
     * @throws Exception
     */
    public void setTestProperties(String baseURL) throws Exception
    {
        
        try
        {
            /* load framework.properties */
            testProperties = LoadEnvironmentProperties.getProperty("framework.properties");
            
            /* load right baseURL based on site domain */
            if (baseURL == null || baseURL == ""){
            baseURL = testProperties.getProperty("client.web.uri");
            }
            // set TOM siteDomain
            AbstractPage.setDomain(siteDomain);

            // set TOM baseURL
            AbstractPage.setBaseURL(baseURL);

            /* set browser and siteURL using the test properties */
            webDriverBrowser = testProperties.getProperty("webdriver.browser");

            /* set siteUrl */
            siteUrl = "http://" + baseURL;
            /* ford environment, like wwwedu and intpublish */
            frameworkEnv = globalproperties.getProperty("framework.env");
            
            /* Report on test properties */
          //  REPORTER.Info("Initial Test Properties Set: Environment [" + frameworkEnv + "], Browser [" + webDriverBrowser + "], URL [" + siteUrl + "]");
            REPORTER.Info("Initial Test Properties Set: Environment [" + frameworkEnv + "], URL [" + siteUrl + "]");

            
        } catch (final Exception e)
        {
            String errorMessage = "setSite was not completed " + e.toString();
            REPORTER.Error(errorMessage);
            throw new Exception(errorMessage);
        }
    }

    /**
     * Return current domain
     * 
     * @return String siteDomain
     * 
     */
    public static String getSite()
    {
        return siteDomain;
    }

    /** Overwrites the siteDomain
     * @throws Exception */
    private void setSite(String site) throws Exception
    {
        siteDomain = site;
        
    }
    
    /**
     * This will return the Bodog environment, like MDEV, INT or TEST
     * 
     * @return frameworkEnv
     * @throws NullPointerException 
     */
    public static String getFrameworkEnv() throws NullPointerException
    {
        globalproperties = LoadEnvironmentProperties.getProperty("framework.properties");
        frameworkEnv = globalproperties.getProperty("framework.env");
        
        if (frameworkEnv ==null)
        {
            throw new NullPointerException("frameworkEnv value is null, make sure to set frameworkEnv in the POM and/or Profile you are running");
        }
        
        return frameworkEnv;

    }
    
    
    /**
     * This will return testPashe  declared in the profile, like integration, smoke or regression
     * 
     * @return testPhase
     * @throws Exception 
     */
    public static String getTestPhase() throws Exception
    {
        globalproperties = LoadEnvironmentProperties.getProperty("framework.properties");
        testPhase = globalproperties.getProperty("test.phase");
        
        if (testPhase==null)
        {
            throw new Exception("testPhase value is null, make sure to set testPhase in the mvn Profile you are running");
        }
        
        return testPhase;
    }

    /** Overwrites the speed */
    public void setSpeed(String speed)
    {
    	REPORTER.Debug("setSpeed: " + speed);
    	//FIXME webDriverBrowserTimeOut = speed;
    }

    /** Overwrites the browser, has to be set before start of client */
    public void setBrowser(String browserName)
    {
        webDriverBrowser = browserName;
    }

    /** Overwrites the browser, has to be set before start of client */
    public void setBaseURL(String baseURL)
    {
        siteUrl = baseURL;
    }

    /** Overwrites the browser, has to be set before start of client */
    public void setProxyUrl(String proxyUrl)
    {
    	webDriverProxy = new Proxy();
        webDriverProxy.setAutodetect(false);
    	webDriverProxy.setHttpProxy(proxyUrl);
    }
    
    /**
	 * Method to set the Geo Location to the postcode SS15 6EE
	 * 
	 */	
/*	public void setGeoLocation(){
		chromeDriver.executeScript("window.navigator.geolocation.getCurrentPosition = 
			    function(success){
			         var position = {"coords" : {
			                                       "latitude": "51.581", 
			                                       "longitude": "0.405"
			                                     }
			                         }; 
			         success(position);}");
		
	}*/

}
