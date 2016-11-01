package kuroodo.discordbot.client.handlers;

import java.util.HashMap;

import kuroodo.discordbot.Init;
import net.dv8tion.jda.entities.User;

public class GameManagerHandler {
	public static final HashMap<String, GameManager> gameSessions = new HashMap<>();

	public static void addGameManager(String sessionName, GameManager session) {
		gameSessions.put(sessionName, session);
	}

	public static void endGameManager(String sessionName) {
		Init.getListeners().remove(gameSessions.get(sessionName));
		gameSessions.remove(sessionName);
	}

	public static boolean isUserInASession(User user) {
		for (GameManager manager : gameSessions.values()) {
			for (User player : manager.getPlayers()) {
				if (player == user) {
					return true;
				}
			}
		}

		return false;
	}
}
