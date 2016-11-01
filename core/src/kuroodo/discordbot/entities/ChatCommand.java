package kuroodo.discordbot.entities;

import kuroodo.discordbot.Init;
import kuroodo.discordbot.helpers.ChatHelper;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.events.message.priv.PrivateMessageReceivedEvent;

public abstract class ChatCommand implements Command {

	protected GuildMessageReceivedEvent event;

	protected String commandName = "";
	protected String commandParameters = "";

	protected boolean shouldUpdate = false, isAdminCommand = false, isModCommand = false, isUserAuthorized = false;

	public ChatCommand() {

	}

	@Override
	public void update(float delta) {
	}

	@Override
	public void executeCommand(String commandParams, GuildMessageReceivedEvent event) {
		this.event = event;

		if (isAdminCommand && !ChatHelper.isUserAdmin(event.getAuthor())
				&& event.getAuthor() != Init.getServerOwner()) {
			shouldUpdate = false;
			sendMessage(event.getAuthor().getAsMention() + " You do not have access to that command!");
			return;
		} else if (isModCommand
				&& (!ChatHelper.isUserModerator(event.getAuthor()) && !ChatHelper.isUserAdmin(event.getAuthor()))
				&& event.getAuthor() != Init.getServerOwner()) {
			shouldUpdate = false;
			sendMessage(event.getAuthor().getAsMention() + " You do not have access to that command!");
			return;
		} else {
			isUserAuthorized = true;
		}

		this.commandParameters = commandParams;

		// System.out.println("Execture: " + commandParameters);

		sortSubText();
	}

	@Override
	public void executeCommand(String commandParams, PrivateMessageReceivedEvent event) {

	}

	@Override
	public abstract String info();

	protected void sendMessage(String message) {
		// try {
		// event.getChannel().sendMessage(message);
		// } catch (RateLimitedException e) {
		// Init.getActionScheduler()
		// .addTopPriorityAction(new ActionSendPublicMessage(event.getChannel(),
		// message, true, 3.5f));
		// }

		event.getChannel().sendMessageAsync(message, null);
	}

	protected void sendPrivateMessage(String message) {
		// try {
		// event.getAuthor().getPrivateChannel().sendMessage(message);
		// } catch (RateLimitedException e) {
		// Init.getActionScheduler().addTopPriorityAction(
		// new ActionSendPrivateMessage(event.getAuthor().getPrivateChannel(),
		// message, true, 3.5f));
		// }
		event.getAuthor().getPrivateChannel().sendMessageAsync(message, null);

	}

	protected void sortSubText() {
		// Check if a user is being mentioned, then convert mention to a
		// username (string)
		try {
			if (commandParameters.charAt(1) == '@') {
				// String firstWord = "";
				// String restOfString = "";
				//
				// firstWord = ChatHelper.splitString(commandParameters)[0];
				//
				// // Create the rest of string, if rest of string has
				// characters
				// for (int x = 1; x <
				// ChatHelper.splitString(commandParameters).length; x++) {
				// if
				// (ChatHelper.splitString(commandParameters)[x].trim().length()
				// > 0) {
				// restOfString = restOfString + " " +
				// ChatHelper.splitString(commandParameters)[x];
				// }
				// }
				//
				// firstWord = ChatHelper.getUsernameById(firstWord);
				// commandParameters = firstWord + restOfString;
				//
				// System.out.println("Sorted: " + commandParameters);
			}

		} catch (StringIndexOutOfBoundsException e) {

		}
	}

	public boolean isAdminCommand() {
		return isAdminCommand;
	}

	public boolean isModCommand() {
		return isModCommand;
	}

	public String commandName() {
		return commandName;
	}

	public boolean shouldUpdate() {
		return shouldUpdate;
	}

}
