package kuroodo.discordbot.games;

import java.util.ArrayList;

import kuroodo.discordbot.entities.GameSession;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

public class ExampleGame extends GameSession {

	private Member player1, player2;
	private String magicword = "java";

	public ExampleGame(ArrayList<Member> players, boolean hasMultiPLayer, TextChannel gameChannel) {
		super(players, hasMultiPLayer, gameChannel);
		player1 = currentPlayer;

		if (isMultiplayer) {
			for (Member player : players) {
				if (player != player1) {
					player2 = player;
				}
			}
		}
	}

	@Override
	public void recievePlayerInput(Member player, String input, Message inputMessage) {
		super.recievePlayerInput(player, input, inputMessage);
		if (isInputValid) {
			sendMessage("You said " + input);

			if (input.toLowerCase().equals(magicword)) {
				sendMessage("That's the magic word!");
			} else {
				sendMessage("That's NOT the magic word!");
			}

			if (isMultiplayer) {
				rotatePlayers();
			}
		} else {
			inputMessage.deleteMessage();
		}
	}

	private void rotatePlayers() {
		final Member p1 = player1;
		final Member p2 = player2;

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
	}

	private void currentTurnMsg() {
		sendMessage("It is " + getCurrentPlayer().getAsMention() + "'s turn!");
	}

	@Override
	public void gameStart() {
		sendMessage(
				"Welcome to TestGame! Guess the magic word\n Play game by typing: !game [word] Example: !game apple\nType !gamehelp for help!");
		if (isMultiplayer) {
			currentTurnMsg();
		}
	}

	@Override
	public void gameHelpInfo() {
		sendMessage("Find the magic word \n Play game by typing: !game [word] Example: !game apple");
	}

}
