package kuroodo.discordbot.botcommands;

import kuroodo.discordbot.Init;
import kuroodo.discordbot.entities.BotCommand;
import kuroodo.discordbot.helpers.ChatHelper;
import net.dv8tion.jda.entities.VoiceChannel;
import net.dv8tion.jda.events.message.priv.PrivateMessageReceivedEvent;

public class BotCommandUnblock extends BotCommand {

	@Override
	public void executeCommand(String commandParams, PrivateMessageReceivedEvent event) {
		super.executeCommand(commandParams, event);

		boolean found = false;

		if (commandParams.toLowerCase().equals("all")) {
			Init.unblockAllVoiceChannels();
			event.getChannel().sendMessage("Successfully blocked " + commandParams);
			System.out.println("[" + event.getAuthor().getUsername() + "]" + "Successfully blocked " + commandParams);
		} else {
			for (VoiceChannel channel : ChatHelper.getVoiceChannels()) {
				if (channel.getName().equals(commandParams)) {
					Init.unblockVoiceChannel(commandParams);
					found = true;

					event.getChannel().sendMessage("Successfully blocked " + commandParams);
					System.out.println("[" + event.getAuthor().getUsername() + "]" + "Successfully blocked " + commandParams);
				}
			}

			if (!found) {
				event.getChannel().sendMessage("CHANNEL NAME " + commandParams + " NOT FOUND!");
				System.out.println("[" + event.getAuthor().getUsername() + "]" + "CHANNEL NAME " + commandParams + " NOT FOUND!");
			}

		}
	}

	@Override
	public String info() {
		return "";
	}

}
