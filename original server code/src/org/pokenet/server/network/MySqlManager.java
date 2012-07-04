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
    private final String mysql_connectionURL;
    
    public MySqlManager()
    {
    	final String host = GameServer.getDatabaseHost();
    	
    	mysql_connectionURL = "jdbc:mysql://" + host + "/" + GameServer.getDatabaseName() +"?autoReconnect=true";
    	if(!open())
    	{
    		System.out.println("Cannot connect to the database, please check your settings.");
    		System.exit(-1);
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
    private boolean connect(String username, String password) {
    	try {
	        mysql_connection = DriverManager.getConnection(mysql_connectionURL, username, password);
	        if(!mysql_connection.isClosed()) {
	           	return true;
	        }else {
	        	return false;
    		}
		} catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
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
        	stm.close();
        	return true;
    	} catch (SQLException e) {
    		e.printStackTrace();
    		return false;
    	}
    }
    
    /**
     * Closes the connection to the mysql server. Returns true on success.
     * @return
     */
    private boolean close() {
        try {
        	if(!mysql_connection.isClosed()) { // Maybe it's closed already?
        		mysql_connection.close();
        	}
            return true;
        }
        catch (SQLException e)
        {
        	e.printStackTrace();
        	return false;
        }
    }
    
    private boolean open()
    {
    	final String username = GameServer.getDatabaseUsername();
    	final String password = GameServer.getDatabasePassword();
    	final String database = GameServer.getDatabaseName();
    	
    	if(connect(username, password) && selectDatabase(database))
    	{
    		return true;
    	}
    	return false;
    }
    
    /**
     * Returns a result set for a query
     * @param query
     * @return
     */
    public ResultSet query(String query){
    	//System.out.println("Let's search for some data;	" + query);
    	//open();
    	
    	ResultSet mysql_result = null;
    	try {
    		Statement stmt = mysql_connection.createStatement();
            if (query.startsWith("SELECT")) {
                //Use the "executeQuery" function because we have to retrieve data
                //Return the data as a resultset
                mysql_result = stmt.executeQuery(query);
            } else {
                //It's an UPDATE, INSERT, or DELETE statement
                //Use the"executeUpdaye" function and return a null result
            	stmt.executeUpdate(query);
            }
            //stmt.close(); // Omdat het kan.
    	} catch(SQLException e) {
    		e.printStackTrace();
    	}
    	
        //close();
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
