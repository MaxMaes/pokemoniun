package org.pokenet.client.messages.events;

import java.util.HashMap;
import org.pokenet.client.GameClient;
import org.pokenet.client.Session;
import org.pokenet.client.messages.MessageEvent;
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.protocol.ServerMessage;

public class ShopListEvent implements MessageEvent
{

	@Override
	public void Parse(Session Session, ServerMessage Request, ClientMessage Message)
	{
		HashMap<Integer, Integer> stock = new HashMap<Integer, Integer>();
		String[] merchData = Request.readString().split(",");
		for(int i = 0; i < merchData.length; i++)
		{
			String[] tempStockData = merchData[i].split(":");
			stock.put(Integer.parseInt(tempStockData[0]), Integer.parseInt(tempStockData[1]));
		}
		GameClient.getInstance().getUi().startShop(stock);
	}
}
