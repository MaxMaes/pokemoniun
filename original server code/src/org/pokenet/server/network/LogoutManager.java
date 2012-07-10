package org.pokenet.server.network;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;
import org.pokenet.server.GameServer;
import org.pokenet.server.backend.entity.Bag;
import org.pokenet.server.backend.entity.BagItem;
import org.pokenet.server.backend.entity.Player;
import org.pokenet.server.backend.entity.PokemonBox;
import org.pokenet.server.battle.DataService;
import org.pokenet.server.battle.Pokemon;
import org.pokenet.server.battle.PokemonSpecies;
import org.pokenet.server.battle.mechanics.statuses.abilities.IntrinsicAbility;
import org.pokenet.server.feature.DatabaseConnection;

/**
 * Handles logging players out
 * 
 * @author shadowkanji
 */
public class LogoutManager implements Runnable
{
	private Queue<Player> m_logoutQueue;
	private Thread m_thread;
	private boolean m_isRunning = false;

	/**
	 * Default constructor
	 */
	public LogoutManager()
	{
		m_logoutQueue = new LinkedList<Player>();
	}

	/**
	 * Returns how many players are in the save queue
	 * 
	 * @return
	 */
	public int getPlayerAmount()
	{
		return m_logoutQueue.size();
	}

	/**
	 * Attempts to logout a player by saving their data. Returns true on success
	 * 
	 * @param player
	 */
	private boolean attemptLogout(Player player)
	{
		// Remove player from their map if it hasn't been done already
		if(player.getMap() != null)
			player.getMap().removeChar(player);
		TcpProtocolHandler.removePlayer(player);
		GameServer.getInstance().updatePlayerCount();
		// Store all player information
		if(!savePlayer(player))
		{
			return false;
		}
		// Finally, store that the player is logged out and close connection
		try
		{
			PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("UPDATE pn_members SET lastLoginServer = ? WHERE id = ?");
			ps.setString(1, null);
			ps.setInt(2, player.getId());
			ps.executeUpdate();
			ps.close();
		}
		catch(SQLException ex)
		{
			ex.printStackTrace();
		}
		GameServer.getServiceManager().getMovementService().removePlayer(player.getName());
		return true;
	}

	/**
	 * Queues a player to be logged out
	 * 
	 * @param player
	 */
	public void queuePlayer(Player player)
	{
		if(m_thread == null || !m_thread.isAlive())
			start();
		if(!m_logoutQueue.contains(player))
			m_logoutQueue.offer(player);
	}

	/**
	 * Called by m_thread.start()
	 */
	public void run()
	{
		GameServer.THREADS++;
		System.out.println("LogoutManager started.");
		while(m_isRunning)
		{
			synchronized(m_logoutQueue)
			{
				if(m_logoutQueue.peek() != null)
				{
					Player p = m_logoutQueue.poll();
					synchronized(p)
					{
						if(p != null)
						{
							if(!attemptLogout(p))
							{
								m_logoutQueue.add(p);
							}
							else
							{
								p.dispose();
								System.out.println("INFO: " + p.getName() + " logged out.");
								p = null;
							}
						}
					}
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
		System.out.println("LogoutManager stopped (" + GameServer.THREADS + " threads remaining)");
		System.out.println("INFO: All player data saved successfully.");
	}

	/**
	 * Start this logout manager
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
	 * Stop this logout manager
	 */
	public void stop()
	{
		// Stop the thread
		m_isRunning = false;
	}

	/**
	 * Saves a player object to the database (Updates an existing player)
	 * 
	 * @param p
	 * @return
	 */
	private boolean savePlayer(Player p)
	{
		try
		{
			/*
			 * First, check if they have logged in somewhere else. This is useful for when as server loses its internet connection
			 */
			PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM pn_members WHERE id = ?");
			ps.setInt(1, p.getId());
			ResultSet rs = ps.executeQuery();
			rs.first();
			if(rs.getLong("lastLoginTime") == p.getLastLoginTime())
			{
				/* Check they are not trading */
				if(p.isTrading())
				{
					/* If the trade is still executing, don't save them yet */
					if(!p.getTrade().endTrade())
						return false;
				}
				/*
				 * Update the player row
				 */
				String badges = "";
				for(int i = 0; i < 42; i++)
				{
					if(p.getBadges()[i] == 1)
						badges = badges + "1";
					else
						badges = badges + "0";
				}
				PreparedStatement ps1 = DatabaseConnection
						.getConnection()
						.prepareStatement(
								"UPDATE pn_members SET muted = ?, sprite = ?, money = ?, skHerb = ?, skCraft = ?, skFish = ?, skTrain = ?, skCoord = ?, skBreed = ?, x = ?, y = ?, mapX = ?, mapY = ?, healX = ?, healY = ?, healMapX = ?, healMapY = ?, isSurfing = ?, badges = ? WHERE id = ?");
				ps1.setBoolean(1, p.isMuted()); // muted
				ps1.setInt(2, p.getRawSprite()); // sprite
				ps1.setInt(3, p.getMoney()); // money
				ps1.setInt(4, p.getHerbalismExp()); // skHerb
				ps1.setInt(5, p.getCraftingExp()); // skCraft
				ps1.setInt(6, p.getFishingExp()); // skFish
				ps1.setInt(7, p.getTrainingExp()); // skTrain
				ps1.setInt(8, p.getCoordinatingExp()); // skCoord
				ps1.setInt(9, p.getBreedingExp()); // skBreed
				ps1.setInt(10, p.getX()); // x
				ps1.setInt(11, p.getY()); // y
				ps1.setInt(12, p.getMapX()); // mapX
				ps1.setInt(13, p.getMapY()); // mapY
				ps1.setInt(14, p.getHealX()); // healX
				ps1.setInt(15, p.getHealY()); // healY
				ps1.setInt(16, p.getHealMapX()); // healMapX
				ps1.setInt(17, p.getHealMapY()); // healMapY
				ps1.setBoolean(18, p.isSurfing()); // isSurfing
				ps1.setString(19, badges); // badges
				ps1.setInt(20, p.getId()); // id

				ps1.executeUpdate();
				ps1.close();
				/*
				 * Second, update the party
				 */
				// Save all the Pokemon
				for(int i = 0; i < 6; i++)
				{
					if(p.getParty() != null && p.getParty()[i] != null)
					{
						if(p.getParty()[i].getDatabaseID() == -1)
						{
							// This is a new Pokemon, add it to the database
							if(saveNewPokemon(p.getParty()[i], p.getName()) == -1)
								return false;
						}
						else
						{
							// Old Pokemon, just update
							if(!savePokemon(p.getParty()[i], p.getName()))
								return false;
						}
					}
				}
				// Save all the Pokemon id's in the player's party
				if(p.getParty() != null)
				{
					ps1 = DatabaseConnection.getConnection()
							.prepareStatement("UPDATE pn_party SET pokemon0 = ?, pokemon1 = ?, pokemon2 = ?, pokemon3 = ?, pokemon4 = ?, pokemon5 = ? WHERE member = ?");
					ps1.setInt(1, p.getParty()[0] != null ? p.getParty()[0].getDatabaseID() : -1);
					ps1.setInt(2, p.getParty()[1] != null ? p.getParty()[1].getDatabaseID() : -1);
					ps1.setInt(3, p.getParty()[2] != null ? p.getParty()[2].getDatabaseID() : -1);
					ps1.setInt(4, p.getParty()[3] != null ? p.getParty()[3].getDatabaseID() : -1);
					ps1.setInt(5, p.getParty()[4] != null ? p.getParty()[4].getDatabaseID() : -1);
					ps1.setInt(6, p.getParty()[5] != null ? p.getParty()[5].getDatabaseID() : -1);
					ps1.setInt(7, p.getId());
					ps1.executeUpdate();
					ps1.close();
				}
				else
					return true;
				/*
				 * Save the player's bag
				 */
				if(p.getBag() == null || !saveBag(p.getBag()))
					return false;
				/*
				 * Finally, update all the boxes
				 */
				for(int i = 0; i < 9; i++)
				{
					PokemonBox box = p.getBox(i);
					if(box != null)
					{
						for(int j = 0; j < 30; j++)
						{
							if(box.getPokemon(j) != null)
							{
								if(box.getPokemon(j).getDatabaseID() == -1)
								{
									/* This is a new Pokemon, create it in the database */
									if(saveNewPokemon(box.getPokemon(j), p.getName()) == -1)
										return false;
								}
								else
								{
									/* Update an existing pokemon */
									if(!savePokemon(box.getPokemon(j), p.getName()))
									{
										return false;
									}
								}
							}
						}
					}
				}
				rs.close();
				ps.close();
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
	 * Saves a pokemon to the database that didn't exist in it before
	 * 
	 * @param p
	 */
	private int saveNewPokemon(Pokemon p, String currentTrainer)
	{
		try
		{
			/*
			 * Due to issues with Pokemon not receiving abilities, we're going to ensure they have one
			 */
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
			/*
			 * Insert the Pokemon into the database
			 */
			PreparedStatement ps = DatabaseConnection
					.getConnection()
					.prepareStatement(
							"INSERT INTO pn_pokemon (name, speciesName, exp, baseExp, expType, isFainted, level, happiness, gender, nature, abilityName, itemName, isShiny, currentTrainerName, originalTrainerName, date, contestStats) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			ps.setString(1, p.getName());
			ps.setString(2, p.getSpeciesName());
			ps.setDouble(3, p.getExp());
			ps.setDouble(4, p.getBaseExp());
			ps.setString(5, p.getExpType().name());
			ps.setBoolean(6, p.isFainted());
			ps.setInt(7, p.getLevel());
			ps.setInt(8, p.getHappiness());
			ps.setInt(9, p.getGender());
			ps.setString(10, p.getNature().getName());
			ps.setString(11, p.getAbilityName());
			ps.setString(12, p.getItemName());
			ps.setBoolean(13, p.isShiny());
			ps.setString(14, currentTrainer);
			ps.setString(15, p.getOriginalTrainer());
			ps.setString(16, p.getDateCaught());
			ps.setString(17, p.getContestStatsAsString());
			ps.executeUpdate();
			ps.close();
			/*
			 * Get the pokemon's database id and attach it to the pokemon. This needs to be done so it can be attached to the player in the database later.
			 */

			ps = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM pn_pokemon WHERE originalTrainerName = ? AND date = ? AND name = ? AND exp = ?");
			ps.setString(1, p.getOriginalTrainer());
			ps.setString(2, p.getDateCaught());
			ps.setString(3, p.getSpeciesName());
			ps.setDouble(4, p.getExp());
			ResultSet rs = ps.executeQuery();

			rs.first();
			p.setDatabaseID(rs.getInt("id"));
			rs.close();
			ps.close();

			ps = DatabaseConnection
					.getConnection()
					.prepareStatement(
							"UPDATE pn_pokemon SET name = ?, speciesName = ?, exp = ?, baseExp = ?, expType = ?, isFainted = ?, level = ?, happiness = ?, gender = ?, nature = ?, abilityName = ?, itemName = ?, isShiny = ?, currentTrainerName = ?, originalTrainerName = ?, date = ?, contestStats = ?, move0 = ?, move1 = ?, move2 = ?, move3 = ?, hp = ?, atk = ?, def = ?, speed = ?, spATK = ?, spDEF = ?, evHP = ?, evATK = ?, evDEF = ?, evSPD = ?, evSPATK = ?, evSPDEF = ?, ivHP = ?, ivATK = ?, ivDEF = ?, ivSPD = ?, ivSPATK = ?, ivSPDEF = ?, pp0 = ?, pp1 = ?, pp2 = ?, pp3 = ?, maxpp0 = ?, maxpp1 = ?, maxpp2 = ?, maxpp3 = ?, ppUp0 = ?, ppUp1 = ?, ppUp2 = ?, ppUp3 = ? WHERE id = ?");
			ps.setString(1, p.getName());
			ps.setString(2, p.getSpeciesName());
			ps.setDouble(3, p.getExp());
			ps.setDouble(4, p.getBaseExp());
			ps.setString(5, p.getExpType().name());
			ps.setBoolean(6, p.isFainted());
			ps.setInt(7, p.getLevel());
			ps.setInt(8, p.getHappiness());
			ps.setInt(9, p.getGender());
			ps.setString(10, p.getNature().getName());
			ps.setString(11, p.getAbilityName());
			ps.setString(12, p.getItemName());
			ps.setBoolean(13, p.isShiny());
			ps.setString(14, currentTrainer);
			ps.setString(15, p.getOriginalTrainer());
			ps.setString(16, p.getDateCaught());
			ps.setString(17, p.getContestStatsAsString());
			ps.setString(18, p.getMove(0).getName());
			ps.setString(19, p.getMove(1) == null ? null : p.getMove(1).getName());
			ps.setString(20, p.getMove(2) == null ? null : p.getMove(2).getName());
			ps.setString(21, p.getMove(3) == null ? null : p.getMove(3).getName());
			ps.setInt(22, p.getHealth());
			ps.setInt(23, p.getStat(1));
			ps.setInt(24, p.getStat(2));
			ps.setInt(25, p.getStat(3));
			ps.setInt(26, p.getStat(4));
			ps.setInt(27, p.getStat(5));
			ps.setInt(28, p.getEv(0));
			ps.setInt(29, p.getEv(1));
			ps.setInt(30, p.getEv(2));
			ps.setInt(31, p.getEv(3));
			ps.setInt(32, p.getEv(4));
			ps.setInt(33, p.getEv(5));
			ps.setInt(34, p.getIv(0));
			ps.setInt(35, p.getIv(1));
			ps.setInt(36, p.getIv(2));
			ps.setInt(37, p.getIv(3));
			ps.setInt(38, p.getIv(4));
			ps.setInt(39, p.getIv(5));
			ps.setInt(40, p.getPp(0));
			ps.setInt(41, p.getPp(1));
			ps.setInt(42, p.getPp(2));
			ps.setInt(43, p.getPp(3));
			ps.setInt(44, p.getMaxPp(0));
			ps.setInt(45, p.getMaxPp(1));
			ps.setInt(46, p.getMaxPp(2));
			ps.setInt(47, p.getMaxPp(3));
			ps.setInt(48, p.getPpUpCount(0));
			ps.setInt(49, p.getPpUpCount(1));
			ps.setInt(50, p.getPpUpCount(2));
			ps.setInt(51, p.getPpUpCount(3));
			ps.setInt(52, p.getDatabaseID()); // Da fuq, 52 columns in the table pn_pokemon >; Wooohoo, did a quick count and it should be correct.
			ps.executeUpdate();
			ps.close();
			
			return rs.getInt("id");
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			return -1;
		}
	}

	/**
	 * Updates a pokemon in the database
	 * 
	 * @param p
	 */
	private boolean savePokemon(Pokemon p, String currentTrainer)
	{
		try
		{
			/*
			 * Update the pokemon in the database
			 */
			PreparedStatement ps = DatabaseConnection
					.getConnection()
					.prepareStatement(
							"UPDATE pn_pokemon SET move0 = ?, move1 = ?, move2 = ?, move3 = ?, hp = ?, atk = ?, def = ?, speed = ?, spATK = ?, spDEF = ?, evHP = ?, evATK = ?, evDEF = ?, evSPD = ?, evSPATK = ?, evSPDEF = ?, ivHP = ?, ivATK = ?, ivDEF = ?, ivSPD = ?, ivSPATK = ?, ivSPDEF = ?, pp0 = ?, pp1 = ?, pp2 = ?, pp3 = ?, maxpp0 = ?, maxpp1 = ?, maxpp2 = ?, maxpp3 = ?, ppUp0 = ?, ppUp1 = ?, ppUp2 = ?, ppUp3 = ? WHERE id = ?");
			ps.setString(1, p.getMove(0).getName());
			ps.setString(2, p.getMove(1) == null ? null : p.getMove(1).getName());
			ps.setString(3, p.getMove(2) == null ? null : p.getMove(2).getName());
			ps.setString(4, p.getMove(3) == null ? null : p.getMove(3).getName());
			ps.setInt(5, p.getHealth());
			ps.setInt(6, p.getStat(1));
			ps.setInt(7, p.getStat(2));
			ps.setInt(8, p.getStat(3));
			ps.setInt(9, p.getStat(4));
			ps.setInt(10, p.getStat(5));
			ps.setInt(11, p.getEv(0));
			ps.setInt(12, p.getEv(1));
			ps.setInt(13, p.getEv(2));
			ps.setInt(14, p.getEv(3));
			ps.setInt(15, p.getEv(4));
			ps.setInt(16, p.getEv(5));
			ps.setInt(17, p.getIv(0));
			ps.setInt(18, p.getIv(1));
			ps.setInt(19, p.getIv(2));
			ps.setInt(20, p.getIv(3));
			ps.setInt(21, p.getIv(4));
			ps.setInt(22, p.getIv(5));
			ps.setInt(23, p.getPp(0));
			ps.setInt(24, p.getPp(1));
			ps.setInt(25, p.getPp(2));
			ps.setInt(26, p.getPp(3));
			ps.setInt(27, p.getMaxPp(0));
			ps.setInt(28, p.getMaxPp(1));
			ps.setInt(29, p.getMaxPp(2));
			ps.setInt(30, p.getMaxPp(3));
			ps.setInt(31, p.getPpUpCount(0));
			ps.setInt(32, p.getPpUpCount(1));
			ps.setInt(33, p.getPpUpCount(2));
			ps.setInt(34, p.getPpUpCount(3));
			ps.setInt(35, p.getDatabaseID());
			ps.executeUpdate();

			ps.close();

			
			return true;
		}
		catch(SQLException e)
		{
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
	private boolean saveBag(Bag b)
	{
		try
		{
			// Destroy item data to prevent dupes.
			PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("DELETE FROM pn_bag WHERE member = ?");
			ps.setInt(1, b.getMemberId());
			ps.executeUpdate();
			ps.close();
			for(int i = 0; i < b.getItems().size(); i++)
			{
				BagItem item = b.getItems().get(i);
				if(item != null)
				{
					/*
					 * NOTE: Items are stored as values 1 - 999
					 */
					ps = DatabaseConnection.getConnection().prepareStatement("INSERT INTO pn_bag (member, item, quantity) VALUES (?, ?, ?)");
					ps.setInt(1, b.getMemberId());
					ps.setInt(2, item.getItemNumber());
					ps.setInt(3, item.getQuantity());
					ps.executeUpdate();
					ps.close();
				}
			}
			return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

}
