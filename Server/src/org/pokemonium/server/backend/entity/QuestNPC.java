package org.pokemonium.server.backend.entity;

import java.util.ArrayList;

/**
 * 
 * @author sadhi
 *
 */
public class QuestNPC extends NPC 
{
	private ArrayList<QuestStage> quests; 
	
	/**
	 * Constructor
	 */
	public QuestNPC()
	{
		super();
	}

	/**
	 * add speech to a questStage
	 * 
	 * @param parseDouble
	 * @param parseInt
	 */
	public void addQuestSpeech(double questStage, int speech) {
		for(QuestStage q : quests)
		{
			if(q.getStartStage() == questStage)
			{
				q.addSpeech(speech);
				break;
			}
		}
		
	}
	
}

class QuestStage
{
	double startStage;	//example: 12.0
	double endStage;	//example: 12.1
	private final ArrayList<Integer> m_questspeech = new ArrayList<Integer>();
	
	public double getStartStage()
	{
		return startStage;
	}
	
	public double getEndStage()
	{
		return endStage;
	}
	
	public void setStartStage(double s)
	{
		startStage = s;
	}
	
	public void setEndStage(double e)
	{
		endStage = e;
	}
	
	/**
	 * Adds speech to this npc
	 * 
	 * @param id
	 */
	public void addSpeech(int id)
	{
		m_questspeech.add(id);
	}
}
