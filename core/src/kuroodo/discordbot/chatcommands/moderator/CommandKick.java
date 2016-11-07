package kuroodo.discordbot.chatcommands.moderator;

import kuroodo.discordbot.Init;
import kuroodo.discordbot.entities.ChatCommand;
import kuroodo.discordbot.helpers.JDAHelper;
import kuroodo.discordbot.helpers.JSonReader;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

public class CommandKick extends ChatCommand {

	public CommandKick() {
		isModCommand = true;
	}

	// TODO: Cleanup some of this code by putting it into methods
	@Override
	public void executeCommand(String commandParams, GuildMessageReceivedEvent event) {
		super.executeCommand(commandParams, event);

		if (!isUserAuthorized) {
			return;
		}

		User userToKick = JDAHelper.getUserByID(JDAHelper.splitString(commandParameters)[0]);

		if (userToKick == null) {
			userToKick = JDAHelper.getUserByUsername(JDAHelper.splitString(commandParameters)[0]);
			if (userToKick == null) {
				userToKick = event.getMessage().getMentionedUsers().get(0);
			}
		}

		String reasonForKick = JDAHelper.splitString(commandParameters)[1];

		if (userToKick != null) {
			TextChannel adminChannel = JDAHelper.getTextChannelByName(JSonReader.getPreferencesValue("adminchannel"));

			// Check if trying to kick bot
			if (userToKick == Init.getJDA().getSelfInfo()) {

				Init.getServerOwner().getPrivateChannel()
						.sendMessageAsync(event.getAuthor().getUsername() + " Just tried to kick me!", null);
				event.getChannel()
						.sendMessageAsync(event.getAuthor().getAsMention() + " You dare to conspire against me?", null);

				// Check if trying to kick server owner
			} else if (userToKick == Init.getServerOwner()) {

				Init.getServerOwner().getPrivateChannel()
						.sendMessageAsync(event.getAuthor().getUsername() + " Just tried to kick you!", null);
				event.getChannel().sendMessageAsync(
						event.getAuthor().getAsMention() + " You dare to conspire against the server?", null);
			} else if (JDAHelper.isUserAdmin(event.getAuthor())) {
				if (adminChannel != null) {
					adminChannel.sendMessageAsync(userToKick.getUsername() + " has been kicked for " + reasonForKick,
							null);
				}

				userToKick.getPrivateChannel().sendMessageAsync(
						"You have been kicked from " + JDAHelper.getGuild().getName() + " for " + reasonForKick, null);
				JDAHelper.getGuild().getManager().kick(userToKick);

			} else if (JDAHelper.isUserModerator(event.getAuthor())) {
				// Check if moderator is trying to kick an admin
				if (JDAHelper.isUserAdmin(userToKick)) {
					Init.getServerOwner().getPrivateChannel()
							.sendMessageAsync(event.getAuthor().getUsername() + " Just tried to kick an admin", null);
					event.getChannel().sendMessageAsync(
							event.getAuthor().getAsMention() + " Moderators cannot kick admins!", null);
				} else {
					if (adminChannel != null) {
						adminChannel.sendMessageAsync(
								userToKick.getUsername() + " has been kicked for " + reasonForKick, null);
					}

					userToKick.getPrivateChannel().sendMessageAsync(
							"You have been kicked from " + JDAHelper.getGuild().getName() + " for " + reasonForKick,
							null);
					JDAHelper.getGuild().getManager().kick(userToKick);
				}
			}
		}
		event.getMessage().deleteMessage();
	}

	@Override
	public String info() {
		return "kick a member from the server. usage !kick username reason . Example: !kick Keemstar starting too much drama";
	}

}
