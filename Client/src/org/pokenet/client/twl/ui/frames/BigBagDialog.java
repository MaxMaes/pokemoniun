package org.pokenet.client.twl.ui.frames;

import java.util.ArrayList;
import java.util.HashMap;
import org.pokenet.client.GameClient;
import org.pokenet.client.backend.ItemDatabase;
import org.pokenet.client.backend.entity.PlayerItem;
import org.pokenet.client.constants.ServerPacket;
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.ui.base.TWLImageButton;
import de.matthiasmann.twl.Alignment;
import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.Widget;
import de.matthiasmann.twl.renderer.Image;

/**
 * The big bag dialog
 * 
 * @author Myth1c
 */
public class BigBagDialog extends ResizableFrame
{
	protected TWLImageButton[] m_categoryButtons;
	protected int m_curCategory = 0;
	protected ArrayList<Button> m_itemBtns;
	protected Button m_leftButton, m_rightButton, m_cancel;
	protected ItemPopup m_popup;
	protected ArrayList<Label> m_stockLabels;
	protected boolean m_update = false;
	private HashMap<Integer, ArrayList<PlayerItem>> m_items;
	private HashMap<Integer, Integer> m_scrollIndex;
	private Image[] bagicons;
	private Image bagicon;

	public BigBagDialog()
	{
		setCenter();
		initGUI();
		loadItems();
	}

	/**
	 * Adds an item to the bag
	 * 
	 * @param id
	 * @param amount
	 */
	public void addItem(int id, boolean newItem)
	{
		if(newItem)
			for(PlayerItem item : GameClient.getInstance().getOurPlayer().getItems())
				if(item.getNumber() == id)
					// Potions and medicine
					if(item.getItem().getCategory().equalsIgnoreCase("Potions") || item.getItem().getCategory().equalsIgnoreCase("Medicine"))
						m_items.get(1).add(item);
					else if(item.getItem().getCategory().equalsIgnoreCase("Food"))
						m_items.get(2).add(item);
					else if(item.getItem().getCategory().equalsIgnoreCase("Pokeball"))
						m_items.get(3).add(item);
					else if(item.getItem().getCategory().equalsIgnoreCase("TM"))
						m_items.get(4).add(item);
					else
						m_items.get(0).add(item);
		m_update = true;
	}

	/**
	 * Closes the bag
	 */
	public void closeBag()
	{
		setVisible(false);
		// GameClient.getInstance().getDisplay().remove(this); TODO:
	}

	/**
	 * Destroys the item popup
	 */
	public void destroyPopup()
	{
		/* if(getDisplay().containsChild(m_popup)) //TODO: Chappie popup?
		 * {
		 * m_popup.destroyPopup();
		 * m_popup = null;
		 * } */
	}

	/** Initializes the interface */
	public void initGUI()
	{
		/* Does this cause a memory leak in JAVA if called more than once? If so does java have a delete keyword? */
		m_items = new HashMap<Integer, ArrayList<PlayerItem>>();
		m_scrollIndex = new HashMap<Integer, Integer>();
		m_itemBtns = new ArrayList<Button>();
		m_stockLabels = new ArrayList<Label>();
		m_categoryButtons = new TWLImageButton[5];
		// remove any existing Bag gui content
		removeAllChildren();
		String respath = System.getProperty("res.path");
		if(respath == null)
			respath = "";
		try
		{
			Image[] bagcat = new Image[] { GameClient.getInstance().getTheme().getImage("item"), GameClient.getInstance().getTheme().getImage("item_hover"),
					GameClient.getInstance().getTheme().getImage("item_pressed") };

			Image[] potioncat = new Image[] { GameClient.getInstance().getTheme().getImage("potion"), GameClient.getInstance().getTheme().getImage("potion_hover"),
					GameClient.getInstance().getTheme().getImage("potion_pressed") };

			Image[] berriescat = new Image[] { GameClient.getInstance().getTheme().getImage("berrie"), GameClient.getInstance().getTheme().getImage("berrie_hover"),
					GameClient.getInstance().getTheme().getImage("berrie_pressed") };

			Image[] pokecat = new Image[] { GameClient.getInstance().getTheme().getImage("pokeball"), GameClient.getInstance().getTheme().getImage("berrie_hover"),
					GameClient.getInstance().getTheme().getImage("berrie_pressed") };

			Image[] tmscat = new Image[] { GameClient.getInstance().getTheme().getImage("tm"), GameClient.getInstance().getTheme().getImage("tm_hover"),
					GameClient.getInstance().getTheme().getImage("tm_pressed") };

			for(int i = 0; i < m_categoryButtons.length; i++)
			{
				final int j = i;
				switch(i)
				{
					case 0:
						m_categoryButtons[i] = new TWLImageButton(bagcat[0], bagcat[1], bagcat[2]);
						m_categoryButtons[i].setTooltipContent("Bag");
						break;
					case 1:
						m_categoryButtons[i] = new TWLImageButton(potioncat[0], potioncat[1], potioncat[2]);
						m_categoryButtons[i].setTooltipContent("Potions");
						break;
					case 2:
						m_categoryButtons[i] = new TWLImageButton(berriescat[0], berriescat[1], berriescat[2]);
						m_categoryButtons[i].setTooltipContent("Food");
						break;
					case 3:
						m_categoryButtons[i] = new TWLImageButton(pokecat[0], pokecat[1], pokecat[2]);
						m_categoryButtons[i].setTooltipContent("Pokeballs");
						break;
					case 4:
						m_categoryButtons[i] = new TWLImageButton(tmscat[0], tmscat[1], tmscat[2]);
						m_categoryButtons[i].setTooltipContent("TMs");
						break;
				}
				m_items.put(i, new ArrayList<PlayerItem>());
				m_scrollIndex.put(i, 0);
				m_categoryButtons[i].setSize(40, 40);
				if(i == 0)
					m_categoryButtons[i].setPosition(80, 10);
				else
					m_categoryButtons[i].setPosition(m_categoryButtons[i - 1].getX() + 65, 10);
				m_categoryButtons[i].addCallback(new Runnable()
				{
					@Override
					public void run()
					{
						destroyPopup();
						m_curCategory = j;
						m_update = true;
					}
				});
				add(m_categoryButtons[i]);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		// Bag Image
		loadBagIcons();

		// Scrolling Button LEFT
		m_leftButton = new Button("<");
		m_leftButton.setSize(20, 40);
		m_leftButton.setPosition(15, 95);
		m_leftButton.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				destroyPopup();
				int i = m_scrollIndex.get(m_curCategory) - 1;
				m_scrollIndex.remove(m_curCategory);
				m_scrollIndex.put(m_curCategory, i);
				m_update = true;
			}
		});
		add(m_leftButton);
		// Item Buttons and Stock Labels
		for(int i = 0; i < 4; i++)
		{
			final int j = i;
			// Starts the item buttons
			Button item = new Button();
			item.setSize(60, 60);
			item.setPosition(50 + 80 * i, 85);
			item.addCallback(new Runnable()
			{
				@Override
				public void run()
				{
					destroyPopup();
					useItem(j);
				}
			});
			m_itemBtns.add(item);
			add(item);
			// Starts the item labels
			Label stock = new Label();
			stock.setSize(60, 40);
			stock.setPosition(50 + 80 * i, 135);
			stock.setAlignment(Alignment.CENTER);
			m_stockLabels.add(stock);
			add(stock);
		}

		// Scrolling Button Right
		m_rightButton = new Button(">");
		m_rightButton.setSize(20, 40);
		m_rightButton.setPosition(365, 95);
		m_rightButton.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				destroyPopup();
				int i = m_scrollIndex.get(m_curCategory) + 1;
				m_scrollIndex.remove(m_curCategory);
				m_scrollIndex.put(m_curCategory, i);
				m_update = true;
			}
		});
		add(m_rightButton);
		// Close Button
		m_cancel = new Button("Close");
		m_cancel.setSize(400, 32);
		m_cancel.setPosition(0, 195);
		m_cancel.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				destroyPopup();
				closeBag();
			}
		});
		add(m_cancel);
		// Frame properties
		addCloseCallback(new Runnable()
		{
			@Override
			public void run()
			{
				destroyPopup();
				closeBag();
			}
		});

		setTitle("Bag");
		setResizableAxis(ResizableAxis.NONE);
		setSize(m_categoryButtons.length * 80, 250);
		setVisible(true);
		setCenter();
	}

	private void loadBagIcons()
	{
		bagicons = new Image[] { GameClient.getInstance().getTheme().getImage("bag_topleft"), GameClient.getInstance().getTheme().getImage("bag_topright"),
				GameClient.getInstance().getTheme().getImage("bag_middle"), GameClient.getInstance().getTheme().getImage("bag_front"), GameClient.getInstance().getTheme().getImage("bag_bottom"),
				GameClient.getInstance().getTheme().getImage("bag_topleft") };
		bagicon = bagicons[3];
	}

	/**
	 * Removes an item to the bag
	 * 
	 * @param id
	 * @param amount
	 */
	public void removeItem(int id, boolean remove)
	{
		/* The remove variable indicates that this is the last of the item, and it should be removed from the inventory */
		if(remove)
		{
			for(PlayerItem item : GameClient.getInstance().getOurPlayer().getItems())
				if(item.getNumber() == id)
					// Potions and medicine
					if(item.getItem().getCategory().equalsIgnoreCase("Potions") || item.getItem().getCategory().equalsIgnoreCase("Medicine"))
						m_items.get(1).remove(item);
					else if(item.getItem().getCategory().equalsIgnoreCase("Food"))
						m_items.get(2).remove(item);
					else if(item.getItem().getCategory().equalsIgnoreCase("Pokeball"))
						m_items.get(3).remove(item);
					else if(item.getItem().getCategory().equalsIgnoreCase("TM"))
						m_items.get(4).remove(item);
					else
						m_items.get(0).remove(item);
			/* There is probably a better way to do the code below, but what essentially occurs is a re-initialization of the bag screen. Then the category is set back to the previous category. The affect this has for the user is, the item is instantly removed from the players bag screen when the last of the item is used. */
			int tmpCurCategory = m_curCategory;
			initGUI();
			loadItems();
			m_curCategory = tmpCurCategory;
		}
		m_update = true;
	}

	/**
	 * Centers the frame
	 */
	public void setCenter()
	{
		int height = (int) GameClient.getInstance().getDisplay().getHeight();
		int width = (int) GameClient.getInstance().getDisplay().getWidth();
		int x = width / 2 - 200;
		int y = height / 2 - 200;
		setSize(getWidth(), getHeight());
		setPosition(x, y);
	}

	// TODO: dunno what to do with this yet
	/* @Override
	 * public void update(GUIContext gc, int delta)
	 * {
	 * super.update(gc, delta);
	 * if(m_update)
	 * {
	 * m_update = false;
	 * // Enable/disable scrolling
	 * if(m_scrollIndex.get(m_curCategory) == 0)
	 * m_leftButton.setEnabled(false);
	 * else
	 * m_leftButton.setEnabled(true);
	 * if(m_scrollIndex.get(m_curCategory) + 4 >= m_items.get(m_curCategory).size())
	 * m_rightButton.setEnabled(false);
	 * else
	 * m_rightButton.setEnabled(true);
	 * // Update items and stocks
	 * System.out.println("Looping through items to display");
	 * for(int i = 0; i < 5; i++)
	 * try
	 * {
	 * m_itemBtns.get(i).setName(String.valueOf(m_items.get(m_curCategory).get(m_scrollIndex.get(m_curCategory) + i).getNumber()));
	 * m_itemBtns.get(i).setTooltipContent(
	 * m_items.get(m_curCategory).get(m_scrollIndex.get(m_curCategory) + i).getItem().getName() + "\n"
	 * + m_items.get(m_curCategory).get(m_scrollIndex.get(m_curCategory) + i).getItem().getDescription());
	 * m_itemBtns.get(i).setImage(m_items.get(m_curCategory).get(m_scrollIndex.get(m_curCategory) + i).getBagImage());
	 * m_stockLabels.get(i).setText("x" + m_items.get(m_curCategory).get(m_scrollIndex.get(m_curCategory) + i).getQuantity());
	 * m_itemBtns.get(i).setEnabled(true);
	 * }
	 * catch(Exception e)
	 * {
	 * m_itemBtns.get(i).setImage(null);
	 * m_itemBtns.get(i).setTooltipContent("");
	 * m_itemBtns.get(i).setText("");
	 * m_stockLabels.get(i).setText("");
	 * m_itemBtns.get(i).setEnabled(false);
	 * }
	 * }
	 * } */

	/**
	 * An item was used!
	 * 
	 * @param i
	 */
	public void useItem(int i)
	{
		destroyPopup();
		if(m_curCategory == 3)
		// if(m_curCategory == 0 || m_curCategory == 3)
		{
			if(ItemDatabase.getInstance().getItem(Integer.valueOf(m_itemBtns.get(i).getText())).getCategory().equalsIgnoreCase("Field"))
				m_popup = new ItemPopup(((String) m_itemBtns.get(i).getTooltipContent()).split("\n")[0], Integer.parseInt(m_itemBtns.get(i).getText()), false, false);
			// System.out.println("win " + i);
			else
				m_popup = new ItemPopup(((String) m_itemBtns.get(i).getTooltipContent()).split("\n")[0], Integer.parseInt(m_itemBtns.get(i).getText()), true, false);
			// System.out.println("fail " + i);
			// System.out.println(ItemDatabase.getInstance().getItem(Integer.valueOf(m_itemBtns.get(i).getName()))
			// .getCategory());
			m_popup.setPosition(m_itemBtns.get(i).getInnerX(), m_itemBtns.get(i).getInnerY() + m_itemBtns.get(i).getHeight() - 48);
			add(m_popup);
		}
		else
		{
			m_popup = new ItemPopup(((String) m_itemBtns.get(i).getTooltipContent()).split("\n")[0], Integer.parseInt(m_itemBtns.get(i).getText()), true, false);
			m_popup.setPosition(m_itemBtns.get(i).getInnerX(), m_itemBtns.get(i).getInnerY() + m_itemBtns.get(i).getHeight() - 48);
			add(m_popup);
			// System.out.println("check " + i);
		}
	}

	private void loadItems()
	{
		try
		{
			// Load the player's items and sort them by category
			for(PlayerItem item : GameClient.getInstance().getOurPlayer().getItems())
				// Field items
				if(item.getItem().getCategory().equalsIgnoreCase("Field") || item.getItem().getCategory().equalsIgnoreCase("Evolution") || item.getItem().getCategory().equalsIgnoreCase("Held"))
					m_items.get(0).add(item);
				else if(item.getItem().getCategory().equalsIgnoreCase("Potions") || item.getItem().getCategory().equalsIgnoreCase("Medicine")
						|| item.getItem().getCategory().equalsIgnoreCase("Vitamins"))
					m_items.get(1).add(item);
				else if(item.getItem().getCategory().equalsIgnoreCase("Food"))
					m_items.get(2).add(item);
				else if(item.getItem().getCategory().equalsIgnoreCase("Pokeball"))
					m_items.get(3).add(item);
				else if(item.getItem().getCategory().equalsIgnoreCase("TM"))
					m_items.get(4).add(item);
			m_update = true;
		}
		catch(Exception e)
		{
			System.out.println("item trouble");
			e.printStackTrace();
		}
	}

	@Override
	public void paintWidget(GUI gui)
	{
		bagicon.draw(getAnimationState(), 18 + getInnerX(), 0 + getInnerY());
	}
}

/**
 * The use dialog for items
 * 
 * @author Myth1c
 */
class ItemPopup extends Widget
{
	private Button m_cancel;
	private Button m_destroy;
	private Button m_give;
	private Label m_name;
	private TeamPopup m_team;
	private Button m_use;

	/**
	 * Default Constructor
	 * 
	 * @param item
	 * @param id
	 * @param useOnPokemon
	 * @param isBattle
	 */
	public ItemPopup(String item, int id, boolean useOnPokemon, boolean isBattle)
	{
		final int m_id = id;
		final boolean m_useOnPoke = useOnPokemon;
		final boolean m_isBattle = isBattle;
		setPosition(getX() - 1, getY() + 1);
		// Item name label
		m_name = new Label(item.split("\n")[0]);
		m_name.setPosition(0, 0);
		add(m_name);
		// Use button
		m_use = new Button("Use");
		m_use.setSize(100, 25);
		m_use.setPosition(0, m_name.getY() + m_name.getHeight() + 3);
		m_use.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				useItem(m_id, m_useOnPoke, m_isBattle);
			}
		});
		add(m_use);
		if(!isBattle)
		{
			m_give = new Button("Give");
			m_give.setSize(100, 25);
			m_give.setPosition(0, m_use.getY() + 25);
			// m_give.setEnabled(false);
			m_give.addCallback(new Runnable()
			{
				@Override
				public void run()
				{
					giveItem(m_id);
				}
			});
			add(m_give);
		}
		// Destroy the item
		m_destroy = new Button("Drop");
		m_destroy.setSize(100, 25);
		if(!isBattle)
			m_destroy.setPosition(0, m_give.getY() + 25);
		else
			m_destroy.setPosition(0, m_use.getY() + 25);
		m_destroy.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				ClientMessage message = new ClientMessage(ServerPacket.ITEM_DESTROY);
				message.addInt(m_id);
				GameClient.getInstance().getSession().send(message);
				destroyPopup();
			}
		});
		add(m_destroy);

		// Close the popup
		m_cancel = new Button("Cancel");
		m_cancel.setSize(100, 25);
		m_cancel.setPosition(0, m_destroy.getY() + 25);
		m_cancel.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				destroyPopup();
			}
		});
		add(m_cancel);
		if(!isBattle)
			setSize(100, 140);
		else
			setSize(100, 115);
		setVisible(true);
		// setAlwaysOnTop(true); //TODO: Chappie magic :D
	}

	/**
	 * Destroys the popup
	 */
	public void destroyPopup()
	{
		removeChild(m_team);
		m_team = null;
		removeChild(this);
	}

	/**
	 * Give the item to a pokemon
	 * 
	 * @param id
	 */
	public void giveItem(int id)
	{
		// setAlwaysOnTop(false); //TODO: Chappie magic :D
		removeChild(m_team);
		m_team = null;
		m_team = new TeamPopup(this, id, false, false);
		m_team.setPosition(m_give.getInnerX() + getWidth(), m_give.getInnerY() - 15);
		add(m_team);

	}

	/**
	 * Use the item. usedOnPoke determine whether the item should be applied to a pokemon
	 * 
	 * @param id
	 * @param usedOnPoke
	 */
	public void useItem(int id, boolean usedOnPoke, boolean isBattle)
	{
		removeChild(m_team);
		m_team = null;
		if(usedOnPoke)
		{
			// setAlwaysOnTop(false); //TODO: Chappie magic :D
			m_team = new TeamPopup(this, id, true, isBattle);
			m_team.setPosition(m_use.getInnerX() + getWidth(), m_use.getInnerY() - 15);
			add(m_team);
			// System.out.println("i use");
		}
		else
		{
			ClientMessage message = new ClientMessage(ServerPacket.ITEM_USE);
			message.addString(String.valueOf(id));
			GameClient.getInstance().getSession().send(message);
			destroyPopup();
		}
	}
}

/**
 * PopUp that lists the player's pokemon in order to use/give an item
 * 
 * @author ZombieBear
 */
class TeamPopup extends Widget
{
	private Label[] pokelabels;
	private Label m_details;
	private ItemPopup m_parent;
	private int item;
	private boolean use;
	private boolean isBattle;

	/**
	 * Default constructor
	 * 
	 * @param itemId
	 * @param use
	 * @param useOnPoke
	 */
	public TeamPopup(ItemPopup parent, int itemId, boolean isuse, boolean isbattle)
	{
		setPosition(getX() - 1, getY() + 1);

		m_parent = parent;
		item = itemId;
		use = isuse;
		isBattle = isbattle;
		int y = 0;
		pokelabels = new Label[GameClient.getInstance().getOurPlayer().getPokemon().length];
		for(int i = 0; i < GameClient.getInstance().getOurPlayer().getPokemon().length; i++)
		{
			pokelabels[i] = new Label(GameClient.getInstance().getOurPlayer().getPokemon()[i].getName());
			pokelabels[i].setSize(100, 15);
			pokelabels[i].setPosition(0, y);
			y += 18;
			add(pokelabels[i]);
		}

		// Frame configuration
		setSize(100, 135);
		setVisible(true);
		// setAlwaysOnTop(true); //TODO: Chappie magic :D
	}

	/**
	 * Send the server a packet to inform it an item was used
	 * 
	 * @param use
	 * @param id
	 * @param pokeIndex
	 * @param isBattle
	 */
	public void processItemUse(boolean use, int id, int pokeIndex, boolean isBattle)
	{
		if(use)
		{
			ClientMessage message = new ClientMessage(ServerPacket.ITEM_USE);
			message.addString(id + "," + pokeIndex);
			GameClient.getInstance().getSession().send(message);
		}
		else
		{
			ClientMessage message = new ClientMessage(ServerPacket.ITEM_GIVE);
			message.addString(id + "," + pokeIndex);
			GameClient.getInstance().getSession().send(message);
			GameClient.getInstance().getOurPlayer().getPokemon()[pokeIndex].setHoldItem(ItemDatabase.getInstance().getItem(id).getName());
		}
		m_parent.destroyPopup();
	}

	@Override
	public boolean handleEvent(Event evt)
	{
		if(evt.isMouseEvent())
		{
			int idx = 0;
			for(Label l : pokelabels)
			{
				isMouseInside(evt);
				if(evt.getType() == Event.Type.MOUSE_ENTERED)
				{
					// l.setForeground(new Color(255, 215, 0)); TODO: Set color
				}
				else if(evt.getType() == Event.Type.MOUSE_EXITED)
				{
					// l.setForeground(new Color(255, 255, 255)); TODO: Set color
				}
				else if(evt.getType() == Event.Type.MOUSE_CLICKED)
				{
					processItemUse(use, item, idx, isBattle);
				}
				idx++;
			}
			return true;
		}
		else
		{
			return false;
		}
	}
}