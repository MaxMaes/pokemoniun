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
import org.pokenet.server.battle.mechanics.MoveQueueException;
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
		/* TODO: TM's and Balls left to test. */
		if(player.getBag().containsItem(itemId) < 0)
			return false;
		Item item = GameServer.getServiceManager().getItemDatabase().getItem(itemId);
		boolean returnValue = false;
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
		}
		/* Determine what do to with the item */
		if(item.getAttributes().contains(ItemAttribute.MOVESLOT))
		{
			int pokePartyPos = Integer.parseInt(data[0]);
			Pokemon poke = player.getParty()[pokePartyPos];
			/* TMs & HMs */
			if(player.isBattling() || poke == null)
				return false;
			String moveName = item.getName().toUpperCase().substring(5);
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
			int pokePartyPos = Integer.parseInt(data[0]);
			Pokemon poke = player.getParty()[pokePartyPos];
			/* Status healers, hold items, etc. */
			if(item.getCategory().equalsIgnoreCase("POTIONS"))
			{
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
					case ItemID.POTION:
						String message = "You used a Potion on " + poke.getName() + "./nThe Potion restored 20 HP.";
						poke.changeHealth(Potion.POTION_HP);
						returnValue = processPotion(player, poke.getHealth(), itemId, pokePartyPos, message);
						break;
					case ItemID.SUPER_POTION:
						message = "You used a Super Potion on " + poke.getName() + "./nThe Super Potion restored 50 HP.";
						poke.changeHealth(Potion.SUPER_POTION_HP);
						returnValue = processPotion(player, poke.getHealth(), itemId, pokePartyPos, message);
						break;
					case ItemID.HYPER_POTION:
						message = "You used a Hyper Potion on " + poke.getName() + "./nThe Hyper Potion restored 200 HP.";
						poke.changeHealth(Potion.HYPER_POTION_HP);
						returnValue = processPotion(player, poke.getHealth(), itemId, pokePartyPos, message);
						break;
					case ItemID.MAX_POTION:
						message = "You used a Max Potion on " + poke.getName() + "./nThe Max Potion restored " + poke.getRawStat(0) + " HP.";
						poke.changeHealth(poke.getRawStat(0));
						returnValue = processPotion(player, poke.getHealth(), itemId, pokePartyPos, message);
						break;
					case ItemID.FULL_RESTORE:
						message = "You used a Full Restore on " + poke.getName() + "./nThe Full Restore restored " + poke.getRawStat(0) + " HP.";
						poke.changeHealth(poke.getRawStat(0));
						ServerMessage hpChange = new ServerMessage(ClientPacket.POKE_HP_CHANGE);
						hpChange.addInt(pokePartyPos);
						hpChange.addInt(poke.getHealth());
						player.getSession().Send(hpChange);
						returnValue = processCureStatus(player, poke, itemId, message);
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

					}

				}
				return returnValue;
			}
			else if(item.getCategory().equalsIgnoreCase("MEDICINE"))
			{
				if(poke == null)
					return false;
				/* TODO: Implement revive before this piece of code! */
				if(poke.getHealth() <= 0)
				{
					ServerMessage cantUse = new ServerMessage(ClientPacket.CANT_USE_ITEM);
					player.getSession().Send(cantUse);
					return false;
				}
				switch(itemId)
				{
					case ItemID.ANTIDOTE:
						String message = "You used an Antidote on " + poke.getName() + "./nThe Antidote restored " + poke.getName() + " status to normal";
						returnValue = processStatusRemoval(player, poke, itemId, PoisonEffect.class, message);
						break;
					case ItemID.PARALYZ_HEAL:
						message = "You used a Paralyz Heal on " + poke.getName() + "./nThe Paralyz Heal restored " + poke.getName() + " status to normal";
						returnValue = processStatusRemoval(player, poke, itemId, ParalysisEffect.class, message);
						break;
					case ItemID.AWAKENING:
						message = "You used an Awakening on " + poke.getName() + "./nThe Awakening restored " + poke.getName() + " status to normal";
						returnValue = processStatusRemoval(player, poke, itemId, SleepEffect.class, message);
						break;
					case ItemID.BURN_HEAL:
						message = "You used a Burn Heal on " + poke.getName() + "./nThe Burn Heal restored " + poke.getName() + " status to normal";
						returnValue = processStatusRemoval(player, poke, itemId, BurnEffect.class, message);
						break;
					case ItemID.ICE_HEAL:
						message = "You used an Ice Heal on " + poke.getName() + "./nThe Ice Heal restored " + poke.getName() + " status to normal";
						returnValue = processStatusRemoval(player, poke, itemId, FreezeEffect.class, message);
						break;
					case ItemID.FULL_HEAL:
						message = "You used a Full Heal on " + poke.getName() + "./nThe Full Heal restored " + poke.getName() + " status to normal";
						returnValue = processCureStatus(player, poke, itemId, message);
						break;
					case ItemID.LAVA_COOKIE:
						message = "You used a Lava Cookie on " + poke.getName() + "./nThe Lava Cookie restored " + poke.getName() + " status to normal";
						returnValue = processCureStatus(player, poke, itemId, message);
						break;
					case ItemID.OLD_GATEAU:
						message = "You used an Old Gateau on " + poke.getName() + "./nThe Old Gateau restored " + poke.getName() + " status to normal";
						returnValue = processCureStatus(player, poke, itemId, message);
						break;
				}
				return returnValue;
			}
			else if(item.getCategory().equalsIgnoreCase("FOOD"))
			{
				Random rand = new Random();
				if(poke == null)
					return false;
				switch(itemId)
				{
					case ItemID.CHERI_BERRY:
						String message = poke.getName() + " ate the Cheri Berry./nThe Cheri Berry restored " + poke.getName() + " status to normal";
						returnValue = processStatusRemoval(player, poke, itemId, ParalysisEffect.class, message);
						break;
					case ItemID.CHESTO_BERRY:
						message = poke.getName() + " ate the Chesto Berry./nThe Chesto Berry restored " + poke.getName() + " status to normal";
						returnValue = processStatusRemoval(player, poke, itemId, SleepEffect.class, message);
						break;
					case ItemID.PECHA_BERRY:
						message = poke.getName() + " ate the Pecha Berry./nThe Pecha Berry restored " + poke.getName() + " status to normal";
						returnValue = processStatusRemoval(player, poke, itemId, PoisonEffect.class, message);
						break;
					case ItemID.RAWST_BERRY:
						message = poke.getName() + " ate the Rawst Berry./nThe Rawst Berry restored " + poke.getName() + " status to normal";
						returnValue = processStatusRemoval(player, poke, itemId, BurnEffect.class, message);
						break;
					case ItemID.ASPEAR_BERRY:
						message = poke.getName() + " ate the Aspear Berry./nThe Aspear Berry restored " + poke.getName() + " status to normal";
						returnValue = processStatusRemoval(player, poke, itemId, FreezeEffect.class, message);
						break;
					case ItemID.LEPPA_BERRY:
						message = "Leppa Berry had no effect.";
						/* Move selection not completed, temp message TODO: Add support for this */
						int ppSlot = Integer.parseInt(data[1]);
						if(poke.getPp(ppSlot) + 10 <= poke.getMaxPp(ppSlot))
							poke.setPp(ppSlot, poke.getPp(ppSlot) + 10);
						else
							poke.setPp(ppSlot, poke.getMaxPp(ppSlot));
						returnValue = itemUsedMessage(player, itemId, message);
						break;
					case ItemID.ORAN_BERRY:
						poke.changeHealth(10);
						returnValue = processPotion(player, poke.getHealth(), itemId, pokePartyPos, poke.getName() + " ate the Oran Berry./nThe Oran Berry restored 10HP");
						break;
					case ItemID.PERSIM_BERRY:
						message = poke.getName() + " ate the Persim Berry./nThe Persim Berry restored " + poke.getName() + " status to normal";
						returnValue = processStatusRemoval(player, poke, itemId, ConfuseEffect.class, message);
						break;
					case ItemID.LUM_BERRY:
						message = poke.getName() + " ate the Lum Berry./nThe Lum Berry restored " + poke.getName() + " status to normal";
						returnValue = processCureStatus(player, poke, itemId, message);
						break;
					case ItemID.SITRUS_BERRY:
						poke.changeHealth(30);
						returnValue = processPotion(player, poke.getHealth(), itemId, pokePartyPos, poke.getName() + " ate the Sitrus Berry/nThe Sitrus Berry restored 30HP");
						break;
					case ItemID.FIGY_BERRY:
						poke.changeHealth(poke.getRawStat(0) / 8);
						message = poke.getName() + " ate the Figy Berry./nThe Figy Berry restored" + poke.getRawStat(0) / 8 + " HP to " + poke.getName() + "!";
						returnValue = processPotion(player, poke.getHealth(), itemId, pokePartyPos, message);
						break;
					case ItemID.WIKI_BERRY:
						poke.changeHealth(poke.getRawStat(0) / 8);
						message = poke.getName() + " ate the Wiki Berry./nThe Wiki Berry restored" + poke.getRawStat(0) / 8 + " HP to " + poke.getName() + "!";
						returnValue = processPotion(player, poke.getHealth(), itemId, pokePartyPos, message);
						break;
					case ItemID.MAGO_BERRY:
						poke.changeHealth(poke.getRawStat(0) / 8);
						message = poke.getName() + " ate the Mago Berry./nThe Mago Berry restored" + poke.getRawStat(0) / 8 + " HP to " + poke.getName() + "!";
						returnValue = processPotion(player, poke.getHealth(), itemId, pokePartyPos, message);
						break;
					case ItemID.AGUAV_BERRY:
						poke.changeHealth(poke.getRawStat(0) / 8);
						message = poke.getName() + " ate the Aguav Berry./nThe Aguav Berry restored" + poke.getRawStat(0) / 8 + " HP to " + poke.getName() + "!";
						returnValue = processPotion(player, poke.getHealth(), itemId, pokePartyPos, message);
						break;
					case ItemID.IAPAPA_BERRY:
						poke.changeHealth(poke.getRawStat(0) / 8);
						message = poke.getName() + " ate the Iapapa Berry./nThe Iapapa Berry restored" + poke.getRawStat(0) / 8 + " HP to " + poke.getName() + "!";
						returnValue = processPotion(player, poke.getHealth(), itemId, pokePartyPos, message);
						break;
					case ItemID.VOLTORB_LOLLIPOP:
						message = poke.getName() + " ate the Voltorb Lollipop./nThe Lollipop restored 50 HP to " + poke.getName() + "!";
						poke.changeHealth(50);
						int random = rand.nextInt(10);
						if(random < 3)
						{
							poke.addStatus(new ParalysisEffect());
							message += "/n" + poke.getName() + " was Paralyzed from the Lollipop!";
						}
						returnValue = processPotion(player, poke.getHealth(), itemId, pokePartyPos, message);
						break;
					case ItemID.SWEET_CHILLS:
						message = poke.getName() + " ate the Sweet Chill/nThe Sweet Chill restored " + poke.getName() + "'s moves!";
						for(ppSlot = 0; ppSlot < 4; ppSlot++)
						{
							if(poke.getPp(ppSlot) + 5 <= poke.getMaxPp(ppSlot))
								poke.setPp(ppSlot, poke.getPp(ppSlot) + 5);
							else
								poke.setPp(ppSlot, poke.getMaxPp(ppSlot));
						}
						random = rand.nextInt(10);
						if(random < 3)
						{
							poke.addStatus(new FreezeEffect());
							message += "/n" + poke.getName() + " was frozen solid from the cold candy!";
						}
						returnValue = itemUsedMessage(player, itemId, message);
						break;
					case ItemID.CINNAMON_CANDY:
						message = poke.getName() + " ate the Cinnamon Candy./nThe Cinnamon Candy restored " + poke.getName() + "'s status to normal!";
						poke.removeStatusEffects(true);
						random = rand.nextInt(10);
						if(random < 3)
						{
							poke.addStatus(new BurnEffect());
							message += "/n" + poke.getName() + " was burned from the candy!";
						}
						processPotion(player, poke.getHealth(), itemId, pokePartyPos, message);
						break;
					case ItemID.CANDY_CORN:
						message = poke.getName() + " ate the Candy Corn./n" + poke.getName() + " is happier!";
						int happiness = poke.getHappiness() + 15;
						if(happiness <= 300)
							poke.setHappiness(happiness);
						else
							poke.setHappiness(300);
						random = rand.nextInt(10);
						if(random < 3)
						{
							poke.addStatus(new PoisonEffect());
							message += "/n" + poke.getName() + " got Poisoned from the rotten candy!";
						}
						returnValue = itemUsedMessage(player, itemId, message);
						break;
					case ItemID.POKE_CHOC:
						message = poke.getName() + " ate the Poke'Choc Bar!/n" + poke.getName() + " is happier!";
						happiness = poke.getHappiness() + 10;
						if(happiness <= 300)
							poke.setHappiness(happiness);
						else
							poke.setHappiness(300);
						random = rand.nextInt(10);
						if(random <= 3)
						{
							poke.changeHealth(30);
							message += "/n" + poke.getName() + " recovered 30HP.";
						}
						returnValue = itemUsedMessage(player, itemId, message);
						break;
					case ItemID.GUMMILAX:
						message = poke.getName() + " ate the Gummilax./n" + poke.getName() + " is happier!";
						happiness = poke.getHappiness() + rand.nextInt(30);
						if(happiness <= 255)
							poke.setHappiness(happiness);
						else
							poke.setHappiness(255);
						random = rand.nextInt(10);
						if(random < 3)
						{
							poke.addStatus(new ParalysisEffect());
							message += "/nThe gummi was too sweet for " + poke.getName() + "./n" + poke.getName() + " fell asleep!";
						}
						returnValue = itemUsedMessage(player, itemId, message);
						break;
					case ItemID.GENGUM:
						message = poke.getName() + " ate the Gengum.";
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
						returnValue = processPotion(player, poke.getHealth(), itemId, pokePartyPos, message);
						break;
				}
				return returnValue;
			}
		}
		else if(item.getAttributes().contains(ItemAttribute.BATTLE))
		{
			/* Pokeballs */
			try
			{
				switch(itemId)
				{
					case ItemID.POKE_BALL:
						returnValue = processBallUsage(player, PokeBall.POKEBALL);
						break;
					case ItemID.GREAT_BALL:
						returnValue = processBallUsage(player, PokeBall.GREATBALL);
						break;
					case ItemID.ULTRA_BALL:
						returnValue = processBallUsage(player, PokeBall.ULTRABALL);
						break;
					case ItemID.MASTER_BALL:
						returnValue = processBallUsage(player, PokeBall.MASTERBALL);
						break;
					case ItemID.SAFARI_BALL:
						returnValue = processBallUsage(player, PokeBall.SAFARIBALL);
						break;
					case ItemID.PARK_BALL:
						returnValue = processBallUsage(player, PokeBall.PARKBALL);
						break;
					case ItemID.LEVEL_BALL:
						returnValue = processBallUsage(player, PokeBall.LEVELBALL);
						break;
					case ItemID.LURE_BALL:
						returnValue = processBallUsage(player, PokeBall.LUREBALL);
						break;
					case ItemID.MOON_BALL:
						returnValue = processBallUsage(player, PokeBall.MOONBALL);
						break;
					case ItemID.FRIEND_BALL:
						returnValue = processBallUsage(player, PokeBall.FRIENDBALL);
						break;
					case ItemID.FAST_BALL:
						returnValue = processBallUsage(player, PokeBall.FASTBALL);
						break;
					case ItemID.HEAVY_BALL:
						returnValue = processBallUsage(player, PokeBall.HEAVYBALL);
						break;
					case ItemID.LOVE_BALL:
						returnValue = processBallUsage(player, PokeBall.LOVEBALL);
						break;
					case ItemID.LUXURY_BALL:
						returnValue = processBallUsage(player, PokeBall.LUXURY);
						break;
					case ItemID.PREMIER_BALL:
						returnValue = processBallUsage(player, PokeBall.PREMIERBALL);
						break;
					case ItemID.NET_BALL:
						returnValue = processBallUsage(player, PokeBall.NETBALL);
						break;
					case ItemID.DIVE_BALL:
						returnValue = processBallUsage(player, PokeBall.DIVEBALL);
						break;
					case ItemID.NEST_BALL:
						returnValue = processBallUsage(player, PokeBall.NESTBALL);
						break;
					case ItemID.REPEAT_BALL:
						returnValue = processBallUsage(player, PokeBall.REPEATBALL);
						break;
					case ItemID.TIMER_BALL:
						returnValue = processBallUsage(player, PokeBall.TIMERBALL);
						break;
					case ItemID.HEAL_BALL:
						returnValue = processBallUsage(player, PokeBall.HEALBALL);
						break;
					case ItemID.DUSK_BALL:
						returnValue = processBallUsage(player, PokeBall.DUSKBALL);
						break;
					case ItemID.CHERISH_BALL:
						returnValue = processBallUsage(player, PokeBall.CHERISHBALL);
						break;
					case ItemID.QUICK_BALL:
						returnValue = processBallUsage(player, PokeBall.QUICKBALL);
						break;
				}
				return returnValue;
			}
			catch(MoveQueueException mqe)
			{
				return false;
			}
		}
		return false;
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
			player.getSession().Send(hpChange);
			ServerMessage itemUse = new ServerMessage(ClientPacket.USE_ITEM);
			itemUse.addString(message);
			player.getSession().Send(itemUse);
		}
		else
			player.getBattleField().executeItemTurn(itemId);
		return true;
	}

	private boolean processStatusRemoval(Player player, Pokemon poke, int itemId, Class<?> effect, String message)
	{
		poke.removeStatus(effect);
		return itemUsedMessage(player, itemId, message);
	}

	private boolean processCureStatus(Player player, Pokemon poke, int itemId, String message)
	{
		poke.removeStatusEffects(true);
		return itemUsedMessage(player, itemId, message);
	}

	private boolean processBallUsage(Player player, PokeBall pokeball) throws MoveQueueException
	{
		if(player.getBattleField() instanceof WildBattleField)
		{
			WildBattleField w = (WildBattleField) player.getBattleField();
			if(!w.throwPokeball(PokeBall.POKEBALL))
				w.queueMove(0, BattleTurn.getMoveTurn(-1));
			return true;
		}
		return false;
	}

	private boolean itemUsedMessage(Player player, int itemId, String message)
	{
		if(!player.isBattling())
		{
			ServerMessage itemUse = new ServerMessage(ClientPacket.USE_ITEM);
			itemUse.addString(message);
			player.getSession().Send(itemUse);
		}
		else
			player.getBattleField().executeItemTurn(itemId);
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
