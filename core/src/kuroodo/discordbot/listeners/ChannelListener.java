/**
 * Listens to all channel activity
 */
package kuroodo.discordbot.listeners;

import java.util.function.Consumer;

import kuroodo.discordbot.Init;
import kuroodo.discordbot.entities.JDAListener;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.channel.text.TextChannelCreateEvent;
import net.dv8tion.jda.core.events.channel.text.TextChannelDeleteEvent;
import net.dv8tion.jda.core.events.channel.text.update.TextChannelUpdateNameEvent;
import net.dv8tion.jda.core.events.channel.text.update.TextChannelUpdatePermissionsEvent;
import net.dv8tion.jda.core.events.channel.voice.VoiceChannelCreateEvent;
import net.dv8tion.jda.core.events.channel.voice.VoiceChannelDeleteEvent;
import net.dv8tion.jda.core.events.channel.voice.update.VoiceChannelUpdateNameEvent;
import net.dv8tion.jda.core.events.channel.voice.update.VoiceChannelUpdatePermissionsEvent;

public class ChannelListener extends JDAListener {

	public void update(float Delta) {

	}

	@Override
	public void onTextChannelCreate(TextChannelCreateEvent event) {
		String message = "A TextChannel named: " + event.getChannel().getName() + " was created";

		sendPrivateMessage(Init.getServerOwner(), message);

		System.out.println("EVENT: " + message);
	}

	@Override
	public void onTextChannelDelete(TextChannelDeleteEvent event) {
		if (!event.getChannel().getName().startsWith("gamesession")) {
			String message = "A TextChannel named: " + event.getChannel().getName() + " was deleted";

			sendPrivateMessage(Init.getServerOwner(), message);

			System.out.println("EVENT: " + message);
		}
	}

	@Override
	public void onTextChannelUpdateName(TextChannelUpdateNameEvent event) {
		if (!event.getChannel().getName().startsWith("gamesession")) {
			String message = "TextChannel " + event.getOldName() + " was renamed: " + event.getChannel().getName();

			sendPrivateMessage(Init.getServerOwner(), message);

			System.out.println("EVENT: " + message);
		}
	}

	@Override
	public void onTextChannelUpdatePermissions(TextChannelUpdatePermissionsEvent event) {
		if (!event.getChannel().getName().startsWith("gamesession")) {
			String message = "The permissions for " + event.getChannel().getName() + " were changed";

			sendPrivateMessage(Init.getServerOwner(), message);

			System.out.println("EVENT: " + message);
		}
	}

	// ------------------------------
	// ---- VoiceChannel Events -----
	// ------------------------------
	@Override
	public void onVoiceChannelCreate(VoiceChannelCreateEvent event) {
		String message = "A VoiceChannel named: " + event.getChannel().getName() + " was created";

		sendPrivateMessage(Init.getServerOwner(), message);

		System.out.println("EVENT: " + message);
	}

	@Override
	public void onVoiceChannelDelete(VoiceChannelDeleteEvent event) {
		String message = "A VoiceChannel named: " + event.getChannel().getName() + " was deleted";

		sendPrivateMessage(Init.getServerOwner(), message);

		System.out.println("EVENT: " + message);
	}

	@Override
	public void onVoiceChannelUpdateName(VoiceChannelUpdateNameEvent event) {
		String message = "VoiceChannel " + event.getOldName() + " was renamed: " + event.getChannel().getName();

		sendPrivateMessage(Init.getServerOwner(), message);

		System.out.println("EVENT: " + message);
	}

	@Override
	public void onVoiceChannelUpdatePermissions(VoiceChannelUpdatePermissionsEvent event) {
		String message = "The permissions for " + event.getChannel().getName() + " were changed";

		sendPrivateMessage(Init.getServerOwner(), message);

		System.out.println("EVENT: " + message);
	}

	private void sendPrivateMessage(User user, String message) {
		user.openPrivateChannel().queue(new Consumer<PrivateChannel>() {
			@Override
			public void accept(PrivateChannel t) {
				t.sendMessage(message).queue();
			}
		});
	}

}
