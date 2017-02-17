package kuroodo.discordbot.botcommands;

import kuroodo.discordbot.Init;
import kuroodo.discordbot.audio.AudioPlayer;
import kuroodo.discordbot.entities.BotCommand;
import kuroodo.discordbot.entities.JDAListener;
import kuroodo.discordbot.helpers.JDAHelper;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;

public class BotCommandBlock extends BotCommand {

	@Override
	public void executeCommand(String commandParams, PrivateMessageReceivedEvent event) {
		super.executeCommand(commandParams, event);

		String message = "";
		boolean foundAChannel = false;

		AudioPlayer player = null;

		for (JDAListener listener : Init.getListeners()) {
			if (listener.listenerName.equals("AudioPlayer")) {
				player = (AudioPlayer) listener;
				break;
			}
		}

		if (player != null) {
			for (VoiceChannel channel : JDAHelper.getVoiceChannels()) {
				if (channel.getName().equals(commandParams)) {
					player.blockVoiceChannel(JDAHelper.getVoiceChannelByName(commandParams));
					foundAChannel = true;

					message = "Successfully blocked " + commandParams;

					sendMessage(message);
					System.out.println("[" + event.getAuthor().getName() + message);
					break;
				}
			}
		}
		if (!foundAChannel) {
			message = "CHANNEL NAME " + commandParams + " NOT FOUND!";
			sendMessage(message);
			System.out.println("[" + event.getAuthor().getName() + "]" + message);
		}
	}

	@Override
	public String info() {
		// TODO Auto-generated method stub
		return null;
	}

}
