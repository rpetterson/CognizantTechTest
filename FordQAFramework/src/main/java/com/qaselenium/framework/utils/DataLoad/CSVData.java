package com.qaselenium.framework.utils.DataLoad;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.net.URL;

import org.apache.log4j.Logger;

import com.csvreader.CsvReader;

/**
 * It stores all Test Cases read from .csv file parameterNames --> names of each
 * parameter read from .csv (first row of .csv) num_parameters --> number of
 * columns of .csv testScriptData --> list of TestCaseData (test name +
 * parameters) numberTestCasesToRun --> numbers of tests cases read to be run
 */
public class CSVData {

	private List<String> parameterNames = new ArrayList<String>();

	private int num_parameters;

	private List<TestCaseData[]> testScriptData = new LinkedList<TestCaseData[]>();

	private int numberTestCasesToRun;

	/*
	 * delimiter with separator used in .csv file ex: (char) (44) --> comma ex:
	 * (char) (9) --> TAB
	 */
	private final static char delimiter = (char) (44);

	protected static final Logger LOGGER = Logger.getLogger("TEST");

	/**
	 * Constructor
	 * 
	 * Load Structure with all test cases from .csv file
	 * 
	 * @pathFile pathFile of .csv.
	 * @throws Exception
	 * @see TestCaseData class
	 */
	public CSVData(String fileName) throws Exception {

		CsvReader reader = null;
		String[] headers = null;

		/* relative path of resource file is built */
		final URL url = ClassLoader.getSystemResource(fileName);
		String pathFile = url.getPath();
		numberTestCasesToRun = 0;

		try {

			reader = new CsvReader(pathFile, delimiter);

			/* It reads headers from file (first row) */
			if (reader.readHeaders()) {
				num_parameters = reader.getHeaderCount();
				headers = reader.getHeaders();
			}

			/* Initialize the parameterNames list with headers read from file */
			for (int counterParameters = 0; counterParameters < num_parameters; counterParameters++) {
				parameterNames.add(headers[counterParameters]);
			}

			/* All records are read */
			while (reader.readRecord()) {
				/* Object to store the list of parameters of a test */
				TestCaseData testCaseData = new TestCaseData();

				/* List is filled with values of current record */
				for (int counterParameters = 0; counterParameters < num_parameters; counterParameters++) {
					testCaseData.getParameters().put(
							headers[counterParameters],
							reader.get(headers[counterParameters]));
				}

				// collection of TestCaseData is generated
				testScriptData.add(new TestCaseData[] { testCaseData });

				numberTestCasesToRun++;
			}

		} catch (Exception e) {
			String errorMessage = "CSV file with all test cases doesn't read - "
					+ e.getMessage();
			LOGGER.fatal(errorMessage);
			throw new Exception(errorMessage);
		} finally {
			if (reader != null) {
				reader.close();
			}
		}

	}

	/**
	 * Constructor
	 * 
	 * Load Structure with a single test case from .csv file
	 * 
	 * @pathFile pathFile of .csv.
	 * @testCaseId Id of test case to be loaded.
	 * @throws Exception
	 * @see TestCaseData class
	 */
	public CSVData(String fileName, Integer testCaseId) throws Exception {
		CsvReader reader = null;
		String[] headers = null;

		/* relative path of resource file is built */
		final URL url = ClassLoader.getSystemResource(fileName);
		String pathFile = url.getPath();

		numberTestCasesToRun = 0;

		try {

			reader = new CsvReader(pathFile, delimiter);

			/* It reads headers from file (first row) */
			if (reader.readHeaders()) {
				num_parameters = reader.getHeaderCount();
				headers = reader.getHeaders();
			}

			/* Initialize the parameterNames list with headers read from file */
			for (int counterParameters = 0; counterParameters < num_parameters; counterParameters++) {
				parameterNames.add(headers[counterParameters]);
			}

			/* All records are read until required testCase is found */
			while (reader.readRecord()) {
				/* Only required test case is selected */
				if ((reader.getCurrentRecord() + 1) == (testCaseId)) {
					/* Object to store the list of parameters of a test */
					TestCaseData testCaseData = new TestCaseData();

					/* List is filled with values of current record */
					for (int counterParameters = 0; counterParameters < num_parameters; counterParameters++) {
						testCaseData.getParameters().put(
								headers[counterParameters],
								reader.get(headers[counterParameters]));
					}

					// collection of TestCaseData is generated
					testScriptData.add(new TestCaseData[] { testCaseData });

					numberTestCasesToRun++;

					// As test has been found, execution exits from while
					break;
				}
			}

		} catch (Exception e) {
			String errorMessage = "CSV file with only a test cases doesn't read - "
					+ e.getMessage();
			LOGGER.fatal(errorMessage);
			throw new Exception(errorMessage);
		} finally {
			if (reader != null) {
				reader.close();
			}
		}

	}

	/**
	 * Constructor
	 * 
	 * Load Structure with a range of test cases from .csv file
	 * 
	 * @pathFile pathFile of .csv.
	 * @initialTestCase Id of first test of range to be loaded.
	 * @finalTestCase Id of last test of range to be loaded.
	 * @throws Exception
	 * @see TestCaseData class
	 */
	public CSVData(String fileName, Integer initialTestCase,
			Integer finalTestCase) throws Exception {
		CsvReader reader = null;
		String[] headers = null;

		/* relative path of resource file is built */
		final URL url = ClassLoader.getSystemResource(fileName);
		String pathFile = url.getPath();

		numberTestCasesToRun = 0;

		try {

			reader = new CsvReader(pathFile, delimiter);

			/* It reads headers from file (first row) */
			if (reader.readHeaders()) {
				num_parameters = reader.getHeaderCount();
				headers = reader.getHeaders();
			}

			/* Initialize the parameterNames list with headers read from file */
			for (int counterParameters = 0; counterParameters < num_parameters; counterParameters++) {
				parameterNames.add(headers[counterParameters]);
			}

			/* All records are read until range of test cases are selected */
			while (reader.readRecord()) {
				/* Only test cases inside range are selected */
				if (((reader.getCurrentRecord() + 1) >= initialTestCase)
						&& ((reader.getCurrentRecord() + 1) <= finalTestCase)) {
					/* Object to store the list of parameters of a test */
					TestCaseData testCaseData = new TestCaseData();

					/* List is filled with values of current recird */
					for (int counterParameters = 0; counterParameters < num_parameters; counterParameters++) {
						testCaseData.getParameters().put(
								headers[counterParameters],
								reader.get(headers[counterParameters]));
					}

					// collection of TestCaseData is generated
					testScriptData.add(new TestCaseData[] { testCaseData });

					numberTestCasesToRun++;
				} else if (reader.getCurrentRecord() > finalTestCase) {
					/* if all range was got, while loop is finished */
					break;
				}

			}

		} catch (Exception e) {
			String errorMessage = "CSV file with a range of test cases doesn't read - "
					+ e.getMessage();
			LOGGER.fatal(errorMessage);
			throw new Exception(errorMessage);
		} finally {
			if (reader != null) {
				reader.close();
			}
		}

	}

	/**
	 * methods to access to private attributes
	 */

	public List<String> getParameterNames() {
		return parameterNames;
	}

	public void setParameterNames(List<String> parameterNames) {
		this.parameterNames = parameterNames;
	}

	public int getNum_parameters() {
		return num_parameters;
	}

	public void setNum_parameters(int num_parameters) {
		this.num_parameters = num_parameters;
	}

	public List<TestCaseData[]> getTestScriptData() {
		return testScriptData;
	}

	public void setTestScriptData(List<TestCaseData[]> testScriptData) {
		this.testScriptData = testScriptData;
	}

	public int getNumberTestCasesToRun() {
		return numberTestCasesToRun;
	}

}
