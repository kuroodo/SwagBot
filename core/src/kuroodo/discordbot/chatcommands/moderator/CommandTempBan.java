package kuroodo.discordbot.chatcommands.moderator;

import kuroodo.discordbot.Init;
import kuroodo.discordbot.entities.ChatCommand;
import kuroodo.discordbot.helpers.ChatHelper;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.managers.GuildManager;

public class CommandTempBan extends ChatCommand {
	public CommandTempBan() {
		isModCommand = true;
	}

	@Override
	public void executeCommand(String commandParams, GuildMessageReceivedEvent event) {
		super.executeCommand(commandParams, event);

		if (!isUserAuthorized) {
			return;
		}

		User user = event.getMessage().getMentionedUsers().get(0);// ChatHelper.getUserByID(ChatHelper.splitString(commandParameters)[0]);
		String reason = ChatHelper.splitString(commandParameters)[1];

		if (user != null) {
			// Check if trying to ban bot
			if (user == Init.getJDA().getSelfInfo()) {

				Init.getServerOwner().getPrivateChannel()
						.sendMessage(event.getAuthor().getUsername() + " Just tried to temp ban me!");
				event.getChannel().sendMessage(event.getAuthor().getAsMention() + " You dare to conspire against me?");

				// Check if trying to ban server owner
			} else if (user == Init.getServerOwner()) {

				Init.getServerOwner().getPrivateChannel()
						.sendMessage(event.getAuthor().getUsername() + " Just tried to TEMPban you!");
				event.getChannel()
						.sendMessage(event.getAuthor().getAsMention() + " You dare to conspire against the server?");

			} else if (ChatHelper.isUserAdmin(event.getAuthor())) {

				user.getPrivateChannel().sendMessage("You have been temporarily banned/silenced from "
						+ ChatHelper.getGuild().getName() + " for " + reason);
				executeBan(user);

			} else if (ChatHelper.isUserModerator(event.getAuthor())) {

				// Check if moderator is trying to ban an admin
				if (ChatHelper.isUserAdmin(user)) {
					Init.getServerOwner().getPrivateChannel()
							.sendMessage(event.getAuthor().getUsername() + " Just tried to TEMPban an admin");
				} else {
					user.getPrivateChannel().sendMessage("You have been temporarily banned/silenced from "
							+ ChatHelper.getGuild().getName() + " for " + reason);
					executeBan(user);
				}
			}
		}
		event.getMessage().deleteMessage();

	}

	private void executeBan(User user) {
		System.out.println("executing");
		final String DEATHROLENAME = "TempBan";

		GuildManager manager = ChatHelper.getGuild().getManager();

		ChatHelper.removeUsersRoles(user, manager);
		manager.addRoleToUser(user, ChatHelper.getRoleByName(DEATHROLENAME));

		manager.update();
	}

	@Override
	public String info() {
		return "Temporarily silences a user. usage !tempban username reason . Example: !tempban Keemstar starting too much drama";
	}

}
