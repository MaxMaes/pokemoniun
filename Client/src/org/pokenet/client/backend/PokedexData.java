package org.pokenet.client.backend;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import org.ini4j.Ini;
import org.ini4j.Ini.Section;

/**
 * Contains all information required by the pokedex
 * @author Myth1c
 *
 */
public class PokedexData
{
	private static HashMap<Integer, String[]> dexdata;
	private static HashMap<Integer, ArrayList<Integer>[]> locations;
	private static HashMap<Integer, Object[]> locationids;
	
	public static void loadPokedexData()
	{
		dexdata = new HashMap<Integer, String[]>();
		locations = new HashMap<Integer, ArrayList<Integer>[]>();
		locationids = new HashMap<Integer, Object[]>();
		String respath = System.getProperty("res.path");
		if(respath == null)
			respath = "";
		Ini pokemon = null;
		Ini locations = null;
		Ini locationid = null;
		try
		{
			pokemon = new Ini(new FileInputStream(respath + "res/pokemon.ini"));
			locations = new Ini(new FileInputStream(respath + "res/locations.ini"));
			locationid = new Ini(new FileInputStream(respath + "res/locationids.ini"));
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return;
		}
		
		for(int i = 0; i < 92; i++)
		{
			Ini.Section s = locationid.get(String.valueOf(i));
			Object[] data = new Object[3];
			data[0] = s.get("Name");
			data[1] = s.get("MapX");
			data[2] = s.get("MapY");
			
			locationids.put(i, data);
		}
		
		for(int i = 1; i < 494; i++)
		{
			Ini.Section s = pokemon.get(String.valueOf(i));
			parsePokemonInfo(s, i);
			s = locations.get(String.valueOf(i));
			parseLocationInfo(s, i);
		}
	}

	@SuppressWarnings("unchecked")
	private static void parseLocationInfo(Section s, int i)
	{
		ArrayList<Integer>[] data = new ArrayList[4];
		data[0] = new ArrayList<Integer>();
		data[1] = new ArrayList<Integer>();
		data[2] = new ArrayList<Integer>();
		data[3] = new ArrayList<Integer>();
		locations.put(i, data);
		
		String line = new String();
		String[] details;
		line = s.get("Day");
		details = line.split(",");
		for(String st : details)
		{
			if(!st.equals(""))
				locations.get(i)[0].add(Integer.parseInt(st));
		}
		
		line = s.get("Night");
		details = line.split(",");
		for(String st : details)
		{
			if(!st.equals(""))
				locations.get(i)[1].add(Integer.parseInt(st));
		}
		
		line = s.get("Fish");
		details = line.split(",");
		for(String st : details)
		{
			if(!st.equals(""))
				locations.get(i)[2].add(Integer.parseInt(st));
		}
		
		line = s.get("Surf");
		details = line.split(",");
		for(String st : details)
		{
			if(!st.equals(""))
				locations.get(i)[3].add(Integer.parseInt(st));
		}
	}

	private static void parsePokemonInfo(Section s, int index)
	{
		String[] data = new String[26];
		data[0] = s.get("Name");
		data[1] = s.get("InternalName");
		data[2] = s.get("Kind");
		data[3] = s.get("Pokedex");
		data[4] = s.get("Type1");
		data[5] = s.get("Type2");
		data[6] = s.get("BaseStats");
		data[7] = s.get("Rareness");
		data[8] = s.get("BaseEXP");
		data[9] = s.get("Happiness");
		data[10] = s.get("GrowthRate");
		data[11] = s.get("StepsToHatch");
		data[12] = s.get("Color");
		data[13] = s.get("Habitat");
		data[14] = s.get("EffortPoints");
		data[15] = s.get("Abilities");
		data[16] = s.get("Compatibility");
		data[17] = s.get("Height");
		data[18] = s.get("Weight");
		data[19] = s.get("GenderRate");
		data[20] = s.get("Moves");
		data[21] = s.get("EggMoves");
		data[22] = s.get("Evolutions");
		data[23] = s.get("BattlerPlayerY");
		data[24] = s.get("BattlerEnemyY");
		data[25] = s.get("BattlerAltitude");

		dexdata.put(index, data);
	}
	
	public static Object[] getLocationInfo(int id)
	{
		return locationids.get(id);
	}
	
	public static ArrayList<Integer>[] getLocations(int id)
	{
		return locations.get(id);
	}
	
	public static String getName(int id)
	{
		if(dexdata.get(id) == null)
		{
			return "";
		}
		return dexdata.get(id)[0];
	}
	
	public static String getInternalName(int id)
	{
		return dexdata.get(id)[1];
	}
	
	public static String getKind(int id)
	{
		return dexdata.get(id)[2];
	}
	
	public static String getStory(int id)
	{
		return dexdata.get(id)[3];
	}
	
	public static String getType1(int id)
	{
		return dexdata.get(id)[4];
	}
	
	public static String getType2(int id)
	{
		return dexdata.get(id)[5];
	}
	
	public static String getTypestring(int id)
	{
		if(dexdata.get(id)[5] == null)
			return dexdata.get(id)[4];
		else
			return dexdata.get(id)[4] + "/" + dexdata.get(id)[5];
	}
	
	public static String getBaseStats(int id)
	{
		return dexdata.get(id)[6];
	}
	
	public static String getRareness(int id)
	{
		return dexdata.get(id)[7];
	}
	
	public static String getBaseEXP(int id)
	{
		return dexdata.get(id)[8];
	}
	
	public static String getHappiness(int id)
	{
		return dexdata.get(id)[9];
	}
	
	public static String getGrowthRate(int id)
	{
		return dexdata.get(id)[10];
	}
	
	public static String getStepsToHatch(int id)
	{
		return dexdata.get(id)[11];
	}
	
	public static String getColor(int id)
	{
		return dexdata.get(id)[12];
	}
	
	public static String getHabitat(int id)
	{
		return dexdata.get(id)[13];
	}
	
	public static String getEffortPoints(int id)
	{
		return dexdata.get(id)[14];
	}
	
	public static String getAbilities(int id)
	{
		return dexdata.get(id)[15];
	}
	
	public static String getCompatibility(int id)
	{
		return dexdata.get(id)[16];
	}
	
	public static String getHeight(int id)
	{
		return dexdata.get(id)[17];
	}
	
	public static String getWeight(int id)
	{
		return dexdata.get(id)[18];
	}
	
	public static String getGenderRate(int id)
	{
		return dexdata.get(id)[19];
	}
	
	public static String getMoves(int id)
	{
		return dexdata.get(id)[20];
	}
	
	public static String getEggMoves(int id)
	{
		return dexdata.get(id)[21];
	}
	
	public static String getEvolutions(int id)
	{
		return dexdata.get(id)[22];
	}
	
	public static String getBattlerPlayerY(int id)
	{
		return dexdata.get(id)[23];
	}
	
	public static String getBattlerEnemyY(int id)
	{
		return dexdata.get(id)[24];
	}
	
	public static String getBattlerAltitude(int id)
	{
		return dexdata.get(id)[25];
	}
}
