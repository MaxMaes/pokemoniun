package org.pokemonium.client.backend.entity;

import java.util.LinkedList;
import java.util.Queue;
import org.newdawn.slick.Image;
import org.pokemonium.client.backend.SpriteFactory;

/**
 * Represents a player
 * 
 * @author shadowkanji
 */
public class Player
{
	public enum Direction
	{
		Down, Left, Right, Up
	}

	private static SpriteFactory m_spriteFactory;

	// Handles animating footsteps
	public boolean m_leftOrRight = false;

	protected Image m_currentImage;

	protected Direction m_direction = Direction.Down;

	protected int m_id;
	protected boolean m_isAnimating = false;
	/* Handles movement queue */
	protected Queue<Direction> m_movementQueue = new LinkedList<Direction>();
	protected boolean m_ours = false;
	protected int m_sprite;
	protected int m_svrX;
	protected int m_svrY;
	protected String m_username;
	protected boolean m_wasOnGrass = false;

	protected int m_x;
	protected int m_y;

	protected int m_adminLevel;

	/**
	 * Returns the sprite factory
	 * 
	 * @return
	 */
	public static SpriteFactory getSpriteFactory()
	{
		return m_spriteFactory;
	}

	public static void loadSpriteFactory()
	{
		m_spriteFactory = new SpriteFactory();
	}

	public static void setSpriteFactory(SpriteFactory spriteFactory)
	{
		m_spriteFactory = spriteFactory;
	}

	/**
	 * Returns true if our player can move
	 * 
	 * @return
	 */
	public boolean canMove()
	{
		if(getX() == getServerX() && getY() == getServerY())
			return true;
		return false;
	}

	public void clearMovementQueue()
	{
		m_movementQueue.clear();
	}

	/**
	 * Returns the current image of this player's animation
	 * 
	 * @return
	 */
	public Image getCurrentImage()
	{
		return m_currentImage;
	}

	/**
	 * Returns this player's direction
	 * 
	 * @return
	 */
	public Direction getDirection()
	{
		return m_direction;
	}

	/**
	 * Returns this player's id
	 * 
	 * @return
	 */
	public int getId()
	{
		return m_id;
	}

	/**
	 * Returns the next movement for the player
	 * 
	 * @return
	 */
	public Direction getNextMovement()
	{
		return m_movementQueue.poll();
	}

	/**
	 * Returns this player's x co-ordinate serverside
	 * 
	 * @return
	 */
	public int getServerX()
	{
		return m_svrX;
	}

	/**
	 * Returns this player's y co-ordinate serverside
	 * 
	 * @return
	 */
	public int getServerY()
	{
		return m_svrY;
	}

	/**
	 * Returns this player's sprite
	 * 
	 * @return
	 */
	public int getSprite()
	{
		return m_sprite;
	}

	/**
	 * Returns the type of player
	 * 
	 * @return 0 for NPC, 1 for Player, 2 for HMObject
	 */
	public int getType()
	{
		return 0;
	}

	/**
	 * Returns this player's username
	 * 
	 * @return
	 */
	public String getUsername()
	{
		return m_username;
	}

	/**
	 * Returns this player's x co-ordinate
	 * 
	 * @return
	 */
	public int getX()
	{
		return m_x;
	}

	/**
	 * Returns this player's y co-ordinate
	 * 
	 * @return
	 */
	public int getY()
	{
		return m_y;
	}

	/**
	 * Returns true if this player is/should be animating
	 * 
	 * @return
	 */
	public boolean isAnimating()
	{
		return m_isAnimating;
	}

	/**
	 * Returns true if this is our player
	 * 
	 * @return
	 */
	public boolean isOurPlayer()
	{
		return m_ours;
	}

	/**
	 * Loads the player's sprite image
	 */
	public void loadSpriteImage()
	{
		m_currentImage = Player.getSpriteFactory().getSprite(m_direction, false, m_leftOrRight, m_sprite);
	}

	/**
	 * Moves this player down
	 */
	public void moveDown()
	{
		m_svrY += 32;
		m_isAnimating = true;
	}

	/**
	 * Moves this player left
	 */
	public void moveLeft()
	{
		m_svrX -= 32;
		m_isAnimating = true;
	}

	/**
	 * Moves this player right
	 */
	public void moveRight()
	{
		m_svrX += 32;
		m_isAnimating = true;
	}

	/**
	 * Moves this player up
	 */
	public void moveUp()
	{
		m_svrY -= 32;
		m_isAnimating = true;
	}

	/**
	 * Queues a movement
	 * 
	 * @param d
	 */
	public void queueMovement(Direction d)
	{
		/* if(m_direction != d || !GameClient.getInstance().getMapMatrix().getCurrentMap().isColliding(this, d) && !GameClient.getInstance().getHUD().getBattleDialog().isVisible())
		 * ;
		 * ; // TODO: Owh yeah... Lets make an awesome big IF statement and DO ABSOLUTELY NOTHING WITH IT. */
		m_movementQueue.offer(d);
	}

	/**
	 * Returns true if player has a movement queued and can be moved
	 * 
	 * @return
	 */
	public boolean requiresMovement()
	{
		return canMove() && m_movementQueue.size() > 0;
	}

	/**
	 * Sets if this player is animating
	 * 
	 * @param a
	 */
	public void setAnimating(boolean a)
	{
		m_isAnimating = a;
	}

	/**
	 * Sets this player's current image
	 * 
	 * @param i
	 */
	public void setCurrentImage(Image i)
	{
		m_currentImage = i;
	}

	/**
	 * Sets this player's direction
	 * 
	 * @param d
	 */
	public void setDirection(Direction d)
	{
		m_direction = d;
	}

	/**
	 * Sets this player's id
	 * 
	 * @param id
	 */
	public void setId(int id)
	{
		m_id = id;
	}

	/**
	 * Sets if this is our player
	 * 
	 * @param b
	 */
	public void setOurPlayer(boolean b)
	{
		m_ours = b;
	}

	/**
	 * Sets this player's x co-ordinate on the server
	 * 
	 * @param x
	 */
	public void setServerX(int x)
	{
		m_svrX = x;
	}

	/**
	 * Sets this player's y co-ordinate on the server
	 * 
	 * @param y
	 */
	public void setServerY(int y)
	{
		m_svrY = y;
	}

	/**
	 * Sets this player's sprite
	 * 
	 * @param sprite
	 */
	public void setSprite(int sprite)
	{
		m_sprite = sprite;
	}

	/**
	 * Sets this player's username
	 * 
	 * @param username
	 */
	public void setUsername(String username)
	{
		m_username = username;
	}

	/**
	 * Sets this player's x co-ordinate
	 * 
	 * @param x
	 */
	public void setX(int x)
	{
		m_x = x;
	}

	/**
	 * Sets this player's y co-ordinate
	 * 
	 * @param y
	 */
	public void setY(int y)
	{
		m_y = y;
	}

	public void setAdminLevel(int admin)
	{
		m_adminLevel = admin;
	}

	public int getAdminLevel()
	{
		return m_adminLevel;
	}
}
