package org.pokenet.client.ui.frames;

import org.pokenet.client.GameClient;
import org.pokenet.client.backend.BattleManager;
import de.matthiasmann.twl.Widget;

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
	public BattleBag(Widget root)
	{
		super(root);
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
	public void useItem(int i, Widget root)
	{
		destroyPopup(root);
		if(m_curCategory == 0 || m_curCategory == 3)
		{
			m_popup = new ItemPopup(((String) m_itemBtns.get(i).getTooltipContent()).split("\n")[0], Integer.parseInt(m_itemBtns.get(i).getText()), false, true, root);
			m_popup.setPopupPosition(m_itemBtns.get(i).getInnerX(), m_itemBtns.get(i).getInnerY() + m_itemBtns.get(i).getHeight() - 48);
		}
		else
		{
			m_popup = new ItemPopup(((String) m_itemBtns.get(i).getTooltipContent()).split("\n")[0], Integer.parseInt(m_itemBtns.get(i).getText()), true, true, root);
			m_popup.setPopupPosition(m_itemBtns.get(i).getInnerX(), m_itemBtns.get(i).getInnerY() + m_itemBtns.get(i).getHeight() - 48);
		}

		m_popup.setItemUsedCallback(new Runnable()
		{
			@Override
			public void run()
			{
				closeBag();
			}
		});
	}
}
