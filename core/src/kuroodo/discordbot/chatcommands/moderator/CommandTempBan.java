package kuroodo.discordbot.chatcommands.moderator;

import kuroodo.discordbot.Init;
import kuroodo.discordbot.entities.ChatCommand;
import kuroodo.discordbot.helpers.JDAHelper;
import kuroodo.discordbot.helpers.JSonReader;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.managers.GuildController;

public class CommandTempBan extends ChatCommand {
	// TODO: Change command name to Silence
	// TODO: Implement timer that un-tempbans user after specified time
	public CommandTempBan() {
		isModCommand = true;
	}

	// TODO: Cleanup some of this code by putting it into methods
	@Override
	public void executeCommand(String commandParams, GuildMessageReceivedEvent event) {
		super.executeCommand(commandParams, event);

		if (!isUserAuthorized) {
			return;
		}

		Member memberToTempBan = JDAHelper.getGuild().getMember(event.getMessage().getMentionedUsers().get(0));// ChatHelper.getUserByID(ChatHelper.splitString(commandParameters)[0]);
		String reasonForTempBan = JDAHelper.splitString(commandParameters)[1];

		if (memberToTempBan != null) {
			// Check if trying to ban bot
			if (memberToTempBan.getUser() == Init.getJDA().getSelfUser()) {

				Init.getServerOwner().getPrivateChannel()
						.sendMessage(event.getAuthor().getName() + " Just tried to temp ban me!").queue();
				event.getChannel().sendMessage(event.getAuthor().getAsMention() + " You dare to conspire against me?")
						.queue();

				// Check if trying to ban server owner
			} else if (memberToTempBan.getUser() == Init.getServerOwner()) {
				Init.getServerOwner().getPrivateChannel()
						.sendMessage(event.getAuthor().getName() + " Just tried to temp ban you!").queue();
				event.getChannel()
						.sendMessage(event.getAuthor().getAsMention() + " You dare to conspire against the server?")
						.queue();

			} else if (JDAHelper.isMemberAdmin(JDAHelper.getGuild().getMember(event.getAuthor()))) {
				sendBanNotifications(memberToTempBan, reasonForTempBan);
				tempBanUser(memberToTempBan);
			} else if (JDAHelper.isMemberModerator(JDAHelper.getGuild().getMember(event.getAuthor()))) {

				// Check if moderator is trying to ban an admin
				if (JDAHelper.isMemberAdmin(memberToTempBan)) {
					Init.getServerOwner().getPrivateChannel()
							.sendMessage(event.getAuthor().getName() + " Just tried to temp ban an admin").queue();
					event.getChannel().sendMessage(event.getAuthor().getAsMention() + " Moderators cannot temp ban admins!")
							.queue();
				} else {
					sendBanNotifications(memberToTempBan, reasonForTempBan);
					tempBanUser(memberToTempBan);
				}
			}
		}
		event.getMessage().deleteMessage();

	}

	private void tempBanUser(Member member) {
		final String DEATHROLENAME = "TempBan";
		GuildController controller = JDAHelper.getGuild().getController();

		JDAHelper.removeUsersRoles(member, controller);
		controller.addRolesToMember(member, JDAHelper.getRoleByName(DEATHROLENAME)).queue();
	}

	private void sendBanNotifications(Member member, String reason) {
		TextChannel adminChannel = JDAHelper.getTextChannelByName(JSonReader.getPreferencesValue("adminchannel"));

		if (adminChannel != null) {
			adminChannel.sendMessage(member.getUser().getName() + " has been temporarily banned for " + reason).queue();
		}

		member.getUser().getPrivateChannel().sendMessage(
				"You have been temporarily banned/silenced from " + JDAHelper.getGuild().getName() + " for " + reason)
				.queue();
		;
	}

	@Override
	public String info() {
		return "Temporarily silences a user. usage !tempban username reason . Example: !tempban Keemstar starting too much drama";
	}

}
