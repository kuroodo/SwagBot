package kuroodo.discordbot.chatcommands;

import kuroodo.discordbot.entities.ChatCommand;
import kuroodo.discordbot.helpers.ChatHelper;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

public class CommandRoast extends ChatCommand {

	public CommandRoast() {
		super();
	}

	@Override
	public void executeCommand(String commandParams, GuildMessageReceivedEvent event) {
		super.executeCommand(commandParams, event);
		User user = ChatHelper.getUserByID(commandParameters);

		if (user == null) {
			user = ChatHelper.getUserByUsername(commandParameters);
			if (user == null) {
				if (!event.getMessage().getMentionedUsers().isEmpty()) {
					user = event.getMessage().getMentionedUsers().get(0);
				}
			}
		}

		if (user != null) {
			event.getMessage().deleteMessage();
			sendMessage(user.getAsMention() + " https://www.youtube.com/watch?v=_tWC5qtfby4");
		}
	}

	@Override
	public String info() {
		return "A hidden command, send a video notifying a victim that they got roasted. Usage: roasted [username]"
				+ "\nExample: !roasted Daniel";
	}

}
