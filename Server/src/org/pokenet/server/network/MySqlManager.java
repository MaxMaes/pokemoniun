package org.pokenet.server.network;

/*
 * Simple MySQL Java Class
 * Makes it similair to PHP
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.pokenet.server.backend.entity.Bag;
import org.pokenet.server.backend.entity.Player;
import org.pokenet.server.battle.DataService;
import org.pokenet.server.battle.Pokemon;
import org.pokenet.server.battle.PokemonSpecies;
import org.pokenet.server.battle.mechanics.statuses.abilities.IntrinsicAbility;

/**
 * Handles MySql connections
 * @author Daniel Morante
 */
public class MySqlManager {
    private Connection mysql_connection;
    private ResultSet mysql_result;    
    private String mysql_connectionURL;
    
    /**
     * Connects to the server. Returns true on success.
     * @param server
     * @param username
     * @param password
     * @return
     */
    public boolean connect(String server, String username, String password) {
        try {
            //Open Connection
            mysql_connectionURL = "jdbc:mysql://" + server+"?autoReconnect=true";
            mysql_connection = DriverManager.getConnection(mysql_connectionURL, username, password);
            if(!mysql_connection.isClosed())
            	return true;
            else
            	return false;
        } catch( Exception x ) {
          x.printStackTrace();
          return false;
        }
    }
    
    /**
     * Selects the current database. Returns true on success
     * @param database
     * @return
     */
    public boolean selectDatabase(String database) {
    	try {
        	Statement stm = mysql_connection.createStatement();
        	stm.executeQuery("USE " + database);
        	return true;
    	} catch (Exception e) {
    		e.printStackTrace();
    		return false;
    	}
    }
    
    /**
     * Closes the connection to the mysql server. Returns true on success.
     * @return
     */
    public boolean close(){
        try{
            mysql_connection.close();
            mysql_connection = null;
            return true;
        }
        catch (Exception x) {
             x.printStackTrace();
             return false;
        }
    }
    
    /**
     * Returns a result set for a query
     * @param query
     * @return
     */
    public ResultSet query(String query) {
    	System.out.println("Database is being called: " + query); // Debug to see how many times the database gets called.
        //Create Statement object
        Statement stmt;
        
        /*
         * We want to keep things simple, so...
         *
         * Detect whether this is an INSERT, DELETE, or UPDATE statement      
         * And use the executeUpdate() function
         *
         * Or...
         * 
         * Detect whether this is a SELECT statement and use the executeQuery()
         * Function. 
         * 
        */  
        
        if (query.startsWith("SELECT")) {
            //Use the "executeQuery" function because we have to retrieve data
            //Return the data as a resultset
            try{
                //Execute Query
                stmt = mysql_connection.createStatement();
                mysql_result = stmt.executeQuery(query);
            }
            catch(Exception x) {
                x.printStackTrace();
            }
            
            //Return Result
            return mysql_result;
        }
        else {
            //It's an UPDATE, INSERT, or DELETE statement
            //Use the"executeUpdaye" function and return a null result
            try{
                //Execute Query
                stmt = mysql_connection.createStatement();
                stmt.executeUpdate(query);
            }
            catch(Exception x) {
                x.printStackTrace();
            }
            
            //Return nothing
            return null;
        }
    }    
    
    public static String parseSQL(String text)
	{
		try {
			if(text == null) text = "";
			text = text.replace("'", "''");
			text = text.replace("\\", "\\\\");
		} catch (Exception e) {}
		return  text;
	}
    
    /**
	 * Saves a player object to the database (Updates an existing player)
	 * 
	 * @param p
	 * @return
	 */
	public boolean savePlayer(Player p) {
		try {
			/*
			 * First, check if they have logged in somewhere else. This is
			 * useful for when as server loses its internet connection
			 */
			ResultSet data = query("SELECT * FROM pn_members WHERE id='" + p.getId()
							+ "'");
			data.first();
			if (data.getLong("lastLoginTime") == p.getLastLoginTime()) {
				/* Check they are not trading */
				if (p.isTrading()) {
					/* If the trade is still executing, don't save them yet */
					if (!p.getTrade().endTrade())
						return false;
				}
				/*
				 * Update the player row
				 */
				String badges = "";
				for (int i = 0; i < 42; i++) {
					if (p.getBadges()[i] == 1)
						badges = badges + "1";
					else
						badges = badges + "0";
				}
				query("UPDATE pn_members SET " + "muted='"
						+ p.isMuted() + "', " + "sprite='" + p.getRawSprite()
						+ "', " + "money='" + p.getMoney() + "', " + "skHerb='"
						+ p.getHerbalismExp() + "', " + "skCraft='"
						+ p.getCraftingExp() + "', " + "skFish='"
						+ p.getFishingExp() + "', " + "skTrain='"
						+ p.getTrainingExp() + "', " + "skCoord='"
						+ p.getCoordinatingExp() + "', " + "skBreed='"
						+ p.getBreedingExp() + "', " + "x='" + p.getX() + "', "
						+ "y='" + p.getY() + "', " + "mapX='" + p.getMapX()
						+ "', " + "mapY='" + p.getMapY() + "', " + "healX='"
						+ p.getHealX() + "', " + "healY='" + p.getHealY()
						+ "', " + "healMapX='" + p.getHealMapX() + "', "
						+ "healMapY='" + p.getHealMapY() + "', "
						+ "isSurfing='" + String.valueOf(p.isSurfing()) + "', "
						+ "badges='" + badges + "' " + "WHERE id='" + p.getId()
						+ "'");
				/*
				 * Second, update the party
				 */
				// Save all the Pokemon
				for (int i = 0; i < 6; i++) {
					if (p.getParty() != null && p.getParty()[i] != null) {
						if (p.getParty()[i].getDatabaseID() < 1) {
							// This is a new Pokemon, add it to the database
							if (saveNewPokemon(p.getParty()[i], p.getName()) < 1)
								return false;
						} else {
							// Old Pokemon, just update
							if (!savePokemon(p.getParty()[i], p.getName()))
								return false;
						}
					}
				}
				// Save all the Pokemon id's in the player's party
				if (p.getParty() != null) {
					query("UPDATE pn_party SET "
							+ "pokemon0='"
							+ (p.getParty()[0] != null ? p.getParty()[0]
									.getDatabaseID() : -1)
							+ "', "
							+ "pokemon1='"
							+ (p.getParty()[1] != null ? p.getParty()[1]
									.getDatabaseID() : -1)
							+ "', "
							+ "pokemon2='"
							+ (p.getParty()[2] != null ? p.getParty()[2]
									.getDatabaseID() : -1)
							+ "', "
							+ "pokemon3='"
							+ (p.getParty()[3] != null ? p.getParty()[3]
									.getDatabaseID() : -1)
							+ "', "
							+ "pokemon4='"
							+ (p.getParty()[4] != null ? p.getParty()[4]
									.getDatabaseID() : -1)
							+ "', "
							+ "pokemon5='"
							+ (p.getParty()[5] != null ? p.getParty()[5]
									.getDatabaseID() : -1) + "' "
							+ "WHERE member='" + p.getId() + "'");
				} else
					return true;
				/*
				 * Save the player's bag
				 */
				if (p.getBag() == null || !saveBag(p.getBag()))
					return false;
				/*
				 * Finally, update all the boxes
				 */
				if (p.getBoxes() != null) {
					for (int i = 0; i < 9; i++) {
						if (p.getBoxes()[i] != null) {
							/* Save all pokemon in box */
							for (int j = 0; j < p.getBoxes()[i].getPokemon().length; j++) {
								if (p.getBoxes()[i].getPokemon()[j] != null) {
									if (p.getBoxes()[i].getPokemon()[j]
											.getDatabaseID() < 1) {
										/*
										 * This is a new Pokemon, create it in
										 * the database
										 */
										if (saveNewPokemon(
												p.getBoxes()[i].getPokemon(j),
												p.getName()) < 1)
											return false;
									} else {
										/* Update an existing pokemon */
										if (!savePokemon(
												p.getBoxes()[i].getPokemon()[j],
												p.getName())) {
											return false;
										}
									}
								}
							}
						}
					}
				}
				// Dispose of the player object
				if (p.getMap() != null)
					p.getMap().removeChar(p);
				return true;
			} else
				return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Saves a pokemon to the database that didn't exist in it before
	 * 
	 * @param p
	 */
	private int saveNewPokemon(Pokemon p, String currentTrainer) {
		try {
			/*
			 * Due to issues with Pokemon not receiving abilities, we're going
			 * to ensure they have one
			 */
			if (p.getAbility() == null
					|| p.getAbility().getName().equalsIgnoreCase("")) {
				String[] abilities = PokemonSpecies.getDefaultData()
						.getPossibleAbilities(p.getSpeciesName());
				/* First select an ability randomly */
				String ab = "";
				if (abilities.length == 1)
					ab = abilities[0];
				else
					ab = abilities[DataService.getBattleMechanics().getRandom()
							.nextInt(abilities.length)];
				p.setAbility(IntrinsicAbility.getInstance(ab), true);
			}
			/*
			 * Insert the Pokemon into the database
			 */
			query("INSERT INTO pn_pokemon"
					+ "(name, speciesName, exp, baseExp, expType, isFainted, level, happiness, "
					+ "gender, nature, abilityName, itemName, isShiny, currentTrainerName, originalTrainerName, date, contestStats)"
					+ "VALUES (" + "'"
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
					+ "', "
					+ "'"
					+ MySqlManager.parseSQL(p.getDateCaught())
					+ "', "
					+ "'" + p.getContestStatsAsString() + "')");
			/*
			 * Get the pokemon's database id and attach it to the pokemon. This
			 * needs to be done so it can be attached to the player in the
			 * database later.
			 */
			ResultSet result = query("SELECT * FROM pn_pokemon WHERE originalTrainerName='"
							+ MySqlManager.parseSQL(p.getOriginalTrainer())
							+ "' AND date='"
							+ MySqlManager.parseSQL(p.getDateCaught())
							+ "' AND name='"
							+ p.getSpeciesName()
							+ "' AND exp='" + String.valueOf(p.getExp()) + "'");
			result.first();
			p.setDatabaseID(result.getInt("id"));
			query("UPDATE pn_pokemon SET move0='"
					+ MySqlManager.parseSQL(p.getMove(0).getName())
					+ "', move1='"
					+ (p.getMove(1) == null ? "null" : MySqlManager.parseSQL(p
							.getMove(1).getName()))
					+ "', move2='"
					+ (p.getMove(2) == null ? "null" : MySqlManager.parseSQL(p
							.getMove(2).getName()))
					+ "', move3='"
					+ (p.getMove(3) == null ? "null" : MySqlManager.parseSQL(p
							.getMove(3).getName())) + "', hp='" + p.getHealth()
					+ "', atk='" + p.getStat(1) + "', def='" + p.getStat(2)
					+ "', speed='" + p.getStat(3) + "', spATK='" + p.getStat(4)
					+ "', spDEF='" + p.getStat(5) + "', evHP='" + p.getEv(0)
					+ "', evATK='" + p.getEv(1) + "', evDEF='" + p.getEv(2)
					+ "', evSPD='" + p.getEv(3) + "', evSPATK='" + p.getEv(4)
					+ "', evSPDEF='" + p.getEv(5) + "' WHERE id='"
					+ p.getDatabaseID() + "'");
			query("UPDATE pn_pokemon SET ivHP='" + p.getIv(0) + "', ivATK='"
					+ p.getIv(1) + "', ivDEF='" + p.getIv(2) + "', ivSPD='"
					+ p.getIv(3) + "', ivSPATK='" + p.getIv(4) + "', ivSPDEF='"
					+ p.getIv(5) + "', pp0='" + p.getPp(0) + "', pp1='"
					+ p.getPp(1) + "', pp2='" + p.getPp(2) + "', pp3='"
					+ p.getPp(3) + "', maxpp0='" + p.getMaxPp(0)
					+ "', maxpp1='" + p.getMaxPp(1) + "', maxpp2='"
					+ p.getMaxPp(2) + "', maxpp3='" + p.getMaxPp(3)
					+ "', ppUp0='" + p.getPpUpCount(0) + "', ppUp1='"
					+ p.getPpUpCount(1) + "', ppUp2='" + p.getPpUpCount(2)
					+ "', ppUp3='" + p.getPpUpCount(3) + "' WHERE id='"
					+ p.getDatabaseID() + "'");
			return result.getInt("id");
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	/**
	 * Updates a pokemon in the database
	 * 
	 * @param pokemon
	 */
	public boolean savePokemon(Pokemon pokemon, String currentTrainer) {
		try {
			/*
			 * Update the pokemon in the database
			 */
			query("UPDATE pn_pokemon SET " + "name='"
					+ MySqlManager.parseSQL(pokemon.getName()) + "', "
					+ "speciesName='"
					+ MySqlManager.parseSQL(pokemon.getSpeciesName()) + "', "
					+ "exp='" + String.valueOf(pokemon.getExp()) + "', "
					+ "baseExp='" + pokemon.getBaseExp() + "', " + "expType='"
					+ MySqlManager.parseSQL(pokemon.getExpType().name()) + "', "
					+ "isFainted='" + String.valueOf(pokemon.isFainted()) + "', "
					+ "level='" + pokemon.getLevel() + "', " + "happiness='"
					+ pokemon.getHappiness() + "', " + "itemName='"
					+ MySqlManager.parseSQL(pokemon.getItemName()) + "', "
					+ "currentTrainerName='" + currentTrainer + "', " // Currentrainer isn't the same as pokemon.getTrainerName(); ?
					+ "contestStats='" + pokemon.getContestStatsAsString() + "' "
					+ "WHERE id='" + pokemon.getDatabaseID() + "'");
			try {
				query("UPDATE pn_pokemon SET move0='"
								+ (pokemon.getMove(0) == null ? "null" : MySqlManager
										.parseSQL(pokemon.getMove(0).getName()))
								+ "', move1='"
								+ (pokemon.getMove(1) == null ? "null" : MySqlManager
										.parseSQL(pokemon.getMove(1).getName()))
								+ "', move2='"
								+ (pokemon.getMove(2) == null ? "null" : MySqlManager
										.parseSQL(pokemon.getMove(2).getName()))
								+ "', move3='"
								+ (pokemon.getMove(3) == null ? "null" : MySqlManager
										.parseSQL(pokemon.getMove(3).getName()))
								+ "', hp='" + pokemon.getHealth() + "', atk='"
								+ pokemon.getStat(1) + "', def='" + pokemon.getStat(2)
								+ "', speed='" + pokemon.getStat(3) + "', spATK='"
								+ pokemon.getStat(4) + "', spDEF='" + pokemon.getStat(5)
								+ "', evHP='" + pokemon.getEv(0) + "', evATK='"
								+ pokemon.getEv(1) + "', evDEF='" + pokemon.getEv(2)
								+ "', evSPD='" + pokemon.getEv(3) + "', evSPATK='"
								+ pokemon.getEv(4) + "', evSPDEF='" + pokemon.getEv(5)
								+ "' WHERE id='" + pokemon.getDatabaseID() + "'");
			} catch (NullPointerException e) {
				e.printStackTrace();
				System.out.println("Database is " + this);
				System.out.println("Pokemon object is " + pokemon);
				System.out.println("Database ID is " + pokemon.getDatabaseID());
				System.out.println("Pokemon name is " + pokemon.getName());
				System.out.println("Pokemon moves are "
						+ pokemon.getMove(0).getName() + "|" + pokemon.getMove(1).getName()
						+ "|" + pokemon.getMove(2).getName() + "|"
						+ pokemon.getMove(3).getName());
				System.out.println("', hp='" + pokemon.getHealth() + "', atk='"
						+ pokemon.getStat(1) + "', def='" + pokemon.getStat(2)
						+ "', speed='" + pokemon.getStat(3) + "', spATK='"
						+ pokemon.getStat(4) + "', spDEF='" + pokemon.getStat(5)
						+ "', evHP='" + pokemon.getEv(0) + "', evATK='" + pokemon.getEv(1)
						+ "', evDEF='" + pokemon.getEv(2) + "', evSPD='" + pokemon.getEv(3)
						+ "', evSPATK='" + pokemon.getEv(4) + "', evSPDEF='"
						+ pokemon.getEv(5));
			}
			query("UPDATE pn_pokemon SET ivHP='" + pokemon.getIv(0)
					+ "', ivATK='" + pokemon.getIv(1) + "', ivDEF='" + pokemon.getIv(2)
					+ "', ivSPD='" + pokemon.getIv(3) + "', ivSPATK='" + pokemon.getIv(4)
					+ "', ivSPDEF='" + pokemon.getIv(5) + "', pp0='" + pokemon.getPp(0)
					+ "', pp1='" + pokemon.getPp(1) + "', pp2='" + pokemon.getPp(2)
					+ "', pp3='" + pokemon.getPp(3) + "', maxpp0='" + pokemon.getMaxPp(0)
					+ "', maxpp1='" + pokemon.getMaxPp(1) + "', maxpp2='"
					+ pokemon.getMaxPp(2) + "', maxpp3='" + pokemon.getMaxPp(3)
					+ "', ppUp0='" + pokemon.getPpUpCount(0) + "', ppUp1='"
					+ pokemon.getPpUpCount(1) + "', ppUp2='" + pokemon.getPpUpCount(2)
					+ "', ppUp3='" + pokemon.getPpUpCount(3) + "' WHERE id='"
					+ pokemon.getDatabaseID() + "'");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Saves a bag to the database.
	 * 
	 * @param b
	 * @return
	 */
	private boolean saveBag(Bag b) {
		try {
			// Destroy item data to prevent dupes.
			query("DELETE FROM pn_bag WHERE member='"
					+ b.getMemberId() + "'");
			for (int i = 0; i < b.getItems().size(); i++) {
				if (b.getItems().get(i) != null) {
					/*
					 * NOTE: Items are stored as values 1 - 999
					 */
					query("INSERT INTO pn_bag (member,item,quantity) VALUES ('"
									+ b.getMemberId()
									+ "', '"
									+ b.getItems().get(i).getItemNumber()
									+ "', '"
									+ b.getItems().get(i).getQuantity() + "')");
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
