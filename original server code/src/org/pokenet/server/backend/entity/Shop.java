package org.pokenet.server.backend.entity;

import java.util.HashMap;
import java.util.Iterator;

import org.pokenet.server.GameServer;
import org.pokenet.server.Log;

/**
 * Handles shops. Stored internally in npcs with a shop attribute.
 * @author shadowkanji
 *
 */
public class Shop {
	private HashMap<Integer, Integer> m_stock;
	private HashMap<Integer, Integer> m_prices;
	/*
	 * Delta represents how often the stock should be updated
	 * 
	 * As players buy more stock from this shop,
	 * delta is decreased and restocks occur more quickly
	 * 
	 * If this shop is rarely used, it is rarely restocked
	 */
	private long lastStockUpdate;
	
	public Shop(int type) {
		m_stock = new HashMap<Integer, Integer>();
		m_prices = new HashMap<Integer, Integer>();
		/*
		 * Generate all the item stocks amd prices
		 */
	    for (int i : GameServer.getServiceManager().getItemDatabase().getShopItems(type)){
	    	m_stock.put(i, 100);
	    	m_prices.put(i, GameServer.getServiceManager().getItemDatabase().getItem(i).getPrice());
	    }
	}

	/**
	 * Updates stock levels
	 */
	public void updateStock() {
		if(lastStockUpdate < (System.currentTimeMillis() - (30 * 60 * 1000))) { // We update the stock every 30 minutes
			Iterator<Integer> it = m_stock.keySet().iterator();
			int s;
			while(it.hasNext()) {
				s = it.next();
				int q = m_stock.get(s);
				q = q + 25 <= 100 ? q + 25 : 100;
				m_stock.put(s, q);
			}
			lastStockUpdate = System.currentTimeMillis();
		}
	}
	
	/**
	 * Returns a string of stock data to be sent to the client
	 * @return
	 */
	public String getStockData() {
		String result = "";
		for (int i : m_stock.keySet())
			result = result + i + ":" + m_stock.get(i) + ",";
		/*
		 * Return the data string without the trailing comma
		 */
		return result.substring(0, result.length() - 1);
	}
	
	/**
	 * Returns the price of an item
	 * @param itemName
	 * @return
	 */
	public int getPriceForItem(int itemid) {	
		return m_prices.get(itemid);
	}
	
	/**
	 * Returns the id of the item bought. -1 if there was no item in stock
	 * @param itemName
	 * @param quantity
	 * @return
	 */
	public boolean buyItem(int itemId, int quantity) {
		int stock = 0;
		stock = m_stock.get(itemId);
		if(stock - quantity > 0) {
			m_stock.put(itemId, (stock - quantity));
			return true;
		}
		return false;
	}
	
	/**
	 * Returns the amount the item was sold for
	 * @param itemId
	 * @param quantity
	 * @return
	 */
	public int sellItem(int itemId, int quantity) {
		return ((GameServer.getServiceManager().
				getItemDatabase().getItem(itemId).getPrice() / 2) * quantity);
	}
}
