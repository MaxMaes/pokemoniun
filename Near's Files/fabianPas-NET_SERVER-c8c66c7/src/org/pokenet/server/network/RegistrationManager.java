package org.pokenet.server.network;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import org.pokenet.server.GameServer;
import org.pokenet.server.battle.DataService;
import org.pokenet.server.battle.Pokemon;
import org.pokenet.server.battle.PokemonSpecies;
import org.pokenet.server.battle.mechanics.PokemonNature;
import org.pokenet.server.battle.mechanics.moves.MoveListEntry;
import org.pokenet.server.client.Session;
import org.pokenet.server.protocol.ServerMessage;

/**
 * Handles registrations
 * @author shadowkanji
 *
 */
public class RegistrationManager implements Runnable {
	private MySQLManager m_database;
	
	/**
	 * Constructor
	 */
	public RegistrationManager() {
		m_database = new MySQLManager();
	}
	/**
	 * Registers a new player
	 * @param session
	 */
	public void register(Session session, int Region, String Packet) throws Exception {
		if(session.getChannel() != null) {
			return;
		}
		// packet id, region, data, data, data, data you know ;)
		
		int region = Region;
		String[] info = Packet.split(",");
		/* Check the username */
		if(info[0].equalsIgnoreCase("NULL") || info[0].equalsIgnoreCase("!NPC!")) {
			//Invalid - resevered for NPCs
			ServerMessage message = new ServerMessage();
			message.Init(87);
			message.addInt(4);
			session.Send(message);
			
			return;
		}
		if(info[0].startsWith(" ") || info[0].endsWith(" ") || info[0].contains("!")
				|| info[0].contains("&") || info[0].contains("%") || info[0].contains("(")
				|| info[0].contains(")") || info[0].contains("[") || info[0].contains("]")
				|| info[0].contains("~") || info[0].contains("#") || info[0].contains("|")
				|| info[0].contains("?") || info[0].contains("/") || info[0].contains("\"")) {
			//Invalid
			ServerMessage message = new ServerMessage();
			message.Init(87);
			message.addInt(4);
			session.Send(message);
			return;
		}
		m_database = new MySQLManager();
		if(m_database.connect(GameServer.getDatabaseHost(), GameServer.getDatabaseUsername(), GameServer.getDatabasePassword())) {
			m_database.selectDatabase(GameServer.getDatabaseName());
			int s = Integer.parseInt(info[4]);
			/*
			 * Check if the user exists
			 */
			ResultSet data = m_database.query("SELECT * FROM pn_members WHERE username='" + MySQLManager.parseSQL(info[0]) + "'");
			data.first();
			try {				
				if(data != null && data.getString("username") != null && data.getString("username").equalsIgnoreCase(MySQLManager.parseSQL(info[0]))) {
					ServerMessage message = new ServerMessage();
					message.Init(87);
					message.addInt(2);
					session.Send(message);
					return;
				}
			} catch (Exception e) {}
			/*
			 * Check if an account is already registered with the email
			 */
			data = m_database.query("SELECT * FROM pn_members WHERE email='" + MySQLManager.parseSQL(info[2]) + "'");
			data.first();
			try {				
				if(data != null && data.getString("email") != null && data.getString("email").equalsIgnoreCase(MySQLManager.parseSQL(info[2]))) {
					ServerMessage message = new ServerMessage();
					message.Init(87);
					message.addInt(5);
					session.Send(message);
					return;
				}
				if(info[2].length() > 52) {
					ServerMessage message = new ServerMessage();
					message.Init(87);
					message.addInt(6);
					session.Send(message);
					return;
				}
			} catch (Exception e) {}
			/*
			 * Check if user is not trying to register their starter as a non-starter Pokemon
			 */
			if(!(s == 1 || s == 4 || s == 7 || s == 152 || s == 155 || s == 158 || s == 252 || s == 255 || s == 258
					|| s == 387 || s == 390 || s == 393)) {
				ServerMessage message = new ServerMessage();
				message.Init(87);
				message.addInt(4);
				session.Send(message);
				return;
			}
			/*
			 * Generate badge string
			 */
			String badges = "";
			for(int i = 0; i < 50; i++)
				badges = badges + "0";
			/*
			 * Generate starting position
			 */
			int mapX = 0;
			int mapY = 0;
			int x = 256;
			int y = 440;
			switch(region) {
			case 0:
				/* Kanto */
				mapX = 3;
				mapY = 1;
				x = 512;
				y = 440;
				break;
			case 1:
				/* Johto */
				mapX = 0;
				mapY = 0;
				x = 256;
				y = 440;
				break;
			default:
				/* Prevent breaking the system */
				mapX = 0;
				mapY = 0;
				x = 256;
				y = 440;
				break;
			}
			/*
			 * Insert player into database
			 */
			m_database.query("INSERT INTO pn_members (username, password, dob, email, lastLoginTime, lastLoginServer, " +
					"sprite, money, skHerb, skCraft, skFish, skTrain, skCoord, skBreed, " +
					"x, y, mapX, mapY, badges, healX, healY, healMapX, healMapY, isSurfing, adminLevel, muted) VALUE " +
					"('" + MySQLManager.parseSQL(info[0]) + "', '" + MySQLManager.parseSQL(info[1]) + "', '" + MySQLManager.parseSQL(info[3]) + "', '" + MySQLManager.parseSQL(info[2]) + "', " +
							"'0', 'null', '" + MySQLManager.parseSQL(info[5]) + "', '0', '0', " +
									"'0', '0', '0', '0', '0', '" + x + "', '" + y + "', " +
									"'" + mapX + "', '" + mapY + "', '" + badges + "', '" + x + "', '" + y + "', '" 
									+ mapX + "', '" + mapY + "', 'false', '0', 'false')");
			/*
			 * Retrieve the player's unique id
			 */
			data = m_database.query("SELECT * FROM pn_members WHERE username='" + MySQLManager.parseSQL(info[0]) + "'");
			data.first();
			int playerId = data.getInt("id");
			//Player's bag is now created "on the fly" as soon as player gets his first item. 
			/*
			 * Create the players party
			 */
			Pokemon p = this.createStarter(s);
			p.setOriginalTrainer(info[0]);
			p.setDateCaught(new SimpleDateFormat("yyyy-MM-dd:HH-mm-ss").format(new Date()));
			this.saveNewPokemon(p, m_database);
			
			m_database.query("INSERT INTO pn_party (member, pokemon0, pokemon1, pokemon2, pokemon3, pokemon4, pokemon5) VALUES ('" +
					+ playerId + "','" + p.getDatabaseID() + "','-1','-1','-1','-1','-1')");
			data = m_database.query("SELECT * FROM pn_party WHERE member='" + playerId + "'");
			data.first();
			/*
			 * Attach pokemon to the player
			 */
			m_database.query("UPDATE pn_members SET party='" + data.getInt("id") + "' WHERE id='" + playerId + "'");
			/* Attach a bag of 5 pokeballs to the player */
			m_database.query("INSERT INTO pn_bag (member,item,quantity) VALUES ('" + playerId + "', '35', '5')");
			/*
			 * Finish
			 */
			m_database.close();
			ServerMessage message = new ServerMessage();
			message.Init(86);
			session.Send(message);
		} else {
			ServerMessage message = new ServerMessage();
			message.Init(87);
			message.addInt(1);
			session.Send(message);
		}
	}
	
	/**
	 * Saves a pokemon to the database that didn't exist in it before
	 * @param p
	 */
	private boolean saveNewPokemon(Pokemon p, MySQLManager db) {
		try {
			/*
			 * Insert the Pokemon into the database
			 */
			db.query("INSERT INTO pn_pokemon" +
					"(name, speciesName, exp, baseExp, expType, isFainted, level, happiness, " +
					"gender, nature, abilityName, itemName, isShiny, currentTrainerName, originalTrainerName, date, contestStats)" +
					"VALUES (" +
					"'" + MySQLManager.parseSQL(p.getName()) +"', " +
					"'" + MySQLManager.parseSQL(p.getSpeciesName()) +"', " +
					"'" + String.valueOf(p.getExp()) +"', " +
					"'" + p.getBaseExp() +"', " +
					"'" + MySQLManager.parseSQL(p.getExpType().name()) +"', " +
					"'" + String.valueOf(p.isFainted()) +"', " +
					"'" + p.getLevel() +"', " +
					"'" + p.getHappiness() +"', " +
					"'" + p.getGender() +"', " +
					"'" + MySQLManager.parseSQL(p.getNature().getName()) +"', " +
					"'" + MySQLManager.parseSQL(p.getAbilityName()) +"', " +
					"'" + MySQLManager.parseSQL(p.getItemName()) +"', " +
					"'" + String.valueOf(p.isShiny()) +"', " +
					"'" + MySQLManager.parseSQL(p.getOriginalTrainer()) + "', " +
					"'" + MySQLManager.parseSQL(p.getOriginalTrainer()) + "', " +
					"'" + MySQLManager.parseSQL(p.getDateCaught()) + "', " +
					"'" + p.getContestStatsAsString() + "')");
			/*
			 * Get the pokemon's database id and attach it to the pokemon.
			 * This needs to be done so it can be attached to the player in the database later.
			 */
			ResultSet result = db.query("SELECT * FROM pn_pokemon WHERE originalTrainerName='"  + MySQLManager.parseSQL(p.getOriginalTrainer()) + 
					"' AND date='" + MySQLManager.parseSQL(p.getDateCaught()) + "'");
			result.first();
			p.setDatabaseID(result.getInt("id"));
			db.query("UPDATE pn_pokemon SET move0='" + MySQLManager.parseSQL(p.getMove(0).getName()) +
					"', move1='" + (p.getMove(1) == null ? "null" : MySQLManager.parseSQL(p.getMove(1).getName())) +
					"', move2='" + (p.getMove(2) == null ? "null" : MySQLManager.parseSQL(p.getMove(2).getName())) +
					"', move3='" + (p.getMove(3) == null ? "null" : MySQLManager.parseSQL(p.getMove(3).getName())) +
					"', hp='" + p.getHealth() +
					"', atk='" + p.getStat(1) +
					"', def='" + p.getStat(2) +
					"', speed='" + p.getStat(3) +
					"', spATK='" + p.getStat(4) +
					"', spDEF='" + p.getStat(5) +
					"', evHP='" + p.getEv(0) +
					"', evATK='" + p.getEv(1) +
					"', evDEF='" + p.getEv(2) +
					"', evSPD='" + p.getEv(3) +
					"', evSPATK='" + p.getEv(4) +
					"', evSPDEF='" + p.getEv(5) +
					"' WHERE id='" + p.getDatabaseID() + "'");
			db.query("UPDATE pn_pokemon SET ivHP='" + p.getIv(0) +
					"', ivATK='" + p.getIv(1) +
					"', ivDEF='" + p.getIv(2) +
					"', ivSPD='" + p.getIv(3) +
					"', ivSPATK='" + p.getIv(4) +
					"', ivSPDEF='" + p.getIv(5) +
					"', pp0='" + p.getPp(0) +
					"', pp1='" + p.getPp(1) +
					"', pp2='" + p.getPp(2) +
					"', pp3='" + p.getPp(3) +
					"', maxpp0='" + p.getMaxPp(0) +
					"', maxpp1='" + p.getMaxPp(1) +
					"', maxpp2='" + p.getMaxPp(2) +
					"', maxpp3='" + p.getMaxPp(3) +
					"', ppUp0='" + p.getPpUpCount(0) +
					"', ppUp1='" + p.getPpUpCount(1) +
					"', ppUp2='" + p.getPpUpCount(2) +
					"', ppUp3='" + p.getPpUpCount(3) +
					"' WHERE id='" + p.getDatabaseID() + "'");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Creates a starter Pokemon
	 * @param speciesIndex
	 * @return
	 * @throws Exception
	 */
	private Pokemon createStarter(int speciesIndex) throws Exception {
		/*
		 * Get the Pokemon species. Use getPokemonByName as once the 
		 * species array gets to gen 3 it loses the pokedex numbering
		 */
		PokemonSpecies species = null;
		switch(speciesIndex) {
		case 1:
			species = PokemonSpecies.getDefaultData().getPokemonByName("Bulbasaur");
			break;
		case 4:
			species = PokemonSpecies.getDefaultData().getPokemonByName("Charmander");
			break;
		case 7:
			species = PokemonSpecies.getDefaultData().getPokemonByName("Squirtle");
			break;
		case 152:
			species = PokemonSpecies.getDefaultData().getPokemonByName("Chikorita");
			break;
		case 155:
			species = PokemonSpecies.getDefaultData().getPokemonByName("Cyndaquil");
			break;
		case 158:
			species = PokemonSpecies.getDefaultData().getPokemonByName("Totodile");
			break;
		case 252:
			species = PokemonSpecies.getDefaultData().getPokemonByName("Treecko");
			break;
		case 255:
			species = PokemonSpecies.getDefaultData().getPokemonByName("Torchic");
			break;
		case 258:
			species = PokemonSpecies.getDefaultData().getPokemonByName("Mudkip");
			break;
		case 387:
			species = PokemonSpecies.getDefaultData().getPokemonByName("Turtwig");
			break;
		case 390:
			species = PokemonSpecies.getDefaultData().getPokemonByName("Chimchar");
			break;
		case 393:
			species = PokemonSpecies.getDefaultData().getPokemonByName("Piplup");
			break;
		default:
			species = PokemonSpecies.getDefaultData().getPokemonByName("Mudkip");
		}
        
        ArrayList<MoveListEntry> possibleMoves = new ArrayList<MoveListEntry>();
        MoveListEntry[] moves = new MoveListEntry[4];
        Random random = DataService.getBattleMechanics().getRandom();
        for (int i = 0; i < species.getStarterMoves().length; i++) {
                possibleMoves.add(DataService.getMovesList().getMove(
                		species.getStarterMoves()[i]));
        }
        for (int i = 1; i <= 5; i++) {
                if (species.getLevelMoves().containsKey(i)) {
                        possibleMoves.add(DataService.getMovesList().getMove(
                        		species.getLevelMoves().get(i)));
                }
        }
        /*
         * possibleMoves sometimes has null moves stored in it, get rid of them
         */
        for(int i = 0; i < possibleMoves.size(); i++) {
        	if(possibleMoves.get(i) == null)
        		possibleMoves.remove(i);
        }
        /*
         * Now the store the final set of moves for the Pokemon
         */
        if (possibleMoves.size() <= 4) {
                for (int i = 0; i < possibleMoves.size(); i++) {
                        moves[i] = possibleMoves.get(i);
                }
        } else {
        	MoveListEntry m = null;
                for (int i = 0; i < 4; i++) {
                        if (possibleMoves.size() == 0) {
                            moves[i] = null;
                        } else {
                        	while(m == null) {
                        		m = possibleMoves.get(random.nextInt(possibleMoves
                                        .size()));
                        	}
                            moves[i] = m;
                            possibleMoves.remove(m);
                            m = null;
                        }
                }
        }
        /*
         * Get all possible abilities
         */
        String [] abilities = species.getAbilities();
        /* First select an ability randomly */
        String ab = abilities[random.nextInt(abilities.length)];
        /*
         * Unfortunately, all Pokemon have two possible ability slots but some only have one.
         * If the null slot was selected, select the other slot
         */
        while(ab == null || ab.equalsIgnoreCase("")) {
        	ab = abilities[random.nextInt(abilities.length)];
        }
        /*
         * Create the Pokemon object
         */
        Pokemon starter = new Pokemon(
        		DataService.getBattleMechanics(),
                        species,
                        PokemonNature.N_QUIRKY,
                                        ab,
                        null, (random.nextInt(100) > 87 ? Pokemon.GENDER_FEMALE
                                        : Pokemon.GENDER_MALE), 5, new int[] {
                                        random.nextInt(32), // IVs
                                        random.nextInt(32), random.nextInt(32),
                                        random.nextInt(32), random.nextInt(32),
                                        random.nextInt(32) }, new int[] { 0, 0, 0, 0, 0, 0 }, //EVs
                        moves, new int[] { 0, 0, 0, 0 });
        /* Attach their growth rate */
        starter.setExpType(species.getGrowthRate());
        /* Attach base experience */
        starter.setBaseExp(species.getBaseEXP());
        /* Set their current exp */
        starter.setExp(DataService.getBattleMechanics().getExpForLevel(starter, 5));
        /* Set their happiness */
        starter.setHappiness(species.getHappiness());
        /* Set their name as their species */
        starter.setName(starter.getSpeciesName());
        return starter;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
