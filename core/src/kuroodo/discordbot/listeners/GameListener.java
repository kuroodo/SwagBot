package kuroodo.discordbot.listeners;

import java.util.ArrayList;
import java.util.function.Consumer;

import kuroodo.discordbot.Init;
import kuroodo.discordbot.chatcommands.moderator.CommandClearChat;
import kuroodo.discordbot.client.handlers.GlobalGameManager;
import kuroodo.discordbot.entities.GameSession;
import kuroodo.discordbot.entities.JDAListener;
import kuroodo.discordbot.games.ExampleGame;
import kuroodo.discordbot.games.tictactoe.GameTicTacToe;
import kuroodo.discordbot.helpers.JDAHelper;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.PermissionOverride;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.managers.ChannelManager;
import net.dv8tion.jda.core.managers.GuildController;
import net.dv8tion.jda.core.managers.RoleManager;
import net.dv8tion.jda.core.managers.RoleManagerUpdatable;

public class GameListener extends JDAListener {
	private TextChannel gameChannel;
	private GameSession gameSession;

	private int sessionID;

	private ArrayList<Member> players;
	private boolean isMP = false, isSessionStarted = false;
	private String gameName;

	public GameListener(String gameName, Member playerOne, Member playerTwo, int sessionId, boolean isMP) {
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

	}

	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		super.onGuildMessageReceived(event);

		if (event.getAuthor() != null) {
			if (event.getChannel() == gameChannel) {
				if (event.getMessage().getRawContent().startsWith("!end") && players.contains(event.getMember())) {
					endGameSession();
					return;

				} else if (event.getMessage().getRawContent().startsWith("!gamehelp") && gameSession != null) {
					gameSession.gameHelpInfo();
					return;
				} else {
					sendGameInput(event, event.getMessage().getRawContent());
				}
			}
		}
	}

	private void sendGameInput(GuildMessageReceivedEvent event, String message) {
		if (gameSession != null) {
			if (message.startsWith("!game")) {
				// send everything after !game
				gameSession.recievePlayerInput(event.getMember(), JDAHelper.splitString(message)[1],
						event.getMessage());
			}
		} else if (isMP && message.equals("!accept")) {
			if (event.getMember() == players.get(1)) {
				startMPSession(event);
			}
		}
	}

	public void update(float delta) {

		if (!isSessionStarted) {
			if (gameChannel != null) {
				isSessionStarted = true;
				if (isMP) {
					final String message = players.get(1).getAsMention() + "You have been challanged to a game of "
							+ gameName + " by " + players.get(0).getAsMention() + "\n type !accept to join!";

					sendMessage(message);
				} else {
					startSPSession();
				}
			}
		}

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

		JDAHelper.getGuild().getController().createTextChannel("gamesession_" + sessionId)
				.queue(new Consumer<TextChannel>() {

					@Override
					public void accept(TextChannel t) {
						final ChannelManager chManager = t.getManager();

						setUpChannelPermissions(chManager);

						gameChannel = (TextChannel) chManager.getChannel();

						if (gameChannel == null) {
							System.out.println("YES");
						} else {
							System.out.println("NO");
						}
					}
				});

		if (gameChannel == null) {
			System.out.println("YES2");
		} else {
			System.out.println("NO2");
		}

	}

	private void setUpChannelPermissions(ChannelManager chManager) {
		Role role = JDAHelper.getRoleByName("gameroleexample");

		JDAHelper.getGuild().getController().createRole().queue(new Consumer<Role>() {

			@Override
			public void accept(Role t) {
				final RoleManagerUpdatable roleManager = t.getManagerUpdatable();

				roleManager.getNameField().setValue("session" + sessionID);

				// TODO: Try to use Permissions.getValues() instead
				for (Permission permission : roleManager.getRole().getPermissions()) {
					roleManager.getPermissionField().revokePermissions(permission);
				}

				for (Permission permission : role.getPermissions()) {
					roleManager.getPermissionField().givePermissions(permission);
				}

				roleManager.update().queue();

				Role sessionRole = roleManager.getRole();

				chManager.getChannel().createPermissionOverride(sessionRole).queue(new Consumer<PermissionOverride>() {

					@Override
					public void accept(PermissionOverride t) {
						t.getManager().grant(Permission.MESSAGE_READ).queue();
						// t.getManager().grant(Permission.MESSAGE_WRITE).queue();
						// t.getManager().grant(Permission.MESSAGE_TTS).queue();
					}
				});

				chManager.getChannel().createPermissionOverride(JDAHelper.getRoleByName("@everyone"))
						.queue(new Consumer<PermissionOverride>() {

							@Override
							public void accept(PermissionOverride t) {
								// t.getManager().deny(Permission.CREATE_INSTANT_INVITE).queue();
								// t.getManager().deny(Permission.MESSAGE_WRITE).queue();
								t.getManager().deny(Permission.MESSAGE_READ).queue();
								// t.getManager().deny(Permission.MESSAGE_MANAGE).queue();
							}
						});

				chManager.getChannel().createPermissionOverride(JDAHelper.getRoleByName("Admin"))
						.queue(new Consumer<PermissionOverride>() {

							@Override
							public void accept(PermissionOverride t) {
								t.getManager().deny(Permission.MANAGE_PERMISSIONS).queue();
								t.getManager().deny(Permission.MESSAGE_READ).queue();

							}
						});

				final GuildController gController = JDAHelper.getGuild().getController();

				for (Member player : players) {
					gController.addRolesToMember(player, sessionRole).queue();
				}
				gController.addRolesToMember(JDAHelper.getGuild().getMember(Init.getJDA().getSelfUser()), sessionRole)
						.queue();
			}
		});

	}

	private void startMPSession(GuildMessageReceivedEvent event) {
		gameSession = getNewGameInstance(gameName, true);
		deleteMessages(event);
		gameSession.gameStart();
	}

	private void startSPSession() {
		gameSession = getNewGameInstance(gameName, false);
		gameSession.gameStart();
	}

	private void sendMessage(String message) {
		if (gameChannel != null) {
			gameChannel.sendMessage(message).queue();
		}
	}

	// TODO: Make it so the user that all users can only type the accept,
	// decline, etc message
	private void deleteMessages(GuildMessageReceivedEvent event) {
		CommandClearChat.deleteMessages(event);
	}

	public void endGameSession() {
		final ChannelManager chManager = gameChannel.getManager();
		chManager.getChannel().delete().queue();
		final RoleManager roleManager = JDAHelper.getRoleByName("session" + sessionID).getManager();
		roleManager.getRole().delete().queue();

		GlobalGameManager.removeGameListener("session" + sessionID);

		if (gameSession != null) {
			gameSession.endGame();
		}
	}

	public int getSessionID() {
		return sessionID;
	}

	public ArrayList<Member> getPlayers() {
		return players;
	}
}