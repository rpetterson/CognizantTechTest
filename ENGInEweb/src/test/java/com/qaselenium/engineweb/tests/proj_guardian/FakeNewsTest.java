package com.qaselenium.engineweb.tests.proj_guardian;


import java.util.Collection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.qaselenium.engineweb.testingobjectmodel.OtherPageObjects.proj_guardian.FakeNewsValidator;
import com.qaselenium.framework.utils.DataLoad.TestCaseData;
import com.qaselenium.framework.utils.reporter.Reporter;
import com.qaselenium.framework.utils.testCase.AbstractTestCase;
import com.qaselenium.framework.utils.testCase.AbstractTestCaseDataDriven;


/**
 * 
 * @author rpette27
 *
 */

/*
 * ******************************************************************
 * DESRCIPTION OF TEST CASE: ASSERTS THAT NEWS ARTICLE IN THE GUARDIAN IS OR IS NOT FAKE NEWS              *
 * ****************************************************************** 
 */


@RunWith(Parameterized.class)
public class FakeNewsTest extends AbstractTestCaseDataDriven {
	
	 public Reporter REPORTER = AbstractTestCase.REPORTER;

	 public FakeNewsTest(TestCaseData testCaseData) {
	        super(testCaseData);
       
    }

    @Parameterized.Parameters
    public static Collection<TestCaseData[]> getListTestCaseData()
            throws Exception {
        try {

            String className = new CurrentClassGetter().getClassName();
            className = className.substring(className.lastIndexOf(".") + 1);

            /* replace integration with testphase parameter. */
            String fileName = className +"_regression"+ ".csv";
            System.out.println("csv filename=" + fileName);

            /* return getTestCaseDataFromCSV(fileName,"2"); */
            return getTestCaseDataFromCSV(fileName);

        } catch (Exception e) {
            String errorMessage = "Collection of TestCaseData from .csv file could not be returned - "
                    + e.getMessage();
            LOGGER.fatal(errorMessage);
            throw new Exception(errorMessage);
        }
    }

    @Test
    public void isFakeNews() throws Exception {
    	

    	FakeNewsValidator fakeNews = new FakeNewsValidator();
    	
    	fakeNews.load();
    	// Navigates to the Guardians main news page and asserts URL is correct against test data
    	fakeNews.navigateToNews(testCaseData.getParameters().get("baseURL"));
    	
    	fakeNews.locateFirstNewsArticle();
    	fakeNews.navigateToGoogle();
    	fakeNews.assertGoogleUrlIsReturned();
    	fakeNews.searchNewsTitleInGoogle();
    	fakeNews.isFakeNews();
    	
       
    	
   }
 
}
