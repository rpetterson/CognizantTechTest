package com.qaselenium.engineweb.testingobjectmodel.OtherPageObjects;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.qaselenium.framework.utils.AbstractDatabaseDriver;

public class EngineDataBaseDriver extends AbstractDatabaseDriver {

	public void connectToSqlServer() throws SQLException {

		createSQLServerConnection("jdbc:sqlserver://FCDB1013:1433;databaseName=ENGOWN;","PRX_ENGOWN_QA2", "gdbsthd#yu14");
	}
	
    /**
     * 
     * @param ownerEmail
     * @return
     */
	public boolean hasOwnerPersistedToDatabase(String ownerEmail) {
		ResultSet recordSet = null;
		boolean ownerAddedToDatabase = false;

		try {
			/* Connect to the DB */
			connectToSqlServer();

			String sqlQuery = "select EMAIL from customer where email = '"+ ownerEmail + "'and country_code='GBR'";

			recordSet = runRecordSetQuery(sqlQuery);

			if (recordSet.next()) {
				if (recordSet.getString("email").contains(ownerEmail)) {
					ownerAddedToDatabase = true;
				}
			}
			closeDBConnection();
		}
		// If unable to connect for any reason report an error
		catch (SQLException e) {
			REPORTER.Error("Error while verifying database "+e.getMessage());
		}

		return ownerAddedToDatabase;

	}
	/**
	 * 
	 * @param ownerEmail
	 */
	public void deleteTestUserBeforeAddingSameUser(String ownerEmail) {

		if (hasOwnerPersistedToDatabase(ownerEmail)) {

			try {
				/* Connect to the DB */
				connectToSqlServer();
				String sqlQuery = "delete from customer where email = '"+ ownerEmail + "' and country_code='GBR'";
				runUpdateQuery(sqlQuery);
			}
			// If unable to connect for any reason report an error
			catch (SQLException e) {
				REPORTER.Error("Error while deleting "+e.getMessage());
			}
		}  if (hasOwnerPersistedToDatabase(ownerEmail)) {
			
			REPORTER.Error("Removed the user " + ownerEmail+ " from the database ");
		}
		else {
			REPORTER.Info("Removed the user " + ownerEmail+ " from the database ");
		}

	}
	/*
	 * 
	 * author repette27
	 * @param ownerEmail
	 * 
	 */
	public boolean hasOwnedVehicleBeenAddedToMyFord (String ownerEmail) {
		ResultSet recordSet = null;
		boolean ownedVehicleAddedToDatabase = false;

		try {
			/* Connect to the DB */
			connectToSqlServer();

			String sqlQuery = "SELECT COUNT(*) as count FROM VEHICLE WHERE CUSTOMER_ID_FK in (SELECT CUSTOMER_ID_K FROM CUSTOMER WHERE EMAIL = '" +ownerEmail+ "') and VEHICLE_TYPE='owned'"; 
			
			recordSet = runRecordSetQuery(sqlQuery);
			
			
			
			if (recordSet.next()) {
				System.out.println("+++++++++ The Value in the DB for vehicle count is " + recordSet.getInt("count"));
				if (recordSet.getInt("count")>0){
				
					ownedVehicleAddedToDatabase = true;
				}
			}
			closeDBConnection();
		}
		// If unable to connect for any reason report an error
		catch (SQLException e) {
			REPORTER.Error("Error while verifying database "+e.getMessage());
		}

		return ownedVehicleAddedToDatabase;

	}
	
	public boolean hasOwnedVehicleBeenDeletedFromMyFord (String ownerEmail) {
		ResultSet recordSet = null;
		boolean ownedVehicleDeletedFromDatabase = false;

		try {
			/* Connect to the DB */
			connectToSqlServer();

			String sqlQuery = "SELECT COUNT(*) as count FROM VEHICLE WHERE CUSTOMER_ID_FK in (SELECT CUSTOMER_ID_K FROM CUSTOMER WHERE EMAIL = '" +ownerEmail+ "') and VEHICLE_TYPE='owned'";

			recordSet = runRecordSetQuery(sqlQuery);

			if (recordSet.next()) {
				System.out.println("+++++++++ The Value in the DB for vehicle count after deleting is " + recordSet.getInt("count"));
				if (recordSet.getInt("count")==0){
				
					ownedVehicleDeletedFromDatabase = true;
				}
			}
			closeDBConnection();
		}
		// If unable to connect for any reason report an error
		catch (SQLException e) {
			REPORTER.Error("Error while verifying database "+e.getMessage());
		}

		return ownedVehicleDeletedFromDatabase;
	
	}

	public boolean hasConfiguredVehicleBeenAddedToMyFord(String ownerEmail) {
		ResultSet recordSet = null;
		boolean configuredVehicleAddedToDatabase = false;

		try {
			/* Connect to the DB */
			connectToSqlServer();

			String sqlQuery = "SELECT COUNT(*) as count FROM VEHICLE WHERE CUSTOMER_ID_FK in (SELECT CUSTOMER_ID_K FROM CUSTOMER WHERE EMAIL = '" +ownerEmail+ "') and VEHICLE_TYPE='configured'"; 
			
			recordSet = runRecordSetQuery(sqlQuery);
			
			
			
			if (recordSet.next()) {
				System.out.println("+++++++++ The Value in the DB for configured vehicle count is " + recordSet.getInt("count"));
				if (recordSet.getInt("count")>0){
				
					configuredVehicleAddedToDatabase = true;
				}
			}
			closeDBConnection();
		}
		// If unable to connect for any reason report an error
		catch (SQLException e) {
			REPORTER.Error("Error while verifying database "+e.getMessage());
		}

		return configuredVehicleAddedToDatabase;

	}
	
	public boolean hasConfiguredVehicleBeenDeletedFromMyFord (String ownerEmail) {
		ResultSet recordSet = null;
		boolean configuredVehicleDeletedFromDatabase = false;

		try {
			/* Connect to the DB */
			connectToSqlServer();

			String sqlQuery = "SELECT COUNT(*) as count FROM VEHICLE WHERE CUSTOMER_ID_FK in (SELECT CUSTOMER_ID_K FROM CUSTOMER WHERE EMAIL = '" +ownerEmail+ "') and VEHICLE_TYPE='owned'";

			recordSet = runRecordSetQuery(sqlQuery);

			if (recordSet.next()) {
				System.out.println("+++++++++ The Value in the DB for vehicle count after deleting is " + recordSet.getInt("count"));
				if (recordSet.getInt("count")==0){
				
					configuredVehicleDeletedFromDatabase = true;
				}
			}
			closeDBConnection();
		}
		// If unable to connect for any reason report an error
		catch (SQLException e) {
			REPORTER.Error("Error while verifying database "+e.getMessage());
		}

		return configuredVehicleDeletedFromDatabase;
	
	}
}
