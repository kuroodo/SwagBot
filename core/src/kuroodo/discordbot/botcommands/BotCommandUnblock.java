package kuroodo.discordbot.botcommands;

import kuroodo.discordbot.Init;
import kuroodo.discordbot.entities.BotCommand;
import kuroodo.discordbot.helpers.JDAHelper;
import net.dv8tion.jda.events.message.priv.PrivateMessageReceivedEvent;

public class BotCommandUnblock extends BotCommand {

	@Override
	public void executeCommand(String commandParams, PrivateMessageReceivedEvent event) {
		super.executeCommand(commandParams, event);

		String message = "";
		boolean foundAChannel = false;

		if (commandParams.toLowerCase().equals("all")) {
			Init.unblockAllVoiceChannels();

			message = "Successfully unblocked " + commandParams;
			event.getChannel().sendMessageAsync(message, null);
			System.out.println("[" + event.getAuthor().getUsername() + "]" + message);
		} else {
			if (JDAHelper.getVoiceChannelByName(commandParams) != null) {
				Init.unblockVoiceChannel(commandParams);
				foundAChannel = true;

				message = "Successfully unblocked " + commandParams;
				event.getChannel().sendMessageAsync(message, null);
				System.out.println(message);
			}
			if (!foundAChannel) {
				message = "CHANNEL NAME " + commandParams + " NOT FOUND!";
				event.getChannel().sendMessageAsync(message, null);
				System.out.println("[" + event.getAuthor().getUsername() + "]" + message);
			}
		}
	}

	@Override
	public String info() {
		return "";
	}
}