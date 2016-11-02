package kuroodo.discordbot.chatcommands.moderator;

import kuroodo.discordbot.Init;
import kuroodo.discordbot.entities.ChatCommand;
import kuroodo.discordbot.helpers.JDAHelper;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

public class CommandBan extends ChatCommand {
	// TODO: Able to specify ban days in parameters
	private final int BAN_DAYS = 1;

	public CommandBan() {
		isModCommand = true;
	}

	@Override
	public void executeCommand(String commandParams, GuildMessageReceivedEvent event) {
		super.executeCommand(commandParams, event);

		if (!isUserAuthorized) {
			return;
		}

		User userToBan = JDAHelper.getUserByID(JDAHelper.splitString(commandParameters)[0]);

		if (userToBan == null) {
			userToBan = JDAHelper.getUserByUsername(JDAHelper.splitString(commandParameters)[0]);
			if (userToBan == null) {
				userToBan = event.getMessage().getMentionedUsers().get(0);
			}
		}

		String banReason = JDAHelper.splitString(commandParameters)[1];

		if (userToBan != null) {
			// Check if trying to ban bot
			if (userToBan == Init.getJDA().getSelfInfo()) {

				Init.getServerOwner().getPrivateChannel()
						.sendMessageAsync(event.getAuthor().getUsername() + " Just tried to ban me!", null);
				event.getChannel()
						.sendMessageAsync(event.getAuthor().getAsMention() + " You dare to conspire against me?", null);

				// Check if trying to ban server owner
			} else if (userToBan == Init.getServerOwner()) {

				Init.getServerOwner().getPrivateChannel()
						.sendMessageAsync(event.getAuthor().getUsername() + " Just tried to ban you!", null);
				event.getChannel().sendMessageAsync(
						event.getAuthor().getAsMention() + " You dare to conspire against the server?", null);

			} else if (JDAHelper.isUserAdmin(event.getAuthor())) {

				userToBan.getPrivateChannel().sendMessageAsync(
						"You have been banned from " + JDAHelper.getGuild().getName() + " for " + banReason, null);
				JDAHelper.getGuild().getManager().ban(userToBan, BAN_DAYS);

			} else if (JDAHelper.isUserModerator(event.getAuthor())) {

				// Check if moderator is trying to ban an admin
				if (JDAHelper.isUserAdmin(userToBan)) {
					Init.getServerOwner().getPrivateChannel()
							.sendMessageAsync(event.getAuthor().getUsername() + " Just tried to ban an admin", null);
					event.getChannel().sendMessageAsync(
							event.getAuthor().getAsMention() + " Moderators cannot ban admins!", null);
				} else {
					userToBan.getPrivateChannel().sendMessageAsync(
							"You have been banned from " + JDAHelper.getGuild().getName() + " for " + banReason, null);
					JDAHelper.getGuild().getManager().ban(userToBan, BAN_DAYS);
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
