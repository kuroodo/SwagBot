package kuroodo.discordbot.chatcommands;

import kuroodo.discordbot.entities.ChatCommand;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class CommandAvatar extends ChatCommand {

	public CommandAvatar() {

	}

	@Override
	public void executeCommand(String commandParams, GuildMessageReceivedEvent event) {
		super.executeCommand(commandParams, event);

		if (!event.getMessage().getMentionedUsers().isEmpty()) {
			User user = event.getMessage().getMentionedUsers().get(0);

			if (user != null) {
				sendMessage("." + user.getAsMention() + "'s avatar is " + user.getAvatarUrl());
			}
		} else {
			sendMessage("Please mention a valid user");
		}

	}

	@Override
	public String info() {
		return "Get a link to a users avatar usage: !avatar [user] Example: !avatar @user";
	}
}