package kuroodo.discordbot.client.handlers;

import java.util.Collection;
import java.util.HashMap;

@SuppressWarnings("rawtypes")
public class ChatCommandHandler {
	public static HashMap<String, Class> commands = new HashMap<>();

	public static void registerCommand(String commandName, Class commandClass) {
		commands.put(commandName, commandClass);
	}

	public static Collection<Class> getCommands() {
		return commands.values();

	}

	public static Class getCommand(String commandName) {
		return commands.get(commandName);
	}
	
	public static boolean isContainsCommand(String commandName){
		return commands.containsKey(commandName);
	}
}
