package org.pokenet.server.network;

/*
 * Simple MySQL Java Class
 * Makes it similair to PHP
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.pokenet.server.GameServer;

/**
 * Handles MySql connections
 * @author Daniel Morante
 * @author XtremeJedi
 */
public class MySqlManager {
	private static MySqlManager mInstance;
    private Connection mysql_connection;
    private String mysql_connectionURL;
    
    public MySqlManager()
    {
    	String host = GameServer.getDatabaseHost();
    	String username = GameServer.getDatabaseUsername();
    	String password = GameServer.getDatabasePassword();
    	final String database = GameServer.getDatabaseName();
    	
    	if(!connect(host, username, password)) 
    	{
    		// When we cannot connect to the database.
    		System.out.println("Cannot connect to the database, please check your settings.");
			System.exit(-1);
    	} else {
    		if(!selectDatabase(database))
    		{
    			// When we cannot select the database.
    			System.out.println("Cannot select the database, please check your settings.");
    			System.exit(-1);
    		}
    	}
    }
    
    public static MySqlManager getInstance()
    {
    	if(mInstance == null) 
    	{
    		mInstance = new MySqlManager();
    	}
    	return mInstance;
    }
    
    /**
     * Connects to the server. Returns true on success.
     * @param host
     * @param username
     * @param password
     * @return
     */
    private boolean connect(String host, String username, String password) {
    	if(mysql_connection == null) { // We don't want to open twice, do we?
	        try {
	            //Open Connection
	            mysql_connectionURL = "jdbc:mysql://" + host + "?autoReconnect=true";
	            mysql_connection = DriverManager.getConnection(mysql_connectionURL, username, password);
	            if(!mysql_connection.isClosed())
	            	return true;
	            else
	            	return false;
	        } catch(Exception x) {
	        	x.printStackTrace();
	        	return false;
	        }
    	}
    	return true;
    }
    
    /**
     * Selects the current database. Returns true on success
     * @param database
     * @return
     */
    private boolean selectDatabase(String database) {
    	try {
        	Statement stm = mysql_connection.createStatement();
        	stm.executeQuery("USE " + database);
        	return true;
    	} catch (Exception e) {
    		e.printStackTrace();
    		return false;
    	}
    }
    
    /**
     * Closes the connection to the mysql server. Returns true on success.
     * @return
     */
    public boolean close() {
        try {
        	if(!mysql_connection.isClosed()) { // Maybe it's closed already?
        		
        	}
            return true;
        }
        catch (SQLException e)
        {
        	e.printStackTrace();
        	return false;
        }
    }
    
    /**
     * Returns a result set for a query
     * @param query
     * @return
     */
    public ResultSet query(String query){
    	System.out.println("Let's search for some data;	" + query);
        //Create Statement object
        
        /*
         * We want to keep things simple, so...
         *
         * Detect whether this is an INSERT, DELETE, or UPDATE statement      
         * And use the executeUpdate() function
         *
         * Or...
         * 
         * Detect whether this is a SELECT statement and use the executeQuery()
         * Function. 
         * 
        */  
        
    	ResultSet mysql_result = null;
        if (query.startsWith("SELECT")) {
            //Use the "executeQuery" function because we have to retrieve data
            //Return the data as a resultset
            try{
                //Execute Query
                Statement stmt = mysql_connection.createStatement();
                mysql_result = stmt.executeQuery(query);
            }
            catch(SQLException e)
            {
            	e.printStackTrace();
            }
        }
        else {
            //It's an UPDATE, INSERT, or DELETE statement
            //Use the"executeUpdaye" function and return a null result
            try{
                //Execute Query
                Statement stmt = mysql_connection.createStatement();
                stmt.executeUpdate(query);
            }
            catch(SQLException e)
            {
            	e.printStackTrace();
            }
        }
        return mysql_result;
    }    
    
    public static String parseSQL(String text)
	{
		if(text == null) text = "";
		text = text.replace("'", "''");
		text = text.replace("\\", "\\\\");
		return  text;
	}
}
