package kuroodo.discordbot.chatcommands.admin;

import java.util.function.Consumer;

import kuroodo.discordbot.entities.ChatCommand;
import kuroodo.discordbot.helpers.JDAHelper;
import kuroodo.discordbot.helpers.JSonReader;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.managers.RoleManagerUpdatable;

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
			JDAHelper.getGuild().getController().createRole().queue(new Consumer<Role>() {

				@Override
				public void accept(Role t) {
					RoleManagerUpdatable roleManager = t.getManagerUpdatable();

					roleManager.getNameField().setValue(commandParameters);
					roleManager.getPermissionField().revokePermissions(t.getPermissions());
					roleManager.update().queue();
				}
			});

			if (adminChannel != null) {
				adminChannel
						.sendMessage(
								event.getAuthor().getAsMention() + " created a new role called " + commandParameters)
						.queue();
			}
		} else {
			if (adminChannel != null) {
				adminChannel.sendMessage(event.getAuthor().getAsMention() + " A role with the name " + commandParameters
						+ " already exists").queue();
			}
		}
	}

	@Override
	public String info() {
		return "Makes a new role, with no permissions. Usage: !makerole rolename .Example !makerole Member";
	}

}
