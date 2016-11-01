package kuroodo.discordbot.chatcommands;

import kuroodo.discordbot.entities.ChatCommand;
import kuroodo.discordbot.helpers.ChatHelper;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

public class CommandSlap extends ChatCommand {

	@Override
	public void executeCommand(String commandParams, GuildMessageReceivedEvent event) {
		super.executeCommand(commandParams, event);
		User user = ChatHelper.getUserByID(commandParams);

		if (user == null) {
			user = ChatHelper.getUserByUsername(commandParams);
			if (user == null) {
				if (!event.getMessage().getMentionedUsers().isEmpty()) {
				user = event.getMessage().getMentionedUsers().get(0);
				}
			}
		}

		if (user != null) {
			sendMessage(event.getAuthor().getAsMention() + " slapped " + user.getAsMention());
			event.getMessage().deleteMessage();
		}
	}

	@Override
	public String info() {
		return "Slap a user. Usage: !slap [username] \nExample: !slap testname";
	}
}

// OLD CODE

// int userIndex = 0;
// for (int i = 0; i < event.getGuild().getUsers().size(); i++) {
// if (event.getGuild().getUsers().get(i).getUsername().equals(sUser)) {
// userIndex = i;
// }
//
// }

// if (event.getMessage().getRawContent().equals("!slap " + sUser)) {
// System.out.println("in");
// event.getChannel().sendMessage(event.getAuthor().getAsMention() + " slapped "
// + event.getGuild().getUsers().get(userIndex).getAsMention());
// event.getMessage().deleteMessage();
// }
