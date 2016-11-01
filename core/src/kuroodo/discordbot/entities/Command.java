package kuroodo.discordbot.entities;

import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.events.message.priv.PrivateMessageReceivedEvent;

public interface Command {

	void update(float delta);

	void executeCommand(String commandParams, GuildMessageReceivedEvent event);

	void executeCommand(String commandParams, PrivateMessageReceivedEvent event);

	String info();
}
