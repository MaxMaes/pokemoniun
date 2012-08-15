package org.pokenet.client.ui.frames;

import java.io.InputStream;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.loading.LoadingList;
import org.pokenet.client.GameClient;
import org.pokenet.client.backend.FileLoader;
import org.pokenet.client.backend.PokedexData;
import org.pokenet.client.ui.base.ImageButton;
import org.pokenet.client.ui.base.NewImageButton;
import mdes.slick.sui.Frame;
import mdes.slick.sui.Label;
import mdes.slick.sui.event.ActionEvent;
import mdes.slick.sui.event.ActionListener;
/**
 * Pokedex dialog
 * @author Myth1c
 *
 */
public class PokedexDialog extends Frame
{
	private NewImageButton up, down, left, right;
	private NewImageButton inc1, inc5, inc10, inc50;
	private Label pokedexsprite;
	private Label pokemonsprite;
	private Label pokemonname;
	private Label pokemontypes;
	private Label pokemonnumber;
	private int selection = 1;
	private int scrollindex = 1;
	private int[] trainerPokedex;
	private int incrementer = 1;
	private NewImageButton currIncButton;
	
	
	public PokedexDialog()
	{
		initGUI();
	}

	public void updatePokemonInfo()
	{
		pokemonname.setText(PokedexData.getName(selection));
		pokemonname.pack();
		pokemonname.setLocation(pokedexsprite.getX()+178 - (pokemonname.getWidth()/2), pokedexsprite.getY()+75);
		
		pokemontypes.setText(PokedexData.getTypestring(selection));
		pokemontypes.pack();
		pokemontypes.setLocation(pokedexsprite.getX()+178 - (pokemontypes.getWidth()/2), pokemonname.getY() + 17);
		
		String number = "#";
		if(selection < 10)
			number = number + "00" + selection;
		else if(selection < 100)
			number = number + "0" + selection;
		else
			number = "#" + selection;
		
		pokemonnumber.setText(number);
		pokemonnumber.pack();
		pokemonnumber.setLocation(pokedexsprite.getX()+178 - (pokemonnumber.getWidth()/2), pokemonname.getY() - 25);
		
		LoadingList.setDeferredLoading(true);
        pokemonsprite.setImage(getSprite(selection, 3));
        LoadingList.setDeferredLoading(false);
		pokemonsprite.setSize(80, 80);
		pokemonsprite.setLocation(pokedexsprite.getX()+33, pokedexsprite.getY()+39);
	}
	
	
	/**
	 * Gets the pokemons sprite
	 * @param pokenumber pokemons pokedex number
	 * @param male 2=male, 3=female
	 * @return
	 */
	private Image getSprite(int pokenumber, int male) 
    {
    	String respath = System.getProperty("res.path");
		if(respath==null)
			respath="";
		LoadingList.setDeferredLoading(true);
		Image i = null;
    	try
    	{
    		InputStream f;
    		String path = new String();
    		String index = new String();


    		if (pokenumber < 10) 
    		{
    			index = "00" + String.valueOf(pokenumber);
    		} 
    		else if (pokenumber < 100) 
    		{
    			index = "0" + String.valueOf(pokenumber);
    		} 
    		else 
    		{
    			index = String.valueOf(pokenumber);
    		}
    		
    		int pathGender;
    		if (male != 2)
    			pathGender = 3;
    		else
    			pathGender = 2;

    		try 
    		{
    			path = respath+"res/pokemon/front/normal/" +  index + "-"
    				+ pathGender + ".png";
    			f = FileLoader.loadFile(path);
    			i = new Image(f, path.toString(), false);
    		} 
    		catch (Exception e) 
    		{
    			if(pathGender == 3)
    				pathGender = 2;
    			else
    				pathGender = 3;
    			path = respath+"res/pokemon/front/normal/" + index + "-"
    				+ pathGender + ".png";
    			i = new Image(path.toString(), false);
    			e.printStackTrace();
    		}
    		LoadingList.setDeferredLoading(false);
    	}
    	catch (SlickException e)
    	{e.printStackTrace();}
    	return i;
    }
	
	public Image loadImage(String path) throws SlickException
	{
		LoadingList.setDeferredLoading(true);
		String respath = System.getProperty("res.path");
		if(respath == null)
			respath = "";
		Image i = new Image(FileLoader.loadFile(respath + path), path, false);
		LoadingList.setDeferredLoading(false);
		return i;
	}
	
	public void initGUI()
	 {
		// This one has to be initialized fist, since it's the 'background'
		initPokedexSprite(); 
		 
		pokemonname = new Label();
		pokemonname.setFont(GameClient.getPokedexFontMedium());
		
		pokemontypes = new Label();
		pokemontypes.setFont(GameClient.getPokedexFontSmall());
		
		pokemonnumber = new Label();
		pokemonnumber.setFont(GameClient.getPokedexFontLarge());
		
		pokemonsprite = new Label();
		pokemonsprite.setSize(80, 80);
		pokemonsprite.setLocation(pokedexsprite.getX()+33, pokedexsprite.getY()+39);
		
		updatePokemonInfo();
		
		setBackground(new Color(0,0,0,0));
		getTitleBar().setVisible(false);
		setSize(519, 397 + getTitleBar().getHeight());
		
		add(pokemonname);
		add(pokemontypes);
		add(pokemonnumber);
		add(pokemonsprite);
		
		setResizable(false);
	 }
	
	private void initPokedexSprite()
	{
		try
		{
			pokedexsprite = new Label();
			pokedexsprite.setImage(loadImage("res/ui/pokedex/pokedex.png"));
			pokedexsprite.setSize(519,377);
			pokedexsprite.setLocation(0,20);
			
			up = new NewImageButton();
			down = new NewImageButton();
			left = new NewImageButton();
			right = new NewImageButton();
			
			inc1 = new NewImageButton();
			inc5 = new NewImageButton();
			inc10 = new NewImageButton();
			inc50 = new NewImageButton();
			
			Image downImage = loadImage("res/ui/pokedex/button_down.png");
			down.setSize(30, 30);
			down.setImages(downImage, downImage, downImage);
			down.setForeground(Color.white);
			down.setLocation(pokedexsprite.getX() + pokedexsprite.getWidth() - downImage.getWidth() - 8, pokedexsprite.getY() + pokedexsprite.getHeight() - downImage.getHeight() - 47);
			down.setActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					if(arg0.getActionCommand().equals("Up"))
					{
						if((selection - incrementer) >= 1)
						{
							selection-=incrementer;
							updatePokemonInfo();
						}
					}
				}
			});
			
			Image upImage = loadImage("res/ui/pokedex/button_up.png");
			up.setImages(upImage, upImage, upImage);
			up.setSize(30, 30);
			up.setForeground(Color.white);
			up.setLocation(down.getX(), down.getY() - down.getHeight() - 5);
			up.setActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					if(arg0.getActionCommand().equals("Up"))
					{
						if((selection + incrementer) <= 493)
						{
							selection+=incrementer;
							updatePokemonInfo();
						}
					}
				}
			});
			
			Image leftImage = loadImage("res/ui/pokedex/button_left.png");
			left.setImages(leftImage, leftImage, leftImage);
			left.setSize(20, 38);
			left.setLocation(pokedexsprite.getX() + 32, pokedexsprite.getY() + pokedexsprite.getHeight() - leftImage.getHeight() - 76);
			left.setForeground(Color.white);
			left.setActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					System.out.println(arg0.getActionCommand());
				}
			});
			
			Image rightImage = loadImage("res/ui/pokedex/button_right.png");
			right.setImages(rightImage, rightImage, rightImage);
			right.setSize(20, 38);
			right.setLocation(left.getX() + 177, left.getY());
			right.setForeground(Color.white);
			right.setActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					System.out.println(arg0.getActionCommand());
				}
			});
			
			Image inc1i = loadImage("res/ui/pokedex/incrementer.png");
			Image inc1ipressed = loadImage("res/ui/pokedex/incrementer_pressed.png");
			inc1.setImages(inc1ipressed,inc1i, inc1ipressed);
			inc1.setFont(GameClient.getPokedexFontMini());
			inc1.setText("1");
			inc1.setSize(36,14);
			inc1.setLocation(down.getX()-3, pokedexsprite.getY() + 70);
			inc1.setForeground(Color.white);
			currIncButton = inc1;
			currIncButton.disable();
			inc1.setActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					if(inc1.enabled())
					{
						currIncButton.enable();
						currIncButton = inc1;
						inc1.disable();
						incrementer = 1;
					}
				}
			});
			
			Image inc5i = loadImage("res/ui/pokedex/incrementer.png");
			Image inc5ipressed = loadImage("res/ui/pokedex/incrementer_pressed.png");
			inc5.setImages(inc5ipressed,inc5i, inc5ipressed);
			inc5.setFont(GameClient.getPokedexFontMini());
			inc5.setText("5");
			inc5.setSize(36,14);
			inc5.setLocation(inc1.getX(), inc1.getY() + inc1.getHeight() + 5);
			inc5.setForeground(Color.white);
			inc5.setActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					if(inc5.enabled())
					{
						currIncButton.enable();
						currIncButton = inc5;
						inc5.disable();
						incrementer = 5;
					}
				}
			});
			
			Image inc10i = loadImage("res/ui/pokedex/incrementer.png");
			Image inc10ipressed = loadImage("res/ui/pokedex/incrementer_pressed.png");
			inc10.setImages(inc10ipressed,inc10i, inc10ipressed);
			inc10.setFont(GameClient.getPokedexFontMini());
			inc10.setText("10");
			inc10.setSize(36,14);
			inc10.setLocation(inc5.getX(), inc5.getY() + inc5.getHeight() + 5);
			inc10.setForeground(Color.white);
			inc10.setActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					if(inc10.enabled())
					{
						currIncButton.enable();
						currIncButton = inc10;
						inc10.disable();
						incrementer = 10;
					}
				}
			});
			
			Image inc50i = loadImage("res/ui/pokedex/incrementer.png");
			Image inc50ipressed = loadImage("res/ui/pokedex/incrementer_pressed.png");
			inc50.setImages(inc50ipressed,inc50i, inc50ipressed);
			inc50.setFont(GameClient.getPokedexFontMini());
			inc50.setText("50");
			inc50.setSize(36,14);
			inc50.setLocation(inc10.getX(), inc10.getY() + inc10.getHeight() + 5);
			inc50.setForeground(Color.white);
			inc50.setActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					if(inc50.enabled())
					{
						currIncButton.enable();
						currIncButton = inc50;
						inc50.disable();
						incrementer = 50;
					}
				}
			});
		}
		catch(SlickException e)
		{
			e.printStackTrace();
		}
		add(pokedexsprite);
		add(down);
		add(up);
		add(left);
		add(right);
		add(inc1);
		add(inc5);
		add(inc10);
		add(inc50);
	}

		
	/**
	* Sets fram (in)visible and sets the pokedex data.
	*/
	@Override
	public void setVisible(boolean toSet)
	{
		if(toSet)
		{
			initGUI();
			this.trainerPokedex = GameClient.getInstance().getOurPlayer().getPokedex();
		}
		super.setVisible(toSet);
	}
}
