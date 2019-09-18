package com.qaselenium.engineweb.testingobjectmodel.OtherPageObjects;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.PageFactory;

import com.qaselenium.framework.testingobjectmodel.AbstractPage;
import com.qaselenium.framework.utils.LoadEnvironmentProperties;

/**
 * 
 * @author halazar
 * 
 */
public class CommonPage extends AbstractPage {

	// place holder for properties files so we only load it once
	Properties globalproperties = LoadEnvironmentProperties.getProperty("settings.properties");

	public String baseURL  = getBaseURL();

	protected static Properties ENGINE_WEB_PROPORTIES = null;

	protected JavascriptExecutor JSExecutor = (JavascriptExecutor) webDriver;

	/**
	 * CONSTRUCTOR: CommonPage
	 * 
	 * This constructor allows all the pages to be automatically instantiate
	 * with elements.
	 * 
	 */
	public CommonPage() {
		/* Initial page with page factory objects. */
		PageFactory.initElements(webDriver, this);

	}

	/**
	 * this is a method to refresh current page
	 */
	public void pageRefresh() {

		webDriver.manage().timeouts().implicitlyWait(8, TimeUnit.SECONDS);

		this.webDriver.navigate().refresh();

		webDriver.manage().timeouts().implicitlyWait(8, TimeUnit.SECONDS);
	}

	// to be overriden in sub pages.
	public String getPageObjectID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getPageTITLE() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getPageURL() {
		// TODO Auto-generated method stub
		return baseURL;
	}
	
	
	public String getPageSource(){
		String pageSource =null;
		pageSource = webDriver.getPageSource();
		return pageSource;
	}

}