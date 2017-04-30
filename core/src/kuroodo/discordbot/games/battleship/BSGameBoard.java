package kuroodo.discordbot.games.battleship;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class BSGameBoard {
	private final LinkedHashMap<String, Character> board = new LinkedHashMap<>();
	private final HashMap<String, Character> carrier = new HashMap<>();
	private final HashMap<String, Character> battleShip = new HashMap<>();
	private final HashMap<String, Character> cruiser = new HashMap<>();
	private final HashMap<String, Character> submarine = new HashMap<>();
	private final HashMap<String, Character> destroyer = new HashMap<>();

	private int carrierHealth, battleshipHealth, cruiserHealth, submarineHealth, destroyerHealth;

	public BSGameBoard() {
		makeFreshBoard();
		resetShipHealth();
	}

	public void makeFreshBoard() {
		board.clear();

		for (char alphabet = 'A'; alphabet <= 'J'; alphabet++) {
			for (int numIndex = 1; numIndex <= 10; numIndex++) {
				board.put(String.valueOf(alphabet) + numIndex, '~');
			}
		}
	}

	public void resetShipHealth() {
		carrierHealth = BSData.CARRIER_HEALTH;
		battleshipHealth = BSData.BATTLESHIP_HEALTH;
		cruiserHealth = BSData.CRUISER_HEALTH;
		submarineHealth = BSData.SUBMARINE_HEALTH;
		destroyerHealth = BSData.DESTROYER_HEALTH;
	}

	public void addShip(EShipClass shipClass, String gridLocation) {
		switch (shipClass) {
		case CARRIER:
			board.put(gridLocation, BSData.CARRIER_MARKER);
			break;
		case BATTLESHIP:
			board.put(gridLocation, BSData.BATTLESHIP_MARKER);
			break;
		case SUBMARIINE:
			board.put(gridLocation, BSData.SUBARMINE_MARKER);
			break;
		case CRUISER:
			board.put(gridLocation, BSData.CRUISER_MARKER);
			break;
		case DESTROYER:
			board.put(gridLocation, BSData.DESTROYER_MARKER);
			break;
		}
	}

	public void markSlot(String slot) {
		// System.out.println(carrierHealth);
		// System.out.println(battleshipHealth);
		// System.out.println(cruiserHealth);
		// System.out.println(submarineHealth);
		// System.out.println(destroyerHealth);

		if (isSlotEmpty(slot)) {
			board.replace(slot, BSData.MISS_MARKER);
		} else if (!isSlotHit(slot) && !isSlotMiss(slot)) {
			switch (getShipFromMarker(slot)) {
			case CARRIER:
				carrierHealth--;
				board.replace(slot, BSData.HIT_MARKER);
				break;
			case BATTLESHIP:
				battleshipHealth--;
				board.replace(slot, BSData.HIT_MARKER);
				break;
			case SUBMARIINE:
				submarineHealth--;
				board.replace(slot, BSData.HIT_MARKER);
				break;
			case CRUISER:
				cruiserHealth--;
				board.replace(slot, BSData.HIT_MARKER);
				break;
			case DESTROYER:
				destroyerHealth--;
				board.replace(slot, BSData.HIT_MARKER);
				break;
			}
		}
	}

	public void replaceSlotMarker(String slot, char marker) {
		board.replace(slot, marker);
	}

	public boolean isFleetDestroyed() {
		return (carrierHealth == 0 && battleshipHealth == 0 && cruiserHealth == 0 && submarineHealth == 0
				&& destroyerHealth == 0);
	}

	private EShipClass getShipFromMarker(String slot) {
		switch (board.get(slot)) {
		case BSData.CARRIER_MARKER:
			return EShipClass.CARRIER;
		case BSData.BATTLESHIP_MARKER:
			return EShipClass.BATTLESHIP;
		case BSData.SUBARMINE_MARKER:
			return EShipClass.SUBMARIINE;
		case BSData.CRUISER_MARKER:
			return EShipClass.CRUISER;
		case BSData.DESTROYER_MARKER:
			return EShipClass.DESTROYER;
		default:
			return null;
		}
	}

	public String getBoardAsString() {
		String message = "```\n-\t1\t2\t3\t4\t5\t6\t7\t8\t9\t10\t";

		for (char alphabet = 'A'; alphabet <= 'J'; alphabet++) {
			message = message + "\n" + alphabet + "\t";
			for (int numIndex = 1; numIndex <= 10; numIndex++) {
				message = message + board.get(String.valueOf(alphabet) + numIndex) + "\t";
			}
		}

		message = message + "\n```";
		return message;
	}

	public boolean isValidSlot(String boardSlot) {
		return board.containsKey(boardSlot);
	}

	public boolean isValidIndex(int index) {
		return ((index >= 0) || (index <= board.size()));
	}

	public Character getBoardSlot(String boardSlot) {
		return board.get(boardSlot);
	}

	public LinkedHashMap<String, Character> getBoard() {
		return board;
	}

	public boolean isSlotHit(String slot) {
		return board.get(slot) == BSData.HIT_MARKER;
	}

	public boolean isSlotMiss(String slot) {
		return board.get(slot) == BSData.MISS_MARKER;
	}

	public boolean isSlotEmpty(String slot) {
		return board.get(slot) == BSData.WATER_MARKER;
	}
}