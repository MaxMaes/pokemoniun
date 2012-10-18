package org.pokenet.server.feature;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.pokenet.server.GameServer;

public class DatabaseConnection
{
	private static class ThreadLocalConnection extends ThreadLocal<Connection>
	{
		static
		{
			try
			{
				Class.forName("com.mysql.jdbc.Driver"); // touch the mysql driver
			}
			catch(ClassNotFoundException e)
			{
				System.out.println("Could not locate the JDBC mysql driver.");
			}
		}

		@Override
		public Connection get()
		{
			Connection con = super.get();
			try
			{
				if(!con.isClosed())
					return con;
			}
			catch(SQLException sql)
			{
				// Munch munch, we'll get a new connection. :)
			}
			con = getConnection();
			super.set(con);
			return con;
		}

		@Override
		protected Connection initialValue()
		{
			return getConnection();
		}

		private Connection getConnection()
		{
			try
			{
				return DriverManager.getConnection(url, user, pass);
			}
			catch(SQLException sql)
			{
				System.out.println("Could not create a SQL Connection object. Please make sure you've correctly configured the database properties!");
				return null;
			}
		}
	}

	private static ThreadLocal<Connection> con = new ThreadLocalConnection();
	private final static String pass = GameServer.getDatabasePassword();
	private final static String url = "jdbc:mysql://" + GameServer.getDatabaseHost() + "/" + GameServer.getDatabaseName() + "?autoReconnect=true";

	private final static String user = GameServer.getDatabaseUsername();

	public static Connection getConnection()
	{
		return con.get();
	}

	public static void release() throws SQLException
	{
		con.get().close();
		con.remove();
	}
}