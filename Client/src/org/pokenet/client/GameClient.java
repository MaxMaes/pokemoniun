package org.pokenet.client;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ConcurrentModificationException;
import java.util.Date;
import mdes.slick.sui.Container;
import mdes.slick.sui.Display;
import mdes.slick.sui.event.ActionEvent;
import mdes.slick.sui.event.ActionListener;
import org.jboss.netty.channel.ChannelFuture;
import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.loading.DeferredResource;
import org.newdawn.slick.loading.LoadingList;
import org.pokenet.client.backend.Animator;
import org.pokenet.client.backend.BattleManager;
import org.pokenet.client.backend.ClientMap;
import org.pokenet.client.backend.ClientMapMatrix;
import org.pokenet.client.backend.ItemDatabase;
import org.pokenet.client.backend.KeyManager;
import org.pokenet.client.backend.KeyManager.Action;
import org.pokenet.client.backend.MoveLearningManager;
import org.pokenet.client.backend.Options;
import org.pokenet.client.backend.PokedexData;
import org.pokenet.client.backend.SoundManager;
import org.pokenet.client.backend.SpriteFactory;
import org.pokenet.client.backend.entity.OurPlayer;
import org.pokenet.client.backend.entity.Player;
import org.pokenet.client.backend.entity.Player.Direction;
import org.pokenet.client.backend.time.TimeService;
import org.pokenet.client.backend.time.WeatherService;
import org.pokenet.client.backend.time.WeatherService.Weather;
import org.pokenet.client.constants.Language;
import org.pokenet.client.constants.Music;
import org.pokenet.client.constants.ServerPacket;
import org.pokenet.client.network.Connection;
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.ui.LoadingScreen;
import org.pokenet.client.ui.LoginScreen;
import org.pokenet.client.ui.UserInterface;
import org.pokenet.client.ui.base.ConfirmationDialog;
import org.pokenet.client.ui.base.MessageDialog;
import org.pokenet.client.ui.frames.AlertPopupDialog;
import org.pokenet.client.ui.frames.PlayerPopupDialog;

/**
 * The game client
 * 
 * @author shadowkanji
 * @author ZombieBear
 * @author Nushio
 */
public class GameClient extends BasicGame
{
	private Session m_session;
	private static boolean debug = false;
	private final static int FPS = 30;
	private final static String GAME_TITLE = "Pokemonium 1.5.0.3";
	private static AppGameContainer gameContainer;
	private Connection m_connection;
	private String m_filepath;
	private Font m_fontLarge, m_fontSmall, m_trueTypeFont, m_pokedexfontsmall, m_pokedexfontmedium, m_pokedexfontlarge, m_pokedexfontmini, m_pokedexfontbetweenminiandsmall;
	private static GameClient m_instance;
	private String m_language = Language.ENGLISH;
	private Image m_loadBarLeft, m_loadBarRight, m_loadBarMiddle;
	private Image m_loadImage; // Made these static to prevent memory leak.
	private boolean m_loadSurroundingMaps = false;
	private DeferredResource m_nextResource;
	private SoundManager m_soundPlayer;
	private Image[] m_spriteImageArray = new Image[400]; /* TODO: WARNING: Replace with actual number of sprites */
	private UserManager m_userManager;
	private static Options options;
	private final DecimalFormat percentage = new DecimalFormat("###.##");
	private final long startTime = System.currentTimeMillis();
	private final int DEFAULT_PORT = 7002;
	private int lastPressedKey;
	private Animator m_animator;
	private boolean m_chatServerIsActive;
	private boolean m_close = false; // Used to tell the game to close or not.
	private Color m_daylight;
	private ConfirmationDialog m_dcConfirm;
	private Display m_display;
	private ConfirmationDialog m_exitConfirm;
	private AlertPopupDialog m_activeAlert;
	private boolean m_isNewMap = false;
	private LoadingScreen m_loading;
	private LoginScreen m_login;
	private ClientMapMatrix m_mapMatrix;
	private int m_mapX, m_mapY, m_playerId;
	private MoveLearningManager m_moveLearningManager;
	private OurPlayer m_ourPlayer = null;
	private PlayerPopupDialog m_playerDialog;
	private boolean m_started = false;
	private TimeService m_time;// = new TimeService();
	private UserInterface m_ui;
	private WeatherService m_weather;// = new WeatherService();
	private MessageDialog m_messageDialog; // We only want 1 messagedialog, don't we?

	/**
	 * Default constructor
	 * 
	 * @param title
	 */
	private GameClient(String title)
	{
		super(title);
	}

	/** Load options */
	private void initClient()
	{
		m_filepath = System.getProperty("res.path");
		if(m_filepath == null)
			m_filepath = "";
		options = new Options();
		m_soundPlayer = new SoundManager(options.isSoundMuted());
		m_soundPlayer.start();
		m_soundPlayer.setTrack(Music.INTRO_AND_GYM);
		m_loadSurroundingMaps = options.isSurroundingMapsEnabled();
	}

	/**
	 * Changes the playing track
	 * 
	 * @param fileKey
	 */
	public void changeTrack(String fileKey)
	{
		m_soundPlayer.setTrack(fileKey);
	}

	public Connection getConnections()
	{
		return m_connection;
	}

	/** Returns the File Path, if any */
	public String getFilePath()
	{
		return m_filepath;
	}

	/**
	 * Returns the font in large
	 * 
	 * @return
	 */
	public Font getFontLarge()
	{
		return m_fontLarge;
	}

	/**
	 * Returns the font in small
	 * 
	 * @return
	 */
	public Font getFontSmall()
	{
		return m_fontSmall;
	}

	/**
	 * Returns this instance of game client
	 * 
	 * @return
	 */
	public static GameClient getInstance()
	{
		if(m_instance == null)
			m_instance = new GameClient(GAME_TITLE);
		return m_instance;
	}

	/**
	 * Returns the language selection
	 * 
	 * @return
	 */
	public String getLanguage()
	{
		return m_language;
	}

	/** Returns the options */
	public Options getOptions()
	{
		return options;
	}

	/**
	 * Returns the pokedex font between small and mini;
	 */
	public Font getPokedexFontBetweenSmallAndMini()
	{
		return m_pokedexfontbetweenminiandsmall;
	}

	/**
	 * Returns the pokedex font in large
	 */
	public Font getPokedexFontLarge()
	{
		return m_pokedexfontlarge;
	}

	/**
	 * Returns the pokedex font in medium
	 */
	public Font getPokedexFontMedium()
	{
		return m_pokedexfontmedium;
	}

	/**
	 * Returns the pokedex font in mini
	 */
	public Font getPokedexFontMini()
	{
		return m_pokedexfontmini;
	}

	/**
	 * Returns the pokedex font in small
	 */
	public Font getPokedexFontSmall()
	{
		return m_pokedexfontsmall;
	}

	/**
	 * Returns the sound player
	 * 
	 * @return
	 */
	public SoundManager getSoundPlayer()
	{
		return m_soundPlayer;
	}

	public Font getTrueTypeFont()
	{
		return m_trueTypeFont;
	}

	public void log(String message)
	{
		if(debug)
			System.out.println(message);
	}

	/**
	 * If you don't know what this does, you shouldn't be programming!
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		GameClient.getInstance().initClient();
		/* Pipe errors to a file */
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		Date now = new Date(System.currentTimeMillis());
		try
		{
			File logFolder = new File("logs");
			if(!logFolder.exists())
				logFolder.mkdir();
			File log = new File("logs\\" + sdf.format(now) + "-log.txt");
			if(!log.exists())
				log.createNewFile();
			PrintStream p = new PrintStream(log);
			System.setErr(p);
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
		/* See if we need to debug. */
		if(args.length > 0)
			if(args[0].equalsIgnoreCase("-debug") || args[0].equalsIgnoreCase("-d"))
				debug = true;

		KeyManager.initialize();
		PokedexData.loadPokedexData();

		boolean fullscreen = false;
		try
		{
			fullscreen = options.isFullscreenEnabled();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		try
		{
			gameContainer = new AppGameContainer(GameClient.getInstance(), 800, 600, fullscreen);
			gameContainer.setTargetFrameRate(FPS);
			if(!fullscreen)
				gameContainer.setAlwaysRender(true);
			gameContainer.start();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	/** Creates a message Box */
	public void messageDialog(String message, Container container)
	{
		if(m_messageDialog == null || !m_messageDialog.isVisible())
			m_messageDialog = new MessageDialog(message.replace('~', '\n'), container);
	}

	/** Reloads options */
	public void reloadOptions()
	{
		try
		{
			GameClient.setOptions(new Options());
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.exit(32);
		}
	}

	public boolean chatServerIsActive()
	{
		return m_chatServerIsActive;
	}

	public void showAlert(String title, String text)
	{
		if(m_activeAlert != null)
		{
			getUi().getDisplay().remove(m_activeAlert);
		}
		ActionListener ok = new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				m_activeAlert.setVisible(false);
				getDisplay().remove(m_activeAlert);
				m_activeAlert = null;
			}
		};

		m_activeAlert = new AlertPopupDialog(title, text, ok);
		getUi().getDisplay().add(m_activeAlert);
	}

	/**
	 * When the close button is pressed.
	 */
	@Override
	public boolean closeRequested()
	{
		if(m_exitConfirm == null)
		{
			ActionListener yes = new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					try
					{
						if(m_ourPlayer != null)
						{
							ClientMessage dc = new ClientMessage(ServerPacket.LOGOUT_REQUEST);
							m_session.send(dc);
						}
						System.exit(0);
						m_close = true;
					}
					catch(Exception e)
					{
						e.printStackTrace();
						m_close = true;
					}
				}
			};
			ActionListener no = new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					m_exitConfirm.setVisible(false);
					getDisplay().remove(m_exitConfirm);
					m_exitConfirm = null;
					m_close = false;
				}
			};
			m_exitConfirm = new ConfirmationDialog("Are you sure you want to exit?", yes, no);
			if(getUi() != null)
				getUi().getDisplay().add(m_exitConfirm);
			else
				System.out.println("Attempting to close before is client loaded, ignoring");
		}
		return m_close;
	}

	/**
	 * Tries to connect to a requested server. Will notify the user if connection was succesfully or failed.
	 * 
	 * @param hoststring The string that specifies the host including port (e.g. 127.0.0.1:7002).
	 **/
	public void connect(String hoststring)
	{
		m_loading.setVisible(true);
		String[] address = hoststring.split(":");
		String host = address[0];
		int port = DEFAULT_PORT;
		try
		{
			if(address.length == 2)
				port = Integer.parseInt(address[1]);
		}
		catch(NumberFormatException nfe)
		{
			nfe.printStackTrace();
		}
		Socket socket = null;
		m_connection = new Connection(host, port);
		try
		{
			/* Dirty check if the server is alive. */
			socket = new Socket(host, port);
			socket.close();
			if(m_connection.Connect())
			{
				System.out.println("Client connected to port " + port);
				m_userManager = new UserManager();
				m_login.showLogin();
			}
			else
				System.out.println("Problem connecting to the server.");
		}
		catch(Exception e)
		{
			GameClient.getInstance().messageDialog("The server is offline, please check back later.", GameClient.getInstance().getDisplay());
			m_loading.setVisible(false);
		}
	}

	@Override
	public void controllerDownPressed(int controller)
	{
		if(m_ui.getNPCSpeech() == null && !m_login.isVisible() && !m_ui.getChat().isActive() && !getDisplay().containsChild(m_playerDialog))
			if(m_ourPlayer != null && !m_isNewMap && m_ourPlayer.canMove())
				if(!m_mapMatrix.getCurrentMap().isColliding(m_ourPlayer, Direction.Down))
					move(Direction.Down);
				else if(m_ourPlayer.getDirection() != Direction.Down)
					move(Direction.Down);
	}

	@Override
	public void controllerLeftPressed(int controller)
	{
		if(m_ui.getNPCSpeech() == null && !m_login.isVisible() && !m_ui.getChat().isActive() && !getDisplay().containsChild(m_playerDialog))
			if(m_ourPlayer != null && !m_isNewMap && m_ourPlayer.canMove())
				if(!m_mapMatrix.getCurrentMap().isColliding(m_ourPlayer, Direction.Left))
					move(Direction.Left);
				else if(m_ourPlayer.getDirection() != Direction.Left)
					move(Direction.Left);
	}

	@Override
	public void controllerRightPressed(int controller)
	{
		if(m_ui.getNPCSpeech() == null && !m_login.isVisible() && !m_ui.getChat().isActive() && !getDisplay().containsChild(m_playerDialog))
			if(m_ourPlayer != null && !m_isNewMap && m_ourPlayer.canMove())
				if(!m_mapMatrix.getCurrentMap().isColliding(m_ourPlayer, Direction.Right))
					move(Direction.Right);
				else if(m_ourPlayer.getDirection() != Direction.Right)
					move(Direction.Right);
	}

	@Override
	public void controllerUpPressed(int controller)
	{
		if(m_ui.getNPCSpeech() == null && !m_login.isVisible() && !m_ui.getChat().isActive() && !getDisplay().containsChild(m_playerDialog))
			if(m_ourPlayer != null && !m_isNewMap && m_ourPlayer.canMove())
				if(!m_mapMatrix.getCurrentMap().isColliding(m_ourPlayer, Direction.Up))
					move(Direction.Up);
				else if(m_ourPlayer.getDirection() != Direction.Up)
					move(Direction.Up);
	}

	/** Disconnects from the current game/chat server */
	public void disconnect()
	{
		if(m_session != null)
		{
			ChannelFuture channelFuture = m_session.getChannel().close();
			m_session = null;
			channelFuture.awaitUninterruptibly();
			assert channelFuture.isSuccess(): "Warning the Session was not closed";
		}
	}

	/**
	 * The user requests a disconnect and the player is logged out. The player has to confirm he wants to log out.
	 */
	public void disconnectRequest()
	{
		if(m_dcConfirm == null)
		{
			ActionListener yes = new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					try
					{
						ClientMessage dc = new ClientMessage(ServerPacket.LOGOUT_REQUEST);
						m_session.send(dc);
						m_dcConfirm.setVisible(false);
						m_dcConfirm = null;
						reset();
						disconnect();
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}
			};
			ActionListener no = new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					m_dcConfirm.setVisible(false);
					getDisplay().remove(m_dcConfirm);
					m_dcConfirm = null;
				}
			};
			m_dcConfirm = new ConfirmationDialog("Are you sure you want to logout?", yes, no);
			if(getUi() != null)
				getUi().getDisplay().add(m_dcConfirm);
			else
				System.out.println("Attempting close before client loaded, ignoring");
		}
		else
			m_dcConfirm.setVisible(true);
	}

	/** Returns the display */
	public Display getDisplay()
	{
		return m_display;
	}

	/**
	 * Returns the loading screen
	 * 
	 * @return
	 */
	public LoadingScreen getLoadingScreen()
	{
		return m_loading;
	}

	/**
	 * Returns the login screen
	 * 
	 * @return
	 */
	public LoginScreen getLoginScreen()
	{
		return m_login;
	}

	public void enableKeyRepeat()
	{
		gameContainer.getInput().enableKeyRepeat();
	}

	public void disableKeyRepeat()
	{
		gameContainer.getInput().enableKeyRepeat();
	}

	/**
	 * Returns the map matrix
	 * 
	 * @return
	 */
	public ClientMapMatrix getMapMatrix()
	{
		return m_mapMatrix;
	}

	/**
	 * Returns our player
	 * 
	 * @return
	 */
	public OurPlayer getOurPlayer()
	{
		return m_ourPlayer;
	}

	/**
	 * Returns this player's id
	 * 
	 * @return
	 */
	public int getPlayerId()
	{
		return m_playerId;
	}

	/**
	 * Returns the time service
	 * 
	 * @return
	 */
	public TimeService getTimeService()
	{
		return m_time;
	}

	/** Returns the user interface */
	public UserInterface getUi()
	{
		return m_ui;
	}

	public UserManager getUserManager()
	{
		return m_userManager;
	}

	/**
	 * Returns the weather service
	 * 
	 * @return
	 */
	public WeatherService getWeatherService()
	{
		return m_weather;
	}

	/** Called before the window is created */
	@Override
	public void init(GameContainer gc) throws SlickException
	{
		// gc.getGraphics().setBackground(Color.white);

		// Load the images.
		m_loadImage = new Image("res/load.jpg");
		m_loadBarLeft = new Image("res/ui/loadbar/left.png");
		m_loadBarRight = new Image("res/ui/loadbar/right.png");
		m_loadBarMiddle = new Image("res/ui/loadbar/middle.png");

		// m_loadImage = m_loadImage.getScaledCopy(gc.getWidth() / m_loadImage.getWidth());
		m_loadImage = m_loadImage.getScaledCopy(800.0f / m_loadImage.getWidth());

		LoadingList.setDeferredLoading(true);

		gc.getGraphics().setWorldClip(-32, -32, 832, 832);
		gc.setShowFPS(false); /* Toggle this to show FPS */
		m_display = new Display(gc);

		/* Setup variables */
		m_fontLarge = new AngelCodeFont(m_filepath + "res/fonts/dp.fnt", m_filepath + "res/fonts/dp.png");
		m_fontSmall = new AngelCodeFont(m_filepath + "res/fonts/dp-small.fnt", m_filepath + "res/fonts/dp-small.png");
		m_pokedexfontsmall = new AngelCodeFont(m_filepath + "res/fonts/dex-small.fnt", m_filepath + "res/fonts/dex-small.png");
		m_pokedexfontmedium = new AngelCodeFont(m_filepath + "res/fonts/dex-medium.fnt", m_filepath + "res/fonts/dex-medium.png");
		m_pokedexfontlarge = new AngelCodeFont(m_filepath + "res/fonts/dex-large.fnt", m_filepath + "res/fonts/dex-large.png");
		m_pokedexfontmini = new AngelCodeFont(m_filepath + "res/fonts/dex-mini.fnt", m_filepath + "res/fonts/dex-mini.png");
		m_pokedexfontbetweenminiandsmall = new AngelCodeFont(m_filepath + "res/fonts/dex-betweenminiandsmall.fnt", m_filepath + "res/fonts/dex-betweenminiandsmall.png");

		// Player.loadSpriteFactory();

		loadSprites();

		try
		{
			/* DOES NOT WORK YET!!! */
			m_trueTypeFont = new TrueTypeFont(java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, new File(m_filepath + "res/fonts/PokeFont.ttf")).deriveFont(java.awt.Font.PLAIN, 10), false);
			// m_trueTypeFont = m_fontSmall;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			m_trueTypeFont = m_fontSmall;
		}
		/* Time/Weather Services */
		m_time = new TimeService();
		m_weather = new WeatherService();
		if(options != null)
			m_weather.setEnabled(options.isWeatherEnabled());

		/* Add the ui components */
		m_loading = new LoadingScreen();
		m_display.add(m_loading);

		m_login = new LoginScreen();
		m_login.showLanguageSelect();
		m_display.add(m_login);

		// m_ui = new UserInterface(m_display);
		// m_ui.setAllVisible(false);

		/* Item DB */
		ItemDatabase m_itemdb = new ItemDatabase();
		m_itemdb.reinitialise();

		/* Move Learning Manager */
		m_moveLearningManager = MoveLearningManager.getInstance();
		m_moveLearningManager.start();

		/* The animator and map matrix */
		m_mapMatrix = new ClientMapMatrix();
		m_animator = new Animator(m_mapMatrix);

		gc.getInput().enableKeyRepeat();
		// LoadingList.setDeferredLoading(false);

	}

	/**
	 * Accepts the user input.
	 * 
	 * @param key The integer representing the key pressed.
	 * @param c ???
	 */
	@Override
	public void keyPressed(int key, char c)
	{
		lastPressedKey = key;
	}

	/**
	 * Returns false if the user has disabled surrounding map loading
	 * 
	 * @return
	 */
	public boolean loadSurroundingMaps()
	{
		return m_loadSurroundingMaps;
	}

	/** Accepts the mouse input */
	@Override
	public void mousePressed(int button, int x, int y)
	{
		// Right Click
		if(button == 1)
			// loop through the players and look for one that's in the
			// place where the user just right-clicked
			for(Player p : m_mapMatrix.getPlayers())
				if(x >= p.getX() + m_mapMatrix.getCurrentMap().getXOffset() && x <= p.getX() + 32 + m_mapMatrix.getCurrentMap().getXOffset()
						&& y >= p.getY() + m_mapMatrix.getCurrentMap().getYOffset() && y <= p.getY() + 40 + m_mapMatrix.getCurrentMap().getYOffset())
					// Brings up a popup menu with player options
					if(!p.isOurPlayer())
					{
						if(getDisplay().containsChild(m_playerDialog))
							getDisplay().remove(m_playerDialog);
						m_playerDialog = new PlayerPopupDialog(p.getUsername());
						m_playerDialog.setLocation(x, y);
						getDisplay().add(m_playerDialog);
					}
		// Left click
		if(button == 0)
		{
			// Get rid of the popup if you click outside of it
			if(getDisplay().containsChild(m_playerDialog))
				if(x > m_playerDialog.getAbsoluteX() || x < m_playerDialog.getAbsoluteX() + m_playerDialog.getWidth())
					m_playerDialog.destroy();
				else if(y > m_playerDialog.getAbsoluteY() || y < m_playerDialog.getAbsoluteY() + m_playerDialog.getHeight())
					m_playerDialog.destroy();
			// repeats space bar items (space bar emulation for mouse. In case you done have a space bar!)
			try
			{
				if(getDisplay().containsChild(m_ui.getChat()))
					m_ui.getChat().dropFocus();
				if(m_ui.getNPCSpeech() == null && !getDisplay().containsChild(BattleManager.getInstance().getBattleWindow()))
				{
					// m_Session.writeTcpMessage("3C");
					ClientMessage message = new ClientMessage(ServerPacket.TALKING_START);
					m_session.send(message);
				}
				if(BattleManager.getInstance().isBattling() && getDisplay().containsChild(BattleManager.getInstance().getTimeLine().getBattleSpeech())
						&& !getDisplay().containsChild(MoveLearningManager.getInstance().getMoveLearning()))
					BattleManager.getInstance().getTimeLine().getBattleSpeech().advance();
				else
					try
					{
						m_ui.getNPCSpeech().advance();
					}
					catch(Exception e)
					{
						m_ui.nullSpeechFrame();
						// m_Session.write("F");
					}
			}
			catch(Exception e)
			{
			}
		}
	}

	public void move(Direction d)
	{
		switch(d)
		{
			case Up:
				ClientMessage up = new ClientMessage(ServerPacket.MOVE_UP);
				m_session.send(up);
				break;
			case Down:
				ClientMessage down = new ClientMessage(ServerPacket.MOVE_DOWN);
				m_session.send(down);
				break;
			case Left:
				ClientMessage left = new ClientMessage(ServerPacket.MOVE_LEFT);
				m_session.send(left);
				break;
			case Right:
				ClientMessage right = new ClientMessage(ServerPacket.MOVE_RIGHT);
				m_session.send(right);
				break;
		}
	}

	/** Renders to the game window */
	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException
	{
		// g.setBackground(Color.black);
		g.setColor(Color.white);

		int total = LoadingList.get().getTotalResources();
		int maxWidth = gc.getWidth() - 20;
		int loaded = LoadingList.get().getTotalResources() - LoadingList.get().getRemainingResources();
		if(!m_started)
		{
			if(m_nextResource != null)
			{
				g.drawImage(m_loadImage, 0, 0);
				g.drawRoundRect(10, gc.getHeight() - 122, maxWidth - 9, 24, 14);

				float bar = loaded / (float) total;
				g.drawImage(m_loadBarLeft, 13, gc.getHeight() - 120);
				g.drawImage(m_loadBarMiddle, 11 + m_loadBarLeft.getWidth(), gc.getHeight() - 120, bar * (maxWidth - 13), gc.getHeight() - 120 + m_loadBarMiddle.getHeight(), 0, 0,
						m_loadBarMiddle.getWidth(), m_loadBarMiddle.getHeight());
				g.drawImage(m_loadBarRight, bar * (maxWidth - 13), gc.getHeight() - 120);
				g.drawString("Loading,  please wait ... " + percentage.format(bar * 100) + "%", 10, gc.getHeight() - 90);
				if(LoadingList.get().getRemainingResources() < 1)
				{
					m_started = true;
				}
			}

			// g.drawString("Loading: " + m_nextResource.getDescription(), 10, gc.getHeight() - 90);

			// non-imagy loading bar
			// g.setColor(m_loadColor);
			// g.setAntiAlias(true);
			// g.fillRoundRect(15, gc.getHeight() - 120, bar*(maxWidth - 10), 20, 10);
		}
		else
		{
			/* Clip the screen, no need to render what we're not seeing */
			g.setWorldClip(-32, -32, 864, 664);
			/* If the player is playing, run this rendering algorithm for maps. The uniqueness here is: For the current map it only renders line by line for the layer that the player's are on, other layers are rendered directly to the screen. All other maps are simply rendered directly to the screen. */
			if(!m_isNewMap && m_ourPlayer != null)
			{
				ClientMap thisMap;
				g.setFont(m_fontLarge);
				g.scale(2, 2);
				for(int x = 0; x <= 2; x++)
					for(int y = 0; y <= 2; y++)
					{
						thisMap = m_mapMatrix.getMap(x, y);
						if(thisMap != null && thisMap.isRendering())
							thisMap.render(thisMap.getXOffset() / 2, thisMap.getYOffset() / 2, 0, 0, (gc.getScreenWidth() - thisMap.getXOffset()) / 32,
									(gc.getScreenHeight() - thisMap.getYOffset()) / 32, false);
					}
				g.resetTransform();
				try
				{
					m_mapMatrix.getCurrentMap().renderTop(g);
				}
				catch(ConcurrentModificationException e)
				{
					m_mapMatrix.getCurrentMap().renderTop(g);
				}

				if(m_mapX > -30)
				{
					// Render the current weather
					if(m_weather.isEnabled() && m_weather.getParticleSystem() != null)
						try
						{
							m_weather.getParticleSystem().render();
						}
						catch(Exception e)
						{
							m_weather.setEnabled(false);
						}
					// Render the current daylight
					if(m_time.getDaylight() > 0 || m_weather.getWeather() != Weather.NORMAL && m_weather.getWeather() != Weather.SANDSTORM)
					{
						g.setColor(m_daylight);
						g.fillRect(0, 0, 800, 600);
					}
				}
			}
			/* Render the UI layer */
			try
			{
				synchronized(m_display)
				{
					try
					{
						m_display.render(gc, g);
					}
					catch(ConcurrentModificationException e)
					{
						m_display.render(gc, g);
					}
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}

	}

	/** Resets the client back to the z */
	public void reset()
	{
		m_session = null;
		m_ourPlayer = null;
		try
		{
			if(BattleManager.getInstance() != null)
				BattleManager.getInstance().endBattle();
			if(getUi().getNPCSpeech() != null)
				getUi().getNPCSpeech().setVisible(false);
			if(getUi().getChat() != null)
				getUi().getChat().setVisible(false);
			getUi().hideHUD(true);
			getUi().setVisible(false);
		}
		catch(Exception e)
		{
		}
		m_soundPlayer.setTrack(Music.INTRO_AND_GYM);
		m_login.setVisible(true);
		m_login.showLanguageSelect();
	}

	/**
	 * Sets values to return to language select, called from server select
	 */
	public void returnToLanguageSelect()
	{
		m_language = Language.ENGLISH;
		m_login.showLanguageSelect();
	}

	/**
	 * Sets values to return to server select, called from login screen
	 */
	public void returnToServerSelect()
	{
		getLoginScreen().setServerRevision(0000);
		disconnect();
		m_login.showServerSelect();
	}

	/**
	 * Sets the language selection
	 * 
	 * @return
	 */
	public void setLanguage(String lang)
	{
		m_language = lang;
	}

	/**
	 * Sets if the client should load surrounding maps
	 * 
	 * @param b
	 */
	public void setLoadSurroundingMaps(boolean load)
	{
		m_loadSurroundingMaps = load;
	}

	/**
	 * Sets the map and loads them on next update() call
	 * 
	 * @param x
	 * @param y
	 */
	public void setMap(int x, int y)
	{
		m_mapX = x;
		m_mapY = y;
		m_isNewMap = true;
		m_loading.setVisible(true);
		m_ui.getReqWindow().clearOffers();
		m_soundPlayer.setTrackByLocation(m_mapMatrix.getMapName(x, y));
	}

	/**
	 * Sets our player
	 * 
	 * @param pl
	 */
	public void setOurPlayer(OurPlayer pl)
	{
		m_ourPlayer = pl;
	}

	/**
	 * Stores the player's id
	 * 
	 * @param id
	 */
	public void setPlayerId(int id)
	{
		m_playerId = id;
	}

	/** Updates the game window */
	/* ! Keep in mind, no calculations in here. Only repaint ! */
	@Override
	public void update(GameContainer gc, int delta) throws SlickException
	{
		if(LoadingList.get().getRemainingResources() > 0)
		{
			m_nextResource = LoadingList.get().getNext();
			try
			{
				m_nextResource.load();
			}
			catch(IOException ioe)
			{
				ioe.printStackTrace();
			}
			return;
		}
		if(!m_started)
		{
			if(m_ui == null)
			{
				LoadingList.setDeferredLoading(false);

				setPlayerSpriteFactory();

				m_weather = new WeatherService();
				m_time = new TimeService();
				if(options != null)
					m_weather.setEnabled(options.isWeatherEnabled());

				m_ui = new UserInterface(m_display);
				m_ui.setAllVisible(false);
				System.out.println("Loading the files took " + (System.currentTimeMillis() - startTime) + " ms (time from start until you get the language select screen)");
			}
		}
		else
		{
			/* Update the gui layer */
			try
			{
				synchronized(m_display)
				{
					m_display.update(gc, delta);
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			if(lastPressedKey > -2)
			{
				if(gc.getInput().isKeyDown(lastPressedKey))
					handleKeyPress(lastPressedKey);
				if(lastPressedKey != KeyManager.getKey(Action.WALK_UP) && lastPressedKey != KeyManager.getKey(Action.WALK_LEFT) && lastPressedKey != KeyManager.getKey(Action.WALK_DOWN)
						&& lastPressedKey != KeyManager.getKey(Action.WALK_RIGHT))
					lastPressedKey = -2;
			}
			/* Check if we need to loads maps */
			if(m_isNewMap && m_loading.isVisible() && m_ourPlayer != null)
			{
				m_mapMatrix.loadMaps(m_mapX, m_mapY, gc.getGraphics());
				m_mapMatrix.getCurrentMap().setName(m_mapMatrix.getMapName(m_mapX, m_mapY));
				m_mapMatrix.getCurrentMap().setXOffset(400 - m_ourPlayer.getX(), false);
				m_mapMatrix.getCurrentMap().setYOffset(300 - m_ourPlayer.getY(), false);
				m_mapMatrix.recalibrate();
				m_ui.getMap().setPlayerLocation();
				m_isNewMap = false;
				m_loading.setVisible(false);
			}
			/* Animate the player */
			if(m_ourPlayer != null)
				m_animator.animate();
			/* Update weather and daylight */
			if(!m_isNewMap)
			{
				int a = 0;
				// Daylight
				m_time.updateDaylight();
				a = m_time.getDaylight();
				// Weather
				if(m_weather.isEnabled() && m_weather.getWeather() != Weather.NORMAL)
				{
					try
					{
						m_weather.getParticleSystem().update(delta);
					}
					catch(Exception e)
					{
						m_weather.setEnabled(false);
					}
					a = a < 100 ? a + 60 : a;
				}
				m_daylight = new Color(0, 0, 0, a);
			}
		}
	}

	private void handleKeyPress(int key)
	{
		if(m_started)
		{
			if(m_login.isVisible())
			{
				if(key == Input.KEY_ENTER || key == Input.KEY_NUMPADENTER)
				{
					System.out.println("ENTER");
					m_login.enterKeyDefault();
				}
				if(key == Input.KEY_TAB)
					m_login.tabKeyDefault();
			}

			if(key == Input.KEY_ENTER)
			{
				if(m_exitConfirm != null)
					try
					{
						System.exit(0);
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				if(m_dcConfirm != null)
				{
					ClientMessage dc = new ClientMessage(ServerPacket.LOGOUT_REQUEST);
					m_session.send(dc);
					disconnect();
					reset();
					m_dcConfirm.setVisible(false);
					m_dcConfirm = null;
				}
			}
		}

		if(key == Input.KEY_ESCAPE)
			if(m_exitConfirm == null)
			{
				ActionListener yes = new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent arg0)
					{
						try
						{
							System.exit(0);
						}
						catch(Exception e)
						{
							e.printStackTrace();
						}
					}
				};
				ActionListener no = new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent arg0)
					{
						m_exitConfirm.setVisible(false);
						getDisplay().remove(m_exitConfirm);
						m_exitConfirm = null;
					}
				};
				m_exitConfirm = new ConfirmationDialog("Are you sure you want to exit?", yes, no);
				getUi().getDisplay().add(m_exitConfirm);
			}
			else
			{
				m_exitConfirm.setVisible(false);
				getDisplay().remove(m_exitConfirm);
				m_exitConfirm = null;
			}
		if(m_ui.getNPCSpeech() == null && !m_ui.getChat().isActive() && !m_login.isVisible() && !getDisplay().containsChild(m_playerDialog) && !BattleManager.getInstance().isBattling() && !m_isNewMap)
			if(m_ourPlayer != null && !m_isNewMap
			/* && m_loading != null && !m_loading.isVisible() */
			&& !BattleManager.getInstance().isBattling() && m_ourPlayer.canMove())
				if(key == KeyManager.getKey(Action.WALK_DOWN))
				{
					if(!m_mapMatrix.getCurrentMap().isColliding(m_ourPlayer, Direction.Down))
					{
						move(Direction.Down);
						m_ourPlayer.queueMovement(Direction.Down);
					}
					else if(m_ourPlayer.getDirection() != Direction.Down)
					{
						move(Direction.Down);
						m_ourPlayer.queueMovement(Direction.Down);
					}
				}
				else if(key == KeyManager.getKey(Action.WALK_UP))
				{
					if(!m_mapMatrix.getCurrentMap().isColliding(m_ourPlayer, Direction.Up))
					{
						move(Direction.Up);
						m_ourPlayer.queueMovement(Direction.Up);
					}
					else if(m_ourPlayer.getDirection() != Direction.Up)
					{
						move(Direction.Up);
						m_ourPlayer.queueMovement(Direction.Up);
					}
				}
				else if(key == KeyManager.getKey(Action.WALK_LEFT))
				{
					if(!m_mapMatrix.getCurrentMap().isColliding(m_ourPlayer, Direction.Left))
					{
						move(Direction.Left);
						m_ourPlayer.queueMovement(Direction.Left);
					}
					else if(m_ourPlayer.getDirection() != Direction.Left)
					{
						move(Direction.Left);
						m_ourPlayer.queueMovement(Direction.Left);
					}
				}
				else if(key == KeyManager.getKey(Action.WALK_RIGHT))
				{
					if(!m_mapMatrix.getCurrentMap().isColliding(m_ourPlayer, Direction.Right))
					{
						move(Direction.Right);
						m_ourPlayer.queueMovement(Direction.Right);
					}
					else if(m_ourPlayer.getDirection() != Direction.Right)
					{
						move(Direction.Right);
						m_ourPlayer.queueMovement(Direction.Right);
					}
				}
				else if(key == Input.KEY_C)
					m_ui.toggleChat();
				else if(key == Input.KEY_1)
					m_ui.toggleStats();
				else if(key == Input.KEY_2)
					m_ui.togglePokedex();
				else if(key == Input.KEY_3)
					m_ui.togglePokemon();
				else if(key == Input.KEY_4)
					m_ui.toggleBag();
				else if(key == Input.KEY_5)
					m_ui.toggleMap();
				else if(key == Input.KEY_6)
					m_ui.toggleFriends();
				else if(key == Input.KEY_7)
					m_ui.toggleRequests();
				else if(key == Input.KEY_8)
					m_ui.toggleOptions();
				else if(key == Input.KEY_9)
					m_ui.toggleHelp();
				else if(key == Input.KEY_0)
					m_ui.disconnect();
				else if(key == KeyManager.getKey(Action.ROD_OLD))
				{
					ClientMessage message = new ClientMessage(ServerPacket.ITEM_USE);
					message.addString("97");
					m_session.send(message);
				}
				else if(key == KeyManager.getKey(Action.ROD_GOOD))
				{
					ClientMessage message = new ClientMessage(ServerPacket.ITEM_USE);
					message.addString("98");
					m_session.send(message);
				}
				else if(key == KeyManager.getKey(Action.ROD_GREAT))
				{
					ClientMessage message = new ClientMessage(ServerPacket.ITEM_USE);
					message.addString("99");
					m_session.send(message);
				}
				else if(key == KeyManager.getKey(Action.ROD_ULTRA))
				{
					ClientMessage message = new ClientMessage(ServerPacket.ITEM_USE);
					message.addString("100");
					m_session.send(message);
				}
		if(key == KeyManager.getKey(Action.POKEMOVE_1) && BattleManager.getInstance().isBattling() && !getDisplay().containsChild(MoveLearningManager.getInstance().getMoveLearning()))
			BattleManager.getInstance().getBattleWindow().useMove(0);
		if(key == KeyManager.getKey(Action.POKEMOVE_2) && BattleManager.getInstance().isBattling() && !getDisplay().containsChild(MoveLearningManager.getInstance().getMoveLearning()))
			BattleManager.getInstance().getBattleWindow().useMove(1);
		if(key == KeyManager.getKey(Action.POKEMOVE_3) && BattleManager.getInstance().isBattling() && !getDisplay().containsChild(MoveLearningManager.getInstance().getMoveLearning()))
			BattleManager.getInstance().getBattleWindow().useMove(2);
		if(key == KeyManager.getKey(Action.POKEMOVE_4) && BattleManager.getInstance().isBattling() && !getDisplay().containsChild(MoveLearningManager.getInstance().getMoveLearning()))
			BattleManager.getInstance().getBattleWindow().useMove(3);
		if(key == KeyManager.getKey(Action.INTERACTION) && !m_login.isVisible() && !m_ui.getChat().isActive() && !getDisplay().containsChild(MoveLearningManager.getInstance().getMoveLearning())
				&& !getDisplay().containsChild(getUi().getShop()))
		{
			if(m_ui.getNPCSpeech() == null && !getDisplay().containsChild(BattleManager.getInstance().getBattleWindow()))
			{
				ClientMessage message = new ClientMessage(ServerPacket.TALKING_START);
				m_session.send(message);
			}
			if(BattleManager.getInstance().isBattling() && getDisplay().containsChild(BattleManager.getInstance().getTimeLine().getBattleSpeech())
					&& !getDisplay().containsChild(MoveLearningManager.getInstance().getMoveLearning()))
				BattleManager.getInstance().getTimeLine().getBattleSpeech().advance();
			else
				try
				{
					m_ui.getNPCSpeech().advance();
				}
				catch(Exception e)
				{
					m_ui.nullSpeechFrame();
				}
		}
	}

	private void loadSprites()
	{
		try
		{
			String respath = System.getProperty("res.path");
			if(respath == null)
				respath = "";
			/* WARNING: Change 385 to the amount of sprites we have in client the load bar only works when we don't make a new SpriteSheet ie. ss = new SpriteSheet(temp, 41, 51); needs to be commented out in order for the load bar to work. */
			for(int i = -7; i < 385; i++)
				try
				{
					final String location = respath + "res/characters/" + i + ".png";
					m_spriteImageArray[i + 7] = new Image(location);
				}
				catch(Exception e)
				{
					// location = respath + "res/characters/" + String.valueOf(i) + ".png";
					// m_spriteImageArray[i + 5] = new Image(location);
				}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}

	public void setPlayerSpriteFactory()
	{
		Player.setSpriteFactory(new SpriteFactory(m_spriteImageArray));
	}

	public Session getSession()
	{
		return m_session;
	}

	public void setSession(Session session)
	{
		m_session = session;
	}

	private static void setOptions(Options opt)
	{
		options = opt;
	}
}
