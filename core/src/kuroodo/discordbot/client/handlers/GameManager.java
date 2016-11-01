package kuroodo.discordbot.client.handlers;

import java.util.ArrayList;

import kuroodo.discordbot.Init;
import kuroodo.discordbot.chatcommands.moderator.CommandClearChat;
import kuroodo.discordbot.entities.GameSession;
import kuroodo.discordbot.entities.JDAListener;
import kuroodo.discordbot.games.TestGame;
import kuroodo.discordbot.games.tictactoe.GameTicTacToe;
import kuroodo.discordbot.helpers.ChatHelper;
import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.managers.ChannelManager;
import net.dv8tion.jda.managers.GuildManager;
import net.dv8tion.jda.managers.RoleManager;

public class GameManager extends JDAListener {
	private TextChannel gameChannel;
	private GameSession gameSession;

	private int sessionID;

	private ArrayList<User> players;
	private boolean isMP = false;
	private String gameName;

	public GameManager(String gameName, User playerOne, User playerTwo, int sessionId, boolean isMP) {
		this.sessionID = sessionId;
		this.gameName = gameName;
		this.isMP = isMP;

		// if (getNewGameInstance(gameName, false) == null) {
		//
		// }

		// System.out.println("Bot: " + playerOne.getUsername());
		// System.out.println("Self: " + playerTwo.getUsername());

		players = new ArrayList<>();
		players.add(playerOne);
		players.add(playerTwo);

		GameManagerHandler.addGameManager("session" + sessionID, this);

		final ChannelManager chManager = ChatHelper.getGuild().createTextChannel("gamesession_" + sessionId);
		chManager.update();

		setUpPermissions(chManager);

		gameChannel = (TextChannel) chManager.getChannel();

		if (isMP) {
			gameSession = getNewGameInstance(gameName, true);

			final String message = playerTwo.getAsMention() + "You have been challanged to a game of " + gameName
					+ " by " + playerOne.getAsMention() + "\n type !accept to join!";

			sendMessage(message);
		} else {
			gameSession = getNewGameInstance(gameName, false);
			gameSession.gameStart();
		}

	}

	// Single Player
	// public GameManager(String gameName, User playerOne, int sessionId) {
	// this.sessionID = sessionId;
	// this.gameName = gameName;
	//
	// players = new ArrayList<>();
	// players.add(playerOne);
	//
	// GameManagerHandler.addGameManager("session" + sessionID, this);
	//
	// final ChannelManager chManager =
	// ChatHelper.getGuild().createTextChannel("session" + sessionId);
	// chManager.update();
	//
	// setUpPermissions(chManager);
	//
	// gameChannel = (TextChannel) chManager.getChannel();
	//
	// gameSession = getNewGameInstance(gameName, false);
	// gameSession.gameStart();
	// }

	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		super.onGuildMessageReceived(event);

		if (event.getChannel() == gameChannel) {
			// Maybe check if author is a player?
			if (event.getMessage().getRawContent().startsWith("!end")) {
				endMatch();
				return;
			} else if (event.getMessage().getRawContent().startsWith("!gamehelp") && gameSession != null) {
				gameSession.gameHelpInfo();
				return;
			}

			sendGameInput(event.getAuthor(), event.getMessage().getRawContent(), event.getMessage());
		}

	}

	private void sendGameInput(User sender, String message, Message eventMessage) {
		if (isMP && message.equals("!accept")) {
			if (sender == players.get(1)) {
				gameSession = getNewGameInstance(gameName, true);
				deleteMessages();
				gameSession.gameStart();
			}

		} else if (message.startsWith("!game")) {
			gameSession.recievePlayerInput(sender, ChatHelper.splitString(message)[1], eventMessage);
		}
	}

	// private void singePlayerInput(User sender, String message, Message
	// eventMessage) {
	// if (message.startsWith("!game")) {
	// gameSession.recievePlayerInput(sender,
	// ChatHelper.splitString(message)[1], eventMessage);
	// }
	// }

	private GameSession getNewGameInstance(String gameName, boolean multiplayer) {
		switch (gameName.toLowerCase()) {
		case "testgame":
			return new TestGame(players, multiplayer, gameChannel);
		case "tictactoe":
			return new GameTicTacToe(players, multiplayer, gameChannel);
		default:
			return null;
		}
	}

	private void setUpPermissions(ChannelManager chManager) {
		Role role = ChatHelper.getRoleByName("gameroleexample");

		final RoleManager roleManager = ChatHelper.getGuild().createRole();
		roleManager.setName("session" + sessionID);

		for (Permission permission : roleManager.getRole().getPermissions()) {
			roleManager.revoke(permission);
		}

		for (Permission permission : role.getPermissions()) {
			roleManager.give(permission);
		}

		roleManager.update();

		Role sessionRole = roleManager.getRole();

		chManager.getChannel().createPermissionOverride(sessionRole).grant(Permission.MESSAGE_READ)
				.grant(Permission.MESSAGE_WRITE).grant(Permission.MESSAGE_TTS).update();

		chManager.getChannel().createPermissionOverride(ChatHelper.getRoleByName("@everyone"))
				.deny(Permission.CREATE_INSTANT_INVITE).deny(Permission.MESSAGE_WRITE).deny(Permission.MESSAGE_READ)
				.deny(Permission.MESSAGE_MANAGE).update();

		chManager.getChannel().createPermissionOverride(ChatHelper.getRoleByName("Admin"))
				.deny(Permission.MANAGE_PERMISSIONS).deny(Permission.MESSAGE_READ).update();

		final GuildManager gManager = ChatHelper.getGuild().getManager();

		for (User player : players) {
			gManager.addRoleToUser(player, sessionRole);
		}
		gManager.addRoleToUser(Init.getJDA().getSelfInfo(), sessionRole);

		gManager.update();
	}

	public void update(float delta) {
		if (gameSession != null) {
			gameSession.update(delta);
		}
	}

	private void sendMessage(String message) {
		if (gameChannel != null) {
			gameChannel.sendMessageAsync(message, null);
		}
	}

	private void deleteMessages() {
		CommandClearChat chatClearer = new CommandClearChat();
		chatClearer.executeCommand("all", gameChannel);
	}

	public int getSessionID() {
		return sessionID;
	}

	public ArrayList<User> getPlayers() {
		return players;
	}

	private void endMatch() {
		final ChannelManager chManager = gameChannel.getManager();
		chManager.delete();
		final RoleManager roleManager = ChatHelper.getRoleByName("session" + sessionID).getManager();
		roleManager.delete();

		GameManagerHandler.endGameManager("session" + sessionID);

		gameSession.endGame();
	}
}
