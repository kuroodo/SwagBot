package kuroodo.discordbot.chatcommands;

import kuroodo.discordbot.entities.ChatCommand;
import kuroodo.discordbot.helpers.JDAHelper;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class CommandPoke extends ChatCommand {

	public CommandPoke() {
		super();
	}

	@Override
	public void executeCommand(String commandParams, GuildMessageReceivedEvent event) {
		super.executeCommand(commandParams, event);

		User user = JDAHelper.getUserByID(commandParameters);

		if (user == null) {
			user = JDAHelper.getUserByUsername(commandParameters);
			if (user == null) {
				if (!event.getMessage().getMentionedUsers().isEmpty()) {
					user = event.getMessage().getMentionedUsers().get(0);
				}
			}
		}

		if (user != null) {
			sendMessage(event.getAuthor().getAsMention() + " poked " + user.getAsMention());
			event.getMessage().delete();
		}
	}

	@Override
	public String info() {
		return "Poke a user. Usage: !poke [username] \nExample: !poke testname";
	}

}