package org.pokenet.client;

import org.pokenet.client.constants.ServerPacket;
import org.pokenet.client.network.Whirlpool;
import org.pokenet.client.protocol.ClientMessage;

/**
 * Generates packets and sends them to the server
 * 
 * @author shadowkanji
 */
public class UserManager
{
	/**
	 * Sends a password change packet
	 * 
	 * @param username
	 * @param newPassword
	 * @param oldPassword
	 */
	public void changePassword(String username, String newPassword, String oldPassword)
	{
		ClientMessage message = new ClientMessage(ServerPacket.CHANGE_PASSWORD);
		message.addString(username + "," + getPasswordHash(username, newPassword) + "," + getPasswordHash(username, oldPassword));
		GameClient.getSession().send(message);
	}

	/**
	 * Sends a login packet to server and chat server
	 * 
	 * @param username
	 * @param password
	 */
	public void login(String username, String password)
	{
		char language = '0';
		if(GameClient.getLanguage().equalsIgnoreCase("english"))
			language = '0';
		else if(GameClient.getLanguage().equalsIgnoreCase("portuguese"))
			language = '1';
		else if(GameClient.getLanguage().equalsIgnoreCase("italian"))
			language = '2';
		else if(GameClient.getLanguage().equalsIgnoreCase("french"))
			language = '3';
		else if(GameClient.getLanguage().equalsIgnoreCase("finnish"))
			language = '4';
		else if(GameClient.getLanguage().equalsIgnoreCase("spanish"))
			language = '5';
		else if(GameClient.getLanguage().equalsIgnoreCase("dutch"))
			language = '6';
		else if(GameClient.getLanguage().equalsIgnoreCase("german"))
			language = '7';

		ClientMessage message = new ClientMessage(ServerPacket.LOGIN);
		message.addString(language + username + "," + getPasswordHash(username, password));
		GameClient.getSession().send(message);
	}

	/**
	 * Sends a registration packet
	 * 
	 * @param username
	 * @param password
	 * @param email
	 * @param dob
	 * @param starter
	 */
	public void register(String username, String password, String email, String dob, int starter, int sprite, int region)
	{
		ClientMessage message = new ClientMessage(ServerPacket.REGISTRATION);
		message.addInt(region);
		message.addString(username + "," + getPasswordHash(username, password) + "," + email + "," + dob + "," + starter + "," + sprite);
		GameClient.getSession().send(message);
	}

	/**
	 * Returns the hashed password
	 * 
	 * @param password
	 * @return
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
}
