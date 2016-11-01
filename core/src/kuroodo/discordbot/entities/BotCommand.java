package kuroodo.discordbot.entities;

import kuroodo.discordbot.Init;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.events.message.priv.PrivateMessageReceivedEvent;

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

		// System.out.println("Execture: " + commandParameters);
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

	public String commandName() {
		return commandName;
	}

	public boolean shouldUpdate() {
		return shouldUpdate;
	}

}
