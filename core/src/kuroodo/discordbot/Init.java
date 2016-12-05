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
import kuroodo.discordbot.listeners.BotCommandListener;
import kuroodo.discordbot.listeners.ChannelListener;
import kuroodo.discordbot.listeners.ChatCommandListener;
import kuroodo.discordbot.listeners.ChatListener;
import kuroodo.discordbot.listeners.ServerListener;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

public class Init extends ApplicationAdapter {

	public static final String SUPERUSER_PASSWORD = JSonReader.getSuperUserPassword(), VERSION = "1.9.5";
	// Users that have special bot commands and access
	public static final ArrayList<User> SUPER_USERS = new ArrayList<>();

	public static String gameMessage = "";

	private static JDA jda;

	private static ArrayList<JDAListener> listeners;

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
			jda = new JDABuilder(AccountType.BOT).setToken(BOTTOKEN).buildBlocking();
			botName = jda.getSelfUser().getName();
			System.out.println("Hello, I Am " + botName + " v" + VERSION);

			storeServerOwner();

			if (!JSonReader.getIsDevMode()) {
				gameMessage = "Type !help For Help (;";
				jda.getPresence().setGame(Game.of(gameMessage));
			} else {
				gameMessage = "Undergoing Testing";
				jda.getPresence().setGame(Game.of(gameMessage));
			}

			setupListeners();
			System.out.println("All Done!");
		} catch (InterruptedException | LoginException | RateLimitedException e) {
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
		serverOwner = JDAHelper.getGuild().getOwner().getUser();
		SUPER_USERS.add(serverOwner);
	}

	private static void setupListeners() {
		listeners.add(new ChatCommandListener());
		listeners.add(new BotCommandListener());
		listeners.add(new ChatListener());
		listeners.add(new ServerListener());
		listeners.add(new ChannelListener());

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

	public static ArrayList<JDAListener> getListeners() {
		return listeners;
	}

	@Override
	public void dispose() {

		System.out.println("Ending all gamesessions");
		GlobalGameManager.endAllGameListenerSession();

		if (jda != null) {
			System.out.println("Shutting Down JDA");
			jda.getPresence().setIdle(true);
			jda.getPresence().setGame(Game.of("Shutting Down..."));
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
