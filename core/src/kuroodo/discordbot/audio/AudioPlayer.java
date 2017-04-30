// ORIGINAL From lavaplayer JDA DEMO: https://github.com/sedmelluq/lavaplayer/tree/master/demo-jda/src/main/java/com/sedmelluq/discord/lavaplayer/demo/jda
// Incomplete class with rough implementation
// TODO: Finish implementation

package kuroodo.discordbot.audio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import kuroodo.discordbot.entities.JDAListener;
import kuroodo.discordbot.helpers.JDAHelper;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class AudioPlayer extends JDAListener {

	private final AudioPlayerManager playerManager;
	private final Map<Long, GuildMusicManager> musicManagers;
	private final ArrayList<VoiceChannel> blockedChannels = new ArrayList<>();

	private boolean connected = false;
	private final float maxIdleTime = 10f;
	private float currentIdleTime = 0f;

	public AudioPlayer() {
		listenerName = "AudioPlayer";
		this.musicManagers = new HashMap<>();

		this.playerManager = new DefaultAudioPlayerManager();
		AudioSourceManagers.registerRemoteSources(playerManager);
		AudioSourceManagers.registerLocalSource(playerManager);
	}

	private synchronized GuildMusicManager getGuildAudioPlayer(Guild guild) {
		long guildId = Long.parseLong(guild.getId());
		GuildMusicManager musicManager = musicManagers.get(guildId);

		if (musicManager == null) {
			musicManager = new GuildMusicManager(playerManager);
			musicManagers.put(guildId, musicManager);
		}

		guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());

		return musicManager;
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		String[] command = event.getMessage().getContent().split(" ", 2);
		Guild guild = event.getGuild();

		if (guild != null) {
			if ("!play".equals(command[0]) && command.length == 2) {
				setupPlay(event.getAuthor(), event.getTextChannel(), command, false);

			} else if ("!playrandom".equals(command[0]) && command.length == 2) {
				setupPlay(event.getAuthor(), event.getTextChannel(), command, true);

			} else if ("!skip".equals(command[0])) {
				skipTrack(event.getTextChannel());
			} else if ("!stop".equals(command[0])) {
				GuildMusicManager musicManager = getGuildAudioPlayer(event.getGuild());
				musicManager.player.stopTrack();
			} else if ("!pause".equals(command[0])) {
				GuildMusicManager musicManager = getGuildAudioPlayer(event.getGuild());
				musicManager.player.setPaused(true);
			} else if ("!resume".equals(command[0])) {
				GuildMusicManager musicManager = getGuildAudioPlayer(event.getGuild());
				musicManager.player.setPaused(false);
			} else if ("!trackinfo".equals(command[0])) {
				String message = "";
				String trackTime = "";

				GuildMusicManager musicManager = getGuildAudioPlayer(event.getGuild());
				message = message + musicManager.player.getPlayingTrack().getInfo().title;

				long milliseconds = Long.valueOf(musicManager.player.getPlayingTrack().getInfo().length);

				trackTime = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(milliseconds),
						TimeUnit.MILLISECONDS.toMinutes(milliseconds), TimeUnit.MILLISECONDS.toSeconds(milliseconds)
								- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds)));

				message = message + " (" + trackTime + ")";

				event.getChannel().sendMessage(message).queue();
			}

		}

		super.onMessageReceived(event);
	}

	private void setupPlay(User author, TextChannel textchannel, String[] command, boolean isRandomized) {
		VoiceChannel userChannel = JDAHelper.getUserVoiceChannel(author.getName());

		if (!isVoiceChannelBlocked(author)) {
			connected = true;
			loadAndPlay(textchannel, userChannel, command[1], isRandomized);
		} else {
			textchannel.sendMessage("Unable to join '" + userChannel.getName() + "'").queue();
		}
	}

	private boolean isVoiceChannelBlocked(User user) {
		VoiceChannel userChannel = JDAHelper.getUserVoiceChannel(user.getName());
		for (VoiceChannel channel : blockedChannels) {
			if (channel == userChannel) {
				return true;
			}
		}
		return false;
	}

	private void loadAndPlay(final TextChannel channel, final VoiceChannel userVoiceChannel, final String trackUrl,
			boolean isRandomized) {
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());

		playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
			@Override
			public void trackLoaded(AudioTrack track) {
				channel.sendMessage("Adding to queue " + track.getInfo().title).queue();

				play(channel.getGuild(), userVoiceChannel, musicManager, track);
			}

			@Override
			public void playlistLoaded(AudioPlaylist playlist) {
				// AudioTrack firstTrack = playlist.getSelectedTrack();
				//
				// if (firstTrack == null) {
				// firstTrack = playlist.getTracks().get(0);
				// }

				channel.sendMessage("Adding playlist " + playlist.getName()).queue();

				play(channel.getGuild(), userVoiceChannel, musicManager, playlist, isRandomized);
			}

			@Override
			public void noMatches() {
				channel.sendMessage("Nothing found by " + trackUrl).queue();
			}

			@Override
			public void loadFailed(FriendlyException exception) {
				channel.sendMessage("Could not play: " + exception.getMessage()).queue();
			}
		});
	}

	private void play(Guild guild, VoiceChannel userVoiceChannel, GuildMusicManager musicManager, AudioTrack track) {
		guild.getAudioManager().openAudioConnection(userVoiceChannel);
		// connectToFirstVoiceChannel();

		musicManager.scheduler.queue(track);
	}

	private void play(Guild guild, VoiceChannel userVoiceChannel, GuildMusicManager musicManager,
			AudioPlaylist playlist, boolean isRandomized) {
		guild.getAudioManager().openAudioConnection(userVoiceChannel);
		// connectToFirstVoiceChannel();

		if (isRandomized) {
			long seed = System.nanoTime();
			Collections.shuffle(playlist.getTracks(), new Random(seed));
		}

		for (AudioTrack track : playlist.getTracks()) {
			musicManager.scheduler.queue(track);
		}

	}

	private void skipTrack(TextChannel channel) {
		GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
		musicManager.scheduler.nextTrack();

		channel.sendMessage("Skipped to next track.").queue();
	}

	public void blockVoiceChannel(VoiceChannel channel) {
		blockedChannels.add(channel);
	}

	public void unblockVoiceChannel(VoiceChannel channel) {
		blockedChannels.remove(channel);
	}

	public void unblockAllVoiceChannels() {
		blockedChannels.clear();
	}

	@Override
	public void update(float delta) {

	}
}