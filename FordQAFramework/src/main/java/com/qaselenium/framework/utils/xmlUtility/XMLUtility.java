package com.qaselenium.framework.utils.xmlUtility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.qaselenium.framework.utils.IOUtility;
import com.qaselenium.framework.utils.reporter.Reporter;
import com.qaselenium.framework.utils.testCase.AbstractTestCase;

/**
 * This is the main class for the XML Utility's where the creator and the main functionality is defined.
 * Developed for Web Services testing, but can be used for other XML manipulations.
 * 
 * @author kpatel
 * 
 */
public class XMLUtility {

	private String defaultOutPutDirectory = "target"+ File.separator +"qa-logs"+ File.separator;
	
	protected Reporter REPORTER = AbstractTestCase.REPORTER;
	
    protected DocumentBuilder documentBuilder;
    protected Document document = null;
	protected DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
	
	private IOUtility ioUtility = new IOUtility();
	
    /**
     * copyXMLTemlpate method takes an instance of the XML file and places it in the qa-logs folder.
     * This allows us to manipulate a copy and not the original.
     * 
     * @param styleSheetFileName
     **/
	
	/* TODO: Modify method to copy all resources into the target folder, should be handled by the framework
	 * Currently called in this Utility and AbstractWsDriver */
	public void copyResourceFile(String fileName){
		 
		  try
		  {
			  /* create output file where styleSheet will be saved to */  
			  File styleSheetFile = new File(defaultOutPutDirectory + fileName);
			  
			  /* load resource stream into the input stream */
			  InputStream inputStream = ClassLoader.getSystemResourceAsStream(fileName);
			  
			  /* create output stream */
			  OutputStream out = new FileOutputStream(styleSheetFile);
			  
			  /* save the file */
			  byte buf[]=new byte[1024];
			  int len;
			  while((len=inputStream.read(buf))>0)
				  out.write(buf,0,len);
				  out.close();
				  inputStream.close();
			  }
		  
		  catch (IOException e){
			  REPORTER.Debug("error: " + e.getMessage());
		  }		  	
	}
	
    /**
     * returnDOMAsString takes in a DOM object Document and converts it to a String 
     * 
     * @param document
     * @return xmlAsString
     **/	
	public String returnDOMAsString(Document document)
    {
      String xmlAsString = "";
      
      	try {     
      		/* initialise TransformFactory */
      		TransformerFactory transformfactory = TransformerFactory.newInstance();
      		Transformer transform = transformfactory.newTransformer();
      		transform.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
      		transform.setOutputProperty(OutputKeys.INDENT, "yes");
      		/* initialise StringWriter */
      		StringWriter stringwriter = new StringWriter();
      		StreamResult result = new StreamResult(stringwriter);
      		DOMSource source = new DOMSource(document);
      		/* convert the DOM object into a String */
      		transform.transform(source, result);
      		xmlAsString = stringwriter.toString();
	      
  		} catch (TransformerConfigurationException e) {
  			REPORTER.Error("error: " + e.getMessage());
  		} catch (TransformerException e) {
  			REPORTER.Error("error: " + e.getMessage());
  		}	
  			REPORTER.Debug("Test XML generated was :\n" + xmlAsString);
  			
  			return xmlAsString;
    }
	
    /**
     * returnStringAsDOM takes in a DOM object Document and converts it to a String 
     * 
     * @param responseXML
     * @return document
     **/
	public Document returnStringAsDOM(String responseXML)
    {

		try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
			/* convert the String object and pass into the DOM variable */
			document = documentBuilder.parse(new InputSource(new StringReader(responseXML)));
		} catch (ParserConfigurationException e) {
			REPORTER.Error("error: " + e.getMessage());
		} catch (SAXException e) {
			REPORTER.Error("error: " + e.getMessage());
		} catch (IOException e) {
			REPORTER.Error("error: " + e.getMessage());
		}
		return document;
    }
	
    /**
     * returnXMLFileAsDOM takes in the path of a XML file copies the file to the default location 
     * and returns it as an object Document. 
     * 
     * @param xmlFilePath
     * @return document
     **/
	public Document returnXMLFileAsDOM(String xmlFilePath)
    {
		copyResourceFile(xmlFilePath);
        
        /* create file */
        File XMLfile = new File(defaultOutPutDirectory + xmlFilePath);

        /* load XML file */
		try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
			document = documentBuilder.parse(XMLfile);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			REPORTER.Error("error: " + e.getMessage());
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			REPORTER.Error("error: " + e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			REPORTER.Error("error: " + e.getMessage());
		} 
		return document;
    }
	
	/**
     * returnXMLFileAsDOM takes in the name of a Resource XML file 
     * and returns its content as an object Document. 
     * 
     * @param fileName
     * @return document
     **/
	public Document returnXMLResourceAsDOM(String fileName)
    {
		
        /* load XML file */
		try {
			/* load resource stream into the input stream */
			InputStream inputStream = ClassLoader.getSystemResourceAsStream(fileName);

			documentBuilder = documentBuilderFactory.newDocumentBuilder();
			document = documentBuilder.parse(inputStream);
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			REPORTER.Error("error: " + e.getMessage());
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			REPORTER.Error("error: " + e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			REPORTER.Error("error: " + e.getMessage());
		} catch (Exception e) {
		// TODO Auto-generated catch block
			REPORTER.Error("error: " + e.getMessage());
		} 
		return document;
    }

	/**
     * returnXMLFileAsString takes in the name of a Resource XML file 
     * and returns its content as a String. 
     * 
     * @param fileName
     * @return String
     **/
	public String returnXMLResourceAsString(String fileName){
		
		/* load resource stream into the input stream */
		InputStream inputStream = ClassLoader.getSystemResourceAsStream(fileName);
		String StringXML = ioUtility.convertInputStreamToString(inputStream);
		REPORTER.Debug("XML String generated from Resource :\n" + StringXML);
		return StringXML;
		
    }
	
}
