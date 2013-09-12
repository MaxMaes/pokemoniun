package org.pokenet.client.ui.frames;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;

import javax.swing.Timer;

import org.newdawn.slick.Color;
import org.newdawn.slick.loading.LoadingList;
import org.pokenet.client.GameClient;
import org.pokenet.client.backend.FileLoader;

import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.Widget;
import de.matthiasmann.twl.renderer.Image;
import de.matthiasmann.twl.renderer.lwjgl.LWJGLRenderer;

/**
 * Town Map
 * 
 * @author Myth1c, Chappie112
 */
public class TownMap extends ResizableFrame
{
	private HashMap<Integer,String> locations_Name;
	private HashMap<Integer,Integer> locations_X;
	private HashMap<Integer,Integer> locations_Y;
	private HashMap<Integer,Integer> locations_Width;
	private HashMap<Integer,Integer> locations_Height;
	private int mouseX = 0;
	private int mouseY = 0;
	private Image m_map;
	private boolean locationVisable = false;
	private Timer m_timer;

	/**
	 * Default constructor
	 */
	public TownMap(Widget root)
	{
		setTitle("World Map");
		m_timer = new Timer(500, new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				locationVisable = !locationVisable;
			}
		});
		

		String respath = System.getProperty("res.path");
		if(respath == null)
			respath = "";

		m_map = FileLoader.loadImage(respath + "res/ui/worldmap.png");
		LoadingList.setDeferredLoading(false);
		
		setSize(m_map.getWidth(), m_map.getHeight()-25); // minus 24 to compensate for the titlebar
		
		loadLocations();
		setResizableAxis(ResizableAxis.NONE);
		setVisible(false);
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
			locations_Name = new HashMap<Integer,String>();
			locations_X = new HashMap<Integer, Integer>();
			locations_Y = new HashMap<Integer, Integer>();
			locations_Width = new HashMap<Integer, Integer>();
			locations_Height = new HashMap<Integer, Integer>();

			String f;
			int i = 0;
			while((f = reader.readLine()) != null)
				if(f.charAt(0) != '*')
				{
					
					final String[] details = f.split(",");
					locations_Name.put(i, details[0]);
					locations_Width.put(i,Integer.parseInt(details[1]));
					locations_Height.put(i, Integer.parseInt(details[2]));
					locations_X.put(i,( Integer.parseInt(details[3]) *9 )+ getX());
					locations_Y.put(i, (Integer.parseInt(details[4])*12 )+ getY());
					i++;
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

		mouseX = e.getMouseX();
		mouseY = e.getMouseY();
		return true;
	}

//	/**
//	 * Set's the players current location    <!! OLD CODE !!!>
//	 */
//	public void setPlayerLocation()
//	{
//		removeChild(m_playerLoc);
//		m_playerLoc = new Label();
//		String currentLoc = GameClient.getInstance().getMapMatrix().getCurrentMap().getName();
//		// m_playerLoc.setBackground(new Color(255, 0, 0, 130)); TODO:
//		
//			m_playerLoc.setSize(m_containers.get(currentLoc).getWidth(), m_containers.get(currentLoc).getHeight());
//			m_playerLoc.setPosition(m_containers.get(currentLoc).getX(), m_containers.get(currentLoc).getY());
//			add(m_playerLoc);
//	}
	
	@Override
	protected void paintWidget(GUI gui) 
	{
		super.paintWidget(gui);
		m_map.draw(getAnimationState(), getX(), getY()+24, 700, 450); // draw the map / add the 24 for compensation for the title bar
		LWJGLRenderer renderer = (LWJGLRenderer)gui.getRenderer(); // get the renderer
		renderer.pauseRendering(); // tell the LWJGL renderer to pause so that we can draw our blip
		try 
		{
			Color c = GameClient.getInstance().getGraphics().getColor(); // retrieve the previous used color to reset later
			GameClient.getInstance().getGraphics().setColor(Color.black);
			for(int id : locations_Name.keySet())
				if(mouseY <= locations_Y.get(id) + locations_Height.get(id) && mouseY >= locations_Y.get(id)
				&& mouseX <= locations_X.get(id) + locations_Width.get(id) && mouseX >= locations_X.get(id))
				GameClient.getInstance().getGraphics().drawString(locations_Name.get(id), locations_X.get(id), locations_Y.get(id));
			//GameClient.getInstance().getGraphics().drawString("pallet", this.getX() + 44*11,this.getY() + 12*11);
			if(locationVisable)
			{
				GameClient.getInstance().getGraphics().setColor(Color.red); // set the red color for the blip 
				String currentLoc = GameClient.getInstance().getMapMatrix().getCurrentMap().getName(); // get the current map of the player
				for(int id2 : locations_Name.keySet())
					if(locations_Name.get(id2) == currentLoc)
					{
						GameClient.getInstance().getGraphics().fillOval(locations_X.get(id2), locations_X.get(id2), 10, 10); // draw the Blip
						break;
					}
			}
			
			GameClient.getInstance().getGraphics().setColor(c); // reset the color 
		}
		finally
		{
			renderer.resumeRendering(); // tell LWJGL that we are done, and that it can resume it's business
		}
	
	}
	
	@Override
	public void setVisible(boolean b)
	{
		
		if(b)
			m_timer.start();
		else
			m_timer.stop();
		super.setVisible(b);
	}
}
