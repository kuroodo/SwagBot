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

	@Override
	public void executeCommand(String commandParams, GuildMessageReceivedEvent event) {
		super.executeCommand(commandParams, event);

		if (!isUserAuthorized) {
			return;
		}

		Member memberToBan = findMember();

		String banReason = JDAHelper.splitString(commandParameters)[1];

		if (memberToBan != null) {

			// Check if trying to ban bot
			if (memberToBan.getUser() == Init.getJDA().getSelfUser()) {

				sendPrivateMessage(Init.getServerOwner(), event.getAuthor().getName() + " Just tried to ban me!");

				event.getChannel().sendMessage(event.getAuthor().getAsMention() + " You dare to conspire against me?")
						.queue();

				// Check if trying to ban server owner
			} else if (memberToBan.getUser() == Init.getServerOwner()) {

				sendPrivateMessage(Init.getServerOwner(), event.getAuthor().getName() + " Just tried to ban you!");
				event.getChannel()
						.sendMessage(event.getAuthor().getAsMention() + " You dare to conspire against the server?")
						.queue();

			} else if (JDAHelper.isMemberAdmin(JDAHelper.getMember(event.getAuthor()))) {
				sendBanNotifications(memberToBan, banReason);
				banMember(memberToBan);
			} else if (JDAHelper.isMemberModerator(JDAHelper.getMember(event.getAuthor()))) {

				// Check if moderator is trying to ban an admin
				if (JDAHelper.isMemberAdmin(memberToBan)) {
					sendPrivateMessage(Init.getServerOwner(),
							event.getAuthor().getName() + " Just tried to ban an admin");

					event.getChannel().sendMessage(event.getAuthor().getAsMention() + " Moderators cannot ban admins!")
							.queue();
				} else {
					sendBanNotifications(memberToBan, banReason);
					banMember(memberToBan);
				}
			}
		}
		event.getMessage().delete();
	}

	private void banMember(Member memberToBan) {
		JDAHelper.getGuild().getController().ban(memberToBan, BAN_DAYS).queue();
	}

	private void sendBanNotifications(Member memberToBan, String reason) {
		TextChannel adminChannel = JDAHelper.getTextChannelByName(JSonReader.getPreferencesValue("adminchannel"));

		if (adminChannel != null) {
			adminChannel.sendMessage(memberToBan.getUser().getName() + " has been banned for " + reason).queue();
		}

		try {

			sendPrivateMessage(memberToBan.getUser(),
					"You have been banned from " + JDAHelper.getGuild().getName() + " for " + reason);
		} catch (IllegalStateException e) {
			System.out.println(
					"Pending kick member " + memberToBan.getUser().getName() + " does not have a private channel");
		}
	}

	private Member findMember() {

		Member member = JDAHelper.getMember(event.getMessage().getMentionedUsers().get(0));

		if (member == null) {
			member = JDAHelper.getMemberByID(JDAHelper.splitString(commandParameters)[0]);
			if (member == null) {
				member = JDAHelper.getMemberByUsername(JDAHelper.splitString(commandParameters)[0]);
			}
		}
		return member;
	}

	@Override
	public String info() {
		return "Ban a member from the server. usage !ban username reason . Example: !ban Keemstar starting too much drama";
	}
}
