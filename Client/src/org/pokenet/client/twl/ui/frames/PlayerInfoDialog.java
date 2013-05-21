package org.pokenet.client.twl.ui.frames;

import org.pokenet.client.GameClient;
import org.pokenet.client.backend.FileLoader;
import org.pokenet.client.backend.entity.Player;
import org.pokenet.client.backend.entity.Player.Direction;
import org.pokenet.client.twl.ui.base.Image;
import de.matthiasmann.twl.Color;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.Widget;

/**
 * A frame that shows trainer information and stats
 * 
 * @author Myth1c
 */
public class PlayerInfoDialog extends Widget
{
	final boolean ALL_REGIONS = false; // set this to TRUE/refactor as regions
										// are completed
	private Image[] m_kanto;
	private Image[] m_johto;
	private Image[] m_hoenn;
	private Image[] m_sinnoh;
	private Image[] m_orange;
	private Image[] m_extras;

	private Image m_playerImage;

	private Label m_kantoLbl;
	private Label m_johtoLbl;
	private Label m_hoennLbl;
	private Label m_sinnohLbl;
	private Label m_orangeLbl;
	private Label m_extrasLbl;

	private Label m_trainerEXP;
	private Label m_breedingEXP;
	private Label m_fishingEXP;
	private Label m_coordinatingEXP;

	private int maxLblWidth;

	/**
	 * Default constructor
	 */
	public PlayerInfoDialog()
	{
		m_kanto = new Image[8];
		m_johto = new Image[8];
		// if (ALL_REGIONS)
		m_hoenn = new Image[8];
		// if (ALL_REGIONS)
		m_sinnoh = new Image[8];
		if(ALL_REGIONS)
			m_orange = new Image[4];
		if(ALL_REGIONS)
			m_extras = new Image[6];

		initGUI();
	}

	/**
	 * Initializes the interface
	 */
	@SuppressWarnings("all")
	private void initGUI()
	{
		// Player Image
		m_playerImage = new Image(FileLoader.toTWLImage(Player.getSpriteFactory().getSprite(Direction.Down, false, false, GameClient.getInstance().getOurPlayer().getSprite())));
		add(m_playerImage);
		// Trainer data labels
		m_trainerEXP = new Label("Trainer Lv:          " + GameClient.getInstance().getOurPlayer().getTrainerLevel());
		m_breedingEXP = new Label("Breeding Lv:       " + GameClient.getInstance().getOurPlayer().getBreedingLevel());
		m_fishingEXP = new Label("Fishing Lv:          " + GameClient.getInstance().getOurPlayer().getFishingLevel());
		m_coordinatingEXP = new Label("Coordinating Lv:  " + GameClient.getInstance().getOurPlayer().getCoordinatingLevel());

		add(m_trainerEXP);
		add(m_breedingEXP);
		add(m_fishingEXP);
		add(m_coordinatingEXP);

		// Start the badge labels
		m_kantoLbl = new Label("Kanto:");
		m_johtoLbl = new Label("Johto:");
		// if (ALL_REGIONS)
		m_hoennLbl = new Label("Hoenn:");
		// if (ALL_REGIONS)
		m_sinnohLbl = new Label("Sinnoh:");
		if(ALL_REGIONS)
			m_orangeLbl = new Label("Orange Islands:");
		if(ALL_REGIONS)
			m_extrasLbl = new Label("Others:");

		// Add Labels to Content Pane
		add(m_kantoLbl);
		add(m_johtoLbl);
		add(m_hoennLbl);
		add(m_sinnohLbl);

		if(ALL_REGIONS)
			add(m_orangeLbl);
		if(ALL_REGIONS)
			add(m_extrasLbl);

		maxLblWidth = ((ALL_REGIONS) ? m_orangeLbl : m_kantoLbl).computeTextWidth();

		loadImages();
		/* 6 rows, 20 pixels each + title bar height */
		if(ALL_REGIONS)
		{
			setSize(160 + 2 + 60, 300);
		}
		else
		{
			setSize(160 + 2 + 60, 155);
		}

		showBadges();
	}

	/**
	 * Loads the status icons
	 */
	public void loadImages()
	{
		String respath = System.getProperty("res.path");
		if(respath == null)
			respath = "";
		String m_path = respath + "res/badges/";
		// Kanto Badges
		for(int i = 0; i < 8; i++)
		{
			// KANTO
			m_kanto[i] = new Image(FileLoader.loadImage(m_path + "kanto" + (i + 1) + ".png"));

			// JOHTO
			m_johto[i] = new Image(FileLoader.loadImage(m_path + "johto" + (i + 1) + ".png"));

			// HOENN
			// if (ALL_REGIONS) {
			m_hoenn[i] = new Image(FileLoader.loadImage(m_path + "hoenn" + (i + 1) + ".png"));

			// }
			// SINNOH
			// if (ALL_REGIONS) {
			m_sinnoh[i] = new Image(FileLoader.loadImage(m_path + "sinnoh" + (i + 1) + ".png"));

			// }
			// ORANGE ISLANDS
			if(ALL_REGIONS)
			{
				if(i < 4)
				{
					m_orange[i] = new Image(FileLoader.loadImage(m_path + "orange" + (i + 1) + ".png"));
					add(m_orange[i]);
				}
			}
			// Extra badges ???
			if(ALL_REGIONS)
			{
				if(i < 6)
				{
					m_extras[i] = new Image(FileLoader.loadImage(m_path + "extra" + (i + 1) + ".png"));
					add(m_extras[i]);
				}
			}
			add(m_kanto[i]);
			add(m_johto[i]);
			add(m_hoenn[i]);
			add(m_sinnoh[i]);
		}
	}

	/**
	 * Shows badges (darkens ones the player does not have)
	 * 
	 * @throws NullPointerException
	 */
	public void showBadges() throws NullPointerException
	{
		int[] badges = GameClient.getInstance().getOurPlayer().getBadges();
		try
		{
			for(int i = 0; i < badges.length; i++)
			{
				if(i < 8)
				{
					// Kanto
					if(badges[i] == 0)
						m_kanto[i].setImage(m_kanto[i].getImage().createTintedVersion(Color.BLACK));
				}
				else if(i < 16)
				{
					// Johto
					if(badges[i] == 0)
						m_johto[i - 8].setImage(m_johto[i - 8].getImage().createTintedVersion(Color.BLACK));
				}
				else if(i < 24)
				{
					// Hoenn
					// if (ALL_REGIONS) {
					if(badges[i] == 0)
						m_hoenn[i - 16].setImage(m_hoenn[i - 16].getImage().createTintedVersion(Color.BLACK));
					// }
				}
				else if(i < 32)
				{
					// Sinnoh
					// if (ALL_REGIONS) {
					if(badges[i] == 0)
						m_sinnoh[i - 24].setImage(m_sinnoh[i - 24].getImage().createTintedVersion(Color.BLACK));
					// }
				}
				else if(i < 36)
				{
					// Orange Islands
					if(ALL_REGIONS)
					{
						if(badges[i] == 0)
							m_orange[i - 32].setImage(m_orange[i - 32].getImage().createTintedVersion(Color.BLACK));
					}
				}
				else if(i < 42)
				{
					// Extras
					if(ALL_REGIONS)
					{
						if(badges[i] == 0)
							m_extras[i - 36].setImage(m_extras[i - 36].getImage().createTintedVersion(Color.BLACK));
					}
				}
				else
				{
					throw new NullPointerException("Bad Badge Number");
				}
			}
		}
		catch(NullPointerException e)
		{
			e.printStackTrace();
			System.err.println("See http://xkcd.com/371/ for details");
		}
	}

	public void updateDialog()
	{
		m_trainerEXP.setText("Trainer Lv:           " + GameClient.getInstance().getOurPlayer().getTrainerLevel());
		m_breedingEXP.setText("Breeding Lv:        " + GameClient.getInstance().getOurPlayer().getBreedingLevel());
		m_fishingEXP.setText("Fishing Lv:           " + GameClient.getInstance().getOurPlayer().getFishingLevel());
		m_coordinatingEXP.setText("Corrdinating Lv: " + GameClient.getInstance().getOurPlayer().getCoordinatingLevel());
		showBadges();
	}

	@Override
	public void layout()
	{
		m_trainerEXP.setPosition(getInnerX() + 85, getInnerY() + 10);
		m_breedingEXP.setPosition(getInnerX() + 85, getInnerY() + 25);
		m_fishingEXP.setPosition(getInnerX() + 85, getInnerY() + 40);
		m_coordinatingEXP.setPosition(getInnerX() + 85, getInnerY() + 55);
		m_playerImage.setPosition(getInnerX() + 2, getInnerY());
		for(int i = 0; i < 8; i++)
		{
			int y = getInnerY() + 65;
			m_kanto[i].setPosition(60 + maxLblWidth + (20 * i) + getInnerX(), y);
			y = 2 + y + 20;
			m_johto[i].setPosition(60 + maxLblWidth + (20 * i) + getInnerX(), y);
			y = 2 + y + 20;
			m_hoenn[i].setPosition(60 + maxLblWidth + (20 * i) + getInnerX(), y);
			y = 2 + y + 20;
			m_sinnoh[i].setPosition(60 + maxLblWidth + (20 * i) + getInnerX(), y);
			y = 2 + y + 20;
			if(ALL_REGIONS)
			{
				if(i < 4)
				{
					m_orange[i].setPosition(2 + maxLblWidth + (20 * i) + getInnerX(), y);
				}
				y = 2 + y + 20;
				if(i < 6)
				{
					m_extras[i].setPosition(2 + maxLblWidth + (20 * i) + getInnerX(), y);
				}
			}
		}
		m_kantoLbl.setPosition(getInnerX() + 5, m_kanto[0].getY() + 8);
		m_johtoLbl.setPosition(getInnerX() + 5, m_johto[0].getY() + 8);
		m_hoennLbl.setPosition(getInnerX() + 5, m_hoenn[0].getY() + 8);
		m_sinnohLbl.setPosition(getInnerX() + 5, m_sinnoh[0].getY() + 8);
	}
}
