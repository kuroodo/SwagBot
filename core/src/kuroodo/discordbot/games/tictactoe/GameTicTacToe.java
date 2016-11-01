package kuroodo.discordbot.games.tictactoe;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Random;

import kuroodo.discordbot.Init;
import kuroodo.discordbot.entities.GameSession;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;

public class GameTicTacToe extends GameSession {

	private User player1, player2;
	private String p1Name, p2Name;
	private char currentSymbol;

	private LinkedHashMap<String, BoardSlot> board;

	private int p1Score = 0, p2Score = 0;

	private boolean aiTurn = false, tie = false;
	private float aiCurrentDelay = 0, aiTotalDelay = 1.5f;

	public GameTicTacToe(ArrayList<User> players, boolean hasMultiPLayer, TextChannel gameChannel) {
		super(players, hasMultiPLayer, gameChannel);
		player1 = currentPlayer;
		p1Name = player1.getUsername();

		// Setup player 2
		for (User player : players) {
			if (player != player1) {
				player2 = player;
			}
		}
		p2Name = player2.getUsername();
	}

	@Override
	public void recievePlayerInput(User player, String input, Message inputMessage) {
		super.recievePlayerInput(player, input, inputMessage);
		System.out.println(input);

		if (finished == true && input.startsWith("playagain")) {
			startFresh();
		} else if (isInputValid && !aiTurn && !finished) {
			if (isLegalMove(input)) {
				board.get(input).markAs(currentSymbol);
				showBoard();
				if (isPlayerWin()) {
					finished = true;
					sendGameSummary();
				} else if (tie) {
					finished = true;
					sendGameSummary();
				} else {
					rotatePlayers();
				}
			}
		} else {
			inputMessage.deleteMessage();
		}
	}

	private boolean isLegalMove(String input) {

		if (board.containsKey(input)) {
			// final char inputChar = input.charAt(0);
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

		// for (int row = 0; row < board.length; row++) {
		// for (int column = 0; column < board[row].length; column++) {
		// if (board[row][column] != 'X' && board[row][column] != 'O') {
		// if (board[row][column] == inputChar) {
		// return true;
		// } else {
		// sendMessage("That slot doesn't exist!");
		// return false;
		// }
		// } else {
		// sendMessage("That slot is already marked!");
		// return false;
		// }
		// }
		// }
	}

	private void rotatePlayers() {
		final User p1 = player1;
		final User p2 = player2;

		player1 = p2;
		player2 = p1;

		currentPlayer = player1;
		if (!isMultiplayer) {
			if (aiTurn) {
				aiTurn = false;
			} else {
				aiTurn = true;
			}
		}

		sendCurrentTurnMsg();

		if (currentSymbol == 'X') {
			currentSymbol = 'O';
		} else {
			currentSymbol = 'X';
		}

	}

	private void aiMove() {
		Random rand = new Random(System.nanoTime());

		ArrayList<BoardSlot> openSlots = new ArrayList<>();

		for (BoardSlot slot : board.values()) {
			if (!slot.isMarked()) {
				openSlots.add(slot);
			}
		}

		int slotIndex = rand.nextInt(openSlots.size());

		String slotName = "";
		slotName = slotName + openSlots.get(slotIndex).getStartSymbol();

		board.get(slotName).markAs(currentSymbol);

		showBoard();

		if (isPlayerWin()) {
			finished = true;
			sendGameSummary();
		} else if (tie) {
			finished = true;
			sendGameSummary();
		} else {
			rotatePlayers();
		}
	}

	private boolean isPlayerWin() {
		ArrayList<BoardSlot> slots = new ArrayList<>(board.values());
		char slotSym1, slotSym2, slotSym3;

		// Horizontal Wins
		// int markedStreak = 0, slotCount = 0;
		// char streakSymbol = '\0';
		// for (BoardSlot slot : board.values()) {
		// slotCount++;
		// if (slot.isMarked()) {
		// if (streakSymbol != '\0') {
		// if (streakSymbol == slot.getCurrentSymbol()) {
		// markedStreak++;
		// } else {
		// markedStreak = 0;
		// }
		// } else {
		// streakSymbol = slot.getCurrentSymbol();
		// markedStreak++;
		// }
		// streakSymbol = slot.getCurrentSymbol();
		// if (markedStreak == 3) {
		// return true;
		// }
		// } else {
		// markedStreak = 0;
		// }
		// // If reached a new row
		// if (slotCount == 3) {
		// slotCount = 0;
		// markedStreak = 0;
		// }
		// }

		// Horizontal Wins
		slotSym1 = slots.get(0).getCurrentSymbol();
		slotSym2 = slots.get(1).getCurrentSymbol();
		slotSym3 = slots.get(2).getCurrentSymbol();
		if (checkBoardConnections(slotSym1, slotSym2, slotSym3))
			return true;

		slotSym1 = slots.get(3).getCurrentSymbol();
		slotSym2 = slots.get(4).getCurrentSymbol();
		slotSym3 = slots.get(5).getCurrentSymbol();
		if (checkBoardConnections(slotSym1, slotSym2, slotSym3))
			return true;

		slotSym1 = slots.get(6).getCurrentSymbol();
		slotSym2 = slots.get(7).getCurrentSymbol();
		slotSym3 = slots.get(8).getCurrentSymbol();
		if (checkBoardConnections(slotSym1, slotSym2, slotSym3))
			return true;

		// Vertical Wins
		slotSym1 = slots.get(0).getCurrentSymbol();
		slotSym2 = slots.get(3).getCurrentSymbol();
		slotSym3 = slots.get(6).getCurrentSymbol();
		if (checkBoardConnections(slotSym1, slotSym2, slotSym3))
			return true;

		slotSym1 = slots.get(1).getCurrentSymbol();
		slotSym2 = slots.get(4).getCurrentSymbol();
		slotSym3 = slots.get(7).getCurrentSymbol();
		if (checkBoardConnections(slotSym1, slotSym2, slotSym3))
			return true;

		slotSym1 = slots.get(2).getCurrentSymbol();
		slotSym2 = slots.get(5).getCurrentSymbol();
		slotSym3 = slots.get(8).getCurrentSymbol();
		if (checkBoardConnections(slotSym1, slotSym2, slotSym3))
			return true;

		// Diagonal Wins
		slotSym1 = slots.get(0).getCurrentSymbol();
		slotSym2 = slots.get(4).getCurrentSymbol();
		slotSym3 = slots.get(8).getCurrentSymbol();
		if (checkBoardConnections(slotSym1, slotSym2, slotSym3))
			return true;
		slotSym1 = slots.get(2).getCurrentSymbol();
		slotSym2 = slots.get(4).getCurrentSymbol();
		slotSym3 = slots.get(6).getCurrentSymbol();
		if (checkBoardConnections(slotSym1, slotSym2, slotSym3))
			return true;

		// If no win, check if all marked to see if tie
		int markedCount = 0;
		for (BoardSlot boardSlot : board.values()) {
			if (boardSlot.isMarked()) {
				markedCount++;
			}
		}

		if (markedCount == 9) {
			tie = true;
			return true;
		}
		return false;
	}

	private boolean checkBoardConnections(char slotSym1, char slotSym2, char slotSym3) {
		if (slotSym1 == slotSym2 && slotSym2 == slotSym3) {
			return true;
		}

		return false;
	}

	@Override
	public void update(float delta) {
		if (!finished) {
			if (aiTurn) {
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
		sendMessage("ENDING!");
	}

	private void sendCurrentTurnMsg() {
		sendMessage("It is " + currentPlayer.getAsMention() + "'s turn!");
	}

	private void sendGameMessage(String message) {
		String msg = "```xl\n" + message + "\n```";
		sendMessage(msg);
	}

	private void showBoard() {
		String boardString = "";

		int slotCount = 0;

		// for (int row = 0; row < board.length; row++) {
		// for (int column = 0; column < board[row].length; column++) {
		// slotCount++;
		// boardString = boardString + board[row][column];
		//
		// if (slotCount == 3) {
		// slotCount = 0;
		// boardString = boardString + "\n-+-+-+-+-\n";
		// } else {
		// boardString = boardString + " : ";
		// }
		//
		// }
		// }

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
		// board = new char[3][3];
		final int boardSize = 9;

		// int totalColumns = 0;
		// for (int row = 0; row < board.length; row++) {
		// for (int column = 0; column < board[row].length; column++) {
		// totalColumns++;
		// board[row][column] = Integer.toString(totalColumns).charAt(0);
		// }
		// }
		//
		int totalSlots = 0;
		for (int currentSlot = 0; currentSlot < boardSize; currentSlot++) {
			totalSlots++;
			board.put(totalSlots + "", new BoardSlot(Integer.toString(totalSlots).charAt(0)));
		}

		// board.put((currentSlot + 1) + "", new BoardSlot((currentSlot + 1)
		// + ""));
	}

	private void sendGameSummary() {
		if (tie) {
			sendMessage("The match was a tie! Would you like to play again? (type !game playagain)");
		} else {
			if (currentPlayer.getUsername().equals(p1Name)) {
				p1Score++;
			} else {
				p2Score++;
			}
			sendMessage("Player " + currentPlayer.getAsMention()
					+ " has won the match!\nWould you like to play again? (type !game playagain)");
		}
		sendMessage(p1Name + " wins:" + p1Score + " | " + p2Name + " wins: " + p2Score);
	}

	private void startFresh() {
		aiTurn = false;
		aiCurrentDelay = 0;
		finished = false;
		tie = false;

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

		if (player1 == Init.getJDA().getSelfInfo()) {
			aiTurn = true;
		}

		sendCurrentTurnMsg();
	}

	@Override
	public void gameStart() {
		startFresh();
	}

	@Override
	public void gameHelpInfo() {
		sendMessage("Get 3 consecutive X's or O's to win!\nTo play, use !game [slot numer] . Example: !game 5");
	}

}
