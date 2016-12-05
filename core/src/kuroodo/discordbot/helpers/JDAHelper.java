/**
 * Helps simplify the programs code by creating helper methods that perform a certain function.
 */
package kuroodo.discordbot.helpers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import kuroodo.discordbot.Init;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.managers.GuildController;

public class JDAHelper {

	public static TextChannel getTextChannelByName(String channelName) {
		for (TextChannel channel : getGuild().getTextChannels()) {
			if (channel.getName().equals(channelName)) {
				return channel;
			}
		}
		System.out.println("ERROR Could not get text channel by name");
		return null;
	}

	public static int getTextChannelCount() {
		return getGuild().getTextChannels().size();
	}

	public static List<TextChannel> getTextChannels() {
		return getGuild().getTextChannels();
	}

	public static VoiceChannel getVoiceChannelByName(String channelName) {
		for (VoiceChannel channel : getGuild().getVoiceChannels()) {
			if (channel.getName().equals(channelName)) {
				return channel;
			}
		}
		System.out.println("ERROR Could not get voice channel by name");
		return null;
	}

	/**
	 * Gets the voice channel a specific user is in
	 * 
	 * @param username
	 *            the users username
	 * @return the voice channel
	 */
	public static VoiceChannel getUserVoiceChannel(String username) {
		for (VoiceChannel channel : getGuild().getVoiceChannels()) {
			for (Member member : channel.getMembers()) {
				if (member.getUser().getName().equals(username)) {
					return channel;
				}
			}
		}
		return null;
	}

	public static int getVoiceChannelCount() {
		return getGuild().getVoiceChannels().size();
	}

	public static List<VoiceChannel> getVoiceChannels() {
		return getGuild().getVoiceChannels();
	}

	/**
	 * Gets the voice channel index of a channel that a specific user is in
	 * 
	 * @param username
	 *            the users username
	 * @return the voice channels index in the list of voice channels
	 */
	public static Integer getUserVoiceChannelIndex(String username) {
		for (int voiceChannelIndex = 0; voiceChannelIndex < getVoiceChannelCount(); voiceChannelIndex++) {
			for (Member member : getVoiceChannels().get(voiceChannelIndex).getMembers()) {
				if (member.getUser().getName().equals(username)) {
					return voiceChannelIndex;
				}
			}
		}

		System.out.println("Error, could not return a channel index");
		return 0;
	}

	public static User getUserByUsername(String username) {
		for (Member member : getGuild().getMembers()) {
			if (member.getUser().getName().equals(username)) {
				return member.getUser();
			}
		}

		System.out.println("ERROR Could Not Get User By Username");
		return null;
	}

	public static Member getMemberByUsername(String username) {
		for (Member member : getGuild().getMembers()) {
			if (member.getUser().getName().equals(username)) {
				return member;
			}
		}

		System.out.println("ERROR Could Not Get MEMBER By Username");
		return null;
	}

	public static User getUserByID(String ID) {
		Member memberToGet = getGuild().getMemberById(ID);
		if (memberToGet != null) {
			return getGuild().getMemberById(ID).getUser();
		} else {
			System.out.println("ERROR could not get user by ID!");
			return null;
		}
	}

	public static Member getMemberByID(String ID) {
		return getGuild().getMemberById(ID);
	}

	public static String getUsernameById(String ID) {
		return getUserByID(ID).getName();
	}

	public static User getRandomOnlineUser() {
		Random rand = new Random();
		rand.setSeed(System.nanoTime());

		ArrayList<User> onlineUsers = new ArrayList<User>();

		for (Member member : getGuild().getMembers()) {
			if (member.getOnlineStatus() == OnlineStatus.ONLINE) {
				onlineUsers.add(member.getUser());
			}
		}

		return onlineUsers.get(rand.nextInt(onlineUsers.size()));
	}

	public static User getRandomUser() {
		Random rand = new Random();
		rand.setSeed(System.nanoTime());
		return getMembers().get(rand.nextInt(getUserCount())).getUser();
	}

	public static void giveRoleToMember(Role role, Member member) {
		if (member == null || role == null) {
			System.out.println("ERROR: COULD NOT GIVE USER A ROLE");
			return;
		}
		getGuild().getController().addRolesToMember(member, role).queue();
		// GuildController guilControllerr = getGuild().getController();
		// guildManager.addRoleToUser(user, role).update();
	}

	public static Role getRoleByName(String roleName) {
		for (Role role : getGuild().getRoles()) {
			if (role.getName().equals(roleName)) {
				return role;
			}
		}
		System.out.println("Error, could not get role by name");
		return null;
	}

	public static Boolean isMemberAdmin(Member member) {
		for (Role role : member.getRoles()) {
			if (role.getName().equals("Admin")) {
				return true;
			}
		}
		return false;
	}

	public static Boolean isMemberModerator(Member member) {
		for (Role role : member.getRoles()) {
			if (role.getName().equals("Moderator")) {
				return true;
			}
		}
		return false;
	}

	public static boolean isUsernameValidUser(String userName) {
		return getUserByUsername(userName) != null;
	}

	public static int getUserCount() {
		return getGuild().getMembers().size();
	}

	public static List<Member> getMembers() {
		return getGuild().getMembers();
	}

	public static Guild getGuild() {
		return Init.getJDA().getGuilds().get(0);
	}

	public static Member getMember(User user) {
		return getGuild().getMember(user);
	}

	/**
	 * Checks the list of audioqueues to find out if a command/message is an
	 * audioqueue
	 * 
	 * @param The
	 *            message/command that the entered. Specifically the word that
	 *            may reperesnt the audioqueue
	 * @return Whether it was an audioqueue or not (true/false)
	 */
	public static boolean isMessageAnAudioQueue(String message) {
		try {
			return new File("sounds/" + message.substring(1) + ".mp3").exists();
		} catch (Exception e) {
			return false;
		}
	}

	public static GuildController removeUsersRoles(Member member, GuildController controller) {
		controller.removeRolesFromMember(member, member.getRoles()).queue();
		return controller;
	}

	// Split a string in 2
	public static String[] splitString(String stringToSplit) {
		String[] splittedMessage = { "", "" };
		splittedMessage[1] = stringToSplit;

		if (stringToSplit.contains(" ")) {
			splittedMessage[0] = stringToSplit.split(" ", -1)[0];
			splittedMessage[1] = splittedMessage[1].replaceAll(splittedMessage[0] + " ", "");

		} else {
			splittedMessage[0] = stringToSplit;
			splittedMessage[1] = "";
		}

		// System.out.println("debug");
		// System.out.println(splittedMessage[0]);
		// System.out.println(splittedMessage[1]);

		return splittedMessage;
	}
}
