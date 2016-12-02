/**
 * Listens to all text channels
 */
package kuroodo.discordbot.listeners;

import kuroodo.discordbot.Init;
import kuroodo.discordbot.entities.JDAListener;
import kuroodo.discordbot.helpers.ChatLogger;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;

public class ChatListener extends JDAListener {

	public void update(float delta) {
	}

	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		try {
			// Use some magic to remove uneccesary characters. Only get letters
			String channelName = event.getChannel().toString().replaceAll("[^A-za-z]+", "");

			String message = "";

			if (event.getAuthor() != null) {
				message = "[" + event.getGuild().getName() + " [" + channelName + "]" + "] " + event.getAuthor().getName()
						+ ":" + event.getMessage().getContent();
			} else {
				message = "[" + event.getGuild().getName() + " [" + channelName + "]" + "] "
						+ "Null User Or Unknown Bot" + ":" + event.getMessage().getContent();
			}

			System.out.println(message);
			ChatLogger.logMessage(message);

		} catch (StringIndexOutOfBoundsException e) {
			if (event.getAuthor() == null) {
				System.out.println(
						"[" + event.getChannel().getName() + "]" + "A null user or unknown bot has sent a message");
			} else {
				System.out.println("[" + event.getChannel().getName() + "]" + "[" + event.getAuthor().getName()
						+ "] " + " has sent an image/file or some other form of media or unsupported text");
			}
		}
	}

	@Override
	public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
		super.onPrivateMessageReceived(event);

		// Check if enter authorization password and if already authorized
		try {
			if (!Init.SUPERUSER_PASSWORD.isEmpty()
					&& event.getMessage().getRawContent().equals(Init.SUPERUSER_PASSWORD)) {
				for (User authorizedUser : Init.SUPER_USERS) {
					if (authorizedUser == event.getAuthor()) {
						event.getChannel().sendMessage("You're already an authorized user!").queue();
						break;
					} else {
						Init.SUPER_USERS.add(event.getAuthor());
					}
				}
			}
		} catch (Exception e) {

		}

		ChatLogger.logMessage(
				"[PRIVATE MESSAGE] " + event.getAuthor().getName() + ":" + event.getMessage().getRawContent());

	}
}
