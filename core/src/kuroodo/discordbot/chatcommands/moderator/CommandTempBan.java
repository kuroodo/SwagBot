package kuroodo.discordbot.chatcommands.moderator;

import kuroodo.discordbot.Init;
import kuroodo.discordbot.entities.ChatCommand;
import kuroodo.discordbot.helpers.JDAHelper;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.managers.GuildManager;

public class CommandTempBan extends ChatCommand {
	// TODO: Implement timer that un-tempbans user after specified time
	public CommandTempBan() {
		isModCommand = true;
	}

	@Override
	public void executeCommand(String commandParams, GuildMessageReceivedEvent event) {
		super.executeCommand(commandParams, event);

		if (!isUserAuthorized) {
			return;
		}

		User userToTempBan = event.getMessage().getMentionedUsers().get(0);// ChatHelper.getUserByID(ChatHelper.splitString(commandParameters)[0]);
		String reasonForTempBan = JDAHelper.splitString(commandParameters)[1];

		if (userToTempBan != null) {
			// Check if trying to ban bot
			if (userToTempBan == Init.getJDA().getSelfInfo()) {

				Init.getServerOwner().getPrivateChannel()
						.sendMessageAsync(event.getAuthor().getUsername() + " Just tried to temp ban me!", null);
				event.getChannel()
						.sendMessageAsync(event.getAuthor().getAsMention() + " You dare to conspire against me?", null);

				// Check if trying to ban server owner
			} else if (userToTempBan == Init.getServerOwner()) {

				Init.getServerOwner().getPrivateChannel()
						.sendMessageAsync(event.getAuthor().getUsername() + " Just tried to TEMPban you!", null);
				event.getChannel().sendMessageAsync(
						event.getAuthor().getAsMention() + " You dare to conspire against the server?", null);

			} else if (JDAHelper.isUserAdmin(event.getAuthor())) {

				userToTempBan.getPrivateChannel().sendMessageAsync("You have been temporarily banned/silenced from "
						+ JDAHelper.getGuild().getName() + " for " + reasonForTempBan, null);
				tempBanUser(userToTempBan);

			} else if (JDAHelper.isUserModerator(event.getAuthor())) {

				// Check if moderator is trying to ban an admin
				if (JDAHelper.isUserAdmin(userToTempBan)) {
					Init.getServerOwner().getPrivateChannel().sendMessageAsync(
							event.getAuthor().getUsername() + " Just tried to TEMPban an admin", null);
					event.getChannel().sendMessageAsync(
							event.getAuthor().getAsMention() + " Moderators cannot kick admins!", null);
				} else {
					userToTempBan.getPrivateChannel().sendMessageAsync("You have been temporarily banned/silenced from "
							+ JDAHelper.getGuild().getName() + " for " + reasonForTempBan, null);
					tempBanUser(userToTempBan);
				}
			}
		}
		event.getMessage().deleteMessage();

	}

	private void tempBanUser(User user) {
		final String DEATHROLENAME = "TempBan";

		GuildManager manager = JDAHelper.getGuild().getManager();

		JDAHelper.removeUsersRoles(user, manager);
		manager.addRoleToUser(user, JDAHelper.getRoleByName(DEATHROLENAME));

		manager.update();
	}

	@Override
	public String info() {
		return "Temporarily silences a user. usage !tempban username reason . Example: !tempban Keemstar starting too much drama";
	}

}
