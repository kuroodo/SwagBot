package kuroodo.discordbot.games.battleship;

import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.core.entities.Member;

public class BattleShipNewGameSetup {
	private BattleShip battleShipSession;

	private ArrayList<String> availableSlots;

	private EShipClass currentShip;
	private int currentShipSlotCount = 0;
	private String currentShipFirstSlot;
	private String lastSlot;

	private Member member;

	private boolean isSetupComplete = false, isFirstShipSlot = true, horizontal = false, diagonal = false;

	public BattleShipNewGameSetup(Member member, BattleShip battleShipSession) {
		this.battleShipSession = battleShipSession;
		this.member = member;

		availableSlots = new ArrayList<>();

		// for (int index = 1; index <= BSData.CARRIER_HEALTH; index++) {
		// battleShipSession.getPlayerBoard(member).addShip(EShipClass.CARRIER,
		// "A" + index);
		// }
		//
		// for (int index = 1; index <= BSData.BATTLESHIP_HEALTH; index++) {
		// battleShipSession.getPlayerBoard(member).addShip(EShipClass.BATTLESHIP,
		// "B" + index);
		// }
		//
		// for (int index = 1; index <= BSData.SUBMARINE_HEALTH; index++) {
		// battleShipSession.getPlayerBoard(member).addShip(EShipClass.SUBMARIINE,
		// "C" + index);
		// }
		//
		// for (int index = 1; index <= BSData.CRUISER_HEALTH; index++) {
		// battleShipSession.getPlayerBoard(member).addShip(EShipClass.CRUISER,
		// "D" + index);
		// }
		//
		// for (int index = 1; index <= BSData.DESTROYER_HEALTH; index++) {
		// battleShipSession.getPlayerBoard(member).addShip(EShipClass.DESTROYER,
		// "E" + index);
		// }
		//
		// isSetupComplete = true;
		// String message = "**All done! Now waiting for the other player to
		// finish setting up their board**";
		// battleShipSession.sendPrivateMessage(member, message);
		// battleShipSession.setPlayerReady(member);
	}

	public void printOpening() {
		String message = "Welcome to battleship!\nPlease refer to your specified gamesession in the servers text channels.\n\nLet's set up your game board!\n";
		battleShipSession.sendPrivateMessage(member, message);

		currentShip = EShipClass.CARRIER;
		currentShipSlotCount = BSData.CARRIER_HEALTH;
		message = "```\nCurrent Ship: " + currentShip.name() + "\nSlots remaining: " + currentShipSlotCount
				+ "\n```\ntype !game <gridlocation> to enter the next grid location for the ship! (ex: !game B5)";
		battleShipSession.sendPrivateMessage(member, message);
	}

	private void printNextShipInfo() {
		String message = "**Moving On To Next Ship**\n```\nCurrent Ship: " + currentShip.name() + "\nSlots remaining: "
				+ currentShipSlotCount
				+ "\n```\ntype !game <gridlocation> to enter the next grid location for the ship! (ex: !game B5)";

		battleShipSession.sendPrivateMessage(member, message);
	}

	public void recieveInput(String input) {
		String message = "";

		if (!isSetupComplete) {
			if (isValidShipPlacement(input)) {

				addNewSlot(input);

				message = battleShipSession.getPlayerBoard(member).getBoardAsString();
				message = message + "```\nCurrent Ship: " + currentShip.name() + "\nSlots remaining: "
						+ currentShipSlotCount
						+ "\n```\ntype !game <gridlocation> to enter the next grid location for the ship! (ex: !game B5)";
				battleShipSession.sendPrivateMessage(member, message);

				if (currentShipSlotCount == 0) {

					// Check if is final ship
					if (currentShip != EShipClass.DESTROYER) {
						setNextShip();
						isFirstShipSlot = true;
						diagonal = false;
						horizontal = false;
						currentShipSlotCount = getCurrentShipTotalSlots();
						printNextShipInfo();
					} else {
						isSetupComplete = true;
						message = "**All done! Now waiting for the other player to finish setting up their board**";
						battleShipSession.sendPrivateMessage(member, message);
						battleShipSession.setPlayerReady(member);
					}
				}
			} else {
				System.out.println("INVALID SLOT");
			}
		}
	}

	private void addNewSlot(String slot) {
		battleShipSession.getPlayerBoard(member).addShip(currentShip, slot);
		currentShipSlotCount--;
	}

	private boolean isValidShipPlacement(String input) {
		// TODO: Finish this

		if (!battleShipSession.getPlayerBoard(member).isValidSlot(input)) {
			return false;
		}

		if (!battleShipSession.getPlayerBoard(member).isSlotEmpty(input)) {
			return false;
		}

		// If just adding first slot for current ship
		if (isFirstShipSlot) {
			isFirstShipSlot = false;
			currentShipFirstSlot = input;
			lastSlot = input;
			return true;
		}

		// If is second or later ship slot
		if (!isFirstShipSlot) {
			return configureLaterSlots(input);
		}

		return false;

	}

	private boolean configureLaterSlots(String input) {

		List<String> keys = new ArrayList<String>(battleShipSession.getPlayerBoard(member).getBoard().keySet());
		BSGameBoard playersBoard = battleShipSession.getPlayerBoard(member);

		try {
			if (horizontal) {
				return (playersBoard.getBoard().get(keys.indexOf(input) - 10) == BSData.getShipSymbol(currentShip)
						|| playersBoard.getBoard().get(keys.indexOf(input) + 10) == BSData.getShipSymbol(currentShip));

			} else if (diagonal) {
				return (playersBoard.getBoard().get(keys.indexOf(input) + 1) == BSData.getShipSymbol(currentShip)
						|| playersBoard.getBoard().get(keys.indexOf(input) - 1) == BSData.getShipSymbol(currentShip));

			} else {
				if (keys.get(keys.indexOf(input) - 10) == keys.get(keys.indexOf(currentShipFirstSlot))
						|| keys.get(keys.indexOf(input) + 10) == keys.get(keys.indexOf(currentShipFirstSlot))) {
					diagonal = true;
				} else if (keys.get(keys.indexOf(input) - 1) == keys.get(keys.indexOf(currentShipFirstSlot))
						|| keys.get(keys.indexOf(input) + 1) == keys.get(keys.indexOf(currentShipFirstSlot))) {
					horizontal = true;
				}

				lastSlot = input;
				return true;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}

		// List<String> keys = new
		// ArrayList<String>(battleShipSession.getPlayerBoard(member).getBoard().keySet());
		//
		// if (horizontal) {
		// if(battleShipSession.getPlayerBoard(member).isValidSlot(keys.indexOf(input)))
		// } else if (diagonal) {
		//
		// } else {
		// if (keys.get(keys.indexOf(input) - 10) ==
		// keys.get(keys.indexOf(currentShipFirstSlot))
		// || keys.get(keys.indexOf(input) + 10) ==
		// keys.get(keys.indexOf(currentShipFirstSlot))) {
		// diagonal = true;
		// horizontal = false;
		// configureLaterSlots(input);
		// } else if (keys.get(keys.indexOf(input) - 1) ==
		// keys.get(keys.indexOf(currentShipFirstSlot))
		// || keys.get(keys.indexOf(input) + 1) ==
		// keys.get(keys.indexOf(currentShipFirstSlot))) {
		// horizontal = true;
		// diagonal = false;
		// configureLaterSlots(input);
		// }
		// }
	}

	private void setFirstSlots(String input) {

		// List<String> keys = new
		// ArrayList<String>(battleShipSession.getPlayerBoard(member).getBoard().keySet());

		//
		// availableSlots.clear();
		//
		// // Left
		// if
		// (battleShipSession.getPlayerBoard(member).isValidSlot(keys.get(keys.indexOf(input)
		// - 1))) {
		// System.out.println("Left slot is valid");
		// System.out.println("Key: " + keys.get(keys.indexOf(input) - 1));
		//
		// availableSlots.add(keys.get(keys.indexOf(input) - 1));
		// }
		//
		// // Right
		// if
		// (battleShipSession.getPlayerBoard(member).isValidSlot(keys.get(keys.indexOf(input)
		// + 1))) {
		// System.out.println("Right slot is valid");
		// System.out.println("Key: " + keys.get(keys.indexOf(input) + 1));
		//
		// availableSlots.add(keys.get(keys.indexOf(input) + 1));
		// }
		//
		// // Up
		// if
		// (battleShipSession.getPlayerBoard(member).isValidSlot(keys.get(keys.indexOf(input)
		// - 10))) {
		// System.out.println("Up slot is valid");
		// System.out.println("Key: " + keys.get(keys.indexOf(input) - 10));
		//
		// availableSlots.add(keys.get(keys.indexOf(input) - 10));
		// }
		//
		// // Down
		// if
		// (battleShipSession.getPlayerBoard(member).isValidSlot(keys.get(keys.indexOf(input)
		// + 10))) {
		// System.out.println("Down slot is valid");
		// System.out.println("Key: " + keys.get(keys.indexOf(input) + 10));
		//
		// availableSlots.add(keys.get(keys.indexOf(input) + 10));
		// }
	}

	private Integer getCurrentShipTotalSlots() {
		switch (currentShip) {
		case CARRIER:
			return BSData.CARRIER_HEALTH;
		case BATTLESHIP:
			return BSData.BATTLESHIP_HEALTH;
		case SUBMARIINE:
			return BSData.SUBMARINE_HEALTH;
		case CRUISER:
			return BSData.CRUISER_HEALTH;
		case DESTROYER:
			return BSData.DESTROYER_HEALTH;
		default:
			return -1;
		}
	}

	private void setNextShip() {
		switch (currentShip) {
		case CARRIER:
			currentShip = EShipClass.BATTLESHIP;
			break;
		case BATTLESHIP:
			currentShip = EShipClass.SUBMARIINE;
			break;
		case SUBMARIINE:
			currentShip = EShipClass.CRUISER;
			break;
		case CRUISER:
			currentShip = EShipClass.DESTROYER;
			break;
		default:
			break;
		}
	}

}