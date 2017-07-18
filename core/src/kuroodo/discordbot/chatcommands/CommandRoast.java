package kuroodo.discordbot.chatcommands;

import kuroodo.discordbot.entities.ChatCommand;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class CommandRoast extends ChatCommand {

	public CommandRoast() {
		super();
	}

	@Override
	public void executeCommand(String commandParams, GuildMessageReceivedEvent event) {
		super.executeCommand(commandParams, event);

		if (!event.getMessage().getMentionedUsers().isEmpty()) {
			User user = event.getMessage().getMentionedUsers().get(0);

			if (user != null) {
				event.getMessage().delete();
				sendMessage(user.getAsMention() + " https://www.youtube.com/watch?v=_tWC5qtfby4");
			}
		} else {
			sendMessage("Please mention a valid user");
		}

	}

	@Override
	public String info() {
		return "A hidden command, send a video notifying a victim that they got roasted. Usage: !roasted [user]"
				+ "\nExample: !roasted @user";
	}

}
