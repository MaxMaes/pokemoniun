package org.pokenet.server.backend;

import java.util.Random;
import org.pokenet.server.GameServer;
import org.pokenet.server.backend.entity.Player;
import org.pokenet.server.backend.item.Item;
import org.pokenet.server.backend.item.Item.ItemAttribute;
import org.pokenet.server.battle.BattleTurn;
import org.pokenet.server.battle.DataService;
import org.pokenet.server.battle.Pokemon;
import org.pokenet.server.battle.PokemonEvolution;
import org.pokenet.server.battle.PokemonEvolution.EvolutionTypes;
import org.pokenet.server.battle.PokemonSpecies;
import org.pokenet.server.battle.impl.WildBattleField;
import org.pokenet.server.battle.mechanics.statuses.BurnEffect;
import org.pokenet.server.battle.mechanics.statuses.ConfuseEffect;
import org.pokenet.server.battle.mechanics.statuses.FreezeEffect;
import org.pokenet.server.battle.mechanics.statuses.ParalysisEffect;
import org.pokenet.server.battle.mechanics.statuses.PoisonEffect;
import org.pokenet.server.battle.mechanics.statuses.SleepEffect;
import org.pokenet.server.constants.ClientPacket;
import org.pokenet.server.protocol.ServerMessage;

/**
 * Processes an item using a thread
 * 
 * @author shadowkanji
 */
public class ItemProcessor implements Runnable
{
	/* The enum that handles Pokeball types */
	public enum PokeBall
	{
		CHERISHBALL, DIVEBALL, DUSKBALL, FASTBALL, FRIENDBALL, GREATBALL, HEALBALL, HEAVYBALL, LEVELBALL, LOVEBALL, LUREBALL, LUXURY, MASTERBALL, MOONBALL, NESTBALL, NETBALL, PARKBALL, POKEBALL, PREMIERBALL, QUICKBALL, REPEATBALL, SAFARIBALL, TIMERBALL, ULTRABALL,
	};

	private final Player m_player;
	private final String[] m_details;

	/**
	 * Constructor
	 * 
	 * @param p
	 * @param details
	 */
	public ItemProcessor(Player p, String[] details)
	{
		m_player = p;
		m_details = details;
	}

	/**
	 * Executes the item usage
	 */
	public void run()
	{
		String[] data = new String[m_details.length - 1];
		for(int i = 1; i < m_details.length; i++)
		{
			data[i - 1] = m_details[i];
		}
		int itemNumber = Integer.parseInt(m_details[0]);
		if(useItem(m_player, itemNumber, data) && !GameServer.getServiceManager().getItemDatabase().getItem(itemNumber).getName().contains("Rod"))
		{
			m_player.getBag().removeItem(itemNumber, 1);
			ServerMessage message = new ServerMessage(m_player.getSession());
			message.init(ClientPacket.REMOVE_ITEM_BAG.getValue());
			message.addInt(itemNumber);
			message.addInt(1);
			message.sendResponse();
		}
	}

	/**
	 * Uses an item in the player's bag. Returns true if it was used.
	 * 
	 * @param player
	 * @param itemId
	 * @param data
	 *        - extra data received from client
	 * @return
	 */
	public boolean useItem(Player player, int itemId, String[] data)
	{
		/* TODO: Rewrite this thread/function/monster in iterations. */
		/* Check that the bag contains the item */
		if(player.getBag().containsItem(itemId) < 0)
			return false;
		/* We have the item, so let us use it */
		Item item = GameServer.getServiceManager().getItemDatabase().getItem(itemId);
		/* Pokemon object we might need */
		Pokemon poke = null;
		try
		{
			/* Check if the item is a rod */
			if(item.getName().equalsIgnoreCase("OLD ROD"))
			{
				if(!player.isBattling() && !player.isFishing())
				{
					player.fish(0);
					return true;
				}
			}
			else if(item.getName().equalsIgnoreCase("GOOD ROD"))
			{
				if(!player.isBattling() && !player.isFishing())
				{
					if(player.getFishingLevel() >= 15)
						player.fish(15);
					else
					{
						// Notify client that you need a fishing level of 15 or higher for this rod
						ServerMessage message = new ServerMessage(m_player.getSession());
						message.init(ClientPacket.CANT_USE_ROD.getValue());
						message.addInt(15);
						message.sendResponse();
					}
					return true;
				}
			}
			else if(item.getName().equalsIgnoreCase("GREAT ROD"))
			{
				if(!player.isBattling() && !player.isFishing())
				{
					if(player.getFishingLevel() >= 50)
						player.fish(35);
					else
					{
						// Notify client that you need a fishing level of 50 or higher for this rod
						ServerMessage message = new ServerMessage(m_player.getSession());
						message.init(ClientPacket.CANT_USE_ROD.getValue());
						message.addInt(50);
						message.sendResponse();
					}
					return true;
				}
			}
			else if(item.getName().equalsIgnoreCase("ULTRA ROD"))
			{
				if(!player.isBattling() && !player.isFishing())
				{
					if(player.getFishingLevel() >= 70)
						player.fish(50);
					else
					{
						// Notify client that you need a fishing level of 70 or higher for this rod
						ServerMessage message = new ServerMessage(m_player.getSession());
						message.init(ClientPacket.CANT_USE_ROD.getValue());
						message.addInt(70);
						message.sendResponse();
					}
					return true;
				}
			}
			/* Check if the item is a repel or escape rope */
			else if(item.getName().equalsIgnoreCase("REPEL"))
			{
				player.setRepel(100);
				return true;
			}
			else if(item.getName().equalsIgnoreCase("SUPER REPEL"))
			{
				player.setRepel(200);
				return true;
			}
			else if(item.getName().equalsIgnoreCase("MAX REPEL"))
			{
				player.setRepel(250);
				return true;
			}
			else if(item.getName().equalsIgnoreCase("ESCAPE ROPE"))
			{
				if(player.isBattling())
					return false;
				/* Warp the player to their last heal point */
				player.setX(player.getHealX());
				player.setY(player.getHealY());
				player.setMap(GameServer.getServiceManager().getMovementService().getMapMatrix().getMapByGamePosition(player.getHealMapX(), player.getHealMapY()), null);
				return true;
			}
			/* Else, determine what do to with the item */
			if(item.getAttributes().contains(ItemAttribute.MOVESLOT))
			{
				/* TMs & HMs */
				try
				{
					/* Can't use a TM/HM during battle */
					if(player.isBattling())
						return false;
					/* Player is not in battle, learn the move */
					poke = player.getParty()[Integer.parseInt(data[0])];
					if(poke == null)
						return false;
					String moveName = item.getName().substring(5);
					/* Ensure the Pokemon can learn this move */
					if(DataService.getMoveSetData().getMoveSet(poke.getSpeciesNumber()).canLearn(moveName))
					{
						poke.getMovesLearning().add(moveName);
						ServerMessage message = new ServerMessage(ClientPacket.MOVE_LEARN_LVL);
						message.addInt(Integer.parseInt(data[0]));
						message.addString(moveName);
						m_player.getSession().Send(message);
						return true;
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
					return false;
				}
			}
			else if(item.getAttributes().contains(ItemAttribute.POKEMON))
			{
				/* Status healers, hold items, etc. */
				if(item.getCategory().equalsIgnoreCase("POTIONS"))
				{
					/* Potions */
					int hpBoost = 0;
					poke = player.getParty()[Integer.parseInt(data[0])];
					String message = "";
					if(poke == null)
						return false;

					if(poke.getHealth() <= 0)
					{
						ServerMessage cantUse = new ServerMessage(ClientPacket.CANT_USE_ITEM);
						player.getSession().Send(cantUse);
						return false;
					}

					if(item.getId() == 1)
					{ // Potion
						hpBoost = 20;
						poke.changeHealth(hpBoost);
						message = "You used Potion on " + poke.getName() + "/nThe Potion restored 20 HP";
					}
					else if(item.getId() == 2)
					{// Super Potion
						hpBoost = 50;
						poke.changeHealth(hpBoost);
						message = "You used Super Potion on " + poke.getName() + "/nThe Super Potion restored 50 HP";
					}
					else if(item.getId() == 3)
					{ // Hyper Potion
						hpBoost = 200;
						poke.changeHealth(hpBoost);
						message = "You used Hyper Potion on " + poke.getName() + "/nThe Hyper Potion restored 200 HP";
					}
					else if(item.getId() == 4)
					{// Max Potion
						poke.changeHealth(poke.getRawStat(0));
						message = "You used Max Potion on " + poke.getName() + "/nThe Max Potion restored All HP";
					}
					else
					{
						return false;
					}
					if(!player.isBattling())
					{
						/* Update the client */
						ServerMessage hpChange = new ServerMessage(ClientPacket.POKE_HP_CHANGE);
						hpChange.addInt(Integer.parseInt(data[0]));
						hpChange.addInt(poke.getHealth());
						m_player.getSession().Send(hpChange);
						ServerMessage itemUse = new ServerMessage(ClientPacket.USE_ITEM);
						itemUse.addString(message);
						m_player.getSession().Send(itemUse);
					}
					else
					{
						/* Player is in battle, take a hit from enemy */
						player.getBattleField().executeItemTurn(item.getId());
					}
					return true;
				}
				else if(item.getCategory().equalsIgnoreCase("EVOLUTION"))
				{
					/* Evolution items can't be used in battle */
					if(player.isBattling())
						return false;
					/* Get the pokemon's evolution data */
					poke = player.getParty()[Integer.parseInt(data[0])];
					/* Ensure poke exists */
					if(poke == null)
						return false;
					PokemonSpecies pokeData = PokemonSpecies.getDefaultData().getPokemonByName(poke.getSpeciesName());
					for(int j = 0; j < pokeData.getEvolutions().length; j++)
					{
						PokemonEvolution evolution = pokeData.getEvolutions()[j];
						/* Check if this pokemon evolves by item */
						if(evolution.getType() == EvolutionTypes.Item)
						{
							/* Check if the item is an evolution stone If so, evolve the
							 * Pokemon */
							if(item.getId() == 164 && evolution.getAttribute().equalsIgnoreCase("FIRESTONE"))
							{
								poke.setEvolution(evolution);
								poke.evolutionResponse(true, player);
								return true;
							}
							else if(item.getId() == 165 && evolution.getAttribute().equalsIgnoreCase("WATERSTONE"))
							{
								poke.setEvolution(evolution);
								poke.evolutionResponse(true, player);
								return true;
							}
							else if(item.getId() == 166 && evolution.getAttribute().equalsIgnoreCase("THUNDERSTONE"))
							{
								poke.setEvolution(evolution);
								poke.evolutionResponse(true, player);
								return true;
							}
							else if(item.getId() == 173 && evolution.getAttribute().equalsIgnoreCase("LEAFSTONE"))
							{
								poke.setEvolution(evolution);
								poke.evolutionResponse(true, player);
								return true;
							}
							else if(item.getId() == 168 && evolution.getAttribute().equalsIgnoreCase("MOONSTONE"))
							{
								poke.setEvolution(evolution);
								poke.evolutionResponse(true, player);
								return true;
							}
							else if(item.getId() == 167 && evolution.getAttribute().equalsIgnoreCase("SUNSTONE"))
							{
								poke.setEvolution(evolution);
								poke.evolutionResponse(true, player);
								return true;
							}
							else if(item.getId() == 169 && evolution.getAttribute().equalsIgnoreCase("SHINYSTONE"))
							{
								poke.setEvolution(evolution);
								poke.evolutionResponse(true, player);
								return true;
							}
							else if(item.getId() == 170 && evolution.getAttribute().equalsIgnoreCase("DUSKSTONE"))
							{
								poke.setEvolution(evolution);
								poke.evolutionResponse(true, player);
								return true;
							}
							else if(item.getId() == 171 && evolution.getAttribute().equalsIgnoreCase("DAWNSTONE"))
							{
								poke.setEvolution(evolution);
								poke.evolutionResponse(true, player);
								return true;
								/* TODO: Add Oval Stone? */
							}
						}
					}
				}
				else if(item.getCategory().equalsIgnoreCase("MEDICINE"))
				{
					poke = player.getParty()[Integer.parseInt(data[0])];
					if(poke == null)
						return false;

					// Check if this pokemon is alive to use all items but revive, REVIVE NOT IMPLEMENTED!!! Implement it before this piece of code!
					if(poke.getHealth() <= 0)
					{
						ServerMessage cantUse = new ServerMessage(ClientPacket.CANT_USE_ITEM);
						player.getSession().Send(cantUse);
						return false;
					}

					if(item.getId() == 16)
					{ // Antidote
						String message = "You used Antidote on " + poke.getName() + "/nThe Antidote restored " + poke.getName() + " status to normal";
						poke.removeStatus(PoisonEffect.class);
						if(player.isBattling())
							player.getBattleField().executeItemTurn(item.getId());
						else
						{
							ServerMessage itemUse = new ServerMessage(ClientPacket.USE_ITEM);
							itemUse.addString(message);
							player.getSession().Send(itemUse);
						}
						return true;
					}
					else if(item.getId() == 17)
					{ // Parlyz Heal
						String message = "You used Parlyz Heal on " + poke.getName() + "/nThe Parlyz Heal restored " + poke.getName() + " status to normal";
						poke.removeStatus(ParalysisEffect.class);
						if(player.isBattling())
							player.getBattleField().forceExecuteTurn();
						else
						{
							ServerMessage itemUse = new ServerMessage(ClientPacket.USE_ITEM);

							itemUse.addString(message);
							player.getSession().Send(itemUse);
						}
						return true;
					}
					else if(item.getId() == 18)
					{ // Awakening
						String message = "You used Awakening on " + poke.getName() + "/nThe Awakening restored " + poke.getName() + " status to normal";
						poke.removeStatus(SleepEffect.class);
						if(player.isBattling())
							player.getBattleField().forceExecuteTurn();
						else
						{
							// p.getTcpSession().write("Ii" + message);
							ServerMessage itemUse = new ServerMessage(ClientPacket.USE_ITEM);

							itemUse.addString(message);
							player.getSession().Send(itemUse);
						}
						return true;
					}
					else if(item.getId() == 19)
					{ // Burn Heal
						String message = "You used Burn Heal on " + poke.getName() + "/nThe Burn Heal restored " + poke.getName() + " status to normal";
						poke.removeStatus(BurnEffect.class);
						if(player.isBattling())
							player.getBattleField().forceExecuteTurn();
						else
						{
							// p.getTcpSession().write("Ii" + message);
							ServerMessage itemUse = new ServerMessage(ClientPacket.USE_ITEM);

							itemUse.addString(message);
							player.getSession().Send(itemUse);
						}
						return true;
					}
					else if(item.getId() == 20)
					{ // Ice Heal
						String message = "You used Ice Heal on " + poke.getName() + "/nThe Ice Heal restored " + poke.getName() + " status to normal";
						poke.removeStatus(FreezeEffect.class);
						if(player.isBattling())
							player.getBattleField().forceExecuteTurn();
						else
						{
							ServerMessage itemUse = new ServerMessage(ClientPacket.USE_ITEM);
							itemUse.addString(message);
							player.getSession().Send(itemUse);
						}
						return true;
					}
					else if(item.getId() == 21)
					{ // Full Heal
						String message = "You used Full Heal on " + poke.getName() + "/nThe Full Heal restored " + poke.getName() + " status to normal";
						poke.removeStatusEffects(true);
						if(player.isBattling())
							player.getBattleField().forceExecuteTurn();
						else
						{
							ServerMessage itemUse = new ServerMessage(ClientPacket.USE_ITEM);
							itemUse.addString(message);
							player.getSession().Send(itemUse);
						}
						return true;
					}
					else if(item.getName().equalsIgnoreCase("LAVA COOKIE"))
					{
						// just like a FULL HEAL
						poke.removeStatusEffects(true);
						if(player.isBattling())
						{
							player.getBattleField().executeItemTurn(item.getId());
						}
						return true;
					}
					else if(item.getName().equalsIgnoreCase("OLD GATEAU"))
					{
						// just like a FULL HEAL
						poke.removeStatusEffects(true);
						if(player.isBattling())
						{
							player.getBattleField().executeItemTurn(item.getId());
						}
						return true;
					}
				}
				else if(item.getCategory().equalsIgnoreCase("FOOD"))
				{
					poke = player.getParty()[Integer.parseInt(data[0])];
					Random rand = new Random();
					if(poke == null)
						return false;
					if(item.getId() == 200)
					{ // Cheri Berry
						String message = poke.getName() + " ate the Cheri Berry/nThe Cheri Berry restored " + poke.getName() + " status to normal";
						poke.removeStatus(ParalysisEffect.class);
						if(player.isBattling())
							player.getBattleField().executeItemTurn(item.getId());
						else
						{
							ServerMessage itemUse = new ServerMessage(ClientPacket.USE_ITEM);
							itemUse.addString(message);
							player.getSession().Send(itemUse);
						}
						return true;
					}
					else if(item.getId() == 201)
					{ // Chesto Berry
						String message = poke.getName() + " ate the Chesto Berry/nThe Chesto Berry restored " + poke.getName() + " status to normal";
						poke.removeStatus(SleepEffect.class);
						if(player.isBattling())
							player.getBattleField().executeItemTurn(item.getId());
						else
						{
							ServerMessage itemUse = new ServerMessage(ClientPacket.USE_ITEM);
							itemUse.addString(message);
							player.getSession().Send(itemUse);
						}
						return true;
					}
					else if(item.getId() == 202)
					{ // Pecha Berry
						String message = poke.getName() + " ate the Pecha Berry/nThe Pecha Berry restored " + poke.getName() + " status to normal";
						poke.removeStatus(PoisonEffect.class);
						if(player.isBattling())
							player.getBattleField().executeItemTurn(item.getId());
						else
						{
							ServerMessage itemUse = new ServerMessage(ClientPacket.USE_ITEM);
							itemUse.addString(message);
							player.getSession().Send(itemUse);
						}
						return true;
					}
					else if(item.getId() == 203)
					{ // Rawst Berry
						String message = poke.getName() + " ate the Rawst Berry/nThe Rawst Berry restored " + poke.getName() + " status to normal";
						poke.removeStatus(BurnEffect.class);
						if(player.isBattling())
							player.getBattleField().executeItemTurn(item.getId());
						else
						{
							ServerMessage itemUse = new ServerMessage(ClientPacket.USE_ITEM);
							itemUse.addString(message);
							player.getSession().Send(itemUse);
						}
						return true;
					}
					else if(item.getId() == 204)
					{ // Aspear Berry
						String message = poke.getName() + " ate the Aspear Berry/nThe Aspear Berry restored " + poke.getName() + " status to normal";
						poke.removeStatus(FreezeEffect.class);
						if(player.isBattling())
							player.getBattleField().executeItemTurn(item.getId());
						else
						{
							ServerMessage itemUse = new ServerMessage(ClientPacket.USE_ITEM);
							itemUse.addString(message);
							player.getSession().Send(itemUse);
						}
						return true;
					}
					else if(item.getId() == 205)
					{ // Leppa Berry
						String message = "Leppa Berry had no effect";
						/* Move selection not completed, temp message TODO: Add support for this */
						int ppSlot = Integer.parseInt(data[1]);
						if(poke.getPp(ppSlot) + 10 <= poke.getMaxPp(ppSlot))
							poke.setPp(ppSlot, poke.getPp(ppSlot) + 10);
						else
							poke.setPp(ppSlot, poke.getMaxPp(ppSlot));
						if(player.isBattling())
							player.getBattleField().executeItemTurn(item.getId());
						else
						{
							ServerMessage itemUse = new ServerMessage(ClientPacket.USE_ITEM);
							itemUse.addString(message);
							player.getSession().Send(itemUse);
						}
						return true;
					}
					else if(item.getId() == 206)
					{ // Oran Berry
						String message = poke.getName() + " ate the Oran Berry/nThe Oran Berry restored 10HP";
						poke.changeHealth(10);
						if(!player.isBattling())
						{
							ServerMessage hpChange = new ServerMessage(ClientPacket.POKE_HP_CHANGE);
							hpChange.addInt(Integer.parseInt(data[0]));
							hpChange.addInt(poke.getHealth());
							player.getSession().Send(hpChange);
							ServerMessage itemUse = new ServerMessage(ClientPacket.USE_ITEM);
							itemUse.addString(message);
							player.getSession().Send(itemUse);
						}
						else
							player.getBattleField().executeItemTurn(item.getId());
						return true;
					}
					else if(item.getId() == 207)
					{ // Persim Berry
						String message = poke.getName() + " ate the Persim Berry/nThe Persim Berry restored " + poke.getName() + " status to normal";
						poke.removeStatus(ConfuseEffect.class);
						if(player.isBattling())
							player.getBattleField().executeItemTurn(item.getId());
						else
						{
							ServerMessage itemUse = new ServerMessage(ClientPacket.USE_ITEM);
							itemUse.addString(message);
							player.getSession().Send(itemUse);
							return true;
						}
					}
					else if(item.getId() == 208)
					{ // Lum Berry
						String message = poke.getName() + " ate the Lum Berry/nThe Lum Berry restored " + poke.getName() + " status to normal";
						poke.removeStatusEffects(true);
						if(player.isBattling())
							player.getBattleField().executeItemTurn(item.getId());
						else
						{
							ServerMessage itemUse = new ServerMessage(ClientPacket.USE_ITEM);
							itemUse.addString(message);
							player.getSession().Send(itemUse);
							return true;
						}
					}
					else if(item.getId() == 209)
					{ // Sitrus Berry
						String message = poke.getName() + " ate the Sitrus Berry/nThe Sitrus Berry restored 30HP";
						poke.changeHealth(30);
						if(!player.isBattling())
						{
							ServerMessage hpChange = new ServerMessage(ClientPacket.POKE_HP_CHANGE);
							hpChange.addInt(Integer.parseInt(data[0]));
							hpChange.addInt(poke.getHealth());
							player.getSession().Send(hpChange);
							ServerMessage itemUse = new ServerMessage(ClientPacket.USE_ITEM);
							itemUse.addString(message);
							player.getSession().Send(itemUse);
						}
						else
							player.getBattleField().executeItemTurn(item.getId());
						return true;
					}
					else if(item.getId() == 210)
					{ // Figy Berry
						String message = poke.getName() + " ate the Figy Berry/nThe Figy Berry restored" + poke.getRawStat(0) / 8 + " HP to " + poke.getName() + "!";
						poke.changeHealth(poke.getRawStat(0) / 8);
						if(!player.isBattling())
						{
							ServerMessage hpChange = new ServerMessage(ClientPacket.POKE_HP_CHANGE);
							hpChange.addInt(Integer.parseInt(data[0]));
							hpChange.addInt(poke.getHealth());
							player.getSession().Send(hpChange);
							ServerMessage itemUse = new ServerMessage(ClientPacket.USE_ITEM);
							itemUse.addString(message);
							player.getSession().Send(itemUse);
						}
						else
							player.getBattleField().executeItemTurn(item.getId());
						return true;
					}
					else if(item.getId() == 214)
					{ // Wiki Berry
						String message = poke.getName() + " ate the Wiki Berry/nThe Wiki Berry restored" + poke.getRawStat(0) / 8 + " HP to " + poke.getName() + "!";
						poke.changeHealth(poke.getRawStat(0) / 8);
						if(!player.isBattling())
						{
							ServerMessage hpChange = new ServerMessage(ClientPacket.POKE_HP_CHANGE);
							hpChange.addInt(Integer.parseInt(data[0]));
							hpChange.addInt(poke.getHealth());
							player.getSession().Send(hpChange);
							ServerMessage itemUse = new ServerMessage(ClientPacket.USE_ITEM);
							itemUse.addString(message);
							player.getSession().Send(itemUse);
						}
						else
							player.getBattleField().executeItemTurn(item.getId());
						return true;
					}
					else if(item.getId() == 212)
					{ // Mago Berry
						String message = poke.getName() + " ate the Mago Berry/nThe Mago Berry restored" + poke.getRawStat(0) / 8 + " HP to " + poke.getName() + "!";
						poke.changeHealth(poke.getRawStat(0) / 8);
						if(!player.isBattling())
						{
							ServerMessage hpChange = new ServerMessage(ClientPacket.POKE_HP_CHANGE);
							hpChange.addInt(Integer.parseInt(data[0]));
							hpChange.addInt(poke.getHealth());
							player.getSession().Send(hpChange);
							ServerMessage itemUse = new ServerMessage(ClientPacket.USE_ITEM);
							itemUse.addString(message);
							player.getSession().Send(itemUse);
						}
						else
							player.getBattleField().executeItemTurn(item.getId());
						return true;
					}
					else if(item.getId() == 213)
					{ // Aguav Berry
						String message = poke.getName() + " ate the Aguav Berry/nThe Aguav Berry restored" + poke.getRawStat(0) / 8 + " HP to " + poke.getName() + "!";
						poke.changeHealth(poke.getRawStat(0) / 8);
						if(!player.isBattling())
						{
							ServerMessage hpChange = new ServerMessage(ClientPacket.POKE_HP_CHANGE);
							hpChange.addInt(Integer.parseInt(data[0]));
							hpChange.addInt(poke.getHealth());
							player.getSession().Send(hpChange);
							ServerMessage itemUse = new ServerMessage(ClientPacket.USE_ITEM);
							itemUse.addString(message);
							player.getSession().Send(itemUse);
						}
						else
							player.getBattleField().executeItemTurn(item.getId());
						return true;
					}
					else if(item.getId() == 214)
					{ // Iapapa Berry
						String message = poke.getName() + " ate the Iapapa Berry/nThe Iapapa Berry restored" + poke.getRawStat(0) / 8 + " HP to " + poke.getName() + "!";
						poke.changeHealth(poke.getRawStat(0) / 8);
						if(!player.isBattling())
						{
							ServerMessage hpChange = new ServerMessage(ClientPacket.POKE_HP_CHANGE);
							hpChange.addInt(Integer.parseInt(data[0]));
							hpChange.addInt(poke.getHealth());
							player.getSession().Send(hpChange);
							ServerMessage itemUse = new ServerMessage(ClientPacket.USE_ITEM);
							itemUse.addString(message);
							player.getSession().Send(itemUse);
						}
						else
							player.getBattleField().executeItemTurn(item.getId());
						return true;
					}
					else if(item.getId() == 800)
					{ // Voltorb Lollipop
						String message = poke.getName() + " ate the Voltorb Lollipop/nThe Lollipop restored 50 HP to " + poke.getName() + "!";
						poke.changeHealth(50);
						int random = rand.nextInt(10);
						if(random < 3)
						{
							poke.addStatus(new ParalysisEffect());
							message += "/n" + poke.getName() + " was Paralyzed from the Lollipop!";
						}
						if(player.isBattling())
						{
							player.getBattleField().executeItemTurn(item.getId());
						}
						else
						{
							ServerMessage hpChange = new ServerMessage(ClientPacket.POKE_HP_CHANGE);
							hpChange.addInt(Integer.parseInt(data[0]));
							hpChange.addInt(poke.getHealth());
							player.getSession().Send(hpChange);
							ServerMessage itemUse = new ServerMessage(ClientPacket.USE_ITEM);
							itemUse.addString(message);
							player.getSession().Send(itemUse);
						}
						return true;
					}
					else if(item.getId() == 801)
					{ // Sweet Chills
						String message = poke.getName() + " ate the Sweet Chill/nThe Sweet Chill restored " + poke.getName() + "'s moves!";
						for(int ppSlot = 0; ppSlot < 4; ppSlot++)
						{
							if(poke.getPp(ppSlot) + 5 <= poke.getMaxPp(ppSlot))
								poke.setPp(ppSlot, poke.getPp(ppSlot) + 5);
							else
								poke.setPp(ppSlot, poke.getMaxPp(ppSlot));
						}
						int random = rand.nextInt(10);
						if(random < 3)
						{
							try
							{
								poke.addStatus(new FreezeEffect());
								message += "/n" + poke.getName() + " was frozen solid from the cold candy!";
							}
							catch(Exception e)
							{
							}// Already under a status effect.
						}
						if(player.isBattling())
							player.getBattleField().executeItemTurn(item.getId());
						else
						{
							ServerMessage itemUse = new ServerMessage(ClientPacket.USE_ITEM);
							itemUse.addString(message);
							player.getSession().Send(itemUse);
						}
						return true;
					}
					else if(item.getId() == 802)
					{ // Cinnamon Candy
						String message = poke.getName() + " ate the Cinnamon Candy./nThe Cinnamon Candy restored " + poke.getName() + "'s status to normal!";
						poke.removeStatusEffects(true);
						int random = rand.nextInt(10);
						if(random < 3)
						{
							poke.addStatus(new BurnEffect());
							message += "/n" + poke.getName() + " was burned from the candy!";
						}
						if(player.isBattling())
						{
							player.getBattleField().executeItemTurn(item.getId());
						}
						else
						{
							ServerMessage hpChange = new ServerMessage(ClientPacket.POKE_HP_CHANGE);
							hpChange.addInt(Integer.parseInt(data[0]));
							hpChange.addInt(poke.getHealth());
							player.getSession().Send(hpChange);
							ServerMessage itemUse = new ServerMessage(ClientPacket.USE_ITEM);
							itemUse.addString(message);
							player.getSession().Send(itemUse);
						}
						return true;
					}
					else if(item.getId() == 803)
					{ // Candy Corn
						String message = poke.getName() + " ate the Candy Corn./n" + poke.getName() + " is happier!";
						int happiness = poke.getHappiness() + 15;
						if(happiness <= 300)
							poke.setHappiness(happiness);
						else
							poke.setHappiness(300);
						int random = rand.nextInt(10);
						if(random < 3)
						{
							poke.addStatus(new PoisonEffect());
							message += "/n" + poke.getName() + " got Poisoned from the rotten candy!";
						}
						if(player.isBattling())
							player.getBattleField().executeItemTurn(item.getId());
						else
						{
							ServerMessage itemUse = new ServerMessage(ClientPacket.USE_ITEM);
							itemUse.addString(message);
							player.getSession().Send(itemUse);
							return true;
						}
					}
					else if(item.getId() == 804)
					{ // Poke'Choc
						String message = poke.getName() + " ate the Poke'Choc Bar!/n" + poke.getName() + " is happier!";
						int happiness = poke.getHappiness() + 10;
						if(happiness <= 300)
							poke.setHappiness(happiness);
						else
							poke.setHappiness(300);
						int random = rand.nextInt(10);
						if(random <= 3)
						{
							poke.changeHealth(30);
							message += "/n" + poke.getName() + " recovered 30HP.";
						}
						if(player.isBattling())
							player.getBattleField().executeItemTurn(item.getId());
						else
						{
							ServerMessage itemUse = new ServerMessage(ClientPacket.USE_ITEM);
							itemUse.addString(message);
							player.getSession().Send(itemUse);
							return true;
						}
					}
					else if(item.getId() == 805)
					{ // Gummilax
						String message = poke.getName() + " ate the Gummilax./n" + poke.getName() + " is happier!";
						int happiness = poke.getHappiness() + rand.nextInt(30);
						if(happiness <= 255)
							poke.setHappiness(happiness);
						else
							poke.setHappiness(255);
						int random = rand.nextInt(10);
						if(random < 3)
						{
							poke.addStatus(new ParalysisEffect());
							message += "/nThe gummi was too sweet for " + poke.getName() + "./n" + poke.getName() + " fell asleep!";
						}
						if(player.isBattling())
							player.getBattleField().executeItemTurn(item.getId());
						else
						{
							ServerMessage itemUse = new ServerMessage(ClientPacket.USE_ITEM);
							itemUse.addString(message);
							player.getSession().Send(itemUse);
							return true;
						}
					}
					else if(item.getId() == 806)
					{ // Gengum
						String message = poke.getName() + " ate the Gengum.";
						int randHealth = rand.nextInt(100);
						randHealth -= 20;
						if(poke.getHealth() + randHealth < 0)
							poke.setHealth(1);
						else
							poke.changeHealth(randHealth);
						if(randHealth > 0)
							message += "/n" + poke.getName() + " healed " + randHealth + "HP";
						else
							message += "/n" + poke.getName() + " lost " + -randHealth + "HP";
						if(player.isBattling())
						{
							player.getBattleField().queueMove(0, BattleTurn.getMoveTurn(-1));
						}
						else
						{
							ServerMessage hpChange = new ServerMessage(ClientPacket.POKE_HP_CHANGE);
							hpChange.addInt(Integer.parseInt(data[0]));
							hpChange.addInt(poke.getHealth());
							player.getSession().Send(hpChange);
							ServerMessage itemUse = new ServerMessage(ClientPacket.USE_ITEM);
							itemUse.addString(message);
							player.getSession().Send(itemUse);
						}
						return true;
					}
				}
			}
			else if(item.getAttributes().contains(ItemAttribute.BATTLE))
			{
				/* Pokeballs */
				if(item.getName().equalsIgnoreCase("POKE BALL"))
				{
					if(player.getBattleField() instanceof WildBattleField)
					{
						WildBattleField w = (WildBattleField) player.getBattleField();
						if(!w.throwPokeball(PokeBall.POKEBALL))
							w.queueMove(0, BattleTurn.getMoveTurn(-1));
						return true;
					}
				}
				else if(item.getName().equalsIgnoreCase("GREAT BALL"))
				{
					if(player.getBattleField() instanceof WildBattleField)
					{
						WildBattleField w = (WildBattleField) player.getBattleField();
						if(!w.throwPokeball(PokeBall.GREATBALL))
							w.queueMove(0, BattleTurn.getMoveTurn(-1));
						return true;
					}
				}
				else if(item.getName().equalsIgnoreCase("ULTRA BALL"))
				{
					if(player.getBattleField() instanceof WildBattleField)
					{
						WildBattleField w = (WildBattleField) player.getBattleField();
						if(!w.throwPokeball(PokeBall.ULTRABALL))
							w.queueMove(0, BattleTurn.getMoveTurn(-1));
						return true;
					}
				}
				else if(item.getName().equalsIgnoreCase("MASTER BALL"))
				{
					if(player.getBattleField() instanceof WildBattleField)
					{
						WildBattleField w = (WildBattleField) player.getBattleField();
						if(!w.throwPokeball(PokeBall.MASTERBALL))
							w.queueMove(0, BattleTurn.getMoveTurn(-1));
						return true;
					}
				}
				else if(item.getName().equalsIgnoreCase("LEVEL BALL"))
				{
					if(player.getBattleField() instanceof WildBattleField)
					{
						WildBattleField w = (WildBattleField) player.getBattleField();
						if(!w.throwPokeball(PokeBall.LEVELBALL))
							w.queueMove(0, BattleTurn.getMoveTurn(-1));
						return true;
					}
				}
				else if(item.getName().equalsIgnoreCase("LURE BALL"))
				{
					if(player.getBattleField() instanceof WildBattleField)
					{
						WildBattleField w = (WildBattleField) player.getBattleField();
						if(!w.throwPokeball(PokeBall.LUREBALL))
							w.queueMove(0, BattleTurn.getMoveTurn(-1));
						return true;
					}
				}
				else if(item.getName().equalsIgnoreCase("MOON BALL"))
				{
					if(player.getBattleField() instanceof WildBattleField)
					{
						WildBattleField w = (WildBattleField) player.getBattleField();
						if(!w.throwPokeball(PokeBall.MOONBALL))
							w.queueMove(0, BattleTurn.getMoveTurn(-1));
						return true;
					}
				}
				else if(item.getName().equalsIgnoreCase("FRIEND BALL"))
				{
					if(player.getBattleField() instanceof WildBattleField)
					{
						WildBattleField w = (WildBattleField) player.getBattleField();
						if(!w.throwPokeball(PokeBall.FRIENDBALL))
							w.queueMove(0, BattleTurn.getMoveTurn(-1));
						return true;
					}
				}
				else if(item.getName().equalsIgnoreCase("LOVE BALL"))
				{
					if(player.getBattleField() instanceof WildBattleField)
					{
						WildBattleField w = (WildBattleField) player.getBattleField();
						if(!w.throwPokeball(PokeBall.LOVEBALL))
							w.queueMove(0, BattleTurn.getMoveTurn(-1));
						return true;
					}
				}
				else if(item.getName().equalsIgnoreCase("HEAVY BALL"))
				{
					if(player.getBattleField() instanceof WildBattleField)
					{
						WildBattleField w = (WildBattleField) player.getBattleField();
						if(!w.throwPokeball(PokeBall.HEAVYBALL))
							w.queueMove(0, BattleTurn.getMoveTurn(-1));
						return true;
					}
				}
				else if(item.getName().equalsIgnoreCase("FAST BALL"))
				{
					if(player.getBattleField() instanceof WildBattleField)
					{
						WildBattleField w = (WildBattleField) player.getBattleField();
						if(!w.throwPokeball(PokeBall.FASTBALL))
							w.queueMove(0, BattleTurn.getMoveTurn(-1));
						return true;
					}
				}
				else if(item.getName().equalsIgnoreCase("PARK BALL"))
				{
					if(player.getBattleField() instanceof WildBattleField)
					{
						WildBattleField w = (WildBattleField) player.getBattleField();
						if(!w.throwPokeball(PokeBall.PARKBALL))
							w.queueMove(0, BattleTurn.getMoveTurn(-1));
						return true;
					}
				}
				else if(item.getName().equalsIgnoreCase("PREMIER BALL"))
				{
					if(player.getBattleField() instanceof WildBattleField)
					{
						WildBattleField w = (WildBattleField) player.getBattleField();
						if(!w.throwPokeball(PokeBall.PREMIERBALL))
							w.queueMove(0, BattleTurn.getMoveTurn(-1));
						return true;
					}
				}
				else if(item.getName().equalsIgnoreCase("REPEAT BALL"))
				{
					if(player.getBattleField() instanceof WildBattleField)
					{
						WildBattleField w = (WildBattleField) player.getBattleField();
						if(!w.throwPokeball(PokeBall.REPEATBALL))
							w.queueMove(0, BattleTurn.getMoveTurn(-1));
						return true;
					}
				}
				else if(item.getName().equalsIgnoreCase("TIMER BALL"))
				{
					if(player.getBattleField() instanceof WildBattleField)
					{
						WildBattleField w = (WildBattleField) player.getBattleField();
						if(!w.throwPokeball(PokeBall.TIMERBALL))
							w.queueMove(0, BattleTurn.getMoveTurn(-1));
						return true;
					}
				}
				else if(item.getName().equalsIgnoreCase("NEST BALL"))
				{
					if(player.getBattleField() instanceof WildBattleField)
					{
						WildBattleField w = (WildBattleField) player.getBattleField();
						if(!w.throwPokeball(PokeBall.NESTBALL))
							w.queueMove(0, BattleTurn.getMoveTurn(-1));
						return true;
					}
				}
				else if(item.getName().equalsIgnoreCase("NET BALL"))
				{
					if(player.getBattleField() instanceof WildBattleField)
					{
						WildBattleField w = (WildBattleField) player.getBattleField();
						if(!w.throwPokeball(PokeBall.NETBALL))
							w.queueMove(0, BattleTurn.getMoveTurn(-1));
						return true;
					}
				}
				else if(item.getName().equalsIgnoreCase("DIVE BALL"))
				{
					if(player.getBattleField() instanceof WildBattleField)
					{
						WildBattleField w = (WildBattleField) player.getBattleField();
						if(!w.throwPokeball(PokeBall.DIVEBALL))
							w.queueMove(0, BattleTurn.getMoveTurn(-1));
						return true;
					}
				}
				else if(item.getName().equalsIgnoreCase("LUXURY BALL"))
				{
					if(player.getBattleField() instanceof WildBattleField)
					{
						WildBattleField w = (WildBattleField) player.getBattleField();
						if(!w.throwPokeball(PokeBall.LUXURY))
							w.queueMove(0, BattleTurn.getMoveTurn(-1));
						return true;
					}
				}
				else if(item.getName().equalsIgnoreCase("HEAL BALL"))
				{
					if(player.getBattleField() instanceof WildBattleField)
					{
						WildBattleField w = (WildBattleField) player.getBattleField();
						if(!w.throwPokeball(PokeBall.HEALBALL))
							w.queueMove(0, BattleTurn.getMoveTurn(-1));
						return true;
					}
				}
				else if(item.getName().equalsIgnoreCase("QUICK BALL"))
				{
					if(player.getBattleField() instanceof WildBattleField)
					{
						WildBattleField w = (WildBattleField) player.getBattleField();
						if(!w.throwPokeball(PokeBall.QUICKBALL))
							w.queueMove(0, BattleTurn.getMoveTurn(-1));
						return true;
					}
				}
				else if(item.getName().equalsIgnoreCase("DUSK BALL"))
				{
					if(player.getBattleField() instanceof WildBattleField)
					{
						WildBattleField w = (WildBattleField) player.getBattleField();
						if(!w.throwPokeball(PokeBall.DUSKBALL))
							w.queueMove(0, BattleTurn.getMoveTurn(-1));
						return true;
					}
				}
				else if(item.getName().equalsIgnoreCase("CHERISH BALL"))
				{
					if(player.getBattleField() instanceof WildBattleField)
					{
						WildBattleField w = (WildBattleField) player.getBattleField();
						if(!w.throwPokeball(PokeBall.CHERISHBALL))
							w.queueMove(0, BattleTurn.getMoveTurn(-1));
						return true;
					}
				}
			}
			return false;
		}
		catch(Exception e)
		{
			return false;
		}
	}
}
