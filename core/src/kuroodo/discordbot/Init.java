package kuroodo.discordbot;

import java.util.ArrayList;

import javax.security.auth.login.LoginException;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import kuroodo.discordbot.client.handlers.GlobalGameManager;
import kuroodo.discordbot.entities.JDAListener;
import kuroodo.discordbot.helpers.ChatLogger;
import kuroodo.discordbot.helpers.JDAHelper;
import kuroodo.discordbot.helpers.JSonReader;
import kuroodo.discordbot.listeners.AudioPlayer;
import kuroodo.discordbot.listeners.BotCommandListener;
import kuroodo.discordbot.listeners.ChannelListener;
import kuroodo.discordbot.listeners.ChatCommandListener;
import kuroodo.discordbot.listeners.ChatListener;
import kuroodo.discordbot.listeners.ServerListener;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;
import net.dv8tion.jda.entities.User;

public class Init extends ApplicationAdapter {

	public static final String SUPERUSER_PASSWORD = JSonReader.getSuperUserPassword(), VERSION = "1.9.4";
	// Users that have special bot commands and access
	public static final ArrayList<User> SUPER_USERS = new ArrayList<>();

	private static JDA jda;

	private static ArrayList<JDAListener> listeners;

	private static AudioPlayer audioPlayer = new AudioPlayer();

	private static String botName = "";

	private static User serverOwner;

	@Override
	public void create() {
		listeners = new ArrayList<>();
		startJDA();
	}

	private static void startJDA() {
		System.out.println("starting");
		final String BOTTOKEN = JSonReader.getLogin();

		try {
			jda = new JDABuilder().setBotToken(BOTTOKEN).buildBlocking();
			botName = jda.getSelfInfo().getUsername();
			System.out.println("Hello, I Am " + botName + " v" + VERSION);

			storeServerOwner();

			if (!JSonReader.getIsDevMode()) {
				jda.getAccountManager().setGame("Type !help For Help (;");
			} else {
				jda.getAccountManager().setGame("Undergoing Testing");
			}

			setupListeners();
		} catch (InterruptedException | LoginException e) {
			System.out.println("ERROR: " + e.getMessage());
		}

	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(240, 240, 240, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (jda != null) {
			for (JDAListener listener : listeners) {
				listener.update(Gdx.graphics.getDeltaTime());
			}
		}
	}

	private static void storeServerOwner() {
		String ownerID = JDAHelper.getGuild().getOwnerId();

		for (User user : JDAHelper.getUsers()) {
			if (user.getId().equals(ownerID)) {
				serverOwner = user;
			}
		}
		SUPER_USERS.add(serverOwner);
	}

	private static void setupListeners() {
		listeners.add(new ChatCommandListener());
		listeners.add(new BotCommandListener());
		listeners.add(new ChatListener());
		listeners.add(new ServerListener());
		listeners.add(new ChannelListener());
		listeners.add(audioPlayer);

		for (JDAListener listener : listeners) {
			jda.addEventListener(listener);
		}
	}

	public static User getServerOwner() {
		return serverOwner;
	}

	public static JDA getJDA() {
		return jda;
	}

	public static String getBotName() {
		return botName;
	}

	// TODO: Find a better place for these methods
	public static void blockVoiceChannel(String channelName) {
		audioPlayer.blockVoiceChannel(channelName);
	}

	public static void unblockVoiceChannel(String channelName) {
		audioPlayer.unblockVoiceChannel(channelName);
	}

	public static void unblockAllVoiceChannels() {
		audioPlayer.unblockAllVoiceChannels();
	}

	public static ArrayList<JDAListener> getListeners() {
		return listeners;
	}

	@Override
	public void dispose() {

		System.out.println("Ending all gamesessions");
		GlobalGameManager.endAllGameListenerSession();

		if (jda != null) {
			System.out.println("Shutting Down JDA");
			jda.getAccountManager().setIdle(true);
			jda.getAccountManager().setGame("Shutting Down...");
			System.out.println("Disposing and clearing all listeners");
			for (JDAListener listener : listeners) {
				listener.dispose();
			}
			listeners.clear();
			jda.shutdown(true);
		}

		ChatLogger.closeLog();
		super.dispose();
	}
}
