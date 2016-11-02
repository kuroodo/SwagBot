package kuroodo.discordbot.botcommands;

import kuroodo.discordbot.entities.BotCommand;
import kuroodo.discordbot.helpers.ChatHelper;
import net.dv8tion.jda.events.message.priv.PrivateMessageReceivedEvent;

public class BotCommandSay extends BotCommand {

	@Override
	public void executeCommand(String commandParams, PrivateMessageReceivedEvent event) {
		super.executeCommand(commandParams, event);

		try {
			ChatHelper.getTextChannelByName(ChatHelper.splitString(commandParameters)[0])
					.sendMessage(ChatHelper.splitString(commandParameters)[1]);
			System.out.println("[" + event.getAuthor().getUsername() + "]" + "Used SwagBot to say something");
		} catch (NullPointerException e) {
			event.getChannel().sendMessage("[" + event.getAuthor().getUsername() + "]"
					+ "Either you didn't specify a channel, or you tried to send a blank msg");
		}
	}

	@Override
	public String info() {
		return "";
	}

}
