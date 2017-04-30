package kuroodo.discordbot.entities;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;

public interface Game {

	public void update(float delta);

	public void recievePlayerInput(Member player, String input, Message inputMessage);

	public void recievePrivatePlayerInput(Member player, String input, Message inputMessage);

	public boolean isMultiplayer();

	public boolean isgameFinished();

	// Return player in current turn/action and has input focus
	public Member getCurrentPlayer();

	// Called when the GameListener is ending the game
	public void endGame();

	public void gameStart();

	// Print out information to help understand and interact game
	public void gameHelpInfo();
}

// How Games Work:
// Game stores all necessary methods for a base game class
// GameSession implements game.
// All main game classes (like TicTacToe) inherit from GameSession
// GameListener creates a specified game session and gives any parameters to
// ...initialize it
// GameListener listens for server messages and passes on any game relevant
// ...input to it's GameSession
// All GameListeners are stored in the GlobalGameManager class
// GlobalGameManager is used to store and get GameListeners
// When CommandPlayGame is called and a new game can be made, it creates a
// ...GameListener for the game and then stores it in the GlobalGameManager
