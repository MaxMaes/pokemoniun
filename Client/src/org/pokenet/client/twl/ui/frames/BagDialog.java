package org.pokenet.client.twl.ui.frames;

import java.util.ArrayList;
import java.util.List;
import org.pokenet.client.GameClient;
import org.pokenet.client.backend.FileLoader;
import org.pokenet.client.backend.entity.PlayerItem;
import org.pokenet.client.twl.ui.base.ImageButton;
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
	private ImageButton[] m_itemButtons;
	private List<PlayerItem> m_items;

	/**
	 * Default Constructor
	 * 
	 * @param bag
	 */
	public BagDialog(ArrayList<PlayerItem> bag, Runnable cancel, Runnable bagOpen)
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

		initGUI(cancel, bagOpen);
	}

	/**
	 * Initializes the interface
	 */
	public void initGUI(Runnable cancel, Runnable openBag)
	{
		setClip(true);
		m_itemButtons = new ImageButton[m_items.size()];
		String respath = System.getProperty("res.path");
		if(respath == null)
			respath = "";
		for(int i = 0; i < m_items.size(); i++)
		{
			final int j = i;
			Image img = FileLoader.loadImage(respath + "res/items/24/" + m_items.get(i).getNumber() + ".png");
			m_itemButtons[i] = new ImageButton(img, "       x" + m_items.get(i).getQuantity());
			m_itemButtons[i].setTooltipContent(m_items.get(i).getItem().getName() + "\n" + m_items.get(i).getItem().getDescription());
			m_itemButtons[i].setImagePosition(5, 5);

			m_itemButtons[i].addCallback(new Runnable()
			{
				@Override
				public void run()
				{
					itemClicked(m_items.get(j)); // TODO: this has to be done different I guess. Don't like the final int j.
				}
			});
			add(m_itemButtons[i]);
		}
		m_bag = new Button("Bag");
		m_bag.setTooltipContent("Opens the Bag to see your items");
		m_bag.addCallback(cancel);
		add(m_bag);

		m_cancel = new Button("Cancel");
		m_cancel.setTooltipContent("Closes this dialog");
		m_cancel.addCallback(cancel);
		add(m_cancel);
		setVisible(true);
	}

	/**
	 * Handles events on click
	 * 
	 * @param item
	 */
	public abstract void itemClicked(PlayerItem item);

	@Override
	public void layout()
	{
		for(int i = 0; i < m_itemButtons.length; i++)
		{
			if(i > 0)
				m_itemButtons[i].setPosition(getInnerX(), m_itemButtons[i - 1].getY() + m_itemButtons[i - 1].getHeight());
			m_itemButtons[i].setSize(60, 30);
		}

		m_cancel.setSize(60, 20);
		m_cancel.setPosition(m_itemButtons[m_itemButtons.length - 1].getX(), m_itemButtons[m_itemButtons.length - 1].getY() + m_itemButtons[m_itemButtons.length - 1].getHeight());
		m_bag.setSize(60, 40);
		m_bag.setPosition(m_cancel.getX(), m_cancel.getY() + m_cancel.getHeight());
	}
}
