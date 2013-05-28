package org.pokenet.client.twl.ui.frames;

import org.pokenet.client.GameClient;
import org.pokenet.client.backend.BattleManager;

/**
 * Bag used during battles
 * 
 * @author ZombieBear
 */
public class BattleBag extends BigBagDialog
{
	/**
	 * Default Constructor
	 */
	public BattleBag()
	{
		super();
		m_categoryButtons[0].setEnabled(false);
		m_categoryButtons[4].setEnabled(false);
		m_curCategory = 1;
		update();
	}

	@Override
	public void closeBag()
	{
		BattleManager.getInstance().getBattleWindow().showAttack();
		GameClient.getInstance().getHUD().removeBattlebag();
	}

	@Override
	public void useItem(int i)
	{
		destroyPopup();
		if(m_curCategory == 0 || m_curCategory == 3)
		{
			m_popup = new ItemPopup(((String) m_itemBtns.get(i).getTooltipContent()).split("\n")[0], Integer.parseInt(m_itemBtns.get(i).getText()), false, true);
			m_popup.setPosition(m_itemBtns.get(i).getInnerX(), m_itemBtns.get(i).getInnerY() + m_itemBtns.get(i).getHeight() - 48);
			add(m_popup);
		}
		else
		{
			m_popup = new ItemPopup(((String) m_itemBtns.get(i).getTooltipContent()).split("\n")[0], Integer.parseInt(m_itemBtns.get(i).getText()), true, true);
			m_popup.setPosition(m_itemBtns.get(i).getInnerX(), m_itemBtns.get(i).getInnerY() + m_itemBtns.get(i).getHeight() - 48);
			add(m_popup);
		}
		closeBag();
	}
}
