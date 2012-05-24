package org.pokenet.client.backend;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Scanner;
import org.newdawn.slick.Input;

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
			InputStream in = new FileInputStream(path);
			BufferedReader f = new BufferedReader(new InputStreamReader(in));
			Scanner reader = new Scanner(f);
			String s;
			String[] data;
			String[] keydata;
			while(reader.hasNextLine())
			{
				s = reader.nextLine();
				if(s.startsWith("//"))
				{
					s = reader.nextLine();
				}
				if(s.equalsIgnoreCase("[MOVEMENT]"))
				{
					s = reader.nextLine();
				}
				if(s.equalsIgnoreCase("[RODS]"))
				{
					s = reader.nextLine();
				}
				if(s.equalsIgnoreCase("[MOVES]"))
				{
					s = reader.nextLine();
				}
				if(s.equalsIgnoreCase("[INTERACTION]"))
				{
					s = reader.nextLine();
				}
				if(s == "")
				{
					s = reader.nextLine();
				}
				
				data = s.split("=");
				keydata = data[1].split(",");
				
				if(data[0].equals("UP"))
					for(String st : keydata)
						keys.put(Action.WALK_UP, stringToInt(st));
				
				else if(data[0].equals("LEFT"))
					for(String st : keydata)
						keys.put(Action.WALK_LEFT, stringToInt(st));
				
				else if(data[0].equals("RIGHT"))
					for(String st : keydata)
						keys.put(Action.WALK_RIGHT, stringToInt(st));
				
				else if(data[0].equals("DOWN"))
					for(String st : keydata)
						keys.put(Action.WALK_DOWN, stringToInt(st));
				
				else if(data[0].equals("OLD"))
					for(String st : keydata)
						keys.put(Action.ROD_OLD, stringToInt(st));
				
				else if(data[0].equals("GOOD"))
					for(String st : keydata)
						keys.put(Action.ROD_GOOD, stringToInt(st));
				
				else if(data[0].equals("GREAT"))
					for(String st : keydata)
						keys.put(Action.ROD_GREAT, stringToInt(st));
				
				else if(data[0].equals("ULTRA"))
					for(String st : keydata)
						keys.put(Action.ROD_ULTRA, stringToInt(st));
				
				else if(data[0].equals("ATTACK1"))
					for(String st : keydata)
						keys.put(Action.POKEMOVE_1, stringToInt(st));
				
				else if(data[0].equals("ATTACK2"))
					for(String st : keydata)
						keys.put(Action.POKEMOVE_2, stringToInt(st));
				
				else if(data[0].equals("ATTACK3"))
					for(String st : keydata)
						keys.put(Action.POKEMOVE_3, stringToInt(st));
				
				else if(data[0].equals("ATTACK4"))
					for(String st : keydata)
						keys.put(Action.POKEMOVE_4, stringToInt(st));
				else if(data[0].equals("TALK"))
					for(String st : keydata)
						keys.put(Action.INTERACTION, stringToInt(st));
			}
		}
		catch(FileNotFoundException fnfe)
		{
			System.out.println("INFO: Keys.ini not found! Setting default settings");
		}
		finally
		{
			System.out.println("INFO: Keys Loaded");
		}
	}

	/**
	 * Returns the int from the Input class associated with this String.
	 * @param st The string to convert.
	 * @return The int from the Input class associated with this String.
	 */
	private static Integer stringToInt(String st)
	{
		if(st.equals("UP"))
			return Input.KEY_UP;
		else if(st.equals("LEFT"))
			return Input.KEY_LEFT;
		else if(st.equals("RIGHT"))
			return Input.KEY_RIGHT;
		else if(st.equals("DOWN"))
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
