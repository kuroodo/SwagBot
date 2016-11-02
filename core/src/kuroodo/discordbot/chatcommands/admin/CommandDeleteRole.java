package kuroodo.discordbot.chatcommands.admin;

import kuroodo.discordbot.entities.ChatCommand;
import kuroodo.discordbot.helpers.JDAHelper;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

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

		// Ensure that role exists or that it isn't admin/mod
		if (JDAHelper.getRoleByName(commandParameters) == null || commandParameters.equals("Admin")
				|| commandParameters.equals("Moderator")) {

			JDAHelper.getTextChannelByName("headquarters").sendMessageAsync(event.getAuthor().getAsMention()
					+ " A role with the name " + commandParameters + " doesn't exist or is locked", null);
		} else {
			JDAHelper.getRoleByName(commandParameters).getManager().delete();

			JDAHelper.getTextChannelByName("headquarters").sendMessageAsync(
					event.getAuthor().getAsMention() + " deleted role called " + commandParameters, null);
		}
	}

	@Override
	public String info() {
		return "Delete a role. Usage !deleterole rolename . Example: !deletrole Member";
	}

}
