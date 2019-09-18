package com.qaselenium.framework.utils.reporter;

// DOM imports
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.xpath.XPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * The XML Engine will be used to report test results for each executed test
 * iteration in a structured manor, and will output
 * 
 */
public class XMLEngine {

	/* object that holds the root of the XML file */
	private Element rootElement = null;

	/* object that holds the document (entire XML) */
	private Document document = null;

	/* Object that holds the current Iteration Node */
	private Element currentTestNode = null;

	/* Object that holds the current Step Node */
	private Element currentStep = null;
 
	/* Value and object required to store the generated XML File */
	private String filePath = null;

	/* set default log path which can be overwritten with constructor */
	private String defaultOutPutDirectory = "target" + File.separator
			+ "qa-logs" + File.separator;

	/*
	 * This is the name used for the default stylesheet which will be copied to
	 * QA logs directory
	 */
	private String defaultStyleSheetName = "defaultStyleSheet.xsl";
    private String defaultLogoName = "logo.png";

	/**
	 * Constructor which automatically starts the XML engine and creates the
	 * file.
	 * 
	 */  
	public XMLEngine(String TestRunIdentifier) {
		startEngine(TestRunIdentifier);
	}

	/**
	 * Constructor which automatically starts the XML engine and creates the
	 * file and sets the output directory (mainly for web services).
	 * 
	 * @param TestRunIdentifier
	 * @param AbsolutePath
	 */
	public XMLEngine(String TestRunIdentifier, String AbsolutePath) {
		startEngine(TestRunIdentifier);
		defaultOutPutDirectory = AbsolutePath;
	}

	/**
	 * This is a private method used to copy the stylesheets from the framework
	 * resource into the qa-logs directory and can be used for multiple
	 * stylesheets going forward.
	 * 
	 * @param styleSheetFileName
	 */
	private void copyStylesheet(String styleSheetFileName) {

		try {
			/* Create output file where styleSheet will be saved to */
			File styleSheetFile = new File(defaultOutPutDirectory + File.separator + styleSheetFileName);

			/* Load resource stream into the input stream */
			InputStream inputStream = ClassLoader.getSystemResourceAsStream(styleSheetFileName);

			/* Create output stream */
			OutputStream out = new FileOutputStream(styleSheetFile);

			/* save the file */
			byte buf[] = new byte[1024];
			int len;
			while ((len = inputStream.read(buf)) > 0)
				out.write(buf, 0, len);
			out.close();
			inputStream.close();
		}

		catch (IOException e) {
			e.printStackTrace();
		}

	}

    private void copyLogo(String logoName) {

        try {
			/* Create output file where styleSheet will be saved to */
            File styleSheetFile = new File(defaultOutPutDirectory + File.separator + logoName);

			/* Load resource stream into the input stream */
            InputStream inputStream = ClassLoader.getSystemResourceAsStream(logoName);

			/* Create output stream */
            OutputStream out = new FileOutputStream(styleSheetFile);

			/* save the file */
            byte buf[] = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0)
                out.write(buf, 0, len);
            out.close();
            inputStream.close();
        }

        catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
	 * Method to retrieve the target directory as it has been added to the
	 * property file.
	 * 
	 * @param fileName
	 */
	private void generateFilePath(String fileName) {

		/* if the directory does not exist, create it */
		File qaLogsDirectory = new File(defaultOutPutDirectory);
		if (!qaLogsDirectory.exists()) {
			/* create the directory if it does not exist */
			qaLogsDirectory.mkdir();
		}

		/* set the filePath value */
		filePath = qaLogsDirectory + File.separator + fileName + ".XML";
	}

	
	private void startEngine(String TestRunIdentifier) {

		try {
			/* create document builder */
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder;
			documentBuilder = documentBuilderFactory.newDocumentBuilder();

			/*
			 * Set the FilePath value and check if the report file already
			 * exists
			 */
			generateFilePath(TestRunIdentifier);

			/* copy the default stylesheet to the qa-log directory */
			copyStylesheet(defaultStyleSheetName);
            copyLogo(defaultLogoName);

			File XMLfile = new File(filePath);
			boolean exists = XMLfile.exists();

			if (!exists) {
				/*
				 * create rootElement Log with relevant attributes and add it to
				 * the document
				 */
				document = documentBuilder.newDocument();
				rootElement = document.createElement("Log");

				// Add Processing Instructions for StyleSheet
				Node Log = document.getFirstChild();
				Node pi = document.createProcessingInstruction(
						"xml-stylesheet", "type=\"text/xsl\" href=\""
								+ defaultStyleSheetName + "\"");
				document.insertBefore(pi, Log);

				// Add Attributes to Log Element
				rootElement.setAttribute("TestRunID", TestRunIdentifier);
				rootElement.setAttribute("Start", returnDate());
				rootElement.setAttribute("Finish", "");
				rootElement.setAttribute("Status", "PASS");
				rootElement.setAttribute("DebugStatus", "");
				rootElement.setAttribute("TotalNumberExecuted", "0");
				rootElement.setAttribute("TotalPass", "0");
				rootElement.setAttribute("TotalFail", "0");
				rootElement.setAttribute("TotalBlocks", "0");
				document.appendChild(rootElement);
				saveXMLFile();
			} else {
				// load the XML file
				try {
					// reload the XML file
					document = documentBuilder.parse(XMLfile);
					rootElement = (Element) document
							.getElementsByTagName("Log").item(0);

				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public void addNewIteration(String TestIdentifier) {

		currentTestNode = document.createElement("TestIteration");
		currentTestNode.setAttribute("Start", returnDate());
		currentTestNode.setAttribute("Finish", "");
		currentTestNode.setAttribute("TestIdentifier", TestIdentifier);
		currentTestNode.setAttribute("Status", "PASS");
		rootElement.appendChild(currentTestNode);
	}

	/**
	 * This method is used to close the Iteration and update the metrics. Should
	 * be called once the test has finished.
	 */
	public void closeIteration() {
		/*
		 * Update the currentTestNode with Finish Time and update it in the
		 * rootElement
		 */
		currentTestNode.setAttribute("Finish", returnDate());
		rootElement.appendChild(currentTestNode);

		/* update metrics as soon as new Iteration is added */
		updateTestMetrics();
		saveXMLFile();
	}


	public void report(String Status, String StepName, String Expected,
			String Actual, String Details) {

		/* create a step node and set it in currentStep object. */
		currentTestNode.appendChild(document.createElement("Step"));
		currentStep = (Element) currentTestNode.getLastChild();

		/*
		 * Add child nodes to step and populate them using the addDataToElement
		 * method
		 */
		Node nodeStatus = currentStep.appendChild(document
				.createElement("Status"));
		nodeStatus.appendChild(document.createCDATASection(Status));

		/*
		 * Add status attribute to step so that we can filter on it using the
		 * Stylesheet.
		 */
		currentStep.setAttribute("Status", Status);

		/* Add Step Name Attribute to current node */
		Node nodeStepName = currentStep.appendChild(document
				.createElement("StepName"));
		nodeStepName.appendChild(document.createCDATASection(StepName));

		/* Add Expected Attribute to current node */
		Node nodeExpected = currentStep.appendChild(document
				.createElement("Expected"));
		nodeExpected.appendChild(document.createCDATASection(Expected));

		Node nodeActual = currentStep.appendChild(document
				.createElement("Actual"));
		nodeActual.appendChild(document.createCDATASection(Actual));

		Node nodeDetails = currentStep.appendChild(document
				.createElement("Details"));
		nodeDetails.appendChild(document.createCDATASection(Details));

		Node nodeTime = currentStep.appendChild(document.createElement("Time"));
		nodeTime.appendChild(document.createCDATASection(returnDate()));

		/*
		 * Only apply new status to TestIteration if it's a fail or if it's a
		 * block and existing status is not fail
		 */
		if (!Status.equalsIgnoreCase("PASS")
				&& !Status.equalsIgnoreCase("WARNING")
				&& !Status.equalsIgnoreCase("INFO")
				&& !Status.equalsIgnoreCase("DEBUG")) {
			if (Status.equalsIgnoreCase("FAIL")
					|| Status.equalsIgnoreCase("ERROR")
					&& !currentTestNode.getAttribute("Status")
							.equalsIgnoreCase("FAIL")) {
				currentTestNode.setAttribute("Status", Status.toUpperCase());
			}
		}

	}

	/**
	 * This method is used to update the metrics at the top of the XML and is
	 * called once a test has finished and will update the metrics after each
	 * test.
	 * 
	 * @exception TransformerException
	 */
	private void updateTestMetrics() {

		try {
			/*
			 * Count all the TestIteration Elements and set the
			 * TotalNumberExecuted Value
			 */
			int TotalNumberExecuted = rootElement.getChildNodes().getLength();
			rootElement.setAttribute("TotalNumberExecuted",
					String.valueOf(TotalNumberExecuted));

			/*
			 * Using Xpath count all the failure nodes and update the TotalFail
			 * value
			 */
			NodeList FailureNodes = XPathAPI.selectNodeList(rootElement,
					"//TestIteration[@Status='FAIL']");
			rootElement.setAttribute("TotalFail",
					String.valueOf(FailureNodes.getLength()));

			/*
			 * Using Xpath count all the pass nodes and update the TotalPass
			 * value
			 */
			NodeList PassNodes = XPathAPI.selectNodeList(rootElement,
					"//TestIteration[@Status='PASS']");
			rootElement.setAttribute("TotalPass",
					String.valueOf(PassNodes.getLength()));

			/*
			 * Using Xpath count all the blocked nodes and update the
			 * TotalBlocks value
			 */
			NodeList BlockNodes = XPathAPI.selectNodeList(rootElement,
					"//TestIteration[@Status='ERROR']");
			rootElement.setAttribute("TotalBlocks",
					String.valueOf(BlockNodes.getLength()));

			/* check if any failures and update high level */
			if (FailureNodes.getLength() > 0) {
				rootElement.setAttribute("Status", "FAIL");
			}
			/*
			 * If no failures but there are blocks update high level with
			 * BLOCKED
			 */
			else if (BlockNodes.getLength() > 0) {
				rootElement.setAttribute("Status", "ERROR");
			}

		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * This method is used to save the XML file store in the document object
	 * using the private fileOutputStream object which holds the filepath.
	 * 
	 * @exception TransformerConfigurationException
	 * @exception TransformerException
	 * 
	 */
	private void saveXMLFile() {

		try {
			// overwrite XML file with new document source.

			FileOutputStream fileOutputStream = null;
			fileOutputStream = new FileOutputStream(new File(filePath));

			/*
			 * Using the fileOutputStream store the document object XML source
			 * to an XML file
			 */
			StreamResult streamResult = new StreamResult(fileOutputStream);
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			transformer.transform(source, streamResult);

		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Stop the XML engine and set the Finish Time and update any metrics (might
	 * be needed if last test crashed) and save the XML file
	 */
	public void stopEngine() {
		rootElement.setAttribute("Finish", returnDate());
		updateTestMetrics();
		saveXMLFile();
	}

	/**
	 * This method is used to return a time stamp which is then added to the XML
	 * file to give an indication about when the the test started and finished.
	 * 
	 * @return String A formated string of current time and date.
	 */
	private static String returnDate() {
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date).toString();
	}

}