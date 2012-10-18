package org.pokenet.client.backend;

import org.lwjgl.util.Timer;
import org.pokenet.client.backend.entity.Player;
import org.pokenet.client.backend.entity.Player.Direction;

public class Animator
{
	private static final int ANIMATION_INCREMENT = 4;

	private ClientMapMatrix m_mapMatrix;

	private Timer m_timer;

	// Sets up calls
	public Animator(ClientMapMatrix maps)
	{
		m_mapMatrix = maps;
		m_timer = new Timer();
	}

	// Prepares for animation
	public void animate()
	{
		try
		{
			while(m_timer.getTime() <= 0.025)
				Timer.tick();
			ClientMap map = m_mapMatrix.getCurrentMap();
			if(map != null)
				for(int i = 0; i < m_mapMatrix.getPlayers().size(); i++)
					animatePlayer(m_mapMatrix.getPlayers().get(i));
			m_timer.reset();
		}
		catch(Exception e)
		{
			m_timer.reset();
		}
	}

	/**
	 * Animates players moving
	 * 
	 * @param player
	 */
	private void animatePlayer(Player player)
	{
		/* Check if we need to move the player */
		if(player.requiresMovement())
			switch(player.getNextMovement())
			{
				case Down:
					if(player.getDirection() == Direction.Down)
						player.setServerY(player.getY() + 32);
					else
					{
						player.setDirection(Direction.Down);
						player.loadSpriteImage();
					}
					break;
				case Left:
					if(player.getDirection() == Direction.Left)
						player.setServerX(player.getX() - 32);
					else
					{
						player.setDirection(Direction.Left);
						player.loadSpriteImage();
					}
					break;
				case Right:
					if(player.getDirection() == Direction.Right)
						player.setServerX(player.getX() + 32);
					else
					{
						player.setDirection(Direction.Right);
						player.loadSpriteImage();
					}
					break;
				case Up:
					if(player.getDirection() == Direction.Up)
						player.setServerY(player.getY() - 32);
					else
					{
						player.setDirection(Direction.Up);
						player.loadSpriteImage();
					}
					break;
			}
		/* Keep the screen following the player, i.e. move the map also */
		if(player.isOurPlayer())
			if(player.getX() > player.getServerX())
				m_mapMatrix.getCurrentMap().setXOffset(m_mapMatrix.getCurrentMap().getXOffset() + ANIMATION_INCREMENT, true);
			else if(player.getX() < player.getServerX())
				m_mapMatrix.getCurrentMap().setXOffset(m_mapMatrix.getCurrentMap().getXOffset() - ANIMATION_INCREMENT, true);
			else if(player.getY() > player.getServerY())
				m_mapMatrix.getCurrentMap().setYOffset(m_mapMatrix.getCurrentMap().getYOffset() + ANIMATION_INCREMENT, true);
			else if(player.getY() < player.getServerY())
				m_mapMatrix.getCurrentMap().setYOffset(m_mapMatrix.getCurrentMap().getYOffset() - ANIMATION_INCREMENT, true);
		/* Move the player on screen */
		if(player.getX() > player.getServerX())
		{
			// Choose the sprite according to the player's position
			if(player.getX() % 32 == 0)
			{
				player.setDirection(Direction.Left);
				player.m_leftOrRight = !player.m_leftOrRight;
				player.setCurrentImage(Player.getSpriteFactory().getSprite(player.getDirection(), true, player.m_leftOrRight, player.getSprite()));
			}

			player.setX(player.getX() - ANIMATION_INCREMENT);
			if(player.getX() > player.getServerX() && player.getX() % 32 == 0)
				/* If the player is still behind the server, make sure the stopped animation is shown */
				player.setCurrentImage(Player.getSpriteFactory().getSprite(player.getDirection(), false, player.m_leftOrRight, player.getSprite()));
		}
		else if(player.getX() < player.getServerX())
		{
			if(player.getX() % 32 == 0)
			{
				player.setDirection(Direction.Right);
				player.m_leftOrRight = !player.m_leftOrRight;
				player.setCurrentImage(Player.getSpriteFactory().getSprite(player.getDirection(), true, player.m_leftOrRight, player.getSprite()));
			}
			player.setX(player.getX() + ANIMATION_INCREMENT);
			if(player.getX() < player.getServerX() && player.getX() % 32 == 0)
				/* If the player is still behind the server, make sure the stopped animation is shown */
				player.setCurrentImage(Player.getSpriteFactory().getSprite(player.getDirection(), false, player.m_leftOrRight, player.getSprite()));
		}
		else if(player.getY() > player.getServerY())
		{
			if((player.getY() + 8) % 32 == 0)
			{
				player.setDirection(Direction.Up);
				player.m_leftOrRight = !player.m_leftOrRight;
				player.setCurrentImage(Player.getSpriteFactory().getSprite(player.getDirection(), true, player.m_leftOrRight, player.getSprite()));
			}
			player.setY(player.getY() - ANIMATION_INCREMENT);
			if(player.getY() > player.getServerY() && (player.getY() + 8) % 32 == 0)
				/* If the player is still behind the server, make sure the stopped animation is shown */
				player.setCurrentImage(Player.getSpriteFactory().getSprite(player.getDirection(), false, player.m_leftOrRight, player.getSprite()));
		}
		else if(player.getY() < player.getServerY())
		{
			if((player.getY() + 8) % 32 == 0)
			{
				player.setDirection(Direction.Down);
				player.m_leftOrRight = !player.m_leftOrRight;
				player.setCurrentImage(Player.getSpriteFactory().getSprite(player.getDirection(), true, player.m_leftOrRight, player.getSprite()));
			}
			player.setY(player.getY() + ANIMATION_INCREMENT);
			if(player.getY() < player.getServerY() && (player.getY() + 8) % 32 == 0)
				/* If the player is still behind the server, make sure the stopped animation is shown */
				player.setCurrentImage(Player.getSpriteFactory().getSprite(player.getDirection(), false, player.m_leftOrRight, player.getSprite()));
		}
		/* The player is now in sync with the server, stop moving/animating them */
		if(player.getX() == player.getServerX() && player.getY() == player.getServerY())
		{
			player.setDirection(player.getDirection());
			player.setAnimating(false);
			player.loadSpriteImage();
		}
	}
}
