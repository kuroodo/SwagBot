/**
 * Listens to all text channels
 */
package kuroodo.discordbot.listeners;

import kuroodo.discordbot.Init;
import kuroodo.discordbot.entities.JDAListener;
import kuroodo.discordbot.helpers.ChatLogger;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.events.message.priv.PrivateMessageReceivedEvent;

public class ChatListener extends JDAListener {

	public void update(float delta) {
	}

	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		try {
			// Use some magic to remove uneccesary characters. Only get letters
			String channelName = event.getChannel().toString().replaceAll("[^A-za-z]+", "");

			String message = "[" + event.getGuild().getName() + " [" + channelName + "]" + "] " + event.getAuthorName()
					+ ":" + event.getMessage().getContent();

			System.out.printf(message);
			ChatLogger.logMessage(message);

		} catch (StringIndexOutOfBoundsException e) {
			System.out.println("[" + event.getChannel().getName() + "]" + "[" + event.getAuthor().getUsername() + "] "
					+ " has sent an image/file or some other form of media or unsupported text");
		}
	}

	@Override
	public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
		super.onPrivateMessageReceived(event);

		// Check if enter authorization password and if already authorized
		try {
			if (event.getMessage().getRawContent().equals(Init.SUPERUSER_PASSWORD)) {
				for (User authorizedUser : Init.SUPER_USERS) {
					if (authorizedUser == event.getAuthor()) {
						event.getChannel().sendMessageAsync("You're already an authorized user!", null);
						break;
					} else {
						Init.SUPER_USERS.add(event.getAuthor());
					}
				}
			}
		} catch (Exception e) {

		}

		ChatLogger.logMessage(
				"[PRIVATE MESSAGE] " + event.getAuthor().getUsername() + ":" + event.getMessage().getRawContent());

	}
}
