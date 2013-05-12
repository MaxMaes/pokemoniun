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
import org.pokenet.server.constants.ItemID;
import org.pokenet.server.constants.Potion;
import org.pokenet.server.constants.Rod;
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
	 * Uses an item in the player's bag.
	 * 
	 * @param player Reference to the player.
	 * @param itemId The id of the item to be used.
	 * @param data Extra data received from client
	 * @return True if the item can be used, otherwise false.
	 */
	public boolean useItem(Player player, int itemId, String[] data)
	{
		if(player.getBag().containsItem(itemId) < 0)
			return false;
		/* TODO: Rewrite this thread/function/monster in iterations. */
		int pokePartyPos = Integer.parseInt(data[0]);
		Pokemon poke = player.getParty()[pokePartyPos];
		Item item = GameServer.getServiceManager().getItemDatabase().getItem(itemId);
		String itemName = item.getName().toUpperCase();
		boolean returnValue = false;
		try
		{
			switch(itemId)
			{
				case ItemID.OLD_ROD:
					returnValue = processRod(player, Rod.OLD_ROD_LVL);
					return returnValue;
				case ItemID.GOOD_ROD:
					returnValue = processRod(player, Rod.GOOD_ROD_LVL);
					return returnValue;
				case ItemID.GREAT_ROD:
					returnValue = processRod(player, Rod.GREAT_ROD_LVL);
					return returnValue;
				case ItemID.ULTRA_ROD:
					returnValue = processRod(player, Rod.ULTRA_ROD_LVL);
					return returnValue;
				case ItemID.REPEL:
					player.setRepel(100);
					return true;
				case ItemID.SUPER_REPEL:
					player.setRepel(200);
					return true;
				case ItemID.MAX_REPEL:
					player.setRepel(250);
					return true;
				case ItemID.ESCAPE_ROPE:
					if(player.isBattling())
						return false;
					/* Warp the player to their last heal point */
					player.setX(player.getHealX());
					player.setY(player.getHealY());
					player.setMap(GameServer.getServiceManager().getMovementService().getMapMatrix().getMapByGamePosition(player.getHealMapX(), player.getHealMapY()), null);
					return true;
					/* default:
					 * return false; */
			}
			/* Determine what do to with the item */
			if(item.getAttributes().contains(ItemAttribute.MOVESLOT))
			{
				/* TMs & HMs */
				if(player.isBattling() || poke == null)
					return false;
				String moveName = itemName.substring(5);
				if(DataService.getMoveSetData().getMoveSet(poke.getSpeciesNumber()).canLearn(moveName))
				{
					poke.getMovesLearning().add(moveName);
					ServerMessage message = new ServerMessage(ClientPacket.MOVE_LEARN_LVL);
					message.addInt(pokePartyPos);
					message.addString(moveName);
					m_player.getSession().Send(message);
					return true;
				}
			}
			else if(item.getAttributes().contains(ItemAttribute.POKEMON))
			{
				/* Status healers, hold items, etc. */
				if(item.getCategory().equalsIgnoreCase("POTIONS"))
				{
					/* Potions */
					if(poke == null)
						return false;
					if(poke.getHealth() <= 0)
					{
						ServerMessage cantUse = new ServerMessage(ClientPacket.CANT_USE_ITEM);
						player.getSession().Send(cantUse);
						return false;
					}
					switch(itemId)
					{
						case 1:
							poke.changeHealth(Potion.POTION_HP);
							returnValue = processPotion(player, poke.getHealth(), itemId, pokePartyPos, "You used Potion on " + poke.getName() + "/nThe Potion restored 20 HP");
							break;
						case 2:
							poke.changeHealth(Potion.SUPER_POTION_HP);
							returnValue = processPotion(player, poke.getHealth(), itemId, pokePartyPos, "You used Super Potion on " + poke.getName() + "/nThe Super Potion restored 50 HP");
							break;
						case 3:
							poke.changeHealth(Potion.HYPER_POTION_HP);
							returnValue = processPotion(player, poke.getHealth(), itemId, pokePartyPos, "You used Hyper Potion on " + poke.getName() + "/nThe Hyper Potion restored 200 HP");
							break;
						case 4:
							poke.changeHealth(poke.getRawStat(0));
							returnValue = processPotion(player, poke.getHealth(), itemId, pokePartyPos, "You used Max Potion on " + poke.getName() + "/nThe Max Potion restored All HP");
							break;
						default:
							returnValue = false;
							break;
					}
					return returnValue;
				}
				else if(item.getCategory().equalsIgnoreCase("EVOLUTION"))
				{
					/* Evolution items can't be used in battle, Pokemon shouldn't be null */
					if(player.isBattling() || poke == null)
						return false;
					/* Get the pokemon's evolution data */
					PokemonSpecies pokeData = PokemonSpecies.getDefaultData().getPokemonByName(poke.getSpeciesName());
					for(int j = 0; j < pokeData.getEvolutions().length; j++)
					{
						PokemonEvolution evolution = pokeData.getEvolutions()[j];
						/* Check if this pokemon evolves by item */
						if(evolution.getType() == EvolutionTypes.Item)
						{
							/* TODO: Add Oval Stone? */
							/* Check if the item is an evolution stone If so, evolve the Pokemon */
							if(itemId == ItemID.FIRE_STONE && evolution.getAttribute().equalsIgnoreCase("FIRESTONE"))
								returnValue = evolveWithItem(evolution, poke, player);
							else if(itemId == ItemID.WATER_STONE && evolution.getAttribute().equalsIgnoreCase("WATERSTONE"))
								returnValue = evolveWithItem(evolution, poke, player);
							else if(itemId == ItemID.THUNDER_STONE && evolution.getAttribute().equalsIgnoreCase("THUNDERSTONE"))
								returnValue = evolveWithItem(evolution, poke, player);
							else if(itemId == ItemID.LEAF_STONE && evolution.getAttribute().equalsIgnoreCase("LEAFSTONE"))
								returnValue = evolveWithItem(evolution, poke, player);
							else if(itemId == ItemID.MOON_STONE && evolution.getAttribute().equalsIgnoreCase("MOONSTONE"))
								returnValue = evolveWithItem(evolution, poke, player);
							else if(itemId == ItemID.SUN_STONE && evolution.getAttribute().equalsIgnoreCase("SUNSTONE"))
								returnValue = evolveWithItem(evolution, poke, player);
							else if(itemId == ItemID.SHINY_STONE && evolution.getAttribute().equalsIgnoreCase("SHINYSTONE"))
								returnValue = evolveWithItem(evolution, poke, player);
							else if(itemId == ItemID.DUSK_STONE && evolution.getAttribute().equalsIgnoreCase("DUSKSTONE"))
								returnValue = evolveWithItem(evolution, poke, player);
							else if(itemId == ItemID.DAWN_STONE && evolution.getAttribute().equalsIgnoreCase("DAWNSTONE"))
								returnValue = evolveWithItem(evolution, poke, player);
							return returnValue;
						}
					}
				}
				else if(item.getCategory().equalsIgnoreCase("MEDICINE"))
				{
					if(poke == null)
						return false;
					// Check if this pokemon is alive to use all items but revive, TODO: Implement revive before this piece of code!
					if(poke.getHealth() <= 0)
					{
						ServerMessage cantUse = new ServerMessage(ClientPacket.CANT_USE_ITEM);
						player.getSession().Send(cantUse);
						return false;
					}

					if(itemId == 16)
					{ // Antidote
						String message = "You used Antidote on " + poke.getName() + "/nThe Antidote restored " + poke.getName() + " status to normal";
						poke.removeStatus(PoisonEffect.class);
						if(player.isBattling())
							player.getBattleField().executeItemTurn(itemId);
						else
						{
							ServerMessage itemUse = new ServerMessage(ClientPacket.USE_ITEM);
							itemUse.addString(message);
							player.getSession().Send(itemUse);
						}
						return true;
					}
					else if(itemId == 17)
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
					else if(itemId == 18)
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
					else if(itemId == 19)
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
					else if(itemId == 20)
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
					else if(itemId == 21)
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
					else if(itemName.equalsIgnoreCase("LAVA COOKIE"))
					{
						// just like a FULL HEAL
						poke.removeStatusEffects(true);
						if(player.isBattling())
						{
							player.getBattleField().executeItemTurn(itemId);
						}
						return true;
					}
					else if(itemName.equalsIgnoreCase("OLD GATEAU"))
					{
						// just like a FULL HEAL
						poke.removeStatusEffects(true);
						if(player.isBattling())
						{
							player.getBattleField().executeItemTurn(itemId);
						}
						return true;
					}
				}
				else if(item.getCategory().equalsIgnoreCase("FOOD"))
				{
					Random rand = new Random();
					if(poke == null)
						return false;
					if(itemId == 200)
					{ // Cheri Berry
						String message = poke.getName() + " ate the Cheri Berry/nThe Cheri Berry restored " + poke.getName() + " status to normal";
						poke.removeStatus(ParalysisEffect.class);
						if(player.isBattling())
							player.getBattleField().executeItemTurn(itemId);
						else
						{
							ServerMessage itemUse = new ServerMessage(ClientPacket.USE_ITEM);
							itemUse.addString(message);
							player.getSession().Send(itemUse);
						}
						return true;
					}
					else if(itemId == 201)
					{ // Chesto Berry
						String message = poke.getName() + " ate the Chesto Berry/nThe Chesto Berry restored " + poke.getName() + " status to normal";
						poke.removeStatus(SleepEffect.class);
						if(player.isBattling())
							player.getBattleField().executeItemTurn(itemId);
						else
						{
							ServerMessage itemUse = new ServerMessage(ClientPacket.USE_ITEM);
							itemUse.addString(message);
							player.getSession().Send(itemUse);
						}
						return true;
					}
					else if(itemId == 202)
					{ // Pecha Berry
						String message = poke.getName() + " ate the Pecha Berry/nThe Pecha Berry restored " + poke.getName() + " status to normal";
						poke.removeStatus(PoisonEffect.class);
						if(player.isBattling())
							player.getBattleField().executeItemTurn(itemId);
						else
						{
							ServerMessage itemUse = new ServerMessage(ClientPacket.USE_ITEM);
							itemUse.addString(message);
							player.getSession().Send(itemUse);
						}
						return true;
					}
					else if(itemId == 203)
					{ // Rawst Berry
						String message = poke.getName() + " ate the Rawst Berry/nThe Rawst Berry restored " + poke.getName() + " status to normal";
						poke.removeStatus(BurnEffect.class);
						if(player.isBattling())
							player.getBattleField().executeItemTurn(itemId);
						else
						{
							ServerMessage itemUse = new ServerMessage(ClientPacket.USE_ITEM);
							itemUse.addString(message);
							player.getSession().Send(itemUse);
						}
						return true;
					}
					else if(itemId == 204)
					{ // Aspear Berry
						String message = poke.getName() + " ate the Aspear Berry/nThe Aspear Berry restored " + poke.getName() + " status to normal";
						poke.removeStatus(FreezeEffect.class);
						if(player.isBattling())
							player.getBattleField().executeItemTurn(itemId);
						else
						{
							ServerMessage itemUse = new ServerMessage(ClientPacket.USE_ITEM);
							itemUse.addString(message);
							player.getSession().Send(itemUse);
						}
						return true;
					}
					else if(itemId == 205)
					{ // Leppa Berry
						String message = "Leppa Berry had no effect";
						/* Move selection not completed, temp message TODO: Add support for this */
						int ppSlot = Integer.parseInt(data[1]);
						if(poke.getPp(ppSlot) + 10 <= poke.getMaxPp(ppSlot))
							poke.setPp(ppSlot, poke.getPp(ppSlot) + 10);
						else
							poke.setPp(ppSlot, poke.getMaxPp(ppSlot));
						if(player.isBattling())
							player.getBattleField().executeItemTurn(itemId);
						else
						{
							ServerMessage itemUse = new ServerMessage(ClientPacket.USE_ITEM);
							itemUse.addString(message);
							player.getSession().Send(itemUse);
						}
						return true;
					}
					else if(itemId == 206)
					{ // Oran Berry
						String message = poke.getName() + " ate the Oran Berry/nThe Oran Berry restored 10HP";
						poke.changeHealth(10);
						if(!player.isBattling())
						{
							ServerMessage hpChange = new ServerMessage(ClientPacket.POKE_HP_CHANGE);
							hpChange.addInt(pokePartyPos);
							hpChange.addInt(poke.getHealth());
							player.getSession().Send(hpChange);
							ServerMessage itemUse = new ServerMessage(ClientPacket.USE_ITEM);
							itemUse.addString(message);
							player.getSession().Send(itemUse);
						}
						else
							player.getBattleField().executeItemTurn(itemId);
						return true;
					}
					else if(itemId == 207)
					{ // Persim Berry
						String message = poke.getName() + " ate the Persim Berry/nThe Persim Berry restored " + poke.getName() + " status to normal";
						poke.removeStatus(ConfuseEffect.class);
						if(player.isBattling())
							player.getBattleField().executeItemTurn(itemId);
						else
						{
							ServerMessage itemUse = new ServerMessage(ClientPacket.USE_ITEM);
							itemUse.addString(message);
							player.getSession().Send(itemUse);
							return true;
						}
					}
					else if(itemId == 208)
					{ // Lum Berry
						String message = poke.getName() + " ate the Lum Berry/nThe Lum Berry restored " + poke.getName() + " status to normal";
						poke.removeStatusEffects(true);
						if(player.isBattling())
							player.getBattleField().executeItemTurn(itemId);
						else
						{
							ServerMessage itemUse = new ServerMessage(ClientPacket.USE_ITEM);
							itemUse.addString(message);
							player.getSession().Send(itemUse);
							return true;
						}
					}
					else if(itemId == 209)
					{ // Sitrus Berry
						String message = poke.getName() + " ate the Sitrus Berry/nThe Sitrus Berry restored 30HP";
						poke.changeHealth(30);
						if(!player.isBattling())
						{
							ServerMessage hpChange = new ServerMessage(ClientPacket.POKE_HP_CHANGE);
							hpChange.addInt(pokePartyPos);
							hpChange.addInt(poke.getHealth());
							player.getSession().Send(hpChange);
							ServerMessage itemUse = new ServerMessage(ClientPacket.USE_ITEM);
							itemUse.addString(message);
							player.getSession().Send(itemUse);
						}
						else
							player.getBattleField().executeItemTurn(itemId);
						return true;
					}
					else if(itemId == 210)
					{ // Figy Berry
						String message = poke.getName() + " ate the Figy Berry/nThe Figy Berry restored" + poke.getRawStat(0) / 8 + " HP to " + poke.getName() + "!";
						poke.changeHealth(poke.getRawStat(0) / 8);
						if(!player.isBattling())
						{
							ServerMessage hpChange = new ServerMessage(ClientPacket.POKE_HP_CHANGE);
							hpChange.addInt(pokePartyPos);
							hpChange.addInt(poke.getHealth());
							player.getSession().Send(hpChange);
							ServerMessage itemUse = new ServerMessage(ClientPacket.USE_ITEM);
							itemUse.addString(message);
							player.getSession().Send(itemUse);
						}
						else
							player.getBattleField().executeItemTurn(itemId);
						return true;
					}
					else if(itemId == 214)
					{ // Wiki Berry
						String message = poke.getName() + " ate the Wiki Berry/nThe Wiki Berry restored" + poke.getRawStat(0) / 8 + " HP to " + poke.getName() + "!";
						poke.changeHealth(poke.getRawStat(0) / 8);
						if(!player.isBattling())
						{
							ServerMessage hpChange = new ServerMessage(ClientPacket.POKE_HP_CHANGE);
							hpChange.addInt(pokePartyPos);
							hpChange.addInt(poke.getHealth());
							player.getSession().Send(hpChange);
							ServerMessage itemUse = new ServerMessage(ClientPacket.USE_ITEM);
							itemUse.addString(message);
							player.getSession().Send(itemUse);
						}
						else
							player.getBattleField().executeItemTurn(itemId);
						return true;
					}
					else if(itemId == 212)
					{ // Mago Berry
						String message = poke.getName() + " ate the Mago Berry/nThe Mago Berry restored" + poke.getRawStat(0) / 8 + " HP to " + poke.getName() + "!";
						poke.changeHealth(poke.getRawStat(0) / 8);
						if(!player.isBattling())
						{
							ServerMessage hpChange = new ServerMessage(ClientPacket.POKE_HP_CHANGE);
							hpChange.addInt(pokePartyPos);
							hpChange.addInt(poke.getHealth());
							player.getSession().Send(hpChange);
							ServerMessage itemUse = new ServerMessage(ClientPacket.USE_ITEM);
							itemUse.addString(message);
							player.getSession().Send(itemUse);
						}
						else
							player.getBattleField().executeItemTurn(itemId);
						return true;
					}
					else if(itemId == 213)
					{ // Aguav Berry
						String message = poke.getName() + " ate the Aguav Berry/nThe Aguav Berry restored" + poke.getRawStat(0) / 8 + " HP to " + poke.getName() + "!";
						poke.changeHealth(poke.getRawStat(0) / 8);
						if(!player.isBattling())
						{
							ServerMessage hpChange = new ServerMessage(ClientPacket.POKE_HP_CHANGE);
							hpChange.addInt(pokePartyPos);
							hpChange.addInt(poke.getHealth());
							player.getSession().Send(hpChange);
							ServerMessage itemUse = new ServerMessage(ClientPacket.USE_ITEM);
							itemUse.addString(message);
							player.getSession().Send(itemUse);
						}
						else
							player.getBattleField().executeItemTurn(itemId);
						return true;
					}
					else if(itemId == 214)
					{ // Iapapa Berry
						String message = poke.getName() + " ate the Iapapa Berry/nThe Iapapa Berry restored" + poke.getRawStat(0) / 8 + " HP to " + poke.getName() + "!";
						poke.changeHealth(poke.getRawStat(0) / 8);
						if(!player.isBattling())
						{
							ServerMessage hpChange = new ServerMessage(ClientPacket.POKE_HP_CHANGE);
							hpChange.addInt(pokePartyPos);
							hpChange.addInt(poke.getHealth());
							player.getSession().Send(hpChange);
							ServerMessage itemUse = new ServerMessage(ClientPacket.USE_ITEM);
							itemUse.addString(message);
							player.getSession().Send(itemUse);
						}
						else
							player.getBattleField().executeItemTurn(itemId);
						return true;
					}
					else if(itemId == 800)
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
							player.getBattleField().executeItemTurn(itemId);
						}
						else
						{
							ServerMessage hpChange = new ServerMessage(ClientPacket.POKE_HP_CHANGE);
							hpChange.addInt(pokePartyPos);
							hpChange.addInt(poke.getHealth());
							player.getSession().Send(hpChange);
							ServerMessage itemUse = new ServerMessage(ClientPacket.USE_ITEM);
							itemUse.addString(message);
							player.getSession().Send(itemUse);
						}
						return true;
					}
					else if(itemId == 801)
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
							player.getBattleField().executeItemTurn(itemId);
						else
						{
							ServerMessage itemUse = new ServerMessage(ClientPacket.USE_ITEM);
							itemUse.addString(message);
							player.getSession().Send(itemUse);
						}
						return true;
					}
					else if(itemId == 802)
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
							player.getBattleField().executeItemTurn(itemId);
						}
						else
						{
							ServerMessage hpChange = new ServerMessage(ClientPacket.POKE_HP_CHANGE);
							hpChange.addInt(pokePartyPos);
							hpChange.addInt(poke.getHealth());
							player.getSession().Send(hpChange);
							ServerMessage itemUse = new ServerMessage(ClientPacket.USE_ITEM);
							itemUse.addString(message);
							player.getSession().Send(itemUse);
						}
						return true;
					}
					else if(itemId == 803)
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
							player.getBattleField().executeItemTurn(itemId);
						else
						{
							ServerMessage itemUse = new ServerMessage(ClientPacket.USE_ITEM);
							itemUse.addString(message);
							player.getSession().Send(itemUse);
							return true;
						}
					}
					else if(itemId == 804)
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
							player.getBattleField().executeItemTurn(itemId);
						else
						{
							ServerMessage itemUse = new ServerMessage(ClientPacket.USE_ITEM);
							itemUse.addString(message);
							player.getSession().Send(itemUse);
							return true;
						}
					}
					else if(itemId == 805)
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
							player.getBattleField().executeItemTurn(itemId);
						else
						{
							ServerMessage itemUse = new ServerMessage(ClientPacket.USE_ITEM);
							itemUse.addString(message);
							player.getSession().Send(itemUse);
							return true;
						}
					}
					else if(itemId == 806)
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
							hpChange.addInt(pokePartyPos);
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
				if(itemName.equalsIgnoreCase("POKE BALL"))
				{
					if(player.getBattleField() instanceof WildBattleField)
					{
						WildBattleField w = (WildBattleField) player.getBattleField();
						if(!w.throwPokeball(PokeBall.POKEBALL))
							w.queueMove(0, BattleTurn.getMoveTurn(-1));
						return true;
					}
				}
				else if(itemName.equalsIgnoreCase("GREAT BALL"))
				{
					if(player.getBattleField() instanceof WildBattleField)
					{
						WildBattleField w = (WildBattleField) player.getBattleField();
						if(!w.throwPokeball(PokeBall.GREATBALL))
							w.queueMove(0, BattleTurn.getMoveTurn(-1));
						return true;
					}
				}
				else if(itemName.equalsIgnoreCase("ULTRA BALL"))
				{
					if(player.getBattleField() instanceof WildBattleField)
					{
						WildBattleField w = (WildBattleField) player.getBattleField();
						if(!w.throwPokeball(PokeBall.ULTRABALL))
							w.queueMove(0, BattleTurn.getMoveTurn(-1));
						return true;
					}
				}
				else if(itemName.equalsIgnoreCase("MASTER BALL"))
				{
					if(player.getBattleField() instanceof WildBattleField)
					{
						WildBattleField w = (WildBattleField) player.getBattleField();
						if(!w.throwPokeball(PokeBall.MASTERBALL))
							w.queueMove(0, BattleTurn.getMoveTurn(-1));
						return true;
					}
				}
				else if(itemName.equalsIgnoreCase("LEVEL BALL"))
				{
					if(player.getBattleField() instanceof WildBattleField)
					{
						WildBattleField w = (WildBattleField) player.getBattleField();
						if(!w.throwPokeball(PokeBall.LEVELBALL))
							w.queueMove(0, BattleTurn.getMoveTurn(-1));
						return true;
					}
				}
				else if(itemName.equalsIgnoreCase("LURE BALL"))
				{
					if(player.getBattleField() instanceof WildBattleField)
					{
						WildBattleField w = (WildBattleField) player.getBattleField();
						if(!w.throwPokeball(PokeBall.LUREBALL))
							w.queueMove(0, BattleTurn.getMoveTurn(-1));
						return true;
					}
				}
				else if(itemName.equalsIgnoreCase("MOON BALL"))
				{
					if(player.getBattleField() instanceof WildBattleField)
					{
						WildBattleField w = (WildBattleField) player.getBattleField();
						if(!w.throwPokeball(PokeBall.MOONBALL))
							w.queueMove(0, BattleTurn.getMoveTurn(-1));
						return true;
					}
				}
				else if(itemName.equalsIgnoreCase("FRIEND BALL"))
				{
					if(player.getBattleField() instanceof WildBattleField)
					{
						WildBattleField w = (WildBattleField) player.getBattleField();
						if(!w.throwPokeball(PokeBall.FRIENDBALL))
							w.queueMove(0, BattleTurn.getMoveTurn(-1));
						return true;
					}
				}
				else if(itemName.equalsIgnoreCase("LOVE BALL"))
				{
					if(player.getBattleField() instanceof WildBattleField)
					{
						WildBattleField w = (WildBattleField) player.getBattleField();
						if(!w.throwPokeball(PokeBall.LOVEBALL))
							w.queueMove(0, BattleTurn.getMoveTurn(-1));
						return true;
					}
				}
				else if(itemName.equalsIgnoreCase("HEAVY BALL"))
				{
					if(player.getBattleField() instanceof WildBattleField)
					{
						WildBattleField w = (WildBattleField) player.getBattleField();
						if(!w.throwPokeball(PokeBall.HEAVYBALL))
							w.queueMove(0, BattleTurn.getMoveTurn(-1));
						return true;
					}
				}
				else if(itemName.equalsIgnoreCase("FAST BALL"))
				{
					if(player.getBattleField() instanceof WildBattleField)
					{
						WildBattleField w = (WildBattleField) player.getBattleField();
						if(!w.throwPokeball(PokeBall.FASTBALL))
							w.queueMove(0, BattleTurn.getMoveTurn(-1));
						return true;
					}
				}
				else if(itemName.equalsIgnoreCase("PARK BALL"))
				{
					if(player.getBattleField() instanceof WildBattleField)
					{
						WildBattleField w = (WildBattleField) player.getBattleField();
						if(!w.throwPokeball(PokeBall.PARKBALL))
							w.queueMove(0, BattleTurn.getMoveTurn(-1));
						return true;
					}
				}
				else if(itemName.equalsIgnoreCase("PREMIER BALL"))
				{
					if(player.getBattleField() instanceof WildBattleField)
					{
						WildBattleField w = (WildBattleField) player.getBattleField();
						if(!w.throwPokeball(PokeBall.PREMIERBALL))
							w.queueMove(0, BattleTurn.getMoveTurn(-1));
						return true;
					}
				}
				else if(itemName.equalsIgnoreCase("REPEAT BALL"))
				{
					if(player.getBattleField() instanceof WildBattleField)
					{
						WildBattleField w = (WildBattleField) player.getBattleField();
						if(!w.throwPokeball(PokeBall.REPEATBALL))
							w.queueMove(0, BattleTurn.getMoveTurn(-1));
						return true;
					}
				}
				else if(itemName.equalsIgnoreCase("TIMER BALL"))
				{
					if(player.getBattleField() instanceof WildBattleField)
					{
						WildBattleField w = (WildBattleField) player.getBattleField();
						if(!w.throwPokeball(PokeBall.TIMERBALL))
							w.queueMove(0, BattleTurn.getMoveTurn(-1));
						return true;
					}
				}
				else if(itemName.equalsIgnoreCase("NEST BALL"))
				{
					if(player.getBattleField() instanceof WildBattleField)
					{
						WildBattleField w = (WildBattleField) player.getBattleField();
						if(!w.throwPokeball(PokeBall.NESTBALL))
							w.queueMove(0, BattleTurn.getMoveTurn(-1));
						return true;
					}
				}
				else if(itemName.equalsIgnoreCase("NET BALL"))
				{
					if(player.getBattleField() instanceof WildBattleField)
					{
						WildBattleField w = (WildBattleField) player.getBattleField();
						if(!w.throwPokeball(PokeBall.NETBALL))
							w.queueMove(0, BattleTurn.getMoveTurn(-1));
						return true;
					}
				}
				else if(itemName.equalsIgnoreCase("DIVE BALL"))
				{
					if(player.getBattleField() instanceof WildBattleField)
					{
						WildBattleField w = (WildBattleField) player.getBattleField();
						if(!w.throwPokeball(PokeBall.DIVEBALL))
							w.queueMove(0, BattleTurn.getMoveTurn(-1));
						return true;
					}
				}
				else if(itemName.equalsIgnoreCase("LUXURY BALL"))
				{
					if(player.getBattleField() instanceof WildBattleField)
					{
						WildBattleField w = (WildBattleField) player.getBattleField();
						if(!w.throwPokeball(PokeBall.LUXURY))
							w.queueMove(0, BattleTurn.getMoveTurn(-1));
						return true;
					}
				}
				else if(itemName.equalsIgnoreCase("HEAL BALL"))
				{
					if(player.getBattleField() instanceof WildBattleField)
					{
						WildBattleField w = (WildBattleField) player.getBattleField();
						if(!w.throwPokeball(PokeBall.HEALBALL))
							w.queueMove(0, BattleTurn.getMoveTurn(-1));
						return true;
					}
				}
				else if(itemName.equalsIgnoreCase("QUICK BALL"))
				{
					if(player.getBattleField() instanceof WildBattleField)
					{
						WildBattleField w = (WildBattleField) player.getBattleField();
						if(!w.throwPokeball(PokeBall.QUICKBALL))
							w.queueMove(0, BattleTurn.getMoveTurn(-1));
						return true;
					}
				}
				else if(itemName.equalsIgnoreCase("DUSK BALL"))
				{
					if(player.getBattleField() instanceof WildBattleField)
					{
						WildBattleField w = (WildBattleField) player.getBattleField();
						if(!w.throwPokeball(PokeBall.DUSKBALL))
							w.queueMove(0, BattleTurn.getMoveTurn(-1));
						return true;
					}
				}
				else if(itemName.equalsIgnoreCase("CHERISH BALL"))
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

	/**
	 * Processes the type of fishing rod and fishes if the player can use it.
	 * 
	 * @param player Reference to the player.
	 * @param rodLvl The required lvl to use the fishing rod.
	 * @return True is the player can use the fishing rod, otherwise false.
	 */
	private boolean processRod(Player player, int rodLvl)
	{
		if(!player.isBattling() && !player.isFishing())
		{
			if(player.getFishingLevel() >= rodLvl)
				player.fish(rodLvl);
			else
			{
				ServerMessage message = new ServerMessage(m_player.getSession());
				message.init(ClientPacket.CANT_USE_ROD.getValue());
				message.addInt(rodLvl);
				message.sendResponse();
				return false;
			}
		}
		return true;
	}

	/**
	 * Processes the potion's effects and sends them to the client.
	 * 
	 * @param player Reference to the player.
	 * @param pokeHp The pokemons current HP.
	 * @param itemId The id of the used item.
	 * @param pokeId The pokemons position in the players party.
	 * @param message The message to be sent to the client.
	 * @return True, unless an exception is thrown.
	 */
	private boolean processPotion(Player player, int pokeHp, int itemId, int pokeId, String message)
	{
		if(!player.isBattling())
		{
			ServerMessage hpChange = new ServerMessage(ClientPacket.POKE_HP_CHANGE);
			hpChange.addInt(pokeId);
			hpChange.addInt(pokeHp);
			m_player.getSession().Send(hpChange);
			ServerMessage itemUse = new ServerMessage(ClientPacket.USE_ITEM);
			itemUse.addString(message);
			m_player.getSession().Send(itemUse);
		}
		else
		{
			player.getBattleField().executeItemTurn(itemId);
		}
		return true;
	}

	/**
	 * Starts the evolution progress for the Pokemon.
	 * 
	 * @param evolution The evolution type.
	 * @param poke The Pokemon to evolve.
	 * @param player The owner of the Pokemon
	 * @return True in all cases.
	 */
	private boolean evolveWithItem(PokemonEvolution evolution, Pokemon poke, Player player)
	{
		poke.setEvolution(evolution);
		poke.evolutionResponse(true, player);
		return true;
	}
}
