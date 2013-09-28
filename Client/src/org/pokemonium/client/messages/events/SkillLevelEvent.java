package org.pokemonium.client.messages.events;

import org.pokemonium.client.GameClient;
import org.pokemonium.client.Session;
import org.pokemonium.client.messages.MessageEvent;
import org.pokemonium.client.protocol.ClientMessage;
import org.pokemonium.client.protocol.ServerMessage;

public class SkillLevelEvent implements MessageEvent
{

	/**
	 * Updates all the player's skill levels.
	 * This will replace the messages: TrainerLevelEvent, BreedLevelEvent, FishLevelEvent and CoordinateLevelEvent.
	 */
	@Override
	public void parse(Session session, ServerMessage request, ClientMessage message)
	{
		int trainerLvl = request.readInt();
		int breedingLvl = request.readInt();
		int fishingLvl = request.readInt();
		int coordinatingLvl = request.readInt();
		if(GameClient.getInstance().getOurPlayer().getTrainerLevel() != -1 && GameClient.getInstance().getOurPlayer().getTrainerLevel() != trainerLvl)
			GameClient.getInstance().getHUD().getChat().addSystemMessage("*" + "Congratulations! Your trainer level is now " + trainerLvl + ".");
		GameClient.getInstance().getOurPlayer().setTrainerLevel(trainerLvl);

		if(GameClient.getInstance().getOurPlayer().getBreedingLevel() != -1 && GameClient.getInstance().getOurPlayer().getBreedingLevel() != breedingLvl)
			GameClient.getInstance().getHUD().getChat().addSystemMessage("*" + "Congratulations! Your breeding level is now " + breedingLvl + ".");
		GameClient.getInstance().getOurPlayer().setBreedingLevel(breedingLvl);

		if(GameClient.getInstance().getOurPlayer().getFishingLevel() != -1 && GameClient.getInstance().getOurPlayer().getFishingLevel() != fishingLvl)
			GameClient.getInstance().getHUD().getChat().addSystemMessage("*" + "Congratulations! Your fishing level is now " + fishingLvl + ".");
		GameClient.getInstance().getOurPlayer().setFishingLevel(fishingLvl);

		if(GameClient.getInstance().getOurPlayer().getCoordinatingLevel() != -1 && GameClient.getInstance().getOurPlayer().getCoordinatingLevel() != coordinatingLvl)
			GameClient.getInstance().getHUD().getChat().addSystemMessage("*" + "Congratulations! Your fishing level is now " + coordinatingLvl + ".");
		GameClient.getInstance().getOurPlayer().setCoordinatingLevel(coordinatingLvl);
	}

}
