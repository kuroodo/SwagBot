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

// TODO: Server owner can enabled a permanent voice channel block command
// (unblockable by others)
// TODO: Add super-users to json file