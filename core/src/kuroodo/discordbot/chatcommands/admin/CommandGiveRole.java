package kuroodo.discordbot.chatcommands.admin;

import kuroodo.discordbot.entities.ChatCommand;
import kuroodo.discordbot.helpers.JDAHelper;
import kuroodo.discordbot.helpers.JSonReader;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.managers.GuildController;

public class CommandGiveRole extends ChatCommand {

	public CommandGiveRole() {
		isAdminCommand = true;
	}

	@Override
	public void executeCommand(String commandParams, GuildMessageReceivedEvent event) {
		super.executeCommand(commandParams, event);

		if (!isUserAuthorized) {
			return;
		}

		TextChannel adminChannel = JDAHelper.getTextChannelByName(JSonReader.getPreferencesValue("adminchannel"));
		GuildController guildManager = JDAHelper.getGuild().getController();

		Role role = JDAHelper.getRoleByName(JDAHelper.splitString(commandParameters)[0]);
		Member member = findMember(event);

		if (member != null && role != null) {
			guildManager.addRolesToMember(member, role).queue();

			if (adminChannel != null) {
				adminChannel.sendMessage(event.getAuthor().getAsMention() + " gave " + member.getUser().getName()
						+ " the role of " + role.getName()).queue();
			}

		} else {
			sendPrivateMessage("The role or user you have inputted do not exist, or not in case sensitive!");
		}
	}

	private Member findMember(GuildMessageReceivedEvent event) {
		if (!event.getMessage().getMentionedUsers().isEmpty()) {
			return JDAHelper.getMember(event.getMessage().getMentionedUsers().get(0));
		}

		sendMessage("Please mention a valid user");
		return null;
	}

	@Override
	public String info() {
		return "Give a role to a member. Usage !giverole rolename user . Example: !giverole Member SomeFaggot";
	}

}
