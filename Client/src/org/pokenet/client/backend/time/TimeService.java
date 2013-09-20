package org.pokenet.client.backend.time;

import org.pokenet.client.GameClient;

/**
 * Handles time and rendering of night. Also acts as a label for usage in GUI.
 * 
 * @author Myth1c
 **/
public class TimeService implements Runnable
{
	private int m_hour, m_minutes, m_daylight, m_targetDaylight;
	private boolean m_running = true;
	private Thread m_thread;

	/** Default constructor **/
	public TimeService()
	{
		m_thread = new Thread(this);
	}

	/**
	 * This function return the current daylight.
	 * 
	 * @return The current daylight.
	 **/
	public int getDaylight()
	{
		return m_daylight;
	}

	/**
	 * This function returns the daylight target.
	 * 
	 * @return The daylight target.
	 **/
	public int getTargetDaylight()
	{
		return m_targetDaylight;
	}

	/**
	 * This function checks if it is night.
	 * 
	 * @return Returns true if night, otherwise false.
	 **/
	public boolean isNight()
	{
		return m_hour >= 18 && m_hour <= 7;
	}

	/** Called by thread.start() **/
	@Override
	public void run()
	{
		while(m_running)
		{
			String min;
			String hour;
			m_minutes = m_minutes == 59 ? 0 : m_minutes + 1;
			if(m_minutes == 0)
			{
				m_hour = m_hour == 23 ? 0 : m_hour + 1;
				switch(m_hour)
				{
					case 3:
						m_targetDaylight = 150;
						break;
					case 4:
						m_targetDaylight = 125;
						break;
					case 5:
						m_targetDaylight = 100;
						break;
					case 6:
						m_targetDaylight = 75;
						break;
					case 7:
						m_targetDaylight = 0;
						break;
					case 17:
						m_targetDaylight = 75;
						break;
					case 18:
						m_targetDaylight = 100;
						break;
					case 19:
						m_targetDaylight = 125;
						break;
					case 20:
						m_targetDaylight = 150;
						break;
					case 21:
						m_targetDaylight = 175;
						break;
					default:
						break;
				}
			}
			hour = m_hour < 10 ? "0" + String.valueOf(m_hour) : String.valueOf(m_hour);
			min = m_minutes < 10 ? "0" + String.valueOf(m_minutes) : String.valueOf(m_minutes);
			GameClient.getInstance().getHUD().getTopBar().getTimeLabel().setText(hour + ":" + min);
			try
			{
				Thread.sleep(10 * 1000); // Sleep for 10 seconds
			}
			catch(InterruptedException ie)
			{
			}
		}
	}

	/**
	 * Sets the current time and starts the time service.
	 * 
	 * @param The number of hours.
	 * @param The number of minutes.
	 **/
	public void setTime(int hour, int minutes)
	{
		m_hour = hour;
		m_minutes = minutes;
		if(hour == 17)
		{
			m_daylight = 75;
			m_targetDaylight = 75;
		}
		else if(hour == 18)
		{
			m_daylight = 100;
			m_targetDaylight = 100;
		}
		else if(hour == 19)
		{
			m_daylight = 125;
			m_targetDaylight = 125;
		}
		else if(hour == 20)
		{
			m_daylight = 150;
			m_targetDaylight = 150;
		}
		else if(hour == 3)
		{
			m_daylight = 150;
			m_targetDaylight = 150;
		}
		else if(hour == 4)
		{
			m_daylight = 125;
			m_targetDaylight = 125;
		}
		else if(hour == 5)
		{
			m_daylight = 100;
			m_targetDaylight = 100;
		}
		else if(hour == 6)
		{
			m_daylight = 75;
			m_targetDaylight = 75;
		}
		else if(hour >= 8 && hour <= 17)
		{
			m_daylight = 0;
			m_targetDaylight = 0;
		}
		else
		{
			m_daylight = 175;
			m_targetDaylight = 175;
		}
		GameClient.getInstance().getHUD().getTopBar().getTimeLabel().setText(hour + ":" + minutes);
		/* TODO: (IllegalStateExceptions may be generated here.) Stop was causing this next part not to work for some reason... hopefully this doesn't cause any problems if setTime gets called in the future. Seems to generate IllegalStateExceptions */
		if(!m_thread.isAlive())
			m_thread.start();
	}

	/** Updates daylight **/
	public void updateDaylight()
	{
		if(m_daylight < m_targetDaylight)
			m_daylight++;
		else if(m_daylight > m_targetDaylight)
			m_daylight--;
	}

	public void stopTimeService()
	{
		m_running = false;
	}
}
