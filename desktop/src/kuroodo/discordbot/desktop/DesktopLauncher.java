package kuroodo.discordbot.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import kuroodo.discordbot.Init;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = Init.getBotName() + " " + Init.VERSION + " Bot Client";
		config.width = 270;
		config.height = 1;
		new LwjglApplication(new Init(), config);
	}
}

// TODO: Bot controller can force a block and only they can unblock it
// TODO: Add super users to json file

// Idea:
// TODO: Create a "BotSettings" class that will store custom settings
// Settings like the welcome message/channel and such
// Some settings should be read via json or xml