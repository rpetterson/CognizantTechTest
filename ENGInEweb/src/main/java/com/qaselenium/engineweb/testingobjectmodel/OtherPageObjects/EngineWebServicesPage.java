package com.qaselenium.engineweb.testingobjectmodel.OtherPageObjects;

import com.qaselenium.engineweb.testingobjectmodel.OtherPageObjects.CommonPage;

/**
 * 
 * @author halazar
 *
 */
public class EngineWebServicesPage extends CommonPage {


    protected String pageURL = baseURL;

    protected final String PageID = "page-login";

    protected String pageTITLE = "";
    

	public void navigateToSyncHomePage() {
		navigateTo("/cs/ContentServer?pagename=ENGInE/SYNC2/*******************");

		String response = webDriver.getPageSource();

		if (response.contains("")) {

		} else {

		}

	}

	public void navigateToSyncAppsCategory() {
		navigateTo("/cs/ContentServer?pagename=ENGInE/SYNC2/*******************");
		String response = webDriver.getPageSource();
		System.out.println("response" + response);
		if (response.contains("")) {

		} else {

		}
	}


	public void navigateToeUsedVehicleDeatils() {
		// TODO Auto-generated method stub
		
	}

	public void navigateToeUsedVehiclefilter() {
		// TODO Auto-generated method stub
		
	}
   
  
    @Override
    protected String getPageURL() {
        return pageURL;
    }

    @Override
    protected String getPageTITLE() {
        return "Explore the Ford Range";
    }


}
