package kuroodo.discordbot.chatcommands;

import kuroodo.discordbot.entities.ChatCommand;
import kuroodo.discordbot.helpers.ChatHelper;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

public class CommandSpartanKick extends ChatCommand {
	@Override
	public void executeCommand(String commandParams, GuildMessageReceivedEvent event) {
		super.executeCommand(commandParams, event);
		User user = ChatHelper.getUserByID(commandParameters);

		if (user == null) {
			user = ChatHelper.getUserByUsername(commandParameters);
			if (user == null) {
				if (!event.getMessage().getMentionedUsers().isEmpty()) {
					user = event.getMessage().getMentionedUsers().get(0);
				}
			}
		}

		if (user != null) {
			int voiceChannelSize = event.getGuild().getVoiceChannels().size() - 1;
			int usersCurrentVoiceChannel = ChatHelper.getUserVoiceChannelIndex(user.getUsername());

			final int kickCount = 3;
			for (int i = 0; i < kickCount; i++) {

				usersCurrentVoiceChannel++;
				if (usersCurrentVoiceChannel > voiceChannelSize) {
					usersCurrentVoiceChannel = 0;
				}
				ChatHelper.getGuild().getManager().moveVoiceUser(user,
						ChatHelper.getVoiceChannels().get(usersCurrentVoiceChannel));
				try {
					// Wait for a like less than a second before continuing
					Thread.sleep(250);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public String info() {
		return "Spartankick someone across voice channels. usage !spartankick [user] example: !spartankick faggottron9000";
	}

}
