package org.pokenet.client.backend;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import org.ini4j.Ini;
import org.ini4j.Ini.Section;
import org.ini4j.InvalidIniFormatException;

public class Options
{
	private int volume;
	private boolean fullscreenEnabled;
	private boolean weatherEnabled;
	private boolean surroundingMapsEnabled;
	private boolean loadSoundEnabled;
	
	private Ini optionsIni;
	
	public Options()
	{
		try
		{		
			optionsIni = new Ini(new FileInputStream("res/options.ini"));
			Section interfaceSection = optionsIni.get("INTERFACE");
			Section soundSection = optionsIni.get("SOUND");
			
			setFullscreenEnabled(convertIntToBoolean(Integer.parseInt(interfaceSection.get("FULLSCREEN"))));
			setSurroundingMapsEnabled(convertIntToBoolean(Integer.parseInt(interfaceSection.get("SHOW_SURROUNDING_MAPS"))));
			setWeatherEnabled(convertIntToBoolean(Integer.parseInt(interfaceSection.get("SHOW_WEATHER"))));
			setLoadSoundEnabled(convertIntToBoolean(Integer.parseInt(soundSection.get("DOLOAD"))));
			volume = Integer.parseInt(soundSection.get("VOLUME"));
		}
		catch(InvalidIniFormatException iie)
		{
			iie.printStackTrace();
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
	}
	
	public void saveSettings()
	{
		try
		{
			FileWriter fstream = new FileWriter("res/options.ini");
			BufferedWriter out = new BufferedWriter(fstream);
			out.write("[INTERFACE]");
			out.newLine();
			out.write("FULLSCREEN=" + convertBoolean(fullscreenEnabled));
			out.newLine();
			out.write("SHOW_SURROUNDING_MAPS=" + convertBoolean(surroundingMapsEnabled));
			out.newLine();
			out.write("SHOW_WEATHER=" + convertBoolean(weatherEnabled));
			out.newLine();
			out.write("[SOUND]");
			out.newLine();
			out.write("DOLOAD=1");
			out.newLine();
			out.write("VOLUME=" + volume);
			out.newLine();
			out.close();
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();	
		}		
	}

	public boolean isFullscreenEnabled()
	{
		return fullscreenEnabled;
	}

	public void setFullscreenEnabled(boolean isEnabled)
	{
		fullscreenEnabled = isEnabled;
	}

	public boolean isWeatherEnabled()
	{
		return weatherEnabled;
	}

	public void setWeatherEnabled(boolean isEnabled)
	{
		weatherEnabled = isEnabled;
	}

	public boolean isLoadSoundEnabled()
	{
		return loadSoundEnabled;
	}

	public void setLoadSoundEnabled(boolean isEnabled)
	{
		loadSoundEnabled = isEnabled;
	}

	public boolean isSurroundingMapsEnabled()
	{
		return surroundingMapsEnabled;
	}

	public void setSurroundingMapsEnabled(boolean isEnabled)
	{
		surroundingMapsEnabled = isEnabled;
	}
	
	
	public void setVolume(int newVolume)
	{
		volume = newVolume;
	}
	
	public int getVolume()
	{
		return volume;
	}

	public boolean isSoundMuted()
	{
		if(getVolume() == 0)
			return true;
		return false;
	}
	
	public boolean convertIntToBoolean(int intValue)
	{
		return (intValue != 0);
	}
	
	public int convertBoolean(boolean boolValue)
	{
		return boolValue ? 1 : 0;
	}
}
