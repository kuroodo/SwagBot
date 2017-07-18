package kuroodo.discordbot.chatcommands;

import kuroodo.discordbot.entities.ChatCommand;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class CommandSlap extends ChatCommand {

	@Override
	public void executeCommand(String commandParams, GuildMessageReceivedEvent event) {
		super.executeCommand(commandParams, event);

		if (!event.getMessage().getMentionedUsers().isEmpty()) {
			User user = event.getMessage().getMentionedUsers().get(0);

			if (user != null) {
				sendMessage(event.getAuthor().getAsMention() + " slapped " + user.getAsMention());
				event.getMessage().delete();
			}
		} else {
			sendMessage("Please mention a valid user");
		}
	}

	@Override
	public String info() {
		return "Slap a user. Usage: !slap [user] \nExample: !slap @user";
	}
}