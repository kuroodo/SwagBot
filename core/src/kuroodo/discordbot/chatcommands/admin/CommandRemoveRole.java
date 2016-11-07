package kuroodo.discordbot.chatcommands.admin;

import kuroodo.discordbot.entities.ChatCommand;
import kuroodo.discordbot.helpers.JDAHelper;
import kuroodo.discordbot.helpers.JSonReader;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.managers.GuildManager;

public class CommandRemoveRole extends ChatCommand {

	public CommandRemoveRole() {
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
			guildManager.removeRoleFromUser(user, role).update();

			if (adminChannel != null) {
				adminChannel.sendMessageAsync(event.getAuthor().getAsMention() + " just removed " + user.getUsername()
						+ "'s role: " + role.getName(), null);
			}
		} else {
			sendPrivateMessage(" the role or user you have inputted do not exist, or not in case sensitive!");
		}

	}

	@Override
	public String info() {
		return "Remove a members roler. Usage !removerole rolename user . Example: !removerole Member SomeFaggot";
	}
}