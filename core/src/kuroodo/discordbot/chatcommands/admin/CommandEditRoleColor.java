package kuroodo.discordbot.chatcommands.admin;

import java.awt.Color;

import kuroodo.discordbot.entities.ChatCommand;
import kuroodo.discordbot.helpers.JDAHelper;
import kuroodo.discordbot.helpers.JSonReader;
import net.dv8tion.jda.entities.TextChannel;
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

		TextChannel adminChannel = JDAHelper.getTextChannelByName(JSonReader.getPreferencesValue("adminchannel"));

		String roleName = JDAHelper.splitString(commandParameters)[0];
		String roleColor = JDAHelper.splitString(commandParameters)[1];

		if (JDAHelper.getRoleByName(roleName) == null || roleName.equals("Admin") || roleName.equals("Moderator")) {
			if (adminChannel != null) {
				adminChannel.sendMessageAsync(event.getAuthor().getAsMention() + " The role named " + roleName
						+ "doesn't exist, or you tried editting a locked role", null);
			}
		} else {
			try {
				JDAHelper.getRoleByName(roleName).getManager().setColor(Color.decode(roleColor)).update();
			} catch (NumberFormatException e) {
				if (adminChannel != null) {
					adminChannel.sendMessageAsync(event.getAuthor().getAsMention()
							+ " Make sure you set the color to be a hex number. Example: !editrolecolor roleName #003CFF", null);
				}
			}
		}
	}

	@Override
	public String info() {
		return "Change the color of a role, using a hex code for the color. "
				+ "Usage: !editrolecolor rolename hexcolorcode .Example !editrolecolor Member #D490AB";
	}

}
