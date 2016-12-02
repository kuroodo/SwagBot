/**
 * Listens to any server-based activity
 */
package kuroodo.discordbot.listeners;

import kuroodo.discordbot.Init;
import kuroodo.discordbot.entities.JDAListener;
import kuroodo.discordbot.helpers.JDAHelper;
import kuroodo.discordbot.helpers.JSonReader;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.DisconnectEvent;
import net.dv8tion.jda.core.events.ReconnectedEvent;
import net.dv8tion.jda.core.events.guild.GuildBanEvent;
import net.dv8tion.jda.core.events.guild.GuildUnbanEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.core.events.role.RoleCreateEvent;
import net.dv8tion.jda.core.events.role.RoleDeleteEvent;
import net.dv8tion.jda.core.events.role.update.RoleUpdateNameEvent;
import net.dv8tion.jda.core.events.role.update.RoleUpdatePermissionsEvent;

public class ServerListener extends JDAListener {

	public void update(float Delta) {
	}

	@Override
	public void onDisconnect(DisconnectEvent event) {
		super.onDisconnect(event);
	}

	@Override
	public void onReconnect(ReconnectedEvent event) {
		Init.getJDA().getPresence().setGame(Game.of("Type !help For Help (;"));
		super.onReconnect(event);
	}

	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {

		// Give new member a role if permitted
		if (JSonReader.getPreferencesValue("givenewmemberrole").equalsIgnoreCase("true")) {
			final Role roleToGive = JDAHelper.getRoleByName(JSonReader.getPreferencesValue("newmemberrolename"));
			if (roleToGive != null) {
				JDAHelper.giveRoleToMember(roleToGive, event.getMember());
			} else {
				System.out
						.println("ERROR: Role to give to new member does not exist! Check the name in prefernces.json");
			}
		}

		String message = " User " + event.getMember().getUser().getName() + " joined the server";
		System.out.println(message);
		Init.getServerOwner().getPrivateChannel().sendMessage(message).queue();

		// Send welcome message to channel if permitted
		if (JSonReader.getPreferencesValue("sendwelcomemessage").equalsIgnoreCase("true")) {
			final String WELCOME_CHANNEL_NAME = JSonReader.getPreferencesValue("welcomechannel");

			if (JDAHelper.getTextChannelByName(WELCOME_CHANNEL_NAME) != null) {
				message = event.getMember().getAsMention() + " " + JSonReader.getPreferencesValue("welcomemessage");
				JDAHelper.getTextChannelByName(WELCOME_CHANNEL_NAME).sendMessage(message).queue();
			} else {
				System.out.println(
						"ERROR: Channel to welcome new users in does not exist! Check the name in prefernces.json");
			}
		}

		super.onGuildMemberJoin(event);
	}

	@Override
	public void onGuildBan(GuildBanEvent event) {
		String message = "User " + event.getUser().getName() + " was banned from the server";
		System.out.println(message);
		Init.getServerOwner().getPrivateChannel().sendMessage(message).queue();
	}

	@Override
	public void onGuildUnban(GuildUnbanEvent event) {
		final String message = "User " + event.getUser().getName() + " was unbanned from the server";
		System.out.println(message);
		Init.getServerOwner().getPrivateChannel().sendMessage(message).queue();
		;
	}

	@Override
	public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
		final String message = "User " + event.getMember().getUser().getName() + " has left server, or been kicked";
		System.out.println(message);
		Init.getServerOwner().getPrivateChannel().sendMessage(message).queue();
	}

	@Override
	public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event) {
		String message = "User " + event.getMember().getUser().getName() + " was just given the following roles: ";

		for (Role role : event.getRoles()) {
			message = message + role.getName();

			if (role.getName().equals("Admin") || role.getName().equals("Moderator")) {

				String secondMsg = "Welcome to the staff team for " + event.getGuild().getName()
						+ ". Please type !commands or !help in the server chat to review the available staff commands";

				event.getMember().getUser().getPrivateChannel().sendMessage(secondMsg).queue();
			}
		}
		System.out.println(message);
		Init.getServerOwner().getPrivateChannel().sendMessage(message).queue();
	}

	@Override
	public void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent event) {
		String message = event.getMember().getUser().getName() + " just had the following roles removed: ";
		for (Role r : event.getRoles()) {
			message = message + r.getName() + ", ";
		}

		System.out.println(message);
		Init.getServerOwner().getPrivateChannel().sendMessage(message).queue();
		;
	}

	@Override
	public void onRoleCreate(RoleCreateEvent event) {
		final String message = "New role " + event.getRole().getName() + " has been created";
		System.out.println(message);
		Init.getServerOwner().getPrivateChannel().sendMessage(message).queue();
		;
	}

	@Override
	public void onRoleDelete(RoleDeleteEvent event) {
		final String message = "The following role was deleted  " + event.getGuild().getName();
		System.out.println(message);
		Init.getServerOwner().getPrivateChannel().sendMessage(message).queue();
		;
	}

	@Override
	public void onRoleUpdatePermissions(RoleUpdatePermissionsEvent event) {
		final String message = "The following role's permissions were changed  " + event.getRole().getName();
		System.out.println(message);
		Init.getServerOwner().getPrivateChannel().sendMessage(message).queue();
	}

	@Override
	public void onRoleUpdateName(RoleUpdateNameEvent event) {
		final String message = "The following role has a new name" + event.getRole().getName();
		System.out.println(message);
		Init.getServerOwner().getPrivateChannel().sendMessage(message).queue();
	}

	// TODO: Handle more role events

}
