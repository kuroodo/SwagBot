/**
 * Listens to any server-based activity
 */
package kuroodo.discordbot.listeners;

import kuroodo.discordbot.Init;
import kuroodo.discordbot.entities.JDAListener;
import kuroodo.discordbot.helpers.ChatHelper;
import net.dv8tion.jda.entities.Role;
import net.dv8tion.jda.events.DisconnectEvent;
import net.dv8tion.jda.events.ReconnectedEvent;
import net.dv8tion.jda.events.guild.member.GuildMemberBanEvent;
import net.dv8tion.jda.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.events.guild.member.GuildMemberUnbanEvent;
import net.dv8tion.jda.events.guild.role.GuildRoleCreateEvent;
import net.dv8tion.jda.events.guild.role.GuildRoleDeleteEvent;
import net.dv8tion.jda.events.guild.role.GuildRoleUpdateNameEvent;
import net.dv8tion.jda.events.guild.role.GuildRoleUpdatePermissionEvent;

public class ServerListener extends JDAListener {

	public void update(float Delta) {

	}

	@Override
	public void onDisconnect(DisconnectEvent event) {
		super.onDisconnect(event);
	}

	@Override
	public void onReconnect(ReconnectedEvent event) {
		Init.getJDA().getAccountManager().setGame("Type !help For Help (;");
		super.onReconnect(event);
	}

	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {

		// TODO: Put this string in a better scope. Lok at todo in
		// DesktopLauncher
		final String WELCOME_CHANNEL_NAME = "general";

		// TODO: Create variable for the role to give
		ChatHelper.giveRoleToUser(ChatHelper.getRoleByName("Member"), event.getUser());

		String message = " User " + event.getUser().getUsername() + " joined the server";
		System.out.println(message);
		Init.getServerOwner().getPrivateChannel().sendMessageAsync(message, null);

		message = "Welcome " + event.getUser().getAsMention();
		ChatHelper.getTextChannelByName(WELCOME_CHANNEL_NAME).sendMessageAsync(message, null);

		super.onGuildMemberJoin(event);
	}

	@Override
	public void onGuildMemberBan(GuildMemberBanEvent event) {
		String message = "User " + event.getUser().getUsername() + " was banned from the server";

		System.out.println(message);
		Init.getServerOwner().getPrivateChannel().sendMessageAsync(message, null);
	}

	@Override
	public void onGuildMemberUnban(GuildMemberUnbanEvent event) {
		final String message = "User " + event.getUser().getUsername() + " was unbanned from the server";

		System.out.println(message);
		Init.getServerOwner().getPrivateChannel().sendMessageAsync(message, null);
	}

	@Override
	public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
		final String message = "User " + event.getUser().getUsername() + " has left server, or been kicked";

		System.out.println(message);
		Init.getServerOwner().getPrivateChannel().sendMessageAsync(message, null);
	}

	@Override
	public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event) {
		String message = "User " + event.getUser().getUsername() + " was just given the following roles: ";

		for (Role role : event.getRoles()) {
			message = message + role.getName();

			if (role.getName().equals("Admin") || role.getName().equals("Moderator")) {

				String secondMsg = "Welcome to the staff team for " + event.getGuild().getName()
						+ ". Please type !commands or !help in the server chat to review the available staff commands";

				event.getUser().getPrivateChannel().sendMessageAsync(secondMsg, null);
			}
		}
		System.out.println(message);
		Init.getServerOwner().getPrivateChannel().sendMessageAsync(message, null);
	}

	@Override
	public void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent event) {
		String message = event.getUser().getUsername() + " just had the following roles removed: ";
		for (Role r : event.getRoles()) {
			message = message + r.getName() + ", ";
		}

		System.out.println(message);
		Init.getServerOwner().getPrivateChannel().sendMessageAsync(message, null);
	}

	@Override
	public void onGuildRoleCreate(GuildRoleCreateEvent event) {
		final String message = "New role " + event.getRole().getName() + " has been created";
		System.out.println(message);
		Init.getServerOwner().getPrivateChannel().sendMessageAsync(message, null);
	}

	@Override
	public void onGuildRoleDelete(GuildRoleDeleteEvent event) {
		final String message = "The following role was deleted  " + event.getGuild().getName();
		System.out.println(message);
		Init.getServerOwner().getPrivateChannel().sendMessageAsync(message, null);
	}

	@Override
	public void onGuildRoleUpdatePermission(GuildRoleUpdatePermissionEvent event) {
		final String message = "The following role's permissions were changed  " + event.getRole().getName();
		System.out.println(message);
		Init.getServerOwner().getPrivateChannel().sendMessageAsync(message, null);
		super.onGuildRoleUpdatePermission(event);
	}

	@Override
	public void onGuildRoleUpdateName(GuildRoleUpdateNameEvent event) {
		final String message = "The following role has a new name" + event.getRole().getName();
		System.out.println(message);
		Init.getServerOwner().getPrivateChannel().sendMessageAsync(message, null);
		super.onGuildRoleUpdateName(event);
	}

}
