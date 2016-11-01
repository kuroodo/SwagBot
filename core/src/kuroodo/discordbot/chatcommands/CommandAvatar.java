package kuroodo.discordbot.chatcommands;

import kuroodo.discordbot.entities.ChatCommand;
import kuroodo.discordbot.helpers.ChatHelper;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

public class CommandAvatar extends ChatCommand {

	public CommandAvatar() {

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
			sendMessage("." + user.getAsMention() + "'s avatar is " + user.getAvatarUrl());
		}

	}

	@Override
	public String info() {
		return "Get a link to a users avatar usage: !avatar [usersname] Example: !avatar Kuroodo";
	}

}
