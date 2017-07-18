package kuroodo.discordbot.chatcommands;

import kuroodo.discordbot.entities.ChatCommand;
import kuroodo.discordbot.helpers.JDAHelper;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class CommandSlap extends ChatCommand {

	@Override
	public void executeCommand(String commandParams, GuildMessageReceivedEvent event) {
		super.executeCommand(commandParams, event);
		User user = JDAHelper.getUserByID(commandParams);

		if (user == null) {
			user = JDAHelper.getUserByUsername(commandParams);
			if (user == null) {
				if (!event.getMessage().getMentionedUsers().isEmpty()) {
					user = event.getMessage().getMentionedUsers().get(0);
				}
			}
		}

		if (user != null) {
			sendMessage(event.getAuthor().getAsMention() + " slapped " + user.getAsMention());
			event.getMessage().delete();
		}
	}

	@Override
	public String info() {
		return "Slap a user. Usage: !slap [username] \nExample: !slap testname";
	}
}