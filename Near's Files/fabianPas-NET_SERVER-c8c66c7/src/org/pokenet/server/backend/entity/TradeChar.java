package org.pokenet.server.backend.entity;

import org.pokenet.server.battle.Pokemon;
import org.pokenet.server.protocol.ServerMessage;

/**
 * Represents an NPC that wants to trade Pokemon
 * @author shadowkanji
 *
 */
public class TradeChar extends NonPlayerChar implements Tradeable {
	private Trade m_trade = null;
	private boolean m_tradeAccepted = false;
	private PlayerChar m_player;
	/*
	 * Requested Pokemon data
	 */
	private String m_requestedSpecies = "";
	/*
	 * Offered Pokemon data
	 */
	private Pokemon [] m_party;
	
	/**
	 * Constructor
	 */
	public TradeChar() {
		setBadge(-1);
		setHealer(false);
		setPartySize(0);
	}
	
	/**
	 * Sets the Pokemon the NPC wants
	 * @param species
	 * @param level
	 * @param nature
	 */
	public void setRequestedPokemon(String species, int level, String nature) {
		m_requestedSpecies = species;
		//TODO: Add support for levels and natures
	}
	
	/**
	 * Sets the Pokemon the NPC offers
	 * @param species
	 * @param level
	 */
	public void setOfferedSpecies(String species, int level) {
		m_party = new Pokemon[1];
		m_party[0] = Pokemon.getRandomPokemon(species, level);
	}
	
	public void talkToPlayer(PlayerChar p) {
		m_player = p;
		if(m_trade == null) {
			/* Can trade */
			m_trade = new Trade(this, p);
			p.setTrade(m_trade);
			m_trade.setOffer(this, 0, 0);
			
			ServerMessage message = new ServerMessage();
			message.Init(50);
			message.addInt(3);
			message.addString("I'm looking for a " + m_requestedSpecies +
							". Want to trade one for my " + m_party[0].getName() + "?");
			m_player.getSession().Send(message);
		} else {
			/* Can't trade */			
			ServerMessage message = new ServerMessage();
			message.Init(50);
			message.addInt(3);
			message.addString("I can't trade with you right now");
			m_player.getSession().Send(message);
		}
	}

	public boolean acceptedTradeOffer() {
		return m_tradeAccepted;
	}

	public void cancelTrade() {
		m_trade.endTrade();
	}

	public void cancelTradeOffer() {}

	public void finishTrading() {
		m_trade = null;
		m_tradeAccepted = false;
		
		ServerMessage message = new ServerMessage();
		message.Init(50);
		message.addInt(3);
		message.addString("Thanks! It's just what I was looking for!");
		m_player.getSession().Send(message);
	}

	public int getMoney() {
		return 999999;
	}

	public Pokemon[] getParty() {
		return m_party;
	}

	public Trade getTrade() {
		return m_trade;
	}

	public boolean isTrading() {
		return m_trade != null;
	}

	public void receiveTradeOffer(TradeOffer[] o) {
		if(o[0].getInformation().equalsIgnoreCase(m_requestedSpecies)) {
			//This is the Pokemon the TradeChar wanted
			setTradeAccepted(true);
		} else {
			//This is the wrong Pokemon
			ServerMessage message = new ServerMessage();
			message.Init(50);
			message.addInt(3);
			message.addString("This is not what I'm looking for!\n" +
					"Come back when you find the right Pokemon!");
			m_player.getSession().Send(message);
			
		}
	}

	public void receiveTradeOfferCancelation() {}

	public void setMoney(int money) {}

	public void setTrade(Trade t) {
		m_trade = t;
	}

	public void setTradeAccepted(boolean b) {
		m_tradeAccepted = b;
		if(b) {
			m_trade.checkForExecution();
		}
	}

	public String getIpAddress() {
		return "";
	}
}
