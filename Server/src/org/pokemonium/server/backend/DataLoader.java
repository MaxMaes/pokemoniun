package org.pokemonium.server.backend;

import java.io.File;
import java.util.HashMap;
import java.util.Scanner;

import org.pokemonium.server.backend.entity.HMObject;
import org.pokemonium.server.backend.entity.NPC;
import org.pokemonium.server.backend.entity.QuestNPC;
import org.pokemonium.server.backend.entity.TradeChar;
import org.pokemonium.server.backend.entity.Positionable.Direction;
import org.pokemonium.server.backend.map.ServerMap;
import org.pokemonium.server.backend.map.WarpTile;

/**
 * Handles data loading for maps (NPCs, Warps, HMObjects, etc.)
 * 
 * @author shadowkanji
 */
public class DataLoader implements Runnable
{
	private final File m_file;
	private final ServerMap m_map;

	/**
	 * Constructor
	 * 
	 * @param f
	 */
	public DataLoader(File file, ServerMap map)
	{
		m_file = file;
		m_map = map;
		new Thread(this, "MapDataLoader-Thread").start();
	}

	/**
	 * Called by starting the thread
	 */
	public void run()
	{
		try(Scanner reader = new Scanner(m_file))
		{
			NPC npc = null;
			QuestNPC questnpc = null;
			WarpTile warp = null;
			HMObject hmObject = null;
			TradeChar t = null;
			String line;
			String[] details;
			String[] txt;
			String direction = "Down";
			while(reader.hasNextLine())
			{
				line = reader.nextLine();
				switch(line)
				{
				case "[npc]":
				
					npc = new NPC();
					npc.setName(reader.nextLine());
					direction = reader.nextLine();
					if(direction.equalsIgnoreCase("UP"))
						npc.setFacing(Direction.Up);
					else if(direction.equalsIgnoreCase("LEFT"))
						npc.setFacing(Direction.Left);
					else if(direction.equalsIgnoreCase("RIGHT"))
						npc.setFacing(Direction.Right);
					else
						npc.setFacing(Direction.Down);
					npc.setSprite(Integer.parseInt(reader.nextLine()));
					if(npc.getName().equalsIgnoreCase("NULL") && npc.getSprite() != 0)
						npc.setName("NPC");
					npc.setX(Integer.parseInt(reader.nextLine()) * 32);
					npc.setY(Integer.parseInt(reader.nextLine()) * 32 - 8);
					npc.setOriginalDirection(npc.getFacing());
					// Load possible Pokemons
					line = reader.nextLine();
					if(!line.equalsIgnoreCase("NULL"))
					{
						details = line.split(",");
						HashMap<String, Integer> pokes = new HashMap<String, Integer>();
						for(int i = 0; i < details.length; i = i + 2)
							pokes.put(details[i], Integer.parseInt(details[i + 1]));
						npc.setPossiblePokemon(pokes);
					}
					// Set minimum party level
					npc.setPartySize(Integer.parseInt(reader.nextLine()));
					npc.setBadge(Integer.parseInt(reader.nextLine()));
					// Add all speech, if any
					line = reader.nextLine();
					if(!line.equalsIgnoreCase("NULL"))
					{
						details = line.split(",");
						for(int i = 0; i < details.length; i++)
							npc.addSpeech(Integer.parseInt(details[i]));
					}
					npc.setHealer(Boolean.parseBoolean(reader.nextLine().toLowerCase()));
					npc.setBox(Boolean.parseBoolean(reader.nextLine().toLowerCase()));

					// Setting ShopKeeper as an int.
					String shop = reader.nextLine();
					try
					{
						npc.setShopKeeper(Integer.parseInt(shop.trim()));
					}
					catch(Exception e)
					{
						try
						{
							/* Must be an old shop */
							if(Boolean.parseBoolean(shop.trim().toLowerCase()))
								npc.setShopKeeper(1); // Its an old shop! Yay!
							else
								npc.setShopKeeper(0); // Its an old npc. Not a shop.
						}
						catch(Exception ex)
						{
							npc.setShopKeeper(0);// Dunno what the hell it is, but its not a shop.
						}
					}
				break;
				case "[/npc]":
					m_map.addChar(npc);
				break;
				case "[warp]":
				
					warp = new WarpTile();
					warp.setX(Integer.parseInt(reader.nextLine()));
					warp.setY(Integer.parseInt(reader.nextLine()));
					warp.setWarpX(Integer.parseInt(reader.nextLine()) * 32);
					warp.setWarpY(Integer.parseInt(reader.nextLine()) * 32 - 8);
					warp.setWarpMapX(Integer.parseInt(reader.nextLine()));
					warp.setWarpMapY(Integer.parseInt(reader.nextLine()));
					warp.setBadgeRequirement(Integer.parseInt(reader.nextLine()));
				break;
				case "[/warp]":
					m_map.addWarp(warp);
				break;
				case "[hmobject]":
				
					hmObject = new HMObject();
					hmObject.setName(reader.nextLine());
					hmObject.setType(HMObject.parseHMObject(hmObject.getName()));
					hmObject.setX(Integer.parseInt(reader.nextLine()) * 32);
					hmObject.setOriginalX(hmObject.getX());
					hmObject.setY(Integer.parseInt(reader.nextLine()) * 32 - 8);
					hmObject.setOriginalY(hmObject.getY());
				break;
				case "[/hmobject]":
					hmObject.setMap(m_map, Direction.Down);
				break;
				case "[trade]":
				
					t = new TradeChar();
					t.setName(reader.nextLine());
					direction = reader.nextLine();
					if(direction.equalsIgnoreCase("UP"))
						t.setFacing(Direction.Up);
					else if(direction.equalsIgnoreCase("LEFT"))
						t.setFacing(Direction.Left);
					else if(direction.equalsIgnoreCase("RIGHT"))
						t.setFacing(Direction.Right);
					else
						t.setFacing(Direction.Down);
					t.setSprite(Integer.parseInt(reader.nextLine()));
					t.setX(Integer.parseInt(reader.nextLine()) * 32);
					t.setY(Integer.parseInt(reader.nextLine()) * 32 - 8);
					t.setRequestedPokemon(reader.nextLine(), Integer.parseInt(reader.nextLine()), reader.nextLine());
					t.setOfferedSpecies(reader.nextLine(), Integer.parseInt(reader.nextLine()));
				break;
				case "[/trade]":
					m_map.addChar(t);
				break;
				case "[questNPC]":
					questnpc = new QuestNPC();
					questnpc.setName(reader.nextLine());
					direction = reader.nextLine();
					if(direction.equalsIgnoreCase("UP"))
						questnpc.setFacing(Direction.Up);
					else if(direction.equalsIgnoreCase("LEFT"))
						questnpc.setFacing(Direction.Left);
					else if(direction.equalsIgnoreCase("RIGHT"))
						questnpc.setFacing(Direction.Right);
					else
						questnpc.setFacing(Direction.Down);
					questnpc.setSprite(Integer.parseInt(reader.nextLine()));
					if(questnpc.getName().equalsIgnoreCase("NULL") && questnpc.getSprite() != 0)
						questnpc.setName("NPC");
					questnpc.setX(Integer.parseInt(reader.nextLine()) * 32);
					questnpc.setY(Integer.parseInt(reader.nextLine()) * 32 - 8);
					questnpc.setOriginalDirection(questnpc.getFacing());
					/*
					 * decide on what to do with pokemon.
					 * for now quest npcs don't have pokemon
					 */
					line = reader.nextLine();
					// Set minimum party level
					line = reader.nextLine();
					questnpc.setPartySize(0);
					line = reader.nextLine();
					questnpc.setBadge(-1);
					// Add all speech, if any
					line = reader.nextLine();
					if(!line.equalsIgnoreCase("NULL"))
					{
						details = line.split(";");
						for(int i = 0; i < details.length; i++)
						{
							if(details[i].split(":")[0].equals("normal"))
							{
								txt = details[i].split(":")[1].split(",");
								for(int j = 0; j < txt.length; j++)
									questnpc.addSpeech(Integer.parseInt(txt[j]));
							}
							else	//this is quest speech
							{
								txt = details[i].split(":")[1].split(",");
								for(int j = 0; j < txt.length; j++)
									questnpc.addQuestSpeech(Double.parseDouble(details[i].split(":")[0]), Integer.parseInt(txt[j]));
							}
						}
					}
					questnpc.setHealer(Boolean.parseBoolean(reader.nextLine().toLowerCase()));
					questnpc.setBox(Boolean.parseBoolean(reader.nextLine().toLowerCase()));

					// Setting ShopKeeper as an int.
					questnpc.setShopKeeper(0);
				break;
				case "[/questNPC]":
					m_map.addChar(questnpc);
				break;
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.err.println("Error in " + m_map.getX() + "." + m_map.getY() + ".txt - Invalid NPC, " + "HM Object or WarpTile");
		}
	}
}
