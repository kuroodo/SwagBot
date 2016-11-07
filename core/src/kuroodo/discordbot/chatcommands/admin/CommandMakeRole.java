package kuroodo.discordbot.chatcommands.admin;

import kuroodo.discordbot.entities.ChatCommand;
import kuroodo.discordbot.helpers.JDAHelper;
import kuroodo.discordbot.helpers.JSonReader;
import net.dv8tion.jda.Permission;
import net.dv8tion.jda.entities.TextChannel;
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

		TextChannel adminChannel = JDAHelper.getTextChannelByName(JSonReader.getPreferencesValue("adminchannel"));

		if (JDAHelper.getRoleByName(commandParameters) == null) {
			JDAHelper.getGuild().createRole().setName(commandParameters).update();

			// Clear roll of all default permissions
			for (Permission permission : JDAHelper.getRoleByName(commandParameters).getPermissions()) {
				JDAHelper.getRoleByName(commandParameters).getManager().revoke(permission).update();
			}

			if (adminChannel != null) {
				adminChannel.sendMessageAsync(
						event.getAuthor().getAsMention() + " created a new role called " + commandParameters, null);
			}
		} else {
			if (adminChannel != null) {
				adminChannel.sendMessageAsync(event.getAuthor().getAsMention() + " A role with the name "
						+ commandParameters + " already exists", null);
			}
		}
	}

	@Override
	public String info() {
		return "Makes a new role, with no permissions. Usage: !makerole rolename .Example !makerole Member";
	}

}
