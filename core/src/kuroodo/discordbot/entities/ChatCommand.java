package kuroodo.discordbot.entities;

import kuroodo.discordbot.Init;
import kuroodo.discordbot.helpers.JDAHelper;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.events.message.priv.PrivateMessageReceivedEvent;

public abstract class ChatCommand implements Command {

	protected GuildMessageReceivedEvent event;

	protected String commandName = "";
	protected String commandParameters = "";

	protected boolean shouldUpdate = false, isAdminCommand = false, isModCommand = false, isUserAuthorized = true;

	public ChatCommand() {

	}

	@Override
	public void update(float delta) {
	}

	@Override
	public void executeCommand(String commandParams, GuildMessageReceivedEvent event) {
		this.event = event;

		this.commandParameters = commandParams;

		checkUserHasAccessToCommand();

		// System.out.println("Command Params [executeParams - ChatCommand]: " +
		// commandParameters);
		sortSubText();
	}

	@Override
	public void executeCommand(String commandParams, PrivateMessageReceivedEvent event) {

	}

	@Override
	public abstract String info();

	protected void sendMessage(String message) {
		event.getChannel().sendMessageAsync(message, null);
	}

	protected void sendPrivateMessage(String message) {
		event.getAuthor().getPrivateChannel().sendMessageAsync(message, null);
	}

	// Method being kept for temporary reference
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

	public boolean checkUserHasAccessToCommand() {
		if (isAdminCommand && !JDAHelper.isUserAdmin(event.getAuthor()) && event.getAuthor() != Init.getServerOwner()) {
			isUserAuthorized = false;
			shouldUpdate = false;
			sendPrivateMessage(event.getAuthor().getAsMention() + " You do not have access to that command!");
		} else if (isModCommand
				&& (!JDAHelper.isUserModerator(event.getAuthor()) && !JDAHelper.isUserAdmin(event.getAuthor()))
				&& event.getAuthor() != Init.getServerOwner()) {
			isUserAuthorized = false;
			shouldUpdate = false;
			sendPrivateMessage(event.getAuthor().getAsMention() + " You do not have access to that command!");
		}

		return isUserAuthorized;
	}

	public boolean checkUserHasAccessToCommand(GuildMessageReceivedEvent event) {
		if (isAdminCommand && !JDAHelper.isUserAdmin(event.getAuthor()) && event.getAuthor() != Init.getServerOwner()) {
			isUserAuthorized = false;
			shouldUpdate = false;
		} else if (isModCommand
				&& (!JDAHelper.isUserModerator(event.getAuthor()) && !JDAHelper.isUserAdmin(event.getAuthor()))
				&& event.getAuthor() != Init.getServerOwner()) {
			isUserAuthorized = false;
			shouldUpdate = false;
		}

		return isUserAuthorized;
	}

	public String commandName() {
		return commandName;
	}

	public boolean shouldUpdate() {
		return shouldUpdate;
	}

}
