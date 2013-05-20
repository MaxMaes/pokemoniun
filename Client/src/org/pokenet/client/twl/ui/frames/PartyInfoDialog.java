package org.pokenet.client.twl.ui.frames;

import java.util.List;
import org.pokenet.client.GameClient;
import org.pokenet.client.backend.Translator;
import org.pokenet.client.backend.entity.OurPokemon;
import org.pokenet.client.constants.ServerPacket;
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.twl.ui.base.Image;
import org.pokenet.client.twl.ui.base.ProgressBar;
import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.Widget;

/**
 * Party information frame
 * 
 * @author Myth1c
 */
public class PartyInfoDialog extends Widget
{
	private Widget[] m_container;
	private ProgressBar[] m_hp;
	private Image[] m_hpBar;
	private Label[] m_level;
	private Image[] m_pokeIcon;
	private Label[] m_pokeName;
	private OurPokemon[] m_pokes;
	private Button[] m_switchDown;

	private Button[] m_switchUp;

	/**
	 * Default constructor
	 * 
	 * @param ourPokes
	 * @param out
	 */
	public PartyInfoDialog(OurPokemon[] ourPokes)
	{
		m_pokes = ourPokes;
		allocateVariables();
		loadImages(ourPokes);
		/* ContentPane location is moved here instead of in initGUI so that if/when initGui is recalled the ConentPane doesn't move. */
		initGUI();
	}

	/** Initializes interface */
	public void initGUI()
	{
		int y = -8;
		int pokemonCount = 0;
		for(int i = 0; i < 6; i++)
		{
			if(m_pokes[i] != null)
			{
				pokemonCount++;
			}
		}
		for(int i = 0; i < pokemonCount; i++)
		{
			final int j = i;
			m_container[i] = new Widget();
			m_container[i].setTheme("partyslot");
			m_container[i].setSize(170, 42);
			m_container[i].setVisible(true);
			m_container[i].setPosition(2, y + 10);
			System.out.println("Y: " + y);
			add(m_container[i]);
			y += 41;
			String respath = System.getProperty("res.path");
			if(respath == null)
				respath = "";
			try
			{
				Image tempImage = new Image();
				if(i == 0)
				{
					tempImage = new Image(GameClient.getInstance().getTheme().getImage("party_active"));
				}
				else
				{
					tempImage = new Image(GameClient.getInstance().getTheme().getImage("party_inactive"));
				}
				tempImage.setSize(170, 42);
				tempImage.setPosition(0, -4);
				m_container[i].add(tempImage);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}

			try
			{
				m_hpBar[i] = new Image(GameClient.getInstance().getTheme().getImage("party_hpbar"));
				m_hpBar[i].setSize(98, 11);
				m_hpBar[i].setVisible(false);
				m_container[i].add(m_hpBar[i]);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			try
			{
				// m_Widget[i].add(m_pokeBall[i]);
				// m_pokeBall[i].setPosition(4, 4);
				/* TODO:
				 * m_pokeName[i].addMouseListener(new MouseAdapter()
				 * {
				 * @Override
				 * public void mouseEntered(MouseEvent e)
				 * {
				 * super.mouseEntered(e);
				 * m_pokeName[j].setForeground(new Color(255, 215, 0));
				 * }
				 * @Override
				 * public void mouseExited(MouseEvent e)
				 * {
				 * super.mouseExited(e);
				 * m_pokeName[j].setForeground(new Color(255, 255, 255));
				 * }
				 * @Override
				 * public void mouseReleased(MouseEvent e)
				 * {
				 * super.mouseReleased(e);
				 * PokemonInfoDialog info = new PokemonInfoDialog(m_pokes[j]);
				 * // info.setAlwaysOnTop(true); //TODO: Chappie magic :D
				 * getDisplay().add(info);
				 * }
				 * }); */
				m_container[i].add(m_pokeIcon[i]);
				m_pokeIcon[i].setPosition(2, 3);
				m_container[i].add(m_pokeName[i]);
				m_pokeName[i].setPosition(42, 5);
				m_container[i].add(m_level[i]);
				m_level[i].setPosition(m_pokeName[i].getX() + m_pokeName[i].getWidth() + 10, m_pokeName[i].getY());
				m_container[i].add(m_hp[i]);
				m_hp[i].setSize(72, 5);
				m_hp[i].setPosition(m_hpBar[i].getX() + 23, m_hpBar[i].getY() + 3);
				if(i != 0)
				{
					m_switchUp[i] = new Button();
					m_switchUp[i].setTheme("arrow_up");
					m_switchUp[i].addCallback(new Runnable()
					{
						@Override
						public void run()
						{
							ClientMessage message = new ClientMessage(ServerPacket.SWAP_PARTY);
							message.addInt(j);
							message.addInt(j - 1);
							GameClient.getInstance().getSession().send(message);
							// GameClient.getInstance().getPacketGenerator().writeTcpMessage("0D" + String.valueOf(j) + "," + String.valueOf(j - 1));
							// reinitialize the gui
							removeAllChildren();
							allocateVariables();
							loadImages(m_pokes);
							initGUI();
						}
					});
					m_switchUp[i].setSize(16, 16);
					m_container[i].add(m_switchUp[i]);
				}
				if(i != 5)
				{
					m_switchDown[i] = new Button();
					m_switchDown[i].setTheme("arrow_down");
					m_switchDown[i].addCallback(new Runnable()
					{
						@Override
						public void run()
						{
							ClientMessage message = new ClientMessage(ServerPacket.SWAP_PARTY);
							message.addInt(j);
							message.addInt(j + 1);
							GameClient.getInstance().getSession().send(message);
							// GameClient.getInstance().getPacketGenerator().writeTcpMessage("0D" + String.valueOf(j) + "," + String.valueOf(j + 1));
							// reinitialize the gui
							removeAllChildren();
							allocateVariables();
							loadImages(m_pokes);
							initGUI();
						}
					});
					m_switchDown[i].setSize(16, 16);
					m_switchDown[i].setPosition(24, 0);
					m_container[i].add(m_switchDown[i]);
				}
			}
			catch(NullPointerException npe)
			{
				npe.printStackTrace();
			}
		}
		update(m_pokes);
		this.setSize(170, 270);
	}

	/**
	 * Loads necessary images
	 * 
	 * @param pokes
	 */
	public void loadImages(OurPokemon[] pokes)
	{
		for(int i = 0; i < pokes.length; i++)
		{
			m_pokeName[i] = new Label();

			m_level[i] = new Label();
			m_hp[i] = new ProgressBar(0, 0);
			m_hp[i].setProgressImage(GameClient.getInstance().getTheme().getImage("hpbar_high"));

			String respath = System.getProperty("res.path");
			if(respath == null)
				respath = "";
			try
			{
				List<String> translated = Translator.translate("_GUI");
				if(pokes[i] != null)
				{
					m_level[i].setText(translated.get(32) + String.valueOf(pokes[i].getLevel()));
					m_pokeName[i].setText(pokes[i].getName());
					m_pokeIcon[i] = new Image(pokes[i].getIcon());
					m_pokeIcon[i].setSize(32, 32);
					m_hp[i].setMaximum(pokes[i].getMaxHP());
					m_hp[i].setProgressImage(GameClient.getInstance().getTheme().getImage("hpbar_high"));
					m_hp[i].setValue(pokes[i].getCurHP());
					if(pokes[i].getCurHP() > pokes[i].getMaxHP() / 2)
						m_hp[i].setProgressImage(GameClient.getInstance().getTheme().getImage("hpbar_high"));
					else if(pokes[i].getCurHP() < pokes[i].getMaxHP() / 2 && pokes[i].getCurHP() > pokes[i].getMaxHP() / 3)
						m_hp[i].setProgressImage(GameClient.getInstance().getTheme().getImage("hpbar_middle"));
					else if(pokes[i].getCurHP() < pokes[i].getMaxHP() / 3)
						m_hp[i].setProgressImage(GameClient.getInstance().getTheme().getImage("hpbar_low"));
					m_pokeIcon[i].setImage(pokes[i].getIcon());
					m_pokeIcon[i].setSize(32, 32);
					m_pokeName[i].setText(pokes[i].getName());
					m_level[i].setText(translated.get(32) + String.valueOf(pokes[i].getLevel()));
				}
				else
					m_hp[i].setVisible(false);
			}
			catch(NullPointerException e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * Sets sprite number
	 * 
	 * @param x
	 * @return
	 */
	public int setSpriteNumber(int x)
	{
		int i = 0;
		if(x <= 385)
			i = x + 1;
		else if(x <= 388)
			i = 386;
		else if(x <= 414)
			i = x - 2;
		else if(x <= 416)
			i = 413;
		else
			i = x - 4;
		return i;
	}

	/**
	 * Updates info
	 * 
	 * @param pokes
	 */
	public void update(OurPokemon[] pokes)
	{
		m_pokes = pokes;
		List<String> translated = Translator.translate("_GUI");
		int pokemonCount = 0;
		for(int i = 0; i < 6; i++)
		{
			if(m_pokes[i] != null)
				pokemonCount++;
		}
		for(int i = 0; i < pokemonCount; i++)
			try
			{
				if(pokes[i] != null)
				{
					m_hp[i].setMaximum(pokes[i].getMaxHP());
					m_hp[i].setValue(pokes[i].getCurHP());
					if(pokes[i].getCurHP() > pokes[i].getMaxHP() / 2)
						m_hp[i].setProgressImage(GameClient.getInstance().getTheme().getImage("hpbar_high"));
					else if(pokes[i].getCurHP() < pokes[i].getMaxHP() / 2 && pokes[i].getCurHP() > pokes[i].getMaxHP() / 3)
						m_hp[i].setProgressImage(GameClient.getInstance().getTheme().getImage("hpbar_middle"));
					else if(pokes[i].getCurHP() < pokes[i].getMaxHP() / 3)
						m_hp[i].setProgressImage(GameClient.getInstance().getTheme().getImage("hpbar_low"));
					m_pokeIcon[i].setImage((pokes[i].getIcon()));
					m_pokeName[i].setText(pokes[i].getName());
					m_level[i].setText(translated.get(32) + String.valueOf(pokes[i].getLevel()));
					m_level[i].setPosition(m_pokeName[i].getX() + m_pokeName[i].getWidth() + 10, 5);

					m_pokeIcon[i].setPosition(2, 3);
					m_pokeName[i].setPosition(45, 5);
					m_hpBar[i].setPosition(45, m_pokeName[i].getY() + m_pokeName[i].getHeight() + 3);
					m_hp[i].setPosition(m_hpBar[i].getX() + 23, m_hpBar[i].getY() + 3);
					m_hpBar[i].setVisible(true);
					m_hp[i].setVisible(true);
					if(i != 0)
						m_switchUp[i].setVisible(true);
					if(i != 5)
						m_switchDown[i].setVisible(true);
				}
				else
				{
					if(i != 0)
						m_switchUp[i].setVisible(false);
					if(i != 5)
						m_switchDown[i].setVisible(false);
					m_hpBar[i].setVisible(false);
					m_hp[i].setVisible(false);
					m_level[i].setText("");
					m_pokeIcon[i].setImage(null);
				}
			}
			catch(NullPointerException npe)
			{
				npe.printStackTrace();
			}
	}

	private void allocateVariables()
	{
		m_switchDown = new Button[6];
		m_switchUp = new Button[6];
		m_hp = new ProgressBar[6];
		m_level = new Label[6];
		m_pokeName = new Label[6];
		m_pokeIcon = new Image[6];
		m_hpBar = new Image[6];
		m_container = new Widget[6];
	}
}
