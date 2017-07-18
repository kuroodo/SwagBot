package kuroodo.discordbot.chatcommands.moderator;

import kuroodo.discordbot.Init;
import kuroodo.discordbot.entities.ChatCommand;
import kuroodo.discordbot.helpers.JDAHelper;
import kuroodo.discordbot.helpers.JSonReader;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

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

		Member memberToKick = findMember();

		String reasonForKick = JDAHelper.splitString(commandParameters)[1];

		if (memberToKick != null) {
			// Check if trying to kick bot
			if (memberToKick.getUser() == Init.getJDA().getSelfUser()) {

				sendPrivateMessage(Init.getServerOwner(), event.getAuthor().getName() + " Just tried to kick me!");
				event.getChannel().sendMessage(event.getAuthor().getAsMention() + " You dare to conspire against me?")
						.queue();

				// Check if trying to kick server owner
			} else if (memberToKick.getUser() == Init.getServerOwner()) {

				sendPrivateMessage(Init.getServerOwner(), event.getAuthor().getName() + " Just tried to kick you!");

				event.getChannel()
						.sendMessage(event.getAuthor().getAsMention() + " You dare to conspire against the server?")
						.queue();

			} else if (JDAHelper.isMemberAdmin(JDAHelper.getMember(event.getAuthor()))) {
				sendKickNotifications(memberToKick, reasonForKick);
				kickMember(memberToKick);
			} else if (JDAHelper.isMemberModerator(JDAHelper.getMember(event.getAuthor()))) {

				// Check if moderator is trying to kick an admin
				if (JDAHelper.isMemberAdmin(memberToKick)) {
					sendPrivateMessage(Init.getServerOwner(),
							event.getAuthor().getName() + " Just tried to kick an admin");

					event.getChannel().sendMessage(event.getAuthor().getAsMention() + " Moderators cannot kick admins!")
							.queue();
				} else {
					sendKickNotifications(memberToKick, reasonForKick);
					kickMember(memberToKick);
				}
			}
		}
		event.getMessage().delete();
	}

	private void kickMember(Member memberToKick) {
		JDAHelper.getGuild().getController().kick(memberToKick).queue();
	}

	private void sendKickNotifications(Member memberToKick, String reason) {
		TextChannel adminChannel = JDAHelper.getTextChannelByName(JSonReader.getPreferencesValue("adminchannel"));

		if (adminChannel != null) {
			adminChannel.sendMessage(memberToKick.getUser().getName() + " has been kicked for " + reason).queue();
		}

		try {
			sendPrivateMessage(memberToKick.getUser(),
					"You have been kicked from " + JDAHelper.getGuild().getName() + " for " + reason);
		} catch (IllegalStateException e) {
			System.out.println(
					"Pending kick member " + memberToKick.getUser().getName() + " does not have a private channel");
		}
	}

	private Member findMember() {
		Member member = JDAHelper.getMemberByID(JDAHelper.splitString(commandParameters)[0]);

		if (member == null) {
			member = JDAHelper.getMemberByUsername(JDAHelper.splitString(commandParameters)[0]);
			if (member == null) {
				member = JDAHelper.getMember(event.getMessage().getMentionedUsers().get(0));
			}
		}
		return member;
	}

	@Override
	public String info() {
		return "kick a member from the server. usage !kick username reason . Example: !kick Keemstar starting too much drama";
	}

}
