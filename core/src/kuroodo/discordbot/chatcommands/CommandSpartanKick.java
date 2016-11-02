package kuroodo.discordbot.chatcommands;

import kuroodo.discordbot.entities.ChatCommand;
import kuroodo.discordbot.helpers.JDAHelper;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

public class CommandSpartanKick extends ChatCommand {
	@Override
	public void executeCommand(String commandParams, GuildMessageReceivedEvent event) {
		super.executeCommand(commandParams, event);
		User user = JDAHelper.getUserByID(commandParameters);

		if (user == null) {
			user = JDAHelper.getUserByUsername(commandParameters);
			if (user == null) {
				if (!event.getMessage().getMentionedUsers().isEmpty()) {
					user = event.getMessage().getMentionedUsers().get(0);
				}
			}
		}

		if (user != null && JDAHelper.getUserVoiceChannel(user.getUsername()) != null) {
			int voiceChannelSize = JDAHelper.getVoiceChannelCount() - 1;
			int usersCurrentVoiceChannelIndex = JDAHelper.getUserVoiceChannelIndex(user.getUsername());

			final int totalKicks = 3;
			for (int i = 0; i < totalKicks; i++) {

				usersCurrentVoiceChannelIndex++;
				if (usersCurrentVoiceChannelIndex > voiceChannelSize) {
					usersCurrentVoiceChannelIndex = 0;
				}
				JDAHelper.getGuild().getManager().moveVoiceUser(user,
						JDAHelper.getVoiceChannels().get(usersCurrentVoiceChannelIndex));
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