package org.pokenet.server.network;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.pokenet.server.GameServer;

/**
 * 
 * @author XtremeJedi
 *
 */

/*
 * This class does nothing just YET.
 */

public class DatabaseConnection {
    private static ThreadLocal<Connection> con = new ThreadLocalConnection();
    private static String url = GameServer.getDatabaseHost();
    private static String user = GameServer.getDatabaseUsername();
    private static String pass = GameServer.getDatabasePassword();

    public static Connection getConnection() {
        return con.get();
    }

    public static void close() {
    	try {
    		con.get().close();
            con.remove();
    	} catch(SQLException ex) {
    		ex.printStackTrace();
    	}
    }

    private static class ThreadLocalConnection extends ThreadLocal<Connection> {
        static {
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                System.out.println("Could not locate the JDBC mysql driver.");
            }
        }

        @Override
        protected Connection initialValue() {
            return getConnection();
        }

        private Connection getConnection() {
            try {
                return DriverManager.getConnection(url, user, pass);
            } catch (SQLException sql) {
                System.out.println("Could not create a SQL Connection object. Please make sure you've correctly configured the database properties!");
                return null;
            }
        }

        @Override
        public Connection get() {
            Connection con = super.get();
            try {
                if (!con.isClosed()) {
                    return con;
                }
            } catch (SQLException sql) {
            	// Don't worry we'll just create a new connection :)
            }
            con = getConnection();
            super.set(con);
            return con;
        }
    }
}