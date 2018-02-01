package kuroodo.discordbot.listeners;

import kuroodo.discordbot.entities.JDAListener;
import kuroodo.discordbot.helpers.JDAHelper;
import kuroodo.discordbot.helpers.JSonReader;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceMoveEvent;

public class VoiceChannelListener extends JDAListener {

	private VoiceChannel muteChannel;
	private Role muteRole;

	public VoiceChannelListener() {
		muteChannel = JDAHelper.getVoiceChannelByName(JSonReader.getPreferencesValue("mutechannel"));
		muteRole = JDAHelper.getRoleByName(JSonReader.getPreferencesValue("muterole"));
	}

	@Override
	public void onGuildVoiceMove(GuildVoiceMoveEvent event) {
		if (muteChannel != null && muteRole != null) {

			if (event.getChannelJoined() == muteChannel) {
				if (!JDAHelper.isMemberAdmin(event.getMember()) && !JDAHelper.isMemberModerator(event.getMember())
						&& !JDAHelper.isMemberSeer(event.getMember())) {
					JDAHelper.giveRoleToMember(muteRole, event.getMember());
				}
			} else if (JDAHelper.memberHasRole(event.getMember(), muteRole)) {
				JDAHelper.removeRoleFromMember(muteRole, event.getMember());
			}
		}
		super.onGuildVoiceMove(event);
	}

	@Override
	public void update(float delta) {
	}
}