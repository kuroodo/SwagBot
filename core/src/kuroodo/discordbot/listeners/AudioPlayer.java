/**
 *	Manages the bots ability to play audio, and any audio commands.
 */

package kuroodo.discordbot.listeners;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.PatternSyntaxException;

import javax.sound.sampled.UnsupportedAudioFileException;

import kuroodo.discordbot.Init;
import kuroodo.discordbot.entities.JDAListener;
import kuroodo.discordbot.helpers.JDAHelper;
import net.dv8tion.jda.audio.player.FilePlayer;
import net.dv8tion.jda.audio.player.Player;
import net.dv8tion.jda.audio.player.URLPlayer;
import net.dv8tion.jda.entities.VoiceChannel;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

public class AudioPlayer extends JDAListener {
	private ArrayList<String> blockedChannels;

	private Player player = null;
	private String chanName = "";

	private boolean connected = false;
	private final float maxIdleTime = 60f;
	private float currentIdleTime = 0f;

	/*
	 * This class is a complete mess. Thats because I originally did not know
	 * how it worked or what I was doing. It will eventually be replaced by
	 * JDA-Player
	 */
	public AudioPlayer() {
		blockedChannels = new ArrayList<>();
	}

	public void update(float delta) {

		if (Init.getJDA().getAudioManager(JDAHelper.getGuild()).isConnected()) {
			connected = true;
		}
		// Calculate time that bot isn't used. If idle for more than
		// maxIdleTime, close the audio connection
		try {
			if (connected && !player.isPaused()) {
				currentIdleTime += delta;

				if (currentIdleTime >= maxIdleTime && !player.isPlaying() && !player.isPaused()) {
					Init.getJDA().getAudioManager(JDAHelper.getGuild()).closeAudioConnection();
					resetIdleCount();
					connected = false;
				} else if (player.isPlaying()) {
					resetIdleCount();
				}
			}
		} catch (NullPointerException e) {

		}
	}

	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

		if (event.getAuthor() != null) {
			String message = event.getMessage().getContent();
			try {

				// Start an audio connection with a VoiceChannel
				if (message.startsWith("!")
						&& (message.startsWith("!join ") || JDAHelper.isMessageAnAudioQueue(message))) {

					if (JDAHelper.isMessageAnAudioQueue(message)) {
						chanName = JDAHelper.getUserVoiceChannel(event.getAuthor().getUsername()).getName();
					} else {
						// Separates the name of the channel so that we can
						// search
						// for
						// it
						chanName = message.substring(6);
					}

					// Scans through the VoiceChannels in this Guild, looking
					// for
					// one
					// with a case-insensitive matching name.
					VoiceChannel channel = event.getGuild().getVoiceChannels().stream()
							.filter(vChan -> vChan.getName().equalsIgnoreCase(chanName)).findFirst().orElse(null); // If
																													// there
																													// isn't
																													// a
																													// matching
																													// name,
																													// return
																													// null.
					if (channel == null) {
						event.getChannel().sendMessage(
								"There isn't a VoiceChannel in this Guild with the name: '" + chanName + "'");
						return;
					} else {
						for (String channelName : blockedChannels) {
							if (channel.getName().equals(channelName)) {
								event.getChannel().sendMessage("Unable to join '" + chanName + "'");
								return;
							}
						}
					}

					if (!event.getJDA().getAudioManager(JDAHelper.getGuild()).isConnected()) {
						System.out.println("Try");
						event.getJDA().getAudioManager(JDAHelper.getGuild()).openAudioConnection(channel);
						resetIdleCount();
					} else {
						if (event.getJDA().getAudioManager(JDAHelper.getGuild()).getConnectedChannel() != channel) {
							event.getJDA().getAudioManager(JDAHelper.getGuild()).moveAudioConnection(channel);
							resetIdleCount();
						}
					}
				}
				// Disconnect the audio connection with the VoiceChannel.
				if (message.equals("!leave")) {
					event.getJDA().getAudioManager(JDAHelper.getGuild()).closeAudioConnection();
					resetIdleCount();
				}

				// Start playing audio with our FilePlayer. If we haven't
				// created
				// and
				// registered a FilePlayer yet, do that.

				String command = "";
				String audioSourceType = "";
				String audioLocation = "";

				command = JDAHelper.splitString(message)[0];
				String secondString = JDAHelper.splitString(message)[1];

				audioSourceType = JDAHelper.splitString(secondString)[0];
				audioLocation = JDAHelper.splitString(secondString)[1];

				// System.out.println("Command: " + command);
				// System.out.println("source: " + audioSourceType);
				// System.out.println("location: " + audioLocation);

				if (command.equals("!play") || JDAHelper.isMessageAnAudioQueue(message)) {
					// If the player didn't exist or new audio command,
					// create it and start playback.
					if (player == null || audioSourceType.equals("url") || audioSourceType.equals("file")
							|| JDAHelper.isMessageAnAudioQueue(message)) {
						stopPlayer();
						player = null;
						File audioFile = null;
						URL audioUrl = null;

						try {
							if (JDAHelper.isMessageAnAudioQueue(message)) {
								audioFile = new File("sounds/" + message.substring(1) + ".mp3");

								player = new FilePlayer(audioFile);
							} else if (audioSourceType.equals("file")) {
								audioFile = new File(audioLocation);

								player = new FilePlayer(audioFile);
							} else if (audioSourceType.equals("url")) {
								audioUrl = new URL(audioLocation);

								player = new URLPlayer(event.getJDA(), audioUrl);
							}

							// Provide the handler to send audio.
							// NOTE: You don't have to set the handler each time
							// you
							// create an audio connection with the
							// AudioManager. Handlers persist between audio
							// connections.
							// Furthermore, handler playback is also
							// paused when a connection is severed
							// (closeAudioConnection), however it would probably
							// be
							// better
							// to pause the play back yourself before severing
							// the
							// connection (If you are using a player class
							// you could just call the pause() method.
							// Otherwise,
							// make
							// canProvide() return false).
							// Once again, you don't HAVE to pause before
							// severing
							// an
							// audio connection,
							// but it probably would be good to do.
							event.getJDA().getAudioManager(JDAHelper.getGuild()).setSendingHandler(player);

							// Start playback. This will only start after the
							// AudioConnection has completely connected.
							// NOTE: "completely connected" is not just joining
							// the
							// VoiceChannel. Think about when your Discord
							// client joins a VoiceChannel. You appear in the
							// channel
							// lobby immediately, but it takes a few
							// moments before you can start communicating.
							player.play();
							resetIdleCount();
						} catch (IOException e) {
							event.getChannel().sendMessage(
									"Could not load the file. Does it exist?  File name: " + audioFile.getName());
							e.printStackTrace();
						} catch (UnsupportedAudioFileException e) {
							event.getChannel()
									.sendMessage("Could not load file. It either isn't an audio file or isn't a"
											+ " recognized audio format.");
							e.printStackTrace();
						}
					} else if (player.isStarted() && player.isStopped()) // If
																			// it
																			// did
																			// exist,
																			// has
																			// it
																			// been
																			// stop()'d
																			// before?
					{
						event.getChannel()
								.sendMessage("The player has been stopped. To start playback, please use 'restart'");
						return;
					} else // It exists and hasn't been stopped before, so play.
							// Note:
							// if it was already playing, this will have no
							// effect.
					{
						player.play();
						resetIdleCount();
					}

				}

				// You can't pause, stop or restart before a player has even
				// been
				// created!
				if (player == null
						&& (message.equals("!pause") || message.equals("!stop") || message.equals("!restart"))) {
					event.getChannel().sendMessage("You need to 'play' before you can preform that command.");
					return;
				}

				if (player != null) {
					if (message.equals("!pause"))
						player.pause();
					if (message.equals("!stop"))
						stopPlayer();
					if (message.equals("!restart"))
						player.restart();
				}
			} catch (PatternSyntaxException e) {
				System.out.println("[" + event.getChannel().getName() + "]" + "[" + event.getAuthor().getUsername()
						+ "] " + " has sent an image/file or some other form of media or unsupported text");
			}
		}
	}

	public void stopPlayer() {
		if (player != null) {
			player.stop();
			resetIdleCount();
		}
	}

	private void resetIdleCount() {
		currentIdleTime = 0f;
	}

	public void blockVoiceChannel(String channelName) {
		for (String string : blockedChannels) {
			if (string.equals(channelName)) {
				return;
			}
		}
		blockedChannels.add(channelName);
	}

	public void unblockVoiceChannel(String channelName) {
		blockedChannels.remove(channelName);
	}

	public void unblockAllVoiceChannels() {
		blockedChannels.clear();
	}

}
