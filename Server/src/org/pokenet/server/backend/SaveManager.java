package org.pokenet.server.backend;

import java.sql.ResultSet;
import org.pokenet.server.backend.entity.Bag;
import org.pokenet.server.backend.entity.BagItem;
import org.pokenet.server.backend.entity.Player;
import org.pokenet.server.battle.DataService;
import org.pokenet.server.battle.Pokemon;
import org.pokenet.server.battle.PokemonSpecies;
import org.pokenet.server.battle.mechanics.statuses.abilities.IntrinsicAbility;
import org.pokenet.server.network.MySqlManager;

public class SaveManager
{
	private MySqlManager m_database;
	private int fail = 0;

	/* TODO: Check some queries and possibly rewrite. */
	public SaveManager()
	{
		m_database = MySqlManager.getInstance();
	}

	/**
	 * Saves a bag to the database.
	 * 
	 * @param bag
	 * @return
	 */
	public boolean saveBag(Bag bag)
	{
		for(BagItem item : bag.getItems())
		{
			if(item != null)
				m_database.query("INSERT INTO pn_bag (member,item,quantity) VALUES (" + bag.getMemberId() + ", " + item.getItemNumber() + ", " + item.getQuantity()
						+ ") ON DUPLICATE KEY UPDATE quantity = " + item.getQuantity() + ";");
		}
		return true;
	}

	/**
	 * Saves a pokemon to the database that didn't exist in it before
	 * 
	 * @param p
	 */
	public int saveNewPokemon(Pokemon poke, String currentTrainer)
	{
		try
		{
			/* Due to issues with Pokemon not receiving abilities, we're going to ensure they have one */
			if(poke.getAbility() == null || poke.getAbility().getName().equalsIgnoreCase(""))
			{
				String[] abilities = PokemonSpecies.getDefaultData().getPossibleAbilities(poke.getSpeciesName());
				/* First select an ability randomly */
				String ab = "";
				if(abilities.length == 1)
					ab = abilities[0];
				else
					ab = abilities[DataService.getBattleMechanics().getRandom().nextInt(abilities.length)];
				poke.setAbility(IntrinsicAbility.getInstance(ab), true);
			}
			/* Insert the Pokemon into the database */
			m_database.query("INSERT INTO pn_pokemon VALUES (NULL, '" + MySqlManager.parseSQL(poke.getName()) + "', '" + MySqlManager.parseSQL(poke.getSpeciesName()) + "', '"
					+ String.valueOf(poke.getExp()) + "', " + poke.getPokemonBaseExp() + ", '" + MySqlManager.parseSQL(poke.getExpType().name()) + "', '" + String.valueOf(poke.isFainted()) + "', "
					+ poke.getLevel() + ", " + poke.getHappiness() + ", " + poke.getGender() + ", '" + MySqlManager.parseSQL(poke.getNature().getName()) + "', '"
					+ MySqlManager.parseSQL(poke.getAbilityName()) + "', '" + MySqlManager.parseSQL(poke.getItemName()) + "', '" + String.valueOf(poke.isShiny()) + "', '"
					+ MySqlManager.parseSQL(poke.getOriginalTrainer()) + "', '" + MySqlManager.parseSQL(currentTrainer) + "', '" + poke.getContestStatsAsString() + "', '"
					+ MySqlManager.parseSQL(poke.getMove(0).getName()) + "', '" + (poke.getMove(1) == null ? "null" : MySqlManager.parseSQL(poke.getMove(1).getName())) + "', '"
					+ (poke.getMove(2) == null ? "null" : MySqlManager.parseSQL(poke.getMove(2).getName())) + "', '"
					+ (poke.getMove(3) == null ? "null" : MySqlManager.parseSQL(poke.getMove(3).getName())) + "', " + poke.getHealth() + ", " + poke.getStat(1) + ", " + poke.getStat(2) + ", "
					+ poke.getStat(3) + ", " + poke.getStat(4) + ", " + poke.getStat(5) + ", " + poke.getEv(0) + ", " + poke.getEv(1) + ", " + poke.getEv(2) + ", " + poke.getEv(3) + ", "
					+ poke.getEv(4) + ", " + poke.getEv(5) + ", " + poke.getIv(0) + ", " + poke.getIv(1) + ", " + poke.getIv(2) + ", " + poke.getIv(3) + ", " + poke.getIv(4) + ", " + poke.getIv(5)
					+ ", " + poke.getPp(0) + ", " + poke.getPp(1) + ", " + poke.getPp(2) + ", " + poke.getPp(3) + ", " + poke.getMaxPp(0) + ", " + poke.getMaxPp(1) + ", " + poke.getMaxPp(2) + ", "
					+ poke.getMaxPp(3) + ", " + poke.getPpUpCount(0) + ", " + poke.getPpUpCount(1) + ", " + poke.getPpUpCount(2) + ", " + poke.getPpUpCount(3) + ", '"
					+ MySqlManager.parseSQL(poke.getDateCaught()) + "', " + poke.getCaughtWithBall() + ");");
			/* Get the pokemon's database id and attach it to the pokemon. This needs to be done so it can be attached to the player in the database later. */
			ResultSet result = m_database.query("SELECT id FROM pn_pokemon WHERE originalTrainerName='" + MySqlManager.parseSQL(poke.getOriginalTrainer()) + "' AND date='"
					+ MySqlManager.parseSQL(poke.getDateCaught()) + "';");
			result.first();
			int pokeId = result.getInt("id");
			poke.setDatabaseID(pokeId);
			return pokeId;
		}
		catch(Exception e)
		{
			System.err.println("INSERT INTO pn_pokemon VALUES (NULL, '" + MySqlManager.parseSQL(poke.getName()) + "', '" + MySqlManager.parseSQL(poke.getSpeciesName()) + "', '"
					+ String.valueOf(poke.getExp()) + "', " + poke.getPokemonBaseExp() + ", '" + MySqlManager.parseSQL(poke.getExpType().name()) + "', '" + String.valueOf(poke.isFainted()) + "', "
					+ poke.getLevel() + ", " + poke.getHappiness() + ", " + poke.getGender() + ", '" + MySqlManager.parseSQL(poke.getNature().getName()) + "', '"
					+ MySqlManager.parseSQL(poke.getAbilityName()) + "', '" + MySqlManager.parseSQL(poke.getItemName()) + "', '" + String.valueOf(poke.isShiny()) + "', '"
					+ MySqlManager.parseSQL(poke.getOriginalTrainer()) + "', '" + MySqlManager.parseSQL(currentTrainer) + "', '" + poke.getContestStatsAsString() + "', '"
					+ MySqlManager.parseSQL(poke.getMove(0).getName()) + "', '" + (poke.getMove(1) == null ? "null" : MySqlManager.parseSQL(poke.getMove(1).getName())) + "', '"
					+ (poke.getMove(2) == null ? "null" : MySqlManager.parseSQL(poke.getMove(2).getName())) + "', '"
					+ (poke.getMove(3) == null ? "null" : MySqlManager.parseSQL(poke.getMove(3).getName())) + "', " + poke.getHealth() + ", " + poke.getStat(1) + ", " + poke.getStat(2) + ", "
					+ poke.getStat(3) + ", " + poke.getStat(4) + ", " + poke.getStat(5) + ", " + poke.getEv(0) + ", " + poke.getEv(1) + ", " + poke.getEv(2) + ", " + poke.getEv(3) + ", "
					+ poke.getEv(4) + ", " + poke.getEv(5) + ", " + poke.getIv(0) + ", " + poke.getIv(1) + ", " + poke.getIv(2) + ", " + poke.getIv(3) + ", " + poke.getIv(4) + ", " + poke.getIv(5)
					+ ", " + poke.getPp(0) + ", " + poke.getPp(1) + ", " + poke.getPp(2) + ", " + poke.getPp(3) + ", " + poke.getMaxPp(0) + ", " + poke.getMaxPp(1) + ", " + poke.getMaxPp(2) + ", "
					+ poke.getMaxPp(3) + ", " + poke.getPpUpCount(0) + ", " + poke.getPpUpCount(1) + ", " + poke.getPpUpCount(2) + ", " + poke.getPpUpCount(3) + ", '"
					+ MySqlManager.parseSQL(poke.getDateCaught()) + "', " + poke.getCaughtWithBall() + ");");
			e.printStackTrace();
			return -1;
		}
	}

	/**
	 * Saves a player object to the database (Updates an existing player)
	 * 
	 * @param p
	 * @return
	 */
	public int savePlayer(Player p)
	{
		try(ResultSet data = m_database.query("SELECT lastLoginTime FROM `pn_members` WHERE id='" + p.getId() + "'"))
		{
			fail = 0;
			/* First, check if they have logged in somewhere else. This is useful for when a server loses it's internet connection. */
			if(data.first() && data.getLong("lastLoginTime") == p.getLastLoginTime())
			{
				/* Check they are not trading */
				if(p.isTrading())
					/* If the trade is still executing, don't save them yet */
					if(!p.getTrade().endTrade())
						fail++;
				// return false;
				/* Update the player row */
				String badges = "";
				for(int i = 0; i < p.getBadges().length; i++)
					if(p.hasBadge(i))
						badges += "1";
					else
						badges += "0";
				m_database.query("UPDATE pn_members SET " + "muted='" + p.isMuted() + "', " + "sprite='" + p.getRawSprite() + "', " + "money='" + p.getMoney() + "', " + "skHerb='"
						+ p.getHerbalismExp() + "', " + "skCraft='" + p.getCraftingExp() + "', " + "skFish='" + p.getFishingExp() + "', " + "skTrain='" + p.getTrainingExp() + "', " + "skCoord='"
						+ p.getCoordinatingExp() + "', " + "skBreed='" + p.getBreedingExp() + "', " + "x='" + p.getX() + "', " + "y='" + p.getY() + "', " + "mapX='" + p.getMapX() + "', " + "mapY='"
						+ p.getMapY() + "', " + "healX='" + p.getHealX() + "', " + "healY='" + p.getHealY() + "', " + "healMapX='" + p.getHealMapX() + "', " + "healMapY='" + p.getHealMapY() + "', "
						+ "isSurfing='" + String.valueOf(p.isSurfing()) + "', " + "badges='" + badges + "' " + "WHERE id='" + p.getId() + "';");
				/* Second, update the party */
				// Save all the Pokemon
				for(int i = 0; i < 6; i++)
					if(p.getParty() != null && p.getParty()[i] != null)
						if(p.getParty()[i].getDatabaseID() < 1)
						{
							// This is a new Pokemon, add it to the database
							if(saveNewPokemon(p.getParty()[i], p.getName()) < 1)
							{
								System.out.println("failed to save pokemon: " + p.getParty()[i].getName() + " of " + p.getName());
								fail++;
								// return false;
							}
						}
						else // Old Pokemon, just update
						if(!savePokemon(p.getParty()[i], p.getName()))
						{
							fail++;
							// return false;
						}
				// Save all the Pokemon id's in the player's party
				if(p.getParty() != null)
					m_database.query("UPDATE pn_party SET " + "pokemon0='" + (p.getParty()[0] != null ? p.getParty()[0].getDatabaseID() : -1) + "', " + "pokemon1='"
							+ (p.getParty()[1] != null ? p.getParty()[1].getDatabaseID() : -1) + "', " + "pokemon2='" + (p.getParty()[2] != null ? p.getParty()[2].getDatabaseID() : -1) + "', "
							+ "pokemon3='" + (p.getParty()[3] != null ? p.getParty()[3].getDatabaseID() : -1) + "', " + "pokemon4='" + (p.getParty()[4] != null ? p.getParty()[4].getDatabaseID() : -1)
							+ "', " + "pokemon5='" + (p.getParty()[5] != null ? p.getParty()[5].getDatabaseID() : -1) + "' " + "WHERE member='" + p.getId() + "';");
				else
					return fail;
				/* Save the player's bag */
				if(p.getBag() == null || !saveBag(p.getBag()))
				{
					fail++;
					// return false;
				}
				/* Finally, update all the boxes */
				if(p.getBoxes() != null)
					for(int i = 0; i < 9; i++)
						if(p.getBoxes()[i] != null)
							/* Save all pokemon in box */
							for(int j = 0; j < p.getBoxes()[i].getPokemon().length; j++)
								if(p.getBoxes()[i].getPokemon()[j] != null)
									if(p.getBoxes()[i].getPokemon()[j].getDatabaseID() < 1)
									{
										/* This is a new Pokemon, create it in the database */
										if(saveNewPokemon(p.getBoxes()[i].getPokemon(j), p.getName()) < 1)
										{
											System.out.println("failed to save pokemon: " + p.getBoxes()[i].getPokemon(j).getName() + " of " + p.getName());
											fail++;
											// return false;
										}
									}
									else /* Update an existing pokemon */
									if(!savePokemon(p.getBoxes()[i].getPokemon()[j], p.getName()))
									{
										fail++;
										// return false;
									}
				// Dispose of the player object
				if(p.getMap() != null)
					p.getMap().removeChar(p);
				return fail;
			}
			else
				return fail;
		}
		catch(Exception e)
		{
			System.err.println("UPDATE pn_members SET " + "muted='" + p.isMuted() + "', " + "sprite='" + p.getRawSprite() + "', " + "money='" + p.getMoney() + "', " + "skHerb='" + p.getHerbalismExp()
					+ "', " + "skCraft='" + p.getCraftingExp() + "', " + "skFish='" + p.getFishingExp() + "', " + "skTrain='" + p.getTrainingExp() + "', " + "skCoord='" + p.getCoordinatingExp()
					+ "', " + "skBreed='" + p.getBreedingExp() + "', " + "x='" + p.getX() + "', " + "y='" + p.getY() + "', " + "mapX='" + p.getMapX() + "', " + "mapY='" + p.getMapY() + "', "
					+ "healX='" + p.getHealX() + "', " + "healY='" + p.getHealY() + "', " + "healMapX='" + p.getHealMapX() + "', " + "healMapY='" + p.getHealMapY() + "', " + "isSurfing='"
					+ String.valueOf(p.isSurfing()) + "', " + "badges='" + "' " + "WHERE id='" + p.getId() + "'");
			System.err.println(p.getName() + " has " + fail + " fails.");
			e.printStackTrace();
			return fail;
		}
	}

	/**
	 * Updates a pokemon in the database
	 * 
	 * @param poke
	 */
	public boolean savePokemon(Pokemon poke, String currentTrainer)
	{
		/* Due to issues with Pokemon not receiving abilities, we're going to ensure they have one */
		String ab = "";
		if(poke.getAbility() == null || poke.getAbility().getName().equalsIgnoreCase(""))
		{
			String[] abilities = PokemonSpecies.getDefaultData().getPossibleAbilities(poke.getSpeciesName());
			/* First select an ability randomly */
			if(abilities.length == 1)
				ab = abilities[0];
			else
				ab = abilities[DataService.getBattleMechanics().getRandom().nextInt(abilities.length)];
			poke.setAbility(IntrinsicAbility.getInstance(ab), true);
		}
		else
		{
			ab = poke.getAbility().getName();
		}
		/* Update the pokemon in the database */
		try
		{
			m_database.query("UPDATE pn_pokemon SET name = '" + MySqlManager.parseSQL(poke.getName()) + "', speciesName = '" + MySqlManager.parseSQL(poke.getSpeciesName()) + "', exp = "
					+ String.valueOf(poke.getExp()) + ", baseExp = " + poke.getPokemonBaseExp() + ", expType = '" + MySqlManager.parseSQL(poke.getExpType().name()) + "', isFainted = '"
					+ String.valueOf(poke.isFainted()) + "', level = " + poke.getLevel() + ", happiness = " + poke.getHappiness() + ", abilityName = '" + ab + "', itemName = '"
					+ MySqlManager.parseSQL(poke.getItemName()) + "', currentTrainerName = '" + currentTrainer + "', contestStats = '" + poke.getContestStatsAsString() + "', move0 = '"
					+ (poke.getMove(0) == null ? "null" : MySqlManager.parseSQL(poke.getMove(0).getName())) + "', move1 = '"
					+ (poke.getMove(1) == null ? "null" : MySqlManager.parseSQL(poke.getMove(1).getName())) + "', move2 = '"
					+ (poke.getMove(2) == null ? "null" : MySqlManager.parseSQL(poke.getMove(2).getName())) + "', move3 = '"
					+ (poke.getMove(3) == null ? "null" : MySqlManager.parseSQL(poke.getMove(3).getName())) + "', hp = " + poke.getHealth() + ", atk = " + poke.getStat(1) + ", def = "
					+ poke.getStat(2) + ", speed = " + poke.getStat(3) + ", spATK = " + poke.getStat(4) + ", spDEF = " + poke.getStat(5) + ", evHP = " + poke.getEv(0) + ", evATK = " + poke.getEv(1)
					+ ", evDEF = " + poke.getEv(2) + ", evSPD = " + poke.getEv(3) + ", evSPATK = " + poke.getEv(4) + ", evSPDEF = " + poke.getEv(5) + ", ivHP = " + poke.getIv(0) + ", ivATK = "
					+ poke.getIv(1) + ", ivDEF = " + poke.getIv(2) + ", ivSPD = " + poke.getIv(3) + ", ivSPATK = " + poke.getIv(4) + ", ivSPDEF = " + poke.getIv(5) + ", pp0 = " + poke.getPp(0)
					+ ", pp1 = " + poke.getPp(1) + ", pp2 = " + poke.getPp(2) + ", pp3 = " + poke.getPp(3) + ", maxpp0 = " + poke.getMaxPp(0) + ", maxpp1 = " + poke.getMaxPp(1) + ", maxpp2 = "
					+ poke.getMaxPp(2) + ", maxpp3 = " + poke.getMaxPp(3) + ", ppUp0 = " + poke.getPpUpCount(0) + ", ppUp1 = " + poke.getPpUpCount(1) + ", ppUp2 = " + poke.getPpUpCount(2)
					+ ", ppUp3 = " + poke.getPpUpCount(3) + " WHERE id = " + poke.getDatabaseID() + ";");
		}
		catch(NullPointerException e)
		{
			e.printStackTrace();
			System.err.println("Database is " + m_database);
			System.err.println("Pokemon object is " + poke);
			System.err.println("Database ID is " + poke.getDatabaseID());
			System.err.println("Pokemon name is " + poke.getName());
			System.err.println("Pokemon moves are " + poke.getMove(0).getName() + "|" + poke.getMove(1).getName() + "|" + poke.getMove(2).getName() + "|" + poke.getMove(3).getName());
			System.err.println("', hp='" + poke.getHealth() + "', atk='" + poke.getStat(1) + "', def='" + poke.getStat(2) + "', speed='" + poke.getStat(3) + "', spATK='" + poke.getStat(4)
					+ "', spDEF='" + poke.getStat(5) + "', evHP='" + poke.getEv(0) + "', evATK='" + poke.getEv(1) + "', evDEF='" + poke.getEv(2) + "', evSPD='" + poke.getEv(3) + "', evSPATK='"
					+ poke.getEv(4) + "', evSPDEF='" + poke.getEv(5));
		}
		return true;
	}
}
