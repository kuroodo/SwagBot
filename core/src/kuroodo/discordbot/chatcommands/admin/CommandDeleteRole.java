package kuroodo.discordbot.chatcommands.admin;

import kuroodo.discordbot.entities.ChatCommand;
import kuroodo.discordbot.helpers.JDAHelper;
import kuroodo.discordbot.helpers.JSonReader;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class CommandDeleteRole extends ChatCommand {

	public CommandDeleteRole() {
		isAdminCommand = true;
	}

	@Override
	public void executeCommand(String commandParams, GuildMessageReceivedEvent event) {
		super.executeCommand(commandParams, event);

		if (!isUserAuthorized) {
			return;
		}

		TextChannel adminChannel = JDAHelper.getTextChannelByName(JSonReader.getPreferencesValue("adminchannel"));

		// Ensure that role exists or that it isn't admin/mod
		if (JDAHelper.getRoleByName(commandParameters) == null || commandParameters.equals("Admin")
				|| commandParameters.equals("Moderator")) {

			if (adminChannel != null) {
				adminChannel.sendMessage(event.getAuthor().getAsMention() + " A role with the name " + commandParameters
						+ " doesn't exist or is locked").queue();
			}
		} else {
			JDAHelper.getRoleByName(commandParameters).delete().queue();

			if (adminChannel != null) {
				adminChannel.sendMessage(event.getAuthor().getAsMention() + " deleted role called " + commandParameters)
						.queue();
			}
		}
	}

	@Override
	public String info() {
		return "Delete a role. Usage !deleterole rolename . Example: !deletrole Member";
	}

}
