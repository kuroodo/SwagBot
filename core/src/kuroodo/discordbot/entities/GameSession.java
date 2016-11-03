package kuroodo.discordbot.entities;

import java.util.ArrayList;
import java.util.Random;

import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.managers.ChannelManager;

public abstract class GameSession implements Game {
	protected ArrayList<User> players;
	// The player having the turn or action and input focus
	protected User currentPlayer;

	protected TextChannel gameChannel;
	protected Message latestMessage;

	protected boolean gameFinished = false, isMultiplayer = false;

	protected boolean isInputValid;

	public GameSession(ArrayList<User> players, boolean hasMultiPLayer, TextChannel gameChannel) {
		this.players = players;
		isMultiplayer = hasMultiPLayer;
		this.gameChannel = gameChannel;

		// Setup starting players
		Random rand = new Random(System.nanoTime());
		int playerCount = rand.nextInt(players.size());
		currentPlayer = players.get(playerCount);
	}

	@Override
	public abstract void update(float delta);

	@Override
	public void recievePlayerInput(User playerWhoSentInput, String input, Message inputMessage) {
		latestMessage = inputMessage;
		if (playerWhoSentInput != currentPlayer) {
			isInputValid = false;
		} else {
			isInputValid = true;
		}
	}

	protected void sendMessage(String message) {
		gameChannel.sendMessageAsync(message, null);
	}

	@Override
	public boolean isMultiplayer() {
		return isMultiplayer;
	}

	@Override
	public boolean isgameFinished() {
		return gameFinished;
	}

	protected void setCurrentPlayer(User player) {
		currentPlayer = player;
	}

	@Override
	public User getCurrentPlayer() {
		return currentPlayer;
	}

	protected void setChannelTopic(String topic) {
		final ChannelManager chManager = gameChannel.getManager();

		chManager.setTopic(topic);
	}

	@Override
	public abstract void endGame();

	@Override
	public abstract void gameStart();

	@Override
	public abstract void gameHelpInfo();

}
