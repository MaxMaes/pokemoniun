package org.pokenet.server.network;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import org.apache.mina.core.session.IoSession;
import org.pokenet.server.GameServer;
import org.pokenet.server.Log;
import org.pokenet.server.battle.DataService;
import org.pokenet.server.battle.Pokemon;
import org.pokenet.server.battle.PokemonSpecies;
import org.pokenet.server.battle.mechanics.PokemonNature;
import org.pokenet.server.battle.mechanics.moves.MoveListEntry;
import org.pokenet.server.feature.DatabaseConnection;

/**
 * Handles registrations
 * 
 * @author shadowkanji
 */
public class RegistrationManager implements Runnable
{
	private final Queue<IoSession> m_queue = new LinkedList<IoSession>();
	private Thread m_thread;
	private boolean m_isRunning = false;
	private MySqlManager m_database;

	/**
	 * Constructor
	 */
	public RegistrationManager()
	{
		m_database = MySqlManager.getInstance();
	}

	/**
	 * Queues a registration
	 * 
	 * @param session
	 * @param packet
	 */
	public void queueRegistration(IoSession session, String packet)
	{
		if(m_thread == null || !m_thread.isAlive())
			start();
		if(!m_queue.contains(session))
		{
			session.setAttribute("reg", packet);
			m_queue.offer(session);
		}
		session.suspendRead();
		session.suspendWrite();
	}

	/**
	 * Registers a new player
	 * 
	 * @param session
	 */
	public void register(IoSession session) throws Exception
	{
		if(!session.isConnected() || session.isClosing())
		{
			return;
		}
		int region = Integer.parseInt(String.valueOf(((String) session.getAttribute("reg")).charAt(0)));
		String[] info = ((String) session.getAttribute("reg")).substring(1).split(",");
		/* Check the username */
		if(info[0].equalsIgnoreCase("NULL") || info[0].equalsIgnoreCase("!NPC!"))
		{
			// Invalid - resevered for NPCs
			session.write("r4");
			return;
		}
		if(info[0].startsWith(" ") || info[0].endsWith(" ") || info[0].contains("!") || info[0].contains("&") || info[0].contains("%") || info[0].contains("(") || info[0].contains(")")
				|| info[0].contains("[") || info[0].contains("]") || info[0].contains("~") || info[0].contains("#") || info[0].contains("|") || info[0].contains("?") || info[0].contains("/")
				|| info[0].contains("\""))
		{
			// Invalid
			session.write("r4");
			return;
		}
		int s = Integer.parseInt(info[4]);
		/*
		 * Check if the user exists
		 */
		PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM pn_members WHERE username = ?");
		ps.setString(1, info[0]);
		ResultSet rs = ps.executeQuery();
		rs.first();
		String username = rs.getString("username");
		if(rs != null && username != null && username.equalsIgnoreCase(info[0]))
		{
			session.resumeRead();
			session.resumeWrite();
			session.write("r2");
			return;
		}
		rs.close();
		ps.close();
		/*
		 * Check if an account is already registered with the email
		 */
		ps = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM pn_members WHERE email = ?");
		ps.setString(1, info[2]);
		rs = ps.executeQuery();
		rs.first();
		String email = rs.getString("email");
		if(rs != null && email != null && email.equalsIgnoreCase(info[2]))
		{
			session.resumeRead();
			session.resumeWrite();
			session.write("r5");
			return;
		}
		if(info[2].length() > 52)
		{
			session.resumeRead();
			session.resumeWrite();
			session.write("r6");
			return;
		}
		rs.close();
		ps.close();
		/*
		 * Check if user is not trying to register their starter as a non-starter Pokemon
		 */
		if(!(s == 1 || s == 4 || s == 7 || s == 152 || s == 155 || s == 158 || s == 252 || s == 255 || s == 258 || s == 387 || s == 390 || s == 393))
		{
			session.write("r4");
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
		switch(region)
		{
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
		ps = DatabaseConnection
				.getConnection()
				.prepareStatement(
						"INSERT INTO pn_members (username, password, dob, email, lastLoginTime, lastLoginServer, sprite, money, skHerb, skCraft, skFish, skTrain, skCoord, skBreed, x, y, mapX, mapY, badges, healX, healY, healMapX, healMapY, isSurfing, adminLevel, muted) VALUE (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		ps.setString(1, info[0]); // username
		ps.setString(2, info[1]); // password
		ps.setString(3, info[3]); // dob
		ps.setString(4, info[2]); // email
		ps.setInt(5, 0); // lastLoginTime
		ps.setString(6, null); // lastLoginServer
		ps.setString(7, info[5]); // sprite
		ps.setInt(8, 1000); // Money
		ps.setInt(9, 0); // skHerb
		ps.setInt(10, 0); // skCraft
		ps.setInt(11, 0); // skFish
		ps.setInt(12, 0); // skTrain
		ps.setInt(13, 0); // skCoord
		ps.setInt(14, 0); // skBreed
		ps.setInt(15, x); // x
		ps.setInt(16, y); // y
		ps.setInt(17, mapX); // mapX
		ps.setInt(18, mapY); // mapY
		ps.setString(19, badges); // badges
		ps.setInt(20, x); // healX
		ps.setInt(21, y); // healY
		ps.setInt(22, mapX); // healMapX
		ps.setInt(23, mapY); // healMapY
		ps.setBoolean(24, false); // isSurfing
		ps.setInt(25, 0); // adminLevel
		ps.setBoolean(26, false); // muted
		
		ps.executeUpdate();
		ps.close();
		/*
		 * Retrieve the player's unique id
		 */
		ResultSet data = m_database.query("SELECT * FROM pn_members WHERE username='" + MySqlManager.parseSQL(info[0]) + "'");
		data.first();
		int playerId = data.getInt("id");
		// Player's bag is now created "on the fly" as soon as player gets his first item.
		/*
		 * Create the players party
		 */
		Pokemon p = this.createStarter(s);
		p.setOriginalTrainer(info[0]);
		p.setDateCaught(new SimpleDateFormat("yyyy-MM-dd:HH-mm-ss").format(new Date()));
		this.saveNewPokemon(p, m_database);

		m_database.query("INSERT INTO pn_party (member, pokemon0, pokemon1, pokemon2, pokemon3, pokemon4, pokemon5) VALUES ('" + +playerId + "','" + p.getDatabaseID() + "','-1','-1','-1','-1','-1')");
		data = m_database.query("SELECT * FROM pn_party WHERE member='" + playerId + "'");
		data.first();
		/*
		 * Attach pokemon to the player
		 */
		m_database.query("UPDATE pn_members SET party='" + data.getInt("id") + "' WHERE id='" + playerId + "'");
		/* Attach a bag of 5 pokeballs to the player */
		m_database.query("INSERT INTO pn_bag (member,item,quantity) VALUES ('" + playerId + "', '35', '5')");

		session.resumeRead();
		session.resumeWrite();
		session.write("rs");
	}

	/**
	 * Called by m_thread.start()
	 */
	public void run()
	{
		GameServer.THREADS++;
		Log.debug("RegistrationManager started.");
		IoSession session;
		while(m_isRunning)
		{
			synchronized(m_queue)
			{
				if(m_queue.peek() != null)
				{
					session = m_queue.poll();
					try
					{
						this.register(session);
					}
					catch(Exception e)
					{
						e.printStackTrace();
						session.resumeRead();
						session.resumeWrite();
						session.write("r3");
					}
				}
			}
		}
		GameServer.THREADS--;
		Log.debug("RegistrationManager stopped (" + GameServer.THREADS + " threads remaining)");
	}

	/**
	 * Start the registration manager
	 */
	public void start()
	{
		if(m_thread == null || !m_thread.isAlive())
		{
			m_thread = new Thread(this);
			m_isRunning = true;
			m_thread.start();
		}
	}

	/**
	 * Stop the registration manager
	 */
	public void stop()
	{
		m_isRunning = false;
	}

	/**
	 * Saves a pokemon to the database that didn't exist in it before
	 * 
	 * @param p
	 */
	private boolean saveNewPokemon(Pokemon p, MySqlManager db)
	{
		try
		{
			/*
			 * Insert the Pokemon into the database
			 */
			db.query("INSERT INTO pn_pokemon" + "(name, speciesName, exp, baseExp, expType, isFainted, level, happiness, "
					+ "gender, nature, abilityName, itemName, isShiny, currentTrainerName, originalTrainerName, date, contestStats)" + "VALUES (" + "'"
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
					+ MySqlManager.parseSQL(p.getOriginalTrainer())
					+ "', "
					+ "'"
					+ MySqlManager.parseSQL(p.getOriginalTrainer()) + "', " + "'" + MySqlManager.parseSQL(p.getDateCaught()) + "', " + "'" + p.getContestStatsAsString() + "')");
			/*
			 * Get the pokemon's database id and attach it to the pokemon. This needs to be done so it can be attached to the player in the database later.
			 */
			ResultSet result = db.query("SELECT * FROM pn_pokemon WHERE originalTrainerName='" + MySqlManager.parseSQL(p.getOriginalTrainer()) + "' AND date='"
					+ MySqlManager.parseSQL(p.getDateCaught()) + "'");
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
			return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Creates a starter Pokemon
	 * 
	 * @param speciesIndex
	 * @return
	 * @throws Exception
	 */
	private Pokemon createStarter(int speciesIndex) throws Exception
	{
		/*
		 * Get the Pokemon species. Use getPokemonByName as once the species array gets to gen 3 it loses the pokedex numbering
		 */
		PokemonSpecies species = null;
		switch(speciesIndex)
		{
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
		for(int i = 0; i < species.getStarterMoves().length; i++)
		{
			possibleMoves.add(DataService.getMovesList().getMove(species.getStarterMoves()[i]));
		}
		for(int i = 1; i <= 5; i++)
		{
			if(species.getLevelMoves().containsKey(i))
			{
				possibleMoves.add(DataService.getMovesList().getMove(species.getLevelMoves().get(i)));
			}
		}
		/*
		 * possibleMoves sometimes has null moves stored in it, get rid of them
		 */
		for(int i = 0; i < possibleMoves.size(); i++)
		{
			if(possibleMoves.get(i) == null)
				possibleMoves.remove(i);
		}
		/*
		 * Now the store the final set of moves for the Pokemon
		 */
		if(possibleMoves.size() <= 4)
		{
			for(int i = 0; i < possibleMoves.size(); i++)
			{
				moves[i] = possibleMoves.get(i);
			}
		}
		else
		{
			MoveListEntry m = null;
			for(int i = 0; i < 4; i++)
			{
				if(possibleMoves.size() == 0)
				{
					moves[i] = null;
				}
				else
				{
					while(m == null)
					{
						m = possibleMoves.get(random.nextInt(possibleMoves.size()));
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
		String[] abilities = species.getAbilities();
		/* First select an ability randomly */
		String ab = abilities[random.nextInt(abilities.length)];
		/*
		 * Unfortunately, all Pokemon have two possible ability slots but some only have one. If the null slot was selected, select the other slot
		 */
		while(ab == null || ab.equalsIgnoreCase(""))
		{
			ab = abilities[random.nextInt(abilities.length)];
		}
		/*
		 * Create the Pokemon object
		 */
		Pokemon starter = new Pokemon(DataService.getBattleMechanics(), species, PokemonNature.N_QUIRKY, ab, null, (random.nextInt(100) > 87 ? Pokemon.GENDER_FEMALE : Pokemon.GENDER_MALE), 5,
				new int[] { random.nextInt(32), // IVs
						random.nextInt(32), random.nextInt(32), random.nextInt(32), random.nextInt(32), random.nextInt(32) }, new int[] { 0, 0, 0, 0, 0, 0 }, // EVs
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
}
