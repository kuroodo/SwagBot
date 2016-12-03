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

	// TODO: Cleanup some of this code by putting it into methods
	@Override
	public void executeCommand(String commandParams, GuildMessageReceivedEvent event) {
		super.executeCommand(commandParams, event);

		if (!isUserAuthorized) {
			return;
		}

		Member member = findMember();

		String reasonForKick = JDAHelper.splitString(commandParameters)[1];

		if (member != null) {
			TextChannel adminChannel = JDAHelper.getTextChannelByName(JSonReader.getPreferencesValue("adminchannel"));

			// Check if trying to kick bot
			if (member == Init.getJDA().getSelfUser()) {

				Init.getServerOwner().getPrivateChannel()
						.sendMessage(event.getAuthor().getName() + " Just tried to kick me!").queue();
				event.getChannel().sendMessage(event.getAuthor().getAsMention() + " You dare to conspire against me?")
						.queue();

				// Check if trying to kick server owner
			} else if (member == Init.getServerOwner()) {

				Init.getServerOwner().getPrivateChannel()
						.sendMessage(event.getAuthor().getName() + " Just tried to kick you!").queue();
				event.getChannel()
						.sendMessage(event.getAuthor().getAsMention() + " You dare to conspire against the server?")
						.queue();
			} else if (JDAHelper.isMemberAdmin(JDAHelper.getGuild().getMember(event.getAuthor()))) {
				if (adminChannel != null) {
					adminChannel.sendMessage(member.getUser().getName() + " has been kicked for " + reasonForKick)
							.queue();
				}

				member.getUser().getPrivateChannel()
						.sendMessage(
								"You have been kicked from " + JDAHelper.getGuild().getName() + " for " + reasonForKick)
						.queue();
				JDAHelper.getGuild().getController().kick(member).queue();

			} else if (JDAHelper.isMemberModerator(JDAHelper.getGuild().getMember(event.getAuthor()))) {
				// Check if moderator is trying to kick an admin
				if (JDAHelper.isMemberAdmin(member)) {
					Init.getServerOwner().getPrivateChannel()
							.sendMessage(event.getAuthor().getName() + " Just tried to kick an admin").queue();

					event.getChannel().sendMessage(event.getAuthor().getAsMention() + " Moderators cannot kick admins!")
							.queue();
				} else {
					if (adminChannel != null) {
						adminChannel.sendMessage(member.getUser().getName() + " has been kicked for " + reasonForKick)
								.queue();
					}

					member.getUser().getPrivateChannel().sendMessage(
							"You have been kicked from " + JDAHelper.getGuild().getName() + " for " + reasonForKick)
							.queue();
					JDAHelper.getGuild().getController().kick(member).queue();
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
		return "kick a member from the server. usage !kick username reason . Example: !kick Keemstar starting too much drama";
	}

}
