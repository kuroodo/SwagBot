package kuroodo.discordbot.listeners;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import kuroodo.discordbot.Init;
import kuroodo.discordbot.botcommands.BotCommandBlock;
import kuroodo.discordbot.botcommands.BotCommandSay;
import kuroodo.discordbot.botcommands.BotCommandSetGame;
import kuroodo.discordbot.botcommands.BotCommandUnblock;
import kuroodo.discordbot.client.handlers.BotCommandHandler;
import kuroodo.discordbot.entities.BotCommand;
import kuroodo.discordbot.entities.JDAListener;
import kuroodo.discordbot.helpers.JDAHelper;
import net.dv8tion.jda.events.message.priv.PrivateMessageReceivedEvent;

public class BotCommandListener extends JDAListener {
	private ArrayList<BotCommand> commandUpdateQueue;

	public BotCommandListener() {
		commandUpdateQueue = new ArrayList<>();
		registerCommands();
	}

	@Override
	public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
		super.onPrivateMessageReceived(event);

		String commandName = JDAHelper.splitString(event.getMessage().getRawContent())[0].toLowerCase();

		try {
			if (event.getMessage().getRawContent().substring(0, 1).equals("!")
					&& BotCommandHandler.isContainsCommand(commandName)) {
				
				if (Init.SUPER_USERS.contains(event.getAuthor())) {
					startupCommand(event);
				}
			}
		} catch (StringIndexOutOfBoundsException e) {
			System.out.println("[PRIVATE MESSAGE]" + "[" + event.getAuthor().getUsername() + "] "
					+ " has sent an image/file or some other form of media or unsupported text");
		}

	}

	// Create new instance of command based on class
	private void startupCommand(PrivateMessageReceivedEvent event) {
		BotCommand command;
		try {
			command = (BotCommand) BotCommandHandler
					.getCommand(JDAHelper.splitString(event.getMessage().getContent())[0].toLowerCase()).newInstance();

			// System.out.println("CommandListener: " +
			// ChatHelper.splitString(event.getMessage().getRawContent())[1]);

			// Execute command and give it any parameters specified by user
			command.executeCommand(JDAHelper.splitString(event.getMessage().getRawContent())[1], event);
			if (command.shouldUpdate()) {
				commandUpdateQueue.add(command);
			}

		} catch (InstantiationException | IllegalAccessException e) {
			System.out.println("Error getting command class!");
			e.printStackTrace();
		}
	}

	public void update(float delta) {
		try {
			for (BotCommand command : commandUpdateQueue) {
				// Check if we should continue calling update on command
				if (command.shouldUpdate()) {
					command.update(delta);
				} else {
					commandUpdateQueue.remove(command);
				}
			}
		} catch (ConcurrentModificationException e) {
			return;
		}
	}

	// Perhaps there's a better way than using classes directly
	private void registerCommands() {
		BotCommandHandler.registerCommand("!say", BotCommandSay.class);
		BotCommandHandler.registerCommand("!block", BotCommandBlock.class);
		BotCommandHandler.registerCommand("!unblock", BotCommandUnblock.class);
		BotCommandHandler.registerCommand("!setgame", BotCommandSetGame.class);
	}
}
