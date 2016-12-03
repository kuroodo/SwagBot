package kuroodo.discordbot.chatcommands.moderator;

import kuroodo.discordbot.Init;
import kuroodo.discordbot.entities.ChatCommand;
import kuroodo.discordbot.helpers.JDAHelper;
import kuroodo.discordbot.helpers.JSonReader;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class CommandBan extends ChatCommand {
	// TODO: Able to specify ban days in parameters
	private final int BAN_DAYS = 1;

	public CommandBan() {
		isModCommand = true;
	}

	// TODO: Cleanup some of this code by putting it into methods
	@Override
	public void executeCommand(String commandParams, GuildMessageReceivedEvent event) {
		super.executeCommand(commandParams, event);

		if (!isUserAuthorized) {
			return;
		}

		Member memberToBan = findMember();

		String banReason = JDAHelper.splitString(commandParameters)[1];

		if (memberToBan != null) {
			TextChannel adminChannel = JDAHelper.getTextChannelByName(JSonReader.getPreferencesValue("adminchannel"));

			// Check if trying to ban bot
			if (memberToBan.getUser() == Init.getJDA().getSelfUser()) {

				Init.getServerOwner().getPrivateChannel()
						.sendMessage(event.getAuthor().getName() + " Just tried to ban me!").queue();
				event.getChannel().sendMessage(event.getAuthor().getAsMention() + " You dare to conspire against me?")
						.queue();

				// Check if trying to ban server owner
			} else if (memberToBan.getUser() == Init.getServerOwner()) {

				Init.getServerOwner().getPrivateChannel()
						.sendMessage(event.getAuthor().getName() + " Just tried to ban you!").queue();
				event.getChannel()
						.sendMessage(event.getAuthor().getAsMention() + " You dare to conspire against the server?")
						.queue();

			} else if (JDAHelper.isMemberAdmin(JDAHelper.getGuild().getMember(event.getAuthor()))) {

				if (adminChannel != null) {
					adminChannel.sendMessage(memberToBan.getUser().getName() + " has been banned for " + banReason)
							.queue();
				}

				memberToBan.getUser().getPrivateChannel()
						.sendMessage(
								"You have been banned from " + JDAHelper.getGuild().getName() + " for " + banReason)
						.queue();

				JDAHelper.getGuild().getController().ban(memberToBan, BAN_DAYS).queue();

			} else if (JDAHelper.isMemberModerator(JDAHelper.getGuild().getMember(event.getAuthor()))) {

				// Check if moderator is trying to ban an admin
				if (JDAHelper.isMemberAdmin(memberToBan)) {
					Init.getServerOwner().getPrivateChannel()
							.sendMessage(event.getAuthor().getName() + " Just tried to ban an admin").queue();

					event.getChannel().sendMessage(event.getAuthor().getAsMention() + " Moderators cannot ban admins!")
							.queue();
				} else {

					if (adminChannel != null) {
						adminChannel.sendMessage(memberToBan.getUser().getName() + " has been banned for " + banReason)
								.queue();
					}

					memberToBan.getUser().getPrivateChannel()
							.sendMessage(
									"You have been banned from " + JDAHelper.getGuild().getName() + " for " + banReason)
							.queue();
					JDAHelper.getGuild().getController().ban(memberToBan, BAN_DAYS).queue();
				}
			}
		}
		event.getMessage().deleteMessage();
	}

	private Member findMember() {
		Member member = JDAHelper.getMemberByID(JDAHelper.splitString(commandParameters)[0]);

		if (member == null) {
			member = JDAHelper.getMemberByUsername(JDAHelper.splitString(commandParameters)[0]);
			if (member == null) {
				member = JDAHelper.getGuild().getMember(event.getMessage().getMentionedUsers().get(0));
			}
		}
		return member;
	}

	@Override
	public String info() {
		return "Ban a member from the server. usage !ban username reason . Example: !ban Keemstar starting too much drama";
	}
}
