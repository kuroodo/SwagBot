package kuroodo.discordbot.chatcommands;

import kuroodo.discordbot.entities.ChatCommand;
import kuroodo.discordbot.helpers.ChatHelper;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

public class CommandPoke extends ChatCommand {

	public CommandPoke() {
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
			sendMessage(event.getAuthor().getAsMention() + " poked " + user.getAsMention());
			event.getMessage().deleteMessage();
		}
	}

	@Override
	public String info() {
		return "Poke a user. Usage: !poke [username] \nExample: !poke testname";
	}

}