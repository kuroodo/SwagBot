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
// TODO: Add ability to stop message deletion with a command or something
// TODO: Add GetMember method in JDAHelper
// TODO: Notify user via PM if a game session was terminated and by who
// TODO: If a player in a gamesession loses their roll (or is missing) end the
// game session and notify all players why the session ended