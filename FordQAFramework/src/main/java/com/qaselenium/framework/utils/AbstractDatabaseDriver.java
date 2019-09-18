package com.qaselenium.framework.utils;

import java.sql.*;
import com.qaselenium.framework.utils.reporter.Reporter;
import com.qaselenium.framework.utils.testCase.AbstractTestCase;


public abstract class AbstractDatabaseDriver
{

    // This parameter will hold the connection object.
    protected static Connection databaseConnection;

    // This parameter will store which environment we are running against
    protected static String testEnv = null;
     
    /* place holder for drive, it is referenced by AbstractTestCase*/
    public Reporter REPORTER = AbstractTestCase.REPORTER;

    protected static String connectionString = "";
    protected static String connectionUserName = "";
    protected static String connectionPassWord = "";
    
    protected static final String SQLSERVER_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    


    protected void createSQLServerConnection(String connectionURL, String userName, String passWord) throws SQLException
    {
    	/* Set local variables */
    	connectionString = connectionURL;
    	connectionUserName = userName;
    	connectionPassWord = passWord;    	
    	
        /* Attempt to connect to the database */
        try
        {
            /* Load the MySQL Driver with DriverManager */
            Class.forName(SQLSERVER_DRIVER);
        }
        
        /* catch any errors if you are unable to find class for DB2 driver */
        catch (ClassNotFoundException e)
        {
            String errorMessage = "Could not load the SQLServer Driver with DriverManager " + e.toString();
            REPORTER.Error(errorMessage);
            throw new RuntimeException(errorMessage);
        }
        
        /* connect to the database using the connectionURL you just retrieved */
        databaseConnection = DriverManager.getConnection(connectionString, connectionUserName, connectionPassWord);



        /* log that connection has been opened and set to ready only */
        REPORTER.Info("Opened Read-Only sql Connection: connectionURL [" + connectionURL + "] with User [" + userName+ "]");
    }    
    
    public void closeDBConnection() throws SQLException
    {
    	try {   	
		        /* Check if the database is connected */
		        if (!databaseConnection.isClosed())
		        {
		            /* close database connection and report closure */
		            databaseConnection.close();
		            REPORTER.Info("Closed connection to Database with connection String [" + connectionString + "]");
		        } else
		        {
		            /* report that database was already closed and user tried to close */
		            REPORTER.Debug("Connection to Database with connection String [" + connectionString + "], could not be closed as it was already closed.");
		        }
        
		} catch (SQLException e) {
			REPORTER.Error(e.getMessage());
		}
    }

    /**
     * This method can be used to run a query on the database which will retrieve a record set. This can then be used to run
     * certain checks against the data in the recordset.
     * 
     * @exception SQLException If unable to run the query the reports an error
     * @throws SQLException
     * @see
     */
    protected ResultSet runRecordSetQuery(String sqlQuery) throws SQLException
    {
        /* Result set object that will hold the result of the sqlQuery */
        ResultSet recordSet = null;

        /* Check if you are connected to the database */
        if (!databaseConnection.isClosed())
        {
            recordSet = databaseConnection.prepareStatement(sqlQuery).executeQuery();
            
            /* report that query has been executed */
            REPORTER.Info("Query [" + sqlQuery.toString() + "] executed");
        } else
        {
            /* Log an error message because an attempt was made to run the query without a connection. */
           	REPORTER.Error("Unable to execute query as database connection with connection URL [" + connectionString + "] was not open");
        }

        /* Return the result */
        return recordSet;
    }
    
    /**
     * This method can be used to run an "update" query on the database which will update the tables (if the user used to connect
     * has the user rights to make the relevant update).
     * 
     * @exception SQLException If unable to run the query the reports an error
     * @throws SQLException
     * @see
     */
    protected void runUpdateQuery(String sqlQuery) throws SQLException
    {

        /* Check if you are connected to the database */
        if (!databaseConnection.isClosed())
        {
        	/* execute update query and commit the changes */
        	databaseConnection.prepareCall(sqlQuery).executeUpdate();
            databaseConnection.commit();
            
            /* report that query has been executed */
            REPORTER.Info("Query [" + sqlQuery.toString() + "] executed");
        }else
        {
            /* Log an error message because an attempt was made to run the sproc without a connection. */
           REPORTER.Error("Unable to execute update query as database connection with connection URL [" + connectionString + "] was not open");
        }
    }
    
    /**
     * Method to check if connection is up
     * @throws SQLException 
     */
    public boolean isConnected(){
    	
    	boolean dbBooleanStatus = false;
    	
        try {
        	
        	dbBooleanStatus =  !databaseConnection.isClosed();
		
        } catch (SQLException e) {
			// TODO Auto-generated catch block
			REPORTER.Error(e.getMessage());
		}

		return dbBooleanStatus;
    }	

    
    /**
     * Method to check if connection is readOnly.
     * @throws SQLException 
     */
    public boolean isReadOnly(){
 
    	boolean readOnly = false;
    	
        try {
        	
        	readOnly = databaseConnection.isReadOnly();
        
        } catch (SQLException e) {
			// TODO Auto-generated catch block
			REPORTER.Error(e.getMessage());
		}

		return readOnly;
    }
    
}

