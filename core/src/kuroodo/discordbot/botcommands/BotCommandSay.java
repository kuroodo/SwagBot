package kuroodo.discordbot.botcommands;

import kuroodo.discordbot.entities.BotCommand;
import kuroodo.discordbot.helpers.JDAHelper;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;

public class BotCommandSay extends BotCommand {

	@Override
	public void executeCommand(String commandParams, PrivateMessageReceivedEvent event) {
		super.executeCommand(commandParams, event);

		try {
			String channelName = JDAHelper.splitString(commandParameters)[0];
			String messageToChannel = JDAHelper.splitString(commandParameters)[1];

			JDAHelper.getTextChannelByName(channelName).sendMessage(messageToChannel).queue();
			System.out.println("[" + event.getAuthor().getName() + "]" + "Used SwagBot to say something");
		} catch (NullPointerException e) {
			event.getChannel().sendMessage("[" + event.getAuthor().getName() + "]"
					+ "Either you didn't specify a channel, or you tried to send a blank msg").queue();
		}
	}

	@Override
	public String info() {
		return "";
	}
}