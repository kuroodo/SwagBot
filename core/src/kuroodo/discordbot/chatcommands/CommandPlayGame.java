package kuroodo.discordbot.chatcommands;

import kuroodo.discordbot.Init;
import kuroodo.discordbot.client.handlers.GameManager;
import kuroodo.discordbot.client.handlers.GameManagerHandler;
import kuroodo.discordbot.entities.ChatCommand;
import kuroodo.discordbot.enums.EGameList;
import kuroodo.discordbot.helpers.JDAHelper;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

public class CommandPlayGame extends ChatCommand {

	@Override
	public void executeCommand(String commandParameters, GuildMessageReceivedEvent event) {
		super.executeCommand(commandParameters, event);

		if (GameManagerHandler.isUserInASession(event.getAuthor())) {
			// TODO: Add game session channel name to message
			sendPrivateMessage("You are already in a game session!");
			return;
		}
		// Did user specify anything after command?
		if (!JDAHelper.splitString(commandParameters)[0].isEmpty()) {

			String gameName = JDAHelper.splitString(commandParameters)[0];

			if (!doesGameExist(gameName)) {
				sendPrivateMessage("The game you specified does not exist!");
				return;
			}

			// Check if user specified a user to player with
			if (!JDAHelper.splitString(commandParameters)[1].isEmpty()) {

				// Check if the specified user is a real user
				User multiPlayerUser = JDAHelper.getUserByID(JDAHelper.splitString(commandParameters)[1]);

				if (multiPlayerUser == null) {
					multiPlayerUser = JDAHelper.getUserByUsername(commandParameters);
					if (multiPlayerUser == null) {
						if (!event.getMessage().getMentionedUsers().isEmpty()) {
							multiPlayerUser = event.getMessage().getMentionedUsers().get(0);
						}
					}
				}
				// If user was found check if theyre not already in a game session
				if (multiPlayerUser != null && !GameManagerHandler.isUserInASession(multiPlayerUser)) {
					if (multiPlayerUser == Init.getJDA().getSelfInfo()) {
						sendPrivateMessage("To play alone against " + Init.getBotName() + ": !playgame gamename");
					} else {
						loadMP(gameName, multiPlayerUser);
					}
					// // Multiplayer
					// gameManager = new GameManager(gameName,
					// event.getAuthor(), multiPlayerUser,
					// GameManagerHandler.gameSessions.size(), true);
					//
					// Init.getJDA().addEventListener(gameManager);
					// Init.getListeners().add(gameManager);

				} else {
					sendPrivateMessage("That user is already in a session or doesn't exist!");
				}
			} else {
				// // Singleplayer
				// sendMessage("Game session created! Name: session" +
				// GameManagerHandler.gameSessions.size());
				// gameManager = new GameManager(gameName, event.getAuthor(),
				// Init.getJDA().getSelfInfo(),
				// GameManagerHandler.gameSessions.size(), false);
				// Init.getJDA().addEventListener(gameManager);
				// Init.getListeners().add(gameManager);

				loadSP(gameName);

			}

		}
	}

	private void loadMP(String gameName, User multiPlayerUser) {
		GameManager gameManager;

		// Multiplayer
		gameManager = new GameManager(gameName, event.getAuthor(), multiPlayerUser,
				GameManagerHandler.gameSessions.size(), true);

		Init.getJDA().addEventListener(gameManager);
		Init.getListeners().add(gameManager);
	}

	private void loadSP(String gameName) {
		// Singleplayer
		sendPrivateMessage("Game session created! Name: session" + GameManagerHandler.gameSessions.size());
		GameManager gameManager = new GameManager(gameName, event.getAuthor(), Init.getJDA().getSelfInfo(),
				GameManagerHandler.gameSessions.size(), false);
		Init.getJDA().addEventListener(gameManager);
		Init.getListeners().add(gameManager);
	}

	private boolean doesGameExist(String gameName) {
		for (EGameList e : EGameList.values()) {
			if (e.name().toLowerCase().equals(gameName)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public String info() {
		String gameList = "";
		for (EGameList e : EGameList.values()) {
			gameList = gameList + " " + e.name().toLowerCase() + ";";
		}

		return "Play a game! " + "To play alone against " + Init.getBotName() + ": !playgame gamename"
				+ "To play with another player: !playgame gamename playername" + "\nGameList:\n" + gameList
				+ "\nMore games to come soon!";
	}

}
