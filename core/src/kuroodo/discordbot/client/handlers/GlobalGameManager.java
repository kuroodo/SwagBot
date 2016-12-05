package kuroodo.discordbot.client.handlers;

import java.util.HashMap;

import kuroodo.discordbot.Init;
import kuroodo.discordbot.listeners.GameListener;
import net.dv8tion.jda.core.entities.Member;

public class GlobalGameManager {
	public static final HashMap<String, GameListener> gameListeners = new HashMap<>();

	public static void addGameListener(String sessionName, GameListener session) {
		gameListeners.put(sessionName, session);
	}

	public static void removeGameListener(String sessionName) {
		Init.getListeners().remove(gameListeners.get(sessionName));
		gameListeners.remove(sessionName);
	}

	public static void endGameListenerSession(String sessionName) {
		gameListeners.get(sessionName).endGameSession();
	}

	public static void endAllGameListenerSession() {
		for (GameListener listener : gameListeners.values()) {
			listener.endGameSession();
		}
	}

	public static boolean isUserInGameSession(Member user) {
		for (GameListener manager : gameListeners.values()) {
			for (Member player : manager.getPlayers()) {
				if (player == user) {
					return true;
				}
			}
		}
		return false;
	}
}