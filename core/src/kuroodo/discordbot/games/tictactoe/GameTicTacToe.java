package kuroodo.discordbot.games.tictactoe;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Random;

import kuroodo.discordbot.Init;
import kuroodo.discordbot.entities.GameSession;
import kuroodo.discordbot.helpers.JDAHelper;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

public class GameTicTacToe extends GameSession {

	final int boardSize = 9;

	private Member player1, player2;
	private String p1Name, p2Name;
	private char currentSymbol;

	private LinkedHashMap<String, BoardSlot> board;

	private int p1Score = 0, p2Score = 0;

	private boolean isAITurn = false, isTie = false;
	private float aiCurrentDelay = 0, aiTotalDelay = 1.5f;

	public GameTicTacToe(ArrayList<Member> players, boolean hasMultiPLayer, TextChannel gameChannel) {
		super(players, hasMultiPLayer, gameChannel);
		player1 = currentPlayer;
		p1Name = player1.getUser().getName();

		// Setup player 2
		for (Member player : players) {
			if (player != player1) {
				player2 = player;
				break;
			}
		}
		p2Name = player2.getUser().getName();
	}

	@Override
	public void recievePlayerInput(Member player, String input, Message inputMessage) {
		super.recievePlayerInput(player, input, inputMessage);

		if (gameFinished && input.startsWith("playagain")) {
			startFreshMatch();
		} else if (isInputValid && !isAITurn && !gameFinished) {
			if (isLegalMove(input)) {
				board.get(input).markAs(currentSymbol);
				showBoard();
				if (isPlayerWin()) {
					gameFinished = true;
					sendGameSummary();
				} else {
					rotatePlayers();
				}
			}
		} else {
			// Delete any irrelevant/illegal !game messages
			inputMessage.deleteMessage();
		}
	}

	private boolean isLegalMove(String input) {

		if (board.containsKey(input)) {

			final BoardSlot slot = board.get(input);
			if (!slot.isMarked()) {
				return true;
			} else {
				sendMessage("That slot is already marked!");
				return false;
			}
		} else {
			sendMessage("That slot doesn't exist!");
			return false;
		}
	}

	private void rotatePlayers() {
		final Member p1 = player1;
		final Member p2 = player2;

		player1 = p2;
		player2 = p1;

		currentPlayer = player1;
		if (!isMultiplayer) {
			if (isAITurn) {
				isAITurn = false;
			} else {
				isAITurn = true;
			}
		}

		if (currentSymbol == 'X') {
			currentSymbol = 'O';
		} else {
			currentSymbol = 'X';
		}

		sendCurrentTurnMsg();

	}

	private void aiMove() {
		Random rand = new Random(System.nanoTime());

		ArrayList<BoardSlot> openSlots = new ArrayList<>();

		for (BoardSlot slot : board.values()) {
			if (!slot.isMarked()) {
				openSlots.add(slot);
			}
		}

		int slotToMarkIndex = rand.nextInt(openSlots.size());

		String slotName = "";
		slotName = slotName + openSlots.get(slotToMarkIndex).getStartSymbol();

		board.get(slotName).markAs(currentSymbol);

		showBoard();

		if (isPlayerWin()) {
			gameFinished = true;
			sendGameSummary();
		} else {
			rotatePlayers();
		}
	}

	private boolean isPlayerWin() {
		ArrayList<BoardSlot> slots = new ArrayList<>(board.values());
		char slotSym1, slotSym2, slotSym3;

		// Horizontal Wins
		slotSym1 = slots.get(0).getCurrentSymbol();
		slotSym2 = slots.get(1).getCurrentSymbol();
		slotSym3 = slots.get(2).getCurrentSymbol();
		if (isCheckBoardConnection(slotSym1, slotSym2, slotSym3))
			return true;

		slotSym1 = slots.get(3).getCurrentSymbol();
		slotSym2 = slots.get(4).getCurrentSymbol();
		slotSym3 = slots.get(5).getCurrentSymbol();
		if (isCheckBoardConnection(slotSym1, slotSym2, slotSym3))
			return true;

		slotSym1 = slots.get(6).getCurrentSymbol();
		slotSym2 = slots.get(7).getCurrentSymbol();
		slotSym3 = slots.get(8).getCurrentSymbol();
		if (isCheckBoardConnection(slotSym1, slotSym2, slotSym3))
			return true;

		// Vertical Wins
		slotSym1 = slots.get(0).getCurrentSymbol();
		slotSym2 = slots.get(3).getCurrentSymbol();
		slotSym3 = slots.get(6).getCurrentSymbol();
		if (isCheckBoardConnection(slotSym1, slotSym2, slotSym3))
			return true;

		slotSym1 = slots.get(1).getCurrentSymbol();
		slotSym2 = slots.get(4).getCurrentSymbol();
		slotSym3 = slots.get(7).getCurrentSymbol();
		if (isCheckBoardConnection(slotSym1, slotSym2, slotSym3))
			return true;

		slotSym1 = slots.get(2).getCurrentSymbol();
		slotSym2 = slots.get(5).getCurrentSymbol();
		slotSym3 = slots.get(8).getCurrentSymbol();
		if (isCheckBoardConnection(slotSym1, slotSym2, slotSym3))
			return true;

		// Diagonal Wins
		slotSym1 = slots.get(0).getCurrentSymbol();
		slotSym2 = slots.get(4).getCurrentSymbol();
		slotSym3 = slots.get(8).getCurrentSymbol();
		if (isCheckBoardConnection(slotSym1, slotSym2, slotSym3))
			return true;

		slotSym1 = slots.get(2).getCurrentSymbol();
		slotSym2 = slots.get(4).getCurrentSymbol();
		slotSym3 = slots.get(6).getCurrentSymbol();
		if (isCheckBoardConnection(slotSym1, slotSym2, slotSym3))
			return true;

		// If no win, check if all marked to see if tie
		int markedCount = 0;
		for (BoardSlot boardSlot : board.values()) {
			if (boardSlot.isMarked()) {
				markedCount++;
			}
		}

		if (markedCount == boardSize) {
			isTie = true;
			return true;
		}
		return false;
	}

	private boolean isCheckBoardConnection(char slotSym1, char slotSym2, char slotSym3) {
		return slotSym1 == slotSym2 && slotSym2 == slotSym3;
	}

	@Override
	public void update(float delta) {
		if (!gameFinished) {
			if (isAITurn) {
				aiCurrentDelay += delta;
				if (aiCurrentDelay >= aiTotalDelay) {
					aiMove();
					aiCurrentDelay = 0;
				}
			}
		}

	}

	@Override
	public void endGame() {
	}

	private void sendCurrentTurnMsg() {
		sendMessage("It is " + currentPlayer.getAsMention() + "'s turn! **Symbol: " + currentSymbol + "**");
	}

	// private void sendGameMessage(String message) {
	// String msg = "```xl\n" + message + "\n```";
	// sendMessage(msg);
	// }

	private void showBoard() {
		String boardString = "";

		int slotCount = 0;

		for (BoardSlot slot : board.values()) {
			slotCount++;
			boardString = boardString + slot.getCurrentSymbol();

			if (slotCount == 3) {
				slotCount = 0;
				boardString = boardString + "\n-+-+-+-+-\n";
			} else {
				boardString = boardString + " : ";
			}
		}

		String boardMsg = "```xl\n" + boardString + "\n```";
		sendMessage(boardMsg);
	}

	private void setupBoard() {
		int totalSlots = 0;
		for (int currentSlot = 0; currentSlot < boardSize; currentSlot++) {
			totalSlots++;
			board.put(totalSlots + "", new BoardSlot(Integer.toString(totalSlots).charAt(0)));
		}
	}

	private void sendGameSummary() {
		if (isTie) {
			sendMessage("The match was a tie! Would you like to play again? (type !game playagain)");
		} else {
			if (currentPlayer.getUser().getName().equals(p1Name)) {
				p1Score++;
			} else {
				p2Score++;
			}
			sendMessage("Player " + currentPlayer.getAsMention()
					+ " has won the match!\nWould you like to play again? (type !game playagain)");
		}
		sendMessage(p1Name + " wins:" + p1Score + " | " + p2Name + " wins: " + p2Score);
	}

	private void startFreshMatch() {
		isAITurn = false;
		aiCurrentDelay = 0;
		gameFinished = false;
		isTie = false;

		sendMessage(
				"Welcome to TicTacToe! Get 3 consecutive X's or O's to win!\nTo play, use !game [slot numer] . Example: !game 5\nType !gamehelp for help!");

		board = new LinkedHashMap<>();
		Random rand = new Random(System.nanoTime());
		final int result = rand.nextInt(2);
		if (result == 1) {
			currentSymbol = 'X';
		} else {
			currentSymbol = 'O';
		}

		setupBoard();
		showBoard();

		// If player1 is the bot
		if (player1 == JDAHelper.getGuild().getMember(Init.getJDA().getSelfUser())) {
			isAITurn = true;
		}

		sendCurrentTurnMsg();
	}

	@Override
	public void gameStart() {
		startFreshMatch();
	}

	@Override
	public void gameHelpInfo() {
		sendMessage("Get 3 consecutive X's or O's to win!\nTo play, use !game [slot numer] . Example: !game 5");
	}

}
