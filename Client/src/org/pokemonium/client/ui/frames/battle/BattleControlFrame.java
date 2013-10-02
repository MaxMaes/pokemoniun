package org.pokemonium.client.ui.frames.battle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.pokemonium.client.GameClient;
import org.pokemonium.client.backend.BattleManager;
import org.pokemonium.client.constants.ServerPacket;
import org.pokemonium.client.protocol.ClientMessage;
import org.pokemonium.client.ui.components.Image;
import de.matthiasmann.twl.Alignment;
import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.Widget;

/**
 * Battle window interface
 * 
 * @author Myth1c
 */
public class BattleControlFrame extends Widget
{
	// Image Loading tools
	private static String m_path = "res/battle/";

	private static HashMap<String, de.matthiasmann.twl.renderer.Image> m_statusIcons = new HashMap<String, de.matthiasmann.twl.renderer.Image>();
	private List<Button> m_moveButtons = new ArrayList<Button>();
	private List<Label> m_moveTypeLabels = new ArrayList<Label>();
	private List<Button> m_pokeButtons = new ArrayList<Button>();
	private List<Label> m_pokeInfo = new ArrayList<Label>();
	private List<Image> m_pokeStatus = new ArrayList<Image>();
	private List<Label> m_ppLabels = new ArrayList<Label>();
	private Button btnBag;
	private Button btnPoke;
	private Button btnRun;
	private boolean isWild;
	private Button pokeCancelBtn;
	private Widget attackPane;
	private Widget pokePane;

	/**
	 * Default constructor
	 * 
	 * @param title
	 * @param wild
	 */
	public BattleControlFrame()
	{
		String respath = System.getProperty("res.path");
		if(respath == null)
			respath = "";
		m_path = respath + m_path;
		loadStatusIcons();
		initComponents();
		setVisible(true);
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
			m_moveTypeLabels.get(idx).setEnabled(false);
		}
		btnPoke.setEnabled(false);
		btnBag.setEnabled(false);
		btnRun.setEnabled(false);
	}

	/**
	 * Enables moves
	 */
	public void enableMoves()
	{
		GameClient.getInstance().getGUI().invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				attackPane.setVisible(true);
				btnPoke.setEnabled(true);
				btnBag.setEnabled(true);
				btnRun.setEnabled(isWild);

				pokeCancelBtn.setEnabled(true);
				for(int idx = 0; idx < 4; idx++)
				{
					if(!m_moveButtons.get(idx).getText().equals(""))
					{
						m_moveButtons.get(idx).setEnabled(true);
						m_ppLabels.get(idx).setEnabled(true);
					}
					else
					{
						m_moveButtons.get(idx).setEnabled(false);
						m_ppLabels.get(idx).setEnabled(false);
					}
				}
			}
		});
	}

	public HashMap<String, de.matthiasmann.twl.renderer.Image> getStatusIcons()
	{
		return m_statusIcons;
	}

	/**
	 * Loads the status icons
	 */
	public void loadStatusIcons()
	{
		m_statusIcons.put("poison", GameClient.getInstance().getTheme().getImage("status_psn"));
		m_statusIcons.put("sleep", GameClient.getInstance().getTheme().getImage("status_slp"));
		m_statusIcons.put("freze", GameClient.getInstance().getTheme().getImage("status_frz"));
		m_statusIcons.put("burn", GameClient.getInstance().getTheme().getImage("status_brn"));
		m_statusIcons.put("paralysis", GameClient.getInstance().getTheme().getImage("status_par"));
	}

	/**
	 * Centers the battle window
	 */
	public void setCenter()
	{
		int height = (int) GameClient.getInstance().getGUI().getHeight();
		int width = (int) GameClient.getInstance().getGUI().getWidth();
		int x = width / 2 - 128;
		int y = height / 2 - 102;
		setSize(255, 203);
		setPosition(x, y);
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
		pokePane.setVisible(false);
		attackPane.setVisible(true);
	}

	/**
	 * Shows the Bag Pane
	 */
	public void showBag()
	{
		attackPane.setVisible(false);
		pokePane.setVisible(false);
		GameClient.getInstance().getHUD().showBattlebag();
	}

	/**
	 * Shows the pokemon Pane
	 */
	public void showPokePane(boolean isForced)
	{
		BattleManager.getInstance().updatePokePane();
		attackPane.setVisible(false);
		// bagPane.setVisible(false);
		pokePane.setVisible(true);
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
		ClientMessage message = new ClientMessage(ServerPacket.MOVE_SELECTED);
		message.addInt(i);
		GameClient.getInstance().getSession().send(message);
		GameClient.getInstance().getHUD().getBattleSpeechFrame().advance();
	}

	/**
	 * Initializes the interface
	 */
	private void initComponents()
	{
		attackPane = new Widget();
		attackPane.setTheme("attackPane");
		Button move1 = new Button("");
		move1.setTheme("battlebutton");
		move1.setCanAcceptKeyboardFocus(false);
		Button move2 = new Button("");
		move2.setTheme("battlebutton");
		move2.setCanAcceptKeyboardFocus(false);
		Button move3 = new Button("");
		move3.setTheme("battlebutton");
		move3.setCanAcceptKeyboardFocus(false);
		Button move4 = new Button("");
		move4.setTheme("battlebutton");
		move4.setCanAcceptKeyboardFocus(false);

		// start attackPane
		attackPane.add(move1);
		move1.setPosition(7, 10);
		move1.setSize(116, 51);
		move1.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				useMove(0);
			}
		});
		Label pp1 = new Label();
		pp1.setTheme("pplabel");
		pp1.setSize(GameClient.getInstance().getFontSmall().getWidth(pp1.getText()), GameClient.getInstance().getFontSmall().getHeight(pp1.getText()));
		pp1.setPosition(85, 40);
		Label move1Type = new Label();
		move1Type.setTheme("movetypelabel");
		move1Type.setSize(GameClient.getInstance().getFontSmall().getWidth(move1Type.getText()), GameClient.getInstance().getFontSmall().getHeight(move1Type.getText()));
		move1Type.setPosition(12, 40);
		attackPane.add(move1Type);
		attackPane.add(pp1);

		attackPane.add(move2);
		move2.setPosition(130, 10);
		move2.setSize(116, 51);
		move2.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				useMove(1);
			}
		});
		Label pp2 = new Label();
		pp2.setTheme("pplabel");
		pp2.setSize(GameClient.getInstance().getFontSmall().getWidth(pp2.getText()), GameClient.getInstance().getFontSmall().getHeight(pp2.getText()));
		pp2.setPosition(210, 40);
		Label move2Type = new Label();
		move2Type.setTheme("movetypelabel");
		move2Type.setSize(GameClient.getInstance().getFontSmall().getWidth(move2Type.getText()), GameClient.getInstance().getFontSmall().getHeight(move2Type.getText()));
		move2Type.setPosition(135, 40);
		attackPane.add(move2Type);
		attackPane.add(pp2);
		attackPane.add(move3);

		move3.setPosition(7, 65);
		move3.setSize(116, 51);
		move3.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				useMove(2);
			}
		});
		Label pp3 = new Label();
		pp3.setTheme("pplabel");
		pp3.setSize(GameClient.getInstance().getFontSmall().getWidth(pp3.getText()), GameClient.getInstance().getFontSmall().getHeight(pp3.getText()));
		pp3.setPosition(86, 95);
		Label move3Type = new Label();
		move3Type.setTheme("movetypelabel");
		move3Type.setAlignment(Alignment.LEFT);
		move3Type.setSize(GameClient.getInstance().getFontSmall().getWidth(move3Type.getText()), GameClient.getInstance().getFontSmall().getHeight(move3Type.getText()));
		move3Type.setPosition(12, 95);
		attackPane.add(move3Type);
		attackPane.add(pp3);
		attackPane.add(move4);
		move4.setPosition(130, 65);
		move4.setSize(116, 51);
		move4.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				useMove(3);
			}
		});
		Label pp4 = new Label();
		pp4.setTheme("pplabel");
		pp4.setSize(GameClient.getInstance().getFontSmall().getWidth(pp4.getText()), GameClient.getInstance().getFontSmall().getHeight(pp4.getText()));
		pp4.setPosition(210, 95);
		Label move4Type = new Label();
		move4Type.setTheme("movetypelabel");
		move4Type.setSize(GameClient.getInstance().getFontSmall().getWidth(move4Type.getText()), GameClient.getInstance().getFontSmall().getHeight(move4Type.getText()));
		move4Type.setPosition(135, 95);
		attackPane.add(move4Type);
		attackPane.add(pp4);

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

		btnRun = new Button("Run");
		btnRun.setTheme("battlebutton_small");
		btnRun.setCanAcceptKeyboardFocus(false);
		btnRun.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				flee();
			}
		});
		attackPane.add(btnRun);
		btnRun.setSize(60, 47);
		btnRun.setPosition(97, 148);
		btnBag = new Button("Bag");
		btnBag.setTheme("battlebutton_small");
		btnBag.setCanAcceptKeyboardFocus(false);
		attackPane.add(btnBag);
		btnBag.setPosition(3, 122);
		btnBag.setSize(82, 48);
		btnBag.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				showBag();
			}
		});

		btnPoke = new Button("Pokemon");
		btnPoke.setTheme("battlebutton_small");
		btnPoke.setCanAcceptKeyboardFocus(false);
		attackPane.add(btnPoke);
		btnPoke.setPosition(168, 122);
		btnPoke.setSize(82, 48);
		btnPoke.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				showPokePane(false);
			}
		});

		attackPane.setSize(257, 201);
		add(attackPane);

		pokePane = new Widget();
		pokePane.setTheme("attackPane");
		pokePane.setSize(257, 201);
		// pokePane.setPosition(0, -100);
		Button pokeBtn1 = new Button(" ");
		pokeBtn1.setTheme("battlebutton");
		pokePane.add(pokeBtn1);
		pokeBtn1.setSize(116, 51);
		pokeBtn1.setPosition(8, 8);
		pokeBtn1.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				switchPoke(0);
			}
		});
		Button pokeBtn2 = new Button(" ");
		pokeBtn2.setTheme("battlebutton");
		pokePane.add(pokeBtn2);
		pokeBtn2.setSize(116, 51);
		pokeBtn2.setPosition(128, 8);
		pokeBtn2.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				switchPoke(1);
			}
		});
		Button pokeBtn3 = new Button(" ");
		pokeBtn3.setTheme("battlebutton");
		pokePane.add(pokeBtn3);
		pokeBtn3.setSize(116, 51);
		pokeBtn3.setPosition(8, 59);
		pokeBtn3.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				switchPoke(2);
			}
		});
		Button pokeBtn4 = new Button(" ");
		pokeBtn4.setTheme("battlebutton");
		pokePane.add(pokeBtn4);
		pokeBtn4.setSize(116, 51);
		pokeBtn4.setPosition(128, 59);
		pokeBtn4.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				switchPoke(3);
			}
		});
		Button pokeBtn5 = new Button(" ");
		pokeBtn5.setTheme("battlebutton");
		pokePane.add(pokeBtn5);
		pokeBtn5.setSize(116, 51);
		pokeBtn5.setPosition(8, 110);
		pokeBtn5.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				switchPoke(4);
			}
		});
		Button pokeBtn6 = new Button(" ");
		pokeBtn6.setTheme("battlebutton");
		pokePane.add(pokeBtn6);
		pokeBtn6.setSize(116, 51);
		pokeBtn6.setPosition(128, 110);
		pokeBtn6.addCallback(new Runnable()
		{
			@Override
			public void run()
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
			Image status = new Image();
			status.setSize(30, 12);
			m_pokeButtons.get(i).add(status);
			status.setPosition(m_pokeButtons.get(i).getX() + 6, m_pokeButtons.get(i).getY() + 5);
			m_pokeStatus.add(status);
			Label info = new Label();
			m_pokeButtons.get(i).add(info);
			info.setTheme("pokeinfolabel");
			info.setText("                               ");
			info.setPosition(m_pokeButtons.get(i).getX() + 3, m_pokeButtons.get(i).getY() + 34);
			info.setSize(107, 14);
			m_pokeInfo.add(info);
		}
		pokeCancelBtn = new Button("Cancel");
		pokeCancelBtn.setTheme("battlebutton_small");
		pokePane.add(pokeCancelBtn);
		pokeCancelBtn.setPosition(162, 161);
		pokeCancelBtn.setSize(82, 35);
		pokeCancelBtn.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				showAttack();
			}
		});
		pokePane.setVisible(false);
		add(pokePane);
		// End pokePane
	}

	/**
	 * Sends the run packet
	 */
	private void flee()
	{
		ClientMessage message = new ClientMessage(ServerPacket.BATTLE_RUN);
		GameClient.getInstance().getSession().send(message);
	}

	/**
	 * Sends the pokemon switch packet
	 * 
	 * @param i
	 */
	private void switchPoke(int i)
	{
		attackPane.setVisible(false);
		pokePane.setVisible(false);
		// GameClient.getInstance().getPacketGenerator().writeTcpMessage("2D" + i);
		ClientMessage message = new ClientMessage(ServerPacket.SWITCH_POKEMON);
		message.addInt(i);
		GameClient.getInstance().getSession().send(message);
	}

	public List<Button> getMoveButtons()
	{
		return m_moveButtons;
	}

	public List<Label> getPPLabels()
	{
		return m_ppLabels;
	}

	public List<Label> getMoveTypeLabels()
	{
		return m_moveTypeLabels;
	}

	public List<Button> getPokeButtons()
	{
		return m_pokeButtons;
	}

	public List<Image> getPokeStatus()
	{
		return m_pokeStatus;
	}

	public List<Label> getPokeInfo()
	{
		return m_pokeInfo;
	}

	@Override
	public void layout()
	{
		for(int i = 0; i < 4; i++)
		{
			m_moveTypeLabels.get(i).adjustSize();
			m_ppLabels.get(i).adjustSize();
		}
	}
}
