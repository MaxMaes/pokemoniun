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
	private HashMap<Integer, Action> keys;

	public enum Action
	{
		WALK_UP, WALK_DOWN, WALK_LEFT, WALK_RIGHT, POKEMOVE_1, POKEMOVE_2, POKEMOVE_3, POKEMOVE_4, ROD_OLD, ROD_GOOD, ROD_GREAT, ROD_ULTRA, INTERACTION
	};

	public KeyManager()
	{
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
				if(s.equalsIgnoreCase("[MOVEMENT]"))
				{
					s = reader.nextLine();
				}
				else if(s.equalsIgnoreCase("[RODS]"))
				{
					s = reader.nextLine();
				}
				else if(s.equalsIgnoreCase("[MOVES]"))
				{
					s = reader.nextLine();
				}
				else if(s.equalsIgnoreCase("[INTERACTION]"))
				{
					s = reader.nextLine();
				}
				
				data = s.split("=");
				keydata = data[1].split(",");
				if(data[0].equals("UP"))
				{
					for(String st : keydata)
					{
						keys.put(stringToInt(st), Action.WALK_UP);
					}
				}
				else if(data[0].equals("LEFT"))
				{

				}
				else if(data[0].equals("RIGHT"))
				{

				}
				else if(data[0].equals("DOWN"))
				{

				}
			}
		}
		catch(FileNotFoundException fnfe)
		{
			System.out.println("INFO: Keys.ini not found! Setting default settings");
		}
		finally
		{
			// TODO: Default implementation of keys
		}
	}

	private Integer stringToInt(String st)
	{
		if(st.equals("UP"))
		{
			return Input.KEY_UP;
		}
		else if(st.equals("LEFT"))
		{
			return Input.KEY_LEFT;
		}
		else if(st.equals("RIGHT"))
		{
			return Input.KEY_RIGHT;
		}
		else if(st.equals("DOWN"))
		{
			return Input.KEY_DOWN;
		}
		else if(st.equals("A"))
		{
			return Input.KEY_A;
		}
		else if(st.equals("B"))
		{
			return Input.KEY_B;
		}
		else if(st.equals("C"))
		{
			return Input.KEY_C;
		}
		else if(st.equals("D"))
		{
			return Input.KEY_D;
		}
		else if(st.equals("E"))
		{
			return Input.KEY_E;
		}
		else if(st.equals("F"))
		{
			return Input.KEY_F;
		}
		else if(st.equals("G"))
		{
			return Input.KEY_G;
		}
		else if(st.equals("H"))
		{
			return Input.KEY_H;
		}
		else if(st.equals("I"))
		{
			return Input.KEY_I;
		}
		else if(st.equals("J"))
		{
			return Input.KEY_J;
		}
		else if(st.equals("K"))
		{
			return Input.KEY_K;
		}
		else if(st.equals("L"))
		{
			return Input.KEY_L;
		}
		else if(st.equals("M"))
		{
			return Input.KEY_M;
		}
		else if(st.equals("N"))
		{
			return Input.KEY_N;
		}
		else if(st.equals("O"))
		{
			return Input.KEY_O;
		}
		else if(st.equals("P"))
		{
			return Input.KEY_P;
		}
		else if(st.equals("Q"))
		{
			return Input.KEY_Q;
		}
		else if(st.equals("R"))
		{
			return Input.KEY_R;
		}
		else if(st.equals("S"))
		{
			return Input.KEY_S;
		}
		else if(st.equals("T"))
		{
			return Input.KEY_T;
		}
		else if(st.equals("U"))
		{
			return Input.KEY_U;
		}
		else if(st.equals("V"))
		{
			return Input.KEY_V;
		}
		else if(st.equals("W"))
		{
			return Input.KEY_W;
		}
		else if(st.equals("X"))
		{
			return Input.KEY_X;
		}
		else if(st.equals("Y"))
		{
			return Input.KEY_Y;
		}
		else if(st.equals("Z"))
		{
			return Input.KEY_Z;
		}
		else if(st.equals("LSHIFT"))
		{
			return Input.KEY_LSHIFT;
		}
		else if(st.equals("LALT"))
		{
			return Input.KEY_LALT;
		}
		else if(st.equals("RSHIFT"))
		{
			return Input.KEY_RSHIFT;
		}
		else if(st.equals("RALT"))
		{
			return Input.KEY_RALT;
		}
		else if(st.equals("RCTRL"))
		{
			return Input.KEY_RCONTROL;
		}
		else if(st.equals("LCTRL"))
		{
			return Input.KEY_LCONTROL;
		}

		return null;
	}

	public Action getAction()
	{
		// TODO: Implement this...
		return null;
	}
}
