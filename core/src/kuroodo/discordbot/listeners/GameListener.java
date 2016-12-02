package kuroodo.discordbot.listeners;

import java.util.ArrayList;

import kuroodo.discordbot.Init;
import kuroodo.discordbot.chatcommands.moderator.CommandClearChat;
import kuroodo.discordbot.client.handlers.GlobalGameManager;
import kuroodo.discordbot.entities.GameSession;
import kuroodo.discordbot.entities.JDAListener;
import kuroodo.discordbot.games.ExampleGame;
import kuroodo.discordbot.games.tictactoe.GameTicTacToe;
import kuroodo.discordbot.helpers.JDAHelper;
import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.managers.ChannelManager;
import net.dv8tion.jda.managers.GuildManager;
import net.dv8tion.jda.managers.RoleManager;

public class GameListener extends JDAListener {
	private TextChannel gameChannel;
	private GameSession gameSession;

	private int sessionID;

	private ArrayList<User> players;
	private boolean isMP = false;
	private String gameName;

	public GameListener(String gameName, User playerOne, User playerTwo, int sessionId, boolean isMP) {
		this.sessionID = sessionId;
		this.gameName = gameName;
		this.isMP = isMP;
		// System.out.println("Bot: " + playerOne.getUsername());
		// System.out.println("Self: " + playerTwo.getUsername());

		players = new ArrayList<>();
		players.add(playerOne);
		players.add(playerTwo);

		GlobalGameManager.addGameListener("session" + sessionID, this);

		setUpGameChannel(sessionId);

		if (isMP) {
			final String message = playerTwo.getAsMention() + "You have been challanged to a game of " + gameName
					+ " by " + playerOne.getAsMention() + "\n type !accept to join!";

			sendMessage(message);
		} else {
			startSPSession();
		}
	}

	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		super.onGuildMessageReceived(event);

		if (event.getAuthor() != null) {
			if (event.getChannel() == gameChannel) {
				if (event.getMessage().getRawContent().startsWith("!end") && players.contains(event.getAuthor())) {
					endGameSession();
					return;

				} else if (event.getMessage().getRawContent().startsWith("!gamehelp") && gameSession != null) {
					gameSession.gameHelpInfo();
					return;
				} else {
					sendGameInput(event.getAuthor(), event.getMessage().getRawContent(), event.getMessage());
				}
			}
		}
	}

	private void sendGameInput(User sender, String message, Message eventMessage) {
		if (gameSession != null) {
			if (message.startsWith("!game")) {
				// send everything after !game
				gameSession.recievePlayerInput(sender, JDAHelper.splitString(message)[1], eventMessage);
			}
		} else if (isMP && message.equals("!accept")) {
			if (sender == players.get(1)) {
				startMPSession();
			}
		}
	}

	public void update(float delta) {
		if (gameSession != null) {
			gameSession.update(delta);
		}
	}

	private GameSession getNewGameInstance(String gameName, boolean multiplayer) {
		// TODO: Use EGameList and think of better way to store and get the
		// gameInstance
		switch (gameName.toLowerCase()) {
		case "examplegame":
			return new ExampleGame(players, multiplayer, gameChannel);
		case "tictactoe":
			return new GameTicTacToe(players, multiplayer, gameChannel);
		default:
			return null;
		}
	}

	private void setUpGameChannel(int sessionId) {
		final ChannelManager chManager = JDAHelper.getGuild().createTextChannel("gamesession_" + sessionId);
		chManager.update();

		setUpChannelPermissions(chManager);

		gameChannel = (TextChannel) chManager.getChannel();
	}

	private void setUpChannelPermissions(ChannelManager chManager) {
		Role role = JDAHelper.getRoleByName("gameroleexample");

		final RoleManager roleManager = JDAHelper.getGuild().createRole();
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

		chManager.getChannel().createPermissionOverride(JDAHelper.getRoleByName("@everyone"))
				.deny(Permission.CREATE_INSTANT_INVITE).deny(Permission.MESSAGE_WRITE).deny(Permission.MESSAGE_READ)
				.deny(Permission.MESSAGE_MANAGE).update();

		chManager.getChannel().createPermissionOverride(JDAHelper.getRoleByName("Admin"))
				.deny(Permission.MANAGE_PERMISSIONS).deny(Permission.MESSAGE_READ).update();

		final GuildManager gManager = JDAHelper.getGuild().getManager();

		for (User player : players) {
			gManager.addRoleToUser(player, sessionRole);
		}
		gManager.addRoleToUser(Init.getJDA().getSelfInfo(), sessionRole);

		gManager.update();
	}

	private void startMPSession() {
		gameSession = getNewGameInstance(gameName, true);
		deleteMessages();
		gameSession.gameStart();
	}

	private void startSPSession() {
		gameSession = getNewGameInstance(gameName, false);
		gameSession.gameStart();
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

	public void endGameSession() {
		final ChannelManager chManager = gameChannel.getManager();
		chManager.delete();
		final RoleManager roleManager = JDAHelper.getRoleByName("session" + sessionID).getManager();
		roleManager.delete();

		GlobalGameManager.removeGameListener("session" + sessionID);

		if (gameSession != null) {
			gameSession.endGame();
		}
	}

	public int getSessionID() {
		return sessionID;
	}

	public ArrayList<User> getPlayers() {
		return players;
	}
}