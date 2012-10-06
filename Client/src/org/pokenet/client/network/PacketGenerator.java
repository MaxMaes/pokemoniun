package org.pokenet.client.network;

import org.apache.mina.core.session.IoSession;
import org.pokenet.client.GameClient;
import org.pokenet.client.backend.entity.Player.Direction;

/**
 * Generates packets and sends them to the server
 * 
 * @author shadowkanji
 */
public class PacketGenerator
{
	private IoSession m_tcpSession;
	private IoSession m_chatSession;

	// Used when attempting to update passwords with old hash method to the new method
	private boolean updatePasswordHashMethod = false;
	private String lastUsername;
	private String lastPassword;
	
	/**
	 * Sets the TCP session.
	 * 
	 * @param The TCP session.
	 */
	public void setTcpSession(IoSession s)
	{
		m_tcpSession = s;
	}

	/**
	 * Sets the chat session.
	 * 
	 * @param The new chat session.
	 */
	public void setChatSession(IoSession s)
	{
		m_chatSession = s;
	}

	/**
	 * Returns the chat session.
	 * 
	 * @return The chat session.
	 */
	public IoSession getChatSession()
	{
		return m_chatSession;
	}

	/**
	 * Returns the TCP session.
	 * 
	 * @return The TCP session.
	 */
	public IoSession getTcpSession()
	{
		return m_tcpSession;
	}

	/**
	 * Writes a message to chat server.
	 * 
	 * @param The message to be sent to the server.
	 */
	public void writeChatServerMessage(String message)
	{
		System.out.println("WRITING " + message);
		if(m_chatSession != null)
			m_chatSession.write(message);
	}

	/**
	 * Sends a packet over TCP.
	 * 
	 * @param The messgae in string format to send.
	 */
	public void writeTcpMessage(String message)
	{
		//System.out.println(message);
		m_tcpSession.write(message);
	}

	/**
	 * Sends a login packet to server and chat server.
	 * 
	 * @param The player's username.
	 * @param The player's password.
	 */
	public void login(String username, String password)
	{
		// store values in case we need to attempt to update to the salted hashes
		this.lastUsername = username;
		this.lastPassword = password;
		char language = '0';
		if(GameClient.getLanguage().equalsIgnoreCase("english"))
		{
			language = '0';
		}
		else if(GameClient.getLanguage().equalsIgnoreCase("portuguese"))
		{
			language = '1';
		}
		else if(GameClient.getLanguage().equalsIgnoreCase("italian"))
		{
			language = '2';
		}
		else if(GameClient.getLanguage().equalsIgnoreCase("french"))
		{
			language = '3';
		}
		else if(GameClient.getLanguage().equalsIgnoreCase("finnish"))
		{
			language = '4';
		}
		else if(GameClient.getLanguage().equalsIgnoreCase("spanish"))
		{
			language = '5';
		}
		else if(GameClient.getLanguage().equalsIgnoreCase("dutch"))
		{
			language = '6';
		}
		else if(GameClient.getLanguage().equalsIgnoreCase("german"))
		{
			language = '7';
		}
		m_tcpSession.write("01" + language + username + "," + (getPasswordHash(username, password)));

		if(GameClient.getInstance().getPacketGenerator().m_chatSession != null)
			m_chatSession.write("01" + language + username + "," + (getPasswordHash(username, password)));
	}

	/**
	 * Sends a registration packet.
	 * 
	 * @param The player's username.
	 * @param The player's password.
	 * @param The player's e-mail address.
	 * @param The player's Date of Birth.
	 * @param The starter the player has chosen.
	 * @param The Sprite number for the player.
	 * @param The number for the starting region.
	 */
	public void register(String username, String password, String email, String dob, int starter, int sprite, int region)
	{
		m_tcpSession.write("02" + region + username + "," + (getPasswordHash(username, password)) + "," + email + "," + dob + "," + starter + "," + sprite);
	}

	/**
	 * Sends a password change packet.
	 * 
	 * @param username
	 * @param newPassword
	 * @param oldPassword
	 */
	public void changePassword(String username, String newPassword, String oldPassword)
	{
		m_tcpSession.write("03" + username + "," + (getPasswordHash(username, newPassword)) + "," + (getPasswordHash(username, oldPassword)));
	}

	/**
	 * Sends a password change packet to update to the new hash function.
	 * 
	 * @param lastUsername
	 * @param lastPassword
	 */
	public void updatePasswordHashMethod()
	{
		m_tcpSession.write("03" + lastUsername + "," + (getPasswordHash(lastUsername, lastPassword)) + "," + (getOldPasswordHash(lastPassword)));
		updatePasswordHashMethod = true;
	}

	/**
	 * Sends a movement packet.
	 * 
	 * @param The direction to move in.
	 */
	public void move(Direction d)
	{
		switch(d)
		{
			case Up:
				m_tcpSession.write("05");
				break;
			case Down:
				m_tcpSession.write("06");
				break;
			case Left:
				m_tcpSession.write("07");
				break;
			case Right:
				m_tcpSession.write("08");
				break;
		}
	}

	/**
	 * Returns whether or not we are in the process of trying to update their password hash from the old method to the new one.
	 * 
	 * @return Returns yes if the password is being updated, otherwise no.
	 */
	public boolean isUpdatingHashMethod()
	{
		return updatePasswordHashMethod;
	}

	/**
	 * Resets values after attempting to update a password hash.
	 */
	public void endUpdateHashMethod()
	{
		updatePasswordHashMethod = false;
		lastUsername = "";
		lastPassword = "";
	}

	/**
	 * Returns the username last attempted to log on as (used to update hashes).
	 * 
	 * @return The last known username used to login.
	 */
	public String getLastUsername()
	{
		return lastUsername;
	}

	/**
	 * Returns the password last used during attempt to log on (used to update hashes).
	 * 
	 * @return The last known password used to login.
	 */
	public String getLastPassword()
	{
		return lastPassword;
	}

	/**
	 * Returns the hashed password.
	 * 
	 * @param The player's username.
	 * @param The player's password.
	 * 
	 * @return The hashed password.
	 */
	private String getPasswordHash(String user, String password)
	{
		String salt = "M0z3ah4SKieNwBboZ94URhIdDbgTNT";
		String user_lowercase = user.toLowerCase();
		// mix the user with the salt to create a unique salt
		String uniqueSalt = "";
		for(int i = 0; i < user_lowercase.length(); i++)
		{
			uniqueSalt = uniqueSalt + user_lowercase.substring(i, i + 1) + salt.substring(i, i + 1);
			// last iteration, add remaining salt to the end
			if(i == user_lowercase.length() - 1)
				uniqueSalt = uniqueSalt + salt.substring(i + 1);
		}

		Whirlpool hasher = new Whirlpool();
		hasher.NESSIEinit();
		// add plaintext password with salt to hasher
		hasher.NESSIEadd(password + uniqueSalt);
		// create array to hold the hashed bytes
		byte[] hashed = new byte[64];
		// run the hash
		hasher.NESSIEfinalize(hashed);
		// turn the byte array into a hexstring
		char[] val = new char[2 * hashed.length];
		String hex = "0123456789ABCDEF";
		for(int i = 0; i < hashed.length; i++)
		{
			int b = hashed[i] & 0xff;
			val[2 * i] = hex.charAt(b >>> 4);
			val[2 * i + 1] = hex.charAt(b & 15);
		}
		return String.valueOf(val);
	}

	/**
	 * Returns the hashed password using the old method.
	 * 
	 * @param The player's password.
	 * 
	 * @return The hashed password.
	 */
	private String getOldPasswordHash(String password)
	{
		Whirlpool hasher = new Whirlpool();
		hasher.NESSIEinit();
		// add the plaintext password to it
		hasher.NESSIEadd(password);
		// create an array to hold the hashed bytes
		byte[] hashed = new byte[64];
		// run the hash
		hasher.NESSIEfinalize(hashed);
		// this stuff basically turns the byte array into a hexstring
		java.math.BigInteger bi = new java.math.BigInteger(hashed);
		String hashedStr = bi.toString(16);            // 120ff0
		if(hashedStr.length() % 2 != 0)
		{
			// Pad with 0
			hashedStr = "0" + hashedStr;
		}
		return hashedStr;
	}
}
