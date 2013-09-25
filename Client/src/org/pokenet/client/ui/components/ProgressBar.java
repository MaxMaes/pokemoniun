package org.pokenet.client.ui.components;

import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.renderer.Image;

public class ProgressBar extends de.matthiasmann.twl.ProgressBar
{
	private float min;
	private float max;
	private float value;
	private float twlVal;
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
		float val = newVal - min;
		val = (val >= 0) ? val : 0;
		twlVal = val / (max - min);
		super.setValue(twlVal);
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
				int barWidth = (int) (twlVal * width);
				if(barWidth < 0)
				{
					barWidth = 0;
				}
				else if(barWidth > width)
				{
					barWidth = width;
				}
				progressImage.draw(getAnimationState(), getInnerX() + width - barWidth, getInnerY(), barWidth, height);
			}
		}
	}
}
