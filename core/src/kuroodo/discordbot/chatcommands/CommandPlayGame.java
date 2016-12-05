package kuroodo.discordbot.chatcommands;

import kuroodo.discordbot.Init;
import kuroodo.discordbot.client.handlers.GlobalGameManager;
import kuroodo.discordbot.entities.ChatCommand;
import kuroodo.discordbot.enums.EGameList;
import kuroodo.discordbot.helpers.JDAHelper;
import kuroodo.discordbot.listeners.GameListener;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class CommandPlayGame extends ChatCommand {

	// TODO: Allow user to specify more than 1 players

	@Override
	public void executeCommand(String commandParameters, GuildMessageReceivedEvent event) {
		super.executeCommand(commandParameters, event);

		if (GlobalGameManager.isUserInGameSession(event.getMember())) {
			sendPrivateMessage("You are already in a game session!");
			return;
		}
		// Did user specify anything after !command?
		if (!JDAHelper.splitString(commandParameters)[0].isEmpty()) {

			String gameName = JDAHelper.splitString(commandParameters)[0].toLowerCase();

			if (!isValidGameName(gameName)) {
				sendPrivateMessage("The game you specified does not exist!");
				return;
			}

			// Check if user specified a user to play with
			if (!JDAHelper.splitString(commandParameters)[1].isEmpty()) {

				// Check if the specified user is a real user
				Member multiPlayerUser = findMultiplayerUser(JDAHelper.splitString(commandParameters)[1]);

				if (multiPlayerUser != null && !GlobalGameManager.isUserInGameSession(multiPlayerUser)) {
					if (multiPlayerUser == JDAHelper.getMember(Init.getJDA().getSelfUser())) {
						sendPrivateMessage("To play alone against " + Init.getBotName() + " type: !playgame gamename");
					} else {
						loadMP(gameName, multiPlayerUser);
					}
				} else {
					sendPrivateMessage("That user is already in a session or doesn't exist!");
				}
			} else {
				loadSP(gameName);
			}
		}
	}

	// Multiplayer
	private void loadMP(String gameName, Member multiPlayerUser) {
		GameListener newGameListener;

		newGameListener = new GameListener(gameName, JDAHelper.getMember(event.getAuthor()), multiPlayerUser,
				GlobalGameManager.gameListeners.size(), true);

		Init.getJDA().addEventListener(newGameListener);
		Init.getListeners().add(newGameListener);
	}

	// Singleplayer
	private void loadSP(String gameName) {
		GameListener newGameListener = new GameListener(gameName, event.getMember(),
				JDAHelper.getMember(Init.getJDA().getSelfUser()), GlobalGameManager.gameListeners.size(), false);

		Init.getJDA().addEventListener(newGameListener);
		Init.getListeners().add(newGameListener);
	}

	private boolean isValidGameName(String gameName) {
		for (EGameList e : EGameList.values()) {
			if (e.name().toLowerCase().equals(gameName)) {
				return true;
			}
		}
		return false;
	}

	private Member findMultiplayerUser(String sUserToFind) {
		Member userToFind = JDAHelper.getMemberByID(sUserToFind);

		if (userToFind == null) {
			userToFind = JDAHelper.getMemberByUsername(sUserToFind);
			if (userToFind == null) {
				if (!event.getMessage().getMentionedUsers().isEmpty()) {
					userToFind = JDAHelper.getMember(event.getMessage().getMentionedUsers().get(0));
				}
			}
		}
		return userToFind;
	}

	@Override
	public String info() {
		String gameList = "";
		for (EGameList e : EGameList.values()) {
			gameList = gameList + " " + e.name().toLowerCase() + ";";
		}

		return "Play a game! " + "To play alone against " + Init.getBotName() + " type: !playgame gamename\n"
				+ "To play with another player: !playgame gamename playername" + "\nGameList:\n" + gameList
				+ "\nMore games to come soon!";
	}
}