package kuroodo.discordbot.chatcommands.admin;

import kuroodo.discordbot.entities.ChatCommand;
import kuroodo.discordbot.helpers.ChatHelper;
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
		
		if (ChatHelper.getRoleByName(commandParameters) == null || commandParameters.equals("Admin")
				|| commandParameters.equals("Moderator") || commandParameters.equals("testkek")) {

			ChatHelper.getTextChannelByName("headquarters").sendMessage(event.getAuthor().getAsMention()
					+ " A role with the name " + commandParameters + " doesn't exist or is locked");
		} else {

			ChatHelper.getRoleByName(commandParameters).getManager().delete();

			ChatHelper.getTextChannelByName("headquarters")
					.sendMessage(event.getAuthor().getAsMention() + " deleted role called " + commandParameters);
		}
	}

	@Override
	public String info() {
		return "Delete a role. Usage !deleterole rolename . Example: !deletrole Member";
	}

}
