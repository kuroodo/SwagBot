package kuroodo.discordbot.chatcommands.admin;

import kuroodo.discordbot.entities.ChatCommand;
import kuroodo.discordbot.helpers.ChatHelper;
import net.dv8tion.jda.Permission;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

public class CommandMakeRole extends ChatCommand {

	public CommandMakeRole() {
		isAdminCommand = true;
	}

	@Override
	public void executeCommand(String commandParams, GuildMessageReceivedEvent event) {
		super.executeCommand(commandParams, event);
		
		if (!isUserAuthorized) {
			return;
		}
		
		
		if (ChatHelper.getRoleByName(commandParameters) == null) {
			ChatHelper.getGuild().createRole().setName(commandParameters).update();

			for (Permission permission : ChatHelper.getRoleByName(commandParameters).getPermissions()) {
				ChatHelper.getRoleByName(commandParameters).getManager().revoke(permission).update();
			}

			ChatHelper.getTextChannelByName("headquarters")
					.sendMessage(event.getAuthor().getAsMention() + " created a new role called " + commandParameters);
		} else {
			ChatHelper.getTextChannelByName("headquarters").sendMessage(event.getAuthor().getAsMention()
					+ " A role with the name " + commandParameters + " already exists");
		}
	}

	@Override
	public String info() {
		return "Makes a new role, with no permissions. Usage: !makerole rolename .Example !makerole Member";
	}

}
