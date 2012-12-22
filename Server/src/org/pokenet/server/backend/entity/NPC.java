package org.pokenet.server.backend.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import org.pokenet.server.battle.DataService;
import org.pokenet.server.battle.Pokemon;
import org.pokenet.server.battle.impl.NpcBattleField;
import org.pokenet.server.battle.impl.NpcSleepTimer;
import org.pokenet.server.protocol.ServerMessage;
import org.pokenet.server.backend.entity.Positionable.Direction;

/**
 * Represents a Non Playable Character
 * 
 * @author shadowkanji
 */
public class NPC extends Character
{
	private int m_badge = -1;
	private boolean m_isBox = false;
	private boolean m_isHeal = false;
	private int m_isShop = 0;
	private long m_lastBattle = 0;
	private int m_minPartySize = 1;
	/* Trainers can have an more than 6 possible Pokemon.
	 * When a battle is started with this NPC, it'll check the min party size.
	 * If you have a party bigger than min party,
	 * it'll generate a party of random size between minParty and your party size + 1.
	 * (Unless your party size is 6) */
	private HashMap<String, Integer> m_possiblePokemon;
	private Shop m_shop = null;
	private final ArrayList<Integer> m_speech = new ArrayList<Integer>();

	/**
	 * Constructor
	 */
	public NPC()
	{
	}

	/**
	 * Adds speech to this npc
	 * 
	 * @param id
	 */
	public void addSpeech(int id)
	{
		m_speech.add(id);
	}

	/**
	 * Returns true if this NPC can battle
	 * 
	 * @return
	 */
	public boolean canBattle()
	{
		return m_lastBattle == 0;
	}

	/**
	 * Returns true if this npc can see the player
	 * 
	 * @param p
	 * @return
	 */
	public boolean canSee(Player p)
	{
		if(!p.isBattling() && !isGymLeader() && canBattle())
		{
			Random r = new Random();
			switch(getFacing())
			{
				case Up:
					if(p.getY() >= getY() - 32 * (r.nextInt(4) + 1))
						return true;
					break;
				case Down:
					if(p.getY() <= getY() + 32 * (r.nextInt(4) + 1))
						return true;
					break;
				case Left:
					if(p.getX() >= getX() - 32 * (r.nextInt(4) + 1))
						return true;
					break;
				case Right:
					if(p.getX() <= getX() + 32 * (r.nextInt(4) + 1))
						return true;
					break;
			}
		}
		return false;
	}

	/**
	 * Challenges a player (NOTE: Should only be called from NpcBattleLauncher)
	 * 
	 * @param p
	 */
	public void challengePlayer(Player p)
	{
		String speech = getSpeech();
		if(!speech.equalsIgnoreCase(""))
		{
			// TcpProtocolHandler.writeMessage(p.getTcpSession(), new NpcSpeechMessage(speech));
			ServerMessage message = new ServerMessage();
			message.Init(50);
			message.addInt(2);
			message.addString(speech);
			p.getSession().Send(message);
		}
	}

	/**
	 * Returns the number of the badge this npc gives. -1 if no badge.
	 * 
	 * @return
	 */
	public int getBadge()
	{
		return m_badge;
	}

	/**
	 * Returns the time this NPC last battled
	 * NOTE: Is valued 0 if the NPC is able to battle
	 * 
	 * @return
	 */
	public long getLastBattleTime()
	{
		return m_lastBattle;
	}

	/**
	 * Returns a dynamically generated Pokemon party based on how well trained a player is
	 * 
	 * @param p
	 * @return
	 */
	public Pokemon[] getParty(Player p)
	{
		Pokemon[] party = new Pokemon[6];
		Pokemon poke;
		int level;
		String name;
		Random r = DataService.getBattleMechanics().getRandom();
		if(isGymLeader())
		{
			if(p.getBadgeCount() > 7)
				/* If a player has 8 badges, level 80s all round */
				for(int i = 0; i < 6; i++)
				{
					name = (String) m_possiblePokemon.keySet().toArray()[r.nextInt(m_possiblePokemon.keySet().size())];
					level = 80;
					poke = Pokemon.getRandomPokemon(name, level);
					party[i] = poke;
				}
			else
				/* If a player hasn't got 8 badges, give them normal levels */
				for(int i = 0; i < m_minPartySize; i++)
				{
					name = (String) m_possiblePokemon.keySet().toArray()[r.nextInt(m_possiblePokemon.keySet().size())];
					level = m_possiblePokemon.get(name);
					poke = Pokemon.getRandomPokemon(name, level);
					party[i] = poke;
				}
		}
		else
		{
			int playerPartySize = p.getPartyCount();
			if(m_minPartySize < playerPartySize && m_possiblePokemon.size() >= playerPartySize + 1)
			{
				/* The player has more Pokemon, generate a random party */
				/* First, get a random party size that is greater than m_minPartySize
				 * and less than or equal to the amount of pokemon in the player's party + 1 */
				int pSize = r.nextInt(playerPartySize + 1 > 6 ? 6 : playerPartySize + 1);
				while(pSize < m_minPartySize)
					pSize = r.nextInt(playerPartySize + 1 > 6 ? 6 : playerPartySize + 1);
				/* Now generate the random Pokemon */
				for(int i = 0; i <= pSize; i++)
				{
					// Select a random Pokemon
					name = (String) m_possiblePokemon.keySet().toArray()[r.nextInt(m_possiblePokemon.keySet().size())];
					level = m_possiblePokemon.get(name);
					/* Level scaling */
					/* while(level < p.getHighestLevel() - 3) {
					 * level = r.nextInt(p.getHighestLevel() + 5);
					 * } */
					poke = Pokemon.getRandomPokemon(name, level);
					party[i] = poke;
				}
			}
			else
				/* Generate a party of size m_minPartySize */
				for(int i = 0; i < m_minPartySize; i++)
				{
					// Select a random Pokemon from this list of possible Pokemons
					name = (String) m_possiblePokemon.keySet().toArray()[r.nextInt(m_possiblePokemon.keySet().size())];
					level = m_possiblePokemon.get(name);
					// Ensure levels are the similiar
					/* while(level < p.getHighestLevel() - 3) {
					 * level = r.nextInt(p.getHighestLevel() + 5);
					 * } */
					poke = Pokemon.getRandomPokemon(name, level);
					party[i] = poke;
				}
		}
		return party;
	}

	/**
	 * Returns true if this npc allows box access
	 * 
	 * @return
	 */
	public boolean isBox()
	{
		return m_isBox;
	}

	/**
	 * Returns true if the npc is a gym leader
	 * 
	 * @return
	 */
	public boolean isGymLeader()
	{
		return m_badge > -1;
	}

	/**
	 * Return true if this npc heals your pokemon
	 * 
	 * @return
	 */
	public boolean isHealer()
	{
		return m_isHeal;
	}

	/**
	 * Returns true if this npc is a shop keeper
	 * 
	 * @return
	 */
	public boolean isShopKeeper()
	{
		if(m_isShop > 0)
			return true;
		return false;
	}

	/**
	 * Returns true if an NPC is a trainer
	 * 
	 * @return
	 */
	public boolean isTrainer()
	{
		return m_possiblePokemon != null && m_minPartySize > 0 && m_possiblePokemon.size() > 0;
	}

	public void resetLastBattleTime()
	{
		m_lastBattle = 0;
	}

	/**
	 * Sets the badge this npc gives, if any
	 * 
	 * @param i
	 */
	public void setBadge(int i)
	{
		m_badge = i;
	}

	/**
	 * Sets if this npc allows box access
	 * 
	 * @param b
	 */
	public void setBox(boolean b)
	{
		m_isBox = b;
	}

	/**
	 * Sets if this npc is a healer or not
	 * 
	 * @param b
	 */
	public void setHealer(boolean b)
	{
		m_isHeal = b;
	}

	/**
	 * Sets the minimum sized party this npc should have
	 * 
	 * @param size
	 */
	public void setPartySize(int size)
	{
		m_minPartySize = size > 6 ? 6 : size;
	}

	/**
	 * Sets the possible Pokemon this trainer can have
	 * 
	 * @param pokes
	 */
	public void setPossiblePokemon(HashMap<String, Integer> pokes)
	{
		m_possiblePokemon = pokes;
	}

	/**
	 * Sets if this npc is a shop keeper
	 * 
	 * @param b
	 */
	public void setShopKeeper(int b)
	{
		m_isShop = b;
		if(b > 0)
			try
			{
				m_shop = new Shop(b);
				m_shop.start();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
	}

	/**
	 * Talks to a player
	 * 
	 * @param p
	 */
	public void talkToPlayer(Player p)
	{
		if(isTrainer())
		{
			if(canBattle())
			{
				String speech = getSpeech();
				if(!speech.equalsIgnoreCase(""))
				{
					// TcpProtocolHandler.writeMessage(p.getTcpSession(), new NpcSpeechMessage(speech));
					ServerMessage message = new ServerMessage();
					message.Init(50);
					message.addInt(2);
					message.addString(speech);
					p.getSession().Send(message);
				}
				updateLastBattleTime();
				p.setBattling(true);
				p.setBattleField(new NpcBattleField(DataService.getBattleMechanics(), p, this));
				return;
			}
			else
			{
				p.setTalking(false);
				return;
			}
		}
		else
		{
			/* If this NPC wasn't a trainer, handle other possibilities */
			String speech = getSpeech();
			if(!speech.equalsIgnoreCase(""))
				if(!p.isShopping())
				{
					// Dont send if player is shopping!
                    //TODO test if this works else remove it
					if(p.m_facing == Direction.Down)
						m_facing = Direction.Up;
					else if (p.m_facing == Direction.Up)
						m_facing = Direction.Down;
					else if (p.m_facing == Direction.Left)
						m_facing = Direction.Right;
					else if(p.m_facing == Direction.Right)
						m_facing = Direction.Left;
					// TcpProtocolHandler.writeMessage(p.getTcpSession(), new NpcSpeechMessage(speech));
					ServerMessage message = new ServerMessage();
					message.Init(50);
					message.addInt(2);
					message.addString(speech);
					p.getSession().Send(message);
				}
			/* If this NPC is a sprite selection npc */
			if(m_name.equalsIgnoreCase("Spriter"))
			{
				p.setSpriting(true);
				// TcpProtocolHandler.writeMessage(p.getTcpSession(), new SpriteSelectMessage());
				ServerMessage message = new ServerMessage();
				message.Init(16);
				p.getSession().Send(message);
				return;
			}
                        //@author sadhi
			/* If this NPC is a sailor show warp options */
			if(m_name.equalsIgnoreCase("Seasond_Sailor")) {
				p.setIsTaveling(true);
				//TcpProtocolHandler.writeMessage(p.getTcpSession(), new BoatMessage());
				ServerMessage message = new ServerMessage();
				message.Init(95);
				p.getSession().Send(message);
				return;
			}
			//@author sadhi
			/* If this NPC is a train conductor */
			if(m_name.equalsIgnoreCase("Ticket_Vendor")) {
				p.setIsTaveling(true);
				//TcpProtocolHandler.writeMessage(p.getTcpSession(), new TrainMessage());
				ServerMessage message = new ServerMessage();
				message.Init(96);
				p.getSession().Send(message);
				return;
			}
			/* Box access */
			if(m_isBox)
			{
				// Send the data for the player's first box, they may change this later
				p.setTalking(false);
				p.setBoxing(true);
				p.sendBoxInfo(0);
			}
			/* Healer */
			if(m_isHeal)
			{
				p.healPokemon();
				p.setLastHeal(p.getX(), p.getY(), p.getMapX(), p.getMapY());
			}
			/* Shop access */
			if(m_isShop > 0)
				// Send shop packet to display shop window clientside
				if(!p.isShopping())
				{ // Dont display if user's shopping
					// TcpProtocolHandler.writeMessage(p.getTcpSession(), new ShopStockMessage(m_shop.getStockData()));
					ServerMessage message = new ServerMessage();
					message.Init(9);
					message.addInt(2);
					message.addString(m_shop.getStockData());
					p.getSession().Send(message);
					p.setShopping(true);
					p.setShop(m_shop);
				}
		}
	}

	/**
	 * Sets the time this NPC last battled
	 * 
	 * @param l
	 */
	public void updateLastBattleTime()
	{
		/* Only set this if they are not gym leaders */
		if(!isGymLeader())
		{
			m_lastBattle = System.currentTimeMillis();
			NpcSleepTimer.addNPC(this);
		}
	}

	/**
	 * Returns a string of this npcs speech ids
	 * 
	 * @param p
	 */
	private String getSpeech()
	{
		String result = "";
		for(int i = 0; i < m_speech.size(); i++)
			result += m_speech.get(i) + ",";
		return result;
	}
}
