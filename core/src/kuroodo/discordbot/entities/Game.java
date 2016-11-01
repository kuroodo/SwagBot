package kuroodo.discordbot.entities;

import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.User;

public interface Game {

	public void update(float delta);

	public void recievePlayerInput(User player, String input, Message inputMessage);

	public boolean isMultiplayer();

	public boolean isgameFinished();

	public User getCurrentTurnPlayer();

	public void endGame();

	public void gameStart();

	public void gameHelpInfo();
}
