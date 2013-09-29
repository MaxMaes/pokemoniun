package org.pokemonium.client.ui.frames;

import java.util.List;
import org.pokemonium.client.GameClient;
import org.pokemonium.client.backend.Translator;
import org.pokemonium.client.backend.entity.OurPokemon;
import org.pokemonium.client.constants.ServerPacket;
import org.pokemonium.client.protocol.ClientMessage;
import org.pokemonium.client.ui.components.Image;
import org.pokemonium.client.ui.components.ProgressBar;
import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.CallbackWithReason;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.Label.CallbackReason;
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
		for(int i = 0; i < 6; i++)
		{
			final int j = i;
			m_container[i] = new Widget();
			m_container[i].setTheme("partyslot");
			m_container[i].setVisible(true);

			add(m_container[i]);
			String respath = System.getProperty("res.path");
			if(respath == null)
				respath = "";

			try
			{
				m_hpBar[i] = new Image(GameClient.getInstance().getTheme().getImage("party_hpbar"));
				m_hpBar[i].setVisible(false);
				m_container[i].add(m_hpBar[i]);
				m_container[i].add(m_pokeIcon[i]);
				m_container[i].add(m_pokeName[i]);
				final int p = i;
				m_pokeName[i].addCallback(new CallbackWithReason<Label.CallbackReason>()
				{
					@Override
					public void callback(CallbackReason arg0)
					{
						if(arg0 == CallbackReason.CLICK)
						{
							GameClient.getInstance().getHUD().showPokemonInfoDialogs(p, m_pokes[p]);
						}

					}
				});
				m_container[i].add(m_level[i]);
				m_container[i].add(m_hp[i]);

				if(i != 0)
				{
					m_switchUp[i] = new Button();
					m_switchUp[i].setTheme("arrow_up");
					m_switchUp[i].setCanAcceptKeyboardFocus(false);
					m_switchUp[i].addCallback(new Runnable()
					{
						@Override
						public void run()
						{
							ClientMessage message = new ClientMessage(ServerPacket.SWAP_PARTY);
							message.addInt(j);
							message.addInt(j - 1);
							GameClient.getInstance().getSession().send(message);
							// reinitialize the gui
							removeAllChildren();
							allocateVariables();
							loadImages(m_pokes);
							initGUI();
						}
					});
					m_container[i].add(m_switchUp[i]);
				}
				if(i != 5)
				{
					m_switchDown[i] = new Button();
					m_switchDown[i].setTheme("arrow_down");
					m_switchDown[i].setCanAcceptKeyboardFocus(false);
					m_switchDown[i].addCallback(new Runnable()
					{
						@Override
						public void run()
						{
							ClientMessage message = new ClientMessage(ServerPacket.SWAP_PARTY);
							message.addInt(j);
							message.addInt(j + 1);
							GameClient.getInstance().getSession().send(message);
							// reinitialize the gui
							removeAllChildren();
							allocateVariables();
							loadImages(m_pokes);
							initGUI();
						}
					});
					m_container[i].add(m_switchDown[i]);
				}
			}
			catch(NullPointerException npe)
			{
				npe.printStackTrace();
			}
		}
		update(m_pokes);
	}

	/**
	 * Loads necessary images
	 * 
	 * @param pokes
	 */
	public void loadImages(OurPokemon[] pokes)
	{
		for(int i = 0; i < 6; i++)
		{
			m_pokeName[i] = new Label();
			m_pokeIcon[i] = new Image();
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
					m_pokeIcon[i].setImage(pokes[i].getIcon());
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
		for(int i = 0; i < 6; i++)
		{
			try
			{
				if(pokes[i] != null)
				{
					m_hp[i].setMaximum(pokes[i].getMaxHP());
					m_hp[i].setValue(pokes[i].getCurHP());
					m_pokeIcon[i].setImage((pokes[i].getIcon()));
					m_pokeName[i].setText(pokes[i].getName());
					m_level[i].setText(translated.get(32) + String.valueOf(pokes[i].getLevel()));
					m_hpBar[i].setVisible(true);
					m_hp[i].setVisible(true);
					if(i != 0 && m_pokes[i] != null)
					{
						m_switchUp[i].setVisible(true);
					}
					if(i != 5 && m_pokes[i] != null)
					{
						m_switchDown[i].setVisible(true);
					}
				}
				else
				{
					if(i != 0 && m_switchUp[i] != null)
					{
						m_switchUp[i].setVisible(false);
					}
					if(i != 5 && m_switchUp[i] != null)
					{
						m_switchDown[i].setVisible(false);
					}
					m_pokeName[i].setText("");
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

	@Override
	public void layout()
	{
		super.layout();
		setSize(170, 42 * 6);
		int containerHeight = 42;
		int containerOffset;
		for(int i = 0; i < 6; i++)
		{
			containerOffset = containerHeight * i;
			m_container[i].setPosition(getInnerX(), 5 + getInnerY() + containerOffset);
			m_pokeIcon[i].setPosition(3 + getInnerX(), 3 + getInnerY() + containerOffset);
			m_pokeName[i].setPosition(42 + getInnerX(), 16 + getInnerY() + containerOffset);
			m_pokeName[i].adjustSize();
			m_hp[i].setSize(72, 5);
			m_pokeIcon[i].setSize(32, 32);
			m_container[i].setSize(170, 42);
			m_hpBar[i].setSize(98, 11);
			if(i != 0 && m_pokes[i] != null)
			{
				m_switchUp[i].setSize(16, 16);
			}
			if(i != 5 && m_pokes[i] != null)
			{
				m_switchDown[i].setSize(16, 16);
			}
			m_pokeIcon[i].setSize(32, 32);
			m_level[i].adjustSize();
			m_level[i].setPosition(getInnerX() + getWidth() - m_level[i].getWidth() - 7, 16 + getInnerY() + containerOffset);
			m_hpBar[i].setPosition(45 + getInnerX(), m_pokeName[i].getY() + m_pokeName[i].computeTextHeight() + 3);
			m_hp[i].setPosition(m_hpBar[i].getX() + 23, m_hpBar[i].getY() + 3);
			if(m_switchDown[i] != null)
				m_switchDown[i].setPosition(24 + getInnerX(), 0 + getInnerY() + containerOffset);
			if(m_pokes[i] != null)
			{
				if(m_pokes[i].getCurHP() > m_pokes[i].getMaxHP() / 2)
					m_hp[i].setProgressImage(GameClient.getInstance().getTheme().getImage("hpbar_high"));
				else if(m_pokes[i].getCurHP() < m_pokes[i].getMaxHP() / 2 && m_pokes[i].getCurHP() > m_pokes[i].getMaxHP() / 3)
					m_hp[i].setProgressImage(GameClient.getInstance().getTheme().getImage("hpbar_middle"));
				else if(m_pokes[i].getCurHP() < m_pokes[i].getMaxHP() / 3)
					m_hp[i].setProgressImage(GameClient.getInstance().getTheme().getImage("hpbar_low"));
			}
		}
	}
}
