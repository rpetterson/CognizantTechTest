package com.qaselenium.engineweb.testingobjectmodel.OtherPageObjects.proj_guardian;







import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.frameToBeAvailableAndSwitchToIt;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

import java.util.ArrayList;
import java.util.List;


import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.qaselenium.engineweb.testingobjectmodel.OtherPageObjects.CommonPage;
import com.qaselenium.engineweb.testingobjectmodel.OtherPageObjects.EngineDataBaseDriver;




/**
 * 
 * @author rpette27
 *
 */


public class FakeNewsValidator extends CommonPage{

    protected String pageURL = baseURL;

    protected final String PageID = "page-login";//change this

    protected String pageTITLE = "";
    
    private String winHandleBefore = "";
    
    
    /*
    * Define all private vars here for locators
    */
    
   
    private By firstNewsArticleHeading = By.className("js-headline-text");
    private By googleSearchField = By.name("q");
    
    private By resultStats= By.id("resultStats");
    
    
    
  
   
    
    WebDriverWait wait = new WebDriverWait(webDriver, 200);
    private EngineDataBaseDriver dbDriver = new EngineDataBaseDriver();
    
    
    public void verifyTitle(String expectedTitle) {
        String actualTitle = webDriver.getTitle();
        if (actualTitle.equals(expectedTitle)) {
            REPORTER.Pass("Login page loaded", "Expected pageTitle is :"+expectedTitle, "Actual pageTitle is :" +actualTitle,"");
        } else {
            REPORTER.Fail("Login page loaded", expectedTitle, actualTitle, "Page title is: " + actualTitle + " and must be '" + expectedTitle + "'");
        }
    }
    
 
    public void navigateToNews(String string) {
    	
    	navigateTo("/tone/news");
        	
        	webDriver.manage().window().maximize();
    		
    	}
    
    
    
    public void verifyUrl(String expectedURL) throws Exception {

		String actualUrl = webDriver.getCurrentUrl();
		if (actualUrl.equals(expectedURL)) {
            REPORTER.Pass("Login page loaded", "Expected pageURL is :"+expectedURL, "Actual pageTitle is :" +actualUrl,"");
        } else {
            REPORTER.Fail("Login page not loaded", expectedURL, actualUrl, "Page title is: " + actualUrl + " and must be '" + expectedURL + "'");
        }
	}
    
    

	


	

	//Locate and read in first news article heading
	public String locateFirstNewsArticle() throws Exception {
		
		
		return getTextForWebElement(firstNewsArticleHeading);
		
		
	}
	
	public void navigateToGoogle() {
		webDriver.navigate().to("http://www.google.com");
		
	}

	//asserts that correct URL for Google is returned
	public void assertGoogleUrlIsReturned() {
		
		isLoaded();
		
	}
	String guardianHeading = firstNewsArticleHeading.toString();

	public void searchNewsTitleInGoogle() {
		//String guardianHeading = firstNewsArticleHeading.toString();
		setTextFieldAndEnter(googleSearchField,guardianHeading);
		
	}


	public void isFakeNews() {
		// wait until the google page shows the result
		WebDriverWait wait = new WebDriverWait(webDriver, 10000);
		wait.until(ExpectedConditions.elementToBeClickable(resultStats));
		
		//store all H3 tags from google results in a List and return these
		List<String> googleHeadings = getTags("h3");
		
		for(int i=0; i < googleHeadings.size(); i++) {
			
			//this needs more work Ideally it needs to check that at least 4 words match in each string
			if(guardianHeading.contains(googleHeadings.get(i))){
				 REPORTER.Pass("This is not Fake News!", "","","");
	        } else {
	            REPORTER.Fail("This is Fake News!", "","","");
	        }
				
			}
		}
			
			
	
		 
		
	}
	
	
		
	



	

    
}


	
