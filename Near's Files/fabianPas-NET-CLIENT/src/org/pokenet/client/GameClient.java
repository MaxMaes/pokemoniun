package org.pokenet.client;

import java.io.File;
import java.net.Socket;
import java.util.ConcurrentModificationException;
import java.util.HashMap;

import mdes.slick.sui.Container;
import mdes.slick.sui.Display;
import mdes.slick.sui.event.ActionEvent;
import mdes.slick.sui.event.ActionListener;

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
import org.newdawn.slick.muffin.FileMuffin;
import org.pokenet.client.backend.Animator;
import org.pokenet.client.backend.BattleManager;
import org.pokenet.client.backend.ClientMap;
import org.pokenet.client.backend.ClientMapMatrix;
import org.pokenet.client.backend.ItemDatabase;
import org.pokenet.client.backend.MoveLearningManager;
import org.pokenet.client.backend.SoundManager;
import org.pokenet.client.backend.SpriteFactory;
import org.pokenet.client.backend.entity.OurPlayer;
import org.pokenet.client.backend.entity.Player;
import org.pokenet.client.backend.entity.Player.Direction;
import org.pokenet.client.backend.time.TimeService;
import org.pokenet.client.backend.time.WeatherService;
import org.pokenet.client.backend.time.WeatherService.Weather;
import org.pokenet.client.network.Connection;
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.ui.LoadingScreen;
import org.pokenet.client.ui.LoginScreen;
import org.pokenet.client.ui.Ui;
import org.pokenet.client.ui.base.ConfirmationDialog;
import org.pokenet.client.ui.base.MessageDialog;
import org.pokenet.client.ui.frames.PlayerPopupDialog;

/**
 * The game client
 * @author shadowkanji
 * @author ZombieBear
 * @author Nushio
 *
 */
@SuppressWarnings("unchecked")
public class GameClient extends BasicGame {
	//Some variables needed
	private static GameClient m_instance;
	private static AppGameContainer gc;
	private ClientMapMatrix m_mapMatrix;
	private OurPlayer m_ourPlayer = null;
	private boolean m_isNewMap = false;
	private int m_mapX, m_mapY, m_playerId;
	private Animator m_animator;
	private static HashMap<String, String> options;
	
	private static UserManager m_userManager;
	
	//Static variables
	private static String m_filepath="";
	private static Font m_fontLarge, m_fontSmall, m_trueTypeFont;
	private static String HOST;
	//UI
	private LoadingScreen m_loading;
	private LoginScreen m_login;
	//The gui display layer
	private Display m_display;

	private WeatherService m_weather;// = new WeatherService();
	private TimeService m_time;// = new TimeService();
	private Ui m_ui;
	private Color m_daylight;
	private static String m_language = "english";
	private static boolean m_languageChosen = false;
	private ConfirmationDialog m_confirm;
	private PlayerPopupDialog m_playerDialog;
	private MoveLearningManager m_moveLearningManager;
	private static SoundManager m_soundPlayer;
	private static boolean m_disableMaps = false;
	public static String UDPCODE = "";
	private static Connection _connection;
	public static Session session;

	public static DeferredResource m_nextResource;
	private boolean m_started;
		
	Color m_loadColor = new Color(70,70,255);
	
	private boolean m_close = false; //Used to tell the game to close or not. 
	private Image[] m_spriteImageArray = new Image[240]; /* WARNING: Replace with actual number of sprites */
	private	Image m_loadImage;
	private Image m_loadBarLeft, m_loadBarRight, m_loadBarMiddle;

	/**
	 * Load options
	 */
	static {
		try {
			try{
				m_filepath = System.getProperty("res.path");
				System.out.println("Path: "+m_filepath);
				if(m_filepath==null)
					m_filepath="";
			}catch(Exception e){
				m_filepath="";
			}
			options = new FileMuffin().loadFile("options.dat");
			if (options == null) {
				options = new HashMap<String,String>();
				options.put("soundMuted", String.valueOf(true));
				options.put("disableMaps", String.valueOf(false));
				options.put("disableWeather", String.valueOf(false));
			}
			m_instance = new GameClient("Pokénet: Squishy Squirtle");
			m_soundPlayer = new SoundManager();
			m_soundPlayer.mute(Boolean.parseBoolean(options.get("soundMuted")));
			m_soundPlayer.start();
			m_disableMaps = Boolean.parseBoolean(options.get("disableMaps"));
		} catch (Exception e) { 
			e.printStackTrace();
			m_instance = new GameClient("Pokénet: Squishy Squirtle");
			m_disableMaps = false;
			m_soundPlayer = new SoundManager();
			m_soundPlayer.mute(false);
			m_soundPlayer.start();
		}

	}

	/**
	 * Default constructor
	 * @param title
	 */
	public GameClient(String title) {
		super(title);
	}

	/**
	 * Called before the window is created
	 */
	@Override
	public void init(GameContainer gc) throws SlickException {
		m_loadImage = new Image("res/load.jpg");
		m_loadImage = m_loadImage.getScaledCopy(800.0f / m_loadImage.getWidth());

		m_loadBarLeft = new Image("res/ui/loadbar/left.png");
		m_loadBarRight = new Image("res/ui/loadbar/right.png");
		m_loadBarMiddle = new Image("res/ui/loadbar/middle.png");
		
		LoadingList.setDeferredLoading(true);
		
		m_instance = this;
		gc.getGraphics().setWorldClip(-32, -32, 832, 832);
		gc.setShowFPS(true);
		m_display = new Display(gc);		

		/*
		 * Setup variables
		 */
		m_fontLarge = new AngelCodeFont(m_filepath+"res/fonts/dp.fnt",m_filepath+"res/fonts/dp.png");
		m_fontSmall = new AngelCodeFont(m_filepath+"res/fonts/dp-small.fnt", m_filepath+"res/fonts/dp-small.png");
		
		loadSprites();
		
		/*
		 * Add the ui components
		 */
		m_loading = new LoadingScreen();
		m_display.add(m_loading);

		m_login = new LoginScreen();

		m_login.showLanguageSelect();
		m_display.add(m_login);

		/*
		 * Item DB
		 */
		ItemDatabase m_itemdb = new ItemDatabase();
		m_itemdb.reinitialise();

		/*
		 * Move Learning Manager
		 */
		m_moveLearningManager = new MoveLearningManager();
		m_moveLearningManager.start();

		/*
		 * The animator and map matrix
		 */
		m_mapMatrix = new ClientMapMatrix();
		m_animator = new Animator(m_mapMatrix);
	}

	private void loadSprites() {
		try {
			String location;
			String respath = System.getProperty("res.path");
			if(respath==null)
				respath="";
			/*
			 * WARNING: Change 224 to the amount of sprites we have in client
			 * the load bar only works when we don't make a new SpriteSheet
			 * ie. ss = new SpriteSheet(temp, 41, 51); needs to be commented out
			 * in order for the load bar to work.
			 */
			for(int i = -5; i < 224; i++) {
				try {

					location = respath+"res/characters/" + String.valueOf(i) + ".png";
					m_spriteImageArray[i + 5] = new Image(location);
					
				} catch (Exception e) {
					location = respath+"res/characters/" + String.valueOf(i) + ".png";
					m_spriteImageArray[i + 5] = new Image(location);
				}
			}
		} catch (Exception e) { 
			e.printStackTrace();
		}
		
	}
	
	void setPlayerSpriteFactory() {
		Player.setSpriteFactory(new SpriteFactory(m_spriteImageArray));
	}
	
	public void move(Direction d) {
		switch(d) {
		case Down:
			ClientMessage down = new ClientMessage();
			down.Init(5);
			session.Send(down);
			break;
		case Up:
			ClientMessage up = new ClientMessage();
			up.Init(4);
			session.Send(up);
			break;
		case Left:
			ClientMessage left = new ClientMessage();
			left.Init(6);
			session.Send(left);
			break;
		case Right:
			ClientMessage right = new ClientMessage();
			right.Init(7);
			session.Send(right);
			break;
		}
	}
	

	/**
	 * Updates the game window
	 */
	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		if (m_nextResource != null) {
			try { 
				m_nextResource.load();
			} catch (Exception e) {
				System.err.println("Failed to load: " + m_nextResource.getDescription() + "\n"
						+ "... WARNING: the game may or may not work because of this");
			}
			
			m_nextResource = null;
		}
		
		if (LoadingList.get().getRemainingResources() > 0) {
			m_nextResource = LoadingList.get().getNext();
		} else {
			if (!m_started) {
				m_started = true;
				m_soundPlayer.setTrack("introandgym");
				if(m_ui == null){
					LoadingList.setDeferredLoading(false);
					
					setPlayerSpriteFactory();

					m_weather = new WeatherService();
					m_time = new TimeService();
					if(options != null)
						m_weather.setEnabled(!Boolean.parseBoolean(options.get("disableWeather")));
					
					m_ui = new Ui(m_display); 
					m_ui.setAllVisible(false);	
				}
				
			}
		}
		
		
		if(m_started){
			if(m_ui.getNPCSpeech() == null && !m_ui.getChat().isActive() && !m_login.isVisible()
					&& !getDisplay().containsChild(m_playerDialog) && !BattleManager.isBattling()
					&& !m_isNewMap){
				if(m_ourPlayer != null && !m_isNewMap 
						 && !BattleManager.isBattling()
						&& m_ourPlayer.canMove()) {
					if (gc.getInput().isKeyDown(Input.KEY_DOWN) || gc.getInput().isKeyDown(Input.KEY_S)) {
						if(!m_mapMatrix.getCurrentMap().isColliding(m_ourPlayer, Direction.Down)) {
							m_ourPlayer.queueMovement(Direction.Down);
							this.move(Direction.Down);
						} else if(m_ourPlayer.getDirection() != Direction.Down) {
							m_ourPlayer.queueMovement(Direction.Down);
							this.move(Direction.Down);
						}
					} else if (gc.getInput().isKeyDown(Input.KEY_UP) || gc.getInput().isKeyDown(Input.KEY_W)) {
						if(!m_mapMatrix.getCurrentMap().isColliding(m_ourPlayer, Direction.Up)) {
							m_ourPlayer.queueMovement(Direction.Up);
							this.move(Direction.Up);
						} else if(m_ourPlayer.getDirection() != Direction.Up) {
							m_ourPlayer.queueMovement(Direction.Up);
							this.move(Direction.Up);
						}
					} else if (gc.getInput().isKeyDown(Input.KEY_LEFT) || gc.getInput().isKeyDown(Input.KEY_A)) {
						if(!m_mapMatrix.getCurrentMap().isColliding(m_ourPlayer, Direction.Left)) {
							m_ourPlayer.queueMovement(Direction.Left);
							this.move(Direction.Left);
						} else if(m_ourPlayer.getDirection() != Direction.Left) {
							m_ourPlayer.queueMovement(Direction.Left);
							this.move(Direction.Left);
						}
					} else if (gc.getInput().isKeyDown(Input.KEY_RIGHT) || gc.getInput().isKeyDown(Input.KEY_D)) {
						if(!m_mapMatrix.getCurrentMap().isColliding(m_ourPlayer, Direction.Right)) {
							m_ourPlayer.queueMovement(Direction.Right);
							this.move(Direction.Right);
						} else if(m_ourPlayer.getDirection() != Direction.Right) {
							m_ourPlayer.queueMovement(Direction.Right);
							this.move(Direction.Right);
						}
					} 
				}
			}
		}
		
		if(m_started){
			/*
			 * Update the gui layer
			 */
			try {
				synchronized (m_display) {
					try{
						m_display.update(gc, delta);
					} catch (Exception e) {}
				}
			} catch (Exception e) { e.printStackTrace(); }
			/*
			 * Check if language was chosen.
			 */
			if(m_language != null && !m_language.equalsIgnoreCase("") && m_languageChosen==true && ((HOST != null && HOST.equalsIgnoreCase("")) || m_userManager == null)) {
				m_login.showServerSelect();
			} else if(m_language == null || m_language.equalsIgnoreCase("")){
				m_login.showLanguageSelect();
			}
			/*
			 * Check if we need to connect to a selected server
			 */
			if(HOST != null && !HOST.equalsIgnoreCase("") && _connection == null) {
				this.connect();
			}
			/*
			 * Check if we need to loads maps
			 */
			if(m_isNewMap && m_loading.isVisible()) {
				m_mapMatrix.loadMaps(m_mapX, m_mapY, gc.getGraphics());
				while(m_ourPlayer == null);
				m_mapMatrix.getCurrentMap().setName(m_mapMatrix.getMapName(m_mapX, m_mapY));
				m_mapMatrix.getCurrentMap().setXOffset(400 - m_ourPlayer.getX(), false);
				m_mapMatrix.getCurrentMap().setYOffset(300 - m_ourPlayer.getY(), false);
				m_mapMatrix.recalibrate();
				m_ui.getMap().setPlayerLocation();
				m_isNewMap = false;
				m_loading.setVisible(false);
			}
			/*
			 * Animate the player
			 */
			if(m_ourPlayer != null) {
				m_animator.animate();
			}
			/*
			 * Update weather and daylight
			 */
			if(!m_isNewMap) {
				int a = 0;
				//Daylight
				m_time.updateDaylight();
				a = m_time.getDaylight();
				//Weather
				if(m_weather.isEnabled() && m_weather.getWeather() != Weather.NORMAL) {
					try {
						m_weather.getParticleSystem().update(delta);
					} catch (Exception e) {
						m_weather.setEnabled(false);
					}
					a = a < 100 ? a + 60 : a;
				}
				m_daylight = new Color(0, 0, 0, a);
			}
		}
		
	}

	/**
	 * Renders to the game window
	 */
	public void render(GameContainer gc, Graphics g) throws SlickException {
		g.drawImage(m_loadImage, 0, 0);
		
		 if (m_nextResource != null) {
			g.setColor(Color.white);
			g.drawString("Loading: "+m_nextResource.getDescription(), 10, gc.getHeight() - 90);
		}
		
		int total = LoadingList.get().getTotalResources();
		int maxWidth = gc.getWidth() - 20;
		int loaded = LoadingList.get().getTotalResources() - LoadingList.get().getRemainingResources();
		if(!m_started){
			
			g.setColor(Color.white);
			g.drawRoundRect(10 ,gc.getHeight() - 122, maxWidth - 9, 24, 14);	
			
			float bar = loaded / (float) total;
			g.drawImage(m_loadBarLeft, 13, gc.getHeight() - 120);
			g.drawImage(m_loadBarMiddle, 11 + m_loadBarLeft.getWidth(), gc.getHeight() - 120, 
					 bar*(maxWidth - 13), gc.getHeight() - 120 + m_loadBarMiddle.getHeight(), 
					 0, 0, m_loadBarMiddle.getWidth(), m_loadBarMiddle.getHeight());
			g.drawImage(m_loadBarRight,  bar*(maxWidth - 13), gc.getHeight() - 120);
		}
		
		if (m_started){

			/* Clip the screen, no need to render what we're not seeing */
			g.setWorldClip(-32, -32, 864, 664);
			/*
			 * If the player is playing, run this rendering algorithm for maps.
			 * The uniqueness here is:
			 *  For the current map it only renders line by line for the layer that the player's are on, 
			 *  other layers are rendered directly to the screen.
			 *  All other maps are simply rendered directly to the screen.
			 */
			if(!m_isNewMap && m_ourPlayer != null) {
				ClientMap thisMap;
				g.setFont(m_fontLarge);
				g.scale(2, 2);
				for (int x = 0; x <= 2; x++) {
					for (int y = 0; y <= 2; y++) {
						thisMap = m_mapMatrix.getMap(x, y);
						if (thisMap != null && thisMap.isRendering()) {
							thisMap.render(thisMap.getXOffset() / 2,
									thisMap.getYOffset() / 2, 0, 0,
									(gc.getScreenWidth() - thisMap.getXOffset()) / 32,
									(gc.getScreenHeight() - thisMap.getYOffset()) / 32,
									false);
						}
					}
				}
				g.resetTransform();
				try {
					m_mapMatrix.getCurrentMap().renderTop(g);
				}catch (ConcurrentModificationException e){
					m_mapMatrix.getCurrentMap().renderTop(g);
				}

				if(m_mapX > -30) {
					//Render the current weather
					if(m_weather.isEnabled() && m_weather.getParticleSystem() != null) {
						try {
							m_weather.getParticleSystem().render();
						} catch(Exception e) {
							m_weather.setEnabled(false);
						}
					}
					//Render the current daylight
					if(m_time.getDaylight() > 0 || 
							(m_weather.getWeather() != Weather.NORMAL && 
									m_weather.getWeather() != Weather.SANDSTORM)) {
						g.setColor(m_daylight);
						g.fillRect(0, 0, 800, 600);
					}
				}
			}
			/*
			 * Render the UI layer
			 */
			try {
				synchronized(m_display) {
					try{
						m_display.render(gc, g);
					} catch (ConcurrentModificationException e){m_display.render(gc, g);}
				}
			} catch (Exception e) { e.printStackTrace(); }	
		}
		
	}

	/**
	 * Accepts the user input.
	 * @param key The integer representing the key pressed.
	 * @param c ???
	 */
	@Override
	public void keyPressed(int key, char c) {
		if(m_started){
			if (m_login.isVisible()){
				if (key == (Input.KEY_ENTER) || key == (Input.KEY_NUMPADENTER))
					m_login.enterKeyDefault();
				if (key == (Input.KEY_TAB))
					m_login.tabKeyDefault();
			}

			if (key == (Input.KEY_ESCAPE)) {
				if(m_confirm==null){
					ActionListener yes = new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							try {
								System.exit(0);
							} catch (Exception e) {
								e.printStackTrace();
							}

						}
					};
					ActionListener no = new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							m_confirm.setVisible(false);
							getDisplay().remove(m_confirm);
							m_confirm = null;
						}
					};
					m_confirm = new ConfirmationDialog("Are you sure you want to exit?",yes,no);
					getUi().getDisplay().add(m_confirm);
				}else{
					System.exit(0);
				}
			}
			if(m_ui.getNPCSpeech() == null && !m_ui.getChat().isActive() && !m_login.isVisible()
					&& !getDisplay().containsChild(m_playerDialog) && !BattleManager.isBattling()
					&& !m_isNewMap){
				if(m_ourPlayer != null && !m_isNewMap 
						/*&& m_loading != null && !m_loading.isVisible()*/
						 && !BattleManager.isBattling()
						&& m_ourPlayer.canMove()) {
					if (key == Input.KEY_C) {
						m_ui.toggleChat();
					} else if (key == (Input.KEY_1)) {
						m_ui.toggleStats();
					} else if (key == (Input.KEY_2)) {
						m_ui.togglePokemon();
					} else if (key == (Input.KEY_3)) {
						m_ui.toggleBag();
					} else if (key == (Input.KEY_4)) {
						m_ui.toggleMap();
					} else if (key == (Input.KEY_5)) {
						m_ui.toggleFriends();
					} else if (key == (Input.KEY_6)) {
						m_ui.toggleRequests();
					} else if (key == (Input.KEY_7)) {
						m_ui.toggleOptions();
					} else if (key == (Input.KEY_8)) {
						m_ui.toggleHelp();
					}
				}
			}
			if ((key == (Input.KEY_SPACE) || key == (Input.KEY_E)) && !m_login.isVisible() &&
					!m_ui.getChat().isActive() && !getDisplay().containsChild(MoveLearningManager.getInstance()
							.getMoveLearning()) && !getDisplay().containsChild(getUi().getShop())) {
				if(m_ui.getNPCSpeech() == null && !getDisplay().containsChild(BattleManager.getInstance()
						.getBattleWindow()) ){
					ClientMessage message = new ClientMessage();
					message.Init(47);
					session.Send(message);
				}
				if (BattleManager.isBattling() && 
						getDisplay().containsChild(BattleManager.getInstance().getTimeLine().getBattleSpeech())
						&& !getDisplay().containsChild(MoveLearningManager.getInstance().getMoveLearning())) {
					BattleManager.getInstance().getTimeLine().getBattleSpeech().advance();
				} else{
					try {
						m_ui.getNPCSpeech().advance();
					} catch (Exception e) { 
						m_ui.nullSpeechFrame();
						//					m_packetGen.write("F"); 
					}
				}
			}	
		}
		
	}

	/*@Override
	public void controllerDownPressed(int controller){
		if(m_ui.getNPCSpeech() == null && m_ui.getChat().isActive()==false && !m_login.isVisible()
				&& !m_ui.getChat().isActive() && !getDisplay().containsChild(m_playerDialog)){
			if(m_ourPlayer != null && !m_isNewMap
					/*&& m_loading != null && !m_loading.isVisible()
					&& m_ourPlayer.canMove()) {
				if(!m_mapMatrix.getCurrentMap().isColliding(m_ourPlayer, Direction.Down)) {
					this.move(Direction.Down);
				} else if(m_ourPlayer.getDirection() != Direction.Down) {
					this.move(Direction.Down);
				}
			}
		}
	}*/

	/*@Override
	public void controllerUpPressed(int controller){
		if(m_ui.getNPCSpeech() == null && m_ui.getChat().isActive()==false && !m_login.isVisible()
				&& !m_ui.getChat().isActive() && !getDisplay().containsChild(m_playerDialog)){
			if(m_ourPlayer != null && !m_isNewMap
					/*&& m_loading != null && !m_loading.isVisible()
					&& m_ourPlayer.canMove()) {
				if(!m_mapMatrix.getCurrentMap().isColliding(m_ourPlayer, Direction.Up)) {
					this.move(Direction.Up);
				} else if(m_ourPlayer.getDirection() != Direction.Up) {
					this.move(Direction.Up);
				}
			}
		}
	}*/

	/*@Override
	public void controllerLeftPressed(int controller){
		if(m_ui.getNPCSpeech() == null && m_ui.getChat().isActive() == false && !m_login.isVisible()
				&& !m_ui.getChat().isActive() && !getDisplay().containsChild(m_playerDialog)){
			if(m_ourPlayer != null && !m_isNewMap
					/*&& m_loading != null && !m_loading.isVisible()
					&& m_ourPlayer.canMove()) {
				if(!m_mapMatrix.getCurrentMap().isColliding(m_ourPlayer, Direction.Left)) {
					this.move(Direction.Left);
				} else if(m_ourPlayer.getDirection() != Direction.Left) {
					this.move(Direction.Left);
				}
			}
		}
	}*/

	/*@Override
	public void controllerRightPressed(int controller){
		if(m_ui.getNPCSpeech() == null && m_ui.getChat().isActive()==false && !m_login.isVisible()
				&& !m_ui.getChat().isActive() && !getDisplay().containsChild(m_playerDialog)){
			if(m_ourPlayer != null && !m_isNewMap
					/*&& m_loading != null && !m_loading.isVisible()
					&& m_ourPlayer.canMove()) {
				if(!m_mapMatrix.getCurrentMap().isColliding(m_ourPlayer, Direction.Right)) {
					this.move(Direction.Right);
				} else if(m_ourPlayer.getDirection() != Direction.Right) {
					this.move(Direction.Right);
				}
			}
		}
	}*/

	/**
	 * Accepts the mouse input
	 */
	@Override
	public void mousePressed(int button, int x, int y) {
		// Right Click
		if (button == 1) {
			// loop through the players and look for one that's in the
			// place where the user just right-clicked
			for (Player p : m_mapMatrix.getPlayers()) {
				if ((x >= p.getX() + m_mapMatrix.getCurrentMap().getXOffset() && x <= p.getX() + 32 + m_mapMatrix.getCurrentMap().getXOffset()) 
						&& (y >= p.getY() + m_mapMatrix.getCurrentMap().getYOffset() && y <= p.getY() + 40 + m_mapMatrix.getCurrentMap().getYOffset())) {
					// Brings up a popup menu with player options
					if (!p.isOurPlayer() && !p.getUsername().equalsIgnoreCase("!NPC!")){
						if (getDisplay().containsChild(m_playerDialog))
							getDisplay().remove(m_playerDialog);
						m_playerDialog = new PlayerPopupDialog(p.getUsername());
						m_playerDialog.setLocation(x, y);
						getDisplay().add(m_playerDialog);
					}
				}
			}
		}
		//Left click
		if (button == 0){
			//Get rid of the popup if you click outside of it
			if (getDisplay().containsChild(m_playerDialog)){
				if (x > m_playerDialog.getAbsoluteX() || x < m_playerDialog.getAbsoluteX()
						+ m_playerDialog.getWidth()){
					m_playerDialog.destroy();
				} else if (y > m_playerDialog.getAbsoluteY() || y < m_playerDialog.getAbsoluteY() 
						+ m_playerDialog.getHeight()){
					m_playerDialog.destroy();
				}
			} 
			//repeats space bar items (space bar emulation for mouse. In case you done have a space bar!)
			try
			{
				if(getDisplay().containsChild(m_ui.getChat())){
					m_ui.getChat().dropFocus();
				}
				if(m_ui.getNPCSpeech() == null && !getDisplay().containsChild(BattleManager.getInstance()
						.getBattleWindow()) ){
					ClientMessage message = new ClientMessage();
					message.Init(47);
					session.Send(message);
				}
				if (BattleManager.isBattling() && 
						getDisplay().containsChild(BattleManager.getInstance().getTimeLine().getBattleSpeech())
						&& !getDisplay().containsChild(MoveLearningManager.getInstance().getMoveLearning())) {
					BattleManager.getInstance().getTimeLine().getBattleSpeech().advance();
				} else{
					try {
						m_ui.getNPCSpeech().advance();
					} catch (Exception e) { 
						m_ui.nullSpeechFrame();
						//					m_packetGen.write("F"); 
					}
				}
			} catch (Exception e) {}
		}
	}

	
	/**
	 * Connects to a selected server
	 */
	public void connect() {
		Socket socket = null;
		_connection = new Connection("127.0.0.1", 7001);
		
		try {
			// dirty hack! :) check if server is alive!!!
			socket = new Socket("127.0.0.1", 7001);
			socket.close();
			
			if (_connection.Connect()) {
				System.out.println("Client connected to port 7001");
				m_userManager = new UserManager();
				m_login.showLogin();
			} else {
				System.out.println("oops.. something went WRONGUH!");
			}
		} catch(Exception e) {
			GameClient.messageDialog("The server is offline, please check back later.", GameClient.getInstance().getDisplay());
		}
	}

	/**
	 * Returns the map matrix
	 * @return
	 */
	public ClientMapMatrix getMapMatrix() {
		return m_mapMatrix;
	}

	/**
	 * If you don't know what this does, you shouldn't be programming!
	 * @param args
	 */
	public static void main(String [] args) {
		boolean fullscreen = false;
		
		try {
			fullscreen = Boolean.parseBoolean(options.get("fullScreen"));
		} catch (Exception e) {
			fullscreen = false;
		}
		try {
			gc = new AppGameContainer(new GameClient("Pokénet: Squishy Squirtle"),
					800, 600, fullscreen);
			
			if (!fullscreen) {
				gc.setAlwaysRender(true);
			}
			
			gc.start();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * When the close button is pressed... 
	 * @param args
	 */
	public boolean closeRequested(){
		if (m_confirm == null){
			ActionListener yes = new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					try {
						System.exit(0);
						m_close = true;
					} catch (Exception e) {
						e.printStackTrace();
						m_close = true;
					}
				}
			};
			ActionListener no = new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					m_confirm.setVisible(false);
					getDisplay().remove(m_confirm);
					m_confirm = null;
					m_close = false;
				}
			};
			m_confirm = new ConfirmationDialog("Are you sure you want to exit?", yes, no);
			getUi().getDisplay().add(m_confirm);
		}		
		return m_close;
	}

	/**
	 * Returns the font in large
	 * @return
	 */
	public static Font getFontLarge() {
		return m_fontLarge;
	}

	/**
	 * Returns the font in small
	 * @return
	 */
	public static Font getFontSmall() {
		return m_fontSmall;
	}

	public static Font getTrueTypeFont() {
		return m_trueTypeFont;
	}

	/**
	 * Sets the server host. The server will connect once m_host is not equal to ""
	 * @param s
	 */
	public static void setHost(String s) {
		HOST = s;
	}

	/**
	 * Returns this instance of game client
	 * @return
	 */
	public static GameClient getInstance() {
		return m_instance;
	}

	/**
	 * Returns the login screen
	 * @return
	 */
	public LoginScreen getLoginScreen() {
		return m_login;
	}

	/**
	 * Returns the loading screen
	 * @return
	 */
	public LoadingScreen getLoadingScreen() {
		return m_loading;
	}

	/**
	 * Returns the weather service
	 * @return
	 */
	public WeatherService getWeatherService() {
		return m_weather;
	}

	/**
	 * Returns the time service
	 * @return
	 */
	public TimeService getTimeService() {
		return m_time;
	}

	/**
	 * Stores the player's id
	 * @param id
	 */
	public void setPlayerId(int id) {
		m_playerId = id;
	}

	/**
	 * Returns this player's id
	 * @return
	 */
	public int getPlayerId() {
		return m_playerId;
	}


	/**
	 * Resets the client back to the z
	 */
	public void reset() {
		HOST = "";
		try {
			if(BattleManager.getInstance() != null)
				BattleManager.getInstance().endBattle();
			if(this.getUi().getNPCSpeech() != null)
				this.getUi().getNPCSpeech().setVisible(false);
			if(this.getUi().getChat() != null)
				this.getUi().getChat().setVisible(false);
			this.getUi().setVisible(false);
		} catch (Exception e) {}
		m_login.setVisible(true);
		m_login.showLanguageSelect();
	}

	/**
	 * Sets the map and loads them on next update() call
	 * @param x
	 * @param y
	 */
	public void setMap(int x, int y) {
		m_mapX = x;
		m_mapY = y;
		m_isNewMap = true;
		m_loading.setVisible(true);
		m_ui.getReqWindow().clearOffers();
		m_soundPlayer.setTrackByLocation(m_mapMatrix.getMapName(x, y));
	}

	/**
	 * Returns our player
	 * @return
	 */
	public OurPlayer getOurPlayer() {
		return m_ourPlayer;
	}

	/**
	 * Sets our player
	 * @param pl
	 */
	public void setOurPlayer(OurPlayer pl) {
		m_ourPlayer = pl;
	}

	/**
	 * Returns the user interface
	 */
	public Ui getUi() {
		return m_ui;
	}

	/**
	 * Returns the File Path, if any
	 */
	public static String getFilePath() {
		return m_filepath;
	}

	/**
	 * Returns the options
	 */
	public static HashMap<String, String> getOptions() {
		return options;
	}

	/**
	 * Reloads options
	 */
	public static void reloadOptions() {
		try {
			options = new FileMuffin().loadFile("options.dat");
			if (options == null) options = new HashMap<String,String>();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(32);
		}
	}

	/**
	 * Returns the sound player
	 * @return
	 */
	public static SoundManager getSoundPlayer() {
		return m_soundPlayer;
	}


	/**
	 * Creates a message Box
	 */
	public static void messageDialog(String message, Container container) {
		new MessageDialog(message.replace('~','\n'), container);
	}

	/**
	 * Returns the display
	 */
	public Display getDisplay(){
		return m_display;
	}

	
	public static Connection getConnections() {
		return _connection;
	}
	
	public UserManager getUserManager() {
		return m_userManager;
	}
	
	/**
	 * Returns the language selection
	 * @return
	 */
	public static String getLanguage() {
		return m_language;
	}
	/**
	 * Sets the language selection
	 * @return
	 */
	public static String setLanguage(String lang) {
		m_language = lang;
		m_languageChosen=true;
		return m_language;
	}

	/**
	 * Changes the playing track
	 * @param fileKey
	 */
	public static void changeTrack(String fileKey){
		m_soundPlayer.setTrack(fileKey);
	}

	/**
	 * Returns false if the user has disabled surrounding map loading
	 * @return
	 */
	public static boolean disableMaps() {
		return m_disableMaps;
	}

	/**
	 * Sets if the client should load surrounding maps
	 * @param b
	 */
	public static void setDisableMaps(boolean b) {
		m_disableMaps = b;
	}
}
