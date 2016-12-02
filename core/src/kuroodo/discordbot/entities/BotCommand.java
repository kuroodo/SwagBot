package kuroodo.discordbot.entities;

import kuroodo.discordbot.Init;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;

public abstract class BotCommand implements Command {

	protected PrivateMessageReceivedEvent event;

	protected String commandName = "";
	protected String commandParameters = "";

	protected boolean shouldUpdate = false, isUserAuthorized = false;

	@Override
	public void update(float delta) {
	}

	@Override
	public void executeCommand(String commandParams, GuildMessageReceivedEvent event) {

	}

	@Override
	public void executeCommand(String commandParams, PrivateMessageReceivedEvent event) {
		this.event = event;

		if (Init.SUPER_USERS.contains(event.getAuthor())) {
			isUserAuthorized = true;
		}

		this.commandParameters = commandParams;
	}

	@Override
	public abstract String info();

	protected void sendMessage(String message) {
		event.getChannel().sendMessage(message).queue();
	}

	protected void sendPrivateMessage(String message) {

		event.getAuthor().getPrivateChannel().sendMessage(message).queue();
	}

	public String commandName() {
		return commandName;
	}

	public boolean shouldUpdate() {
		return shouldUpdate;
	}

}
