package org.pokenet.client.backend;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import org.ini4j.Ini;
import org.ini4j.InvalidIniFormatException;
import org.newdawn.slick.Input;
import org.pokenet.client.GameClient;

public class KeyManager
{
	private static HashMap<Action, Integer> keys;

	public enum Action
	{
		WALK_UP, WALK_DOWN, WALK_LEFT, WALK_RIGHT, POKEMOVE_1, POKEMOVE_2, POKEMOVE_3, POKEMOVE_4, ROD_OLD, ROD_GOOD, ROD_GREAT, ROD_ULTRA, INTERACTION
	};

	public static void initialize()
	{
		keys = new HashMap<Action, Integer>();
		String respath = System.getProperty("res.path");
		if(respath == null)
			respath = "";
		try
		{
			String path = respath + "res/keys.ini";
			Ini ini = null;
			try
			{
				ini = new Ini(new FileInputStream(path));
			}
			catch(InvalidIniFormatException e)
			{
				GameClient.log("ERROR: Malformed keys.ini, a new file will be generated");
				reset();
				return;
			}
			catch(IOException e)
			{
				GameClient.log("ERROR: Error during the loading of keys.ini, could be missing. A new file will be generated");
				reset();
				return;
			}
			String s;

			//INITIALIZE MOVEMENT KEYS
			Ini.Section sec = ini.get("MOVEMENT");
			s = sec.get("UP");
			if(checkNotNull(Action.WALK_UP, s, true))
				keys.put(Action.WALK_UP, stringToInt(s));
			s = sec.get("DOWN");
			if(checkNotNull(Action.WALK_DOWN, s, true))
				keys.put(Action.WALK_DOWN, stringToInt(s));
			s = sec.get("LEFT");
			if(checkNotNull(Action.WALK_LEFT, s, true))
				keys.put(Action.WALK_LEFT, stringToInt(s));
			s = sec.get("RIGHT");
			if(checkNotNull(Action.WALK_RIGHT, s, true))
				keys.put(Action.WALK_RIGHT, stringToInt(s));
			
			//INITIALIZE ROD KEYS
			sec = ini.get("RODS");
			s = sec.get("OLD");
			if(checkNotNull(Action.ROD_OLD, s, true))
				keys.put(Action.ROD_OLD, stringToInt(s));	
			s = sec.get("GOOD");
			if(checkNotNull(Action.ROD_GOOD, s, true))
				keys.put(Action.ROD_GOOD, stringToInt(s));
			s = sec.get("GREAT");
			if(checkNotNull(Action.ROD_GREAT, s, true))
				keys.put(Action.ROD_GREAT, stringToInt(s));
			s = sec.get("ULTRA");
			if(checkNotNull(Action.ROD_ULTRA, s, true))
				keys.put(Action.ROD_ULTRA, stringToInt(s));

			//INITIALIZE BATTLE KEYS
			sec = ini.get("BATTLEMOVES");
			s = sec.get("ATTACK1");
			if(checkNotNull(Action.POKEMOVE_1, s, true))
				keys.put(Action.POKEMOVE_1, stringToInt(s));		
			s = sec.get("ATTACK2");
			if(checkNotNull(Action.POKEMOVE_2, s, true))
				keys.put(Action.POKEMOVE_2, stringToInt(s));	
			s = sec.get("ATTACK3");
			if(checkNotNull(Action.POKEMOVE_3, s, true))
				keys.put(Action.POKEMOVE_3, stringToInt(s));	
			s = sec.get("ATTACK4");
			if(checkNotNull(Action.POKEMOVE_4, s, true))
				keys.put(Action.POKEMOVE_4, stringToInt(s));	
					
			//INITIALIZE INTERACTION KEYS
			sec = ini.get("INTERACTION");
			s = sec.get("TALK");
			if(checkNotNull(Action.INTERACTION, s, true))
				keys.put(Action.INTERACTION, stringToInt(s));	
		}
		finally
		{
			GameClient.log("INFO: Keys Loaded");
		}
	}
	
	private static void reset()
	{
		try
		{
			generateDefaultSettings();
		}
		catch(IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		initialize();
	}
	
	private static boolean checkNotNull(Action action, String key, boolean resetOnNull)
	{
		if(stringToInt(key) == null)
		{
			try
			{
				if(resetOnNull)
				{
					reset();
				}
				else
				{
					generateDefaultSettings();
				}
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			return false;
		}
		else
		{
			return true;
		}
	}
	
	private static void generateDefaultSettings() throws IOException
	{
		// Create file 
		  FileWriter fstream = new FileWriter("res/keys.ini");
		  BufferedWriter out = new BufferedWriter(fstream);
		  out.write(";see specialkeys.txt for formats of keys like left shift, right control, etc");
		  out.newLine();
		  out.write("[MOVEMENT]");
		  out.newLine();
		  out.write("UP=W");
		  out.newLine();
		  out.write("LEFT=A");
		  out.newLine();
		  out.write("RIGHT=D");
		  out.newLine();
		  out.write("DOWN=S");
		  out.newLine();
		  out.write("[RODS]");
		  out.newLine();
		  out.write("OLD=R");
		  out.newLine();
		  out.write("GOOD=T");
		  out.newLine();
		  out.write("GREAT=Y");
		  out.newLine();
		  out.write("ULTRA=U");
		  out.newLine();
		  out.write("[BATTLEMOVES]");
		  out.newLine();
		  out.write("ATTACK1=1");
		  out.newLine();
		  out.write("ATTACK2=2");
		  out.newLine();
		  out.write("ATTACK3=3");
		  out.newLine();
		  out.write("ATTACK4=4");
		  out.newLine();
		  out.write("[INTERACTION]");
		  out.newLine();
		  out.write("TALK=SPACE");
		  out.newLine();
		  out.close();
	}

	/**
	 * Returns the int from the Input class associated with this String.
	 * @param st The string to convert.
	 * @return The int from the Input class associated with this String.
	 */
	private static Integer stringToInt(String st)
	{
		if(st.equals("KEY_UP"))
			return Input.KEY_UP;
		else if(st.equals("KEY_LEFT"))
			return Input.KEY_LEFT;
		else if(st.equals("KEY_RIGHT"))
			return Input.KEY_RIGHT;
		else if(st.equals("KEY_DOWN"))
			return Input.KEY_DOWN;
		else if(st.equals("A"))
			return Input.KEY_A;
		else if(st.equals("B"))
			return Input.KEY_B;
		else if(st.equals("C"))
			return Input.KEY_C;
		else if(st.equals("D"))
			return Input.KEY_D;
		else if(st.equals("E"))
			return Input.KEY_E;
		else if(st.equals("F"))
			return Input.KEY_F;
		else if(st.equals("G"))
			return Input.KEY_G;
		else if(st.equals("H"))
			return Input.KEY_H;
		else if(st.equals("I"))
			return Input.KEY_I;
		else if(st.equals("J"))
			return Input.KEY_J;
		else if(st.equals("K"))
			return Input.KEY_K;
		else if(st.equals("L"))
			return Input.KEY_L;
		else if(st.equals("M"))
			return Input.KEY_M;
		else if(st.equals("N"))
			return Input.KEY_N;
		else if(st.equals("O"))
			return Input.KEY_O;
		else if(st.equals("P"))
			return Input.KEY_P;
		else if(st.equals("Q"))
			return Input.KEY_Q;
		else if(st.equals("R"))
			return Input.KEY_R;
		else if(st.equals("S"))
			return Input.KEY_S;
		else if(st.equals("T"))
			return Input.KEY_T;
		else if(st.equals("U"))
			return Input.KEY_U;
		else if(st.equals("V"))
			return Input.KEY_V;
		else if(st.equals("W"))
			return Input.KEY_W;
		else if(st.equals("X"))
			return Input.KEY_X;
		else if(st.equals("Y"))
			return Input.KEY_Y;
		else if(st.equals("Z"))
			return Input.KEY_Z;
		else if(st.equals("LSHIFT"))
			return Input.KEY_LSHIFT;
		else if(st.equals("LALT"))
			return Input.KEY_LALT;
		else if(st.equals("RSHIFT"))
			return Input.KEY_RSHIFT;
		else if(st.equals("RALT"))
			return Input.KEY_RALT;
		else if(st.equals("RCTRL"))
			return Input.KEY_RCONTROL;
		else if(st.equals("LCTRL"))
			return Input.KEY_LCONTROL;
		else if(st.equals("ENTER"))
			return Input.KEY_ENTER;
		else if(st.equals("NUMPADENTER"))
			return Input.KEY_NUMPADENTER;
		else if(st.equals("HOME"))
			return Input.KEY_HOME;
		else if(st.equals("END"))
			return Input.KEY_END;
		else if(st.equals("0"))
			return Input.KEY_0;
		else if(st.equals("1"))
			return Input.KEY_1;
		else if(st.equals("2"))
			return Input.KEY_2;
		else if(st.equals("3"))
			return Input.KEY_3;
		else if(st.equals("4"))
			return Input.KEY_4;
		else if(st.equals("5"))
			return Input.KEY_5;
		else if(st.equals("6"))
			return Input.KEY_6;
		else if(st.equals("7"))
			return Input.KEY_7;
		else if(st.equals("8"))
			return Input.KEY_7;
		else if(st.equals("9"))
			return Input.KEY_7;
		else if(st.equals("0"))
			return Input.KEY_7;
		else if(st.equals("-"))
			return Input.KEY_MINUS;
		else if(st.equals("+"))
			return Input.KEY_ADD;
		else if(st.equals("BACKSPACE"))
			return Input.KEY_BACK;
		else if(st.equals("TAB"))
			return Input.KEY_TAB;
		else if(st.equals(';'))
			return Input.KEY_SEMICOLON;
		else if(st.equals('.'))
			return Input.KEY_PERIOD;
		else if(st.equals(','))
			return Input.KEY_COMMA;
		else if(st.equals('='))
			return Input.KEY_EQUALS;
		else if(st.equals('['))
			return Input.KEY_LBRACKET;
		else if(st.equals(']'))
			return Input.KEY_RBRACKET;
		else if(st.equals('/'))
			return Input.KEY_SLASH;
		else if(st.equals("DEL"))
			return Input.KEY_DELETE;
		else if(st.equals("SPACE"))
			return Input.KEY_SPACE;
		return null;
	}

	/**
	 * Returns the key associated with this action
	 * @param a The action
	 * @return The key that is associated with the action
	 */
	public static int getKey(Action a)
	{
		return keys.get(a);
	}
}
