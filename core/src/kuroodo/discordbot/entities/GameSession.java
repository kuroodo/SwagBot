package kuroodo.discordbot.entities;

import java.util.ArrayList;
import java.util.Random;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.managers.ChannelManager;

public abstract class GameSession implements Game {
	protected ArrayList<Member> players;
	// The player having the turn or action and input focus
	protected Member currentPlayer;

	protected TextChannel gameChannel;
	protected Message latestMessage;

	protected boolean gameFinished = false, isMultiplayer = false;

	protected boolean isInputValid;

	public GameSession(ArrayList<Member> players, boolean hasMultiPLayer, TextChannel gameChannel) {
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
	public void recievePlayerInput(Member playerWhoSentInput, String input, Message inputMessage) {
		latestMessage = inputMessage;
		isInputValid = (playerWhoSentInput == currentPlayer);
	}

	@Override
	public void recievePrivatePlayerInput(Member playerWhoSentInput, String input, Message inputMessage) {
		latestMessage = inputMessage;
	}

	protected void sendMessage(String message) {
		gameChannel.sendMessage(message).queue();
	}

	@Override
	public boolean isMultiplayer() {
		return isMultiplayer;
	}

	@Override
	public boolean isgameFinished() {
		return gameFinished;
	}

	protected void setCurrentPlayer(Member player) {
		currentPlayer = player;
	}

	@Override
	public Member getCurrentPlayer() {
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
