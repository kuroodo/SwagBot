package kuroodo.discordbot.chatcommands.moderator;

import java.util.ArrayList;
import java.util.function.Consumer;

import kuroodo.discordbot.Init;
import kuroodo.discordbot.entities.ChatCommand;
import kuroodo.discordbot.helpers.JDAHelper;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageHistory;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

// TODO: Update to new JDA versions and improve on code
public class CommandClearChat extends ChatCommand {

	// Discord only allows bulk deletion of max 100 messages
	private static final int MAX_DELETE_LIMIT = 100;

	private final float DELETE_WAIT_TIME = 2f;
	private float deleteTimeCount = 2f;

	private boolean isTimerActive = false;

	private Member memberToFocus;

	private int messageDeleteCount = 0;

	public CommandClearChat() {
		isModCommand = true;
	}

	@Override
	public void update(float delta) {
		super.update(delta);

		deleteTimeCount += delta;

		// Every 2 seconds...
		if (deleteTimeCount >= DELETE_WAIT_TIME && isTimerActive) {

			if (memberToFocus != null) {
				isTimerActive = false;
				MessageHistory.getHistoryAround(event.getChannel(), event.getMessage(), MAX_DELETE_LIMIT)
						.queue(new Consumer<MessageHistory>() {

							@Override
							public void accept(MessageHistory t) {

								ArrayList<Message> messagesToDelete = getMemberMessages(t);

								if (messagesToDelete.size() > 1) {
									messageDeleteCount += messagesToDelete.size();
									event.getChannel().deleteMessages(messagesToDelete).queue();

									deleteTimeCount = 0f;
									isTimerActive = true;
								} else if (messagesToDelete.size() != 0) {
									messageDeleteCount++;
									messagesToDelete.get(0).deleteMessage().queue();

									onDeleteFinish();
									shouldUpdate = false;
								} else {
									onDeleteFinish();
									shouldUpdate = false;
								}
							}
						});
			} else {
				isTimerActive = false;
				MessageHistory.getHistoryAround(event.getChannel(), event.getMessage(), MAX_DELETE_LIMIT)
						.queue(new Consumer<MessageHistory>() {

							@Override
							public void accept(MessageHistory t) {
								if (t.getCachedHistory().size() > 1) {
									messageDeleteCount += t.getCachedHistory().size();
									event.getChannel().deleteMessages(t.getCachedHistory()).queue();

									deleteTimeCount = 0f;
									isTimerActive = true;
								} else {
									onDeleteFinish();
									shouldUpdate = false;
								}
							}
						});
			}

		}

	}

	@Override
	public void executeCommand(String commandParams, GuildMessageReceivedEvent event) {
		super.executeCommand(commandParams, event);

		if (!isUserAuthorized) {
			return;
		}

		memberToFocus = getMember();

		if (commandParameters.equals("all") || memberToFocus != null) {
			onDeleteStart();
			isTimerActive = true;
			shouldUpdate = true;
		} else {
			sendMessage("The User you have specified does not exist or you have specified an invalid parameter");
		}

	}

	// Used for external classes or commands
	public static void deleteMessages(GuildMessageReceivedEvent event) {
		MessageHistory.getHistoryAround(event.getChannel(), event.getMessage(), MAX_DELETE_LIMIT)
				.queue(new Consumer<MessageHistory>() {

					@Override
					public void accept(MessageHistory t) {
						if (t.getCachedHistory().size() > 1) {
							event.getChannel().deleteMessages(t.getCachedHistory()).queue();
						} else {
							event.getMessage().deleteMessage().queue();
						}
					}
				});
	}

	private void onDeleteStart() {
		Init.getJDA().getPresence().setGame(Game.of("Busy..."));
		Init.getJDA().getPresence().setIdle(true);
	}

	private void onDeleteFinish() {
		Init.getJDA().getPresence().setGame(Game.of(Init.gameMessage));
		Init.getJDA().getPresence().setIdle(false);

		sendMessage("DELETED " + messageDeleteCount + " MESSAGES!");
	}

	private ArrayList<Message> getMemberMessages(MessageHistory history) {
		ArrayList<Message> messagesToDelete = new ArrayList<>();
		for (Message msg : history.getCachedHistory()) {
			if (msg.getAuthor() == memberToFocus.getUser()) {
				messagesToDelete.add(msg);
			}
		}
		return messagesToDelete;
	}

	private Member getMember() {
		Member member = JDAHelper.getMemberByID(commandParameters);

		if (member == null) {
			member = JDAHelper.getMemberByUsername(commandParameters);

			if (member == null) {
				if (!event.getMessage().getMentionedUsers().isEmpty()) {
					member = JDAHelper.getMember(event.getMessage().getMentionedUsers().get(0));
				}
			}
		}

		return member;
	}

	@Override
	public String info() {
		return "Clear the chat (or a specific users messages (Note: Only deletes a users messages in last 100 messages) ) for the channel the command was sent on.\n"
				+ "Usage: !clearchat all/username \nExample: !clearchat all or !clearchat someUser123";
	}

}