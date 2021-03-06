package kuroodo.discordbot.chatcommands;

import kuroodo.discordbot.entities.ChatCommand;
import kuroodo.discordbot.helpers.JDAHelper;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class CommandSpartanKick extends ChatCommand {

	// TODO: User a timer instead of sleeping thread
	@Override
	public void executeCommand(String commandParams, GuildMessageReceivedEvent event) {
		super.executeCommand(commandParams, event);

		Member member = findMember(event);

		if (member != null && JDAHelper.getUserVoiceChannel(member.getUser().getName()) != null) {
			int voiceChannelSize = JDAHelper.getVoiceChannelCount() - 1;
			int usersCurrentVoiceChannelIndex = JDAHelper.getUserVoiceChannelIndex(member.getUser().getName());

			final int totalKicks = 3;
			for (int i = 0; i < totalKicks; i++) {

				usersCurrentVoiceChannelIndex++;
				if (usersCurrentVoiceChannelIndex > voiceChannelSize) {
					usersCurrentVoiceChannelIndex = 0;
				}
				JDAHelper.getGuild().getController()
						.moveVoiceMember(member, JDAHelper.getVoiceChannels().get(usersCurrentVoiceChannelIndex))
						.queue();
				try {
					// Wait for a like less than a second before continuing
					Thread.sleep(250);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private Member findMember(GuildMessageReceivedEvent event) {
		if (!event.getMessage().getMentionedUsers().isEmpty()) {
			return JDAHelper.getMember(event.getMessage().getMentionedUsers().get(0));
		}

		sendMessage("Please mention a valid user");
		return null;
	}

	@Override
	public String info() {
		return "Spartankick someone across voice channels. usage !spartankick [user] example: !spartankick @user";
	}
}