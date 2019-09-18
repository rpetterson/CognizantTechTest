package com.qaselenium.framework.utils.reporter;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import com.qaselenium.framework.utils.testCase.AbstractTestCase;
import com.qaselenium.framework.utils.reporter.XMLEngine;


public class Reporter {


	protected static XMLEngine XMLENGINE = new XMLEngine("Ford_TestAutomation_Logs");

	protected static final Logger LOGGER = Logger.getLogger("REPORTER");

	/* place holder for soft assertions errors */
	protected StringBuffer verificationErrors = new StringBuffer();


	public void addNewIteration(String TestIdentifier) {
		XMLENGINE.addNewIteration(TestIdentifier);

		/* This is to keep existing logging going in console. */
		LOGGER.info("*** " + TestIdentifier + " test being RUN ***");
	}

	
	public void closeIteration() {
		XMLENGINE.closeIteration();
	}

	/**
	 * This method is used to stop the reporter XML Engine and will update all
	 * the metrics.
	 */
	public void stopReporter() {
		XMLENGINE.stopEngine();
	}

	public void Pass(String StepIdentifier, String ExpectedResult,
			String ActualResult, String TestInformation) {
		/* create message to report in specific format. */
		String message = "PASS - StepID:" + "[" + StepIdentifier
				+ "]| Expected: [" + ExpectedResult + "]|Actual: ["
				+ ActualResult + "]| Info: " + "[" + TestInformation + "]";

		/* log pass to XML reporting engine */
		XMLENGINE.report("PASS", StepIdentifier, ExpectedResult, ActualResult,
				TestInformation);

		/*
		 * Log the pass as info, as there is no other way to report a pass for
		 * now.
		 */
		LOGGER.info(message);

	}

	public void Fail(String StepIdentifier, String ExpectedResult,
			String ActualResult, String TestInformation) {
		this.Fail(StepIdentifier, ExpectedResult, ActualResult,
				TestInformation, false);
	}

	
	public void Fail(String StepIdentifier, String ExpectedResult,
			String ActualResult, String TestInformation, boolean TakeScreenShot) {
		/* create message to report in specific format. */
		String message = "FAIL - StepID:" + "[" + StepIdentifier
				+ "]| Expected: [" + ExpectedResult + "]|Actual: ["
				+ ActualResult + "]| Info: " + "[" + TestInformation + "]";

		/* log Fail to XML reporting engine */
		XMLENGINE.report("FAIL", StepIdentifier, ExpectedResult, ActualResult,
				TestInformation);

		/* report the failure as it happens */
		LOGGER.info(message);

		/*
		 * Take a screen shot if boolean is set to True as the failure is
		 * critical enough to stop the test.
		 */
		if (TakeScreenShot) {
			takeScreenShot();
		}

		/* catch failure and report later on. */
		try {
			Assert.assertTrue(message, false);
		} catch (Error e) {
			/* catch error in the verificationsErrors object. */
			verificationErrors.append(e.getMessage());
		}
	}

	
	public void FailAndExitTest(String StepIdentifier, String ExpectedResult,
			String ActualResult, String TestInformation) {
		this.FailAndExitTest(StepIdentifier, ExpectedResult, ActualResult,
				TestInformation, false);
	}


	public void FailAndExitTest(String StepIdentifier, String ExpectedResult,
			String ActualResult, String TestInformation, boolean TakeScreenShot) {
		/* create message to report in specific format. */
		String message = "FAIL - StepID:" + "[" + StepIdentifier
				+ "]| Expected: [" + ExpectedResult + "]|Actual: ["
				+ ActualResult + "]| Info: " + "[" + TestInformation + "]";

		/* log FAIL to XML reporting engine */
		XMLENGINE.report("FAIL", StepIdentifier, ExpectedResult, ActualResult,
				"Exiting Test: " + TestInformation);

		/*
		 * Take a screen shot first if boolean is set to True as the failure is
		 * critical enough to stop the test.
		 */
		if (TakeScreenShot) {
			takeScreenShot();
		}

		/* log assert failure as this will stop the test. */
		Assert.fail(message);
	}


	public void Debug(String DebugInformation) {

		/* Only report debug if debugReport is set to true. */
		if (AbstractTestCase.reportDebug) {
			/* log Debug to XML reporting engine */
			XMLENGINE.report("DEBUG", " ", " ", " ", DebugInformation);

			/* Report the Debug information */
			LOGGER.debug("DEBUG - Info: " + "[" + DebugInformation + "]");
		}
	}

	public void Info(String TestInformation) {
		/* log Debug to XML reporting engine */
		XMLENGINE.report("INFO", " ", " ", " ", TestInformation);

		/* Report the test information */
		LOGGER.info("INFO - Info: " + "[" + TestInformation + "]");
	}

	
	public void Error(String ErrorInformation) {
		this.Error(ErrorInformation, false);
	}


	public void Error(String ErrorInformation, boolean TakeScreenShot) {
		/* log Debug to XML reporting engine */
		XMLENGINE.report("ERROR", " ", " ", " ", ErrorInformation);

		/*
		 * Take a screen shot first if boolean is set to True as the failure is
		 * critical enough to stop the test.
		 */
		if (TakeScreenShot) {
			takeScreenShot();
		}

		/* Report the test information */
		LOGGER.error("ERROR - Error: " + "[" + ErrorInformation + "]");
	}

	/**
	 * Clears out the list of verification errors
	 */
	private void clearVerificationErrors() {
		/* clear the string buffer that holds the errors */
		this.Debug("Clearing Verification Errors");
		verificationErrors = new StringBuffer();
	}

	/**
	 * Asserts that there were no verification errors during the current test,
	 * failing immediately if any are found
	 * 
	 * @throws Exception
	 */
	public void checkForVerificationErrors() throws Exception {
		/* get all errors captured and put them into a string */
		String verificationErrorString = verificationErrors.toString();

		/* Clear the verificationErrors string buffer */
		clearVerificationErrors();

		/* If the string is equal to empty then report and fail the entire test */
		if (!"".equals(verificationErrorString)) {
			LOGGER.error("Failed verifications: " + verificationErrorString);
			Assert.fail(verificationErrorString);
		}
	}

	/**
	 * This method is used to create a time stamp for reporting.
	 * 
	 * @return String a time stamp as a string that can be used for reporting
	 */
	private String generateTimeStamp() {
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
		Date date = new Date();
		return dateFormat.format(date).toString();
	}


	public void takeScreenShot() {

		/* if the directory does not exist, create it */
		File screenShotDir = new File("target" + File.separator + "qa-logs"
				+ File.separator + "screenshot" + File.separator);
		if (!screenShotDir.exists()) {
			this.Debug("Creating Screenshots directory ["
					+ screenShotDir.toString() + "]");
			screenShotDir.mkdir();
		}
		/*
		 * get the screen shot and save it in the folder using current time and
		 * date
		 */
		String imagePath = screenShotDir + "/screenshot_" + generateTimeStamp()
				+ ".png";
		File scrFile = ((TakesScreenshot) AbstractTestCase.webDriver)
				.getScreenshotAs(OutputType.FILE);

		try {
			FileUtils.copyFile(scrFile, new File(imagePath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/* generate a message where the screen shot was saved. */
		this.Info("screenshot saved at " + imagePath);
	}

}
