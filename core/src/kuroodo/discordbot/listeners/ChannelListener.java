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

public class ChannelListener extends JDAListener {

	public void update(float Delta) {

	}

	@Override
	public void onTextChannelCreate(TextChannelCreateEvent event) {
		String message = "A TextChannel named: " + event.getChannel().getName() + " was created";
		Init.getServerOwner().getPrivateChannel().sendMessageAsync(message, null);
		System.out.println("EVENT: " + message);
	}

	@Override
	public void onTextChannelDelete(TextChannelDeleteEvent event) {
		String message = "A TextChannel named: " + event.getChannel().getName() + " was deleted";
		Init.getServerOwner().getPrivateChannel().sendMessageAsync(message, null);
		System.out.println("EVENT: " + message);
	}

	@Override
	public void onTextChannelUpdateName(TextChannelUpdateNameEvent event) {
		String message = "TextChannel " + event.getOldName() + " was renamed: " + event.getChannel().getName();
		Init.getServerOwner().getPrivateChannel().sendMessageAsync(message, null);
		System.out.println("EVENT: " + message);
	}

	@Override
	public void onTextChannelUpdatePermissions(TextChannelUpdatePermissionsEvent event) {
		String message = "The permissions for " + event.getChannel().getName() + " were changed";
		Init.getServerOwner().getPrivateChannel().sendMessageAsync(message, null);
		System.out.println("EVENT: " + message);
	}

	// ------------------------------
	// ---- VoiceChannel Events -----
	// ------------------------------
	@Override
	public void onVoiceChannelCreate(VoiceChannelCreateEvent event) {
		String message = "A VoiceChannel named: " + event.getChannel().getName() + " was created";
		Init.getServerOwner().getPrivateChannel().sendMessageAsync(message, null);
		System.out.println("EVENT: " + message);
	}

	@Override
	public void onVoiceChannelDelete(VoiceChannelDeleteEvent event) {
		String message = "A VoiceChannel named: " + event.getChannel().getName() + " was deleted";
		Init.getServerOwner().getPrivateChannel().sendMessageAsync(message, null);
		System.out.println("EVENT: " + message);
	}

	@Override
	public void onVoiceChannelUpdateName(VoiceChannelUpdateNameEvent event) {
		String message = "VoiceChannel " + event.getOldName() + " was renamed: " + event.getChannel().getName();
		Init.getServerOwner().getPrivateChannel().sendMessageAsync(message, null);
		System.out.println("EVENT: " + message);
	}

	@Override
	public void onVoiceChannelUpdatePermissions(VoiceChannelUpdatePermissionsEvent event) {
		String message = "The permissions for " + event.getChannel().getName() + " were changed";
		Init.getServerOwner().getPrivateChannel().sendMessageAsync(message, null);
		System.out.println("EVENT: " + message);
	}
}
