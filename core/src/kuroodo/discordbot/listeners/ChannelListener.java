/**
 * Listens to all channel activity
 */
package kuroodo.discordbot.listeners;

import kuroodo.discordbot.Init;
import kuroodo.discordbot.entities.JDAListener;
import net.dv8tion.jda.events.channel.text.TextChannelCreateEvent;
import net.dv8tion.jda.events.channel.text.TextChannelDeleteEvent;
import net.dv8tion.jda.events.channel.text.TextChannelUpdateNameEvent;
import net.dv8tion.jda.events.channel.text.TextChannelUpdatePermissionsEvent;
import net.dv8tion.jda.events.channel.voice.VoiceChannelCreateEvent;
import net.dv8tion.jda.events.channel.voice.VoiceChannelDeleteEvent;
import net.dv8tion.jda.events.channel.voice.VoiceChannelUpdateNameEvent;
import net.dv8tion.jda.events.channel.voice.VoiceChannelUpdatePermissionsEvent;
import net.dv8tion.jda.exceptions.RateLimitedException;

public class ChannelListener extends JDAListener {

	public void update(float Delta) {

	}

	@Override
	public void onTextChannelCreate(TextChannelCreateEvent event) {
		Init.getServerOwner().getPrivateChannel()
				.sendMessageAsync("A TextChannel named: " + event.getChannel().getName() + " was created", null);

		System.out.println("EVENT: A TextChannel named: " + event.getChannel().getName() + " was created");
	}

	@Override
	public void onTextChannelDelete(TextChannelDeleteEvent event) {
		Init.getServerOwner().getPrivateChannel()
				.sendMessageAsync("A TextChannel named: " + event.getChannel().getName() + " was deleted", null);

		System.out.println("EVENT: A TextChannel named: " + event.getChannel().getName() + " was deleted");
	}

	@Override
	public void onTextChannelUpdateName(TextChannelUpdateNameEvent event) {
		Init.getServerOwner().getPrivateChannel().sendMessageAsync(
				"TextChannel " + event.getOldName() + " was renamed: " + event.getChannel().getName(), null);

		System.out
				.println("EVENT: TextChannel " + event.getOldName() + " was renamed: " + event.getChannel().getName());
	}

	@Override
	public void onTextChannelUpdatePermissions(TextChannelUpdatePermissionsEvent event) {
		try {
			Init.getServerOwner().getPrivateChannel()
					.sendMessageAsync("The permissions for " + event.getChannel().getName() + " were changed", null);

			System.out.println("EVENT: The permissions for " + event.getChannel().getName() + " were changed");
		} catch (RateLimitedException e) {
			System.out.println("ERROR: MESSAGE GOT RATE LIMITED");
		}
	}

	// ------------------------------
	// ---- VoiceChannel Events -----
	// ------------------------------
	@Override
	public void onVoiceChannelCreate(VoiceChannelCreateEvent event) {
		Init.getServerOwner().getPrivateChannel()
				.sendMessageAsync("A VoiceChannel named: " + event.getChannel().getName() + " was created", null);

		System.out.println("EVENT: A VoiceChannel named: " + event.getChannel().getName() + " was created");
	}

	@Override
	public void onVoiceChannelDelete(VoiceChannelDeleteEvent event) {
		Init.getServerOwner().getPrivateChannel()
				.sendMessageAsync("A VoiceChannel named: " + event.getChannel().getName() + " was deleted", null);

		System.out.println("EVENT: A VoiceChannel named: " + event.getChannel().getName() + " was deleted");
	}

	@Override
	public void onVoiceChannelUpdateName(VoiceChannelUpdateNameEvent event) {
		Init.getServerOwner().getPrivateChannel().sendMessageAsync(
				"VoiceChannel " + event.getOldName() + " was renamed: " + event.getChannel().getName(), null);

		System.out
				.println("EVENT: VoiceChannel " + event.getOldName() + " was renamed: " + event.getChannel().getName());
	}

	@Override
	public void onVoiceChannelUpdatePermissions(VoiceChannelUpdatePermissionsEvent event) {
		Init.getServerOwner().getPrivateChannel()
				.sendMessageAsync("The permissions for " + event.getChannel().getName() + " were changed", null);

		System.out.println("EVENT: The permissions for " + event.getChannel().getName() + " were changed");
	}
}
