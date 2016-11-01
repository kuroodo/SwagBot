package kuroodo.discordbot.games;

import java.util.ArrayList;

import kuroodo.discordbot.entities.GameSession;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;

public class TestGame extends GameSession {

	User player1, player2;

	public TestGame(ArrayList<User> players, boolean hasMultiPLayer, TextChannel gameChannel) {
		super(players, hasMultiPLayer, gameChannel);
		player1 = currentPlayer;

		if (isMultiplayer) {
			for (User player : players) {
				if (player != player1) {
					player2 = player;
				}
			}
		}
	}

	@Override
	public void recievePlayerInput(User player, String input, Message inputMessage) {
		super.recievePlayerInput(player, input, inputMessage);
		if (isInputValid) {
			sendMessage("You said " + input);

			String magicword = "ok";
			if (input.equals(magicword)) {
				sendMessage("That's the magic word!");
			}

			if (isMultiplayer) {
				rotatePlayers();
			}
		} else {
			inputMessage.deleteMessage();
		}
	}

	private void rotatePlayers() {
		final User p1 = player1;
		final User p2 = player2;

		player1 = p2;
		player2 = p1;

		currentPlayer = player1;

		currentTurnMsg();
	}

	@Override
	public void update(float delta) {
	}

	@Override
	public void endGame() {
		sendMessage("ENDING!");
	}

	private void currentTurnMsg() {
		sendMessage("It is " + getCurrentTurnPlayer().getAsMention() + "'s turn!");
	}

	@Override
	public void gameStart() {
		sendMessage(
				"Welcome to TestGame! Find the retarded word you idiot\n Play game by typing: !game [word] Example: !game faggot\nType !gamehelp for help!");
		if (isMultiplayer) {
			currentTurnMsg();
		}
	}

	@Override
	public void gameHelpInfo() {
		sendMessage("Find the retarded word you retard\n Play game by typing: !game [word] Example: !game faggot");
	}

}
