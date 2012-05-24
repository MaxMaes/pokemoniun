package org.pokenet.client.updater;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/*
 * For now this class checks if all resources are here, will be used later to check for changes and updates.
 */

/**
 * @author XtremeJedi
 */
public class ClientUpdater
{
	private MessageDigest digest;
	private String respath;
	private List<String> hashes;

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
		respath = System.getProperty("res.path");
		if(respath == null)
		{
			respath = "";
		}
		hashes = new ArrayList<String>();
	}

	/*
	 * If there are too many folders/files, this may cause a stackoverflow. Tested with 8693 files in 67 folders, total size 52.6MB no problem
	 */
	public List<String> loopThroughFiles(File folder)
	{
		File[] files = folder.listFiles();
		for(File file : files)
		{
			if(file.isDirectory())
			{
				loopThroughFiles(file); // Calls same method again.
			}
			else
			{
				String hash = file.getPath() + ";" + getHash(file);
				hashes.add(hash);
				System.out.println(hash);
			}
		}
		return hashes;
	}

	private void dump()
	{
		File resourcesFolder = new File(respath + "res");
		List<String> hashes = loopThroughFiles(resourcesFolder);
		dumpToFile(hashes);
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
			is.close();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		return output;
	}

	private void dumpToFile(List<String> hashes)
	{
		try
		{
			FileWriter fstream = new FileWriter("resources.dump");
			BufferedWriter out = new BufferedWriter(fstream);
			for(String hash : hashes)
			{
				out.write(hash + "\n");
			}
			out.close();
		}
		catch(Exception e)
		{// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}

	/*
	 * For now we read from a local file, will read from a remote file later.
	 */
	private List<String> readDumpFile()
	{
		List<String> read = new ArrayList<String>();
		try
		{
			FileInputStream fstream = new FileInputStream("resources.dump");

			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String line;
			while((line = br.readLine()) != null)
			{
				read.add(line);
			}
			in.close();
		}
		catch(IOException e)
		{
			System.err.println("Error: " + e.getMessage() + " please run the ClientUpdater main to generate a new dump file.");
		}
		return read;
	}

	public boolean checkFiles()
	{
		System.out.println("Checking resource files ...");
		long time = System.currentTimeMillis();
		List<String> fileList = readDumpFile();
		boolean filesFound = true;
		for(String file : fileList)
		{
			String[] line = file.split(";");
			File f = new File(line[0]);
			if(!f.exists() || !getHash(f).equals(line[1]))
			{
				System.out.println("File " + line[0] + " with hash " + line[1] + " not found (make sure you run the dumper first when you've added a new resource file).");
				filesFound = false;
			}
		}
		if(filesFound)
		{
			System.out.println("Done checking resource files in " + (System.currentTimeMillis() - time) + "ms.");
		}
		else
		{
			System.out.println("Some resources may be missing, game not starting.");
		}
		return filesFound;
	}

	/*
	 * Run from this main to dump a hash file from the current resource files.
	 */
	public static void main(String[] args)
	{
		long time = System.currentTimeMillis();
		ClientUpdater updater = new ClientUpdater();
		updater.dump();
		System.out.println("Done dumping in " + (System.currentTimeMillis() - time) + "ms.");
	}
}
