package org.pokenet.client.twl.ui.base;

import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.renderer.Image;

public class ProgressBar extends de.matthiasmann.twl.ProgressBar
{
	private float min;
	private float max;
	private float value;
	private boolean reversed;

	public ProgressBar(float minValue, float maxValue)
	{
		min = minValue;
		max = maxValue;
		reversed = false;
	}

	public ProgressBar(float minValue, float maxValue, boolean reverse)
	{
		min = minValue;
		max = maxValue;
		reversed = reverse;
	}

	@Override
	public void setValue(float newVal)
	{
		value = newVal;
		float twlval = newVal / (max - min);
		super.setValue(twlval);
	}

	public void setMinimum(float newVal)
	{
		min = newVal;
	}

	public void setMaximum(float newVal)
	{
		max = newVal;
	}

	public float getMinimum()
	{
		return min;
	}

	public float getMaximum()
	{
		return max;
	}

	public float getValue()
	{
		return value;
	}

	@Override
	public void paintWidget(GUI gui)
	{
		if(!reversed)
		{
			super.paintWidget(gui);
		}
		else
		{
			int width = getInnerWidth();
			int height = getInnerHeight();
			Image progressImage = getProgressImage();
			if(progressImage != null && value >= 0)
			{
				int imageWidth = progressImage.getWidth();
				int progressWidth = width - imageWidth;
				int scaledWidth = (int) (progressWidth * value);
				if(scaledWidth < 0)
				{
					scaledWidth = 0;
				}
				else if(scaledWidth > progressWidth)
				{
					scaledWidth = progressWidth;
				}
				progressImage.draw(getAnimationState(), getInnerX() + width - scaledWidth, getInnerY(), imageWidth + scaledWidth, height);
			}
		}
	}
}
