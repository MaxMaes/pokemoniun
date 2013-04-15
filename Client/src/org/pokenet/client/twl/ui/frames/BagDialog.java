package org.pokenet.client.twl.ui.frames;

import java.util.ArrayList;
import java.util.List;

import org.pokenet.client.GameClient;
import org.pokenet.client.backend.FileLoader;
import org.pokenet.client.backend.entity.PlayerItem;
import org.pokenet.client.ui.base.TWLImageButton;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.renderer.Image;

/**
 * Bag dialog
 * 
 * @author Myth1c
 */
public abstract class BagDialog extends ResizableFrame
{
	private Button m_bag;
	private Button m_cancel;
	private TWLImageButton[] m_itemButtons;
	private List<PlayerItem> m_items;

	/**
	 * Default Constructor
	 * 
	 * @param bag
	 */
	public BagDialog(ArrayList<PlayerItem> bag)
	{
		m_items = new ArrayList<PlayerItem>();
		// Assign Potion Fave
		if(GameClient.getInstance().getOurPlayer().getItemQuantity(4) > 0)
			m_items.add(new PlayerItem(4, GameClient.getInstance().getOurPlayer().getItemQuantity(4)));
		else if(GameClient.getInstance().getOurPlayer().getItemQuantity(3) > 0)
			m_items.add(new PlayerItem(3, GameClient.getInstance().getOurPlayer().getItemQuantity(3)));
		else if(GameClient.getInstance().getOurPlayer().getItemQuantity(2) > 0)
			m_items.add(new PlayerItem(2, GameClient.getInstance().getOurPlayer().getItemQuantity(2)));
		else
			m_items.add(new PlayerItem(1, GameClient.getInstance().getOurPlayer().getItemQuantity(1)));

		// Assign Antidote Fave
		if(GameClient.getInstance().getOurPlayer().getItemQuantity(5) > 0)
			m_items.add(new PlayerItem(5, GameClient.getInstance().getOurPlayer().getItemQuantity(5)));
		else if(GameClient.getInstance().getOurPlayer().getItemQuantity(21) > 0)
			m_items.add(new PlayerItem(21, GameClient.getInstance().getOurPlayer().getItemQuantity(21)));
		else
			m_items.add(new PlayerItem(16, GameClient.getInstance().getOurPlayer().getItemQuantity(16)));

		// Assign Repel Fave
		if(GameClient.getInstance().getOurPlayer().getItemQuantity(87) > 0)
			m_items.add(new PlayerItem(87, GameClient.getInstance().getOurPlayer().getItemQuantity(87)));
		else if(GameClient.getInstance().getOurPlayer().getItemQuantity(86) > 0)
			m_items.add(new PlayerItem(86, GameClient.getInstance().getOurPlayer().getItemQuantity(86)));
		else
			m_items.add(new PlayerItem(85, GameClient.getInstance().getOurPlayer().getItemQuantity(85)));

		// Assign EscapeRope Fave
		m_items.add(new PlayerItem(91, GameClient.getInstance().getOurPlayer().getItemQuantity(91)));

		// Assign PokeBall Fave
		if(GameClient.getInstance().getOurPlayer().getItemQuantity(38) > 0)
			m_items.add(new PlayerItem(38, GameClient.getInstance().getOurPlayer().getItemQuantity(38)));
		else if(GameClient.getInstance().getOurPlayer().getItemQuantity(37) > 0)
			m_items.add(new PlayerItem(37, GameClient.getInstance().getOurPlayer().getItemQuantity(37)));
		else if(GameClient.getInstance().getOurPlayer().getItemQuantity(36) > 0)
			m_items.add(new PlayerItem(36, GameClient.getInstance().getOurPlayer().getItemQuantity(36)));
		else
			m_items.add(new PlayerItem(35, GameClient.getInstance().getOurPlayer().getItemQuantity(35)));

		initGUI();
	}

	/**
	 * Handles cancelation
	 */
	public abstract void cancelled();

	/**
	 * Initializes the interface
	 */
	public void initGUI()
	{
		m_itemButtons = new TWLImageButton[m_items.size()];
		String respath = System.getProperty("res.path");
		if(respath == null)
			respath = "";
		for(int i = 0; i < m_items.size(); i++)
		{
			final int j = i;
			Image img = FileLoader.loadImage(respath + "res/items/24/" + m_items.get(i).getNumber() + ".png");
			m_itemButtons[i] = new TWLImageButton(img);
			m_itemButtons[i].setText("       x" + m_items.get(i).getQuantity());
			m_itemButtons[i].setTooltipContent(m_items.get(i).getItem().getName() + "\n" + m_items.get(i).getItem().getDescription());

			m_itemButtons[i].addCallback(new Runnable()
			{
				@Override
				public void run()
				{
					itemClicked(m_items.get(j));
				}
			});
			add(m_itemButtons[i]);
		}
		m_bag = new Button("Bag");
		m_bag.setTooltipContent("Opens the Bag to see your items");
		m_bag.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				loadBag();
			}
		});
		add(m_bag);

		m_cancel = new Button("Cancel");
		m_cancel.setTooltipContent("Closes this dialog");
		m_cancel.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				cancelled();
			}
		});
		add(m_cancel);
		pack();
		setVisible(true);
	}

	/**
	 * Handles events on click
	 * 
	 * @param item
	 */
	public abstract void itemClicked(PlayerItem item);

	/**
	 * Handles loading Big Bag
	 */
	public abstract void loadBag();

	/**
	 * Resizes items for optimal size
	 */
	public void pack()
	{
		m_cancel.setSize(getWidth(), 20);
		m_cancel.setPosition(getHeight() - 20, 0);
		m_bag.setSize(getWidth(), 40);
		m_bag.setPosition(0, m_cancel.getY() - 40);
		for(int i = 0; i < m_itemButtons.length; i++)
		{
			if(i > 0)
				m_itemButtons[i].setPosition(0, m_itemButtons[i - 1].getY() + m_itemButtons[i - 1].getHeight());
			m_itemButtons[i].setSize(getWidth(), (getHeight() - 60) / m_items.size());
		}
	}
}
