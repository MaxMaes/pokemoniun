package org.pokenet.client.ui.frames;

import java.util.List;
import org.pokenet.client.GameClient;
import org.pokenet.client.backend.Translator;
import org.pokenet.client.backend.entity.OurPokemon;
import org.pokenet.client.ui.components.Image;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.ResizableFrame;

public class PokemonInfoDialog extends ResizableFrame
{
	private Label data[] = new Label[24];
	private Image icon;
	private Label labels[] = new Label[24];
	private int index;

	public PokemonInfoDialog(OurPokemon poke, int idx)
	{
		index = idx;
		initGUI(poke);
	}

	public void initGUI(OurPokemon poke)
	{
		try
		{
			List<String> translated = Translator.translate("_GUI");

			for(int i = 0; i < 24; i++)
			{
				data[i] = new Label();
				labels[i] = new Label();

				add(labels[i]);
				add(data[i]);
			}
			labels[0].setText(translated.get(1));
			labels[1].setText(translated.get(2));
			labels[2].setText(translated.get(3));
			labels[3].setText(translated.get(4));
			labels[4].setText(translated.get(5));
			labels[5].setText(translated.get(6));
			labels[6].setText(translated.get(7));
			labels[7].setText(translated.get(8));
			labels[8].setText(translated.get(9));
			labels[9].setText(translated.get(10));
			labels[10].setText(translated.get(11));
			labels[11].setText(translated.get(12));
			labels[12].setText(translated.get(13));
			labels[13].setText(translated.get(14));
			labels[14].setText("Hold item:");
			labels[15].setText("OT-Name:");
			labels[16].setText("Move 1:");
			labels[17].setText("Type:");
			labels[18].setText("Move 2:");
			labels[19].setText("Type:");
			labels[20].setText("Move 3:");
			labels[21].setText("Type:");
			labels[22].setText("Move 4:");
			labels[23].setText("Type:");
			// labels[13].setText("Exp to next level:");
			data[0].setText(String.valueOf(poke.getLevel()));
			data[1].setText(poke.getName());
			data[2].setText(String.valueOf(poke.getCurHP()) + "/" + String.valueOf(poke.getMaxHP()));
			data[3].setText(String.valueOf(poke.getAtk()));
			data[4].setText(String.valueOf(poke.getDef()));
			data[5].setText(String.valueOf(poke.getSpatk()));
			data[6].setText(String.valueOf(poke.getSpdef()));
			data[7].setText(String.valueOf(poke.getSpeed()));
			data[8].setText(poke.getAbility());
			data[9].setText(String.valueOf(poke.getExp()));
			data[10].setText(poke.getNature());
			data[11].setText(String.valueOf(poke.getType1()));
			if(poke.getType2() == null)
				data[12].setText("");
			else
				data[12].setText(String.valueOf(poke.getType2()));
			if(poke.getGender() == 1)
				data[13].setText(translated.get(29));
			else if(poke.getGender() == 0)
				data[13].setText(translated.get(30));
			else
				data[13].setText(translated.get(31));

			data[14].setText(poke.getHoldItem());
			data[15].setText(poke.getOriginalTrainer());
			if(!poke.getMoves()[0].equalsIgnoreCase(""))
			{
				data[16].setText(poke.getMoves()[0] + "(" + poke.getMoveCurPP()[0] + "/" + poke.getMoveMaxPP()[0] + ")");
				data[17].setText(poke.getMoveType(0));
			}
			else
			{
				data[16].setText("-");
				data[17].setText("-");
			}
			if(!poke.getMoves()[1].equalsIgnoreCase(""))
			{
				data[18].setText(poke.getMoves()[1] + "(" + poke.getMoveCurPP()[1] + "/" + poke.getMoveMaxPP()[1] + ")");
				data[19].setText(poke.getMoveType(1));
			}
			else
			{
				data[18].setText("-");
				data[19].setText("-");
			}
			if(!poke.getMoves()[2].equalsIgnoreCase(""))
			{
				data[20].setText(poke.getMoves()[2] + "(" + poke.getMoveCurPP()[2] + "/" + poke.getMoveMaxPP()[2] + ")");
				data[21].setText(poke.getMoveType(2));
			}
			else
			{
				data[20].setText("-");
				data[21].setText("-");
			}
			if(!poke.getMoves()[3].equalsIgnoreCase(""))
			{
				data[22].setText(poke.getMoves()[3] + "(" + poke.getMoveCurPP()[3] + "/" + poke.getMoveMaxPP()[3] + ")");
				data[23].setText(poke.getMoveType(3));
			}
			else
			{
				data[22].setText("-");
				data[23].setText("-");
			}

			icon = new Image(poke.getFrontSprite());
			add(icon);

			setVisible(true);
			setTitle(poke.getName());
			addCloseCallback(new Runnable()
			{
				@Override
				public void run()
				{
					GameClient.getInstance().getHUD().removePokemonInfoDialog(index);
				}

			});
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public int setSpriteNumber(int x)
	{
		int i = 0;
		if(x <= 385)
			i = x + 1;
		else if(x <= 388)
			i = 386;
		else if(x <= 414)
			i = x - 2;
		else if(x <= 416)
			i = 413;
		else if(x == 431)
			i = 431;
		else
			i = x - 4;
		return i;
	}

	@Override
	public void layout()
	{
		int x = 70;
		int y = 5;
		for(int i = 0; i < 24; i++)
		{
			data[i].setPosition(x + 80, y);
			labels[i].setPosition(x, y);
			y += 20;
		}
		icon.setPosition(5, 5);
		setSize(270, 510);
	}
}
