package org.pokenet.server.backend;

import java.sql.ResultSet;
import org.pokenet.server.GameServer;
import org.pokenet.server.backend.entity.Bag;
import org.pokenet.server.backend.entity.Player;
import org.pokenet.server.battle.DataService;
import org.pokenet.server.battle.Pokemon;
import org.pokenet.server.battle.PokemonSpecies;
import org.pokenet.server.battle.mechanics.statuses.abilities.IntrinsicAbility;
import org.pokenet.server.network.MySqlManager;

public class SaveManager
{
	private MySqlManager m_database;

	public SaveManager()
	{
		/* TODO: Test for breaking if no DB. */
		m_database = new MySqlManager();
	}

	/**
	 * Saves a bag to the database.
	 * 
	 * @param b
	 * @return
	 */
	public boolean saveBag(Bag b)
	{
		m_database = new MySqlManager();
		if(!m_database.connect(GameServer.getDatabaseHost(), GameServer.getDatabaseUsername(), GameServer.getDatabasePassword()))
			return false;
		if(!m_database.selectDatabase(GameServer.getDatabaseName()))
			return false;
		try
		{
			// Destroy item data to prevent dupes.
			m_database.query("DELETE FROM pn_bag WHERE member='" + b.getMemberId() + "'");
			for(int i = 0; i < b.getItems().size(); i++)
				if(b.getItems().get(i) != null)
					/* NOTE: Items are stored as values 1 - 999 */
					m_database.query("INSERT INTO pn_bag (member,item,quantity) VALUES ('" + b.getMemberId() + "', '" + b.getItems().get(i).getItemNumber() + "', '"
							+ b.getItems().get(i).getQuantity() + "')");
			return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Saves a pokemon to the database that didn't exist in it before
	 * 
	 * @param p
	 */
	public int saveNewPokemon(Pokemon p, String currentTrainer, MySqlManager db)
	{
		m_database = new MySqlManager();
		if(!m_database.connect(GameServer.getDatabaseHost(), GameServer.getDatabaseUsername(), GameServer.getDatabasePassword()))
			return -1;
		if(!m_database.selectDatabase(GameServer.getDatabaseName()))
			return -1;
		try
		{
			/* Due to issues with Pokemon not receiving abilities, we're going to ensure they have one */
			if(p.getAbility() == null || p.getAbility().getName().equalsIgnoreCase(""))
			{
				String[] abilities = PokemonSpecies.getDefaultData().getPossibleAbilities(p.getSpeciesName());
				/* First select an ability randomly */
				String ab = "";
				if(abilities.length == 1)
					ab = abilities[0];
				else
					ab = abilities[DataService.getBattleMechanics().getRandom().nextInt(abilities.length)];
				p.setAbility(IntrinsicAbility.getInstance(ab), true);
			}
			/* Insert the Pokemon into the database */
			db.query("INSERT INTO pn_pokemon" + "(name, speciesName, exp, baseExp, expType, isFainted, level, happiness, "
					+ "gender, nature, abilityName, itemName, isShiny, currentTrainerName, originalTrainerName, date, contestStats, caughtWith)" + "VALUES (" + "'"
					+ MySqlManager.parseSQL(p.getName())
					+ "', "
					+ "'"
					+ MySqlManager.parseSQL(p.getSpeciesName())
					+ "', "
					+ "'"
					+ String.valueOf(p.getExp())
					+ "', "
					+ "'"
					+ p.getBaseExp()
					+ "', "
					+ "'"
					+ MySqlManager.parseSQL(p.getExpType().name())
					+ "', "
					+ "'"
					+ String.valueOf(p.isFainted())
					+ "', "
					+ "'"
					+ p.getLevel()
					+ "', "
					+ "'"
					+ p.getHappiness()
					+ "', "
					+ "'"
					+ p.getGender()
					+ "', "
					+ "'"
					+ MySqlManager.parseSQL(p.getNature().getName())
					+ "', "
					+ "'"
					+ MySqlManager.parseSQL(p.getAbilityName())
					+ "', "
					+ "'"
					+ MySqlManager.parseSQL(p.getItemName())
					+ "', "
					+ "'"
					+ String.valueOf(p.isShiny())
					+ "', "
					+ "'"
					+ currentTrainer
					+ "', "
					+ "'"
					+ MySqlManager.parseSQL(p.getOriginalTrainer())
					+ "', " + "'" + MySqlManager.parseSQL(p.getDateCaught()) + "', " + "'" + p.getContestStatsAsString() + "', " + "'" + p.getCaughtWithBall() + "')");
			/* Get the pokemon's database id and attach it to the pokemon. This needs to be done so it can be attached to the player in the database later. */
			ResultSet result = db.query("SELECT * FROM pn_pokemon WHERE originalTrainerName='" + MySqlManager.parseSQL(p.getOriginalTrainer()) + "' AND date='"
					+ MySqlManager.parseSQL(p.getDateCaught()) + "' AND name='" + p.getSpeciesName() + "' AND exp='" + String.valueOf(p.getExp()) + "'");
			result.first();
			p.setDatabaseID(result.getInt("id"));
			db.query("UPDATE pn_pokemon SET move0='" + MySqlManager.parseSQL(p.getMove(0).getName()) + "', move1='" + (p.getMove(1) == null ? "null" : MySqlManager.parseSQL(p.getMove(1).getName()))
					+ "', move2='" + (p.getMove(2) == null ? "null" : MySqlManager.parseSQL(p.getMove(2).getName())) + "', move3='"
					+ (p.getMove(3) == null ? "null" : MySqlManager.parseSQL(p.getMove(3).getName())) + "', hp='" + p.getHealth() + "', atk='" + p.getStat(1) + "', def='" + p.getStat(2)
					+ "', speed='" + p.getStat(3) + "', spATK='" + p.getStat(4) + "', spDEF='" + p.getStat(5) + "', evHP='" + p.getEv(0) + "', evATK='" + p.getEv(1) + "', evDEF='" + p.getEv(2)
					+ "', evSPD='" + p.getEv(3) + "', evSPATK='" + p.getEv(4) + "', evSPDEF='" + p.getEv(5) + "' WHERE id='" + p.getDatabaseID() + "'");
			db.query("UPDATE pn_pokemon SET ivHP='" + p.getIv(0) + "', ivATK='" + p.getIv(1) + "', ivDEF='" + p.getIv(2) + "', ivSPD='" + p.getIv(3) + "', ivSPATK='" + p.getIv(4) + "', ivSPDEF='"
					+ p.getIv(5) + "', pp0='" + p.getPp(0) + "', pp1='" + p.getPp(1) + "', pp2='" + p.getPp(2) + "', pp3='" + p.getPp(3) + "', maxpp0='" + p.getMaxPp(0) + "', maxpp1='"
					+ p.getMaxPp(1) + "', maxpp2='" + p.getMaxPp(2) + "', maxpp3='" + p.getMaxPp(3) + "', ppUp0='" + p.getPpUpCount(0) + "', ppUp1='" + p.getPpUpCount(1) + "', ppUp2='"
					+ p.getPpUpCount(2) + "', ppUp3='" + p.getPpUpCount(3) + "' WHERE id='" + p.getDatabaseID() + "'");
			return result.getInt("id");
		}
		catch(Exception e)
		{
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
	public boolean savePlayer(Player p)
	{
		m_database = new MySqlManager();
		if(!m_database.connect(GameServer.getDatabaseHost(), GameServer.getDatabaseUsername(), GameServer.getDatabasePassword()))
			return false;
		if(!m_database.selectDatabase(GameServer.getDatabaseName()))
			return false;
		try
		{
			/* First, check if they have logged in somewhere else. This is useful for when as server loses its internet connection */
			ResultSet data = m_database.query("SELECT * FROM `pn_members` WHERE id='" + p.getId() + "'");
			data.first();
			if(data.getLong("lastLoginTime") == p.getLastLoginTime())
			{
				/* Check they are not trading */
				if(p.isTrading())
					/* If the trade is still executing, don't save them yet */
					if(!p.getTrade().endTrade())
						return false;
				/* Update the player row */
				String badges = "";
				for(int i = 0; i < 42; i++)
					if(p.getBadges()[i] == 1)
						badges = badges + "1";
					else
						badges = badges + "0";
				m_database.query("UPDATE pn_members SET " + "muted='" + p.isMuted() + "', " + "sprite='" + p.getRawSprite() + "', " + "money='" + p.getMoney() + "', " + "skHerb='"
						+ p.getHerbalismExp() + "', " + "skCraft='" + p.getCraftingExp() + "', " + "skFish='" + p.getFishingExp() + "', " + "skTrain='" + p.getTrainingExp() + "', " + "skCoord='"
						+ p.getCoordinatingExp() + "', " + "skBreed='" + p.getBreedingExp() + "', " + "x='" + p.getX() + "', " + "y='" + p.getY() + "', " + "mapX='" + p.getMapX() + "', " + "mapY='"
						+ p.getMapY() + "', " + "healX='" + p.getHealX() + "', " + "healY='" + p.getHealY() + "', " + "healMapX='" + p.getHealMapX() + "', " + "healMapY='" + p.getHealMapY() + "', "
						+ "isSurfing='" + String.valueOf(p.isSurfing()) + "', " + "badges='" + badges + "' " + "WHERE id='" + p.getId() + "'");
				/* Second, update the party */
				// Save all the Pokemon
				for(int i = 0; i < 6; i++)
					if(p.getParty() != null && p.getParty()[i] != null)
						if(p.getParty()[i].getDatabaseID() < 1)
						{
							// This is a new Pokemon, add it to the database
							if(saveNewPokemon(p.getParty()[i], p.getName(), m_database) < 1)
								return false;
						}
						else // Old Pokemon, just update
						if(!savePokemon(p.getParty()[i], p.getName()))
							return false;
				// Save all the Pokemon id's in the player's party
				if(p.getParty() != null)
					m_database.query("UPDATE pn_party SET " + "pokemon0='" + (p.getParty()[0] != null ? p.getParty()[0].getDatabaseID() : -1) + "', " + "pokemon1='"
							+ (p.getParty()[1] != null ? p.getParty()[1].getDatabaseID() : -1) + "', " + "pokemon2='" + (p.getParty()[2] != null ? p.getParty()[2].getDatabaseID() : -1) + "', "
							+ "pokemon3='" + (p.getParty()[3] != null ? p.getParty()[3].getDatabaseID() : -1) + "', " + "pokemon4='" + (p.getParty()[4] != null ? p.getParty()[4].getDatabaseID() : -1)
							+ "', " + "pokemon5='" + (p.getParty()[5] != null ? p.getParty()[5].getDatabaseID() : -1) + "' " + "WHERE member='" + p.getId() + "'");
				else
					return true;
				/* Save the player's bag */
				if(p.getBag() == null || !saveBag(p.getBag()))
					return false;
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
										if(saveNewPokemon(p.getBoxes()[i].getPokemon(j), p.getName(), m_database) < 1)
											return false;
									}
									else /* Update an existing pokemon */
									if(!savePokemon(p.getBoxes()[i].getPokemon()[j], p.getName()))
										return false;
				// Dispose of the player object
				if(p.getMap() != null)
					p.getMap().removeChar(p);
				return true;
			}
			else
				return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

		/**
	 * Updates a pokemon in the database
	 * 
	 * @param p
	 */
	public boolean savePokemon(Pokemon p, String currentTrainer)
	{
		try
		{
			/*
			 * Due to issues with Pokemon not receiving abilities, we're going to ensure they have one
			 */
			String ab = "";
			if(p.getAbility() == null || p.getAbility().getName().equalsIgnoreCase(""))
			{
				String[] abilities = PokemonSpecies.getDefaultData().getPossibleAbilities(p.getSpeciesName());
				/* First select an ability randomly */
				
				if(abilities.length == 1)
					ab = abilities[0];
				else
					ab = abilities[DataService.getBattleMechanics().getRandom().nextInt(abilities.length)];
				p.setAbility(IntrinsicAbility.getInstance(ab), true);
			}
			else
			{
				ab = p.getAbility().getName();
			}
			/*
			 * Update the pokemon in the database
			 */try
			{
			m_database.query("UPDATE pn_pokemon SET " + "name='" + MySqlManager.parseSQL(p.getName()) + "', " + "speciesName='" + MySqlManager.parseSQL(p.getSpeciesName()) + "', " + "exp='"
					+ String.valueOf(p.getExp()) + "', " + "baseExp='" + p.getBaseExp() + "', " + "expType='" + MySqlManager.parseSQL(p.getExpType().name()) + "', " + "isFainted='"
					+ String.valueOf(p.isFainted()) + "', " + "level='" + p.getLevel() + "', " + "happiness='" + p.getHappiness() + "', " + "abilityName='" + ab + "', " + "itemName='" + MySqlManager.parseSQL(p.getItemName())
					+ "', " + "currentTrainerName='" + currentTrainer + "', " + "contestStats='" + p.getContestStatsAsString() + "' " + "WHERE id='" + p.getDatabaseID() + "'");
			
				m_database.query("UPDATE pn_pokemon SET move0='" + (p.getMove(0) == null ? "null" : MySqlManager.parseSQL(p.getMove(0).getName())) + "', move1='"
						+ (p.getMove(1) == null ? "null" : MySqlManager.parseSQL(p.getMove(1).getName())) + "', move2='"
						+ (p.getMove(2) == null ? "null" : MySqlManager.parseSQL(p.getMove(2).getName())) + "', move3='"
						+ (p.getMove(3) == null ? "null" : MySqlManager.parseSQL(p.getMove(3).getName())) + "', hp='" + p.getHealth() + "', atk='" + p.getStat(1) + "', def='" + p.getStat(2)
						+ "', speed='" + p.getStat(3) + "', spATK='" + p.getStat(4) + "', spDEF='" + p.getStat(5) + "', evHP='" + p.getEv(0) + "', evATK='" + p.getEv(1) + "', evDEF='" + p.getEv(2)
						+ "', evSPD='" + p.getEv(3) + "', evSPATK='" + p.getEv(4) + "', evSPDEF='" + p.getEv(5) + "' WHERE id='" + p.getDatabaseID() + "'");
			}
			catch(NullPointerException e)
			{
				e.printStackTrace();
				System.err.println("Database is " + m_database);
				System.err.println("Pokemon object is " + p);
				System.err.println("Database ID is " + p.getDatabaseID());
				System.err.println("Pokemon name is " + p.getName());
				System.err.println("Pokemon moves are " + p.getMove(0).getName() + "|" + p.getMove(1).getName() + "|" + p.getMove(2).getName() + "|" + p.getMove(3).getName());
				System.err.println("', hp='" + p.getHealth() + "', atk='" + p.getStat(1) + "', def='" + p.getStat(2) + "', speed='" + p.getStat(3) + "', spATK='" + p.getStat(4) + "', spDEF='"
						+ p.getStat(5) + "', evHP='" + p.getEv(0) + "', evATK='" + p.getEv(1) + "', evDEF='" + p.getEv(2) + "', evSPD='" + p.getEv(3) + "', evSPATK='" + p.getEv(4) + "', evSPDEF='"
						+ p.getEv(5));
			}
			m_database.query("UPDATE pn_pokemon SET ivHP='" + p.getIv(0) + "', ivATK='" + p.getIv(1) + "', ivDEF='" + p.getIv(2) + "', ivSPD='" + p.getIv(3) + "', ivSPATK='" + p.getIv(4)
					+ "', ivSPDEF='" + p.getIv(5) + "', pp0='" + p.getPp(0) + "', pp1='" + p.getPp(1) + "', pp2='" + p.getPp(2) + "', pp3='" + p.getPp(3) + "', maxpp0='" + p.getMaxPp(0)
					+ "', maxpp1='" + p.getMaxPp(1) + "', maxpp2='" + p.getMaxPp(2) + "', maxpp3='" + p.getMaxPp(3) + "', ppUp0='" + p.getPpUpCount(0) + "', ppUp1='" + p.getPpUpCount(1)
					+ "', ppUp2='" + p.getPpUpCount(2) + "', ppUp3='" + p.getPpUpCount(3) + "' WHERE id='" + p.getDatabaseID() + "'");
			return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
}
