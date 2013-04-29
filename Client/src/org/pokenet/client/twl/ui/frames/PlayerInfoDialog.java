package org.pokenet.client.twl.ui.frames;

import org.pokenet.client.GameClient;
import org.pokenet.client.backend.FileLoader;
import org.pokenet.client.backend.entity.Player;
import org.pokenet.client.backend.entity.Player.Direction;

import de.matthiasmann.twl.Color;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.renderer.Image;

/**
 * A frame that shows trainer information and stats
 * 
 * @author ZombieBear
 * @author TriMethylXanthine_OverDose (TMX:OD/adpoliak)
 * 
 */
public class PlayerInfoDialog extends ResizableFrame {
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
	public PlayerInfoDialog() {
		m_kanto = new Image[8];
		m_johto = new Image[8];
		// if (ALL_REGIONS)
		m_hoenn = new Image[8];
		// if (ALL_REGIONS)
		m_sinnoh = new Image[8];
		if (ALL_REGIONS)
			m_orange = new Image[4];
		if (ALL_REGIONS)
			m_extras = new Image[6];

		initGUI();
	}

	/**
	 * Initializes the interface
	 */
	@SuppressWarnings("all")
	private void initGUI() {
		setTitle("Player Information");
		// Player Image
		m_playerImage = FileLoader.toTWLImage(
				Player.getSpriteFactory().getSprite(Direction.Down, false,
						false,
						GameClient.getInstance().getOurPlayer().getSprite()),
				true);

		// Trainer data labels
		m_trainerEXP = new Label("Trainer Lv:          "
				+ GameClient.getInstance().getOurPlayer().getTrainerLevel());
		m_breedingEXP = new Label("Breeding Lv:       "
				+ GameClient.getInstance().getOurPlayer().getBreedingLevel());
		m_fishingEXP = new Label("Fishing Lv:          "
				+ GameClient.getInstance().getOurPlayer().getFishingLevel());
		m_coordinatingEXP = new Label("Corrdinating Lv:  "
				+ GameClient.getInstance().getOurPlayer()
						.getCoordinatingLevel());

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
		if (ALL_REGIONS)
			m_orangeLbl = new Label("Orange Islands:");
		if (ALL_REGIONS)
			m_extrasLbl = new Label("Others:");

		// Add Labels to Content Pane
		add(m_kantoLbl);
		add(m_johtoLbl);
		// if (ALL_REGIONS)
		add(m_hoennLbl);
		// if (ALL_REGIONS)
		add(m_sinnohLbl);
		if (ALL_REGIONS)
			add(m_orangeLbl);
		if (ALL_REGIONS)
			add(m_extrasLbl);

		maxLblWidth = ((ALL_REGIONS) ? m_orangeLbl : m_kantoLbl).getWidth();

		loadImages();
		/*
		 * 6 rows, 20 pixels each + title bar height
		 */
		if (ALL_REGIONS) {
			setSize(160 + 2 + maxLblWidth, 300);
		} else {
			setSize(160 + 2 + maxLblWidth, 200);
		}

		showBadges();
	}

	/**
	 * Loads the status icons
	 */
	public void loadImages() {
		String respath = System.getProperty("res.path");
		if (respath == null)
			respath = "";
		String m_path = respath + "res/badges/";
		// Kanto Badges
		for (int i = 0; i < 8; i++) {
			// KANTO
			m_kanto[i] = FileLoader.loadImage(m_path + "kanto" + (i + 1)
					+ ".png");
			

			// JOHTO
			m_johto[i] = FileLoader.loadImage(m_path + "johto" + (i + 1)
					+ ".png");
			

			// HOENN
			// if (ALL_REGIONS) {
			m_hoenn[i] = FileLoader.loadImage(m_path + "hoenn" + (i + 1)
					+ ".png");
			
			// }
			// SINNOH
			// if (ALL_REGIONS) {
			m_sinnoh[i] = FileLoader.loadImage(m_path + "sinnoh" + (i + 1) + ".png");
			
			// }
			// ORANGE ISLANDS
			if (ALL_REGIONS) {
				if (i < 4) {
					m_orange[i] = FileLoader.loadImage(m_path + "orange" + (i + 1) + ".png");
					
				}
			}
			// Extra badges ???
			if (ALL_REGIONS) {
				if (i < 6) {
					m_extras[i] = FileLoader.loadImage(m_path + "extra" + (i + 1) + ".png");
				}
			}
		}
	}

	/**
	 * Shows badges (darkens ones the player does not have)
	 * 
	 * @throws NullPointerException
	 */
	public void showBadges() throws NullPointerException {
		int[] badges = GameClient.getInstance().getOurPlayer().getBadges();
		try {
			for (int i = 0; i < badges.length; i++) {
				if (i < 8) {
					// Kanto
					if (badges[i] == 0)
						m_kanto[i] = m_kanto[i].createTintedVersion(Color.BLACK);
				} else if (i < 16) {
					// Johto
					if (badges[i] == 0)
						m_johto[i - 8] = m_johto[i - 8].createTintedVersion(Color.BLACK);
				} else if (i < 24) {
					// Hoenn
					// if (ALL_REGIONS) {
					if (badges[i] == 0)
						m_hoenn[i - 16] = m_hoenn[i - 16].createTintedVersion(Color.BLACK);
					// }
				} else if (i < 32) {
					// Sinnoh
					// if (ALL_REGIONS) {
					if (badges[i] == 0)
						m_sinnoh[i - 24] = m_sinnoh[i - 24].createTintedVersion(Color.BLACK);
					// }
				} else if (i < 36) {
					// Orange Islands
					if (ALL_REGIONS) {
						if (badges[i] == 0)
							m_orange[i - 32] = m_orange[i - 32].createTintedVersion(Color.BLACK);
					}
				} else if (i < 42) {
					// Extras
					if (ALL_REGIONS) {
						if (badges[i] == 0)
							m_extras[i - 36] = m_extras[i - 36].createTintedVersion(Color.BLACK);
					}
				} else {
					throw new NullPointerException("Bad Badge Number");
				}
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
			System.err.println("See http://xkcd.com/371/ for details");
		}
	}

	public void updateDialog() {
		m_trainerEXP.setText("Trainer Lv:          "
				+ GameClient.getInstance().getOurPlayer().getTrainerLevel());
		m_breedingEXP.setText("Breeding Lv:       "
				+ GameClient.getInstance().getOurPlayer().getBreedingLevel());
		m_fishingEXP.setText("Fishing Lv:          "
				+ GameClient.getInstance().getOurPlayer().getFishingLevel());
		m_coordinatingEXP.setText("Corrdinating Lv:  "
				+ GameClient.getInstance().getOurPlayer()
						.getCoordinatingLevel());
		showBadges();
	}

	@Override
	public void layout() {
		m_trainerEXP.setPosition(getInnerX() + m_playerImage.getWidth() + 2,
				getInnerY() + 2);
		m_breedingEXP.setPosition(
				getInnerX() + m_playerImage.getWidth() + 2,
				getInnerY() + 2 + m_trainerEXP.getY()
						+ m_trainerEXP.getHeight());
		m_fishingEXP.setPosition(
				getInnerX() + m_playerImage.getWidth() + 2,
				getInnerY() + 2 + m_breedingEXP.getY()
						+ m_breedingEXP.getHeight());
		m_coordinatingEXP.setPosition(
				getInnerX() + m_playerImage.getWidth() + 2,
				getInnerY() + 2 + m_fishingEXP.getY()
						+ m_fishingEXP.getHeight());
	}

	@Override
	public void paintWidget(GUI gui) {
		super.paintWidget(gui);
		
		//Draw the player
		m_playerImage.draw(getAnimationState(), getInnerX() + 2,
				getInnerY() + 2);
		
		//Draw the badges
		for (int i = 0; i < 8; i++) {
			int y = m_coordinatingEXP.getY() + m_coordinatingEXP.getHeight() + 2;
			m_kanto[i].draw(getAnimationState(), maxLblWidth + (20 * i), y);
			m_johto[i].draw(getAnimationState(), 2 + maxLblWidth + (20 * i), 2 + y + m_kanto[i].getHeight());
			y = 2 + y + m_kanto[i].getHeight();
			m_hoenn[i].draw(getAnimationState(), 2 + maxLblWidth + (20 * i), 2 + y + m_johto[i].getHeight());
			y = 2 + y + m_johto[i].getHeight();
			m_sinnoh[i].draw(getAnimationState(), 2 + maxLblWidth + (20 * i), 2 + y + m_hoenn[i].getHeight());
			y = 2 + y + m_hoenn[i].getHeight();
			if (ALL_REGIONS) {
				if (i < 4) {
					m_orange[i].draw(getAnimationState(), 2 + maxLblWidth + (20 * i), 2 + y + m_sinnoh[i].getHeight());
				}
				y = 2 + y + m_sinnoh[i].getHeight();
				if (i < 6) {
					m_extras[i].draw(getAnimationState(), 2 + maxLblWidth + (20 * i), 2 + y + m_orange[i].getHeight());
				}
			}
		}
	}
}
