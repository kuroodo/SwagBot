package kuroodo.discordbot.entities;

import kuroodo.discordbot.Init;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public abstract class JDAListener extends ListenerAdapter {
	public String listenerName = "";

	public abstract void update(float delta);

	public void dispose() {
		Init.getJDA().removeEventListener(this);
	}
}
