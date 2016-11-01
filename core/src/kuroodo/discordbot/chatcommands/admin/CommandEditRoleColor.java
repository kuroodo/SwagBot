package kuroodo.discordbot.chatcommands.admin;

import java.awt.Color;

import kuroodo.discordbot.entities.ChatCommand;
import kuroodo.discordbot.helpers.ChatHelper;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

public class CommandEditRoleColor extends ChatCommand {

	public CommandEditRoleColor() {
		isAdminCommand = true;
	}

	@Override
	public void executeCommand(String commandParams, GuildMessageReceivedEvent event) {
		super.executeCommand(commandParams, event);
		
		if (!isUserAuthorized) {
			return;
		}
		
		
		String roleName = ChatHelper.splitString(commandParameters)[0];
		String roleColor = ChatHelper.splitString(commandParameters)[1];

		if (ChatHelper.getRoleByName(roleName) == null || roleName.equals("Admin") || roleName.equals("Moderator")
				|| roleName.equals("testkek")) {
			ChatHelper.getTextChannelByName("headquarters").sendMessage(event.getAuthor().getAsMention()
					+ " The role named " + roleName + "doesn't exist, or you tried editting a locked role");
		} else {
			try {
				ChatHelper.getRoleByName(roleName).getManager().setColor(Color.decode(roleColor)).update();
			} catch (NumberFormatException e) {
				ChatHelper.getTextChannelByName("headquarters").sendMessage(event.getAuthor().getAsMention()
						+ " Make sure you set the color to be a hex number. Example: !editrolecolor roleName #003CFF");
			}
		}
	}

	@Override
	public String info() {
		return "Change the color of a role, using a hex code for the color. "
				+ "Usage: !editrolecolor rolename hexcolorcode .Example !editrolecolor Member #D490AB";
	}

}
