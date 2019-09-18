package com.qaselenium.framework.testingobjectmodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.qaselenium.framework.utils.reporter.Reporter;
import com.qaselenium.framework.utils.testCase.AbstractTestCase;

/**
 * 
 * @author rpette27
 *
 */
public abstract class AbstractPage {
	/* initiate the TOM logger */
	protected static final Logger LOGGER = Logger.getLogger("TOM ");

	/* place holder for REPORTER */
	public Reporter REPORTER;

	/* place holder for drive, it is referenced by AbstractTestCase */
	public WebDriver webDriver;

	protected String HTTP = "http://";

	/*
	 * place holder for base url,it can be
	 * intpublish-couk.engine.intpublish-fr.engine,intpublish-de.engine, etc
	 */
	private static String baseURL;

	/* place holder for current domain, like couk, fr, de */
	public static String domain;

	/**
	 * Return base url, like int.ford.eu, int.ford.ca or int.ford.co.uk
	 * 
	 * @return baseURL
	 */
	public static String getBaseURL() {
		return baseURL;
	}

	public static void setBaseURL(String baseURL) {
		AbstractPage.baseURL = baseURL;
	}

	/**
	 * Set TOM domain, possible options are "Com", "Ca" or "co.uk" called by
	 * AbstractTest
	 * 
	 * @param1 String domain
	 */
	public static void setDomain(String domain) {
		AbstractPage.domain = domain;
	}

	/**
	 * Constructor of AbstractPage gets a webDriver instance from
	 * AbstractTestCase and set the domain
	 * 
	 * @param1 String domain
	 * @see com.qaselenium.framework.utils.testCase.AbstractTestCase
	 */
	public AbstractPage(String domain) {
		webDriver = AbstractTestCase.webDriver;
		REPORTER = AbstractTestCase.REPORTER;

		/* Initial page with page factory objects. */
		PageFactory.initElements(webDriver, this);

		AbstractPage.domain = domain;
	}

	/**
	 * Default constructor of AbstractPage gets a driver instance from
	 * AbstractTestCase
	 * 
	 * 
	 * @see com.qaselenium.framework.utils.testCase.AbstractTestCase
	 */
	public AbstractPage() {
		webDriver = AbstractTestCase.webDriver;
		REPORTER = AbstractTestCase.REPORTER;

		/* Initial page with page factory objects. */
		PageFactory.initElements(webDriver, this);
	}


	protected abstract String getPageURL();

	protected abstract String getPageTITLE();

	public void verifyPageTitle() {
		/* Grab the title of teh current page */
		String actualTitle = webDriver.getTitle();

		/*
		 * If page title set for page class then do check, no need to do check
		 * if value is empty.
		 */
		if (getPageTITLE() != "" || !getPageTITLE().isEmpty()) {
			
			if (!actualTitle.contains(getPageTITLE())) {
				
				REPORTER.Fail("Verify Page Title","Expected Page Title to contain :[" + getPageTITLE()+ "]", "Actual Page Title:[" + actualTitle+ "]", "URL: " + webDriver.getCurrentUrl(),true);
			} else {
				
				/* Debug to see which page titles were verified */
				REPORTER.Info("Verify Page Title - Expected Page Title to contain :["+ getPageTITLE()+ "], Actual Page Title:["+ actualTitle + "], URL: " + webDriver.getCurrentUrl());
			}
		}
	}
	
	public List<String> getTags(String str) {
	 String TagToWorkWith = str; // here simply change the tag name on which you want to work
	 List<WebElement> myTags =  webDriver.findElements(By.tagName(TagToWorkWith));
	 List<String> headings = new ArrayList<String>();
        // now extracting the value 
        // this for loop will print/extract all the values for tag 'H1'.
        for(int i=0;i<myTags.size();i++){
            // extracting tags text
            System.out.println(TagToWorkWith + " value is : " + myTags.get(i).getText());
            headings.add(myTags.get(i).getText());
            
        }
        return headings;
	}   
        
	
	/**
	 * 
	 * @throws Exception
	 */
	public void load() throws Exception {
			
		if (StringUtils.isNotEmpty(getPageURL())) {
			REPORTER.Info("Opening page " + HTTP + getPageURL());
			webDriver.get(HTTP + getPageURL());
			webDriver.manage().window().maximize(); 
			

			if (webDriver.getCurrentUrl().contains("https")) {
				HTTP = "https://";
			} else {
				HTTP = "http://";

			}

			isLoaded();

		}
	}

	public void isLoaded() {

		if (webDriver.getCurrentUrl().contains("https")) {
			HTTP = "https://";
		} else {
			HTTP = "http://";
		}

		String tmpUrl = HTTP + getPageURL();

		REPORTER.Info("Check if page  [" + tmpUrl + "] is loaded");

		if (!webDriver.getCurrentUrl().contains(tmpUrl)) {
			/* Report that URL does not match expected. */
			REPORTER.Fail("isLoaded()", "Expected current URL to contain: ["+ tmpUrl + "]", "Actual URL: [" + webDriver.getCurrentUrl()+ "]", "", true);
		}
		/* Also verify page title once loaded succesfully */
		//verifyPageTitle();
	}
	
	
	/**
	 * 
	 * @param element
	 * @throws NoSuchElementException
	 */
	public void submit(By element) throws NoSuchElementException {
		/* Check if element exist before clicking on it */
		if (isElementPresent(element)) {
			
			WebElement we = webDriver.findElement(element);

			/* check if the element is displayed */
			if (we.isDisplayed()) {

				/* If exists and is displayed then report Debug and click */
				webDriver.findElement(element).submit();
				REPORTER.Info("Clicked on element [" + element + "]"+ ", on Page [" + this.getClass().getSimpleName() + "]");
			} else {
				/* Report an error as the element exists but is not displayed. */
				REPORTER.Error("Attempted to click on invisible element ["+ element + "] on Page ["+ this.getClass().getSimpleName()+ "]. Element is on the page however isDisplayed is ["+ we.isDisplayed() + "].", true);
			}
		} else {
			/* Report an error as script could not click on an element */
			REPORTER.Error("Attempted to click on element ["+ element+ "] on Page ["+ this.getClass().getSimpleName()+ "], however element could not be found. Please check the script.",true);
		}
	}

	/**
	 * 
	 * @param element
	 * @throws NoSuchElementException
	 */
	public void click(By element) throws NoSuchElementException {
		/* Check if element exist before clicking on it */
		if (isElementPresent(element)) {
			
			WebElement we = webDriver.findElement(element);

			/* check if the element is displayed */
			if (we.isDisplayed()) {

				/* If exists and is displayed then report Debug and click */
				webDriver.findElement(element).click();
				REPORTER.Info("Clicked on element [" + element + "]"+ ", on Page [" + this.getClass().getSimpleName() + "]");
			} else {
				/* Report an error as the element exists but is not displayed. */
				REPORTER.Error("Attempted to click on invisible element ["+ element + "] on Page ["+ this.getClass().getSimpleName()+ "]. Element is on the page however isDisplayed is ["+ we.isDisplayed() + "].", true);
			}
		} else {
			/* Report an error as script could not click on an element */
			REPORTER.Error("Attempted to click on element ["+ element+ "] on Page ["+ this.getClass().getSimpleName()+ "], however element could not be found. Please check the script.",true);
		}
	}
	
	
    
	/**
     * 
     * @param element
     */
	public void waitClick(By element) {

		WebDriverWait wait = new WebDriverWait(webDriver, 10000);
		wait.until(ExpectedConditions.elementToBeClickable(element));

		if (isElementPresent(element)) {

			WebElement we = webDriver.findElement(element);

			/* check if the element is displayed */
			if (we.isDisplayed()) {
				/* If exists and is displayed then report Debug and click */
				webDriver.findElement(element).click();
				REPORTER.Info("Clicked on element [" + element + "]"+ ", on Page [" + this.getClass().getSimpleName() + "]");
			} else {
				/* Report an error as the element exists but is not displayed. */
				REPORTER.Error("Attempted to click on invisible element ["+ element + "] on Page ["+ this.getClass().getSimpleName()+ "]. Element is on the page however isDisplayed is ["+ we.isDisplayed() + "].", true);
			}
		} else {
			/* Report an error as script could not click on an element */
			REPORTER.Error(
					"Attempted to click on element ["+ element+ "] on Page ["+ this.getClass().getSimpleName()+ "], however element could not be found. Please check the script.",true);
		}
	}

	public void selectOption(By by, String optionLabel) {
		selectOption(by, optionLabel, false);
	}

	public void selectOption(By by, String optionLabel, boolean exactMatch) {
		if (isElementPresent(by)) {
			RemoteWebElement rwe = (RemoteWebElement) webDriver.findElement(by);
			
			//System.out.println("rwe printed"+rwe.getText());
			/* check if the element is displayed */
			if (rwe.isDisplayed()) {

				if (optionLabel != "" || optionLabel != null) {
					String MatchTypeXpath = "";

					if (exactMatch) {
						MatchTypeXpath = "//option[text()='" + optionLabel+ "')]";
					} else {
						/* Default is contains */
						MatchTypeXpath = "//option[contains(text(),'"+ optionLabel + "')]";
					}

					WebElement selectBox = webDriver.findElement(by);
					/*
					 * TODO: This check should be done using the selectBox as
					 * well.
					 */
					if (isElementPresent(By.xpath(MatchTypeXpath))) {

						selectBox.findElement(By.xpath(MatchTypeXpath)).click();
						
						/* Report Debug when attempting to click on */
						REPORTER.Info("Selected [" + optionLabel+ "] from dropdown with by [" + by+ "] on Page ["+ this.getClass().getSimpleName() + "]");

					} else {
						
						REPORTER.Error(
								"Attempted to select ["+ optionLabel+ "] from dropdown ["+ by+ "], however value could not be found. Please check script and datasheet",true);
					}
				}

			} else {
				/* Report an error as the element exists but is not displayed. */
				REPORTER.Error("Attempted to click on invisible dropdown element ["+ by+ "] on Page ["+ this.getClass().getSimpleName()+ "]. Dropdown element is on the page however isDisplayed is ["+ rwe.isDisplayed() + "].", true);
			}
		} else {
			/* Report an error as script could not click on an dropdown */
			REPORTER.Error("Attempted to interact with dropdown ["+ by+ "] on Page ["+ this.getClass().getSimpleName()+ "], however dropdown element could not be found. Please check the script.",true);
		}

	}
	
	 public void chooseFromdropDown(By by, String option){
		   List<WebElement> Options = new ArrayList<WebElement>();
		   WebElement optionChooser = webDriver.findElement(by);
		   optionChooser.click();
		   Select select = new Select(optionChooser);
		   Options = select.getOptions();
		   for (WebElement item:Options){
		       if(item.getText().equals(option)){
		           item.click();
		           item.sendKeys(Keys.ENTER);
		          
		       }
		   }
		  }
	
	
	/**
	 * 
	 * @param by
	 * @param optionLabels
	 */
	public void selectMultiOptions(By by, String optionLabels) {

		/* Check if the multi select can be found */
		if (this.isElementPresent(by)) {

			/* Create select Object */
			Select mulitpleSelection = new Select(webDriver.findElement(by));

			// Clear all selected entries.
			mulitpleSelection.deselectAll();

			/* grab list of values */
			List<WebElement> listOfValue = mulitpleSelection.getOptions();

			/* Split values into array */
			String[] valuesArray = optionLabels.split("\\|");

			/* Loop through the array of Values */
			for (String oneValue : valuesArray) {

				boolean ValueFound = false;
				/* Loop through each item in the list and verify */
				for (int counter = 0; counter < listOfValue.size(); counter++) {

					/* check if value matches the dropdown */
					if (listOfValue.get(counter).getText().toLowerCase()
							.contains(oneValue.toLowerCase())) {
						/*
						 * Once a value is found click it, set the boolean and
						 * report on it
						 */
						listOfValue.get(counter).click();
						ValueFound = true;
						REPORTER.Info("Selected ["+ listOfValue.get(counter).getText()+ "] from dropdown with by [" + by+ "] on Page ["+ this.getClass().getSimpleName() + "]");
					}

				}

				/* if value not found */
				if (!ValueFound) {
					/* Report on the fact that the value could not be found */
					REPORTER.Error("The value ["
							+ oneValue
							+ "] could not be found in the Multi Selection List ["
							+ by + "]");
				}
			}
		}
	}

	/**
	 * 
	 * @param CookieName
	 * @param CookieValue
	 */
	public void checkCookieSet(String CookieName, String CookieValue) {
		/* Set the cookie found value as false */
		boolean cookieFound = false;

		/* grab all the current cookies. */
		Set<Cookie> allCookies = webDriver.manage().getCookies();

		/* loop through all the cookies and check them */
		for (Cookie loadedCookie : allCookies) {
			/* Debug all the cookies found */
			REPORTER.Debug("Cookie Name: " + loadedCookie.getName()+ "; Cookie Value: " + loadedCookie.getValue());

			/*
			 * If the cookie name matches the cookie you are looking for check
			 * the value
			 */
			if (loadedCookie.getName().equals(CookieName)) {
				/* if the value does not match then report a failure */
				if (loadedCookie.getValue().equals(CookieValue)) {
					REPORTER.Pass("COOKIE CHECK [" + CookieName + "]",
							"COOKIE VALUE: " + CookieValue, "ACTUAL VALUE: "
									+ loadedCookie.getValue(), "");
				} else {
					/* else report a pass */
					REPORTER.Pass("COOKIE CHECK [" + CookieName + "]","COOKIE VALUE: " + CookieValue, "ACTUAL VALUE: "+ loadedCookie.getValue(), "");
				}

				/* cookie has been found so exit */
				cookieFound = true;
				break;
			}
		}

		/* if cookie was not found then report an error. */
		if (!cookieFound) {
			REPORTER.Fail("COOKIE CHECK [" + CookieName + "]","Expected Cookie " + CookieName + " to be present","Cookie not found", "");
		}

	}

	/**
	 * 
	 * @param element
	 * @param Value
	 */
	public void setTextField(By element, String Value) {
		/* Check if element exist before clicking on it */
		if (isElementPresent(element)) {
			/*
			 * Instantiate a rendered web Element so that we can figure out if
			 * the element is visible
			 */
			WebElement we = webDriver.findElement(element);

			/* check if the element is displayed */
			if (we.isDisplayed()) {

				/* If exists and is displayed then report Debug and click */
				we.clear();
				we.sendKeys(Value);
				REPORTER.Info("Populated webEdit Element [" + element+ "] with value [" + Value + "]" + ", on Page ["+ this.getClass().getSimpleName() + "]");
			} else {
				/* Report an error as the element exists but is not displayed. */
				REPORTER.Error("Attempted to select an invisible element ["+ element + "] on Page ["+ this.getClass().getSimpleName()+ "]. Element is on the page however isDisplayed is ["+ we.isDisplayed() + "].", true);
			}
		} else {
			/* Report an error as script could not click on an element */
			REPORTER.Error("Attempted to select an element ["+ element+ "] on Page ["+ this.getClass().getSimpleName()+ "], however element could not be found. Please check the script.",true);
		}
	}
	
	public void setTextFieldAndEnter(By element, String Value){
		
		setTextField(element, Value);
		webDriver.findElement(element).sendKeys(Keys.ENTER);
		
	}

	/**
	 * 
	 * @param element
	 * @return
	 */
	public String getTextForWebElement(By element) {
		/* set the value found string */
		String ValueFound = null;

		/* Check if element exist before clicking on it */
		if (isElementPresent(element)) {
			/*
			 * Instantiate a rendered web Element so that we can figure out if
			 * the element is visible
			 */
			WebElement we = webDriver.findElement(element);

			/* If exists and is displayed then report Debug and click */
			ValueFound = webDriver.findElement(element).getText();

		} else {
			/* Report an error as script could not click on an element */
			REPORTER.Error("Attempted to select an element ["+ element+ "] on Page ["+ this.getClass().getSimpleName()+ "], however element could not be found. Please check the script.",true);
		}

		return ValueFound;
	}
	
	/**
	 * capture ominute tag by variable
	 * @return
	 */
	public String captureOmnitureVariableValue(String omnitureVariable) {
		String sgVal =null;
		if (webDriver instanceof JavascriptExecutor) {
		     sgVal = (String)((JavascriptExecutor) webDriver)
				.executeScript("return " +omnitureVariable+";");
		}
		return sgVal;
		
	}
	
	
	/**
	 * Method to click the back button on the browser
	 * 
	 */
	public void clickBrowserBackButton() {
		REPORTER.Debug("Clicked the browser back button");
		webDriver.navigate().back();

	}
	
	public void navigateTo(String url){		
		webDriver.navigate().to(HTTP+getBaseURL()+url);
		
	}

	/**
	 * 
	 * @param by
	 * @return
	 */
	public boolean isElementPresent(By by) {
		try {
			webDriver.findElement(by);
			REPORTER.Debug("Found element using by: [" + by.toString() + "]");
			return true; // Success!
		} catch (NoSuchElementException ignored) {
			REPORTER.Debug("Did not find element using by: [" + by.toString() + "]");
			return false;
		}

	}
	
	
	public String getElementAttribute(By by, String attribute){
		WebElement element = webDriver.findElement(by);
		return element.getAttribute(attribute);	
	}	
	
	

}
