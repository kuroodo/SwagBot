package kuroodo.discordbot.chatcommands.moderator;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

import kuroodo.discordbot.Init;
import kuroodo.discordbot.entities.ChatCommand;
import kuroodo.discordbot.helpers.JDAHelper;
import kuroodo.discordbot.helpers.JSonReader;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.managers.GuildController;

public class CommandSilence extends ChatCommand {
	private List<Role> userRoles;
	private boolean isTimed = false;

	public CommandSilence() {
		isModCommand = true;
		userRoles = new ArrayList<Role>();
	}

	@Override
	public void executeCommand(String commandParams, GuildMessageReceivedEvent event) {
		super.executeCommand(commandParams, event);

		if (!isUserAuthorized) {
			return;
		}

		Member memberToSilence = JDAHelper.getMember(event.getMessage().getMentionedUsers().get(0));

		String reasonForSilence;

		if (commandParameters.contains("$")) {
			isTimed = true;
			reasonForSilence = JDAHelper
					.splitString(commandParameters.substring(commandParameters.lastIndexOf("$") + 1))[1];
		} else {
			reasonForSilence = JDAHelper.splitString(commandParameters)[1];
		}

		if (memberToSilence != null) {
			// Check if trying to ban bot
			if (memberToSilence.getUser() == Init.getJDA().getSelfUser()) {

				sendPrivateMessage(Init.getServerOwner(), event.getAuthor().getName() + " Just tried to silence me!");

				event.getChannel().sendMessage(event.getAuthor().getAsMention() + " You dare to conspire against me?")
						.queue();

				// Check if trying to ban server owner
			} else if (memberToSilence.getUser() == Init.getServerOwner()) {
				sendPrivateMessage(Init.getServerOwner(), event.getAuthor().getName() + " Just tried to silence you!");

				event.getChannel()
						.sendMessage(event.getAuthor().getAsMention() + " You dare to conspire against the server?")
						.queue();

			} else if (JDAHelper.isMemberAdmin(JDAHelper.getMember(event.getAuthor()))) {
				sendSilenceNotification(memberToSilence, reasonForSilence);
				silenceUser(memberToSilence);
			} else if (JDAHelper.isMemberModerator(JDAHelper.getMember(event.getAuthor()))) {

				// Check if moderator is trying to ban an admin
				if (JDAHelper.isMemberAdmin(memberToSilence)) {
					sendPrivateMessage(Init.getServerOwner(),
							event.getAuthor().getName() + " Just tried to silence an admin");

					event.getChannel()
							.sendMessage(event.getAuthor().getAsMention() + " Moderators cannot silence admins!")
							.queue();
				} else {
					sendSilenceNotification(memberToSilence, reasonForSilence);
					silenceUser(memberToSilence);
				}
			}
		}
		event.getMessage().delete();

	}

	private void silenceUser(Member memberToSilence) {
		final String SILENCEROLENAME = "Silenced";
		float duration = 0;

		if (isTimed) {
			duration = Float.parseFloat(
					JDAHelper.splitString(commandParameters.substring(commandParameters.lastIndexOf("$") + 1))[0]);
			if (duration < 120f) {
				sendMessage("ERROR: Minimum duration is 120 seconds");
				return;
			}
		}

		userRoles = memberToSilence.getRoles();

		GuildController manager = JDAHelper.getGuild().getController();

		manager.removeRolesFromMember(memberToSilence, memberToSilence.getRoles()).queue(new Consumer<Void>() {

			@Override
			public void accept(Void t) {
				manager.addRolesToMember(memberToSilence, JDAHelper.getRoleByName(SILENCEROLENAME)).queue();
			}
		});

		if (isTimed) {
			startSilenceTimer(memberToSilence, duration);
		}
	}

	private void startSilenceTimer(Member memberToTempBan, float duration) {
		Timer.schedule(new Task() {

			@Override
			public void run() {
				finishSilenceTimer(memberToTempBan);
			}
		}, duration);
	}

	private void finishSilenceTimer(Member member) {
		GuildController manager = JDAHelper.getGuild().getController();

		manager.removeRolesFromMember(member, member.getRoles()).queue(new Consumer<Void>() {

			@Override
			public void accept(Void t) {
				manager.addRolesToMember(member, userRoles).queue();
			}
		});
	}

	private void sendSilenceNotification(Member memberToTempBan, String reason) {
		TextChannel adminChannel = JDAHelper.getTextChannelByName(JSonReader.getPreferencesValue("adminchannel"));

		if (adminChannel != null) {
			adminChannel.sendMessage(memberToTempBan.getUser().getName() + " has been silenced for " + reason).queue();
		}

		sendPrivateMessage(memberToTempBan.getUser(),
				"You have been silenced from " + JDAHelper.getGuild().getName() + " for " + reason);
	}

	@Override
	public String info() {
		return "Temporarily silences a user. usage !silence [user] [reason] . "
				+ "Example: !silence @user starting too much drama"
				+ "\nCan also silence for a duration (in seconds).Include a $ symbol before the duration.\n"
				+ "Usage !silence [user] $[time] [reason] . Example: !silence @user $120 starting too much drama";
	}

}
