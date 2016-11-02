package kuroodo.discordbot.botcommands;

import kuroodo.discordbot.Init;
import kuroodo.discordbot.entities.BotCommand;
import net.dv8tion.jda.events.message.priv.PrivateMessageReceivedEvent;

public class BotCommandSetGame extends BotCommand {

	@Override
	public void executeCommand(String commandParams, PrivateMessageReceivedEvent event) {
		super.executeCommand(commandParams, event);

		if (commandParameters.toLowerCase().equals("default")) {
			Init.getJDA().getAccountManager().setGame("Type !help For Help (;");
			System.out.println("[" + event.getAuthor().getUsername() + "]" + "Successfully changed game status");
		} else {
			Init.getJDA().getAccountManager().setGame(commandParameters);
			System.out.println("[" + event.getAuthor().getUsername() + "]" + "Successfully changed game status");
		}
	}

	@Override
	public String info() {
		return "";
	}

}
