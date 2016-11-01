package kuroodo.discordbot.chatcommands.admin;

import kuroodo.discordbot.entities.ChatCommand;
import kuroodo.discordbot.helpers.ChatHelper;
import net.dv8tion.jda.entities.Role;
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

		GuildManager guildManager = ChatHelper.getGuild().getManager();

		Role role = ChatHelper.getRoleByName(ChatHelper.splitString(commandParameters)[0]);
		User user = ChatHelper.getUserByID(ChatHelper.splitString(commandParameters)[1]);

		if (user == null) {
			user = ChatHelper.getUserByUsername(ChatHelper.splitString(commandParameters)[1]);
			if (user == null) {
				user = event.getMessage().getMentionedUsers().get(0);
			}
		}

		// if (user == null) {
		// user =
		// ChatHelper.getUserByUsername(ChatHelper.splitString(commandParameters)[1]);
		// }
		// Final check
		if (user == null || role == null) {
			sendPrivateMessage(" the role or user you have inputted do not exist, or not in case sensitive!");
			// ChatHelper.getTextChannelByName("headquarters").sendMessage(event.getAuthor().getAsMention()
			// + " the role or user you have inputted do not exist, or not in
			// case sensitive!");
		}

		guildManager.removeRoleFromUser(user, role).update();

		ChatHelper.getTextChannelByName("headquarters").sendMessage(event.getAuthor().getAsMention() + " just removed "
				+ user.getUsername() + "'s role: " + role.getName());
	}

	@Override
	public String info() {
		return "Remove a members roler. Usage !removerole rolename user . Example: !removerole Member SomeFaggot";
	}
}
