package kuroodo.discordbot.games.battleship;

public class BSData {
	public static final char WATER_MARKER = '~', HIT_MARKER = 'X', MISS_MARKER = '#', CARRIER_MARKER = '$',
			BATTLESHIP_MARKER = '@', CRUISER_MARKER = '*', SUBARMINE_MARKER = '%', DESTROYER_MARKER = '&';

	public static final int CARRIER_HEALTH = 5, BATTLESHIP_HEALTH = 4, CRUISER_HEALTH = 3, SUBMARINE_HEALTH = 3,
			DESTROYER_HEALTH = 2;

	public static Character getShipSymbol(EShipClass shipClass) {
		switch (shipClass) {
		case CARRIER:
			return CARRIER_MARKER;
		case BATTLESHIP:
			return BATTLESHIP_MARKER;
		case CRUISER:
			return CRUISER_MARKER;
		case SUBMARIINE:
			return SUBARMINE_MARKER;
		case DESTROYER:
			return DESTROYER_MARKER;
		default:
			return 'e';
		}
	}
}