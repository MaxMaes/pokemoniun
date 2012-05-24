package org.pokenet.client.updater;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/*
 * How this updater works: Download a file from the server which has a list of MD5 hashes of all the resource files. Loop through all the resource files and generate a MD5 hash of those files. Put those generated MD5 hashes in a file and compare it with the file that was grabbed from the server. When there is a MD5 hash that doesn't equal, download the file from the server and restart the client. When all the hashes equal, just start the game. NOTE: for now, the server file with all the hashes listed will be local.
 */

/**
 * @author XtremeJedi
 */
public class ClientUpdater
{
	private MessageDigest digest;

	public ClientUpdater()
	{
		try
		{
			digest = MessageDigest.getInstance("MD5");
		}
		catch(NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
	}

	public void start()
	{
		System.out.println("Checking for updates ...");
		String respath = System.getProperty("res.path");
		if(respath == null)
			respath = "";
		File resourcesFolder = new File(respath + "res");
		checkFiles(resourcesFolder);
		
		// TODO: Compare 2 text files with the hashes.
	}

	/*
	 * If there are too many folders/files, this may cause a stackoverflow. Tested with 8693 files in 67 folders, total size 52.6MB no problem
	 */
	public void checkFiles(File folder)
	{
		File[] files = folder.listFiles();
		for(File file : files)
		{
			if(file.isDirectory())
				checkFiles(file); // Calls same method again.
			else
				getHash(file);
		}
	}

	private String getHash(File file)
	{
		String output = null;
		try
		{
			InputStream is = new FileInputStream(file);
			byte[] buffer = new byte[1024];
			int read = 0;
			while((read = is.read(buffer)) > 0)
			{
				digest.update(buffer, 0, read);
			}
			byte[] md5sum = digest.digest();
			BigInteger bigInt = new BigInteger(1, md5sum);
			output = bigInt.toString(16); // 16 is to convert it to hex.
			System.out.println("MD5: " + output); // TODO: Write this to a file instead of printing it out.
			is.close();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return output;
	}
}
