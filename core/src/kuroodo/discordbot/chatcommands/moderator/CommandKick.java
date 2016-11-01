package kuroodo.discordbot.chatcommands.moderator;

import kuroodo.discordbot.Init;
import kuroodo.discordbot.entities.ChatCommand;
import kuroodo.discordbot.helpers.ChatHelper;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

public class CommandKick extends ChatCommand {

	public CommandKick() {
		isModCommand = true;
	}

	@Override
	public void executeCommand(String commandParams, GuildMessageReceivedEvent event) {
		super.executeCommand(commandParams, event);

		if (!isUserAuthorized) {
			return;
		}

		User user = ChatHelper.getUserByID(ChatHelper.splitString(commandParameters)[0]);

		if (user == null) {
			System.out.println("Null1");
			user = ChatHelper.getUserByUsername(ChatHelper.splitString(commandParameters)[0]);
			if (user == null) {
				System.out.println("Null2");
				user = event.getMessage().getMentionedUsers().get(0);
			}
		}

		String reason = ChatHelper.splitString(commandParameters)[1];

		if (user != null) {
			// Check if trying to kick bot
			if (user == Init.getJDA().getSelfInfo()) {

				Init.getServerOwner().getPrivateChannel()
						.sendMessage(event.getAuthor().getUsername() + " Just tried to kick me!");
				event.getChannel().sendMessage(event.getAuthor().getAsMention() + " You dare to conspire against me?");

				// Check if trying to kick server owner
			} else if (user == Init.getServerOwner()) {

				Init.getServerOwner().getPrivateChannel()
						.sendMessage(event.getAuthor().getUsername() + " Just tried to kick you!");
				event.getChannel()
						.sendMessage(event.getAuthor().getAsMention() + " You dare to conspire against the server?");

			} else if (ChatHelper.isUserAdmin(event.getAuthor())) {

				user.getPrivateChannel()
						.sendMessage("You have been kicked from " + ChatHelper.getGuild().getName() + " for " + reason);
				ChatHelper.getGuild().getManager().kick(user);

			} else if (ChatHelper.isUserModerator(event.getAuthor())) {

				// Check if moderator is trying to kick an admin
				if (ChatHelper.isUserAdmin(user)) {
					Init.getServerOwner().getPrivateChannel()
							.sendMessage(event.getAuthor().getUsername() + " Just tried to kick an admin");
				} else {
					user.getPrivateChannel().sendMessage(
							"You have been kicked from " + ChatHelper.getGuild().getName() + " for " + reason);
					ChatHelper.getGuild().getManager().kick(user);
				}
			}
		}else{
			System.out.println("StillNull");
		}
		event.getMessage().deleteMessage();
	}

	@Override
	public String info() {
		return "kick a member from the server. usage !kick username reason . Example: !kick Keemstar starting too much drama";
	}

}
