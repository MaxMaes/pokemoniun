package org.pokenet.server.network;

import java.io.IOException;
import java.net.Socket;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;
import org.apache.mina.core.session.IoSession;
import org.pokenet.server.GameServer;
import org.pokenet.server.Log;
import org.pokenet.server.backend.entity.Bag;
import org.pokenet.server.backend.entity.Player;
import org.pokenet.server.backend.entity.Player.Language;
import org.pokenet.server.backend.entity.PokemonBox;
import org.pokenet.server.battle.DataService;
import org.pokenet.server.battle.Pokemon;
import org.pokenet.server.battle.Pokemon.ExpTypes;
import org.pokenet.server.battle.PokemonSpecies;
import org.pokenet.server.battle.mechanics.PokemonNature;
import org.pokenet.server.battle.mechanics.moves.MoveListEntry;
import org.pokenet.server.feature.DatabaseConnection;
import org.pokenet.server.feature.TimeService;

/**
 * Handles logging players in
 * 
 * @author shadowkanji
 */
public class LoginManager implements Runnable
{
	private Queue<Object[]> m_loginQueue;
	private Thread m_thread;
	private boolean m_isRunning = false;

	private Queue<Object[]> m_passChangeQueue;

	/**
	 * Default constructor. Requires a logout manager to be passed in so the server can check if player's data is not being saved as they are logging in.
	 * 
	 * @param manager
	 */
	public LoginManager(LogoutManager manager)
	{
		m_loginQueue = new LinkedList<Object[]>();
		m_passChangeQueue = new LinkedList<Object[]>();
		m_thread = null;
	}

	/**
	 * Attempts to login a player. Upon success, it sends a packet to the player to inform them they are logged in.
	 * 
	 * @param session
	 * @param l
	 * @param username
	 * @param password
	 */
	private void attemptLogin(IoSession session, char l, String username, String password)
	{

		// Check if we haven't reach the player limit
		if(TcpProtocolHandler.getPlayerCount() > GameServer.getMaxPlayers())
		{
			session.write("l2");
			return;
		}
		// Now, check they are not banned
		// ResultSet result =
		// m_database.query("SELECT * FROM pn_bans WHERE ip='" +
		// getIp(session) + "'");
		try
		{
			PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM pn_bans WHERE ip = ?");
			ps.setString(1, TcpProtocolHandler.getIp(session));
			ResultSet rs = ps.executeQuery();

			if(rs != null && rs.first())
			{
				// This is player is banned, inform them
				session.write("l4");
				rs.close();
				ps.close();
				return;
			}

			rs.close();
			ps.close();
		}
		catch(SQLException sqle)
		{
			sqle.printStackTrace();
		}

		// Then find the member's information
		// ResultSet result =
		// m_database.query("SELECT * FROM pn_members WHERE username='" +
		// MySqlManager.parseSQL(username) + "'");
		try
		{
			PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM pn_members WHERE username = ?");
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();

			if(!rs.first())
			{
				// Member doesn't exist, say user or pass wrong. We don't
				// want someone to guess usernames.
				session.write("le");
				return;
			}
			// Check if the password is correct
			if(rs.getString("password").compareTo(password) == 0)
			{
				// Remove the player from the map to prevent duplicates
				GameServer.getServiceManager().getMovementService().removePlayer(username);
				long time = System.currentTimeMillis();
				// Now check if they are logged in anywhere else
				if(rs.getString("lastLoginServer").equals(GameServer.getServerName()))
				{
					/*
					 * They are already logged in on this server. Attach the session to the existing player if they exist, if not, just log them in
					 */
					if(TcpProtocolHandler.containsPlayer(username))
					{
						Player p = TcpProtocolHandler.getPlayer(username);
						p.getTcpSession().setAttribute("player", null);
						p.setLastLoginTime(time);
						p.getTcpSession().close(true);
						p.setTcpSession(session);
						p.setLanguage(Language.values()[Integer.parseInt(String.valueOf(l))]);
						
						try
						{
							PreparedStatement ps1 = DatabaseConnection.getConnection().prepareStatement("UPDATE pn_members SET lastLoginServer = ?, lastLoginTime = ?, lastLoginIP = ?, lastLanguageUsed = ? WHERE username = ?");
							ps1.setString(1, GameServer.getServerName());
							ps1.setLong(2, time);
							ps1.setString(3, TcpProtocolHandler.getIp(session));
							ps1.setLong(4, l);
							ps1.setString(5, username);
							ps1.executeUpdate();
							ps1.close();
						}
						catch(SQLException e)
						{
							e.printStackTrace();
						}
						session.setAttribute("player", p);
						this.initialiseClient(p, session);
					}
					else
					{
						session.write("l3");
						return;
					}
				}
				else if(rs.getString("lastLoginServer").equals("null"))
				{
					/*
					 * They are not logged in elsewhere, log them in
					 */
					login(username, l, session, rs);
				}
				else
				{
					/*
					 * They are logged in somewhere else. Check if the server is up, if it is, don't log them in. If not, log them in
					 */
					try
					{
						/**
						 * This is a dirty hack. The old method used isReachable(5000) to determine if the server was alive. isReachable doesn't work unless you run as root due to sending IMCP Echo packets being forbidden under normal user accounts. Instead, We open a socket to determine if server's alive. If it crashes, then server's down.
						 */
						Socket socket = new Socket(rs.getString("lastLoginServer"), 7002);
						socket.close();
						session.write("l3");
						return;
					}
					catch(IOException ex)
					{
						// The server they were on went down and they are
						// trying to login elsewhere
						login(username, l, session, rs);
					}
				}
			}
			else
			{
				// Password is wrong, say so.
				session.write("le");
				return;
			}

			rs.close();
			ps.close();
		}
		catch(SQLException sqle)
		{
			sqle.printStackTrace();
		}

	}

	/**
	 * Places a player in the login queue
	 * 
	 * @param session
	 * @param username
	 * @param password
	 * @param forceLogin - true if player wants to force login
	 */
	public void queuePlayer(IoSession session, String username, String password)
	{
		if(m_thread == null || !m_thread.isAlive())
		{
			start();
		}
		m_loginQueue.offer(new Object[] { session, username, password });
	}

	/**
	 * Places a player in the queue to update their password
	 * 
	 * @param session
	 * @param username
	 * @param newPassword
	 * @param oldPassword
	 */
	public void queuePasswordChange(IoSession session, String username, String newPassword, String oldPassword)
	{
		if(m_thread == null || !m_thread.isAlive())
		{
			start();
		}
		m_passChangeQueue.offer(new Object[] { session, username, newPassword, oldPassword });
	}

	/**
	 * Called by Thread.start()
	 */
	public void run()
	{
		GameServer.THREADS++;
		System.out.println("LoginManager started.");
		Object[] o;
		IoSession session;
		String username;
		String password;
		String newPassword;
		char l;
		while(m_isRunning)
		{
			synchronized(m_loginQueue)
			{
				try
				{
					if(m_loginQueue.peek() != null)
					{
						o = m_loginQueue.poll();
						session = (IoSession) o[0];
						l = ((String) o[1]).charAt(0);
						username = ((String) o[1]).substring(1);
						password = (String) o[2];
						this.attemptLogin(session, l, username, password);
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			try
			{
				Thread.sleep(500);
			}
			catch(Exception e)
			{
			}

			synchronized(m_passChangeQueue)
			{
				try
				{
					if(m_passChangeQueue.peek() != null)
					{
						o = m_passChangeQueue.poll();
						session = (IoSession) o[0];
						username = (String) o[1];
						newPassword = (String) o[2];
						password = (String) o[3];
						this.changePass(username, newPassword, password, session);
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			try
			{
				Thread.sleep(500);
			}
			catch(Exception e)
			{
			}
		}
		GameServer.THREADS--;
		System.out.println("LoginManager stopped (" + GameServer.THREADS + " threads remaining)");
	}

	/**
	 * Starts the login manager
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
	 * Stops the login manager
	 */
	public void stop()
	{
		m_isRunning = false;
	}

	/**
	 * Changes the password of a player
	 * 
	 * @param username
	 * @param newPassword
	 * @param oldPassword
	 * @param session
	 */
	private void changePass(String username, String newPassword, String oldPassword, IoSession session)
	{

		try
		{
			PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM pn_bans WHERE username = ?");
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();

			if(rs.first())
			{
				// if we got a result, compare their old password to the one we
				// have stored for them
				if(rs.getString("password").compareTo(oldPassword) == 0)
				{

					PreparedStatement ps1 = DatabaseConnection.getConnection().prepareStatement("UPDATE pn_members SET password = ? WHERE username = ?");
					ps1.setString(1, newPassword);
					ps1.setString(2, username);
					ps1.executeUpdate();
					ps1.close();
					// tell them their password was changed successfully
					session.write("ps");
					return;
				}
			}

			rs.close();
			ps.close();
		}
		catch(SQLException sqle)
		{
			sqle.printStackTrace();
		}
		// tell them we failed to change their password
		session.write("pe");
	}

	/**
	 * Logs in a player
	 * 
	 * @param username
	 * @param language
	 * @param session
	 * @param result
	 */
	private void login(String username, char language, IoSession session, ResultSet result)
	{
		// They are not logged in elsewhere, set the current login to the
		// current server
		long time = System.currentTimeMillis();
		/* Attempt to log the player in */
		Player player = getPlayerObject(result);
		player.setLastLoginTime(time);
		player.setTcpSession(session);
		player.setLanguage(Language.values()[Integer.parseInt(String.valueOf(language))]);
		/* Update the database with login information */

		/*
		 * m_database.query("UPDATE pn_members SET lastLoginServer='" + MySqlManager.parseSQL(GameServer.getServerName()) + "', lastLoginTime='" + time + "' WHERE username='" + MySqlManager.parseSQL(username) + "'"); m_database.query("UPDATE pn_members SET lastLoginIP='" + getIp(session) + "' WHERE username='" + MySqlManager.parseSQL(username) + "'"); m_database.query("UPDATE pn_members SET lastLanguageUsed='" + language + "' WHERE username='" + MySqlManager.parseSQL(username) + "'");
		 */

		try
		{
			PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(
					"UPDATE pn_members SET lastLoginServer = ?, lastLoginTime = ?, lastLoginIP = ?, lastLanguageUsed = ? WHERE username = ?");
			ps.setString(1, GameServer.getServerName());
			ps.setLong(2, time);
			ps.setString(3, TcpProtocolHandler.getIp(session));
			ps.setLong(4, language);
			ps.setString(5, username);
			ps.executeUpdate();
			ps.close();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}

		session.setAttribute("player", player);
		/*
		 * Send success packet to player, set their map and add them to a movement servic
		 */
		this.initialiseClient(player, session);
		/* Add them to the list of players */
		TcpProtocolHandler.addPlayer(player);
		GameServer.getInstance().updatePlayerCount();
		Log.debug(username + " logged in.");
	}

	/**
	 * Sends initial information to the client
	 * 
	 * @param p
	 * @param session
	 */
	private void initialiseClient(Player p, IoSession session)
	{
		session.write("ls" + p.getId() + "," + TimeService.getTime());
		// Add them to the map
		p.setMap(GameServer.getServiceManager().getMovementService().getMapMatrix().getMapByGamePosition(p.getMapX(), p.getMapY()), null);
		// Add them to a movement service
		GameServer.getServiceManager().getMovementService().getMovementManager().addPlayer(p);
		// Send their Pokemon information to them
		p.updateClientParty();
		// Send bag to them
		p.updateClientBag();
		// Send money
		p.updateClientMoney();
		// Send their friend list to them
		p.updateClientFriends();
		// Send badges
		p.updateClientBadges();
		p.initializeClientSkills();
	}

	/**
	 * Returns a playerchar object from a resultset of player data
	 * 
	 * @param data
	 * @return
	 */
	private Player getPlayerObject(ResultSet result)
	{
		try
		{
			Player p = new Player(result.getString("username"));
			Pokemon[] party = new Pokemon[6];
			PokemonBox[] boxes = new PokemonBox[9];

			p.setName(result.getString("username"));
			p.setVisible(true);
			// Set co-ordinates
			p.setX(result.getInt("x"));
			p.setY(result.getInt("y"));
			p.setMapX(result.getInt("mapX"));
			p.setMapY(result.getInt("mapY"));
			p.setId(result.getInt("id"));
			p.setAdminLevel(result.getInt("adminLevel"));
			p.setMuted(result.getBoolean("muted"));
			p.setLastHeal(result.getInt("healX"), result.getInt("healY"), result.getInt("healMapX"), result.getInt("healMapY"));
			p.setSurfing(Boolean.parseBoolean(result.getString("isSurfing")));
			// Set money and skills
			p.setSprite(result.getInt("sprite"));
			p.setMoney(result.getInt("money"));
			p.setHerbalismExp(result.getInt("skHerb"));
			p.setCraftingExp(result.getInt("skCraft"));
			p.setFishingExp(result.getInt("skFish"));
			p.setTrainingExp(result.getInt("skTrain"));
			p.setCoordinatingExp(result.getInt("skCoord"));
			p.setBreedingExp(result.getInt("skBreed"));
			// Retrieve refences to all Pokemon
			PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM pn_party WHERE id = ?");
			ps.setInt(1, result.getInt("party"));
			ResultSet rs = ps.executeQuery();
			rs.first();

			PreparedStatement ps1 = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM pn_pokemon WHERE currentTrainerName = ?");
			ps1.setString(1, p.getName());
			ResultSet rs1 = ps1.executeQuery();
			int boxNumber = 0;
			int boxPosition = 0;
			/*
			 * Loop through all Pokemon belonging to this player and add them to their party/box
			 */
			while(rs1.next())
			{
				boolean isParty = false;
				int partyIndex = -1;
				/* Checks if Pokemon is in party */
				for(int i = 0; i < 6; i++)
				{
					if(rs.getInt("pokemon" + i) == rs1.getInt("id"))
					{
						isParty = true;
						partyIndex = i;
						break;
					}
				}
				/* If the pokemon is in party, add it to party */
				if(isParty)
				{
					party[partyIndex] = getPokemonObject(rs1);
				}
				else
				{
					/* Else, add it to box if space is available */
					if(boxNumber < 9)
					{
						/* If there's space in this box, add it to the box */
						if(boxPosition < 9)
						{
							boxes[boxNumber] = new PokemonBox();
							boxes[boxNumber].setPokemon(boxPosition, getPokemonObject(rs1));
						}
						else
						{
							/* Else open up a new box and add it to box */
							boxPosition = 0;
							boxNumber++;
							if(boxNumber < 9)
							{
								boxes[boxNumber] = new PokemonBox();
								boxes[boxNumber].setPokemon(boxPosition, getPokemonObject(rs1));
							}
						}
						boxPosition++;
					}
				}
			}
			rs.close();
			ps.close();
			rs1.close();
			ps1.close();
			
			p.setParty(party);
			for(int idx = 0; idx < 9; idx++) // FUUU, we only have 9 boxes, not 30 :S
			{
				p.setBox(idx, boxes[idx]);
			}

			try
			{
				PreparedStatement ps2 = DatabaseConnection.getConnection().prepareStatement(
						"SELECT item, quantity FROM pn_bag WHERE member = ?");
				ps2.setInt(1, result.getInt("id"));
				ResultSet rs2 = ps2.executeQuery();
				
				Bag bag = new Bag(p.getId());
				while(rs2.next())
				{
					bag.addItem(rs2.getInt("item"), rs2.getInt("quantity"));
				}
				p.setBag(bag);
				ps2.close();
				rs2.close();
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}

			// Attach badges
			p.generateBadges(result.getString("badges"));
			return p;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Returns a Pokemon object based on a set of data
	 * 
	 * @param data
	 * @return
	 */
	private Pokemon getPokemonObject(ResultSet data)
	{
		if(data != null)
		{
			try
			{
				/*
				 * First generate the Pokemons moves
				 */
				MoveListEntry[] moves = new MoveListEntry[4];
				moves[0] = (data.getString("move0") != null && !data.getString("move0").equalsIgnoreCase("null") ? DataService.getMovesList().getMove(data.getString("move0")) : null);
				moves[1] = (data.getString("move1") != null && !data.getString("move1").equalsIgnoreCase("null") ? DataService.getMovesList().getMove(data.getString("move1")) : null);
				moves[2] = (data.getString("move2") != null && !data.getString("move2").equalsIgnoreCase("null") ? DataService.getMovesList().getMove(data.getString("move2")) : null);
				moves[3] = (data.getString("move3") != null && !data.getString("move3").equalsIgnoreCase("null") ? DataService.getMovesList().getMove(data.getString("move3")) : null);
				/*
				 * Create the new Pokemon
				 */
				Pokemon p = new Pokemon(DataService.getBattleMechanics(), PokemonSpecies.getDefaultData().getPokemonByName(data.getString("speciesName")), PokemonNature.getNatureByName(data
						.getString("nature")), data.getString("abilityName"), data.getString("itemName"), data.getInt("gender"), data.getInt("level"), new int[] { data.getInt("ivHP"),
						data.getInt("ivATK"), data.getInt("ivDEF"), data.getInt("ivSPD"), data.getInt("ivSPATK"), data.getInt("ivSPDEF") }, new int[] { data.getInt("evHP"), data.getInt("evATK"),
						data.getInt("evDEF"), data.getInt("evSPD"), data.getInt("evSPATK"), data.getInt("evSPDEF") }, moves, new int[] { data.getInt("ppUp0"), data.getInt("ppUp1"),
						data.getInt("ppUp2"), data.getInt("ppUp3") });
				p.reinitialise();
				/*
				 * Set exp, nickname, isShiny and exp gain type
				 */
				p.setBaseExp(data.getInt("baseExp"));
				p.setExp(Double.parseDouble(data.getString("exp")));
				p.setName(data.getString("name"));
				p.setHappiness(data.getInt("happiness"));
				p.setShiny(Boolean.parseBoolean(data.getString("isShiny")));
				p.setExpType(ExpTypes.valueOf(data.getString("expType")));
				p.setOriginalTrainer(data.getString("originalTrainerName"));
				p.setDatabaseID(data.getInt("id"));
				p.setDateCaught(data.getString("date"));
				p.setIsFainted(Boolean.parseBoolean(data.getString("isFainted")));
				/*
				 * Contest stats (beauty, cute, etc.)
				 */
				String[] cstats = data.getString("contestStats").split(",");
				p.setContestStat(0, Integer.parseInt(cstats[0]));
				p.setContestStat(1, Integer.parseInt(cstats[1]));
				p.setContestStat(2, Integer.parseInt(cstats[2]));
				p.setContestStat(3, Integer.parseInt(cstats[3]));
				p.setContestStat(4, Integer.parseInt(cstats[4]));
				/*
				 * Sets the stats
				 */
				p.calculateStats(true);
				p.setHealth(data.getInt("hp"));
				p.setRawStat(1, data.getInt("atk"));
				p.setRawStat(2, data.getInt("def"));
				p.setRawStat(3, data.getInt("speed"));
				p.setRawStat(4, data.getInt("spATK"));
				p.setRawStat(5, data.getInt("spDEF"));
				/*
				 * Sets the pp information
				 */
				p.setPp(0, data.getInt("pp0"));
				p.setPp(1, data.getInt("pp1"));
				p.setPp(2, data.getInt("pp2"));
				p.setPp(3, data.getInt("pp3"));
				p.setPpUp(0, data.getInt("ppUp0"));
				p.setPpUp(0, data.getInt("ppUp1"));
				p.setPpUp(0, data.getInt("ppUp2"));
				p.setPpUp(0, data.getInt("ppUp3"));
				return p;
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
		}
		return null;
	}
}
