package com.pokemonium.autoupdater;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.ini4j.Ini;
import org.ini4j.Ini.Section;
import org.ini4j.InvalidIniFormatException;

/**
 * @author Myth1c
 */

public class AutoUpdater
{
	private String updatelinks = "http://s1.pokemonium.com/updater/updatelinks.ini";
	private String versionlog = "http://s1.pokemonium.com/updater/releaselog.txt";

	
	private String latestVersion = "";
	private String currentVersion = "";
	private int versionidx = 0;
	private int latestVersionidx = 0;
	private boolean updatedUpdater = false;
	
	private ArrayList<String> versions;
	private Ini updatelinksini;
	
	public AutoUpdater()
	{
		if(checkForUpdate())
		{
			try
			{
				updatelinksini = new Ini(new InputStreamReader(new URL(updatelinks).openStream()));
			}
			catch(InvalidIniFormatException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch(MalformedURLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch(IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("A new version is available, please wait while the game is being updated to the latest version.");
			update();
		}
		else
		{
			System.out.println("You are running the latest version of Pokemonium.");
		}
	}

	public void download_zip_file(URL url)
	{
		File theDir = new File("updates/");

		// if the directory does not exist, create it
		if(!theDir.exists())
		{
			theDir.mkdir();
		}

		try
		{
			URLConnection conn = url.openConnection();
			conn.setDoInput(true);
			conn.setRequestProperty("content-type", "binary/data");

			System.out.println("Update size: " + (Math.round(conn.getContentLength() / 1000000f * 100f) / 100f) + "MB");

			InputStream in = conn.getInputStream();
			FileOutputStream out = new FileOutputStream("updates/" + versions.get(versionidx + 1) + ".zip");

			byte[] b = new byte[1024];
			int count;

			while((count = in.read(b)) > 0)
			{
				out.write(b, 0, count);
			}
			out.close();
			in.close();

		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	private void update()
	{
		// Download next versionidx
		while(versionidx != latestVersionidx)
		{
			if(isForced(versionidx + 1))
			{
				downloadUpdate(versionidx + 1);
			}
			else
			{
				System.out.println("This update might be unstable (hence the question), do you still want to update?");
				System.out.println("[Y] / [N]");

				// Y or N
				Scanner s = new Scanner(System.in);
				String str = s.nextLine();
				if(str.equalsIgnoreCase("y"))
				{
					downloadUpdate(versionidx + 1);
				}
				else
				{
					break;
				}
			}
		}

		System.out.println("Done updating.");
		try
		{
			// Create file
			FileWriter fstream = new FileWriter("version.txt");
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(versions.get(versionidx));
			// Close the output stream
			out.close();
		}
		catch(Exception e)
		{// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}

		// Start game
	}

	private boolean isForced(int i)
	{
		Boolean forced = true;
		Section s = updatelinksini.get(versions.get(i));
		String forc = s.get("forced");
		if(forc.equalsIgnoreCase("NO"))
		{
			forced = false;
		}
		else if(forc.equalsIgnoreCase("YES"))
		{
			forced = true;
		}
		return forced;
	}

	private void downloadUpdate(int i)
	{
		String link = "";
		Section s = updatelinksini.get(versions.get(i));
		link = s.get("link");
		System.out.println("Downloading update from: " + link);

		try
		{
			download_zip_file(new URL(link));
		}
		catch(MalformedURLException e)
		{
			e.printStackTrace();
		}

		System.out.println("Done downloading update: " + versions.get(i));

		extractFile();
	}

	public void extractFile()
	{
		System.out.println("Extracting...");

		try
		{
			byte[] buf = new byte[1024];
			ZipInputStream zipinputstream = null;
			ZipEntry zipentry;
			zipinputstream = new ZipInputStream(new FileInputStream("updates/" + versions.get(versionidx + 1) + ".zip"));

			zipentry = zipinputstream.getNextEntry();
			while(zipentry != null)
			{
				// for each entry to be extracted
				String entryName = zipentry.getName();
				entryName = entryName.replace('/', File.separatorChar);
				entryName = entryName.replace('\\', File.separatorChar);
				System.out.println("Extracting: " + entryName);
				int n;
				FileOutputStream fileoutputstream;
				File newFile = new File(entryName);
				if(zipentry.isDirectory())
				{
					if(!newFile.exists())
					{
						if(!newFile.mkdirs())
						{
							break;
						}
					}
					zipentry = zipinputstream.getNextEntry();
					continue;
				}

				fileoutputstream = new FileOutputStream(entryName);

				while((n = zipinputstream.read(buf, 0, 1024)) > -1)
				{
					fileoutputstream.write(buf, 0, n);
				}

				fileoutputstream.close();
				zipinputstream.closeEntry();
				zipentry = zipinputstream.getNextEntry();

				if(entryName.equalsIgnoreCase("updater.jar"))
				{
					updatedUpdater = true;
				}

			}// while

			zipinputstream.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		System.out.println("Done extracting");

		versionidx += 1;

		if(updatedUpdater)
		{
			try
			{
				// Create file
				FileWriter fstream = new FileWriter("version.txt");
				BufferedWriter out = new BufferedWriter(fstream);
				out.write(versions.get(versionidx));
				// Close the output stream
				out.close();
			}
			catch(Exception e)
			{// Catch exception if any
				System.err.println("Error: " + e.getMessage());
			}

			System.out.println("The updater was updated, please restart the game to resume...");
			System.out.println("Press any key to continue");
			Scanner s = new Scanner(System.in);
			s.nextLine();
			System.exit(0);
		}
	}

	/*
	 * Returns true when an update is available
	 */
	public boolean checkForUpdate()
	{
		System.out.println("Checking for new version");

		versions = new ArrayList<String>();

		try
		{
			BufferedReader in = new BufferedReader(new FileReader(new File("Version.txt")));
			currentVersion = in.readLine();
			System.out.println("Current version: " + currentVersion);
			in.close();
		}
		catch(IOException e)
		{
		}

		try
		{
			// URL of the versionlog.txt
			URL versionURL = new URL(versionlog);

			BufferedReader in = new BufferedReader(new InputStreamReader(versionURL.openStream()));
			String str;

			// Get the latest version
			while((str = in.readLine()) != null)
			{
				latestVersionidx++;
				// Add the version string for easier version lookup when downloading
				versions.add(str);

				// When the current line equals the clients current version, save its index for easier version lookup when downloading
				if(str.equals(currentVersion))
				{
					versionidx = latestVersionidx - 1;
				}

				latestVersion = str;
			}
			latestVersionidx--;
			in.close();
		}
		catch(MalformedURLException e)
		{
		}
		catch(IOException e)
		{
		}
		System.out.println("Latest version: " + latestVersion);

		if(versionidx != latestVersionidx)
			return true;
		return false;
	}
}
