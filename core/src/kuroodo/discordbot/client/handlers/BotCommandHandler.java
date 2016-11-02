package kuroodo.discordbot.client.handlers;

import java.util.Collection;
import java.util.HashMap;

@SuppressWarnings("rawtypes")
public class BotCommandHandler {

	public static HashMap<String, Class> botCommands = new HashMap<>();

	public static void registerCommand(String commandName, Class commandClass) {
		botCommands.put(commandName, commandClass);
	}

	public static Collection<Class> getCommands() {
		return botCommands.values();
	}

	public static Class getCommand(String commandName) {
		return botCommands.get(commandName);
	}
	
	public static boolean isContainsCommand(String commandName) {
		return botCommands.containsKey(commandName);
	}
}