package org.pokemonium.client.ui.frames;

import java.util.ArrayList;
import java.util.HashMap;
import org.pokemonium.client.GameClient;
import org.pokemonium.client.backend.ItemDatabase;
import org.pokemonium.client.backend.entity.PlayerItem;
import org.pokemonium.client.constants.ServerPacket;
import org.pokemonium.client.protocol.ClientMessage;
import org.pokemonium.client.ui.components.ImageButton;
import de.matthiasmann.twl.Alignment;
import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.PopupWindow;
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
	protected ImageButton[] m_categoryButtons;
	protected int m_curCategory = 0;
	protected ArrayList<ImageButton> m_itemBtns;
	protected Button m_leftButton, m_rightButton, m_cancel;
	protected ItemPopup m_popup;
	protected ArrayList<Label> m_stockLabels;
	private HashMap<Integer, ArrayList<PlayerItem>> m_items;
	private HashMap<Integer, Integer> m_scrollIndex; // Used to memorize scrolling through different categories
	private Image[] bagicons;
	private org.pokemonium.client.ui.components.Image bagicon;

	public BigBagDialog(Widget root)
	{
		setCenter();
		initGUI(root);
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
		{
			for(PlayerItem item : GameClient.getInstance().getOurPlayer().getItems())
			{
				if(item.getNumber() == id)
				{
					// Potions and medicine
					if(item.getItem().getCategory().equalsIgnoreCase("Potions") || item.getItem().getCategory().equalsIgnoreCase("Medicine"))
					{
						m_items.get(1).add(item);
					}
					else if(item.getItem().getCategory().equalsIgnoreCase("Food"))
					{
						m_items.get(2).add(item);
					}
					else if(item.getItem().getCategory().equalsIgnoreCase("Pokeball"))
					{
						m_items.get(3).add(item);
					}
					else if(item.getItem().getCategory().equalsIgnoreCase("TM"))
					{
						m_items.get(4).add(item);
					}
					else
					{
						m_items.get(0).add(item);
					}
				}
			}
		}
		update();
	}

	/**
	 * Closes the bag
	 */
	public void closeBag()
	{
		GameClient.getInstance().getHUD().toggleBag();
	}

	/**
	 * Destroys the item popup
	 */
	public void destroyPopup(Widget root)
	{
		root.removeChild(m_popup);
		m_popup = null;
	}

	/** Initializes the interface */
	public void initGUI(final Widget root)
	{
		m_items = new HashMap<Integer, ArrayList<PlayerItem>>();
		m_scrollIndex = new HashMap<Integer, Integer>();
		m_itemBtns = new ArrayList<ImageButton>();
		m_stockLabels = new ArrayList<Label>();
		m_categoryButtons = new ImageButton[5];
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

			Image[] pokecat = new Image[] { GameClient.getInstance().getTheme().getImage("pokeball"), GameClient.getInstance().getTheme().getImage("pokeball_hover"),
					GameClient.getInstance().getTheme().getImage("pokeball_pressed") };

			Image[] tmscat = new Image[] { GameClient.getInstance().getTheme().getImage("tm"), GameClient.getInstance().getTheme().getImage("tm_hover"),
					GameClient.getInstance().getTheme().getImage("tm_pressed") };

			for(int i = 0; i < m_categoryButtons.length; i++)
			{
				final int j = i;
				switch(i)
				{
					case 0:
						m_categoryButtons[i] = new ImageButton(bagcat[0], bagcat[1], bagcat[2]);
						m_categoryButtons[i].setTooltipContent("Bag");
						break;
					case 1:
						m_categoryButtons[i] = new ImageButton(potioncat[0], potioncat[1], potioncat[2]);
						m_categoryButtons[i].setTooltipContent("Potions");
						break;
					case 2:
						m_categoryButtons[i] = new ImageButton(berriescat[0], berriescat[1], berriescat[2]);
						m_categoryButtons[i].setTooltipContent("Food");
						break;
					case 3:
						m_categoryButtons[i] = new ImageButton(pokecat[0], pokecat[1], pokecat[2]);
						m_categoryButtons[i].setTooltipContent("Pokeballs");
						break;
					case 4:
						m_categoryButtons[i] = new ImageButton(tmscat[0], tmscat[1], tmscat[2]);
						m_categoryButtons[i].setTooltipContent("TMs");
						break;
				}
				m_items.put(i, new ArrayList<PlayerItem>());
				m_scrollIndex.put(i, 0);
				m_categoryButtons[i].setCanAcceptKeyboardFocus(false);
				m_categoryButtons[i].addCallback(new Runnable()
				{
					@Override
					public void run()
					{
						destroyPopup(root);
						m_curCategory = j;
						update();
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
		m_leftButton.setCanAcceptKeyboardFocus(false);
		m_leftButton.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				destroyPopup(root);
				int i = m_scrollIndex.get(m_curCategory) - 1;
				m_scrollIndex.remove(m_curCategory);
				m_scrollIndex.put(m_curCategory, i);
				update();
			}
		});
		add(m_leftButton);
		// Item Buttons and Stock Labels
		for(int i = 0; i < 4; i++)
		{
			final int j = i;
			// Starts the item buttons
			ImageButton item = new ImageButton();
			item.setCanAcceptKeyboardFocus(false);
			item.addCallback(new Runnable()
			{
				@Override
				public void run()
				{
					destroyPopup(root);
					useItem(j, root);
					update();
				}
			});
			m_itemBtns.add(item);
			add(item);
			// Starts the item labels
			Label stock = new Label();
			stock.setAlignment(Alignment.CENTER);
			m_stockLabels.add(stock);
			add(stock);
		}

		// Scrolling Button Right
		m_rightButton = new Button(">");
		m_rightButton.setCanAcceptKeyboardFocus(false);
		m_rightButton.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				destroyPopup(root);
				int i = m_scrollIndex.get(m_curCategory) + 1;
				m_scrollIndex.remove(m_curCategory);
				m_scrollIndex.put(m_curCategory, i);
				update();
			}
		});
		add(m_rightButton);
		// Close Button
		m_cancel = new Button("Close");

		m_cancel.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				destroyPopup(root);
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
				destroyPopup(root);
				closeBag();
			}
		});
		update();
		setTitle("Bag");
		setResizableAxis(ResizableAxis.NONE);
		setDraggable(true);
		setSize(m_categoryButtons.length * 80, 250);
		setVisible(true);
		setCenter();
	}

	private void loadBagIcons()
	{
		bagicons = new Image[] { GameClient.getInstance().getTheme().getImage("bag_topleft"), GameClient.getInstance().getTheme().getImage("bag_topright"),
				GameClient.getInstance().getTheme().getImage("bag_middle"), GameClient.getInstance().getTheme().getImage("bag_front"), GameClient.getInstance().getTheme().getImage("bag_bottom"),
				GameClient.getInstance().getTheme().getImage("bag_topleft") };
		bagicon = new org.pokemonium.client.ui.components.Image(bagicons[3]);
		add(bagicon);
	}

	/**
	 * Removes an item to the bag
	 * 
	 * @param id
	 * @param amount
	 */
	public void removeItem(int id, boolean remove, Widget root)
	{
		/* The remove variable indicates that this is the last of the item, and it should be removed from the inventory */
		if(remove)
		{
			for(PlayerItem item : GameClient.getInstance().getOurPlayer().getItems())
			{
				if(item.getNumber() == id)
				{
					// Potions and medicine
					if(item.getItem().getCategory().equalsIgnoreCase("Potions") || item.getItem().getCategory().equalsIgnoreCase("Medicine"))
					{
						m_items.get(1).remove(item);
					}
					else if(item.getItem().getCategory().equalsIgnoreCase("Food"))
					{
						m_items.get(2).remove(item);
					}
					else if(item.getItem().getCategory().equalsIgnoreCase("Pokeball"))
					{
						m_items.get(3).remove(item);
					}
					else if(item.getItem().getCategory().equalsIgnoreCase("TM"))
					{
						m_items.get(4).remove(item);
					}
					else
					{
						m_items.get(0).remove(item);
					}
				}
			}
			/* There is probably a better way to do the code below, but what essentially occurs is a re-initialization of the bag screen. Then the category is set back to the previous category. The effect this has for the user is, the item is instantly removed from the players bag screen when the last of the item is used. */
			int tmpCurCategory = m_curCategory;
			initGUI(root);
			loadItems();
			m_curCategory = tmpCurCategory;

		}
		update();
	}

	/**
	 * Centers the frame
	 */
	public void setCenter()
	{
		int height = (int) GameClient.getInstance().getGUIPane().getHeight();
		int width = (int) GameClient.getInstance().getGUIPane().getWidth();
		int x = width / 2 - 200;
		int y = height / 2 - 200;
		setSize(getWidth(), getHeight());
		setPosition(x, y);
	}

	public void update()
	{
		// Enable/disable scrolling
		if(m_scrollIndex.get(m_curCategory) == 0)
		{
			m_leftButton.setEnabled(false);
		}
		else
		{
			m_leftButton.setEnabled(true);
		}
		if(m_scrollIndex.get(m_curCategory) + 4 >= m_items.get(m_curCategory).size())
		{
			m_rightButton.setEnabled(false);
		}
		else
		{
			m_rightButton.setEnabled(true);
		}
		// Update items and stocks
		for(int i = 0; i < 4; i++)
		{
			try
			{
				m_itemBtns.get(i).setText(String.valueOf(m_items.get(m_curCategory).get(m_scrollIndex.get(m_curCategory) + i).getNumber()));
				m_itemBtns.get(i).setTooltipContent(
						m_items.get(m_curCategory).get(m_scrollIndex.get(m_curCategory) + i).getItem().getName() + "\n"
								+ m_items.get(m_curCategory).get(m_scrollIndex.get(m_curCategory) + i).getItem().getDescription());
				m_itemBtns.get(i).setImage(m_items.get(m_curCategory).get(m_scrollIndex.get(m_curCategory) + i).getBagImage());
				m_stockLabels.get(i).setText("x" + m_items.get(m_curCategory).get(m_scrollIndex.get(m_curCategory) + i).getQuantity());
				m_itemBtns.get(i).setEnabled(true);
			}
			catch(Exception e)
			{
				m_itemBtns.get(i).setImage(null);
				m_itemBtns.get(i).setTooltipContent("");
				m_itemBtns.get(i).setText("");
				m_stockLabels.get(i).setText("");
				m_itemBtns.get(i).setEnabled(false);
			}
		}
	}

	/**
	 * An item was used!
	 * 
	 * @param i
	 */
	public void useItem(int i, Widget root)
	{
		destroyPopup(root);
		if(m_curCategory == 3)
		// if(m_curCategory == 0 || m_curCategory == 3)
		{
			if(ItemDatabase.getInstance().getItem(Integer.valueOf(m_itemBtns.get(i).getText())).getCategory().equalsIgnoreCase("Field"))
			{
				m_popup = new ItemPopup(((String) m_itemBtns.get(i).getTooltipContent()).split("\n")[0], Integer.parseInt(m_itemBtns.get(i).getText()), false, false, root);
			}
			else
			{
				m_popup = new ItemPopup(((String) m_itemBtns.get(i).getTooltipContent()).split("\n")[0], Integer.parseInt(m_itemBtns.get(i).getText()), true, false, root);
			}

			m_popup.setPopupPosition(m_itemBtns.get(i).getInnerX(), m_itemBtns.get(i).getInnerY() + m_itemBtns.get(i).getHeight() - 48);

		}
		else
		{
			m_popup = new ItemPopup(((String) m_itemBtns.get(i).getTooltipContent()).split("\n")[0], Integer.parseInt(m_itemBtns.get(i).getText()), true, false, root);
			m_popup.setPopupPosition(m_itemBtns.get(i).getInnerX(), m_itemBtns.get(i).getInnerY() + m_itemBtns.get(i).getHeight() - 48);
		}
	}

	private void loadItems()
	{
		try
		{
			// Load the player's items and sort them by category
			for(PlayerItem item : GameClient.getInstance().getOurPlayer().getItems())
			{
				// Field items
				if(item.getItem().getCategory().equalsIgnoreCase("Field") || item.getItem().getCategory().equalsIgnoreCase("Evolution") || item.getItem().getCategory().equalsIgnoreCase("Held"))
				{
					m_items.get(0).add(item);
				}
				else if(item.getItem().getCategory().equalsIgnoreCase("Potions") || item.getItem().getCategory().equalsIgnoreCase("Medicine")
						|| item.getItem().getCategory().equalsIgnoreCase("Vitamins"))
				{
					m_items.get(1).add(item);
				}
				else if(item.getItem().getCategory().equalsIgnoreCase("Food"))
				{
					m_items.get(2).add(item);
				}
				else if(item.getItem().getCategory().equalsIgnoreCase("Pokeball"))
				{
					m_items.get(3).add(item);
				}
				else if(item.getItem().getCategory().equalsIgnoreCase("TM"))
				{
					m_items.get(4).add(item);
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("item trouble");
			e.printStackTrace();
		}
	}

	@Override
	public void layout()
	{
		super.layout();
		for(int i = 0; i < m_categoryButtons.length; i++)
		{
			m_categoryButtons[i].setSize(40, 40);
			if(i == 0)
			{
				m_categoryButtons[i].setPosition(80 + getInnerX(), 37 + getInnerY());
			}
			else
			{
				m_categoryButtons[i].setPosition(m_categoryButtons[i - 1].getX() + 65, m_categoryButtons[i - 1].getY());
			}
		}
		bagicon.setPosition(10 + getInnerX(), 30 + getInnerY());
		for(int i = 0; i < m_itemBtns.size(); i++)
		{
			Button btn = m_itemBtns.get(i);
			btn.setSize(60, 60);
			btn.setPosition(50 + 80 * i + getInnerX(), 95 + getInnerY());
		}

		int idx = 0;
		for(Label stockLabel : m_stockLabels)
		{
			stockLabel.setSize(60, 40);
			stockLabel.setPosition(50 + 80 * idx + getInnerX(), 145 + getInnerY());
			idx++;
		}
		m_leftButton.setSize(20, 40);
		m_leftButton.setPosition(15 + getInnerX(), 105 + getInnerY());
		m_rightButton.setSize(20, 40);
		m_rightButton.setPosition(365 + getInnerX(), 105 + getInnerY());
		m_cancel.setSize(400, 32);
		m_cancel.setPosition(0 + getInnerX(), 218 + getInnerY());
	}
}

/**
 * The use dialog for items
 * 
 * @author Myth1c
 */
class ItemPopup extends Widget
{
	private Button m_use;
	private Button m_cancel;
	private Button m_destroy;
	private Button m_give;
	private Label m_name;
	private TeamPopup m_team;

	private boolean isInBattle;
	private boolean usedOnPokemon;
	private int itemID;

	private PopupWindow popup;
	private Widget rootWidget;
	private Runnable usedCallback;

	/**
	 * Default Constructor
	 * 
	 * @param item
	 * @param id
	 * @param useOnPokemon
	 * @param isBattle
	 */
	public ItemPopup(String item, int id, boolean useOnPokemon, boolean isBattle, Widget root)
	{
		rootWidget = root;
		itemID = id;
		usedOnPokemon = useOnPokemon;
		isInBattle = isBattle;

		// Item name label
		m_name = new Label(item.split("\n")[0]);
		add(m_name);

		// Use button
		m_use = new Button("Use");
		m_use.setCanAcceptKeyboardFocus(false);
		m_use.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				useItem(itemID, usedOnPokemon, isInBattle);
			}
		});
		add(m_use);

		m_give = new Button("Give");
		m_give.setCanAcceptKeyboardFocus(false);
		m_give.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				giveItem(itemID);
			}
		});
		if(isBattle)
		{
			m_give.setEnabled(false);
		}
		add(m_give);

		// Destroy the item
		m_destroy = new Button("Drop");
		m_destroy.setCanAcceptKeyboardFocus(false);
		m_destroy.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				ClientMessage message = new ClientMessage(ServerPacket.ITEM_DESTROY);
				message.addInt(itemID);
				GameClient.getInstance().getSession().send(message);
				destroyPopup();
			}
		});
		add(m_destroy);

		// Close the popup
		m_cancel = new Button("Cancel");
		m_cancel.setCanAcceptKeyboardFocus(false);
		m_cancel.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				destroyPopup();
			}
		});
		add(m_cancel);

		setVisible(true);

		popup = new PopupWindow(root);
		popup.setTheme("bigbagdialogpopup");
		popup.add(this);
		popup.setCloseOnClickedOutside(true);
		popup.setCloseOnEscape(true);
		popup.openPopup();
	}

	public void setPopupPosition(int x, int y)
	{
		popup.setPosition(x, y);
	}

	/**
	 * Destroys the popup
	 */
	public void destroyPopup()
	{
		if(m_team != null)
		{
			m_team.destroy();
			m_team = null;
		}
		removeChild(this);
		popup.closePopup();
		popup.destroy();
	}

	/**
	 * Give the item to a pokemon
	 * 
	 * @param id
	 */
	public void giveItem(int id)
	{
		if(m_team != null)
		{
			m_team.destroy();
		}
		m_team = new TeamPopup(this, id, false, false, rootWidget);
		m_team.setPopupPosition(popup.getInnerX() + popup.getWidth() - 1, m_give.getInnerY() - 15);
	}

	/**
	 * Use the item. usedOnPoke determine whether the item should be applied to a pokemon
	 * 
	 * @param id
	 * @param usedOnPoke
	 */
	public void useItem(int id, boolean usedOnPoke, boolean isBattle)
	{
		if(m_team != null)
		{
			m_team.destroy();
		}
		if(usedOnPoke)
		{
			m_team = new TeamPopup(this, id, true, isBattle, rootWidget);
			m_team.setPopupPosition(popup.getInnerX() + popup.getWidth() - 1, m_use.getInnerY() - 15);
			if(usedCallback != null)
			{
				m_team.setItemUsedCallback(usedCallback);
			}
		}
		else
		{
			ClientMessage message = new ClientMessage(ServerPacket.ITEM_USE);
			message.addString(String.valueOf(id));
			GameClient.getInstance().getSession().send(message);
			destroyPopup();
		}
	}

	@Override
	public void layout()
	{
		m_name.setPosition(getInnerX() + getWidth() / 2 - m_name.computeTextWidth() / 2, getInnerY() + m_name.computeTextHeight());
		m_use.setSize(100, 25);
		m_use.setPosition(getInnerX() + 5, m_name.getInnerY() + m_name.computeTextHeight() + 3);
		m_give.setSize(100, 25);
		m_give.setPosition(getInnerX() + 5, m_use.getInnerY() + 25);
		m_destroy.setSize(100, 25);
		// if(!isInBattle)
		// {
		m_destroy.setPosition(getInnerX() + 5, m_give.getInnerY() + 25);
		setSize(110, 140);
		popup.setSize(110, 140);
		// }
		// else
		// {
		// m_destroy.setPosition(0, m_use.getY() + 25);
		// setSize(100, 115);
		// }
		m_cancel.setSize(100, 25);
		m_cancel.setPosition(getInnerX() + 5, m_destroy.getInnerY() + 25);

		// popup.adjustSize();

	}

	public void setItemUsedCallback(Runnable callback)
	{
		usedCallback = callback;
	}
}

/**
 * PopUp that lists the player's pokemon in order to use/give an item
 * 
 * @author Myth1c
 */
class TeamPopup extends Widget
{
	private Button[] pokeButtons;
	private ItemPopup m_parent;
	private int item;
	private boolean use;
	private boolean isBattle;

	private PopupWindow popup;
	private Runnable itemUsedCallback;

	/**
	 * Default constructor
	 * 
	 * @param itemId
	 * @param use
	 * @param useOnPoke
	 */
	public TeamPopup(ItemPopup parent, int itemId, boolean isuse, boolean isbattle, Widget root)
	{
		setPosition(getX() - 1, getY() + 1);

		m_parent = parent;
		item = itemId;
		use = isuse;
		isBattle = isbattle;
		pokeButtons = new Button[GameClient.getInstance().getOurPlayer().getPokemon().length];
		for(int i = 0; i < GameClient.getInstance().getOurPlayer().getPartyCount(); i++)
		{
			final int idx = i;
			pokeButtons[i] = new Button(GameClient.getInstance().getOurPlayer().getPokemon()[i].getName());
			pokeButtons[i].setCanAcceptKeyboardFocus(false);
			pokeButtons[i].addCallback(new Runnable()
			{
				@Override
				public void run()
				{
					destroyPopup();
					processItemUse(use, item, idx, isBattle);
				}
			});
			add(pokeButtons[i]);
		}

		// Frame configuration
		setVisible(true);

		popup = new PopupWindow(root);
		popup.setTheme("itemuseteampopup");
		popup.add(this);
		popup.setCloseOnClickedOutside(true);
		popup.setCloseOnEscape(true);
		popup.openPopup();
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
			if(itemUsedCallback != null)
			{
				itemUsedCallback.run();
			}
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

	public void destroyPopup()
	{
		popup.closePopup();
		popup.destroy();
	}

	public void setPopupPosition(int x, int y)
	{
		popup.setPosition(x, y);
	}

	@Override
	public void layout()
	{
		int y = 10;
		for(int i = 0; i < GameClient.getInstance().getOurPlayer().getPartyCount(); i++)
		{
			pokeButtons[i].setSize(100, 25);
			pokeButtons[i].setPosition(getInnerX() + 5, getInnerY() + y);
			y += 25;
		}

		setSize(110, GameClient.getInstance().getOurPlayer().getPartyCount() * 25 + 20);
	}

	public void setItemUsedCallback(Runnable callback)
	{
		itemUsedCallback = callback;
	}
}