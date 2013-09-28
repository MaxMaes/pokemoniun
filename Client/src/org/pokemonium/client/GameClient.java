package org.pokemonium.client;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ConcurrentModificationException;
import java.util.Date;
import org.jboss.netty.channel.ChannelFuture;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
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
import org.pokemonium.client.backend.Animator;
import org.pokemonium.client.backend.BattleManager;
import org.pokemonium.client.backend.ClientMap;
import org.pokemonium.client.backend.ClientMapMatrix;
import org.pokemonium.client.backend.FileLoader;
import org.pokemonium.client.backend.ItemDatabase;
import org.pokemonium.client.backend.ItemSpriteDatabase;
import org.pokemonium.client.backend.KeyManager;
import org.pokemonium.client.backend.MoveLearningManager;
import org.pokemonium.client.backend.Options;
import org.pokemonium.client.backend.PokedexData;
import org.pokemonium.client.backend.PokemonSpriteDatabase;
import org.pokemonium.client.backend.SoundManager;
import org.pokemonium.client.backend.SpriteFactory;
import org.pokemonium.client.backend.TWLInputAdapter;
import org.pokemonium.client.backend.KeyManager.Action;
import org.pokemonium.client.backend.entity.OurPlayer;
import org.pokemonium.client.backend.entity.Player;
import org.pokemonium.client.backend.entity.Player.Direction;
import org.pokemonium.client.backend.time.TimeService;
import org.pokemonium.client.backend.time.WeatherService;
import org.pokemonium.client.backend.time.WeatherService.Weather;
import org.pokemonium.client.constants.Language;
import org.pokemonium.client.constants.Music;
import org.pokemonium.client.constants.ServerPacket;
import org.pokemonium.client.network.Connection;
import org.pokemonium.client.protocol.ClientMessage;
import org.pokemonium.client.ui.GUIPane;
import org.pokemonium.client.ui.HUD;
import org.pokemonium.client.ui.LoginScreen;
import org.pokemonium.client.ui.frames.PlayerPopupDialog;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.renderer.lwjgl.LWJGLRenderer;
import de.matthiasmann.twl.theme.ThemeManager;

/**
 * The game client
 */
public class GameClient extends BasicGame
{
	/**
	 * Returns this instance of game client
	 * Before Battle Music Name
	 * 
	 * @return
	 */
	public static GameClient getInstance()
	{
		if(m_instance == null)
			m_instance = new GameClient(GAME_TITLE);
		return m_instance;
	}

	private static void setOptions(Options opt)
	{
		options = opt;
	}

	private LWJGLRenderer lwjglRenderer;
	private ThemeManager theme;
	private GUI gui;

	private GUIPane root;
	private TWLInputAdapter twlInputAdapter;
	private Session m_session;
	private static boolean debug = false;
	private final static int FPS = 60;
	private final static String GAME_TITLE = "Pokemonium 1.6";
	private static AppGameContainer gameContainer;
	private Connection m_connection;
	private String m_filepath;
	private Font m_fontLarge, m_fontSmall, m_trueTypeFont, m_pokedexfontsmall, m_pokedexfontmedium, m_pokedexfontlarge, m_pokedexfontmini, m_pokedexfontbetweenminiandsmall;
	private volatile static GameClient m_instance;
	private String m_language = Language.ENGLISH;
	private Image m_loadImage; // Made these static to prevent memory leak.
	private boolean m_loadSurroundingMaps = false;
	private DeferredResource m_nextResource;
	private SoundManager m_soundPlayer;
	private Image[] m_spriteImageArray = new Image[500];
	private UserManager m_userManager;
	private static Options options;
	private final long startTime = System.currentTimeMillis();
	private final int DEFAULT_PORT = 7002;
	private int lastPressedKey;
	private Animator m_animator;
	private boolean m_chatServerIsActive;
	private boolean m_close = false; // Used to tell the game to close or not.
	private Color m_daylight;
	private boolean m_isNewMap = false;
	private ClientMapMatrix m_mapMatrix;
	private int m_mapX, m_mapY, m_playerId;
	private MoveLearningManager m_moveLearningManager;
	private OurPlayer m_ourPlayer = null;
	private boolean m_started = false;
	private TimeService m_time;// = new TimeService();
	private WeatherService m_weather;// = new WeatherService();
	private Graphics graphics;

	private static boolean started = false;

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
		}
		catch(SlickException se)
		{
			se.printStackTrace();
		}
		gameContainer.setTargetFrameRate(FPS);

		if(!fullscreen)
			gameContainer.setAlwaysRender(true);
		try
		{
			gameContainer.start();
		}
		catch(SlickException se)
		{
			se.printStackTrace();
		}
		started = true;
	}

	/**
	 * Default constructor
	 * 
	 * @param title
	 */
	private GameClient(String title)
	{
		super(title);
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

	public boolean chatServerIsActive()
	{
		return m_chatServerIsActive;
	}

	/**
	 * When the close button is pressed.
	 */
	@Override
	public boolean closeRequested()
	{
		if(root != null)
		{

			Runnable yes = new Runnable()
			{
				@Override
				public void run()
				{
					try
					{
						if(m_ourPlayer != null)
						{
							ClientMessage dc = new ClientMessage(ServerPacket.LOGOUT_REQUEST);
							m_session.send(dc);
						}
						m_close = true;
						System.exit(0);
					}
					catch(Exception e)
					{
						e.printStackTrace();
						m_close = true;
					}
				}
			};

			Runnable no = new Runnable()
			{
				@Override
				public void run()
				{
					root.hideConfirmationDialog();
					m_close = false;
				}
			};

			root.showConfirmationDialog("Are you sure you want to exit?", yes, no);
		}
		else
			System.out.println("Attempting to close before is client loaded, ignoring");
		return m_close;
	}

	/**
	 * Tries to connect to a requested server. Will notify the user if
	 * connection was succesfully or failed.
	 * 
	 * @param hoststring
	 *        The string that specifies the host including port (e.g.
	 *        127.0.0.1:7002).
	 **/
	public void connect(String hoststring)
	{
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
				root.getLoginScreen().showLogin();
			}
			else
				System.out.println("Problem connecting to the server.");
		}
		catch(Exception e)
		{
			GameClient.getInstance().showMessageDialog("The server is offline, please check back later.");
			getGUIPane().hideLoadingScreen();
		}
	}

	@Override
	public void controllerDownPressed(int controller)
	{
		if(started)
			if(getHUD().getNPCSpeech() == null && !root.getLoginScreen().isVisible() && !getHUD().getChat().hasKeyboardFocus() && !root.getHUD().getPlayerPopupDialog().isVisible())
				if(m_ourPlayer != null && !m_isNewMap && m_ourPlayer.canMove())
					if(!m_mapMatrix.getCurrentMap().isColliding(m_ourPlayer, Direction.Down))
						move(Direction.Down);
					else if(m_ourPlayer.getDirection() != Direction.Down)
						move(Direction.Down);
	}

	@Override
	public void controllerLeftPressed(int controller)
	{
		if(started)
			if(getHUD().getNPCSpeech() == null && !root.getLoginScreen().isVisible() && !getHUD().getChat().hasKeyboardFocus() && !root.getHUD().getPlayerPopupDialog().isVisible())
				if(m_ourPlayer != null && !m_isNewMap && m_ourPlayer.canMove())
					if(!m_mapMatrix.getCurrentMap().isColliding(m_ourPlayer, Direction.Left))
						move(Direction.Left);
					else if(m_ourPlayer.getDirection() != Direction.Left)
						move(Direction.Left);
	}

	@Override
	public void controllerRightPressed(int controller)
	{
		if(started)
			if(getHUD().getNPCSpeech() == null && !root.getLoginScreen().isVisible() && !getHUD().getChat().hasKeyboardFocus() && !root.getHUD().getPlayerPopupDialog().isVisible())
				if(m_ourPlayer != null && !m_isNewMap && m_ourPlayer.canMove())
					if(!m_mapMatrix.getCurrentMap().isColliding(m_ourPlayer, Direction.Right))
						move(Direction.Right);
					else if(m_ourPlayer.getDirection() != Direction.Right)
						move(Direction.Right);
	}

	@Override
	public void controllerUpPressed(int controller)
	{
		if(started)
			if(getHUD().getNPCSpeech() == null && !root.getLoginScreen().isVisible() && !getHUD().getChat().hasKeyboardFocus() && !root.getHUD().getPlayerPopupDialog().isVisible())
				if(m_ourPlayer != null && !m_isNewMap && m_ourPlayer.canMove())
					if(!m_mapMatrix.getCurrentMap().isColliding(m_ourPlayer, Direction.Up))
						move(Direction.Up);
					else if(m_ourPlayer.getDirection() != Direction.Up)
						move(Direction.Up);
	}

	public int getGameContainerWidth()
	{
		return gameContainer.getWidth();
	}

	public int getGameContainerHeight()
	{
		return gameContainer.getHeight();
	}

	public void setFullscreen(boolean toSet)
	{
		try
		{
			gameContainer.setFullscreen(toSet);
		}
		catch(SlickException e)
		{
			e.printStackTrace();
		}
	}

	public void disableKeyRepeat()
	{
		gameContainer.getInput().enableKeyRepeat();
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
	 * The user requests a disconnect and the player is logged out. The player
	 * has to confirm he wants to log out.
	 */
	public void disconnectRequest()
	{
		if(root != null)
		{
			if(root.getConfirmationDialog().isVisible())
				root.hideConfirmationDialog();
			Runnable yes = new Runnable()
			{
				@Override
				public void run()
				{
					try
					{
						ClientMessage dc = new ClientMessage(ServerPacket.LOGOUT_REQUEST);
						m_session.send(dc);
						root.hideConfirmationDialog();
						reset();
						disconnect();
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}
			};

			Runnable no = new Runnable()
			{
				@Override
				public void run()
				{
					root.hideConfirmationDialog();
				}
			};

			root.showConfirmationDialog("Are you sure you want to logout?", yes, no);
		}
		else
			System.out.println("Attempting logout before client loaded, ignoring");
	}

	public void enableKeyRepeat()
	{
		gameContainer.getInput().enableKeyRepeat();
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

	public GUI getGUI()
	{
		return gui;
	}

	public GUIPane getGUIPane()
	{
		return root;
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

	/**
	 * Returns the login screen
	 * 
	 * @return
	 */
	public LoginScreen getLoginScreen()
	{
		return root.getLoginScreen();
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

	/** Returns the options */
	public Options getOptions()
	{
		return options;
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

	public LWJGLRenderer getRenderer()
	{
		return lwjglRenderer;
	}

	public Graphics getGraphics()
	{
		return graphics;
	}

	public Session getSession()
	{
		return m_session;
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

	public ThemeManager getTheme()
	{
		return theme;
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

	public Font getTrueTypeFont()
	{
		return m_trueTypeFont;
	}

	/** Returns the user interface */
	public HUD getHUD()
	{
		return getGUIPane().getHUD();
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

	private void handleKeyPress(int key)
	{
		if(m_started)
		{
			if(root.getLoginScreen().isVisible())
				if(key == Input.KEY_ENTER || key == Input.KEY_NUMPADENTER)
				{
					System.out.println("ENTER");
					root.getLoginScreen().enterKeyDefault();
				}
		}

		if(key == Input.KEY_ENTER)
		{
			if(root.getConfirmationDialog().isVisible() && root.getConfirmationDialog().isVisible())
			{
				root.getConfirmationDialog().runYes();
			}
		}

		if(key == Input.KEY_ESCAPE)
		{
			if(!root.getConfirmationDialog().isVisible())
			{
				Runnable yes = new Runnable()
				{
					@Override
					public void run()
					{
						try
						{
							if(m_ourPlayer != null)
							{
								ClientMessage dc = new ClientMessage(ServerPacket.LOGOUT_REQUEST);
								m_session.send(dc);
							}
							m_close = true;
							System.exit(0);
						}
						catch(Exception e)
						{
							e.printStackTrace();
							m_close = true;
						}
					}
				};

				Runnable no = new Runnable()
				{
					@Override
					public void run()
					{
						root.hideConfirmationDialog();
					}
				};

				root.showConfirmationDialog("Are you sure you want to exit?", yes, no);
			}
			else
			{
				root.hideConfirmationDialog();
			}
		}
		if(getHUD().getNPCSpeech() == null && !getHUD().getChat().hasKeyboardFocus() && !root.getLoginScreen().isVisible() && !BattleManager.getInstance().isBattling() && !m_isNewMap)
		{
			if(m_ourPlayer != null && !m_isNewMap && !BattleManager.getInstance().isBattling() && m_ourPlayer.canMove() && !getHUD().hasShop())
			{
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
					getHUD().toggleChat();
				else if(key == Input.KEY_1)
					getHUD().togglePlayerStats();
				else if(key == Input.KEY_2)
					getHUD().togglePokedex();
				else if(key == Input.KEY_3)
					getHUD().togglePokemon();
				else if(key == Input.KEY_4)
					getHUD().toggleBag();
				else if(key == Input.KEY_5)
					getHUD().toggleMap();
				else if(key == Input.KEY_6)
					getHUD().toggleFriends();
				else if(key == Input.KEY_7)
					getHUD().toggleRequests();
				else if(key == Input.KEY_8)
					getHUD().toggleOptions();
				else if(key == Input.KEY_9)
					getHUD().toggleHelp();
				else if(key == Input.KEY_0)
					getHUD().disconnect();
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
			}
		}
		if(key == KeyManager.getKey(Action.POKEMOVE_1) && !root.getLoginScreen().isVisible() && BattleManager.getInstance().isBattling() && !getHUD().hasMoveLearning())
			BattleManager.getInstance().getBattleWindow().useMove(0);
		if(key == KeyManager.getKey(Action.POKEMOVE_2) && !root.getLoginScreen().isVisible() && BattleManager.getInstance().isBattling() && !getHUD().hasMoveLearning())
			BattleManager.getInstance().getBattleWindow().useMove(1);
		if(key == KeyManager.getKey(Action.POKEMOVE_3) && !root.getLoginScreen().isVisible() && BattleManager.getInstance().isBattling() && !getHUD().hasMoveLearning())
			BattleManager.getInstance().getBattleWindow().useMove(2);
		if(key == KeyManager.getKey(Action.POKEMOVE_4) && !root.getLoginScreen().isVisible() && BattleManager.getInstance().isBattling() && !getHUD().hasMoveLearning())
			BattleManager.getInstance().getBattleWindow().useMove(3);
		if(key == KeyManager.getKey(Action.INTERACTION) && !root.getLoginScreen().isVisible() && !getHUD().getChat().hasKeyboardFocus() && !getHUD().hasMoveLearning())
		{
			if(getHUD().getNPCSpeech() == null && !BattleManager.getInstance().isBattling())// BattleManager.getInstance().getBattleWindow()))
			{
				ClientMessage message = new ClientMessage(ServerPacket.TALKING_START);
				m_session.send(message);
			}
			if(BattleManager.getInstance().isBattling() && getHUD().hasBattleSpeechFrame() && !getHUD().hasMoveLearning())
			{
				getHUD().getBattleSpeechFrame().advance();
			}
			else
			{
				try
				{
					getHUD().getNPCSpeech().advance();
				}
				catch(Exception e)
				{
					getHUD().removeNPCSpeechFrame();
				}
			}
		}
	}

	/** Called before the window is created */
	@Override
	public void init(GameContainer gc) throws SlickException
	{
		Image x16 = new Image("res/icon_16.png");
		Image x32 = new Image("res/icon_32.png");
		Image x128 = new Image("res/icon_128.png");

		ByteBuffer[] buff = new ByteBuffer[3];
		buff[0] = FileLoader.loadImageAsByteBuffer(x16);
		buff[1] = FileLoader.loadImageAsByteBuffer(x32);
		buff[2] = FileLoader.loadImageAsByteBuffer(x128);
		Display.setIcon(buff);

		// Load the images.
		m_loadImage = new Image("res/load.jpg");
		graphics = gc.getGraphics();

		// m_loadImage = m_loadImage.getScaledCopy(gc.getWidth() /
		// m_loadImage.getWidth());
		m_loadImage = m_loadImage.getScaledCopy(800.0f / m_loadImage.getWidth());

		LoadingList.setDeferredLoading(true);

		gc.getGraphics().setWorldClip(-32, -32, 832, 832);
		gc.setShowFPS(false); /* Toggle this to show FPS TODO: include in options */

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
		// save Slick's GL state while loading the theme
		GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
		try
		{
			lwjglRenderer = new LWJGLRenderer();
			File f = new File(m_filepath + "res/themes/default/Default.xml");
			theme = ThemeManager.createThemeManager(f.getAbsoluteFile().toURI().toURL(), lwjglRenderer);
			root = new GUIPane();
			gui = new GUI(root, lwjglRenderer);
			gui.applyTheme(theme);
			gui.setSize(800, 600);
		}
		catch(LWJGLException e)
		{
			e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			GL11.glPopAttrib();
		}

		// connect input
		twlInputAdapter = new TWLInputAdapter(gui, gc.getInput());
		gc.getInput().addPrimaryListener(twlInputAdapter);
		root.getLoginScreen().setServerRevision(2000);
		root.showLoginScreen();

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

		setPlayerSpriteFactory();
		PokemonSpriteDatabase.Init();
		ItemSpriteDatabase.Init();

		m_weather = new WeatherService();
		m_time = new TimeService();
		if(options != null)
			m_weather.setEnabled(options.isWeatherEnabled());

		System.out.println("Loading the files took " + (System.currentTimeMillis() - startTime) + " ms (time from start until you get the language select screen)");
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
	 * Accepts the user input.
	 * 
	 * @param key
	 *        The integer representing the key pressed.
	 * @param c
	 *        ???
	 */
	@Override
	public void keyPressed(int key, char c)
	{
		lastPressedKey = key;
	}

	private void loadSprites()
	{
		try
		{
			String respath = System.getProperty("res.path");
			if(respath == null)
				respath = "";
			/* WARNING: Change 385 to the amount of sprites we have in client
			 * the load bar only works when we don't make a new SpriteSheet ie.
			 * ss = new SpriteSheet(temp, 41, 51); needs to be commented out in
			 * order for the load bar to work. */
			for(int i = -7; i < 474; i++)
			{
				final String location = respath + "res/characters/" + i + ".png";
				m_spriteImageArray[i + 7] = new Image(location);
			}
		}
		catch(SlickException se)
		{
			se.printStackTrace();
		}
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

	public void log(String message)
	{
		if(debug)
			System.out.println(message);
	}

	/** Creates a message Box */
	public void showMessageDialog(final String message)
	{
		// Do this every time because it will mostly be called by events send from Netty and this makes it Gui Thread..
		gui.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				if(!root.getMessageDialog().isVisible())
				{
					root.showMessageDialog(message, new Runnable()
					{
						@Override
						public void run()
						{
							root.hideMessageDialog();
						}
					});
				}
				else
				{
					root.getMessageDialog().queue(message, new Runnable()
					{
						@Override
						public void run()
						{
							root.hideMessageDialog();
						}
					});
				}
			}
		});
	}

	/** Accepts the mouse input */
	@Override
	public void mousePressed(int button, int x, int y)
	{
		// Right Click
		if(button == 1)
		{
			// loop through the players and look for one that's in the
			// place where the user just right-clicked
			for(Player p : m_mapMatrix.getPlayers())
			{
				if(x >= p.getX() + m_mapMatrix.getCurrentMap().getXOffset() && x <= p.getX() + 32 + m_mapMatrix.getCurrentMap().getXOffset()
						&& y >= p.getY() + m_mapMatrix.getCurrentMap().getYOffset() && y <= p.getY() + 40 + m_mapMatrix.getCurrentMap().getYOffset())
				{
					// Brings up a popup menu with player options
					if(!p.isOurPlayer())
					{
						if(root.getHUD().getPlayerPopupDialog() != null)
							root.getHUD().hidePlayerPopupDialog();
						root.getHUD().showPlayerPopupDialog(p.getUsername(), x, y);
					}
				}
			}
		}
		// Left click
		if(button == 0)
		{
			// Get rid of the popup if you click outside of it
			if(root.getHUD().getPlayerPopupDialog() != null)
			{
				PlayerPopupDialog dialog = root.getHUD().getPlayerPopupDialog();
				if(dialog.isVisible())
					if(x > dialog.getInnerX() || x < dialog.getInnerX() + dialog.getWidth())
						dialog.destroy();
					else if(y > dialog.getInnerY() || y < dialog.getInnerY() + dialog.getHeight())
						root.getHUD().hidePlayerPopupDialog();
			}
			// repeats space bar items (space bar emulation for mouse. In case
			// you do not have a space bar!)
			try
			{
				if(getHUD().getChat().isVisible())
				{
					getHUD().getChat().giveupKeyboardFocus();
				}
				if(getHUD().getNPCSpeech() == null && !getHUD().hasBattleDialog())
				{
					ClientMessage message = new ClientMessage(ServerPacket.TALKING_START);
					m_session.send(message);
				}
				if(BattleManager.getInstance().isBattling() && getHUD().hasBattleSpeechFrame() && !getHUD().hasMoveLearning())
				{
					getHUD().getBattleSpeechFrame().advance();
				}
				else if(getHUD().hasNPCSpeechFrame())
				{
					getHUD().getNPCSpeech().advance();
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
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

	/** Reloads options */
	public void reloadOptions()
	{
		try
		{
			GameClient.setOptions(new Options());
			setFullscreen(options.isFullscreenEnabled());
			m_soundPlayer.mute(options.isSoundMuted());
			m_weather.setEnabled(options.isWeatherEnabled());
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.exit(32);
		}
	}

	/** Renders to the game window */
	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException
	{
		g.setColor(Color.white);

		if(!m_started)
		{
			if(LoadingList.get().getRemainingResources() < 1)
				m_started = true;
		}
		else
		{
			/* Clip the screen, no need to render what we're not seeing */
			g.setWorldClip(-32, -32, 864, 664);
			/* If the player is playing, run this rendering algorithm for maps.
			 * The uniqueness here is: For the current map it only renders line
			 * by line for the layer that the player's are on, other layers are
			 * rendered directly to the screen. All other maps are simply
			 * rendered directly to the screen. */
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
				// synchronized(twlInputAdapter)
				// {
				try
				{
					twlInputAdapter.render();
				}
				catch(ConcurrentModificationException e)
				{
					twlInputAdapter.render();
				}
				// }
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
		if(BattleManager.getInstance().isBattling())
			BattleManager.getInstance().endBattle();
		if(getHUD().getNPCSpeech() != null)
			getHUD().removeNPCSpeechFrame();
		if(getHUD().getChat() != null)
			getHUD().hideChat();
		root.hideHUD();
		m_soundPlayer.setTrack(Music.INTRO_AND_GYM);
		root.showLoginScreen();
		root.getLoginScreen().showLanguageSelect();
	}

	/**
	 * Sets values to return to language select, called from server select
	 */
	public void returnToLanguageSelect()
	{
		m_language = Language.ENGLISH;
		getLoginScreen().showLanguageSelect();
	}

	/**
	 * Sets values to return to server select, called from login screen
	 */
	public void returnToServerSelect()
	{
		getLoginScreen().hideServerRevision();
		disconnect();
		getLoginScreen().showServerSelect();
		root.hideHUD();
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
		getGUIPane().showLoadingScreen();
		getHUD().getRequestDialog().clearOffers();
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

	public void setPlayerSpriteFactory()
	{
		Player.setSpriteFactory(new SpriteFactory(m_spriteImageArray));
	}

	public void setSession(Session session)
	{
		m_session = session;
	}

	// public void showAlert(String title, String text)
	// {
	// if(root.getAlertDialog().isVisible())
	// root.hideAlertDialog();
	// Runnable ok = new Runnable()
	// {
	// @Override
	// public void run()
	// {
	// root.hideAlertDialog();
	// }
	// };
	// root.showAlertDialog(title, text, ok);
	// }

	/** Updates the game window */
	/* ! Keep in mind, no calculations in here. Only repaint ! */
	@Override
	public void update(GameContainer gc, int delta) throws SlickException
	{
		twlInputAdapter.update();
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
		if(m_started)
		{
			if(lastPressedKey > -2)
			{
				if(gc.getInput().isKeyDown(lastPressedKey))
					handleKeyPress(lastPressedKey);
				if(lastPressedKey != KeyManager.getKey(Action.WALK_UP) && lastPressedKey != KeyManager.getKey(Action.WALK_LEFT) && lastPressedKey != KeyManager.getKey(Action.WALK_DOWN)
						&& lastPressedKey != KeyManager.getKey(Action.WALK_RIGHT))
					lastPressedKey = -2;
			}
			/* Check if we need to loads maps */
			if(m_isNewMap && m_ourPlayer != null)
			{
				getGUIPane().hideHUD();
				getGUIPane().showLoadingScreen();
				m_mapMatrix.loadMaps(m_mapX, m_mapY, gc.getGraphics());
				m_mapMatrix.getCurrentMap().setName(m_mapMatrix.getMapName(m_mapX, m_mapY));
				m_mapMatrix.getCurrentMap().setXOffset(400 - m_ourPlayer.getX(), false);
				m_mapMatrix.getCurrentMap().setYOffset(300 - m_ourPlayer.getY(), false);
				m_mapMatrix.recalibrate();
				// getHUD().getMap().setPlayerLocation();
				m_isNewMap = false;
				getGUIPane().hideLoadingScreen();
				getGUIPane().showHUD();
			}
			/* Animate the player */
			if(m_ourPlayer != null)
			{
				m_animator.animate();
				/* update battle */
				/* if(BattleManager.getInstance().isBattling() && BattleManager.getInstance().getCurEnemyPoke() != null && BattleManager.getInstance().getCurPoke() != null)
				 * BattleManager.getInstance().getBattleWindow().getCanvas().update(); */
			}
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
}
