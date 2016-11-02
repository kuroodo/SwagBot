package kuroodo.discordbot.botcommands;

import kuroodo.discordbot.Init;
import kuroodo.discordbot.entities.BotCommand;
import kuroodo.discordbot.helpers.JDAHelper;
import net.dv8tion.jda.entities.VoiceChannel;
import net.dv8tion.jda.events.message.priv.PrivateMessageReceivedEvent;

public class BotCommandBlock extends BotCommand {

	@Override
	public void executeCommand(String commandParams, PrivateMessageReceivedEvent event) {
		super.executeCommand(commandParams, event);

		String message = "";
		boolean foundAChannel = false;

		for (VoiceChannel channel : JDAHelper.getVoiceChannels()) {
			if (channel.getName().equals(commandParams)) {
				Init.blockVoiceChannel(commandParams);
				foundAChannel = true;

				message = "Successfully blocked " + commandParams;

				event.getChannel().sendMessageAsync(message, null);
				System.out.println("[" + event.getAuthor().getUsername() + message);
				break;
			}
		}
		if (!foundAChannel) {
			message = "CHANNEL NAME " + commandParams + " NOT FOUND!";
			event.getChannel().sendMessageAsync(message, null);
			System.out.println("[" + event.getAuthor().getUsername() + "]" + message);
		}
	}

	@Override
	public String info() {
		return "";
	}
}