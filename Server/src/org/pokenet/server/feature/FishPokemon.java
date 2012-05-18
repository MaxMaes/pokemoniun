package org.pokenet.server.feature;

/**
 * Stores exp and level information for a Fish Pokemon(one found by fishing)
 * 
 * @author Fshy
 */
public class FishPokemon
{

	private int m_experience;
	private int m_levelReq;
	private int m_rodReq;

	/** Standard Constructor */
	public FishPokemon()
	{
		m_experience = 0;
		m_levelReq = 0;
		m_rodReq = 0;
	}

	/**
	 * Alternative constructor
	 * 
	 * @param levelReq Minimum level required.
	 * @param experience Experience given.
	 * @param rodReq Minimum required fishing rod.
	 */
	public FishPokemon(int levelReq, int experience, int rodReq)
	{
		m_experience = experience;
		m_levelReq = levelReq;
		m_rodReq = rodReq;
	}

	/**
	 * Returns the rod required to fish up this pogey 0 is old rod, 15 is good rod, 35 is great rod, 50 is ultra rod.
	 * 
	 * @return m_rodReq The required rod level.
	 */
	public int getReqRod()
	{
		return m_rodReq;
	}

	/**
	 * Returns the level required to fish up this pogey.
	 * 
	 * @return m_levelReq The required fishing level.
	 */
	public int getReqLevel()
	{
		return m_levelReq;
	}

	/**
	 * Returns the experience for fishing this pogey.
	 * 
	 * @return m_experience The given experience.
	 */
	public int getExperience()
	{
		return m_experience;
	}

	/**
	 * Sets the experience gained from fishing this pogey.
	 * 
	 * @param exp The experience required.
	 */
	public void setExperience(int exp)
	{
		m_experience = exp;
	}

	/**
	 * Sets the level required to encounter/fish this pogey
	 * 
	 * @param lvl The required level.
	 */
	public void setLevelReq(int lvl)
	{
		m_levelReq = lvl;
	}
}
