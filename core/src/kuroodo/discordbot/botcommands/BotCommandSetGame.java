package kuroodo.discordbot.botcommands;

import kuroodo.discordbot.Init;
import kuroodo.discordbot.entities.BotCommand;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;

public class BotCommandSetGame extends BotCommand {

	@Override
	public void executeCommand(String commandParams, PrivateMessageReceivedEvent event) {
		super.executeCommand(commandParams, event);

		if (commandParameters.toLowerCase().equals("default")) {
			Init.getJDA().getPresence().setGame(Game.of("Type !help For Help (;"));
			System.out.println("[" + event.getAuthor().getName() + "]" + "Successfully changed game status");
		} else {
			Init.getJDA().getPresence().setGame(Game.of(commandParameters));
			System.out.println("[" + event.getAuthor().getName() + "]" + "Successfully changed game status");
		}
	}

	@Override
	public String info() {
		return "";
	}

}
