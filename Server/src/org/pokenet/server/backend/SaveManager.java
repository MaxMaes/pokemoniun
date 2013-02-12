package org.pokenet.server.backend;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.pokenet.server.backend.entity.Bag;
import org.pokenet.server.backend.entity.BagItem;
import org.pokenet.server.backend.entity.Player;
import org.pokenet.server.battle.DataService;
import org.pokenet.server.battle.Pokemon;
import org.pokenet.server.battle.PokemonSpecies;
import org.pokenet.server.battle.mechanics.statuses.abilities.IntrinsicAbility;
import org.pokenet.server.feature.DatabaseConnection;
import org.pokenet.server.network.MySqlManager;

public class SaveManager
{
	private MySqlManager m_database;

	public SaveManager()
	{
		m_database = MySqlManager.getInstance();
	}

	/**
	 * Saves a bag to the database.
	 * 
	 * @param b
	 * @return
	 */
	public boolean saveBag(Bag b)
	{
		try
		{
			/* Destroy item data to prevent dupes.
			 * TODO: UPDATE when exists, otherwise INSERT. More efficient and FK safer. */
			PreparedStatement bagStatement = DatabaseConnection.getConnection().prepareStatement("DELETE FROM pn_bag WHERE member = ?");
			bagStatement.setInt(1, b.getMemberId());
			bagStatement.executeUpdate();
			bagStatement.close();
			for(int i = 0; i < b.getItems().size(); i++)
			{
				BagItem item = b.getItems().get(i);
				if(item != null)
				{
					/* NOTE: Items are stored as values 1 - 999 */
					bagStatement = DatabaseConnection.getConnection().prepareStatement("INSERT INTO pn_bag (member, item, quantity) VALUES (?, ?, ?)");
					bagStatement.setInt(1, b.getMemberId());
					bagStatement.setInt(2, item.getItemNumber());
					bagStatement.setInt(3, item.getQuantity());
					bagStatement.executeUpdate();
					bagStatement.close();
				}
			}
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
	public int saveNewPokemon(Pokemon poke, String currentTrainer, MySqlManager db)
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
			PreparedStatement pokeStatement = DatabaseConnection
					.getConnection()
					.prepareStatement(
							"INSERT INTO pn_pokemon VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pokeStatement.setString(1, poke.getName());
			pokeStatement.setString(2, poke.getSpeciesName());
			pokeStatement.setDouble(3, poke.getExp());
			pokeStatement.setDouble(4, poke.getBaseExp());
			pokeStatement.setString(5, poke.getExpType().name());
			pokeStatement.setBoolean(6, poke.isFainted());
			pokeStatement.setInt(7, poke.getLevel());
			pokeStatement.setInt(8, poke.getHappiness());
			pokeStatement.setInt(9, poke.getGender());
			pokeStatement.setString(10, poke.getNature().getName());
			pokeStatement.setString(11, poke.getAbilityName());
			pokeStatement.setString(12, poke.getItemName());
			pokeStatement.setBoolean(13, poke.isShiny());
			pokeStatement.setString(14, poke.getOriginalTrainer());
			pokeStatement.setString(15, currentTrainer);
			pokeStatement.setString(16, poke.getContestStatsAsString());
			pokeStatement.setString(17, poke.getMove(0).getName());
			pokeStatement.setString(18, poke.getMove(1) == null ? null : poke.getMove(1).getName());
			pokeStatement.setString(19, poke.getMove(2) == null ? null : poke.getMove(2).getName());
			pokeStatement.setString(20, poke.getMove(3) == null ? null : poke.getMove(3).getName());
			pokeStatement.setInt(21, poke.getHealth());
			pokeStatement.setInt(22, poke.getStat(1));
			pokeStatement.setInt(23, poke.getStat(2));
			pokeStatement.setInt(24, poke.getStat(3));
			pokeStatement.setInt(25, poke.getStat(4));
			pokeStatement.setInt(26, poke.getStat(5));
			pokeStatement.setInt(27, poke.getEv(0));
			pokeStatement.setInt(28, poke.getEv(1));
			pokeStatement.setInt(29, poke.getEv(2));
			pokeStatement.setInt(30, poke.getEv(3));
			pokeStatement.setInt(31, poke.getEv(4));
			pokeStatement.setInt(32, poke.getEv(5));
			pokeStatement.setInt(33, poke.getIv(0));
			pokeStatement.setInt(34, poke.getIv(1));
			pokeStatement.setInt(35, poke.getIv(2));
			pokeStatement.setInt(36, poke.getIv(3));
			pokeStatement.setInt(37, poke.getIv(4));
			pokeStatement.setInt(38, poke.getIv(5));
			pokeStatement.setInt(39, poke.getPp(0));
			pokeStatement.setInt(40, poke.getPp(1));
			pokeStatement.setInt(41, poke.getPp(2));
			pokeStatement.setInt(42, poke.getPp(3));
			pokeStatement.setInt(43, poke.getMaxPp(0));
			pokeStatement.setInt(44, poke.getMaxPp(1));
			pokeStatement.setInt(45, poke.getMaxPp(2));
			pokeStatement.setInt(46, poke.getMaxPp(3));
			pokeStatement.setInt(47, poke.getPpUpCount(0));
			pokeStatement.setInt(48, poke.getPpUpCount(1));
			pokeStatement.setInt(49, poke.getPpUpCount(2));
			pokeStatement.setInt(50, poke.getPpUpCount(3));
			pokeStatement.setString(51, poke.getDateCaught());
			pokeStatement.setInt(52, poke.getCaughtWithBall());
			pokeStatement.executeUpdate();
			pokeStatement.close();
			/* Get the pokemon's database id and attach it to the pokemon. This needs to be done so it can be attached to the player in the database later. */
			pokeStatement = DatabaseConnection.getConnection().prepareStatement("SELECT id FROM pn_pokemon WHERE originalTrainerName = ? AND date = ?");
			pokeStatement.setString(1, poke.getOriginalTrainer());
			pokeStatement.setString(2, poke.getDateCaught());
			ResultSet result = pokeStatement.executeQuery();
			result.first();
			int pokeDbId = result.getInt("id");
			result.close();
			pokeStatement.close();
			poke.setDatabaseID(pokeDbId);
			return pokeDbId;
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
		try
		{
			/* First, check if they have logged in somewhere else. This is useful for when as server loses its internet connection */
			PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT lastLoginTime FROM pn_members WHERE id = ?");
			ps.setInt(1, p.getId());
			ResultSet data = ps.executeQuery();
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
				PreparedStatement memberStatement = DatabaseConnection
						.getConnection()
						.prepareStatement(
								"UPDATE pn_members SET muted = ?, sprite = ?, money = ?, skHerb = ?, skCraft = ?, skFish = ?, skTrain = ?, skCoord = ?, skBreed = ?, x = ?, y = ?, mapX = ?, mapY = ?, healX = ?, healY = ?, healMapX = ?, healMapY = ?, isSurfing = ?, badges = ? WHERE id = ?");
				memberStatement.setBoolean(1, p.isMuted()); // muted
				memberStatement.setInt(2, p.getRawSprite()); // sprite
				memberStatement.setInt(3, p.getMoney()); // money
				memberStatement.setInt(4, p.getHerbalismExp()); // skHerb
				memberStatement.setInt(5, p.getCraftingExp()); // skCraft
				memberStatement.setInt(6, p.getFishingExp()); // skFish
				memberStatement.setInt(7, p.getTrainingExp()); // skTrain
				memberStatement.setInt(8, p.getCoordinatingExp()); // skCoord
				memberStatement.setInt(9, p.getBreedingExp()); // skBreed
				memberStatement.setInt(10, p.getX()); // x
				memberStatement.setInt(11, p.getY()); // y
				memberStatement.setInt(12, p.getMapX()); // mapX
				memberStatement.setInt(13, p.getMapY()); // mapY
				memberStatement.setInt(14, p.getHealX()); // healX
				memberStatement.setInt(15, p.getHealY()); // healY
				memberStatement.setInt(16, p.getHealMapX()); // healMapX
				memberStatement.setInt(17, p.getHealMapY()); // healMapY
				memberStatement.setBoolean(18, p.isSurfing()); // isSurfing
				memberStatement.setString(19, badges); // badges
				memberStatement.setInt(20, p.getId()); // id
				memberStatement.executeUpdate();
				memberStatement.close();
				/* m_database.query("UPDATE pn_members SET " + "muted='" + p.isMuted() + "', " + "sprite='" + p.getRawSprite() + "', " + "money='" + p.getMoney() + "', " + "skHerb='"
				 * + p.getHerbalismExp() + "', " + "skCraft='" + p.getCraftingExp() + "', " + "skFish='" + p.getFishingExp() + "', " + "skTrain='" + p.getTrainingExp() + "', " + "skCoord='"
				 * + p.getCoordinatingExp() + "', " + "skBreed='" + p.getBreedingExp() + "', " + "x='" + p.getX() + "', " + "y='" + p.getY() + "', " + "mapX='" + p.getMapX() + "', " + "mapY='"
				 * + p.getMapY() + "', " + "healX='" + p.getHealX() + "', " + "healY='" + p.getHealY() + "', " + "healMapX='" + p.getHealMapX() + "', " + "healMapY='" + p.getHealMapY() + "', "
				 * + "isSurfing='" + String.valueOf(p.isSurfing()) + "', " + "badges='" + badges + "' " + "WHERE id='" + p.getId() + "'"); */
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
				{
					/* m_database.query("UPDATE pn_party SET " + "pokemon0='" + (p.getParty()[0] != null ? p.getParty()[0].getDatabaseID() : -1) + "', " + "pokemon1='"
					 * + (p.getParty()[1] != null ? p.getParty()[1].getDatabaseID() : -1) + "', " + "pokemon2='" + (p.getParty()[2] != null ? p.getParty()[2].getDatabaseID() : -1) + "', "
					 * + "pokemon3='" + (p.getParty()[3] != null ? p.getParty()[3].getDatabaseID() : -1) + "', " + "pokemon4='" + (p.getParty()[4] != null ? p.getParty()[4].getDatabaseID() : -1)
					 * + "', " + "pokemon5='" + (p.getParty()[5] != null ? p.getParty()[5].getDatabaseID() : -1) + "' " + "WHERE member='" + p.getId() + "'"); */
					PreparedStatement partyStatement = DatabaseConnection.getConnection().prepareStatement(
							"UPDATE pn_party SET pokemon0 = ?, pokemon1 = ?, pokemon2 = ?, pokemon3 = ?, pokemon4 = ?, pokemon5 = ? WHERE member = ?");
					partyStatement.setInt(1, p.getParty()[0] != null ? p.getParty()[0].getDatabaseID() : -1);
					partyStatement.setInt(2, p.getParty()[1] != null ? p.getParty()[1].getDatabaseID() : -1);
					partyStatement.setInt(3, p.getParty()[2] != null ? p.getParty()[2].getDatabaseID() : -1);
					partyStatement.setInt(4, p.getParty()[3] != null ? p.getParty()[3].getDatabaseID() : -1);
					partyStatement.setInt(5, p.getParty()[4] != null ? p.getParty()[4].getDatabaseID() : -1);
					partyStatement.setInt(6, p.getParty()[5] != null ? p.getParty()[5].getDatabaseID() : -1);
					partyStatement.setInt(7, p.getId());
					partyStatement.executeUpdate();
					partyStatement.close();
				}
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
				data.close();
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
	 * Updates a pokemon in the database
	 * 
	 * @param p
	 */
	public boolean savePokemon(Pokemon p, String currentTrainer)
	{
		try
		{
			/* Due to issues with Pokemon not receiving abilities, we're going to ensure they have one */
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
			/* Update the pokemon in the database */
			PreparedStatement pokemonStatement = DatabaseConnection
					.getConnection()
					.prepareStatement(
							"UPDATE pn_pokemon SET move0 = ?, move1 = ?, move2 = ?, move3 = ?, hp = ?, atk = ?, def = ?, speed = ?, spATK = ?, spDEF = ?, evHP = ?, evATK = ?, evDEF = ?, evSPD = ?, evSPATK = ?, evSPDEF = ?, ivHP = ?, ivATK = ?, ivDEF = ?, ivSPD = ?, ivSPATK = ?, ivSPDEF = ?, pp0 = ?, pp1 = ?, pp2 = ?, pp3 = ?, maxpp0 = ?, maxpp1 = ?, maxpp2 = ?, maxpp3 = ?, ppUp0 = ?, ppUp1 = ?, ppUp2 = ?, ppUp3 = ? WHERE id = ?");
			pokemonStatement.setString(1, p.getMove(0).getName());
			pokemonStatement.setString(2, p.getMove(1) == null ? null : p.getMove(1).getName());
			pokemonStatement.setString(3, p.getMove(2) == null ? null : p.getMove(2).getName());
			pokemonStatement.setString(4, p.getMove(3) == null ? null : p.getMove(3).getName());
			pokemonStatement.setInt(5, p.getHealth());
			pokemonStatement.setInt(6, p.getStat(1));
			pokemonStatement.setInt(7, p.getStat(2));
			pokemonStatement.setInt(8, p.getStat(3));
			pokemonStatement.setInt(9, p.getStat(4));
			pokemonStatement.setInt(10, p.getStat(5));
			pokemonStatement.setInt(11, p.getEv(0));
			pokemonStatement.setInt(12, p.getEv(1));
			pokemonStatement.setInt(13, p.getEv(2));
			pokemonStatement.setInt(14, p.getEv(3));
			pokemonStatement.setInt(15, p.getEv(4));
			pokemonStatement.setInt(16, p.getEv(5));
			pokemonStatement.setInt(17, p.getIv(0));
			pokemonStatement.setInt(18, p.getIv(1));
			pokemonStatement.setInt(19, p.getIv(2));
			pokemonStatement.setInt(20, p.getIv(3));
			pokemonStatement.setInt(21, p.getIv(4));
			pokemonStatement.setInt(22, p.getIv(5));
			pokemonStatement.setInt(23, p.getPp(0));
			pokemonStatement.setInt(24, p.getPp(1));
			pokemonStatement.setInt(25, p.getPp(2));
			pokemonStatement.setInt(26, p.getPp(3));
			pokemonStatement.setInt(27, p.getMaxPp(0));
			pokemonStatement.setInt(28, p.getMaxPp(1));
			pokemonStatement.setInt(29, p.getMaxPp(2));
			pokemonStatement.setInt(30, p.getMaxPp(3));
			pokemonStatement.setInt(31, p.getPpUpCount(0));
			pokemonStatement.setInt(32, p.getPpUpCount(1));
			pokemonStatement.setInt(33, p.getPpUpCount(2));
			pokemonStatement.setInt(34, p.getPpUpCount(3));
			pokemonStatement.setInt(35, p.getDatabaseID());
			pokemonStatement.executeUpdate();
			pokemonStatement.close();
			return true;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
}
