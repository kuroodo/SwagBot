package kuroodo.discordbot.games.battleship;

import java.util.ArrayList;
import java.util.function.Consumer;

import kuroodo.discordbot.entities.GameSession;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.TextChannel;

public class BattleShip extends GameSession {

	private BSGameBoard player1Board, player1SitBoard, player2Board, player2SitBoard;
	private BattleShipNewGameSetup p1Setup, p2Setup;
	private boolean isP1Ready = false, isP2Ready = false, isGameReady = false, isGameOver = false;

	private Member player1, player2;

	public BattleShip(ArrayList<Member> players, boolean hasMultiPLayer, TextChannel gameChannel) {
		super(players, hasMultiPLayer, gameChannel);
		player1Board = new BSGameBoard();
		player1SitBoard = new BSGameBoard();
		player2Board = new BSGameBoard();
		player2SitBoard = new BSGameBoard();

		player1 = currentPlayer;

		// Setup player 2
		for (Member player : players) {
			if (player != player1) {
				player2 = player;
				break;
			}
		}
	}

	@Override
	public void recievePlayerInput(Member playerWhoSentInput, String input, Message inputMessage) {
		super.recievePlayerInput(playerWhoSentInput, input, inputMessage);

		if (isGameReady && !isGameOver && isInputValid) {
			if (isLegalMove(input.toUpperCase())) {

				markBoard(input.toUpperCase());
				sendMoveResultMessage(input.toUpperCase());

				checkMoveResults();
				sendCurrentTurnMsg();
				if (isGameOver) {
					sendMessage("GAME OVER!");
				} else {
					rotatePlayers();
				}
			} else {
				sendMessage("**ERROR: Slot does not exist or has already been hit/missed**");
			}
		}
	}

	@Override
	public void recievePrivatePlayerInput(Member playerWhoSentInput, String input, Message inputMessage) {
		super.recievePrivatePlayerInput(playerWhoSentInput, input, inputMessage);

		if (!isGameReady) {
			if (player1 == playerWhoSentInput) {
				if (!isP1Ready) {
					p1Setup.recieveInput(input.toUpperCase());
				}
			} else {
				if (!isP2Ready) {
					p2Setup.recieveInput(input.toUpperCase());
				}
			}
		}
	}

	private void rotatePlayers() {
		if (currentPlayer == player1) {
			currentPlayer = player2;
		} else {
			currentPlayer = player1;
		}

		// if (!isMultiplayer) {
		// if (isAITurn) {
		// isAITurn = false;
		// } else {
		// isAITurn = true;
		// }
		// }
		//
		// if (currentSymbol == 'X') {
		// currentSymbol = 'O';
		// } else {
		// currentSymbol = 'X';
		// }
	}

	private void checkMoveResults() {
		if (currentPlayer == player1) {
			isGameOver = player2Board.isFleetDestroyed();
		} else {
			isGameOver = player1Board.isFleetDestroyed();
		}
	}

	private void markBoard(String input) {
		if (player1 == currentPlayer) {
			player2Board.markSlot(input);
			player1SitBoard.replaceSlotMarker(input, player2Board.getBoardSlot(input));
		} else {
			player1Board.markSlot(input);
			player2SitBoard.replaceSlotMarker(input, player1Board.getBoardSlot(input));
		}
	}

	private boolean isLegalMove(String input) {
		if (getPlayerSitatuonBoard(currentPlayer).isValidSlot(input)) {
			return (!getPlayerSitatuonBoard(currentPlayer).isSlotHit(input)
					&& !getPlayerSitatuonBoard(currentPlayer).isSlotMiss(input));
		}
		return false;
	}

	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub

	}

	@Override
	public void endGame() {
		// TODO Auto-generated method stub

	}

	public void sendPrivateMessage(Member member, String message) {
		if (member.getUser().hasPrivateChannel()) {
			member.getUser().getPrivateChannel().sendMessage(message).queue();

		} else {
			member.getUser().openPrivateChannel().queue(new Consumer<PrivateChannel>() {

				@Override
				public void accept(PrivateChannel t) {
					t.sendMessage(message).queue();
				}
			});
		}

	}

	private void onBothPlayersReady() {
		sendMessage(
				"Welcome to battleship!\n All game input will be typed and shown here. Your boards will be shown in your individual inboxes\nUse !game [slot] to make your move (i.e !game A5");
		isGameReady = true;
		sendCurrentTurnMsg();
	}

	private void sendMoveResultMessage(String input) {
		if (currentPlayer == player1) {
			if (player2Board.isSlotHit(input)) {
				sendMessage(currentPlayer.getAsMention() + " inputted **" + input + "** and landed a **hit**!");
			} else if (player2Board.isSlotMiss(input)) {
				sendMessage(currentPlayer.getAsMention() + " inputted **" + input + "** and  **missed**!");
			}
		} else {
			if (player1Board.isSlotHit(input)) {
				sendMessage(currentPlayer.getAsMention() + " inputted **" + input + "** and landed a **hit**!");
			} else if (player1Board.isSlotMiss(input)) {
				sendMessage(currentPlayer.getAsMention() + " inputted **" + input + "** and  **missed**!");
			}
		}
	}

	private void sendCurrentTurnMsg() {
		sendMessage("It is " + currentPlayer.getAsMention() + "'s turn!");

		sendPrivateMessage(player1, "**X** = Hit , **#** = MISS , **" + BSData.CARRIER_MARKER + "** = Carrier , **"
				+ BSData.BATTLESHIP_MARKER + "** = Battleship , **" + BSData.SUBARMINE_MARKER + "** = Submarine , **"
				+ BSData.CRUISER_MARKER + "** = Cruiser , **" + BSData.DESTROYER_MARKER + "** = Destroyer");

		sendPrivateMessage(player1, "**This is your board**");
		sendPrivateMessage(player1, player1Board.getBoardAsString());
		sendPrivateMessage(player1, "**This is the other players Board**");
		sendPrivateMessage(player1, player1SitBoard.getBoardAsString());

		
		sendPrivateMessage(player2, "**X** = Hit , **#** = MISS , **" + BSData.CARRIER_MARKER + "** = Carrier , **"
				+ BSData.BATTLESHIP_MARKER + "** = Battleship , **" + BSData.SUBARMINE_MARKER + "** = Submarine , **"
				+ BSData.CRUISER_MARKER + "** = Cruiser , **" + BSData.DESTROYER_MARKER + "** = Destroyer");

		sendPrivateMessage(player2, "**X** = Hit , **#** = MISS");
		sendPrivateMessage(player2, "**This is your board**");
		sendPrivateMessage(player2, player2Board.getBoardAsString());
		sendPrivateMessage(player2, "**This is the other players Board**");
		sendPrivateMessage(player2, player2SitBoard.getBoardAsString());

	}

	@Override
	public void gameStart() {
		p1Setup = new BattleShipNewGameSetup(player1, this);
		p1Setup.printOpening();
		p2Setup = new BattleShipNewGameSetup(player2, this);
		p2Setup.printOpening();
	}

	public BSGameBoard getPlayerBoard(Member member) {
		if (player1 == member) {
			return player1Board;
		} else {
			return player2Board;
		}
	}

	public BSGameBoard getPlayerSitatuonBoard(Member member) {
		if (player1 == member) {
			return player1SitBoard;
		} else {
			return player2SitBoard;
		}
	}

	public void setPlayerReady(Member member) {
		if (player1 == member) {
			isP1Ready = true;
		} else {
			isP2Ready = true;
		}

		if (isP1Ready && isP2Ready) {
			onBothPlayersReady();
		}
	}

	@Override
	public void gameHelpInfo() {
		// TODO Auto-generated method stub

	}

}