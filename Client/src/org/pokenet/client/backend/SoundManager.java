package org.pokenet.client.backend;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioImpl;
import org.newdawn.slick.openal.AudioLoader;

/**
 * Handles music throughout the game
 * 
 * @author ZombieBear
 */
public class SoundManager extends Thread
{
	private static final int MAX_SOUND_THREADS = 3;
	private static String m_audioPath = "res/music/";
	protected String m_trackName;
	private HashMap<String, String> m_fileList = new HashMap<String, String>();
	private HashMap<String, AudioImpl> m_files = new HashMap<String, AudioImpl>();
	private HashMap<String, String> m_locations = new HashMap<String, String>();
	private boolean m_mute = false;

	private boolean m_tracksLoaded = false, m_trackChanged = true, m_isRunning = false;

	/**
	 * Default Constructor
	 */
	public SoundManager(Boolean muted)
	{
		String respath = System.getProperty("res.path");
		if(respath == null)
			respath = "";
		m_audioPath = respath + m_audioPath;
		loadFileList();
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
	}

	/**
	 * Called by m_thread.start().
	 */
	@Override
	public void run()
	{
		while(m_isRunning)
		{
			if(!m_mute)
				while(!m_tracksLoaded)
					loadFiles();
			if(m_trackChanged && m_tracksLoaded)
				try
				{
					m_trackChanged = false;
					if(!m_mute && m_trackName != null)
					{
						System.out.println("Playing: " + m_trackName);
						// LoadingList.setDeferredLoading(true);
						m_files.get(m_trackName).playAsMusic(1, 20, true);
						// LoadingList.setDeferredLoading(false);
					}
					else if(m_mute && m_trackName != null)
						m_files.clear();
				}
				catch(Exception e)
				{
					e.printStackTrace();
					System.err.println("Failed to load " + m_trackName);
					m_trackChanged = false;
				}
			try
			{
				Thread.sleep(1000);
			}
			catch(Exception e)
			{
			}
		}
	}

	/**
	 * Sets the track to play
	 * 
	 * @param key
	 */
	public void setTrack(String key)
	{
		if(key != m_trackName && key != null)
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
			String key = track;
			if(key.contains("Route"))
				key = "Route";
			if(m_locations.get(key) != m_trackName && m_locations.get(key) != null)
			{
				m_trackName = m_locations.get(key);
				m_trackChanged = true;
			}
		}
	}

	/**
	 * Starts the thread
	 */
	@Override
	public void start()
	{
		if(!m_mute)
		{
			m_isRunning = true;
			super.start();
		}
	}

	/**
	 * Loads the file list
	 */
	private void loadFileList()
	{
		try
		{
			BufferedReader stream = FileLoader.loadTextFile(m_audioPath + "index.txt");

			String f;
			while((f = stream.readLine()) != null)
			{
				String[] addFile = f.split(":", 2);
				try
				{
					if(f.charAt(1) != '*')
						m_fileList.put(addFile[0], addFile[1]);
				}
				catch(Exception e)
				{
					System.err.println("Failed to add file: " + addFile[1]);
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.err.println("Failed to load music");
		}
	}

	/**
	 * Loads the files
	 */
	private void loadFiles()
	{
		ExecutorService soundLoader = Executors.newFixedThreadPool(MAX_SOUND_THREADS);
		for(String key : m_fileList.keySet())
			soundLoader.submit(new SoundThread(key));
		soundLoader.shutdown();
		while(!soundLoader.isTerminated())
		{
			/* Wait for the soundLoader to finish loading the in-game music. */
		}
		System.out.println("Background music loading complete");
		m_tracksLoaded = true;
	}

	private class SoundThread implements Runnable
	{
		private String key;
		private Audio music;

		public SoundThread(String audioKey)
		{
			key = audioKey;
		}

		public void run()
		{
			try
			{
				music = AudioLoader.getAudio("OGG", FileLoader.loadFile(m_audioPath + m_fileList.get(key)));
				m_files.put(key, (AudioImpl) music);
			}
			catch(IOException ioe)
			{
				ioe.printStackTrace();
			}
		}
	}

	/**
	 * Loads the locations and their respective keys
	 */
	private void loadLocations()
	{
		String respath = System.getProperty("res.path");
		if(respath == null)
			respath = "";
		try
		{
			BufferedReader stream = FileLoader.loadTextFile(respath + "res/language/english/_MUSICKEYS.txt");

			String f;
			while((f = stream.readLine()) != null)
			{
				String[] addFile = f.split(":", 2);
				try
				{
					m_locations.put(addFile[0], addFile[1]);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}