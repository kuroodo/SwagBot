package kuroodo.discordbot.chatcommands.admin;

import kuroodo.discordbot.entities.ChatCommand;
import kuroodo.discordbot.helpers.JDAHelper;
import kuroodo.discordbot.helpers.JSonReader;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.managers.GuildManager;

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
		GuildManager guildManager = JDAHelper.getGuild().getManager();

		Role role = JDAHelper.getRoleByName(JDAHelper.splitString(commandParameters)[0]);
		User user = JDAHelper.getUserByID(JDAHelper.splitString(commandParameters)[1]);

		if (user == null) {
			user = JDAHelper.getUserByUsername(JDAHelper.splitString(commandParameters)[1]);
			if (user == null) {
				user = event.getMessage().getMentionedUsers().get(0);
			}
		}

		if (user != null && role != null) {
			guildManager.addRoleToUser(user, role).update();

			if (adminChannel != null) {
				adminChannel.sendMessageAsync(event.getAuthor().getAsMention() + " gave " + user.getUsername()
						+ " the role of " + role.getName(), null);
			}
			
		} else {
			sendPrivateMessage("The role or user you have inputted do not exist, or not in case sensitive!");
		}
	}

	@Override
	public String info() {
		return "Give a role to a member. Usage !giverole rolename user . Example: !giverole Member SomeFaggot";
	}

}
