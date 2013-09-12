package org.pokenet.client.backend;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioImpl;
import org.newdawn.slick.openal.AudioLoader;

/**
 * Handles music throughout the game
 * 
 * @author Chappie112
 */
public class SoundManager extends Thread
{
	private static String m_audioPath = "res/music/";
	protected String m_trackName;
	private HashMap<String, String> m_fileList = new HashMap<String, String>();
	private HashMap<String, AudioImpl> m_files = new HashMap<String, AudioImpl>();
	private HashMap<String, String> m_locations = new HashMap<String, String>();
	private boolean m_trackChanged = false;
	private boolean m_isRunning = false;
	private boolean m_mute = false;

	/**
	 * Default Constructor
	 */
	public SoundManager(Boolean muted)
	{
		String respath = System.getProperty("res.path");
		if(respath == null)
			respath = "";
		m_audioPath = respath + m_audioPath;
		m_mute = muted;
		loadLocations();
	}

	public String getTrack()
	{
		return m_trackName;
	}

	/**
	 * Mutes or unmutes the music
	 * 
	 * @param mute
	 */
	public void mute(boolean mute)
	{
		m_mute = mute;
		if(mute)
			AudioImpl.pauseMusic();
		else
			AudioImpl.restartMusic();
	}

	/**
	 * Called by m_thread.start().
	 */
	@Override
	public void run()
	{
		while(m_isRunning)
		{
			if(m_trackChanged && m_trackName != null && !m_mute)
			{
				if(!m_fileList.containsKey(m_trackName))
				{
					loadFile(m_trackName);
				}
				m_trackChanged = false;
				System.out.println("Playing: " + m_trackName);
				m_files.get(m_trackName).playAsMusic(1, 20, true);
			}
			try 
			{
				Thread.sleep(1000);
			} 
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

	private void loadFile(String track) 
	{
		BufferedReader readerStream = FileLoader.loadTextFile(m_audioPath + "index.txt");
		try {
			String f;
			while((f = readerStream.readLine()) != null)
			{
				String[] loadFile = f.split(":", 2);
				if(loadFile[0].equals(track))
				{
					m_fileList.put(loadFile[0], loadFile[1]);
					break;
				}
				else
					continue;
			}
		} 
		catch (IOException e)
		{
			e.printStackTrace();
			System.err.println("ERROR: Failed to open music index!");
		}
		try 
		{
			m_files.put(track,(AudioImpl)AudioLoader.getAudio("OGG", FileLoader.loadFile(m_audioPath + m_fileList.get(track))));
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			System.err.println("Could nOt get audio:" + track);
		}
	}

	/**
	 * Sets the track to play
	 * 
	 * @param key
	 */
	public void setTrack(String key)
	{
		if(key != null && !key.equalsIgnoreCase(m_trackName))
		{
			m_trackName = key;
			m_trackChanged = true;
		}
	}

	/**
	 * Sets the track according to the player's location
	 * 
	 * @param key
	 */
	public void setTrackByLocation(String track)
	{
		if(track != null)
		{
			try
			{
				String key = track;
				if(key.contains("Route"))
					key = "Route";
				if(!m_locations.get(key).equalsIgnoreCase(m_trackName) && m_locations.get(key) != null)
				{
					m_trackName = m_locations.get(key);
					m_trackChanged = true;
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * Starts the thread
	 */
	@Override
	public void start()
	{
		m_isRunning = true;
		super.start();
	}

	/**
	 * Loads the locations and their respective keys
	 */
	private void loadLocations()
	{
		String respath = System.getProperty("res.path");
		if(respath == null)
			respath = "";
		try(BufferedReader stream = FileLoader.loadTextFile(respath + "res/language/english/_MUSICKEYS.txt"))
		{
			String f;
			while((f = stream.readLine()) != null)
			{
				String[] addFile = f.split(":", 2);
				m_locations.put(addFile[0], addFile[1]);
			}
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
	}
}