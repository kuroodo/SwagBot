package kuroodo.discordbot.client.handlers;

import java.util.HashMap;

import kuroodo.discordbot.Init;
import net.dv8tion.jda.entities.User;

public class GlobalGameManager {
	public static final HashMap<String, GameListener> gameListeners = new HashMap<>();

	public static void addGameListener(String sessionName, GameListener session) {
		gameListeners.put(sessionName, session);
	}

	public static void removeGameListener(String sessionName) {
		Init.getListeners().remove(gameListeners.get(sessionName));
		gameListeners.remove(sessionName);
	}

	public static boolean isUserInGameSession(User user) {
		for (GameListener manager : gameListeners.values()) {
			for (User player : manager.getPlayers()) {
				if (player == user) {
					return true;
				}
			}
		}
		return false;
	}
}