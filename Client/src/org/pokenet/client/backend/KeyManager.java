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

/**
 * Contains and manages the controls
 * 
 * @author Myth1c
 */
public class KeyManager
{
	public enum Action
	{
		INTERACTION, POKEMOVE_1, POKEMOVE_2, POKEMOVE_3, POKEMOVE_4, ROD_GOOD, ROD_GREAT, ROD_OLD, ROD_ULTRA, WALK_DOWN, WALK_LEFT, WALK_RIGHT, WALK_UP
	}

	private static HashMap<Action, Integer> keys;;

	/**
	 * Returns the key associated with this action
	 * 
	 * @param a The action
	 * @return The key that is associated with the action
	 */
	public static int getKey(Action a)
	{
		return keys.get(a);
	}

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
				GameClient.getInstance().log("ERROR: Malformed keys.ini, a new file will be generated");
				reset();
				return;
			}
			catch(IOException e)
			{
				GameClient.getInstance().log("ERROR: Error during the loading of keys.ini, could be missing. A new file will be generated");
				reset();
				return;
			}
			String s;

			// INITIALIZE MOVEMENT KEYS
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

			// INITIALIZE ROD KEYS
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

			// INITIALIZE BATTLE KEYS
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

			// INITIALIZE INTERACTION KEYS
			sec = ini.get("INTERACTION");
			s = sec.get("TALK");
			if(checkNotNull(Action.INTERACTION, s, true))
				keys.put(Action.INTERACTION, stringToInt(s));
		}
		finally
		{
			GameClient.getInstance().log("INFO: Keys Loaded");
		}
	}

	private static boolean checkNotNull(Action action, String key, boolean resetOnNull)
	{
		if(stringToInt(key) == null)
		{
			try
			{
				if(resetOnNull)
					reset();
				else
					generateDefaultSettings();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			return false;
		}
		else
			return true;
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

	private static void reset()
	{
		try
		{
			generateDefaultSettings();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		initialize();
	}

	/**
	 * Returns the int from the Input class associated with this String.
	 * 
	 * @param st The string to convert.
	 * @return The int from the Input class associated with this String.
	 */
	private static Integer stringToInt(String st)
	{
		switch(st)
		{
			case "KEY_UP": return Input.KEY_UP;
			case "KEY_LEFT": return Input.KEY_LEFT;
			case "KEY_RIGHT": return Input.KEY_RIGHT;
			case "KEY_DOWN": return Input.KEY_UP;
			case "A": return Input.KEY_A;
			case "B": return Input.KEY_B;
			case "C": return Input.KEY_C;
			case "D": return Input.KEY_D;
			case "E": return Input.KEY_E;
			case "F": return Input.KEY_F;
			case "G": return Input.KEY_G;
			case "H": return Input.KEY_H;
			case "I": return Input.KEY_I;
			case "J": return Input.KEY_J;
			case "K": return Input.KEY_K;
			case "L": return Input.KEY_L;
			case "M": return Input.KEY_M;
			case "N": return Input.KEY_N;
			case "O": return Input.KEY_O;
			case "P": return Input.KEY_P;
			case "Q": return Input.KEY_Q;
			case "R": return Input.KEY_R;
			case "S": return Input.KEY_S;
			case "T": return Input.KEY_T;
			case "U": return Input.KEY_U;
			case "V": return Input.KEY_V;
			case "W": return Input.KEY_W;
			case "X": return Input.KEY_X;
			case "Y": return Input.KEY_Y;
			case "Z": return Input.KEY_Z;
			case "LSHIFT": return Input.KEY_LSHIFT;
			case "LALT": return Input.KEY_LALT;
			case "RSHIFT": return Input.KEY_RSHIFT;
			case "RALT": return Input.KEY_RALT;
			case "RCTRL": return Input.KEY_RCONTROL;
			case "LCTRL": return Input.KEY_LCONTROL;
			case "ENTER": return Input.KEY_ENTER;
			case "NUMPADENTER": return Input.KEY_NUMPADENTER;
			case "HOME": return Input.KEY_HOME;
			case "END": return Input.KEY_END;
			case "0": return Input.KEY_0;
			case "1": return Input.KEY_1;
			case "2": return Input.KEY_2;
			case "3": return Input.KEY_3;
			case "4": return Input.KEY_4;
			case "5": return Input.KEY_5;
			case "6": return Input.KEY_6;
			case "7": return Input.KEY_7;
			case "8": return Input.KEY_8;
			case "9": return Input.KEY_9;
			case "-": return Input.KEY_MINUS;
			case "+": return Input.KEY_ADD;
			case "BACKSPACE": return Input.KEY_BACK;
			case "TAB": return Input.KEY_TAB;
			case ";": return Input.KEY_SEMICOLON;
			case ".": return Input.KEY_PERIOD;
			case ",": return Input.KEY_COMMA;
			case "=": return Input.KEY_EQUALS;
			case "[": return Input.KEY_LBRACKET;
			case "]": return Input.KEY_RBRACKET;
			case "/": return Input.KEY_SLASH;
			case "DEL": return Input.KEY_DELETE;
			case "SPACE": return Input.KEY_SPACE;
			default: return null;
		}
	}
}
