package org.pokenet.client.backend.entity;

import java.util.ArrayList;
import java.util.List;
import org.pokenet.client.backend.ItemDatabase;
import org.pokenet.client.backend.ItemSpriteDatabase;
import de.matthiasmann.twl.renderer.Image;

public class PlayerItem
{
	private Image m_bagImage;

	private Item m_item;

	private int m_number;

	private int m_quantity;

	/**
	 * Default constructor
	 * 
	 * @param number
	 * @param quantity
	 */
	public PlayerItem(int number, int quantity)
	{
		m_number = number;
		m_quantity = quantity;
		m_item = getItem(m_number);
		String respath = System.getProperty("res.path");
		if(respath == null)
			respath = "";
		try
		{

			if(m_item.getCategory().equalsIgnoreCase("TM"))
			{
				m_bagImage = ItemSpriteDatabase.getTM48();
			}
			else
			{
				m_bagImage = ItemSpriteDatabase.getItemsprite48(m_item.getId());
			}
		}
		catch(Exception e)
		{
			try
			{
				m_bagImage = ItemSpriteDatabase.getItemsprite48(0);
			}
			catch(Exception e2)
			{
				e2.printStackTrace();
			}
		}
	}

	public static List<Item> generateFieldItems()
	{
		List<Item> m_items = new ArrayList<Item>();
		m_items = ItemDatabase.getCategoryItems("Field");
		return m_items;
	}

	public static List<Item> generatePokeballs()
	{
		List<Item> m_items = new ArrayList<Item>();
		m_items = ItemDatabase.getCategoryItems("Pokeball");
		return m_items;
	}

	public static List<Item> generatePotions()
	{
		List<Item> m_items = new ArrayList<Item>();
		m_items = ItemDatabase.getCategoryItems("Potions");
		return m_items;
	}

	public static List<Item> generateStatusHeals()
	{
		List<Item> m_items = new ArrayList<Item>();
		m_items = ItemDatabase.getCategoryItems("Medicine");
		return m_items;
	}

	/**
	 * Returns the item based on its item number
	 * 
	 * @param number
	 * @return
	 */
	public static Item getItem(int number)
	{
		Item item = ItemDatabase.getInstance().getItem(number);
		return item;
	}

	public Image getBagImage()
	{
		return m_bagImage;
	}

	public Item getItem()
	{
		return m_item;
	}

	public int getNumber()
	{
		return m_number;
	}

	public int getQuantity()
	{
		return m_quantity;
	}

	public void setItem(Item item)
	{
		m_item = item;
	}

	public void setNumber(int m_number)
	{
		this.m_number = m_number;
	}

	public void setQuantity(int m_quantity)
	{
		this.m_quantity = m_quantity;
	}
}