package org.pokenet.client.ui;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import mdes.slick.sui.Button;
import mdes.slick.sui.Container;
import mdes.slick.sui.Frame;
import mdes.slick.sui.Label;
import mdes.slick.sui.event.ActionEvent;
import mdes.slick.sui.event.ActionListener;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.loading.LoadingList;
import org.pokenet.client.GameClient;
import org.pokenet.client.backend.BattleManager;
import org.pokenet.client.backend.FileLoader;
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.ui.base.BattleButtonFactory;
import org.pokenet.client.ui.frames.BattleBag;

/**
 * Battle window interface
 * 
 * @author ZombieBear
 */
public class BattleWindow extends Frame
{
	// Image Loading tools
	private static String m_path = "res/battle/";

	private static HashMap<String, Image> m_statusIcons = new HashMap<String, Image>();
	static final long serialVersionUID = -4351471892179339349L;
	public BattleBag m_bag;
	public List<Button> m_moveButtons = new ArrayList<Button>();
	public List<Label> m_moveTypeLabels = new ArrayList<Label>();
	public List<Button> m_pokeButtons = new ArrayList<Button>();
	public List<Label> m_pokeInfo = new ArrayList<Label>();
	public List<Label> m_pokeStatus = new ArrayList<Label>();
	public List<Label> m_ppLabels = new ArrayList<Label>();
	private Container attackPane;
	private Button btnBag;
	private Button btnPoke;
	private Button btnRun;
	private Button cancel;
	private Button close;
	private Container endPane;
	private boolean isWild;
	private Button pokeCancelBtn;

	private Container pokesContainer;

	/**
	 * Default constructor
	 * 
	 * @param title
	 * @param wild
	 */
	public BattleWindow(String title)
	{
		String respath = System.getProperty("res.path");
		if(respath == null)
			respath = "";
		m_path = respath + m_path;
		getContentPane().setX(getContentPane().getX() - 1);
		getContentPane().setY(getContentPane().getY() + 1);
		setTitle(title);
		loadStatusIcons();
		initComponents();
		setCenter();
		setSize(259, 369);
	}

	/**
	 * Disables moves
	 */
	public void disableMoves()
	{
		attackPane.setVisible(false);
		for(int idx = 0; idx < 4; idx++)
		{
			m_moveButtons.get(idx).setEnabled(false);
			m_ppLabels.get(idx).setEnabled(false);
		}
		btnPoke.setEnabled(false);
		btnBag.setEnabled(false);
		btnRun.setEnabled(false);
		cancel.setVisible(false);
	}

	/**
	 * Enables moves
	 */
	public void enableMoves()
	{
		attackPane.setVisible(true);
		btnPoke.setEnabled(true);
		btnBag.setEnabled(true);
		btnRun.setEnabled(isWild);

		pokeCancelBtn.setEnabled(true);
		for(int idx = 0; idx < 4; idx++)
			if(!m_moveButtons.get(idx).getText().equals(""))
			{
				m_moveButtons.get(idx).setEnabled(true);
				m_ppLabels.get(idx).setEnabled(true);
			}
		cancel.setVisible(false);
	}

	public HashMap<String, Image> getStatusIcons()
	{
		return m_statusIcons;
	}

	/**
	 * Loads the status icons
	 */
	public void loadStatusIcons()
	{
		LoadingList.setDeferredLoading(true);
		try
		{
			m_statusIcons.put("Poison", new Image(m_path + "PSN.png", false));
			m_statusIcons.put("Sleep", new Image(m_path + "SLP.png", false));
			m_statusIcons.put("Freze", new Image(m_path + "FRZ.png", false));
			m_statusIcons.put("Burn", new Image(m_path + "BRN.png", false));
			m_statusIcons.put("Paralysis", new Image(m_path + "PAR.png", false));
		}
		catch(SlickException se)
		{
			se.printStackTrace();
		}
		LoadingList.setDeferredLoading(false);
	}

	/**
	 * Centers the battle window
	 */
	public void setCenter()
	{
		int height = (int) GameClient.getInstance().getDisplay().getHeight();
		int width = (int) GameClient.getInstance().getDisplay().getWidth();
		int x = width / 2 - 130;
		int y = height / 2 - 238;
		setBounds(x, y, 259, 475);
	}

	/**
	 * Sets whether the battle is a wild pokemon
	 * 
	 * @param isWild
	 */
	public void setWild(boolean isWild)
	{
		this.isWild = isWild;
		btnRun.setEnabled(isWild);
	}

	/**
	 * Shows the attack Pane
	 */
	public void showAttack()
	{
		pokesContainer.setVisible(false);
		// bagPane.setVisible(false);
		attackPane.setVisible(true);
		endPane.setVisible(false);
	}

	/**
	 * Shows the Bag Pane
	 */
	public void showBag()
	{
		attackPane.setVisible(false);
		endPane.setVisible(false);
		pokesContainer.setVisible(false);
		m_bag = new BattleBag();
		m_bag.setAlwaysOnTop(true);
		m_bag.setZIndex(10000000); // Ridiculous z-index ensures that the bag is on top.
		GameClient.getInstance().getDisplay().add(m_bag);
	}

	/**
	 * Shows the pokemon Pane
	 */
	public void showPokePane(boolean isForced)
	{
		BattleManager.getInstance().updatePokePane();
		attackPane.setVisible(false);
		// bagPane.setVisible(false);
		pokesContainer.setVisible(true);
		endPane.setVisible(false);
		if(isForced)
			pokeCancelBtn.setEnabled(false);
		else
			pokeCancelBtn.setEnabled(true);
	}

	/**
	 * Sends a move packet
	 * 
	 * @param i
	 */
	public void useMove(int i)
	{
		disableMoves();
		if(BattleManager.getInstance().getCurPoke().getMoveCurPP()[i] != 0)
		{
			BattleManager.getInstance().getCurPoke().setMoveCurPP(i, BattleManager.getInstance().getCurPoke().getMoveCurPP()[i] - 1);
			BattleManager.getInstance().updateMoves();
		}
		// GameClient.getInstance().getPacketGenerator().writeTcpMessage("2C" + i);
		ClientMessage message = new ClientMessage();
		message.Init(35);
		message.addInt(i);
		GameClient.m_Session.Send(message);
		// BattleManager.getInstance().getTimeLine().getBattleSpeech().advance();
	}

	/**
	 * Initializes the interface
	 */
	private void initComponents()
	{
		LoadingList.setDeferredLoading(true);
		setBackground(new Color(0, 0, 0, 0));
		String respath = System.getProperty("res.path");
		if(respath == null)
			respath = "";

		Label m_bg = new Label();
		try
		{
			InputStream input = FileLoader.loadFile(respath + "res/ui/bg.png");
			m_bg = new Label(new Image(input, respath + "res/ui/bg.png", false));
		}
		catch(SlickException se)
		{
			se.printStackTrace();
		}

		m_bg.setZIndex(1);
		m_bg.setSize(256, 203);
		m_bg.setLocation(0, 142);
		getContentPane().add(m_bg);

		attackPane = new Container();
		attackPane.setZIndex(2);
		attackPane.setBackground(new Color(0, 0, 0, 0));
		Button move1 = BattleButtonFactory.getButton("");
		Button move2 = BattleButtonFactory.getButton("");
		Button move3 = BattleButtonFactory.getButton("");
		Button move4 = BattleButtonFactory.getButton("");
		setResizable(false);
		getTitleBar().setVisible(false);

		// start attackPane
		attackPane.add(move1);
		move1.setLocation(7, 10);
		move1.setSize(116, 51);
		move1.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent evt)
			{
				useMove(0);
			}
		});
		Label pp1 = new Label();
		pp1.setHorizontalAlignment(Label.RIGHT_ALIGNMENT);
		pp1.setBounds(0, move1.getHeight() - 20, move1.getWidth() - 5, 20);
		Label move1Type = new Label();
		move1Type.setHorizontalAlignment(Label.LEFT_ALIGNMENT);
		move1Type.setBounds(2, move1.getHeight() - 20, move1.getWidth() - 5, 20);
		move1.add(move1Type);
		move1.add(pp1);
		attackPane.add(move2);
		move2.setLocation(130, 10);
		move2.setSize(116, 51);
		move2.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent evt)
			{
				useMove(1);
			}
		});
		Label pp2 = new Label();
		pp2.setHorizontalAlignment(Label.RIGHT_ALIGNMENT);
		pp2.setBounds(0, move2.getHeight() - 20, move2.getWidth() - 5, 20);
		Label move2Type = new Label();
		move2Type.setHorizontalAlignment(Label.LEFT_ALIGNMENT);
		move2Type.setBounds(2, move2.getHeight() - 20, move2.getWidth() - 5, 20);
		move2.add(move2Type);
		move2.add(pp2);
		attackPane.add(move3);
		move3.setLocation(7, 65);
		move3.setSize(116, 51);
		move3.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent evt)
			{
				useMove(2);
			}
		});
		Label pp3 = new Label();
		pp3.setHorizontalAlignment(Label.RIGHT_ALIGNMENT);
		pp3.setBounds(0, move3.getHeight() - 20, move3.getWidth() - 5, 20);
		Label move3Type = new Label();
		move3Type.setHorizontalAlignment(Label.LEFT_ALIGNMENT);
		move3Type.setBounds(2, move3.getHeight() - 20, move3.getWidth() - 5, 20);
		move3.add(move3Type);
		move3.add(pp3);

		attackPane.add(move4);
		move4.setLocation(130, 65);
		move4.setSize(116, 51);
		move4.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent evt)
			{
				useMove(3);
			}
		});
		Label pp4 = new Label();
		pp4.setHorizontalAlignment(Label.RIGHT_ALIGNMENT);
		pp4.setBounds(0, move4.getHeight() - 20, move4.getWidth() - 5, 20);
		Label move4Type = new Label();
		move4Type.setHorizontalAlignment(Label.LEFT_ALIGNMENT);
		move4Type.setBounds(2, move4.getHeight() - 20, move4.getWidth() - 5, 20);
		move4.add(move4Type);
		move4.add(pp4);

		pp1.setFont(GameClient.getFontSmall());
		pp2.setFont(GameClient.getFontSmall());
		pp3.setFont(GameClient.getFontSmall());
		pp4.setFont(GameClient.getFontSmall());
		pp1.setForeground(Color.white);
		pp2.setForeground(Color.white);
		pp3.setForeground(Color.white);
		pp4.setForeground(Color.white);
		move1Type.setFont(GameClient.getFontSmall());
		move2Type.setFont(GameClient.getFontSmall());
		move3Type.setFont(GameClient.getFontSmall());
		move4Type.setFont(GameClient.getFontSmall());
		move1Type.setForeground(Color.white);
		move2Type.setForeground(Color.white);
		move3Type.setForeground(Color.white);
		move4Type.setForeground(Color.white);
		m_moveButtons.add(move1);
		m_moveButtons.add(move2);
		m_moveButtons.add(move3);
		m_moveButtons.add(move4);
		m_ppLabels.add(pp1);
		m_ppLabels.add(pp2);
		m_ppLabels.add(pp3);
		m_ppLabels.add(pp4);
		m_moveTypeLabels.add(move1Type);
		m_moveTypeLabels.add(move2Type);
		m_moveTypeLabels.add(move3Type);
		m_moveTypeLabels.add(move4Type);

		btnRun = BattleButtonFactory.getSmallButton("Run");
		btnRun.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent evt)
			{
				run();
			}
		});
		attackPane.add(btnRun);
		btnRun.setBounds(97, 148, 60, 47);
		btnBag = BattleButtonFactory.getSmallButton("Bag");
		attackPane.add(btnBag);
		btnBag.setLocation(3, 122);
		btnBag.setSize(82, 48);
		btnBag.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent evt)
			{
				showBag();
			}
		});

		btnPoke = BattleButtonFactory.getSmallButton("Pokemon");
		attackPane.add(btnPoke);
		btnPoke.setLocation(168, 122);
		btnPoke.setSize(82, 48);
		btnPoke.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent evt)
			{
				showPokePane(false);
			}
		});
		cancel = BattleButtonFactory.getSmallButton("Cancel");
		attackPane.add(cancel);
		cancel.setVisible(false);
		cancel.setLocation(162, 110);
		cancel.setSize(82, 48);
		cancel.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent evt)
			{

			}
		});
		attackPane.setBounds(2, 140, 257, 201);
		getContentPane().add(attackPane);

		pokesContainer = new Container();
		pokesContainer.setZIndex(2);
		pokesContainer.setBackground(new Color(0, 0, 0, 0));
		pokesContainer.setBounds(2, 140, 257, 201);
		Button pokeBtn1 = BattleButtonFactory.getButton(" ");
		pokesContainer.add(pokeBtn1);
		pokeBtn1.setBounds(8, 8, 116, 51);
		pokeBtn1.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent evt)
			{
				switchPoke(0);
			}
		});
		Button pokeBtn2 = BattleButtonFactory.getButton(" ");
		pokesContainer.add(pokeBtn2);
		pokeBtn2.setBounds(128, 8, 116, 51);
		pokeBtn2.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent evt)
			{
				switchPoke(1);
			}
		});
		Button pokeBtn3 = BattleButtonFactory.getButton(" ");
		pokesContainer.add(pokeBtn3);
		pokeBtn3.setBounds(8, 59, 116, 51);
		pokeBtn3.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent evt)
			{
				switchPoke(2);
			}
		});
		Button pokeBtn4 = BattleButtonFactory.getButton(" ");
		pokesContainer.add(pokeBtn4);
		pokeBtn4.setBounds(128, 59, 116, 51);
		pokeBtn4.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent evt)
			{
				switchPoke(3);
			}
		});
		Button pokeBtn5 = BattleButtonFactory.getButton(" ");
		pokesContainer.add(pokeBtn5);
		pokeBtn5.setBounds(8, 110, 116, 51);
		pokeBtn5.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent evt)
			{
				switchPoke(4);
			}
		});
		Button pokeBtn6 = BattleButtonFactory.getButton(" ");
		pokesContainer.add(pokeBtn6);
		pokeBtn6.setBounds(128, 110, 116, 51);
		pokeBtn6.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent evt)
			{
				switchPoke(5);
			}
		});
		m_pokeButtons.add(pokeBtn1);
		m_pokeButtons.add(pokeBtn2);
		m_pokeButtons.add(pokeBtn3);
		m_pokeButtons.add(pokeBtn4);
		m_pokeButtons.add(pokeBtn5);
		m_pokeButtons.add(pokeBtn6);
		for(int i = 0; i < 6; i++)
		{
			Label status = new Label();
			status.setSize(30, 12);
			status.setGlassPane(true);
			m_pokeButtons.get(i).add(status);
			status.setLocation(6, 5);
			Label info = new Label();
			m_pokeButtons.get(i).add(info);
			info.setText("                               ");
			info.setLocation(3, 34);
			info.setSize(107, 14);
			info.setForeground(Color.white);
			info.setGlassPane(true);
			info.setFont(GameClient.getFontSmall());
			m_pokeInfo.add(info);
		}
		pokeCancelBtn = BattleButtonFactory.getSmallButton("Cancel");
		pokesContainer.add(pokeCancelBtn);
		pokeCancelBtn.setLocation(162, 161);
		pokeCancelBtn.setSize(82, 48);
		pokeCancelBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent evt)
			{
				showAttack();
			}
		});
		pokesContainer.setVisible(false);
		getContentPane().add(pokesContainer);
		// End pokesContainer

		endPane = new Container();
		endPane.setZIndex(2);
		endPane.setBackground(new Color(0, 0, 0, 0));
		getContentPane().add(endPane);
		endPane.setBounds(0, 154, 253, 192);
		close = new Button();
		close.setVisible(true);
		endPane.add(close);
		close.setText("Close");
		close.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent evt)
			{
				setVisible(false);
			}
		});
		endPane.setVisible(false);
		LoadingList.setDeferredLoading(false);
	}

	/**
	 * Sends the run packer
	 */
	private void run()
	{
		// GameClient.getInstance().getPacketGenerator().writeTcpMessage("2E");
		ClientMessage message = new ClientMessage();
		message.Init(37);
		GameClient.m_Session.Send(message);
	}

	/**
	 * Sends the pokemon switch packet
	 * 
	 * @param i
	 */
	private void switchPoke(int i)
	{
		attackPane.setVisible(false);
		pokesContainer.setVisible(false);
		// GameClient.getInstance().getPacketGenerator().writeTcpMessage("2D" + i);
		ClientMessage message = new ClientMessage();
		message.Init(36);
		message.addInt(i);
		GameClient.m_Session.Send(message);
	}
}
