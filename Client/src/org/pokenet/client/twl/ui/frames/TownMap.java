package org.pokenet.client.twl.ui.frames;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.lwjgl.util.Timer;
import org.newdawn.slick.loading.LoadingList;
import org.pokenet.client.GameClient;
import org.pokenet.client.backend.FileLoader;
import org.pokenet.client.twl.ui.base.Image;
import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.Widget;

/**
 * Town Map
 * 
 * @author Myth1c
 */
public class TownMap extends ResizableFrame
{
	private HashMap<String, Widget> m_containers;
	private List<String> m_locations;
	private Image m_map;
	private Label m_mapName;
	private Label m_playerLoc;
	private Timer m_timer;

	/**
	 * Default constructor
	 */
	public TownMap()
	{
		setTitle("World Map");
		m_mapName = new Label();
		m_playerLoc = new Label();
		m_timer = new Timer();

		String respath = System.getProperty("res.path");
		if(respath == null)
			respath = "";

		m_map = new Image(respath + "res/ui/worldmap.png");
		LoadingList.setDeferredLoading(false);

		m_map.setSize(700, 500);
		m_map.setPosition(0, 0);
		m_mapName.setPosition(10, 0);

		add(m_map);
		add(m_mapName);

		setSize(700, 500 + 48);
		loadLocations();
		setResizableAxis(ResizableAxis.NONE);
		setVisible(true);
	}

	/**
	 * Reads the list of locations and adds them to the map
	 */
	public void loadLocations()
	{
		String respath = System.getProperty("res.path");
		if(respath == null)
			respath = "";
		try
		{
			BufferedReader reader;

			try
			{
				reader = new BufferedReader(new InputStreamReader(FileLoader.loadFile(respath + "res/language/" + GameClient.getInstance().getLanguage() + "/UI/_MAP.txt")));
			}
			catch(Exception e)
			{
				reader = new BufferedReader(new InputStreamReader(FileLoader.loadFile(respath + "res/language/english/UI/_MAP.txt")));
			}
			m_containers = new HashMap<String, Widget>();
			m_locations = new ArrayList<String>();

			String f;
			while((f = reader.readLine()) != null)
				if(f.charAt(0) != '*')
				{
					final String[] details = f.split(",");
					m_locations.add(details[0]);
					Widget m_surface = new Widget();
					m_surface.setSize(Integer.parseInt(details[1]), Integer.parseInt(details[2]));
					m_surface.setPosition(Integer.parseInt(details[3]) * 8, Integer.parseInt(details[4]) * 8);
					m_containers.put(details[0], m_surface);
					add(m_containers.get(details[0]));
				}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.err.println("Failed to load map locations");
		}
	}

	@Override
	public boolean handleEvent(Event e)
	{
		int i = 0;
		boolean widgetFound = false;
		for(Widget w : m_containers.values())
		{
			if(w.isInside(e.getMouseX(), e.getMouseY()))
			{
				widgetFound = true;
				m_mapName.setText(m_locations.get(i));
			}
			i++;
		}
		if(!widgetFound)
		{
			m_mapName.setText("");
		}
		return true;
	}

	/**
	 * Set's the players current location
	 */
	public void setPlayerLocation()
	{
		try
		{
			removeChild(m_playerLoc);
			m_playerLoc = new Label();
		}
		catch(Exception e)
		{
		}
		String currentLoc = GameClient.getInstance().getMapMatrix().getCurrentMap().getName();
		// m_playerLoc.setBackground(new Color(255, 0, 0, 130)); TODO:
		try
		{
			m_playerLoc.setSize(m_containers.get(currentLoc).getWidth(), m_containers.get(currentLoc).getHeight());
			m_playerLoc.setPosition(m_containers.get(currentLoc).getX(), m_containers.get(currentLoc).getY());
			add(m_playerLoc);
		}
		catch(Exception e)
		{
		}
	}

	/* TODO:
	 * @SuppressWarnings("static-access")
	 * @Override
	 * public void update(GUIContext container, int delta)
	 * {
	 * if(isVisible())
	 * {
	 * m_timer.tick();
	 * if(m_timer.getTime() >= 0.5)
	 * {
	 * if(m_playerLoc.isVisible())
	 * m_playerLoc.setVisible(false);
	 * else
	 * m_playerLoc.setVisible(true);
	 * m_timer.reset();
	 * }
	 * }
	 * } */
}
