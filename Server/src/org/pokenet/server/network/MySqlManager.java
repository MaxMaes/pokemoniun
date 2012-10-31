package org.pokenet.server.network;

/* Simple MySQL Java Class
 * Makes it similair to PHP */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Handles MySql connections
 * 
 * @author Daniel Morante
 * @author XtremeJedi
 */
public class MySqlManager
{
	private static MySqlManager mInstance;
	private Connection mysql_connection;
	private String mysql_connectionURL;
	private ResultSet mysql_result;

	/* public MySqlManager()
	 * {
	 * mysql_connectionURL = "jdbc:mysql://" + GameServer.getDatabaseHost() + ":3306/" + GameServer.getDatabaseName() + "?autoReconnect=true";
	 * if(!open())
	 * {
	 * System.out.println("Cannot connect to the database, please check your settings.");
	 * System.exit(-1);
	 * }
	 * } */

	public static MySqlManager getInstance()
	{
		if(mInstance == null)
			mInstance = new MySqlManager();
		return mInstance;
	}

	public static String parseSQL(String text)
	{
		if(text == null)
			text = "";
		text = text.replace("'", "''");
		text = text.replace("\\", "\\\\");
		return text;
	}

	/**
	 * Returns a result set for a query
	 * 
	 * @param query
	 * @return
	 */
	public ResultSet query(String query)
	{
		Statement stmt;
		if(query.startsWith("SELECT"))
		{
			// Use the "executeQuery" function because we have to retrieve data
			// Return the data as a resultset
			try
			{
				stmt = mysql_connection.createStatement();
				mysql_result = stmt.executeQuery(query);
			}
			catch(Exception x)
			{
				x.printStackTrace();
			}
			return mysql_result;
		}
		else
		{
			// It's an UPDATE, INSERT, or DELETE statement
			// Use the"executeUpdaye" function and return a null result
			try
			{
				stmt = mysql_connection.createStatement();
				stmt.executeUpdate(query);
			}
			catch(Exception x)
			{
				x.printStackTrace();
			}
			return null;
		}
	}

	/* public ResultSet query(String query)
	 * {
	 * ResultSet mysql_result = null;
	 * // System.out.println("Let's search for some data;	" + query);
	 * // open();
	 * try
	 * {
	 * Statement stmt = mysql_connection.createStatement();
	 * if(query.startsWith("SELECT"))
	 * // Use the "executeQuery" function because we have to retrieve data
	 * // Return the data as a resultset
	 * mysql_result = stmt.executeQuery(query);
	 * else
	 * // It's an UPDATE, INSERT, or DELETE statement
	 * // Use the"executeUpdaye" function and return a null result
	 * stmt.executeUpdate(query);
	 * }
	 * catch(SQLException e)
	 * {
	 * e.printStackTrace();
	 * }
	 * // close();
	 * return mysql_result;
	 * } */

	/**
	 * Closes the connection to the mysql server. Returns true on success.
	 * 
	 * @return
	 */
	private boolean close()
	{
		try
		{
			if(!mysql_connection.isClosed())
				mysql_connection.close();
			return true;
		}
		catch(SQLException sqle)
		{
			sqle.printStackTrace();
		}
		return false;
	}

	/**
	 * Selects the current database. Returns true on success
	 * 
	 * @param database
	 * @return
	 */
	public boolean selectDatabase(String database)
	{
		try
		{
			Statement stm = mysql_connection.createStatement();
			stm.executeQuery("USE " + database);
			return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Connects to the server. Returns true on success.
	 * 
	 * @param host
	 * @param username
	 * @param password
	 * @return
	 */
	public boolean connect(String server, String username, String password)
	{
		try
		{
			mysql_connectionURL = "jdbc:mysql://" + server + "?autoReconnect=true";
			mysql_connection = DriverManager.getConnection(mysql_connectionURL, username, password);
			if(!mysql_connection.isClosed())
				return true;
			else
				return false;
		}
		catch(SQLException sqle)
		{
			sqle.printStackTrace();
			return false;
		}
	}

	/* private boolean connect(String username, String password)
	 * {
	 * try
	 * {
	 * mysql_connection = DriverManager.getConnection(mysql_connectionURL, username, password);
	 * if(!mysql_connection.isClosed())
	 * return true;
	 * }
	 * catch(SQLException e)
	 * {
	 * e.printStackTrace();
	 * }
	 * return false;
	 * } */

	/* private boolean open()
	 * {
	 * final String username = GameServer.getDatabaseUsername();
	 * final String password = GameServer.getDatabasePassword();
	 * return connect(username, password);
	 * } */
}
