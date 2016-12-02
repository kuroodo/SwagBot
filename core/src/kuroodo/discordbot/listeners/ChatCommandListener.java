package kuroodo.discordbot.listeners;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import kuroodo.discordbot.Init;
import kuroodo.discordbot.chatcommands.CommandAvatar;
import kuroodo.discordbot.chatcommands.CommandFlipCoin;
import kuroodo.discordbot.chatcommands.CommandHelp;
import kuroodo.discordbot.chatcommands.CommandMagicBall;
import kuroodo.discordbot.chatcommands.CommandPlayGame;
import kuroodo.discordbot.chatcommands.CommandPoke;
import kuroodo.discordbot.chatcommands.CommandRoast;
import kuroodo.discordbot.chatcommands.CommandRoulette;
import kuroodo.discordbot.chatcommands.CommandSlap;
import kuroodo.discordbot.chatcommands.CommandSpartanKick;
import kuroodo.discordbot.chatcommands.admin.CommandDeleteRole;
import kuroodo.discordbot.chatcommands.admin.CommandEditRoleColor;
import kuroodo.discordbot.chatcommands.admin.CommandGiveRole;
import kuroodo.discordbot.chatcommands.admin.CommandMakeRole;
import kuroodo.discordbot.chatcommands.admin.CommandRemoveRole;
import kuroodo.discordbot.chatcommands.moderator.CommandBan;
import kuroodo.discordbot.chatcommands.moderator.CommandClearChat;
import kuroodo.discordbot.chatcommands.moderator.CommandKick;
import kuroodo.discordbot.chatcommands.moderator.CommandTempBan;
import kuroodo.discordbot.client.handlers.ChatCommandHandler;
import kuroodo.discordbot.entities.ChatCommand;
import kuroodo.discordbot.entities.JDAListener;
import kuroodo.discordbot.helpers.JDAHelper;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class ChatCommandListener extends JDAListener {
	private ArrayList<ChatCommand> commandsToUpdateQueue;

	public ChatCommandListener() {
		commandsToUpdateQueue = new ArrayList<>();
		registerCommands();
	}

	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		super.onGuildMessageReceived(event);

		if (event.getAuthor() != null) {
			String commandName = JDAHelper.splitString(event.getMessage().getRawContent())[0].toLowerCase();

			try {
				if (event.getMessage().getRawContent().substring(0, 1).equals("!")
						&& ChatCommandHandler.isContainsCommand(commandName)) {
					startupCommand(event);

					// If user mentioned the bot at the beginning of message
				} else if (!event.getMessage().getMentionedUsers().isEmpty()
						&& event.getMessage().getRawContent().substring(0, 1).equals("@")
						&& event.getMessage().getMentionedUsers().get(0) == Init.getJDA().getSelfUser()) {
					startupCommand(event);
				}
			} catch (StringIndexOutOfBoundsException e) {
			}
		}
	}

	// Create new instance of command based on its class
	private void startupCommand(GuildMessageReceivedEvent event) {
		ChatCommand command;
		try {

			command = (ChatCommand) ChatCommandHandler
					.getCommand(JDAHelper.splitString(event.getMessage().getContent())[0].toLowerCase()).newInstance();

			// System.out.println("CommandListener: " +
			// ChatHelper.splitString(event.getMessage().getRawContent())[1]);

			// Execute command and give it any parameters specified by user
			command.executeCommand(JDAHelper.splitString(event.getMessage().getRawContent())[1], event);
			if (command.shouldUpdate()) {
				commandsToUpdateQueue.add(command);
			}

		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			System.out.println("Error getting command class! Application Will Not Crash");
		}
	}

	public void update(float delta) {
		try {
			for (ChatCommand command : commandsToUpdateQueue) {
				// Check if we should continue calling update on command
				if (command.shouldUpdate()) {
					command.update(delta);
				} else {
					commandsToUpdateQueue.remove(command);
				}
			}
		} catch (ConcurrentModificationException e) {
			return;
		}
	}

	// Perhaps there's a better way than using classes directly
	private void registerCommands() {
		// User Commands
		ChatCommandHandler.registerCommand("!avatar", CommandAvatar.class);
		ChatCommandHandler.registerCommand("!flipcoin", CommandFlipCoin.class);
		ChatCommandHandler.registerCommand("!magicball", CommandMagicBall.class);
		ChatCommandHandler.registerCommand("@" + Init.getJDA().getSelfUser().getName().toLowerCase(),
				CommandMagicBall.class);
		ChatCommandHandler.registerCommand("!poke", CommandPoke.class);
		ChatCommandHandler.registerCommand("!roast", CommandRoast.class);
		ChatCommandHandler.registerCommand("!roulette", CommandRoulette.class);
		ChatCommandHandler.registerCommand("!spartankick", CommandSpartanKick.class);
		ChatCommandHandler.registerCommand("!slap", CommandSlap.class);
		ChatCommandHandler.registerCommand("!playgame", CommandPlayGame.class);
		ChatCommandHandler.registerCommand("!help", CommandHelp.class);
		ChatCommandHandler.registerCommand("!info", CommandHelp.class);

		// Moderator Commands
		ChatCommandHandler.registerCommand("!ban", CommandBan.class);
		ChatCommandHandler.registerCommand("!clearchat", CommandClearChat.class);
		ChatCommandHandler.registerCommand("!kick", CommandKick.class);
		ChatCommandHandler.registerCommand("!tempban", CommandTempBan.class);

		// Admin Commands
		ChatCommandHandler.registerCommand("!deleterole", CommandDeleteRole.class);
		ChatCommandHandler.registerCommand("!editrolecolor", CommandEditRoleColor.class);
		ChatCommandHandler.registerCommand("!giverole", CommandGiveRole.class);
		ChatCommandHandler.registerCommand("!removerole", CommandRemoveRole.class);
		ChatCommandHandler.registerCommand("!makerole", CommandMakeRole.class);
	}
}
