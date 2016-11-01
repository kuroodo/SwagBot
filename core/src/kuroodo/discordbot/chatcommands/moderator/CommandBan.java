package kuroodo.discordbot.chatcommands.moderator;

import kuroodo.discordbot.Init;
import kuroodo.discordbot.entities.ChatCommand;
import kuroodo.discordbot.helpers.ChatHelper;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

public class CommandBan extends ChatCommand {

	public CommandBan() {
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
			user = ChatHelper.getUserByUsername(ChatHelper.splitString(commandParameters)[0]);
			if (user == null) {
				user = event.getMessage().getMentionedUsers().get(0);
			}
		}
		
		String reason = ChatHelper.splitString(commandParameters)[1];

		if (user != null) {
			// Check if trying to ban bot
			if (user == Init.getJDA().getSelfInfo()) {

				Init.getServerOwner().getPrivateChannel()
						.sendMessage(event.getAuthor().getUsername() + " Just tried to ban me!");
				event.getChannel().sendMessage(event.getAuthor().getAsMention() + " You dare to conspire against me?");

				// Check if trying to ban server owner
			} else if (user == Init.getServerOwner()) {

				Init.getServerOwner().getPrivateChannel()
						.sendMessage(event.getAuthor().getUsername() + " Just tried to ban you!");
				event.getChannel()
						.sendMessage(event.getAuthor().getAsMention() + " You dare to conspire against the server?");

			} else if (ChatHelper.isUserAdmin(event.getAuthor())) {

				user.getPrivateChannel()
						.sendMessage("You have been banned from " + ChatHelper.getGuild().getName() + " for " + reason);
				ChatHelper.getGuild().getManager().ban(user, 1);

			} else if (ChatHelper.isUserModerator(event.getAuthor())) {

				// Check if moderator is trying to ban an admin
				if (ChatHelper.isUserAdmin(user)) {
					Init.getServerOwner().getPrivateChannel()
							.sendMessage(event.getAuthor().getUsername() + " Just tried to ban an admin");
				} else {
					user.getPrivateChannel().sendMessage(
							"You have been banned from " + ChatHelper.getGuild().getName() + " for " + reason);
					ChatHelper.getGuild().getManager().ban(user, 1);
				}
			}
		}
		event.getMessage().deleteMessage();

	}

	@Override
	public String info() {
		return "Ban a member from the server. usage !ban username reason . Example: !ban Keemstar starting too much drama";
	}

}
